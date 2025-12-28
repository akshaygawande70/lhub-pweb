package com.ntuc.notification.audit.internal;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.api.constants.AuditErrorCode;
import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.model.AuditLog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuditEventDbMapper}.
 *
 * <p><strong>Business purpose:</strong>
 * Protects audit persistence integrity by validating deterministic mapping behavior used for reporting,
 * troubleshooting, and alert deduplication.</p>
 *
 * <p><strong>Technical purpose:</strong>
 * Verifies field normalization, truncation, fingerprint hashing, and blob encoding using pure JUnit/Mockito
 * without requiring ServiceBuilder implementations at runtime.</p>
 *
 * @author @akshaygawande
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditEventDbMapperTest {

    private final AuditEventDbMapper mapper = new AuditEventDbMapper();

    @Mock
    private AuditLog row;

    @Captor
    private ArgumentCaptor<Blob> blobCaptor;

    // ---------------------------------------------------------------------
    // mapInto() - null safety
    // ---------------------------------------------------------------------

    @Test
    public void mapInto_nullInputs_noop() {
        mapper.mapInto(null, null);

        // Verifies no unexpected side-effects when row is null.
        mapper.mapInto(null, AuditEvent.builder()
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .build());
    }

    @Test
    public void mapInto_nullEvent_noop() {
        mapper.mapInto(row, null);
        verifyNoInteractions(row);
    }

    // ---------------------------------------------------------------------
    // mapInto() - basic field mapping using immutable builder
    // ---------------------------------------------------------------------

    @Test
    public void mapInto_basicFields_mappedSafely() {
        AuditEvent event = AuditEvent.builder()
                .startTimeMs(1000L)
                .endTimeMs(2000L)
                .correlationId("corr-1")
                .jobRunId("job-1")
                .eventId("evt-1")
                .requestId("req-1")
                .companyId(2001L)
                .groupId(3001L)
                .userId(4001L)
                .courseCode("COURSE-101")
                .ntucDTId(12345L)
                .severity(AuditSeverity.INFO)
                .status(AuditStatus.SUCCESS)
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .message("Test message")
                .build();

        mapper.mapInto(row, event);

        // Timestamps derived from startTimeMs
        verify(row).setCreateDate(any(Date.class));
        verify(row).setModifiedDate(any(Date.class));

        // Tenant / actor
        verify(row).setCompanyId(2001L);
        verify(row).setGroupId(3001L);
        verify(row).setUserId(4001L);

        // Business keys
        verify(row).setCourseCode("COURSE-101");
        verify(row).setNtucDTId(12345L);

        // Timeline
        verify(row).setStartTimeMs(1000L);
        verify(row).setEndTimeMs(2000L);
        verify(row).setDurationMs(1000L);

        // Correlation
        verify(row).setCorrelationId("corr-1");
        verify(row).setJobRunId("job-1");
        verify(row).setRequestId("req-1");
        verify(row).setEventId("evt-1");

        // Classification (string enums in DB)
        verify(row).setSeverity("INFO");
        verify(row).setStatus("SUCCESS");
        verify(row).setStep("EXECUTION");
        verify(row).setCategory("EXECUTION");

        // Summary
        verify(row).setMessage("Test message");

        // No email alert fingerprint for non-email category
        verify(row).setAlertFingerprint(isNull());
    }

    @Test
    public void mapInto_messageTruncatedToDbLimit_75Chars() {
        String longMsg =
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"; // > 75

        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .message(longMsg)
                .build();

        mapper.mapInto(row, event);

        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
        verify(row).setMessage(msgCaptor.capture());

        assertEquals(75, msgCaptor.getValue().length());
        assertEquals(longMsg.substring(0, 75), msgCaptor.getValue());
    }

    // ---------------------------------------------------------------------
    // Alert fingerprint hashing
    // ---------------------------------------------------------------------

    @Test
    public void mapInto_emailAlert_fingerprintHashedToFixedLen64() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("fingerprint", "company=1|template=A|error=X");

        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EMAIL_SENT)
                .category(AuditCategory.ALERT_EMAIL)
                .details(details)
                .build();

        mapper.mapInto(row, event);

        ArgumentCaptor<String> fpCaptor = ArgumentCaptor.forClass(String.class);
        verify(row).setAlertFingerprint(fpCaptor.capture());

        assertNotNull(fpCaptor.getValue());
        assertEquals(64, fpCaptor.getValue().length());
        assertTrue(fpCaptor.getValue().matches("^[0-9a-f]{64}$"));
    }

    @Test
    public void mapInto_emailAlert_backwardCompat_dedupeFingerprintHashed() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("dedupeFingerprint", "legacy|fp|value");

        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EMAIL_SUPPRESSED)
                .category(AuditCategory.ALERT_EMAIL)
                .details(details)
                .build();

        mapper.mapInto(row, event);

        ArgumentCaptor<String> fpCaptor = ArgumentCaptor.forClass(String.class);
        verify(row).setAlertFingerprint(fpCaptor.capture());

        assertNotNull(fpCaptor.getValue());
        assertEquals(64, fpCaptor.getValue().length());
        assertTrue(fpCaptor.getValue().matches("^[0-9a-f]{64}$"));
    }

    @Test
    public void mapInto_nonEmailAlert_fingerprintIgnoredEvenIfPresent() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("fingerprint", "should-be-ignored");

        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .details(details)
                .build();

        mapper.mapInto(row, event);

        verify(row).setAlertFingerprint(isNull());
    }

    @Test
    public void mapInto_emailCategory_butNonEmailStep_fingerprintNull() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("fingerprint", "should-not-hash");

        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EXECUTION) // not an EMAIL_* outcome step
                .category(AuditCategory.ALERT_EMAIL)
                .details(details)
                .build();

        mapper.mapInto(row, event);

        verify(row).setAlertFingerprint(isNull());
    }

    // ---------------------------------------------------------------------
    // Details JSON serialization
    // ---------------------------------------------------------------------

    @Test
    public void toDetailsJson_empty_returnsEmptyObject() {
        assertEquals("{}", AuditEventDbMapper.toDetailsJson(null));
        assertEquals("{}", AuditEventDbMapper.toDetailsJson(new HashMap<String, String>()));
    }

    @Test
    public void toDetailsJson_serializesAndEscapes() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("key", "line1\nline2\t\"q\"\\");
        details.put("simple", "value");

        String json = AuditEventDbMapper.toDetailsJson(details);

        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
        assertTrue(json.contains("\\n"));
        assertTrue(json.contains("\\t"));
        assertTrue(json.contains("\\\""));
        assertTrue(json.contains("\\\\"));
        assertTrue(json.contains("\"simple\":\"value\""));
    }

    @Test
    public void toDetailsJson_truncatesWhenTooLarge_returnsTruncatedMarker() {
        Map<String, String> details = new HashMap<String, String>();
        details.put("big", repeat('x', 9000));

        String json = AuditEventDbMapper.toDetailsJson(details);

        assertEquals("{\"truncated\":\"true\"}", json);
    }

    // ---------------------------------------------------------------------
    // Stack trace Blob handling
    // ---------------------------------------------------------------------

    @Test
    public void mapInto_stackTraceStoredAsUtf8Blob() throws Exception {
        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .stackTraceHash("hash123")
                .stackTraceTruncated("stacktrace")
                .build();

        mapper.mapInto(row, event);

        verify(row).setStackTraceHash("hash123");
        verify(row).setStackTraceTruncated(blobCaptor.capture());

        Blob blob = blobCaptor.getValue();
        assertNotNull(blob);

        byte[] bytes = blob.getBytes(1, (int) blob.length());
        assertEquals("stacktrace", new String(bytes, StandardCharsets.UTF_8));
    }

    // ---------------------------------------------------------------------
    // Error fields
    // ---------------------------------------------------------------------

    @Test
    public void mapInto_errorFieldsMappedSafely() {
        AuditEvent event = AuditEvent.builder()
                .step(AuditStep.EXECUTION)
                .category(AuditCategory.DT5_FLOW)
                .errorCode(AuditErrorCode.CLS_CONNECTION_FAILED)
                .errorMessage("Connection failed")
                .exceptionClass("java.net.SocketTimeoutException")
                .build();

        mapper.mapInto(row, event);

        verify(row).setErrorCode("CLS_CONNECTION_FAILED");
        verify(row).setErrorMessage("Connection failed");
        verify(row).setExceptionClass("java.net.SocketTimeoutException");
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
