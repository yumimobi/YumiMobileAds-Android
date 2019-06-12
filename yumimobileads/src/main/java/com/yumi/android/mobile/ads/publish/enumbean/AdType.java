package com.yumi.android.mobile.ads.publish.enumbean;

public enum AdType {


    TYPE_BANNER("0"),

    TYPE_INTERSTITIAL("1");

    private String type;

    AdType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getValue(){
        return Integer.valueOf(type);
    }

}
