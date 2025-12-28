package com.ntuc.notification.onetime.internal.s3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.function.BiConsumer;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

/**
 * Transport-only S3 JSON iterator.
 *
 * Rules:
 * - No audit here.
 * - No business logic here.
 * - Caller decides what to log/audit and how to handle errors.
 */
public class S3JsonIterator implements AutoCloseable {

    private final S3Client s3;
    private final ObjectMapper mapper;

    public S3JsonIterator(Region region, AwsCredentialsProvider credentialsProvider) {
        this(region, credentialsProvider, new ObjectMapper());
    }

    public S3JsonIterator(Region region, AwsCredentialsProvider credentialsProvider, ObjectMapper mapper) {
        Region effectiveRegion = (region != null) ? region : Region.AWS_GLOBAL;

        this.s3 = S3Client.builder()
                .region(effectiveRegion)
                .credentialsProvider(credentialsProvider)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();

        this.mapper = (mapper != null) ? mapper : new ObjectMapper();
    }

    /**
     * Iterates all .json files under prefix.
     *
     * Continues even if one file fails.
     */
    public S3IterationResult forEachJson(String bucket, String prefix, BiConsumer<String, JsonNode> consumer) {
        int filesSeen = 0;
        int jsonProcessed = 0;
        int failed = 0;

        String effectivePrefix = (prefix == null) ? "" : prefix.trim();

        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(effectivePrefix)
                .build();

        ListObjectsV2Iterable pages = s3.listObjectsV2Paginator(listReq);

        for (software.amazon.awssdk.services.s3.model.ListObjectsV2Response page : pages) {
            if (page.contents() == null) {
                continue;
            }

            for (S3Object obj : page.contents()) {
                final String key = obj.key();
                if (key == null || !key.endsWith(".json")) {
                    continue;
                }

                filesSeen++;

                ResponseInputStream<GetObjectResponse> in = null;
                try {
                    in = s3.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
                    JsonNode node = mapper.readTree((InputStream) in);

                    // Consumer may throw; still "continue even if one fails"
                    consumer.accept(key, node);
                    jsonProcessed++;
                }
                catch (Exception ex) {
                    failed++;
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (Exception ignore) {
                            // swallow (transport-only)
                        }
                    }
                }
            }
        }

        return new S3IterationResult(filesSeen, jsonProcessed, failed);
    }

    @Override
    public void close() {
        try {
            s3.close();
        }
        catch (Throwable ignore) {
            // never throw
        }
    }
}
