package com.ntuc.notification.audit.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class AuditLogQueryValidatorTest {

    @Test
    public void normalize_invalidDates_defaultsToToday() {
        AuditLogQueryValidator.Result r =
            AuditLogQueryValidator.normalize("bad", "also-bad", "abc");

        String today = LocalDate.now().toString();
        Assert.assertEquals(today, r.getFromYmd());
        Assert.assertEquals(today, r.getToYmd());
        Assert.assertTrue(r.isChanged());
        Assert.assertFalse(r.getMessages().isEmpty());
    }

    @Test
    public void normalize_toInFuture_clamps() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(2);

        AuditLogQueryValidator.Result r =
            AuditLogQueryValidator.normalize(today.toString(), future.toString(), "");

        Assert.assertEquals(today.toString(), r.getToYmd());
        Assert.assertTrue(r.isChanged());
    }

    @Test
    public void normalize_fromAfterTo_aligns() {
        LocalDate today = LocalDate.now();
        LocalDate y = today.minusDays(1);

        AuditLogQueryValidator.Result r =
            AuditLogQueryValidator.normalize(today.toString(), y.toString(), "");

        Assert.assertEquals(y.toString(), r.getFromYmd());
        Assert.assertEquals(y.toString(), r.getToYmd());
        Assert.assertTrue(r.isChanged());
    }

    @Test
    public void normalize_rangeOver90_clampsFrom() {
        LocalDate today = LocalDate.now();
        LocalDate farPast = today.minusDays(200);

        AuditLogQueryValidator.Result r =
            AuditLogQueryValidator.normalize(farPast.toString(), today.toString(), "");

        Assert.assertEquals(today.minusDays(AuditLogQueryValidator.MAX_RANGE_DAYS).toString(), r.getFromYmd());
        Assert.assertEquals(today.toString(), r.getToYmd());
        Assert.assertTrue(r.isChanged());
    }

    @Test
    public void normalize_queryOver100_truncates() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 150; i++) sb.append('x');

        AuditLogQueryValidator.Result r =
            AuditLogQueryValidator.normalize(LocalDate.now().toString(), LocalDate.now().toString(), sb.toString());

        Assert.assertEquals(AuditLogQueryValidator.MAX_QUERY_LEN, r.getQuery().length());
        Assert.assertTrue(r.isChanged());
    }
}
