package com.yumi.android.mobile.ads.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.yumi.android.mobile.ads.utils.ZplayDebug;


public final class NetworkStatusHandler {

    private static final String TAG = "NetworkStatusHandler";
    private static final boolean onoff = true;

    public static boolean isNetWorkAvaliable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info == null) {
                    return false;
                } else {
                    for (NetworkInfo nf : info) {
                        if (nf.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     *
     * @param context
     * @return 0:2G,1:3G,2:4G,3:WIFI
     */
    public static String getConnectionType(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            String typeName = "unknown";
            if (networkInfo != null) {
                typeName = networkInfo.getTypeName();
                if (typeName.trim().equalsIgnoreCase("wifi")) {
                    return "wifi";
                }
                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                int type = manager.getNetworkType();
                System.err.println(type);
                switch (type) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2g";
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "3g";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4g";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ZplayDebug.e_m(TAG, "getConnectionType error：", e, onoff);
        }
        return "";
    }


}
