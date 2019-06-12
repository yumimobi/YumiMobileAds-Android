package com.yumi.android.mobile.ads.constants;

import android.content.Intent;

/**
 * lgd on 2017/10/16.
 * 所有状态信息，维护原则，新加入的状态码与历史中的状态码不能重复
 */
public enum StatusCode {
    REQUEST_PARAMS_ERROR(1002, "request parameters error."),
    PRELOAD_NO_AD(2005, "no ad"),
    NO_CONNECTION_ERROR(2006, "no connection error"),
    TIMEOUT_ERROR(2007, "timeout error"),
    SERVER_ERROR(2008, "server error"),
    NETWORK_ERROR(5002, "network error"),
    // 未知
    UNKNOWN(-1, "UNKNOWN state");

    private String message;
    public int code;

    StatusCode(int code, String msg) {
        this.code = code;
        message = msg;
    }

    @Override
    public String toString() {
        return message;
    }

    private static final String name = StatusCode.class.getName();

    public void attachTo(Intent intent) {
        intent.putExtra(name, ordinal());
    }

    public static StatusCode detachFrom(Intent intent) {
        if (!intent.hasExtra(name)) {
            throw new IllegalStateException();
        }
        return values()[intent.getIntExtra(name, -1)];
    }
}
