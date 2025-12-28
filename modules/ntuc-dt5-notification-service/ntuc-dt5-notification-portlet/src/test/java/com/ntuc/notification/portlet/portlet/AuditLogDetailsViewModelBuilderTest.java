package com.ntuc.notification.portlet.portlet;

import static org.junit.Assert.assertEquals;

import com.ntuc.notification.portlet.portlet.AuditLogBlobExtractor;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsSource;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsViewModel;
import com.ntuc.notification.portlet.portlet.AuditLogDetailsViewModelBuilder;

import java.sql.Blob;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuditLogDetailsViewModelBuilderTest {

    @Mock
    private Blob detailsBlob;

    @Mock
    private Blob stackBlob;

    @Test
    public void build_mapsAllFields_andConvertsBlobs() {
        AuditLogDetailsSource src = new AuditLogDetailsSource() {
            @Override public long getAuditLogId() { return 123L; }
            @Override public long getNtucDTId() { return 123L; }
            @Override public String getCourseCode() { return "C001"; }
            @Override public String getSeverity() { return "ERROR"; }
            @Override public String getStatus() { return "FAILED"; }
            @Override public String getStep() { return "CLS_FETCH"; }
            @Override public String getCategory() { return "CLS_FAILURE"; }
            @Override public String getMessage() { return "Boom"; }
            @Override public String getErrorCode() { return "CLS_CONNECTION_FAILED"; }
            @Override public String getErrorMessage() { return "Timeout"; }
            @Override public String getExceptionClass() { return "java.net.SocketTimeoutException"; }
            @Override public String getStackTraceHash() { return "abc123"; }
            @Override public Blob getDetailsJson() { return detailsBlob; }
            @Override public Blob getStackTraceTruncated() { return stackBlob; }
        };

        AuditLogBlobExtractor be = blob -> {
            if (blob == detailsBlob) return "{\"k\":\"v\"}";
            if (blob == stackBlob) return "stack...";
            return "";
        };

        AuditLogDetailsViewModel vm = AuditLogDetailsViewModelBuilder.build(src, be);

        assertEquals(123L, vm.getAuditLogId());
        assertEquals("C001", vm.getCourseCode());
        assertEquals("ERROR", vm.getSeverity());
        assertEquals("FAILED", vm.getStatus());
        assertEquals("CLS_FETCH", vm.getStep());
        assertEquals("CLS_FAILURE", vm.getCategory());
        assertEquals("Boom", vm.getMessage());

        assertEquals("CLS_CONNECTION_FAILED", vm.getErrorCode());
        assertEquals("Timeout", vm.getErrorMessage());
        assertEquals("java.net.SocketTimeoutException", vm.getExceptionClass());
        assertEquals("abc123", vm.getStackTraceHash());

        assertEquals("{\"k\":\"v\"}", vm.getDetailsJson());
        assertEquals("stack...", vm.getStackTraceTruncated());
    }

    @Test
    public void build_nullSafe_whenFieldsAreNull() {
        AuditLogDetailsSource src = new AuditLogDetailsSource() {
            @Override public long getAuditLogId() { return 5L; }
            @Override public long getNtucDTId() { return 5L; }
            @Override public String getCourseCode() { return null; }
            @Override public String getSeverity() { return null; }
            @Override public String getStatus() { return null; }
            @Override public String getStep() { return null; }
            @Override public String getCategory() { return null; }
            @Override public String getMessage() { return null; }
            @Override public String getErrorCode() { return null; }
            @Override public String getErrorMessage() { return null; }
            @Override public String getExceptionClass() { return null; }
            @Override public String getStackTraceHash() { return null; }
            @Override public Blob getDetailsJson() { return null; }
            @Override public Blob getStackTraceTruncated() { return null; }
        };

        AuditLogDetailsViewModel vm = AuditLogDetailsViewModelBuilder.build(src, null);

        assertEquals(5L, vm.getAuditLogId());
        assertEquals("", vm.getCourseCode());
        assertEquals("", vm.getSeverity());
        assertEquals("", vm.getDetailsJson());
        assertEquals("", vm.getStackTraceTruncated());
    }

    @Test
    public void build_handlesNullSource() {
        AuditLogDetailsViewModel vm = AuditLogDetailsViewModelBuilder.build(null, null);
        assertEquals(0L, vm.getAuditLogId());
        assertEquals("", vm.getCourseCode());
    }
}
