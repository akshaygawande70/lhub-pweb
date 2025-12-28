package com.ntuc.notification.service.impl;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.service.internal.audit.CourseAuditEventFactory;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClsCourseFieldsProcessorImplTest {

    @Mock private NotificationHandler notificationHandler;
    @Mock private UserLocalService userLocalService;
    @Mock private GroupLocalService groupLocalService;
    @Mock private DDMStructureLocalService ddmStructureLocalService;
    @Mock private ParameterGroupKeys parameterGroupKeys;
    @Mock private DDMTemplateUtil ddmTemplateUtil;
    @Mock private DDMStructureUtil ddmStructureUtil;
    @Mock private AuditEventWriter auditEventWriter;

    @Mock private DDMTemplate ddmTemplate;
    @Mock private DDMStructure ddmStructure;
    @Mock private Group group;
    @Mock private JournalArticle journalArticle;

    private ClsCourseFieldsProcessorImpl impl;

    // Spy the factory so we can verify the normalized AuditStep values without changing production code.
    private CourseAuditEventFactory auditEventFactorySpy;

    @Before
    public void setUp() throws Exception {
        impl = new ClsCourseFieldsProcessorImpl();

        inject(impl, "notificationHandler", notificationHandler);
        inject(impl, "userLocalService", userLocalService);
        inject(impl, "groupLocalService", groupLocalService);
        inject(impl, "ddmStructureLocalService", ddmStructureLocalService);
        inject(impl, "parameterGroupKeys", parameterGroupKeys);
        inject(impl, "ddmTemplateUtil", ddmTemplateUtil);
        inject(impl, "ddmStructureUtil", ddmStructureUtil);
        inject(impl, "auditEventWriter", auditEventWriter);

        auditEventFactorySpy = spy(new CourseAuditEventFactory());
        inject(impl, "auditEventFactory", auditEventFactorySpy);

        Map<ParameterKeyEnum, Object> params = new HashMap<>();
        params.put(ParameterKeyEnum.CLS_COMPANY_ID, "20123");
        params.put(ParameterKeyEnum.CLS_GROUP_ID, "30123");
        params.put(ParameterKeyEnum.GLOBAL_USER_EMAIL, "test@ntuc.org");
        params.put(ParameterKeyEnum.CLS_FOLDERID, "40123");
        params.put(ParameterKeyEnum.CLS_TEMPLATE_NAME, "Course Template");

        inject(impl, "paramValues", params);

        when(userLocalService.getUserIdByEmailAddress(eq(20123L), eq("test@ntuc.org"))).thenReturn(50123L);

        when(ddmTemplate.getClassPK()).thenReturn(60001L);
        when(ddmTemplate.getTemplateKey()).thenReturn("TPL_KEY");

        when(ddmStructure.getStructureKey()).thenReturn("STRUCT_KEY");

        when(ddmStructureLocalService.getDDMStructure(eq(60001L))).thenReturn(ddmStructure);

        when(group.getDefaultLanguageId()).thenReturn("en_US");
        when(group.getAvailableLanguageIds()).thenReturn(new String[] { "en_US", "zh_CN" });
        when(groupLocalService.getGroup(eq(30123L))).thenReturn(group);

        when(ddmStructureUtil.extractFieldDataTypes(eq(ddmStructure))).thenReturn(new HashMap<String, String>());

        // Ensure writes don't throw.
        doNothing().when(auditEventWriter).write(any(AuditEvent.class));
    }

    @Test
    public void handleCourseNotification_unsupportedEventType_writesRejectedAudit_thenThrows() {
        CourseEvent event = validEvent("COURSE-1", "SOME_UNSUPPORTED_EVENT");

        try {
            impl.handleCourseNotification(event, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Event type not supported"));
        }

        verify(auditEventWriter, atLeastOnce()).write(any(AuditEvent.class));
        verify(notificationHandler, never()).process(any());
        verifyProcessCronNever();
    }

    @Test
    public void handleCourseNotification_missingTemplate_writesAudit_thenThrows() {
        CourseEvent event = validEvent("COURSE-2", "PUBLISHED");

        when(ddmTemplateUtil.getTemplateByNameAndGroupId(eq(30123L), eq("Course Template"))).thenReturn(null);

        try {
            impl.handleCourseNotification(event, false);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("DDM template not found"));
        }

        verify(auditEventWriter, atLeastOnce()).write(any(AuditEvent.class));
        verify(notificationHandler, never()).process(any());
    }

    @Test
    public void handleCourseNotification_cron_success_usesExecutionStep() {
        CourseEvent event = validEvent("COURSE-3", "PUBLISHED");

        when(ddmTemplateUtil.getTemplateByNameAndGroupId(eq(30123L), eq("Course Template"))).thenReturn(ddmTemplate);

        impl.handleCourseNotification(event, true);

        verifyProcessCronCalled();
        verify(auditEventWriter, atLeast(2)).write(any(AuditEvent.class));

        // Key assertion: normalized step on the "success" event is EXECUTION (canonical cron/job step).
        verify(auditEventFactorySpy, atLeastOnce()).processingSuccess(
            anyLong(), anyLong(), anyString(),
            eq(30123L), eq(20123L), eq(50123L),
            eq("COURSE-3"), anyLong(),
            eq(AuditStep.EXECUTION)
        );
    }

    @Test
    public void handleCourseNotification_nonCron_success_usesJaProcessStep() throws Exception {
        CourseEvent event = validEvent("COURSE-4", "PUBLISHED");

        when(ddmTemplateUtil.getTemplateByNameAndGroupId(eq(30123L), eq("Course Template"))).thenReturn(ddmTemplate);
        when(notificationHandler.process(any())).thenReturn(journalArticle);

        impl.handleCourseNotification(event, false);

        verify(notificationHandler).process(any());
        verify(auditEventFactorySpy, atLeastOnce()).processingSuccess(
            anyLong(), anyLong(), anyString(),
            eq(30123L), eq(20123L), eq(50123L),
            eq("COURSE-4"), anyLong(),
            eq(AuditStep.JA_PROCESS)
        );
    }

    @Test
    public void handleCourseNotification_nonCron_nullJournalArticle_auditsFailure_thenThrows() throws Exception {
        CourseEvent event = validEvent("COURSE-5", "PUBLISHED");

        when(ddmTemplateUtil.getTemplateByNameAndGroupId(eq(30123L), eq("Course Template"))).thenReturn(ddmTemplate);
        when(notificationHandler.process(any())).thenReturn(null);

        try {
            impl.handleCourseNotification(event, false);
            fail("Expected exception");
        } catch (Exception ex) {
            // The method rethrows (PortalException path wraps to IllegalStateException in outer catch).
            assertNotNull(ex);
        }

        verify(auditEventWriter, atLeastOnce()).write(any(AuditEvent.class));

        // Key assertion: normalized step used for JA processing failures is JA_PROCESS.
        verify(auditEventFactorySpy, atLeastOnce()).processingFailed(
            anyLong(), anyLong(), anyString(),
            eq(30123L), eq(20123L), eq(50123L),
            eq("COURSE-5"), anyLong(),
            eq(AuditStep.JA_PROCESS),
            any(), any(), any(Throwable.class)
        );
    }

    private static CourseEvent validEvent(String courseCode, String eventType) {
        CourseEvent e = new CourseEvent();
        e.setCourseCodeSingle(courseCode);
        e.setEventType(eventType);
        e.setTimestamp("28/12/2025 10:11:12");
        return e;
    }

    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = findField(target.getClass(), fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static Field findField(Class<?> type, String fieldName) throws Exception {
        Class<?> t = type;
        while (t != null) {
            try {
                return t.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                t = t.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
    
    
    private void verifyProcessCronNever() {
        try {
            verify(notificationHandler, never()).processCron(any());
        } catch (Exception e) {
            fail("processCron() is not expected to throw during verify(): " + e.getMessage());
        }
    }

    private void verifyProcessCronCalled() {
        try {
            verify(notificationHandler).processCron(any());
        } catch (Exception e) {
            fail("processCron() is not expected to throw during verify(): " + e.getMessage());
        }
    }
}
