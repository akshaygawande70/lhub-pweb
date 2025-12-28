package com.ntuc.notification.rest.internal.processor.validation;

import org.junit.Test;

import static org.junit.Assert.*;

public class S3PathValidatorTest {

    private final S3PathValidator validator = new S3PathValidator();

    @Test(expected = IllegalArgumentException.class)
    public void validate_null_throws() {
        validator.validate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_blank_throws() {
        validator.validate("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_missingScheme_throws() {
        validator.validate("bucket/prefix/file.json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_wrongScheme_throws() {
        validator.validate("http://bucket/prefix");
    }

    @Test
    public void validate_bucketOnly_ok() {
        validator.validate("s3://my-bucket");
    }

    @Test
    public void validate_bucketWithSlash_ok() {
        validator.validate("s3://my-bucket/");
    }

    @Test
    public void validate_bucketAndPrefix_ok() {
        validator.validate("s3://my-bucket/some/prefix/file.json");
    }

    @Test
    public void parseBucketAndPrefix_bucketOnly() {
        String[] bp = validator.parseBucketAndPrefix("s3://my-bucket");
        assertEquals("my-bucket", bp[0]);
        assertEquals("", bp[1]);
    }

    @Test
    public void parseBucketAndPrefix_bucketSlash() {
        String[] bp = validator.parseBucketAndPrefix("s3://my-bucket/");
        assertEquals("my-bucket", bp[0]);
        assertEquals("", bp[1]);
    }

    @Test
    public void parseBucketAndPrefix_bucketAndPrefix() {
        String[] bp = validator.parseBucketAndPrefix("s3://my-bucket/a/b/c.json");
        assertEquals("my-bucket", bp[0]);
        assertEquals("a/b/c.json", bp[1]);
    }
}
