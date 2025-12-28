package com.ntuc.notification.service.internal.journal;

import com.liferay.journal.model.JournalArticle;
import com.ntuc.notification.journal.api.DynamicElementValueExtractor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class JournalArticleCandidateSelectorTest {

    @Test
    public void findLatestExactCourseCode_nullList_returnsNull() {
        JournalArticleCandidateSelector selector = new JournalArticleCandidateSelector(stubExtractor("C1"));
        Assert.assertNull(selector.findLatestExactCourseCode(null, "C1"));
    }

    @Test
    public void findLatestExactCourseCode_blankExpected_returnsNull() {
        JournalArticleCandidateSelector selector = new JournalArticleCandidateSelector(stubExtractor("C1"));
        Assert.assertNull(selector.findLatestExactCourseCode(Collections.<JournalArticle>emptyList(), "C1"));
        Assert.assertNull(selector.findLatestExactCourseCode(Collections.<JournalArticle>emptyList(), "  "));
    }

    @Test
    public void findLatestExactCourseCode_skipsNullAndEmptyXml() {
        DynamicElementValueExtractor extractor = stubExtractor("C1");
        JournalArticleCandidateSelector selector = new JournalArticleCandidateSelector(extractor);

        JournalArticle a1 = Mockito.mock(JournalArticle.class);
        Mockito.when(a1.getContent()).thenReturn(null);

        JournalArticle a2 = Mockito.mock(JournalArticle.class);
        Mockito.when(a2.getContent()).thenReturn("   ");

        List<JournalArticle> list = Arrays.asList(null, a1, a2);

        Assert.assertNull(selector.findLatestExactCourseCode(list, "C1"));
    }

    @Test
    public void findLatestExactCourseCode_returnsFirstMatchingCandidate() {
        DynamicElementValueExtractor extractor = new DynamicElementValueExtractor() {
            @Override
            public String extract(String xmlContent, String fieldName) {
                // Fake mapping: "xml=code:<value>"
                if (xmlContent == null) return "";
                if (!"courseCode".equals(fieldName)) return "";
                if (xmlContent.startsWith("code:")) {
                    return xmlContent.substring("code:".length());
                }
                return "";
            }
        };

        JournalArticleCandidateSelector selector = new JournalArticleCandidateSelector(extractor);

        JournalArticle a1 = Mockito.mock(JournalArticle.class);
        Mockito.when(a1.getContent()).thenReturn("code:OTHER");

        JournalArticle a2 = Mockito.mock(JournalArticle.class);
        Mockito.when(a2.getContent()).thenReturn("code:C1");

        JournalArticle a3 = Mockito.mock(JournalArticle.class);
        Mockito.when(a3.getContent()).thenReturn("code:C1");

        // ordered list: a2 is the "latest" match, so it must be returned
        List<JournalArticle> list = Arrays.asList(a1, a2, a3);

        Assert.assertSame(a2, selector.findLatestExactCourseCode(list, "C1"));
    }

    private static DynamicElementValueExtractor stubExtractor(final String returnedCode) {
        return new DynamicElementValueExtractor() {
            @Override
            public String extract(String xmlContent, String fieldName) {
                return returnedCode;
            }
        };
    }
}
