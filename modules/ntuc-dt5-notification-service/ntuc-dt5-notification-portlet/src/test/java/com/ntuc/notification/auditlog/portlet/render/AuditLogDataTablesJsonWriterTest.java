package com.ntuc.notification.auditlog.portlet.render;

import com.ntuc.notification.audit.api.dto.AuditLogRowDto;
import com.ntuc.notification.audit.api.dto.AuditLogSearchResult;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuditLogDataTablesJsonWriterTest {

    @Test
    public void toJson_nullResult_usesDrawFallback() {
        String json = AuditLogDataTablesJsonWriter.toJson(null, 7);

        assertTrue(json.contains("\"draw\":7"));
        assertTrue(json.contains("\"recordsTotal\":0"));
        assertTrue(json.contains("\"recordsFiltered\":0"));
        assertTrue(json.contains("\"data\":[]"));
    }

    @Test
    public void toJson_resultDrawZero_usesDrawFallback() {
        AuditLogSearchResult r = new AuditLogSearchResult();
        r.setDraw(0);
        r.setRecordsTotal(10);
        r.setRecordsFiltered(5);

        String json = AuditLogDataTablesJsonWriter.toJson(r, 9);

        assertTrue(json.contains("\"draw\":9"));
        assertTrue(json.contains("\"recordsTotal\":10"));
        assertTrue(json.contains("\"recordsFiltered\":5"));
        assertTrue(json.contains("\"data\":[]"));
    }

    @Test
    public void toErrorJson_escapesQuotesBackslashesAndControls() {
        String msg = "bad \"quote\" and slash \\ and \n \r \t \b \f and \u0001";
        String json = AuditLogDataTablesJsonWriter.toErrorJson(3, msg);

        assertTrue(json.contains("\"draw\":3"));
        assertTrue(json.contains("\"error\":\""));

        assertTrue(json.contains("\\\"quote\\\""));
        assertTrue(json.contains("\\\\"));
        assertTrue(json.contains("\\n"));
        assertTrue(json.contains("\\r"));
        assertTrue(json.contains("\\t"));
        assertTrue(json.contains("\\b"));
        assertTrue(json.contains("\\f"));
        assertTrue(json.contains("\\u0001"));
    }

    @Test
    public void toJson_writesRow_fieldsAndEnums_asStrings() {
        AuditLogRowDto row = new AuditLogRowDto();
        row.setAuditId(123L);
        row.setStartTimeMs(111L);
        row.setEndTimeMs(0L);      // primitive: cannot be null
        row.setDurationMs(5L);
        row.setSeverity(AuditSeverity.ERROR);
        row.setStatus(AuditStatus.FAILED);
        row.setCategory(AuditCategory.DT5_FLOW);
        row.setStep(AuditStep.CLS_FETCH_CRITICAL_START);
        row.setMessage("oops");
        row.setCourseCode("C1");
        row.setNtucDTId(456L);
        row.setCorrelationId("cid");
        row.setJobRunId("job");
        row.setErrorCode("E1");
        row.setErrorMessage("msg");
        row.setExceptionClass("java.lang.RuntimeException");

        AuditLogSearchResult r = new AuditLogSearchResult();
        r.setDraw(1);
        r.setRecordsTotal(1);
        r.setRecordsFiltered(1);
        r.addRow(row);

        String json = AuditLogDataTablesJsonWriter.toJson(r, 0);

        assertTrue(json.contains("\"draw\":1"));
        assertTrue(json.contains("\"auditId\":123"));
        assertTrue(json.contains("\"startTimeMs\":111"));
        assertTrue(json.contains("\"endTimeMs\":0"));
        assertTrue(json.contains("\"severity\":\"ERROR\""));
        assertTrue(json.contains("\"ntucDTId\":456"));
    }
}
