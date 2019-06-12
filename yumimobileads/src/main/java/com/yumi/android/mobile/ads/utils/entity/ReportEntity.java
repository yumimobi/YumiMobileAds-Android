package com.yumi.android.mobile.ads.utils.entity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReportEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> displayTrackerUrl;
    private List<String> clickTrackerUrl;
    /**
     * 记录视频当前播放进度，中断视频播放上报时，使用此值，如播放视频期间点击 home 键返回系统桌面，导致视频暂停，设置此值
     * 在落地页点击时，此值应为视频总长度
     */
    private int videoPosition;
    //以下变量为1.3.0.2新增
    /**
     * 点击区域
     */
    private Map<String, String> clickArea;

    public ReportEntity(
            List<String> displayTrackerUrl,
            List<String> clickTrackerUrl,
            Map<String, String> clickArea
    ) {

        this.displayTrackerUrl = displayTrackerUrl;
        this.clickTrackerUrl = clickTrackerUrl;
        this.clickArea = clickArea;
    }



    public String[] getDisplayTrackerUrl() {
        try {
            JSONArray ja = new JSONArray(displayTrackerUrl);
            int length = ja.length();
            String[] array = new String[length];
            for (int i = 0; i < array.length; i++) {
                array[i] = ja.getString(i);
            }
            return array;
        } catch (JSONException e) {
            return new String[]{};
        }
    }

    public void setDisplayTrackerUrl(List<String> displayTrackerUrl) {
        this.displayTrackerUrl = displayTrackerUrl;
    }

    public String[] getClickTrackerUrl() {
        try {
            JSONArray ja = new JSONArray(clickTrackerUrl);
            int length = ja.length();
            String[] array = new String[length];
            for (int i = 0; i < array.length; i++) {
                array[i] = ja.getString(i);
            }
            return array;
        } catch (JSONException e) {
            return new String[]{};
        }
    }

    public void setClickTrackerUrl(List<String> clickTrackerUrl) {
        this.clickTrackerUrl = clickTrackerUrl;
    }

    public Map<String, String> getClickArea() {
        return clickArea;
    }

    public void setClickArea(Map<String, String> clickArea) {
        this.clickArea = clickArea;
    }

    public int getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(int videoPosition) {
        this.videoPosition = videoPosition;
    }
}
