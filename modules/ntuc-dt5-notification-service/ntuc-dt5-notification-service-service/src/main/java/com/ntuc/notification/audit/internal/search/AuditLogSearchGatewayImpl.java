package com.ntuc.notification.audit.internal.search;

import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;

import com.ntuc.notification.audit.api.dto.AuditLogRowDto;
import com.ntuc.notification.audit.api.dto.AuditLogSearchRequest;
import com.ntuc.notification.audit.api.dto.AuditLogSearchResult;
import com.ntuc.notification.audit.api.spi.AuditLogSearchGateway;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * DB-backed server-side search for AuditLog DataTables.
 *
 * Non-negotiables:
 * - Clamp paging inputs (avoid DOS / huge page).
 * - Only whitelisted sort columns.
 * - Baseline scope always includes companyId + groupId.
 */
@Component(service = AuditLogSearchGateway.class)
public class AuditLogSearchGatewayImpl implements AuditLogSearchGateway {

    static final int DEFAULT_PAGE_SIZE = 25;
    static final int MAX_PAGE_SIZE = 100;

    @Reference
    private AuditLogLocalService auditLogLocalService;

    @Override
    public AuditLogSearchResult search(AuditLogSearchRequest req) {
        AuditLogSearchRequest safe = AuditLogSearchRequestNormalizer.normalize(req);
        AuditLogSearchCriteria criteria = AuditLogSearchCriteriaBuilder.build(safe);

        AuditLogSearchResult result = new AuditLogSearchResult();
        result.setDraw(safe.getDraw());

        long total = auditLogLocalService.dynamicQueryCount(baseScopeQuery(safe, null));
        result.setRecordsTotal(total);

        long filtered = auditLogLocalService.dynamicQueryCount(baseScopeQuery(safe, criteria));
        result.setRecordsFiltered(filtered);

        DynamicQuery pageQuery = baseScopeQuery(safe, criteria);
        pageQuery.setLimit(safe.getStart(), safe.getStart() + safe.getLength());

        Order order = buildOrder(criteria);
        if (order != null) {
            pageQuery.addOrder(order);
        }

        List<?> raw = auditLogLocalService.dynamicQuery(pageQuery);
        List<AuditLog> rows = toAuditLogs(raw);

        result.addAll(mapRows(rows));
        return result;
    }

    private List<AuditLog> toAuditLogs(List<?> raw) {
        if (raw == null || raw.isEmpty()) {
            return Collections.emptyList();
        }

        List<AuditLog> out = new ArrayList<>(raw.size());
        for (Object o : raw) {
            if (o instanceof AuditLog) {
                out.add((AuditLog) o);
            }
        }
        return out;
    }

    private DynamicQuery baseScopeQuery(AuditLogSearchRequest req, AuditLogSearchCriteria criteria) {
        DynamicQuery dq = auditLogLocalService.dynamicQuery();

        Conjunction and = RestrictionsFactoryUtil.conjunction();

        and.add(PropertyFactoryUtil.forName("companyId").eq(req.getCompanyId()));
        and.add(PropertyFactoryUtil.forName("groupId").eq(req.getGroupId()));

        if (criteria != null) {
            applyCriteria(criteria, and);
        }

        dq.add(and);
        return dq;
    }

    private void applyCriteria(AuditLogSearchCriteria c, Conjunction and) {
        if (isNotBlank(c.getSeverity())) and.add(PropertyFactoryUtil.forName("severity").eq(c.getSeverity()));
        if (isNotBlank(c.getStatus())) and.add(PropertyFactoryUtil.forName("status").eq(c.getStatus()));
        if (isNotBlank(c.getCategory())) and.add(PropertyFactoryUtil.forName("category").eq(c.getCategory()));
        if (isNotBlank(c.getStep())) and.add(PropertyFactoryUtil.forName("step").eq(c.getStep()));

        // NEW: exact match on primary key
        if (c.getAuditLogIdEq() != null) {
            and.add(PropertyFactoryUtil.forName("auditLogId").eq(c.getAuditLogIdEq()));
        }

        if (isNotBlank(c.getCorrelationIdEq())) and.add(PropertyFactoryUtil.forName("correlationId").eq(c.getCorrelationIdEq()));
        if (isNotBlank(c.getJobRunIdEq())) and.add(PropertyFactoryUtil.forName("jobRunId").eq(c.getJobRunIdEq()));
        if (isNotBlank(c.getCourseCodeEq())) and.add(PropertyFactoryUtil.forName("courseCode").eq(c.getCourseCodeEq()));

        if (c.getNtucDTIdEq() != null) {
            and.add(PropertyFactoryUtil.forName("ntucDTId").eq(c.getNtucDTIdEq()));
        }

        if (c.getFromTimeMsGe() != null) and.add(PropertyFactoryUtil.forName("startTimeMs").ge(c.getFromTimeMsGe()));
        if (c.getToTimeMsLe() != null) and.add(PropertyFactoryUtil.forName("startTimeMs").le(c.getToTimeMsLe()));

        if (c.getGlobalSearch() != null && isNotBlank(c.getGlobalSearch().getLikeValue())) {
            AuditLogSearchCriteria.LikeAny g = c.getGlobalSearch();

            com.liferay.portal.kernel.dao.orm.Disjunction or = RestrictionsFactoryUtil.disjunction();
            for (String f : g.getFields()) {
                or.add(PropertyFactoryUtil.forName(f).like(g.getLikeValue()));
            }
            and.add(or);
        }
    }



    private Order buildOrder(AuditLogSearchCriteria criteria) {
        if (criteria == null) {
            return OrderFactoryUtil.desc("startTimeMs");
        }

        String col;
        switch (criteria.getSortColumn()) {
            case TIME: col = "startTimeMs"; break;
            case SEVERITY: col = "severity"; break;
            case STATUS: col = "status"; break;
            case CATEGORY: col = "category"; break;
            case STEP: col = "step"; break;
            case MESSAGE: col = "message"; break;
            case CORRELATION_ID: col = "correlationId"; break;
            case JOB_RUN_ID: col = "jobRunId"; break;
            case COURSE_CODE: col = "courseCode"; break;
            case NTUC_DT_ID: col = "ntucDTId"; break; // NEW
            default: col = "startTimeMs";
        }

        return (criteria.getSortDir() == AuditLogSearchRequest.SortDir.ASC)
            ? OrderFactoryUtil.asc(col)
            : OrderFactoryUtil.desc(col);
    }

    private List<AuditLogRowDto> mapRows(List<AuditLog> rows) {
        List<AuditLogRowDto> out = new ArrayList<>();
        if (rows == null) {
            return out;
        }

        for (AuditLog r : rows) {
            if (r == null) continue;

            AuditLogRowDto dto = new AuditLogRowDto();

            dto.setAuditId(r.getAuditLogId());
            dto.setStartTimeMs(r.getStartTimeMs());
            dto.setEndTimeMs(r.getEndTimeMs());
            dto.setDurationMs(r.getDurationMs());

            dto.setMessage(r.getMessage());
            dto.setCorrelationId(r.getCorrelationId());
            dto.setJobRunId(r.getJobRunId());
            dto.setCourseCode(r.getCourseCode());

            // NEW: return ntucDTId to grid
            dto.setNtucDTId(r.getNtucDTId());

            dto.setErrorCode(r.getErrorCode());
            dto.setErrorMessage(r.getErrorMessage());
            dto.setExceptionClass(r.getExceptionClass());

            dto.setSeverity(enumSafeSeverity(r.getSeverity()));
            dto.setStatus(enumSafeStatus(r.getStatus()));
            dto.setCategory(enumSafeCategory(r.getCategory()));
            dto.setStep(enumSafeStep(r.getStep()));

            out.add(dto);
        }

        return out;
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static com.ntuc.notification.audit.api.constants.AuditSeverity enumSafeSeverity(String s) {
        try { return (s == null) ? null : com.ntuc.notification.audit.api.constants.AuditSeverity.valueOf(s); }
        catch (Exception e) { return null; }
    }

    private static com.ntuc.notification.audit.api.constants.AuditStatus enumSafeStatus(String s) {
        try { return (s == null) ? null : com.ntuc.notification.audit.api.constants.AuditStatus.valueOf(s); }
        catch (Exception e) { return null; }
    }

    private static com.ntuc.notification.audit.api.constants.AuditCategory enumSafeCategory(String s) {
        try { return (s == null) ? null : com.ntuc.notification.audit.api.constants.AuditCategory.valueOf(s); }
        catch (Exception e) { return null; }
    }

    private static com.ntuc.notification.audit.api.constants.AuditStep enumSafeStep(String s) {
        try { return (s == null) ? null : com.ntuc.notification.audit.api.constants.AuditStep.valueOf(s); }
        catch (Exception e) { return null; }
    }
}
