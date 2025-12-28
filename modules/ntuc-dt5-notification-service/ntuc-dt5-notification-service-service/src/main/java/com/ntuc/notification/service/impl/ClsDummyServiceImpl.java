package com.ntuc.notification.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.audit.util.MdcUtil;
import com.ntuc.notification.service.ClsConnectionHelper;
import com.ntuc.notification.service.ClsDummyService;
import com.ntuc.notification.service.internal.cls.ClsDummyApiFacade;

import java.util.UUID;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi wiring layer only.
 */
@Component(service = ClsDummyService.class)
public class ClsDummyServiceImpl implements ClsDummyService {

    @Reference
    private ClsConnectionHelper _clsConnectionHelper;

    @Reference
    private AuditEventWriter _auditEventWriter;

    private volatile ClsDummyApiFacade _facade;

    @Activate
    protected void activate() {
        _facade = new ClsDummyApiFacade(_clsConnectionHelper, _auditEventWriter);
    }

    @Override
    public String getDummyJson(String kind, String courseCode, ServiceContext serviceContext) throws PortalException {
        if (Validator.isNull(courseCode)) {
            return null;
        }

        ServiceContext sc = (serviceContext != null) ? serviceContext : ServiceContextThreadLocal.getServiceContext();

        long companyId = (sc != null) ? sc.getCompanyId() : CompanyThreadLocal.getCompanyId();
        long groupId = (sc != null) ? sc.getScopeGroupId() : 0L;
        long userId = (sc != null) ? sc.getUserId() : 0L;

        return _facade.fetchDummyJson(kind, courseCode, companyId, groupId, userId, corrId());
    }

    @Override
    public String getCoursesDummyJson(String courseCode, ServiceContext serviceContext) throws PortalException {
        return getDummyJson("COURSES", courseCode, serviceContext);
    }

    @Override
    public String getSubscriptionsDummyJson(String courseCode, ServiceContext serviceContext) throws PortalException {
        return getDummyJson("SUBSCRIPTIONS", courseCode, serviceContext);
    }

    private static String corrId() {
        String c = MdcUtil.getCorrId();
        return (c != null) ? c : UUID.randomUUID().toString();
    }
}
