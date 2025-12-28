package com.ntuc.notification.audit.internal.email;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.constants.AuditCategory;
import com.ntuc.notification.audit.testutil.AuditEventTestBuilder;
import com.ntuc.notification.email.api.AlertEmailCategory;

import org.junit.Assert;
import org.junit.Test;

public class AlertEmailCategoryResolverTest {

    private final AlertEmailCategoryResolver resolver = new AlertEmailCategoryResolver();

    @Test
    public void clsDomain_mapsToClsFailure() {
        AuditEvent e = AuditEventTestBuilder.create()
            .category(AuditCategory.CLS)
            .build();

        Assert.assertEquals(AlertEmailCategory.CLS_FAILURE, resolver.resolve(e));
    }

    @Test
    public void jaDomain_mapsToJaFailure() {
        AuditEvent e = AuditEventTestBuilder.create()
            .category(AuditCategory.JOURNAL_ARTICLE)
            .build();

        Assert.assertEquals(AlertEmailCategory.JA_FAILURE, resolver.resolve(e));
    }

    @Test
    public void dt5Flow_mapsToDt5Failure() {
        AuditEvent e = AuditEventTestBuilder.create()
            .category(AuditCategory.DT5_FLOW)
            .build();

        Assert.assertEquals(AlertEmailCategory.DT5_FAILURE, resolver.resolve(e));
    }

    @Test
    public void alertEmailDomain_returnsNull_recursionGuard() {
        AuditEvent e = AuditEventTestBuilder.create()
            .category(AuditCategory.ALERT_EMAIL)
            .build();

        Assert.assertNull(resolver.resolve(e));
    }
}
