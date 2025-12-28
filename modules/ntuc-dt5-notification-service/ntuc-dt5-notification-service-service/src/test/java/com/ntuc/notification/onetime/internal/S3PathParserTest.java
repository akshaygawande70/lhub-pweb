package com.ntuc.notification.onetime.internal;

import org.junit.Test;

import static org.junit.Assert.*;

public class S3PathParserTest {

    private final S3PathParser p = new S3PathParser();

    @Test
    public void parse_bucketOnly() {
        S3PathParser.S3Location loc = p.parse("s3://my-bucket");
        assertEquals("my-bucket", loc.getBucket());
        assertEquals("", loc.getPrefix());
    }

    @Test
    public void parse_bucketSlash() {
        S3PathParser.S3Location loc = p.parse("s3://my-bucket/");
        assertEquals("my-bucket", loc.getBucket());
        assertEquals("", loc.getPrefix());
    }

    @Test
    public void parse_bucketPrefix() {
        S3PathParser.S3Location loc = p.parse("s3://my-bucket/a/b/c");
        assertEquals("my-bucket", loc.getBucket());
        assertEquals("a/b/c", loc.getPrefix());
    }

    @Test
    public void parse_prefixLeadingSlash_normalized() {
        S3PathParser.S3Location loc = p.parse("s3://my-bucket//a/b");
        assertEquals("my-bucket", loc.getBucket());
        assertEquals("a/b", loc.getPrefix());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_blank_throws() {
        p.parse("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_missingScheme_throws() {
        p.parse("my-bucket/a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_schemeOnly_throws() {
        p.parse("s3://");
    }
}
