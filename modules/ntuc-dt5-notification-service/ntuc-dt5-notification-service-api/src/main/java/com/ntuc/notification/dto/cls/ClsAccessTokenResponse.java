package com.ntuc.notification.dto.cls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO representing CLS OAuth access token response.
 *
 * Pure API object:
 * - No OSGi annotations
 * - No runtime dependencies
 * - Safe for unit testing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsAccessTokenResponse {

    private String tokenType;
    private int expiresIn;
    private String accessToken;
    private String scope;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
