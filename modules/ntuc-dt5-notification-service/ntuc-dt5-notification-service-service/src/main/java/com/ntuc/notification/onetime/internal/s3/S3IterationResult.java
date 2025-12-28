package com.ntuc.notification.onetime.internal.s3;

import java.io.Serializable;

/**
 * Tiny result for S3 iteration.
 */
public class S3IterationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int filesSeen;
    private final int jsonProcessed;
    private final int failed;

    public S3IterationResult(int filesSeen, int jsonProcessed, int failed) {
        this.filesSeen = filesSeen;
        this.jsonProcessed = jsonProcessed;
        this.failed = failed;
    }

    public int getFilesSeen() {
        return filesSeen;
    }

    public int getJsonProcessed() {
        return jsonProcessed;
    }

    public int getFailed() {
        return failed;
    }

    @Override
    public String toString() {
        return "S3IterationResult{filesSeen=" + filesSeen +
                ", jsonProcessed=" + jsonProcessed +
                ", failed=" + failed + "}";
    }
}
