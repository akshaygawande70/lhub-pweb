package com.ntuc.notification.service.internal.http;

import java.io.IOException;
import java.util.Map;

public interface HttpExecutor {

    HttpResponse execute(
        String method,
        String url,
        Map<String, String> headers,
        byte[] body,
        int connectTimeoutMs,
        int readTimeoutMs
    ) throws IOException;
}
