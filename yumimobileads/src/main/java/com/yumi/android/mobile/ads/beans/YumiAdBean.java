package com.yumi.android.mobile.ads.beans;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YumiAdBean {
     private String id;
     private String placeId;
     private int action;
     private String htmlSnippet;
     private String imageUrl;
     private int w;
     private int h;

     private String appBundle;
     private String storeBundle;
     private String appVer;

     private String targetUrl;

     private List<String> clickTrackers;
     private List<String> impTrackers;
     private List<String> closeTrackers;
     private List<String> appDownloadTrackers;
     private List<String> appDownloadFinishTrackers;
     private List<String> appActivateTrackers;


     private int refreshInterval;
     private int inventoryType;

     private String title;
     private String desc;
     private String sspId;

     private String downloadFileName;
     private int fileSize;
     private float price;
     private String exParam;
     private String sspAdId;

     private YumiVideoBean video;
     @SerializedName("native")
     private YumiNativeBean nativeBean;

     private YumiZplayBean zplay;
     private String logoUrl;
     private String fallbackUrl;
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
