package com.yumi.android.mobile.ads.utils.template;

import android.content.Context;

import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.utils.assets.YumiAssetsReader;

public class YumiTemplate {

    public static String getYumiBannerTemplate(Context context, YumiAdBean adBean) {

        String html = null;
        if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_IMAGE) {
            String resourceName = "banner-image.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##IMG-SRC-URL##", adBean.getImage_url());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());

        } else if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_IMAGE_TEXT) {
            String resourceName = "banner-image-text.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##IMG-SRC-URL##", adBean.getImage_url());
            html = html.replace("##TITLE##",adBean.getTitle());
            html = html.replace("##DESC##",adBean.getDesc());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());
        } else if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_TEXT) {
            String resourceName = "banner-text.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##TITLE##",adBean.getTitle());
            html = html.replace("##DESC##",adBean.getDesc());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());
        }else if(adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_HTML){
            html = adBean.getHtml_snippet();
        }
        return html;
    }

    public static String getYumiInterstitialTemplate(Context context, YumiAdBean adBean) {

        String html = null;
        if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_IMAGE) {
            String resourceName = "interstitial-image.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##IMG-SRC-URL##", adBean.getImage_url());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());

        } else if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_IMAGE_TEXT) {
            String resourceName = "interstitial-image-text.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##IMG-SRC-URL##", adBean.getImage_url());
            html = html.replace("##TITLE##",adBean.getTitle());
            html = html.replace("##DESC##",adBean.getDesc());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());
        } else if (adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_TEXT) {
            String resourceName = "interstitial-text.html";
            html = YumiAssetsReader.getFromAssets(context, resourceName);
            html = html.replace("##IMG-SRC-URL##", adBean.getImage_url());
            html = html.replace("##TITLE##",adBean.getTitle());
            html = html.replace("##DESC##",adBean.getDesc());
            html = html.replace("##A-HREF-URL##",adBean.getTarget_url());
            html = html.replace("##YUMI_LOGO_URL##",adBean.getLogo_url());
        }else if(adBean.getInventory_type() == Constants.inventorytype.INVENTORY_TYPE_HTML){
            html = adBean.getHtml_snippet();
        }
        return html;
    }
}
