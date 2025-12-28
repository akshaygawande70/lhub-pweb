package com.ntuc.notification.service.internal.cls;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CourseCodeMatcherTest {

    @Test
    public void matches_false_when_expected_null() {
        Assert.assertFalse(CourseCodeMatcher.matches(null, "ABC"));
    }

    @Test
    public void matches_false_when_actual_null() {
        Assert.assertFalse(CourseCodeMatcher.matches("ABC", null));
    }

    @Test
    public void matches_false_when_expected_blank() {
        Assert.assertFalse(CourseCodeMatcher.matches("   ", "ABC"));
    }

    @Test
    public void matches_false_when_actual_blank() {
        Assert.assertFalse(CourseCodeMatcher.matches("ABC", "   "));
    }

    @Test
    public void matches_true_case_insensitive() {
        Assert.assertTrue(CourseCodeMatcher.matches("tgs-001", "TGS-001"));
    }

    @Test
    public void matches_true_trimmed() {
        Assert.assertTrue(CourseCodeMatcher.matches("  TGS-001  ", "TGS-001"));
    }

    @Test
    public void matches_false_different_values() {
        Assert.assertFalse(CourseCodeMatcher.matches("TGS-001", "TGS-002"));
    }

    @Test
    public void normalize_null_is_empty() {
        Assert.assertEquals("", CourseCodeMatcher.normalize(null));
    }

    @Test
    public void normalize_trims() {
        Assert.assertEquals("TGS-001", CourseCodeMatcher.normalize("  TGS-001 "));
    }
}
