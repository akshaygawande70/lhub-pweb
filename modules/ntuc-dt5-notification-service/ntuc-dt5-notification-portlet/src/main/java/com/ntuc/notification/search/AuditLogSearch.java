package com.ntuc.notification.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.util.ParamUtil;
import com.ntuc.notification.audit.api.util.BlobUtil;
import com.ntuc.notification.model.AuditLog;
import com.ntuc.notification.service.AuditLogLocalService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

public class AuditLogSearch extends SearchContainer<AuditLog> {

    public static final String EMPTY_RESULTS_MESSAGE = "no-audit-logs-were-found";

    public AuditLogSearch(
            PortletRequest portletRequest,
            PortletURL iteratorURL,
            AuditLogLocalService auditLogLocalService) {

        super(
                portletRequest, null, null,
                DEFAULT_CUR_PARAM, DEFAULT_DELTA,
                iteratorURL, null, EMPTY_RESULTS_MESSAGE);

        if (auditLogLocalService == null) {
            setResults(Collections.emptyList());
            setTotal(0);
            return;
        }

        String keywords = ParamUtil.getString(portletRequest, "keywords");
        String stepFilter = ParamUtil.getString(portletRequest, "actionFilter"); // UI param name kept as-is

        // IMPORTANT: We filter first, then paginate, so total matches UI.
        // If this table can grow very large, move this filtering into a service method with DB-backed search.
        List<AuditLog> allLogs = auditLogLocalService.getAuditLogs(-1, -1);

        List<AuditLog> filtered = allLogs.stream()
                .filter(log -> {
                    if (log == null) {
                        return false;
                    }

                    boolean matchesStep =
                            stepFilter == null || stepFilter.trim().isEmpty()
                                    || (log.getStep() != null && stepFilter.equalsIgnoreCase(log.getStep()));

                    if (!matchesStep) {
                        return false;
                    }

                    if (keywords == null || keywords.trim().isEmpty()) {
                        return true;
                    }

                    String kw = keywords.trim().toLowerCase();

                    // Search in detailsJson blob (your schema)
                    String json = BlobUtil.toString(log.getDetailsJson());
                    return json != null && json.toLowerCase().contains(kw);
                })
                .collect(Collectors.toList());

        setTotal(filtered.size());

        int start = getStart();
        int end = getEnd();

        if (start < 0) start = 0;
        if (end < 0) end = 0;
        if (start > filtered.size()) start = filtered.size();
        if (end > filtered.size()) end = filtered.size();
        if (end < start) end = start;

        setResults(filtered.subList(start, end));
    }
}
