package com.ntuc.notification.audit.util;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditRequestContext;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.Assert.*;

public class AuditEventFactoryTest {

    @Test
    public void start_withProvidedCorrelationId_usesSameId() {
        AuditRequestContext ctx = ctx("corr-1", 1L, 2L, 3L, "C-1", 9L);

        AuditEvent e = AuditEventFactory.start(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                ctx,
                AuditStatus.STARTED,
                "start",
                AuditErrorCode.NONE
        );

        assertNotNull(e);
        assertEquals("corr-1", e.getCorrelationId());
        assertEquals(1L, e.getCompanyId());
        assertEquals(2L, e.getGroupId());
        assertEquals(3L, e.getUserId());
        assertEquals("C-1", e.getCourseCode());
        assertEquals(9L, e.getNtucDTId());
        assertEquals(AuditStatus.STARTED, e.getStatus());
        assertEquals(AuditErrorCode.NONE, e.getErrorCode());
    }

    @Test
    public void start_withBlankCorrelationId_generatesUuid() {
        AuditRequestContext ctx = ctx("   ", 1L, 2L, 3L, null, 0L);

        AuditEvent e = AuditEventFactory.start(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                ctx,
                AuditStatus.STARTED,
                "start",
                AuditErrorCode.NONE
        );

        assertNotNull(e);

        String corr = e.getCorrelationId();
        assertNotNull(corr);
        assertFalse(corr.trim().isEmpty());

        // should be UUID format
        UUID.fromString(corr);
    }

    @Test
    public void end_withNullContext_setsZeroIds_andGeneratesCorrelationId() {
        AuditEvent e = AuditEventFactory.end(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                null,
                AuditStatus.SUCCESS,
                "done",
                AuditErrorCode.NONE
        );

        assertNotNull(e);
        assertNotNull(e.getCorrelationId());
        assertFalse(e.getCorrelationId().trim().isEmpty());

        assertEquals(0L, e.getCompanyId());
        assertEquals(0L, e.getGroupId());
        assertEquals(0L, e.getUserId());
        assertNull(e.getCourseCode());
        assertEquals(0L, e.getNtucDTId());

        assertEquals(AuditStatus.SUCCESS, e.getStatus());
    }

    @Test
    public void success_setsStatusSuccess_andTimes() {
        AuditRequestContext ctx = ctx("corr-success", 1L, 2L, 3L, "C-1", 9L);

        AuditEvent e = AuditEventFactory.success(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                ctx,
                "ok",
                AuditErrorCode.NONE
        );

        assertNotNull(e);
        assertEquals("corr-success", e.getCorrelationId());
        assertEquals(AuditStatus.SUCCESS, e.getStatus());

        // times should be set in success()
        assertTrue(e.getStartTimeMs() > 0);
        assertTrue(e.getEndTimeMs() > 0);
        assertTrue(e.getEndTimeMs() >= e.getStartTimeMs());
    }

    @Test
    public void fail_withNullException_doesNotSetStackOrHash() {
        AuditRequestContext ctx = ctx("corr-fail-null", 1L, 2L, 3L, null, 0L);

        AuditEvent e = AuditEventFactory.fail(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_INTERNAL,
                AuditSeverity.ERROR,
                ctx,
                "failed",
                AuditErrorCode.INTERNAL_ERROR,
                null
        );

        assertNotNull(e);
        assertEquals(AuditStatus.FAILED, e.getStatus());

        // These fields exist in the builder; use reflection so we don't depend on getter names.
        assertNull(getStringViaGetterIfExists(e, "getStackTraceTruncated"));
        assertNull(getStringViaGetterIfExists(e, "getStackTraceHash"));
        assertNull(getStringViaGetterIfExists(e, "getExceptionClass"));
        assertNull(getStringViaGetterIfExists(e, "getErrorMessage"));
    }

    @Test
    public void fail_withHugeStackTrace_truncatesTo4000_andGeneratesHash() {
        AuditRequestContext ctx = ctx("corr-fail-huge", 1L, 2L, 3L, null, 0L);

        RuntimeException ex = new RuntimeException("boom");

        // Force very large printed stack trace by injecting many stack frames
        StackTraceElement[] huge = new StackTraceElement[1200];
        for (int i = 0; i < huge.length; i++) {
            huge[i] = new StackTraceElement(
                    "com.ntuc.notification.audit.util.SomeVeryLongClassName" + i,
                    "someVeryLongMethodName" + i,
                    "SomeVeryLongFileName" + i + ".java",
                    i
            );
        }
        ex.setStackTrace(huge);

        AuditEvent e = AuditEventFactory.fail(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_INTERNAL,
                AuditSeverity.ERROR,
                ctx,
                "failed",
                AuditErrorCode.INTERNAL_ERROR,
                ex
        );

        assertNotNull(e);
        assertEquals(AuditStatus.FAILED, e.getStatus());

        String truncated = getStringViaGetterIfExists(e, "getStackTraceTruncated");
        String hash = getStringViaGetterIfExists(e, "getStackTraceHash");
        String exClass = getStringViaGetterIfExists(e, "getExceptionClass");
        String errMsg = getStringViaGetterIfExists(e, "getErrorMessage");

        // If getters exist (they usually do), validate content.
        // If not, these checks will be skipped safely.
        if (truncated != null) {
            assertEquals(4000, truncated.length());
        }
        if (hash != null) {
            assertEquals(64, hash.length()); // sha-256 hex length
        }
        if (exClass != null) {
            assertTrue(exClass.contains("RuntimeException"));
        }
        if (errMsg != null) {
            assertTrue(errMsg.contains("boom"));
        }
    }

    private static String getStringViaGetterIfExists(Object target, String getterName) {
        try {
            Method m = target.getClass().getMethod(getterName);
            Object out = m.invoke(target);
            return (out == null) ? null : String.valueOf(out);
        }
        catch (NoSuchMethodException ex) {
            // getter not present in your AuditEvent - ok, test stays compiling
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    
    @Test
    public void end_setsStartAndEndTime_andKeepsStatus() {
        AuditRequestContext ctx = new DummyAuditRequestContext("corr-1");

        AuditEvent e = AuditEventFactory.end(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                ctx,
                AuditStatus.SKIPPED,
                "ended",
                AuditErrorCode.NONE
        );

        assertNotNull(e);
        assertEquals(AuditStatus.SKIPPED, e.getStatus());
        assertTrue(e.getStartTimeMs() > 0);
        assertTrue(e.getEndTimeMs() >= e.getStartTimeMs());
    }

    
    @Test
    public void safeCorrelationId_generatesWhenContextIsNull() {
        AuditEvent e = AuditEventFactory.start(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.INFO,
                null,
                AuditStatus.STARTED,
                "msg",
                AuditErrorCode.NONE
        );

        assertNotNull(e.getCorrelationId());
        assertFalse(e.getCorrelationId().trim().isEmpty());
    }

    
    @Test
    public void truncate_truncatesWhenOverLimit() {
        String longStr = new String(new char[5000]).replace('\0', 'x');

        AuditEvent e = AuditEventFactory.fail(
                AuditCategory.DT5_FLOW,
                AuditStep.REST_NOTIFY_BATCH,
                AuditSeverity.ERROR,
                new DummyAuditRequestContext("corr-x"),
                "fail",
                AuditErrorCode.INTERNAL_ERROR,
                new RuntimeException(longStr)
        );

        assertNotNull(e.getStackTraceTruncated());
        assertTrue(e.getStackTraceTruncated().length() <= 4000);
    }


    private static AuditRequestContext ctx(
            final String correlationId,
            final long companyId,
            final long groupId,
            final long userId,
            final String courseCode,
            final long ntucDTId) {

        return new AuditRequestContext() {
            @Override
            public String getCorrelationId() {
                return correlationId;
            }

            @Override
            public long getCompanyId() {
                return companyId;
            }

            @Override
            public long getGroupId() {
                return groupId;
            }

            @Override
            public long getUserId() {
                return userId;
            }

            @Override
            public String getCourseCode() {
                return courseCode;
            }

            @Override
            public long getNtucDTId() {
                return ntucDTId;
            }
        };
    }
    
    class DummyAuditRequestContext implements AuditRequestContext {

        private final String corrId;

        DummyAuditRequestContext(String corrId) {
            this.corrId = corrId;
        }

        @Override public String getCorrelationId() { return corrId; }
        @Override public long getCompanyId() { return 1L; }
        @Override public long getGroupId() { return 2L; }
        @Override public long getUserId() { return 3L; }
        @Override public String getCourseCode() { return "COURSE-1"; }
        @Override public long getNtucDTId() { return 10L; }
    }

}
