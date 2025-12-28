package com.ntuc.notification.auditlog.internal.service;

import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.ntuc.notification.audit.util.AuditLogQueryValidator;
import com.ntuc.notification.auditlog.api.AuditLogQueryRequest;
import com.ntuc.notification.auditlog.api.AuditLogQueryResult;
import com.ntuc.notification.auditlog.api.AuditLogQueryService;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component(service = AuditLogQueryService.class)
public class AuditLogQueryServiceImpl implements AuditLogQueryService {

    @Reference
    private AuditLogLocalService auditLogLocalService;

    @Override
    public AuditLogQueryResult search(AuditLogQueryRequest request) {
        AuditLogQueryValidator.Result norm =
            AuditLogQueryValidator.normalize(request.getFromYmd(), request.getToYmd(), request.getQuery());

        long startMs = atStartOfDayMs(norm.getFrom());
        long endMsExclusive = atStartOfDayMs(norm.getTo().plusDays(1));

        DynamicQuery dq = auditLogLocalService.dynamicQuery();

        dq.add(PropertyFactoryUtil.forName("startTimeMs").ge(startMs));
        dq.add(PropertyFactoryUtil.forName("startTimeMs").lt(endMsExclusive));

        addEqIfPresent(dq, "category", request.getCategory());
        addEqIfPresent(dq, "status", request.getStatus());
        addEqIfPresent(dq, "severity", request.getSeverity());
        addEqIfPresent(dq, "step", request.getStep());

        String q = norm.getQuery();
        if (Validator.isNotNull(q)) {
            String like = "%" + q + "%";

            Disjunction or = RestrictionsFactoryUtil.disjunction();
            or.add(PropertyFactoryUtil.forName("courseCode").like(like));
            or.add(PropertyFactoryUtil.forName("correlationId").like(like));
            or.add(PropertyFactoryUtil.forName("jobRunId").like(like));
            or.add(PropertyFactoryUtil.forName("requestId").like(like));
            or.add(PropertyFactoryUtil.forName("eventId").like(like));
            or.add(PropertyFactoryUtil.forName("errorCode").like(like));
            or.add(PropertyFactoryUtil.forName("exceptionClass").like(like));
            or.add(PropertyFactoryUtil.forName("message").like(like));

            Long asLong = tryParseLong(q);
            if (asLong != null) {
                or.add(PropertyFactoryUtil.forName("ntucDTId").eq(asLong));
                or.add(PropertyFactoryUtil.forName("auditLogId").eq(asLong));
            }

            dq.add(or);
        }

        dq.addOrder(OrderFactoryUtil.desc("startTimeMs"));

        int start = Math.max(0, request.getStart());
        int end = Math.max(start, request.getEnd());

        // ServiceBuilder returns List<?> (often List<Object>) → map safely.
        List<?> rawRows = auditLogLocalService.dynamicQuery(dq, start, end);
        List<AuditLog> rows = toAuditLogs(rawRows);

        int total = (int) auditLogLocalService.dynamicQueryCount(dq);

        FilterValues filters = loadDistinctFilters(startMs, endMsExclusive);

        return new AuditLogQueryResult(
            rows,
            total,
            filters.categories,
            filters.statuses,
            filters.severities,
            filters.steps
        );
    }

    private void addEqIfPresent(DynamicQuery dq, String field, String v) {
        if (Validator.isNotNull(v)) {
            dq.add(PropertyFactoryUtil.forName(field).eq(v.trim()));
        }
    }

    private long atStartOfDayMs(LocalDate d) {
        return d.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Long tryParseLong(String s) {
        try {
            return Long.valueOf(s.trim());
        } catch (Exception ignore) {
            return null;
        }
    }

    private FilterValues loadDistinctFilters(long startMs, long endMsExclusive) {
        DynamicQuery dq = auditLogLocalService.dynamicQuery();
        dq.add(PropertyFactoryUtil.forName("startTimeMs").ge(startMs));
        dq.add(PropertyFactoryUtil.forName("startTimeMs").lt(endMsExclusive));
        dq.addOrder(OrderFactoryUtil.desc("startTimeMs"));

        List<?> raw = auditLogLocalService.dynamicQuery(dq, 0, 2000);
        List<AuditLog> rows = toAuditLogs(raw);

        Set<String> categories = new LinkedHashSet<>();
        Set<String> statuses = new LinkedHashSet<>();
        Set<String> severities = new LinkedHashSet<>();
        Set<String> steps = new LinkedHashSet<>();

        for (AuditLog al : rows) {
            addIfPresent(categories, al.getCategory());
            addIfPresent(statuses, al.getStatus());
            addIfPresent(severities, al.getSeverity());
            addIfPresent(steps, al.getStep());
        }

        return new FilterValues(
            new ArrayList<>(categories),
            new ArrayList<>(statuses),
            new ArrayList<>(severities),
            new ArrayList<>(steps)
        );
    }

    private void addIfPresent(Set<String> set, String v) {
        if (Validator.isNotNull(v)) {
            set.add(v);
        }
    }

    /**
     * ServiceBuilder dynamicQuery commonly returns List<?> / List<Object>.
     * Casting List<Object> → List<AuditLog> is illegal in Java generics.
     *
     * Convert element-by-element with type checks for OSGi safety.
     */
    private List<AuditLog> toAuditLogs(List<?> raw) {
        if (raw == null || raw.isEmpty()) {
            return new ArrayList<>();
        }

        List<AuditLog> out = new ArrayList<>(raw.size());

        for (Object o : raw) {
            if (o instanceof AuditLog) {
                out.add((AuditLog) o);
            } else if (o != null) {
                // If this ever happens, something is wrong with the query projection.
                // We intentionally drop unknown types to keep the portlet stable.
                // (If you want hard-fail, throw IllegalStateException here.)
            }
        }

        return out;
    }

    private static final class FilterValues {
        private final List<String> categories;
        private final List<String> statuses;
        private final List<String> severities;
        private final List<String> steps;

        private FilterValues(
            List<String> categories,
            List<String> statuses,
            List<String> severities,
            List<String> steps
        ) {
            this.categories = categories;
            this.statuses = statuses;
            this.severities = severities;
            this.steps = steps;
        }
    }
}
