package com.ntuc.notification.onetime.internal.eligibility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link OneTimeEligibilityDecider}.
 *
 * Rules verified:
 * - Missing body → ineligible
 * - published / inactive → eligible
 * - other status → ineligible but courseCode preserved
 * - Case-insensitive status handling
 *
 * Pure JUnit (no Liferay, no AWS).
 */
public class OneTimeEligibilityDeciderTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void decide_missingBody_isIneligible() throws Exception {
        OneTimeEligibilityDecision d =
                OneTimeEligibilityDecider.decide(MAPPER.readTree("{\"x\":1}"));

        assertFalse(d.isEligible());
        assertEquals("", d.getCourseCode());
        assertEquals("", d.getEventType());
    }

    @Test
    public void decide_published_isEligible_andSetsEventType() throws Exception {
        String json =
                "{ \"body\": { \"courseCatalogueStatus\": \"published\", \"courseCode\": \"C1\" } }";

        OneTimeEligibilityDecision d =
                OneTimeEligibilityDecider.decide(MAPPER.readTree(json));

        assertTrue(d.isEligible());
        assertEquals("C1", d.getCourseCode());
        assertEquals("published", d.getEventType());
        assertEquals("published", d.getStatus());
    }

    @Test
    public void decide_inactive_isEligible_andSetsEventType() throws Exception {
        String json =
                "{ \"body\": { \"courseCatalogueStatus\": \"inactive\", \"courseCode\": \"C2\" } }";

        OneTimeEligibilityDecision d =
                OneTimeEligibilityDecider.decide(MAPPER.readTree(json));

        assertTrue(d.isEligible());
        assertEquals("C2", d.getCourseCode());
        assertEquals("inactive", d.getEventType());
        assertEquals("inactive", d.getStatus());
    }

    @Test
    public void decide_otherStatus_isIneligible_butKeepsCourseCode() throws Exception {
        String json =
                "{ \"body\": { \"courseCatalogueStatus\": \"draft\", \"courseCode\": \"C3\" } }";

        OneTimeEligibilityDecision d =
                OneTimeEligibilityDecider.decide(MAPPER.readTree(json));

        assertFalse(d.isEligible());
        assertEquals("C3", d.getCourseCode());
        assertEquals("", d.getEventType());
        assertEquals("draft", d.getStatus());
    }

    @Test
    public void decide_statusIsCaseInsensitive() throws Exception {
        String json =
                "{ \"body\": { \"courseCatalogueStatus\": \"PuBlIsHeD\", \"courseCode\": \"C4\" } }";

        OneTimeEligibilityDecision d =
                OneTimeEligibilityDecider.decide(MAPPER.readTree(json));

        assertTrue(d.isEligible());
        assertEquals("C4", d.getCourseCode());
        assertEquals("published", d.getEventType());
    }
}
