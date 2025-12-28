package com.ntuc.notification.onetime.internal;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import com.ntuc.notification.audit.api.AuditEvent;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.api.constants.AuditStatus;
import com.ntuc.notification.audit.api.constants.AuditStep;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.constants.ParameterKeyEnum;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import com.ntuc.notification.onetime.internal.s3.S3IterationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link OneTimeS3LoadExecutor}.
 *
 * @author @akshaygawande
 */
@RunWith(MockitoJUnitRunner.class)
public class OneTimeS3LoadExecutorTest {

    @Mock
    private ParameterGroupKeys parameterGroupKeys;

    @Mock
    private UserLocalService userLocalService;

    @Mock
    private GroupLocalService groupLocalService;

    @Mock
    private DDMTemplateUtil ddmTemplateUtil;

    @Mock
    private DDMStructureUtil ddmStructureUtil;

    @Mock
    private DDMStructureLocalService ddmStructureLocalService;

    @Mock
    private NotificationHandler notificationHandler;

    @Mock
    private AuditEventWriter auditEventWriter;

    @Mock
    private DDMTemplate ddmTemplate;

    @Mock
    private DDMStructure ddmStructure;

    @Mock
    private Group group;

    @Captor
    private ArgumentCaptor<AuditEvent> auditEventCaptor;

    @Test
    public void execute_bucketBlank_writesValidationFailed_andReturns() throws Exception {
        OneTimeS3LoadExecutor exec = new OneTimeS3LoadExecutor(
                parameterGroupKeys,
                userLocalService,
                groupLocalService,
                ddmTemplateUtil,
                ddmStructureUtil,
                ddmStructureLocalService,
                notificationHandler,
                auditEventWriter);

        exec.execute("   ", "p1");

        verify(auditEventWriter).write(auditEventCaptor.capture());

        boolean foundValidationFailed = false;

        for (AuditEvent e : auditEventCaptor.getAllValues()) {
            if (AuditStep.VALIDATION.equals(e.getStep()) && AuditStatus.FAILED.equals(e.getStatus())) {
                foundValidationFailed = true;
                break;
            }
        }

        assertEquals(true, foundValidationFailed);
    }

    @Test
    public void execute_happyPath_startsValidationAndExecution_withEndTimeZero() throws Exception {
        Map<ParameterKeyEnum, Object> params = new HashMap<>();
        params.put(ParameterKeyEnum.CLS_COMPANY_ID, "20101");
        params.put(ParameterKeyEnum.CLS_GROUP_ID, "30101");
        params.put(ParameterKeyEnum.CLS_FOLDERID, "40101");
        params.put(ParameterKeyEnum.CLS_TEMPLATE_NAME, "Course Template");
        params.put(ParameterKeyEnum.CLS_AWS_REGION, "ap-southeast-1");
        params.put(ParameterKeyEnum.GLOBAL_USER_EMAIL, "svc@ntuc.org.sg");

        when(parameterGroupKeys.getAllParameterValues()).thenReturn(params);

        when(userLocalService.getUserIdByEmailAddress(eq(20101L), anyString())).thenReturn(90101L);

        when(ddmTemplateUtil.getTemplateByNameAndGroupId(eq(30101L), eq("Course Template"))).thenReturn(ddmTemplate);
        when(ddmTemplate.getClassPK()).thenReturn(777L);
        when(ddmTemplate.getTemplateKey()).thenReturn("TPL_KEY");

        when(ddmStructureLocalService.getDDMStructure(eq(777L))).thenReturn(ddmStructure);
        when(ddmStructure.getStructureKey()).thenReturn("STRUCT_KEY");

        when(groupLocalService.fetchGroup(eq(30101L))).thenReturn(group);
        when(group.getDefaultLanguageId()).thenReturn("en_US");

        when(ddmStructureUtil.extractFieldDataTypes(eq(ddmStructure))).thenReturn(Collections.singletonMap("courseTitle", "text"));

        OneTimeS3LoadExecutor real = new OneTimeS3LoadExecutor(
                parameterGroupKeys,
                userLocalService,
                groupLocalService,
                ddmTemplateUtil,
                ddmStructureUtil,
                ddmStructureLocalService,
                notificationHandler,
                auditEventWriter);

        OneTimeS3LoadExecutor exec = spy(real);

        // Avoid exercising S3 iterator and item processing; we validate orchestration and audit semantics.
        doReturn(new S3IterationResult(2, 2, 0)).when(exec).processAll(
                anyString(),
                anyString(),
                any(),
                any(),
                anyLong(),
                anyLong(),
                anyLong(),
                any(),
                any(),
                anyString(),
                any());

        exec.execute("bucket", "prefix");

        verify(auditEventWriter).write(auditEventCaptor.capture());

        boolean foundValidationStarted = false;
        boolean foundExecutionStarted = false;

        for (AuditEvent e : auditEventCaptor.getAllValues()) {
            if (AuditStep.VALIDATION.equals(e.getStep()) && AuditStatus.STARTED.equals(e.getStatus())) {
                assertEquals(0L, e.getEndTimeMs());
                foundValidationStarted = true;
            }
            if (AuditStep.EXECUTION.equals(e.getStep()) && AuditStatus.STARTED.equals(e.getStatus())) {
                assertEquals(0L, e.getEndTimeMs());
                foundExecutionStarted = true;
            }
        }

        assertEquals(true, foundValidationStarted);
        assertEquals(true, foundExecutionStarted);
    }
}
