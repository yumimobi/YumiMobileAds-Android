package com.yumi.android.mobile.ads.utils.device;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;


import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.util.Locale;

/**
 * 获取手机信息
 *
 * @author laohuai
 */
public final class PhoneInfoGetter {
    private static String TAG = "PhoneInfoGetter";
    private static boolean onoff = true;
    private static String ua;

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机的型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取android版本号int
     *
     * @return
     */
    public static int getAndroidVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机的生产厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取android版本号int
     *
     * @return
     */
    public static int getAndroidSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取imei信息 为空时 返回androidID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = getAndroidID(context);
            }
            return imei;
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取 imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            return imei == null ? "" : imei;
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取imsi信息
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "";
            }
            String imsi = telephonyManager.getSubscriberId();
            if (imsi == null) {
                imsi = "";
            }
            return imsi;
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取iccid
     *
     * @param context
     * @return
     */
    public static String getICCID(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "";
            }
            String iccid = telephonyManager.getSimSerialNumber();
            if (iccid != null && iccid.length() > 0) {
                return iccid;
            }
            return "";
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取设备屏幕分辩密度
     *
     * @param context
     * @return
     */

    public static float getDisplayDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取设备屏幕分辨率密度dpi
     *
     * @param context
     * @return
     */

    public static int getDisplayDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 获取手机屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 获取语言设置
     *
     * @return
     */
    public static String getLanguage() {
        return Locale.getDefault().toString();
    }

    /**
     * 获取国家设置
     *
     * @param context
     * @return
     */
    public static String getCountry(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "";
            }
            String zone = telephonyManager.getSimCountryIso();
            if (zone == null) {
                zone = "";
            }
            return zone;
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMAC(Context context) {
        try{

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info != null) {
            String mac = info.getMacAddress();
            if (mac == null) {
                mac = "";
            }
            return mac;
        } else {
            return "";
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机PLMN
     *
     * @param context
     * @return
     */
    public static String getPLMN(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (manager == null) {
                return "";
            }
            String plmn = manager.getSimOperator();
            if (plmn == null || plmn.equals("")) {
                return "";
            } else {
                if (plmn.length() > 6) {
                    plmn = plmn.split(",")[0].replace(",", "");
                }
                return plmn.replace(",", "");
            }
        } catch (Exception ex) {

        }
        return "";
    }

    public static String getMNC(Context context) {
        String plmn = getPLMN(context);
        if (plmn.length() >= 5) {
            return plmn.substring(3);
        }
        return "";
    }

    public static String getMCC(Context context) {
        String plmn = getPLMN(context);
        if (plmn.length() >= 5) {
            return plmn.substring(0, 3);
        }
        return "";
    }

    public static String getISOCountryCode(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return "";
            }
            String icc = null;
            if (tm != null) {
                icc = tm.getSimCountryIso();
            }
            return icc == null ? "" : icc;
        } catch (Exception ex) {

        }
        return "";
    }

    /**
     * 获取手机webview的userAgent
     *
     * @return
     */
    public static String getUserAgent(final Context context) {
        if (ua != null) {
            return ua;
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        WebView web = new WebView(context);
                        ua = web.getSettings().getUserAgentString();
                    } catch (Exception ignore) {
                    }
                }
            });
            return "";
        }
    }


    /**
     * 获取androidID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        String androidId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        return androidId == null ? "0000000000000000" : androidId;
    }

    public static boolean isChinaMoblie(Context context) {
        String imsi = getIMSI(context);
        if (imsi != null && imsi.length() > 0) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备类型
     *
     * @param context
     * @return 0:phone 1:pad
     */
    public static int getDeviceType(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double inch = Math.sqrt(Math.pow(dm.widthPixels, 2)
                + Math.pow(dm.heightPixels, 2))
                / (160 * dm.density);
        if (inch >= 8.0d) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取横竖屏的标示，约定1：竖屏 0：横屏
     *
     * @param context
     * @return
     */
    public static int getScreenMode(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 0;
        } else {
            return 1;
        }
    }

    public static boolean isFullScreen(Activity activity) {
        return ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0);
    }

    /**
     * 获取位置
     *
     * @param ctx
     * @return
     */
    public static PhoneInfoGetter.Location getLocation(Context ctx) {

        PhoneInfoGetter.Location loc = new PhoneInfoGetter.Location();
        loc.lat = "0.0";
        loc.lng = "0.0";
        loc.time = "0";
        return loc;
    }

    public static class Location {
        public String lng;
        public String lat;
        public String time;
    }

    /**
     * 获取运营商 46000 46002 46004 46007 代表中国移动 46001 46006 46009代表中国联通 46003 46005 46011
     * 代表中国电信 46020 代表中国铁通
     * 获取设备运营商
     *
     * @return ["中国电信CTCC":"1"]["中国联通CUCC:"3"]["中国移动CMCC":"0"]["other":"4"]
     */
    public static int getOperatorType(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = tm.getNetworkOperator();
            int opeType = 4;
            // 中国联通
            if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
                opeType = 3;
                // 中国移动
            } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
                opeType = 0;
                // 中国电信
            } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
                opeType = 1;
            }
            return opeType;
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getOperatorType error", e, onoff);
            return 4;
        }
    }
}
