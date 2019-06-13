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
     * Brand
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * Model
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * Android version code
     *
     * @return
     */
    public static int getAndroidVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * Manufacturer
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * SysVersion
     *
     * @return
     */
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * if imei is null，get androidId
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
     * IMEI
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
     * IMSI
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
     * iccid
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
     * DisplayDensity
     *
     * @param context
     * @return
     */

    public static float getDisplayDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * DisplayDensityDpi
     *
     * @param context
     * @return
     */

    public static int getDisplayDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * DisplayMetrics
     *
     * @param context
     * @return
     */
    public static int[] getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * Language
     *
     * @return
     */
    public static String getLanguage() {
        return Locale.getDefault().toString();
    }


    /**
     * MAC
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
     * PLMN
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

    /**
     * UserAgent
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
     * AndroidID
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
     * DeviceType
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
     * ScreenMode，1：PORTRAIT 0：LANDSCAPE
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
    /**
     * Activity is Full Screen
     *
     * @return
     */
    public static boolean isFullScreen(Activity activity) {
        return ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0);
    }

    /**
     * OperatorType 46000 46002 46004 46007 CMCC 46001 46006 46009 CUCC 46003 46005 46011
     * CTCC
     * OperatorType
     *
     * @return ["CTCC":"1"]["CUCC:"3"]["CMCC":"0"]["other":"4"]
     */
    public static int getOperatorType(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String operator = tm.getNetworkOperator();
            int opeType = 4;
            // CUCC
            if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
                opeType = 3;
                // CMCC
            } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
                opeType = 0;
                // CTCC
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
