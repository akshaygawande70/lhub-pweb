package com.ntuc.notification.service.internal.journal;

import com.liferay.journal.model.JournalArticle;
import com.ntuc.notification.journal.api.DynamicElementValueExtractor;

import java.util.List;

/**
 * Selects the most appropriate JournalArticle candidate based on XML content.
 *
 * NOTE:
 * - Caller is responsible for pre-filtering candidates (status, group, company, ordering, etc.)
 * - This selector performs the exact courseCode match using DDM XML extraction.
 */
public class JournalArticleCandidateSelector {

    private final DynamicElementValueExtractor extractor;

    public JournalArticleCandidateSelector(DynamicElementValueExtractor extractor) {
        this.extractor = extractor;
    }

    /**
     * Returns the first candidate whose extracted courseCode equals the expected courseCode.
     * Candidates should already be ordered (e.g., by version desc).
     */
    public JournalArticle findLatestExactCourseCode(List<JournalArticle> candidates, String expectedCourseCode) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        String expected = safe(expectedCourseCode);

        for (JournalArticle candidate : candidates) {
            if (candidate == null) {
                continue;
            }
            String xml = candidate.getContent();
            if (xml == null || xml.trim().isEmpty()) {
                continue;
            }

            String code = extractor.extract(xml, "courseCode");
            if (expected.equals(safe(code))) {
                return candidate;
            }
        }
        return null;
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
