package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.constants.AlertScope;
import com.ntuc.notification.audit.api.constants.AuditSeverity;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlertPolicyTest {

    @Test
    public void defaults_areApplied_whenNullsProvided() {
        AlertPolicy p = new AlertPolicy(
            false,
            null,
            0,
            null,
            null
        );

        assertFalse(p.isEmailEnabled());
        assertEquals(AuditSeverity.ERROR, p.getSeverityThreshold());
        assertEquals(1, p.getDedupeWindowMinutes());
        assertEquals(AlertScope.FLOW, p.getScope());
        assertEquals("", p.getFallbackToEmail());
    }

    @Test
    public void dedupeWindow_isClamped_low() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.ERROR,
            -100,
            AlertScope.FLOW,
            "ops@test.com"
        );

        assertEquals(1, p.getDedupeWindowMinutes());
    }

    @Test
    public void dedupeWindow_isClamped_high() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.ERROR,
            99999,
            AlertScope.FLOW,
            "ops@test.com"
        );

        assertEquals(1440, p.getDedupeWindowMinutes());
    }

    @Test
    public void fallbackEmail_isTrimmed() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.ERROR,
            5,
            AlertScope.FLOW,
            "  ops@test.com  "
        );

        assertEquals("ops@test.com", p.getFallbackToEmail());
    }

    @Test
    public void emailEnabled_isPreserved() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.WARNING,
            10,
            AlertScope.COURSE,
            "ops@test.com"
        );

        assertTrue(p.isEmailEnabled());
        assertEquals(AuditSeverity.WARNING, p.getSeverityThreshold());
        assertEquals(AlertScope.COURSE, p.getScope());
    }
}
