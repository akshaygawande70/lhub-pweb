package com.ntuc.notification.rest.internal.processor.context;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.ntuc.notification.audit.util.MdcUtil;

import java.util.UUID;

/**
 * Builds RequestContext from Liferay thread-locals.
 *
 * Strict rule:
 * - currentWithCorrelation(corrId) MUST NOT read MDC and MUST NOT generate a new corrId.
 *
 * Observability rule:
 * - getOrCreateCorrelationId() MAY read MDC and MAY generate a new corrId, and MUST
 *   set it into MDC so downstream components share the same correlationId.
 */
public class LiferayRequestContextProvider implements RequestContextProvider {

    private static final long FALLBACK_GROUP_ID = 0L;

    private final GroupLocalService groupLocalService;
    private final UserLocalService userLocalService;

    public LiferayRequestContextProvider(GroupLocalService groupLocalService, UserLocalService userLocalService) {
        this.groupLocalService = groupLocalService;
        this.userLocalService = userLocalService;
    }

    @Override
    public String getOrCreateCorrelationId() {
        String corrId = MdcUtil.getCorrId();

        if (isBlank(corrId)) {
            corrId = UUID.randomUUID().toString();
            MdcUtil.setCorrId(corrId);
        }

        return corrId;
    }

    @Override
    public RequestContext current() {
        String corrId = getOrCreateCorrelationId();
        ResolvedIds ids = resolveIds();

        return new RequestContext(
            corrId,
            ids.companyId,
            ids.groupId,
            ids.userId,
            "",
            0L
        );
    }

    @Override
    public RequestContext currentWithCorrelation(String correlationId) {
        if (isBlank(correlationId)) {
            throw new IllegalArgumentException("correlationId must not be blank");
        }

        MdcUtil.setCorrId(correlationId);

        ResolvedIds ids = resolveIds();

        return new RequestContext(
            correlationId,
            ids.companyId,
            ids.groupId,
            ids.userId,
            "",
            0L
        );
    }

    private ResolvedIds resolveIds() {
        ResolvedIds ids = new ResolvedIds();

        long companyId = CompanyThreadLocal.getCompanyId();
        if (companyId <= 0) {
            companyId = PortalUtil.getDefaultCompanyId();
        }
        ids.companyId = companyId;

        long groupId = FALLBACK_GROUP_ID;
        Group companyGroup = groupLocalService.fetchCompanyGroup(companyId);
        if (companyGroup != null) {
            groupId = companyGroup.getGroupId();
        }
        ids.groupId = groupId;

        long userId = PrincipalThreadLocal.getUserId();
        if (userId <= 0) {
            try {
                userId = userLocalService.getDefaultUserId(companyId);
            }
            catch (Exception e) {
                userId = 0L;
            }
        }
        ids.userId = userId;

        return ids;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static final class ResolvedIds {
        long companyId;
        long groupId;
        long userId;
    }
}
