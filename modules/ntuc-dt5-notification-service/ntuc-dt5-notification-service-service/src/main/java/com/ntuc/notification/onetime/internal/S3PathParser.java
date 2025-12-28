package com.ntuc.notification.onetime.internal;

import java.util.Objects;

/**
 * Parses S3 paths of the form:
 *
 * <ul>
 *   <li>s3://bucket</li>
 *   <li>s3://bucket/</li>
 *   <li>s3://bucket/prefix</li>
 *   <li>s3://bucket/prefix/</li>
 * </ul>
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Bucket is mandatory</li>
 *   <li>Prefix may be empty</li>
 *   <li>Prefix is normalized to never start with '/'</li>
 * </ul>
 *
 * <p>This class is plain Java and MUST be unit-tested.</p>
 */
public class S3PathParser {

    /**
     * Parses the given S3 path into bucket and prefix.
     *
     * @param s3Path S3 path string (must start with {@code s3://})
     * @return parsed {@link S3Location}
     * @throws IllegalArgumentException if the path is invalid
     */
    public S3Location parse(String s3Path) {
        if (s3Path == null || s3Path.trim().isEmpty()) {
            throw new IllegalArgumentException("s3Path is required");
        }

        String raw = s3Path.trim();

        if (!raw.startsWith("s3://")) {
            throw new IllegalArgumentException("s3Path must start with s3://");
        }

        String remainder = raw.substring("s3://".length()); // bucket[/prefix...]

        if (remainder.isEmpty()) {
            throw new IllegalArgumentException("s3Path must include a bucket name");
        }

        int slashIndex = remainder.indexOf('/');

        final String bucket;
        final String prefix;

        if (slashIndex < 0) {
            bucket = remainder;
            prefix = "";
        }
        else {
            bucket = remainder.substring(0, slashIndex);
            prefix = remainder.substring(slashIndex + 1); // may be empty
        }

        String normalizedBucket = bucket.trim();
        if (normalizedBucket.isEmpty()) {
            throw new IllegalArgumentException("s3Path must include a bucket name");
        }

        String normalizedPrefix = (prefix == null) ? "" : prefix.trim();
        while (normalizedPrefix.startsWith("/")) {
            normalizedPrefix = normalizedPrefix.substring(1);
        }

        return new S3Location(normalizedBucket, normalizedPrefix);
    }

    /**
     * Parsed S3 location.
     */
    public static final class S3Location {

        private final String bucket;
        private final String prefix;

        public S3Location(String bucket, String prefix) {
            this.bucket = Objects.requireNonNull(bucket, "bucket");
            this.prefix = (prefix == null) ? "" : prefix;
        }

        public String getBucket() {
            return bucket;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
