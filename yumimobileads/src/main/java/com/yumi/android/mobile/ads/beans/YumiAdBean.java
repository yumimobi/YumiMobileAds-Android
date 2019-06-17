package com.yumi.android.mobile.ads.beans;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YumiAdBean {
     private String id;
     @SerializedName("place_id")
     private String placeId;
     private int action;
     @SerializedName("html_snippet")
     private String htmlSnippet;
     @SerializedName("image_url")
     private String imageUrl;
     private int w;
     private int h;

     @SerializedName("app_bundle")
     private String appBundle;
     @SerializedName("store_bundle")
     private String storeBundle;
     @SerializedName("app_ver")
     private String appVer;

     @SerializedName("target_url")
     private String targetUrl;

     @SerializedName("click_trackers")
     private List<String> clickTrackers;
     @SerializedName("imp_trackers")
     private List<String> impTrackers;
     @SerializedName("close_trackers")
     private List<String> closeTrackers;
     @SerializedName("app_download_trackers")
     private List<String> appDownloadTrackers;
     @SerializedName("app_download_finish_trackers")
     private List<String> appDownloadFinishTrackers;
     @SerializedName("app_activate_trackers")
     private List<String> appActivateTrackers;

     @SerializedName("refresh_interval")
     private int refreshInterval;
     @SerializedName("inventory_type")
     private int inventoryType;

     private String title;
     private String desc;
     @SerializedName("ssp_id")
     private String sspId;

     @SerializedName("download_file_name")
     private String downloadFileName;
     @SerializedName("file_size")
     private int fileSize;
     private float price;
     @SerializedName("ex_param")
     private String exParam;
     @SerializedName("ssp_ad_id")
     private String sspAdId;

     private YumiVideoBean video;
     @SerializedName("native")
     private YumiNativeBean nativeBean;

     private YumiZplayBean zplay;
     @SerializedName("logo_url")
     private String logoUrl;
     @SerializedName("fallback_url")
     private String fallbackUrl;
     @SerializedName("fallback_action")
     private int fallbackAction;

     public String getId() {
          return id;
     }

     public String getPlaceId() {
          return placeId;
     }

     public int getAction() {
          return action;
     }

     public String getHtmlSnippet() {
          return htmlSnippet;
     }

     public String getImageUrl() {
          return imageUrl;
     }

     public int getW() {
          return w;
     }

     public int getH() {
          return h;
     }

     public String getAppBundle() {
          return appBundle;
     }

     public void setAppBundle(String appBundle) {
          this.appBundle = appBundle;
     }

     public String getStoreBundle() {
          return storeBundle;
     }

     public String getAppVer() {
          return appVer;
     }

     public String getTargetUrl() {
          return targetUrl;
     }

     public void setTargetUrl(String targetUrl) {
          this.targetUrl = targetUrl;
     }

     public List<String> getClickTrackers() {
          return clickTrackers;
     }

     public List<String> getImpTrackers() {
          return impTrackers;
     }

     public List<String> getCloseTrackers() {
          return closeTrackers;
     }


     public List<String> getAppDownloadTrackers() {
          return appDownloadTrackers;
     }

     public List<String> getAppDownloadFinishTrackers() {
          return appDownloadFinishTrackers;
     }

     public List<String> getAppActivateTrackers() {
          return appActivateTrackers;
     }

     public int getRefreshInterval() {
          return refreshInterval;
     }

     public int getInventoryType() {
          return inventoryType;
     }

     public String getTitle() {
          return title;
     }

     public String getDesc() {
          return desc;
     }

     public String getSspId() {
          return sspId;
     }

     public String getDownloadFileName() {
          return downloadFileName;
     }

     public void setDownloadFileName(String downloadFileName) {
          this.downloadFileName = downloadFileName;
     }

     public int getFileSize() {
          return fileSize;
     }

     public float getPrice() {
          return price;
     }

     public String getExParam() {
          return exParam;
     }

     public String getSspAdId() {
          return sspAdId;
     }

     public YumiVideoBean getVideo() {
          return video;
     }

     public YumiNativeBean getNativeBean() {
          return nativeBean;
     }

     public YumiZplayBean getZplay() {
          return zplay;
     }

     public String getLogoUrl() {
          return logoUrl;
     }

     public String getFallbackUrl() {
          return fallbackUrl;
     }

     public int getFallbackAction() {
          return fallbackAction;
     }

}
