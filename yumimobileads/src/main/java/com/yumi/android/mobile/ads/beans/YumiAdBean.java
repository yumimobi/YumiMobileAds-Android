package com.yumi.android.mobile.ads.beans;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YumiAdBean {
     private String id;
     private String place_id;
     private int action;
     private String html_snippet;
     private String image_url;
     private int w;
     private int h;

     private String app_bundle;
     private String store_bundle;
     private String app_ver;

     private String target_url;

     private List<String> click_trackers;
     private List<String> imp_trackers;
     private List<String> close_trackers;
     private List<String> app_download_trackers;
     private List<String> app_download_finish_trackers;
     private List<String> app_activate_trackers;


     private int refresh_interval;
     private int inventory_type;

     private String title;
     private String desc;
     private String ssp_id;

     private String download_file_name;
     private int file_size;
     private float price;
     private String ex_param;
     private String ssp_ad_id;

     private YumiVideoBean video;
     @SerializedName("native")
     private YumiNativeBean nativeBean;

     private YumiZplayBean zplay;
     private String logo_url;
     private String fallback_url;
     private int fallback_action;

     public String getId() {
          return id;
     }

     public String getPlace_id() {
          return place_id;
     }

     public int getAction() {
          return action;
     }

     public String getHtml_snippet() {
          return html_snippet;
     }

     public String getImage_url() {
          return image_url;
     }

     public int getW() {
          return w;
     }

     public int getH() {
          return h;
     }

     public String getApp_bundle() {
          return app_bundle;
     }

     public void setApp_bundle(String app_bundle) {
          this.app_bundle = app_bundle;
     }

     public String getStore_bundle() {
          return store_bundle;
     }

     public String getApp_ver() {
          return app_ver;
     }

     public String getTarget_url() {
          return target_url;
     }

     public void setTarget_url(String target_url) {
          this.target_url = target_url;
     }

     public List<String> getClick_trackers() {
          return click_trackers;
     }

     public List<String> getImp_trackers() {
          return imp_trackers;
     }

     public List<String> getClose_trackers() {
          return close_trackers;
     }


     public List<String> getApp_download_trackers() {
          return app_download_trackers;
     }

     public List<String> getApp_download_finish_trackers() {
          return app_download_finish_trackers;
     }

     public List<String> getApp_activate_trackers() {
          return app_activate_trackers;
     }

     public int getRefresh_interval() {
          return refresh_interval;
     }

     public int getInventory_type() {
          return inventory_type;
     }

     public String getTitle() {
          return title;
     }

     public String getDesc() {
          return desc;
     }

     public String getSsp_id() {
          return ssp_id;
     }

     public String getDownload_file_name() {
          return download_file_name;
     }

     public void setDownload_file_name(String download_file_name) {
          this.download_file_name = download_file_name;
     }

     public int getFile_size() {
          return file_size;
     }

     public float getPrice() {
          return price;
     }

     public String getEx_param() {
          return ex_param;
     }

     public String getSsp_ad_id() {
          return ssp_ad_id;
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

     public String getLogo_url() {
          return logo_url;
     }

     public String getFallback_url() {
          return fallback_url;
     }

     public int getFallback_action() {
          return fallback_action;
     }

}
