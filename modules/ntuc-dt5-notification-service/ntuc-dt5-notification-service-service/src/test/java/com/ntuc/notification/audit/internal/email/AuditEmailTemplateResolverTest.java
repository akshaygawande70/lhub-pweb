package com.ntuc.notification.audit.internal.email;

import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;
import com.ntuc.notification.email.api.AlertEmailCategory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuditEmailTemplateResolverTest {

    @Mock
    private ConfigurationProvider configurationProvider;

    @Mock
    private Dt5EmailTemplateConfiguration cfg;

    @InjectMocks
    private AuditEmailTemplateResolver resolver;

    @Test(expected = IllegalArgumentException.class)
    public void resolve_nullCategory_throws() {
        resolver.resolve(null);
    }

    @Test
    public void resolve_cls_mapsCorrectly() throws Exception {
        when(configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class)).thenReturn(cfg);
        when(cfg.clsFailureSubject()).thenReturn("S1");
        when(cfg.clsFailureBody()).thenReturn("B1");

        AuditEmailTemplateResolver.TemplatePair p = resolver.resolve(AlertEmailCategory.CLS_FAILURE);

        assertEquals("S1", p.getSubject());
        assertEquals("B1", p.getBody());
    }

    @Test
    public void resolve_ja_mapsCorrectly() throws Exception {
        when(configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class)).thenReturn(cfg);
        when(cfg.jaFailureSubject()).thenReturn("S2");
        when(cfg.jaFailureBody()).thenReturn("B2");

        AuditEmailTemplateResolver.TemplatePair p = resolver.resolve(AlertEmailCategory.JA_FAILURE);

        assertEquals("S2", p.getSubject());
        assertEquals("B2", p.getBody());
    }

    @Test
    public void resolve_dt5_mapsCorrectly() throws Exception {
        when(configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class)).thenReturn(cfg);
        when(cfg.dt5FailureSubject()).thenReturn("S3");
        when(cfg.dt5FailureBody()).thenReturn("B3");

        AuditEmailTemplateResolver.TemplatePair p = resolver.resolve(AlertEmailCategory.DT5_FAILURE);

        assertEquals("S3", p.getSubject());
        assertEquals("B3", p.getBody());
    }

    @Test
    public void resolve_clamps() throws Exception {
        when(configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class)).thenReturn(cfg);

        when(cfg.dt5FailureSubject()).thenReturn(repeat("S", AuditEmailTemplateResolver.MAX_SUBJECT_LEN + 5));
        when(cfg.dt5FailureBody()).thenReturn(repeat("B", AuditEmailTemplateResolver.MAX_BODY_LEN + 5));

        AuditEmailTemplateResolver.TemplatePair p = resolver.resolve(AlertEmailCategory.DT5_FAILURE);

        assertEquals(AuditEmailTemplateResolver.MAX_SUBJECT_LEN, p.getSubject().length());
        assertEquals(AuditEmailTemplateResolver.MAX_BODY_LEN, p.getBody().length());
    }

    @Test
    public void resolve_whenProviderThrows_wrapsAsIllegalState() throws Exception {
        when(configurationProvider.getSystemConfiguration(Dt5EmailTemplateConfiguration.class))
            .thenThrow(new RuntimeException("boom"));

        try {
            resolver.resolve(AlertEmailCategory.DT5_FAILURE);
            fail("Expected exception");
        }
        catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Unable to load Dt5EmailTemplateConfiguration"));
        }
    }

    private static String repeat(String s, int n) {
        StringBuilder b = new StringBuilder(n * s.length());
        for (int i = 0; i < n; i++) b.append(s);
        return b.toString();
    }
}
