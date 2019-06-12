package com.yumi.android.mobile.ads.publish.enumbean;

import java.util.Locale;

public enum ErrorCode {

    /**
     * <p> Code failed.
     */
    CODE_FAILED("0"),

    /**
     * <p> Code success.
     */
    CODE_SUCCESS("1"),

    /**
     * <p> The error of SDK internal.
     */
    ERROR_INTERNAL("2"),

    /**
     * <p> The error of error network . Maybe you need VPN or something of kind.
     */
    ERROR_NETWORK_ERROR("3"),

    /**
     * <p> The request success but there is no ad return.
     */
    ERROR_NO_FILL("4"),
    /**
     * <p> Request has non response over limit time.
     */
    ERROR_NON_RESPONSE("5");


    private String code;

    ErrorCode(String code) {
        this.code = code;
    }

    /**
     * Get the ERROR code String.
     *
     * @return code String
     */
    public String getCode() {
        return code;
    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "{\n\tYumiMedia error: %s}", name());
    }
    }

