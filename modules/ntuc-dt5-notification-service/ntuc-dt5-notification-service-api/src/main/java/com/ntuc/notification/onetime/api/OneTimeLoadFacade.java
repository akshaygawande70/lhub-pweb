package com.ntuc.notification.onetime.api;

/**
 * Facade for One-Time S3 load.
 *
 * <p>Design rules:</p>
 * <ul>
 *   <li>REST/resources MUST call this facade only (no direct calls to OneTimeS3LoadService).</li>
 *   <li>Input is the S3 path string received from REST: {@code s3://bucket/prefix}.</li>
 *   <li>Implementation must parse deterministically and delegate to a stable service method.</li>
 *   <li>No reflection. No method-name guessing.</li>
 * </ul>
 *
 * <p>Accepted formats:</p>
 * <ul>
 *   <li>{@code s3://bucket}</li>
 *   <li>{@code s3://bucket/}</li>
 *   <li>{@code s3://bucket/prefix}</li>
 *   <li>{@code s3://bucket/prefix/}</li>
 * </ul>
 */
public interface OneTimeLoadFacade {

    /**
     * Triggers one-time S3 load for the given S3 path.
     *
     * @param s3Path S3 path string formatted as {@code s3://bucket[/prefix]}
     * @throws IllegalArgumentException if {@code s3Path} is null/blank or not a valid {@code s3://} path
     * @throws RuntimeException wrapping execution failures from the underlying service
     */
    void executeS3Path(String s3Path);

}
