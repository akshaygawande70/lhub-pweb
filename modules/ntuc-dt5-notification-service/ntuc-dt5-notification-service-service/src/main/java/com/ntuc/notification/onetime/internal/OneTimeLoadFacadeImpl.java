package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.ntuc.notification.onetime.OneTimeS3LoadService;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;

import java.util.Objects;

/**
 * Service-layer facade implementation for One-Time S3 load.
 *
 * Non-negotiable:
 * - No reflection
 * - Deterministic parsing via {@link S3PathParser}
 * - Delegation to stable {@link OneTimeS3LoadService#execute(String, String)}
 */
public class OneTimeLoadFacadeImpl implements OneTimeLoadFacade {

    private final OneTimeS3LoadService oneTimeS3LoadService;
    private final S3PathParser s3PathParser;

    public OneTimeLoadFacadeImpl(OneTimeS3LoadService oneTimeS3LoadService) {
        this(oneTimeS3LoadService, new S3PathParser());
    }

    // Visible for unit tests
    OneTimeLoadFacadeImpl(OneTimeS3LoadService oneTimeS3LoadService, S3PathParser s3PathParser) {
        this.oneTimeS3LoadService = Objects.requireNonNull(oneTimeS3LoadService, "oneTimeS3LoadService");
        this.s3PathParser = Objects.requireNonNull(s3PathParser, "s3PathParser");
    }

    @Override
    public void executeS3Path(String s3Path) {
        S3PathParser.S3Location loc = s3PathParser.parse(s3Path);

        try {
            oneTimeS3LoadService.execute(loc.getBucket(), loc.getPrefix());
        }
        catch (PortalException e) {
            // Transport-neutral facade: caller maps to HTTP response.
            throw new RuntimeException("One-time S3 load failed", e);
        }
    }
}
