package com.ntuc.notification.rest.internal.processor.context;

import static org.junit.Assert.*;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.ntuc.notification.audit.util.MdcUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link LiferayRequestContextProvider}.
 *
 * Note:
 * These tests use Mockito static mocking for Liferay ThreadLocals and PortalUtil/MdcUtil.
 */
@RunWith(MockitoJUnitRunner.class)
public class LiferayRequestContextProviderTest {

    @Mock
    private GroupLocalService groupLocalService;

    @Mock
    private UserLocalService userLocalService;

    @Mock
    private Group group;

    @Test
    public void getOrCreateCorrelationId_existingFromMdc_returnsSameAndDoesNotGenerate() {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class)) {
            mdc.when(MdcUtil::getCorrId).thenReturn("corr-123");

            String corr = provider.getOrCreateCorrelationId();

            assertEquals("corr-123", corr);
            mdc.verify(MdcUtil::getCorrId);
            mdc.verifyNoMoreInteractions(); // no setCorrId expected
        }
    }

    @Test
    public void getOrCreateCorrelationId_blankInMdc_generatesAndSetsInMdc() {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class)) {
            mdc.when(MdcUtil::getCorrId).thenReturn("   ");

            String corr = provider.getOrCreateCorrelationId();

            assertNotNull(corr);
            assertFalse(corr.trim().isEmpty());

            mdc.verify(MdcUtil::getCorrId);
            mdc.verify(() -> MdcUtil.setCorrId(Mockito.anyString()));
        }
    }

    @Test
    public void current_populatesContext_withResolvedIds_andMdcCorrelation() throws Exception {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        Mockito.when(groupLocalService.fetchCompanyGroup(2001L)).thenReturn(group);
        Mockito.when(group.getGroupId()).thenReturn(3001L);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class);
             MockedStatic<CompanyThreadLocal> ctl = Mockito.mockStatic(CompanyThreadLocal.class);
             MockedStatic<PortalUtil> portal = Mockito.mockStatic(PortalUtil.class);
             MockedStatic<PrincipalThreadLocal> ptl = Mockito.mockStatic(PrincipalThreadLocal.class)) {

            mdc.when(MdcUtil::getCorrId).thenReturn("corr-aaa");

            ctl.when(CompanyThreadLocal::getCompanyId).thenReturn(2001L);
            portal.when(PortalUtil::getDefaultCompanyId).thenReturn(9999L); // should not be used

            ptl.when(PrincipalThreadLocal::getUserId).thenReturn(4001L);

            RequestContext ctx = provider.current();

            assertNotNull(ctx);
            assertEquals("corr-aaa", ctx.getCorrelationId());
            assertEquals(2001L, ctx.getCompanyId());
            assertEquals(3001L, ctx.getGroupId());
            assertEquals(4001L, ctx.getUserId());

            mdc.verify(MdcUtil::getCorrId);
            mdc.verifyNoMoreInteractions();

            Mockito.verify(userLocalService, Mockito.never()).getDefaultUserId(Mockito.anyLong());
        }
    }


    @Test
    public void current_fallsBackCompanyId_andUserId_whenThreadLocalsMissing() throws Exception {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        // company group resolution for default company
        Mockito.when(groupLocalService.fetchCompanyGroup(5555L)).thenReturn(group);
        Mockito.when(group.getGroupId()).thenReturn(6666L);

        // default user resolution for default company
        Mockito.when(userLocalService.getDefaultUserId(5555L)).thenReturn(7777L);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class);
             MockedStatic<CompanyThreadLocal> ctl = Mockito.mockStatic(CompanyThreadLocal.class);
             MockedStatic<PortalUtil> portal = Mockito.mockStatic(PortalUtil.class);
             MockedStatic<PrincipalThreadLocal> ptl = Mockito.mockStatic(PrincipalThreadLocal.class)) {

            // Corr absent -> generated & set
            mdc.when(MdcUtil::getCorrId).thenReturn(null);

            // companyId missing -> fallback to PortalUtil default
            ctl.when(CompanyThreadLocal::getCompanyId).thenReturn(0L);
            portal.when(PortalUtil::getDefaultCompanyId).thenReturn(5555L);

            // userId missing -> fallback to default user service
            ptl.when(PrincipalThreadLocal::getUserId).thenReturn(0L);

            RequestContext ctx = provider.current();

            assertNotNull(ctx);
            assertNotNull(ctx.getCorrelationId());
            assertFalse(ctx.getCorrelationId().trim().isEmpty());

            assertEquals(5555L, ctx.getCompanyId());
            assertEquals(6666L, ctx.getGroupId());
            assertEquals(7777L, ctx.getUserId());

            mdc.verify(MdcUtil::getCorrId);
            mdc.verify(() -> MdcUtil.setCorrId(Mockito.anyString()));
        }
    }

    @Test
    public void current_groupFallbacksToZero_whenCompanyGroupMissing() {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        Mockito.when(groupLocalService.fetchCompanyGroup(101L)).thenReturn(null);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class);
             MockedStatic<CompanyThreadLocal> ctl = Mockito.mockStatic(CompanyThreadLocal.class);
             MockedStatic<PortalUtil> portal = Mockito.mockStatic(PortalUtil.class);
             MockedStatic<PrincipalThreadLocal> ptl = Mockito.mockStatic(PrincipalThreadLocal.class)) {

            mdc.when(MdcUtil::getCorrId).thenReturn("corr-x");
            ctl.when(CompanyThreadLocal::getCompanyId).thenReturn(101L);
            portal.when(PortalUtil::getDefaultCompanyId).thenReturn(9999L);
            ptl.when(PrincipalThreadLocal::getUserId).thenReturn(202L);

            RequestContext ctx = provider.current();

            assertEquals(101L, ctx.getCompanyId());
            assertEquals(0L, ctx.getGroupId()); // FALLBACK_GROUP_ID
            assertEquals(202L, ctx.getUserId());
        }
    }

    @Test
    public void currentWithCorrelation_blank_throwsIllegalArgumentException() {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        try {
            provider.currentWithCorrelation("  ");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("correlationId"));
        }
    }

    @Test
    public void currentWithCorrelation_setsMdcAndUsesProvidedCorrelation() throws Exception {
        LiferayRequestContextProvider provider =
                new LiferayRequestContextProvider(groupLocalService, userLocalService);

        Mockito.when(groupLocalService.fetchCompanyGroup(11L)).thenReturn(group);
        Mockito.when(group.getGroupId()).thenReturn(22L);
        Mockito.when(userLocalService.getDefaultUserId(11L)).thenReturn(33L);

        try (MockedStatic<MdcUtil> mdc = Mockito.mockStatic(MdcUtil.class);
             MockedStatic<CompanyThreadLocal> ctl = Mockito.mockStatic(CompanyThreadLocal.class);
             MockedStatic<PortalUtil> portal = Mockito.mockStatic(PortalUtil.class);
             MockedStatic<PrincipalThreadLocal> ptl = Mockito.mockStatic(PrincipalThreadLocal.class)) {

            // companyId missing -> fallback to PortalUtil default
            ctl.when(CompanyThreadLocal::getCompanyId).thenReturn(0L);
            portal.when(PortalUtil::getDefaultCompanyId).thenReturn(11L);

            // userId missing -> fallback to default user
            ptl.when(PrincipalThreadLocal::getUserId).thenReturn(0L);

            RequestContext ctx = provider.currentWithCorrelation("corr-fixed");

            assertEquals("corr-fixed", ctx.getCorrelationId());
            assertEquals(11L, ctx.getCompanyId());
            assertEquals(22L, ctx.getGroupId());
            assertEquals(33L, ctx.getUserId());

            // Provided correlation must be forced into MDC
            mdc.verify(() -> MdcUtil.setCorrId("corr-fixed"));
            // No read from MDC required by method contract (it doesn't call getCorrId)
            mdc.verifyNoMoreInteractions();
        }
    }
}
