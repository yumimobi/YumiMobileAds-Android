package com.yumi.android.mobile.ads.beans;

import com.google.gson.annotations.SerializedName;

public class YumiLinkBean {

    private String url;
    private String clicktracker;
    private int type;
    @SerializedName("fallback_url")
    private String fallbackUrl;
    @SerializedName("fallback_action")
    private int fallbackAction;

    public String getUrl() {
        return url;
    }

    public String getClicktracker() {
        return clicktracker;
    }

    public int getType() {
        return type;
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }

    public int getFallbackAction() {
        return fallbackAction;
    }

}
