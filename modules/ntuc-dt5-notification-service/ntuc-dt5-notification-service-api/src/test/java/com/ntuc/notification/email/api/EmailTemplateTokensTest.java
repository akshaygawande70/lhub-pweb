package com.ntuc.notification.email.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class EmailTemplateTokensTest {

    @Test
    public void allTokens_containsEveryDeclaredConstant_inOrder() {
        List<String> expected = Arrays.asList(
                EmailTemplateTokens.AUDIT_ID,
                EmailTemplateTokens.CORRELATION_ID,
                EmailTemplateTokens.JOB_RUN_ID,
                EmailTemplateTokens.REQUEST_ID,
                EmailTemplateTokens.TIMESTAMP,
                EmailTemplateTokens.STEP,
                EmailTemplateTokens.CATEGORY,
                EmailTemplateTokens.STATUS,
                EmailTemplateTokens.SEVERITY,
                EmailTemplateTokens.ERROR_CODE,
                EmailTemplateTokens.ERROR_MESSAGE,
                EmailTemplateTokens.EXCEPTION_CLASS,
                EmailTemplateTokens.COURSE_CODE,
                EmailTemplateTokens.NTUC_SB_ID,
                EmailTemplateTokens.ENDPOINT,
                EmailTemplateTokens.RESPONSE_CODE,
                EmailTemplateTokens.DDM_STRUCTURE_KEY,
                EmailTemplateTokens.DDM_TEMPLATE_KEY
        );

        Set<String> actual = EmailTemplateTokens.allTokens();

        assertEquals("Token count mismatch", expected.size(), actual.size());
        assertEquals(expected, new ArrayList<>(actual));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void allTokens_isUnmodifiable() {
        EmailTemplateTokens.allTokens().add("[$EXTRA$]");
    }
}
