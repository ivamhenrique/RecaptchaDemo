package com.hcaptcha.sdk;

import lombok.Data;

/**
 * Token response which contains the token string to be verified
 */
@Data
public class HCaptchaTokenResponse {

    private final String tokenResult;

    public HCaptchaTokenResponse(String tokenResult) {
        this.tokenResult = tokenResult;
    }

    public String getTokenResult() {
        return tokenResult;
    }
}
