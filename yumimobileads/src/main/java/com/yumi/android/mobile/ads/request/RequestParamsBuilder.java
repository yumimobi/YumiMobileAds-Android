package com.yumi.android.mobile.ads.request;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import com.yumi.android.mobile.ads.publish.YumiSetting;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.device.PackageInfoGetter;
import com.yumi.android.mobile.ads.utils.device.PhoneInfoGetter;
import com.yumi.android.mobile.ads.utils.device.WindowSizeUtils;
import com.yumi.android.mobile.ads.utils.device.YumiGooglePlayServiceCheckUtils;
import com.yumi.android.mobile.ads.utils.entity.ADSize;
import com.yumi.android.mobile.ads.utils.network.NetworkStatusHandler;


import org.json.JSONArray;
import org.json.JSONObject;

import static com.yumi.android.mobile.ads.utils.device.WindowSizeUtils.dip2px;

public class RequestParamsBuilder {

    public static final String TAG = "RequestParamUtils";
    private static final boolean onoff = true;

    /**
     * 构建Geo对象请求参数
     */
    private static JSONObject buildGeoParam(final Activity activity) {
        JSONObject geoJson = new JSONObject();
        try {
            geoJson.put("lat", 0.0f);
            geoJson.put("lon", 0.0f);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildGeoParam error：", e, onoff);
        }
        return geoJson;
    }

    /**
     * 构建App对象请求参数
     */
    public static JSONObject buildAppParam(final Activity activity, final String id) {
        JSONObject appjson = new JSONObject();
        try {
            appjson.put("id", id);
            PackageManager packageManager = activity.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), 0);
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            appjson.put("name", (String) packageManager.getApplicationLabel(applicationInfo));
            appjson.put("bundle", activity.getPackageName());
            appjson.put("ver", PackageInfoGetter.getAppVersionName(packageManager, activity.getPackageName()));
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildAppParam error：", e, onoff);
        }
        return appjson;
    }

    /**
     * 构建Device对象请求参数
     */
    public static JSONObject buildDeviceParam(final Activity activity) {
        JSONObject dvjson = new JSONObject();
        try {
            dvjson.put("model", PhoneInfoGetter.getModel());
            dvjson.put("make", PhoneInfoGetter.getBrand());
            dvjson.put("brand", PhoneInfoGetter.getManufacturer());
            dvjson.put("plmn", PhoneInfoGetter.getPLMN(activity));
            dvjson.put("adt", 1);
            dvjson.put("connection_type", NetworkStatusHandler.getConnectionType(activity));
            dvjson.put("carrier", PhoneInfoGetter.getOperatorType(activity));
            dvjson.put("orientation", PhoneInfoGetter.getScreenMode(activity));
            String mac = PhoneInfoGetter.getMAC(activity);
            dvjson.put("mac", mac);
            dvjson.put("imei", PhoneInfoGetter.getDeviceID(activity));
            dvjson.put("imsi", PhoneInfoGetter.getIMSI(activity));
            dvjson.put("android_id", PhoneInfoGetter.getAndroidID(activity));
            dvjson.put("android_adid ", YumiGooglePlayServiceCheckUtils.getGooglePlayID(activity));
            dvjson.put("idfa", "");
            dvjson.put("openudid", "");
            dvjson.put("local", PhoneInfoGetter.getLanguage());
            dvjson.put("os_type", "android");
            dvjson.put("os_version", PhoneInfoGetter.getSysVersion());
            dvjson.put("screen", buildScreenParam(activity));
            dvjson.put("geo", buildGeoParam(activity));

        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildDeviceParam error：", e, onoff);
        }
        return dvjson;
    }

    /**
     * 构建screen对象请求参数
     */
    private static JSONObject buildScreenParam(final Activity activity) {
        JSONObject screenJson = new JSONObject();
        try {
            int[] screen = WindowSizeUtils.getRealSize(activity);
            screenJson.put("w", screen[0]);
            screenJson.put("h", screen[1]);
            screenJson.put("dpi", PhoneInfoGetter.getDisplayDensityDpi(activity));
            screenJson.put("pxratio", PhoneInfoGetter.getDisplayDensity(activity));
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildScreenParam error：", e, onoff);
        }
        return screenJson;
    }

    /**
     * 构建screen对象请求参数
     */
    public static JSONArray buildAdsParam(final Activity activity, AdType adType, String placementID, ADSize adSize) {
        JSONArray ads = new JSONArray();
        try {

            JSONArray inventory_types = new JSONArray();
            inventory_types.put(1);
            inventory_types.put(2);
            inventory_types.put(4);
            inventory_types.put(5);

            JSONObject adJson = new JSONObject();
            int[] size = calculateBannerSize(activity, adType, adSize);
            adJson.put("type", adType.getValue());
            adJson.put("place_id", placementID);
            adJson.put("w", size[0]);
            adJson.put("h", size[1]);
            adJson.put("inventory_types", inventory_types);
            ads.put(adJson);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildAdsParam error：", e, onoff);
        }
        return ads;
    }

    private static int[] calculateBannerSize(Activity activity,AdType adType, ADSize adSize) {
        if(adType == AdType.TYPE_BANNER){
            int width = adSize.getWidth();
            int height = adSize.getHeight();
            return new int[]{width, height};
        }else if(adType == AdType.TYPE_INTERSTITIAL){
            if(WindowSizeUtils.isPortrait(activity)){
                int width = adSize.getWidth();
                int height = adSize.getHeight();
                return new int[]{width, height};
            }else{
                int width = adSize.getHeight();
                int height= adSize.getWidth();
                return new int[]{width, height};
            }
        }

        return new int[]{0, 0};
    }


    /**
     * 构建User对象请求参数
     */
    public static JSONObject buildUserParam() {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("ext", buildUserExtParam());
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildUserParam error：", e, onoff);
        }
        return userJson;
    }

    /**
     * 构建UserExt对象请求参数
     */
    private static JSONObject buildUserExtParam() {
        JSONObject userExtJson = new JSONObject();
        try {
            userExtJson.put("consent", YumiSetting.isConsent() ? "yes" : "no");
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildUserExtParam error：", e, onoff);
        }
        return userExtJson;
    }

    /**
     * 构建Regs对象请求参数
     */
    public static JSONObject buildRegsParam() {
        JSONObject userJson = new JSONObject();
        try {

            userJson.put("coppa", YumiSetting.getCoppa());
            userJson.put("ext", buildRegsExtParam());
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildRegsParam error：", e, onoff);
        }
        return userJson;
    }

    /**
     * 构建RegsExt对象请求参数
     */
    private static JSONObject buildRegsExtParam() {
        JSONObject userExtJson = new JSONObject();
        try {
            userExtJson.put("gdpr", YumiSetting.getGdpr());
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildRegsExtParam error：", e, onoff);
        }
        return userExtJson;
    }
}