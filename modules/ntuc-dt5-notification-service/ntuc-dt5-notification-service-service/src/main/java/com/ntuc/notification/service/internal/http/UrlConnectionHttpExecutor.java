package com.ntuc.notification.service.internal.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlConnectionHttpExecutor implements HttpExecutor {

    @Override
    public HttpResponse execute(
            String method,
            String url,
            Map<String, String> headers,
            byte[] body,
            int connectTimeoutMs,
            int readTimeoutMs)
        throws IOException {

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);

            if (connectTimeoutMs > 0) {
                conn.setConnectTimeout(connectTimeoutMs);
            }
            if (readTimeoutMs > 0) {
                conn.setReadTimeout(readTimeoutMs);
            }

            if (headers != null) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    conn.setRequestProperty(e.getKey(), e.getValue());
                }
            }

            if (body != null && body.length > 0) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body);
                }
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            String respBody = readStreamSafe(is);

            return new HttpResponse(code, respBody);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String readStreamSafe(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining());
        }
    }
}
