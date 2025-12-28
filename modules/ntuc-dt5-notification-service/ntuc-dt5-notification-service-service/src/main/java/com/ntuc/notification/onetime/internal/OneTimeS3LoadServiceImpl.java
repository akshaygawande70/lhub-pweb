package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.onetime.OneTimeS3LoadService;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi component wrapper. Keep it thin and delegate to a plain Java executor.
 */
@Component(service = OneTimeS3LoadService.class, immediate = true)
public class OneTimeS3LoadServiceImpl implements OneTimeS3LoadService {

    @Reference private ParameterGroupKeys parameterGroupKeys;
    @Reference private UserLocalService userLocalService;
    @Reference private GroupLocalService groupLocalService;
    @Reference private DDMTemplateUtil ddmTemplateUtil;
    @Reference private DDMStructureUtil ddmStructureUtil;
    @Reference private DDMStructureLocalService ddmStructureLocalService;
    @Reference private NotificationHandler notificationHandler;
    @Reference private AuditEventWriter auditEventWriter;

    @Override
    public void execute(String bucket, String prefix) throws PortalException {
        OneTimeS3LoadExecutor exec = new OneTimeS3LoadExecutor(
                parameterGroupKeys,
                userLocalService,
                groupLocalService,
                ddmTemplateUtil,
                ddmStructureUtil,
                ddmStructureLocalService,
                notificationHandler,
                auditEventWriter);

        exec.execute(bucket, prefix);
    }
}
