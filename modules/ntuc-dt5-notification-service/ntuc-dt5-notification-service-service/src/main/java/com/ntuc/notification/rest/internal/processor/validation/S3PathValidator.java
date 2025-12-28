package com.ntuc.notification.rest.internal.processor.validation;

public class S3PathValidator {

    public void validate(String s3Path) {
        if (s3Path == null || s3Path.trim().isEmpty()) {
            throw new IllegalArgumentException("s3Path is required");
        }
        if (!s3Path.startsWith("s3://")) {
            throw new IllegalArgumentException("s3Path must start with s3://");
        }
        // Must have bucket at least
        String raw = s3Path.substring("s3://".length());
        if (raw.trim().isEmpty()) {
            throw new IllegalArgumentException("s3Path bucket is required");
        }
    }

    public String[] parseBucketAndPrefix(String s3Path) {
        String raw = s3Path.substring("s3://".length());
        int slash = raw.indexOf('/');
        if (slash < 0) {
            return new String[] { raw, "" };
        }
        return new String[] { raw.substring(0, slash), raw.substring(slash + 1) };
    }
}
