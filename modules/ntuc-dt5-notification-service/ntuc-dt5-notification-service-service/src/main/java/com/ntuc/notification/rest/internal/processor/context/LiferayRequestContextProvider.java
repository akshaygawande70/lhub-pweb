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
 * Builds {@link RequestContext} using Liferay thread-local state and platform services.
 *
 * <p><b>Business purpose:</b>
 * Establishes a consistent execution context (company, group, user, correlation)
 * for REST-triggered notification workflows so that audit, logging, and downstream
 * processing behave predictably.</p>
 *
 * <p><b>Technical purpose:</b>
 * Resolves companyId, groupId, and userId from Liferay ThreadLocals with safe
 * fallbacks, and manages correlationId propagation via MDC.</p>
 *
 * <p><b>Author:</b> @akshaygawande</p>
 */
public class LiferayRequestContextProvider implements RequestContextProvider {

    /**
     * Fallback groupId when a company group cannot be resolved.
     * Kept explicit to avoid accidental use of negative or undefined values.
     */
    private static final long FALLBACK_GROUP_ID = 0L;

    private final GroupLocalService groupLocalService;
    private final UserLocalService userLocalService;

    /**
     * Constructs the provider with required Liferay services.
     *
     * @param groupLocalService service used to resolve company group
     * @param userLocalService  service used to resolve default user
     */
    public LiferayRequestContextProvider(
            GroupLocalService groupLocalService,
            UserLocalService userLocalService) {

        this.groupLocalService = groupLocalService;
        this.userLocalService = userLocalService;
    }

    /**
     * Returns an existing correlationId from MDC or creates a new one if absent.
     *
     * <p><b>Business purpose:</b>
     * Ensures all processing within a request chain can be traced end-to-end.</p>
     *
     * <p><b>Technical purpose:</b>
     * Reads correlationId from MDC; if missing, generates a UUID and stores it
     * back into MDC for downstream reuse.</p>
     *
     * <p><b>Side effects:</b>
     * Writes correlationId into MDC when newly generated.</p>
     *
     * @return non-blank correlationId
     */
    @Override
    public String getOrCreateCorrelationId() {
        String corrId = MdcUtil.getCorrId();

        if (isBlank(corrId)) {
            corrId = UUID.randomUUID().toString();
            MdcUtil.setCorrId(corrId);
        }

        return corrId;
    }

    /**
     * Builds the current {@link RequestContext} using thread-local state.
     *
     * <p><b>Business purpose:</b>
     * Provides a fully resolved execution context for REST processing where
     * correlation may or may not already exist.</p>
     *
     * <p><b>Technical purpose:</b>
     * Ensures a correlationId exists (creating one if required) and resolves
     * companyId, groupId, and userId with safe defaults.</p>
     *
     * <p><b>Side effects:</b>
     * May write correlationId into MDC.</p>
     *
     * @return populated {@link RequestContext}
     */
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

    /**
     * Builds the current {@link RequestContext} using an explicit correlationId.
     *
     * <p><b>Business purpose:</b>
     * Allows externally supplied correlation identifiers (e.g. upstream systems)
     * to be enforced for trace continuity.</p>
     *
     * <p><b>Technical purpose:</b>
     * Validates the provided correlationId, sets it into MDC, and resolves
     * companyId, groupId, and userId.</p>
     *
     * <p><b>Invariants:</b>
     * correlationId must be non-blank.</p>
     *
     * <p><b>Side effects:</b>
     * Writes the provided correlationId into MDC.</p>
     *
     * @param correlationId externally supplied correlation identifier
     * @return populated {@link RequestContext}
     * @throws IllegalArgumentException when correlationId is blank
     */
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

    /**
     * Resolves companyId, groupId, and userId from Liferay context with fallbacks.
     *
     * <p><b>Business purpose:</b>
     * Guarantees stable identifiers even when requests execute outside a
     * fully-authenticated portal thread.</p>
     *
     * <p><b>Technical purpose:</b>
     * Uses {@link CompanyThreadLocal}, {@link PrincipalThreadLocal}, and portal
     * services to derive identifiers, falling back to defaults when required.</p>
     *
     * <p><b>Side effects:</b>
     * None (read-only access to ThreadLocals and services).</p>
     *
     * @return resolved identifier container
     */
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

    /**
     * Null-safe and whitespace-safe blank check.
     *
     * @param s input string
     * @return true when null or empty after trim
     */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Simple value holder for resolved identifiers.
     */
    private static final class ResolvedIds {
        long companyId;
        long groupId;
        long userId;
    }
}
