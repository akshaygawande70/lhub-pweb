package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;

import com.ntuc.notification.audit.api.AuditEventWriter;
import com.ntuc.notification.constants.ParameterGroupKeys;
import com.ntuc.notification.onetime.OneTimeS3LoadService;
import com.ntuc.notification.service.NotificationHandler;
import com.ntuc.notification.util.DDMStructureUtil;
import com.ntuc.notification.util.DDMTemplateUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * OSGi service entry point for one-time S3-driven course data loading.
 *
 * <p>
 * Business Purpose:
 * Acts as a controlled execution hook to trigger a one-time ingestion workflow
 * for course-related data sourced from S3 into the NTUC platform.
 *
 * <p>
 * Technical Purpose:
 * This component is intentionally thin and delegates all processing to
 * {@link OneTimeS3LoadExecutor}, which is implemented as a plain Java class
 * suitable for isolated unit testing.
 *
 * <p>
 * Design Notes:
 * <ul>
 *   <li>This class must remain free of business logic.</li>
 *   <li>OSGi references are resolved here and passed explicitly to the executor.</li>
 *   <li>Audit logging is handled downstream by the executor via {@link AuditEventWriter}.</li>
 * </ul>
 *
 * @author @akshaygawande
 */
@Component(service = OneTimeS3LoadService.class, immediate = true)
public class OneTimeS3LoadServiceImpl implements OneTimeS3LoadService {

    /** Parameter group access for resolving runtime configuration values. */
    @Reference
    private ParameterGroupKeys parameterGroupKeys;

    /** User service used for resolving system or service users. */
    @Reference
    private UserLocalService userLocalService;

    /** Group service used for resolving site and company group context. */
    @Reference
    private GroupLocalService groupLocalService;

    /** Utility for resolving and validating DDM templates. */
    @Reference
    private DDMTemplateUtil ddmTemplateUtil;

    /** Utility for resolving and validating DDM structures. */
    @Reference
    private DDMStructureUtil ddmStructureUtil;

    /** Liferay local service for DDM structure access. */
    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    /** Notification handler used to trigger downstream course notifications. */
    @Reference
    private NotificationHandler notificationHandler;

    /** Centralized audit event writer for persistence and alerting. */
    @Reference
    private AuditEventWriter auditEventWriter;

    /**
     * Executes the one-time S3 load workflow.
     *
     * <p>
     * Business Purpose:
     * Initiates a controlled, one-off import of course data from the specified
     * S3 bucket and prefix, typically used for migration or recovery scenarios.
     *
     * <p>
     * Technical Purpose:
     * Instantiates {@link OneTimeS3LoadExecutor} with all required collaborators
     * and delegates execution to it.
     *
     * <p>
     * Inputs / Invariants:
     * <ul>
     *   <li>{@code bucket} must be a valid and accessible S3 bucket name.</li>
     *   <li>{@code prefix} defines the logical S3 path scope for the load.</li>
     * </ul>
     *
     * <p>
     * Side Effects:
     * <ul>
     *   <li>May read configuration from parameter groups.</li>
     *   <li>May create or update course-related artifacts.</li>
     *   <li>Writes audit events via {@link AuditEventWriter}.</li>
     * </ul>
     *
     * <p>
     * Audit Behavior:
     * Audit lifecycle (STARTED / SUCCESS / FAILED) is fully handled within
     * {@link OneTimeS3LoadExecutor}. This wrapper does not emit audit events.
     *
     * <p>
     * Return Semantics:
     * This method does not return a value.
     *
     * @param bucket the S3 bucket name
     * @param prefix the S3 key prefix to process
     * @throws PortalException if a fatal platform error occurs during execution
     */
    @Override
    public void execute(String bucket, String prefix) throws PortalException {

        OneTimeS3LoadExecutor executor =
                new OneTimeS3LoadExecutor(
                        parameterGroupKeys,
                        userLocalService,
                        groupLocalService,
                        ddmTemplateUtil,
                        ddmStructureUtil,
                        ddmStructureLocalService,
                        notificationHandler,
                        auditEventWriter);

        executor.execute(bucket, prefix);
    }
}
