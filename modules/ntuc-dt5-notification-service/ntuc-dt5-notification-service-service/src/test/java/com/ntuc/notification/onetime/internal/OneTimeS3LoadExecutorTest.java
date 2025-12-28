package com.ntuc.notification.onetime.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntuc.notification.onetime.internal.eligibility.EligibilityDecider;
import com.ntuc.notification.onetime.internal.eligibility.EligibilityDecision;
import org.junit.Test;

import static org.junit.Assert.*;

public class OneTimeS3LoadExecutorTest {

    private static final ObjectMapper M = new ObjectMapper();

    @Test
    public void eligibilityDecider_ineligible_whenMissingBody() throws Exception {
        EligibilityDecision d =
                EligibilityDecider.decide(M.readTree("{\"x\":1}"));

        assertFalse(d.isEligible());
    }

    @Test
    public void eligibilityDecider_published_isEligible() throws Exception {
        String json = "{ \"body\": { \"courseCatalogueStatus\":\"published\", \"courseCode\":\"C1\" } }";

        EligibilityDecision d =
                EligibilityDecider.decide(M.readTree(json));

        assertTrue(d.isEligible());
        assertEquals("C1", d.getCourseCode());
    }

    @Test
    public void eligibilityDecider_inactive_isEligible() throws Exception {
        String json = "{ \"body\": { \"courseCatalogueStatus\":\"inactive\", \"courseCode\":\"C2\" } }";

        EligibilityDecision d =
                EligibilityDecider.decide(M.readTree(json));

        assertTrue(d.isEligible());
        assertEquals("C2", d.getCourseCode());
    }

    @Test
    public void eligibilityDecider_otherStatus_isNotEligible() throws Exception {
        String json = "{ \"body\": { \"courseCatalogueStatus\":\"draft\", \"courseCode\":\"C3\" } }";

        EligibilityDecision d =
                EligibilityDecider.decide(M.readTree(json));

        assertFalse(d.isEligible());
        assertEquals("C3", d.getCourseCode());
    }
}
