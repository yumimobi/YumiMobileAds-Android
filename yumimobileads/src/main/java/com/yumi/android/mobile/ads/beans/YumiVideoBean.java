package com.yumi.android.mobile.ads.beans;

import com.google.gson.annotations.SerializedName;

public class YumiVideoBean {

    private String url;
    @SerializedName("play_duration")
    private int playDuration;
    @SerializedName("player_start_trackers")
    private String playerStartTrackers;
    @SerializedName("player_end_trackers")
    private String playerEndTrackers;
    @SerializedName("target_page_show_trackers")
    private String targetPageShowTrackers;
    @SerializedName("target_page_click_trackers")
    private String targetPageClickTrackers;
    @SerializedName("target_page_close_trackers")
    private String targetPageCloseTrackers;

    public String getUrl() {
        return url;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public String getPlayerStartTrackers() {
        return playerStartTrackers;
    }

    public String getPlayerEndTrackers() {
        return playerEndTrackers;
    }

    public String getTargetPageShowTrackers() {
        return targetPageShowTrackers;
    }

    public String getTargetPageClickTrackers() {
        return targetPageClickTrackers;
    }

    public String getTargetPageCloseTrackers() {
        return targetPageCloseTrackers;
    }


}
