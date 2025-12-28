package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.ntuc.notification.onetime.OneTimeS3LoadService;
import com.ntuc.notification.onetime.api.OneTimeLoadFacade;

import java.util.Objects;

/**
 * Facade implementation for initiating a one-time S3-based data load workflow.
 *
 * <p><b>Business purpose:</b>
 * Acts as a stable entry point for triggering a controlled, one-time ingestion
 * of course-related data from an S3 location without exposing parsing or transport details
 * to callers.</p>
 *
 * <p><b>Technical purpose:</b>
 * Parses an S3 path deterministically and delegates execution to
 * {@link OneTimeS3LoadService} while remaining transport-neutral
 * (REST/cron callers decide how failures are surfaced).</p>
 *
 * <p>Design constraints:</p>
 * <ul>
 *   <li>No reflection</li>
 *   <li>Deterministic S3 path parsing via {@link S3PathParser}</li>
 *   <li>Delegation-only facade with no business logic</li>
 * </ul>
 *
 * @author @akshaygawande
 */
public class OneTimeLoadFacadeImpl implements OneTimeLoadFacade {

    /**
     * Service responsible for executing the actual one-time S3 load.
     */
    private final OneTimeS3LoadService oneTimeS3LoadService;

    /**
     * Parser responsible for extracting bucket and prefix from an S3 path.
     */
    private final S3PathParser s3PathParser;

    /**
     * Creates a facade using the default {@link S3PathParser}.
     *
     * <p><b>Business purpose:</b>
     * Provides a simple construction path for production usage.</p>
     *
     * <p><b>Technical purpose:</b>
     * Delegates to the full constructor while instantiating a default parser.</p>
     *
     * @param oneTimeS3LoadService service executing the S3 load
     */
    public OneTimeLoadFacadeImpl(OneTimeS3LoadService oneTimeS3LoadService) {
        this(oneTimeS3LoadService, new S3PathParser());
    }

    /**
     * Package-visible constructor intended for unit testing.
     *
     * <p><b>Business purpose:</b>
     * Allows controlled substitution of collaborators for predictable test scenarios.</p>
     *
     * <p><b>Technical purpose:</b>
     * Enables injection of a mocked or stubbed {@link S3PathParser}.</p>
     *
     * @param oneTimeS3LoadService service executing the S3 load
     * @param s3PathParser parser used to resolve bucket and prefix
     */
    OneTimeLoadFacadeImpl(
            OneTimeS3LoadService oneTimeS3LoadService,
            S3PathParser s3PathParser) {

        this.oneTimeS3LoadService =
                Objects.requireNonNull(oneTimeS3LoadService, "oneTimeS3LoadService");
        this.s3PathParser =
                Objects.requireNonNull(s3PathParser, "s3PathParser");
    }

    /**
     * Executes a one-time S3 load for the given S3 path.
     *
     * <p><b>Business purpose:</b>
     * Triggers ingestion of data from a specified S3 location as a one-off operation.</p>
     *
     * <p><b>Technical purpose:</b>
     * Parses the provided S3 path into bucket and prefix components and
     * delegates execution to {@link OneTimeS3LoadService}.</p>
     *
     * <p><b>Inputs / invariants:</b>
     * <ul>
     *   <li>{@code s3Path} must be a valid S3 URI understood by {@link S3PathParser}</li>
     * </ul>
     *
     * <p><b>Side effects:</b>
     * <ul>
     *   <li>Invokes downstream service logic that may read from S3 and write to the database</li>
     * </ul>
     *
     * <p><b>Audit behavior:</b>
     * <ul>
     *   <li>No audit is recorded at the facade layer</li>
     *   <li>Audit responsibility is delegated to lower service layers</li>
     * </ul>
     *
     * <p><b>Return semantics:</b>
     * <ul>
     *   <li>No return value</li>
     *   <li>Throws {@link RuntimeException} wrapping {@link PortalException} on failure</li>
     * </ul>
     *
     * @param s3Path S3 URI identifying the source bucket and prefix
     */
    @Override
    public void executeS3Path(String s3Path) {
        S3PathParser.S3Location loc = s3PathParser.parse(s3Path);

        try {
            oneTimeS3LoadService.execute(loc.getBucket(), loc.getPrefix());
        }
        catch (PortalException e) {
            /*
             * Transport-neutral facade:
             * checked exceptions are intentionally wrapped so that
             * REST or scheduler callers can map failures appropriately.
             */
            throw new RuntimeException("One-time S3 load failed", e);
        }
    }
}
