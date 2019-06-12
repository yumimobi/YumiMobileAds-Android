package com.yumi.android.mobile.ads.utils.entity;

import java.util.ArrayList;
import java.util.List;

public class DownloadListItem {

    private String id;
    private String downloadid;
    private String resurl;
    private String path;
    private int status;
    /**
     * 下载完成
     */
    private List<String> downloadedTrackerUrl;

    public DownloadListItem() {
    }

    public DownloadListItem(
            String downloadid,
            String id,
            String resurl,
            String path,
            int status, List<String> downloadedTrackerUrl) {
        this.id = id;
        this.downloadid = downloadid;
        this.resurl = resurl;
        this.path = path;
        this.status = status;
        this.downloadedTrackerUrl = downloadedTrackerUrl;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloadid() {
        return downloadid;
    }

    public void setDownloadid(String downloadid) {
        this.downloadid = downloadid;
    }

    public String getResurl() {
        return resurl;
    }

    public void setResurl(String resurl) {
        this.resurl = resurl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDownloadedTrackerUrl() {
        return downloadedTrackerUrl;
    }

    public void setDownloadedTrackerUrl(List<String> downloadedTrackerUrl) {
        this.downloadedTrackerUrl = downloadedTrackerUrl;
    }

    public String getDownloadedTrackerUrlS() {
        try {
            if (downloadedTrackerUrl.size() == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (String url : downloadedTrackerUrl) {
                sb.append("," + url);
            }
            return sb.toString().substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setDownloadedTrackerUrlS(String downloadedTrackerUrl) {
        try {
            if (downloadedTrackerUrl.length() > 0) {
                String[] downloadedTrackers= downloadedTrackerUrl.split(",");
                if(downloadedTrackers.length >0){
                    this.downloadedTrackerUrl = new ArrayList<>();
                    for(String url : downloadedTrackers){
                        this.downloadedTrackerUrl.add(url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
