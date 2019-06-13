package com.yumi.android.mobile.ads.publish;

import android.support.annotation.NonNull;


import com.yumi.android.mobile.ads.publish.enumbean.ErrorCode;

import java.util.Locale;

/**
 * Description:
 * <p>
 */
public class AdError {
    private final ErrorCode mErrorCode;
    private String mErrorMessage;

    public AdError(@NonNull ErrorCode errorCode) {
        mErrorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    @NonNull
    public ErrorCode getErrorCode() {
        return mErrorCode;
    }

    public String getCode() {
        return getErrorCode().getCode();
    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "{\n\tYumiMedia error: %s, \n\textraMsg: %s \n}", getErrorCode().name(), getErrorMessage());
    }
}
