package com.yumi.android.mobile.ads.beans;

import com.yumi.android.mobile.ads.utils.other.NullCheckUtils;

import java.util.List;

public final class YumiResponseBean {

    private int result;
    private String msg;
    private List<YumiAdBean> ads;
    private String cur;


    public int getResult() {
        return result;
    }


    public String getMsg() {
        return msg;
    }

    public List<YumiAdBean> getAds() {
        return ads;
    }

    public String getCur() {
        return cur;
    }

    public YumiAdBean getFirstAd(){
        return NullCheckUtils.isEmptyCollection(ads) ? null : ads.get(0);
    }
}
