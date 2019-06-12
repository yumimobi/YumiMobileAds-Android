package com.yumi.android.mobile.ads.publish.enumbean;

public enum YumiGDPRStatus {
    PERSONALIZED("1"),
    NON_PERSONALIZED("0"),
    UNKNOWN("");

    private String value;

    YumiGDPRStatus(String v) {
        value = v;
    }

    public String getGDPRValue() {
        return value;
    }
}
