package com.yumi.android.mobile.ads.beans;

import com.google.gson.annotations.SerializedName;

public class YumiZplayBean {

    @SerializedName("app_id")
    private String appId;
    @SerializedName("position_sid")
    private String positionSid;
    @SerializedName("app_secret")
    private String appSecret;
    @SerializedName("trans_data")
    private String transData;
    @SerializedName("deep_link_url")
    private String deepLinkUrl;

    public String getAppId() {
        return appId;
    }

    public String getPositionSid() {
        return positionSid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getTransData() {
        return transData;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

}
