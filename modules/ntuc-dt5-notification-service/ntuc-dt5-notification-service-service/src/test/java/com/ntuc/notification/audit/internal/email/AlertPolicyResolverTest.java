package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.constants.AuditSeverity;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.ParameterKeyEnum;

import java.util.EnumMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlertPolicyResolverTest {

    private final AlertPolicyResolver resolver = new AlertPolicyResolver();

    @Test
    public void resolve_enabled_usesClsAlertEmailEnabled() {
        Map<ParameterKeyEnum, Object> m = new EnumMap<>(ParameterKeyEnum.class);
        m.put(ParameterKeyEnum.CLS_ALERT_EMAIL_ENABLED, "true");

        AlertPolicy p = resolver.resolve(m, 5);

        assertTrue(p.isEmailEnabled());
    }

    @Test
    public void isEligible_blocksEmailSteps() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.INFO,
            5,
            com.ntuc.notification.audit.api.constants.AlertScope.FLOW,
            "a@b.com"
        );

        assertFalse(resolver.isEligible(p, AuditStep.EMAIL_SENT, AuditStatus.FAILED, AuditSeverity.ERROR));
        assertFalse(resolver.isEligible(p, AuditStep.EMAIL_SUPPRESSED, AuditStatus.FAILED, AuditSeverity.ERROR));
        assertFalse(resolver.isEligible(p, AuditStep.EMAIL_SEND_FAILED, AuditStatus.FAILED, AuditSeverity.ERROR));
    }

    @Test
    public void isEligible_requiresFailedOrPartial() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.INFO,
            5,
            com.ntuc.notification.audit.api.constants.AlertScope.FLOW,
            "a@b.com"
        );

        assertFalse(resolver.isEligible(p, AuditStep.CRON_RECORD_FAILED, AuditStatus.SUCCESS, AuditSeverity.ERROR));
        assertTrue(resolver.isEligible(p, AuditStep.CRON_RECORD_FAILED, AuditStatus.FAILED, AuditSeverity.ERROR));
        assertTrue(resolver.isEligible(p, AuditStep.CRON_RECORD_FAILED, AuditStatus.PARTIAL, AuditSeverity.ERROR));
    }

    @Test
    public void isEligible_respectsSeverityThreshold() {
        AlertPolicy p = new AlertPolicy(
            true,
            AuditSeverity.ERROR,
            5,
            com.ntuc.notification.audit.api.constants.AlertScope.FLOW,
            "a@b.com"
        );

        assertFalse(resolver.isEligible(p, AuditStep.CRON_RECORD_FAILED, AuditStatus.FAILED, AuditSeverity.WARNING));
        assertTrue(resolver.isEligible(p, AuditStep.CRON_RECORD_FAILED, AuditStatus.FAILED, AuditSeverity.ERROR));
    }
    
    @Test
    public void isEligible_disabledPolicy_blocksAllEmails() {
        AlertPolicy p = new AlertPolicy(
            false,
            AuditSeverity.INFO,
            5,
            com.ntuc.notification.audit.api.constants.AlertScope.FLOW,
            "a@b.com"
        );

        assertFalse(
            resolver.isEligible(
                p,
                AuditStep.CRON_RECORD_FAILED,
                AuditStatus.FAILED,
                AuditSeverity.ERROR
            )
        );
    }

}
