package com.hcaptcha.sdk;

import android.webkit.JavascriptInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcaptcha.sdk.tasks.OnFailureListener;
import com.hcaptcha.sdk.tasks.OnLoadedListener;
import com.hcaptcha.sdk.tasks.OnSuccessListener;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * The JavaScript Interface which bridges the js and the java code
 */
@Data
@AllArgsConstructor
class HCaptchaJSInterface implements Serializable {

    public static final String JS_INTERFACE_TAG = "JSInterface";

    private final HCaptchaConfig hCaptchaConfig;

    private final OnLoadedListener onLoadedListener;

    private final OnSuccessListener<HCaptchaTokenResponse> onSuccessListener;

    private final OnFailureListener onFailureListener;

    HCaptchaJSInterface(HCaptchaConfig hCaptchaConfig, OnLoadedListener onLoadedListener, OnSuccessListener<HCaptchaTokenResponse> onSuccessListener, OnFailureListener onFailureListener) {
        this.hCaptchaConfig = hCaptchaConfig;
        this.onLoadedListener = onLoadedListener;
        this.onSuccessListener = onSuccessListener;
        this.onFailureListener = onFailureListener;
    }

    @JavascriptInterface
    public String getConfig() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.hCaptchaConfig);
    }

    @JavascriptInterface
    public void onPass(final String token) {
        onSuccessListener.onSuccess(new HCaptchaTokenResponse(token));
    }

    @JavascriptInterface
    public void onError(final int errCode) {
        final HCaptchaError error = HCaptchaError.fromId(errCode);
        onFailureListener.onFailure(new HCaptchaException(error));
    }

    @JavascriptInterface
    public void onLoaded() {
        this.onLoadedListener.onLoaded();
    }

}
