package com.yumi.android.mobile.ads.utils.device;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.yumi.android.mobile.ads.utils.ZplayDebug;

public final class YumiGooglePlayServiceCheckUtils {

    private static final boolean onoff = true;
    private static final String TAG = "YumiGooglePlayServiceCheckUtils";
    private static String googleID = "";
    private static boolean adt = false;

    public final static String getGooglePlayID(final Context context) {
        if (googleID.equals("")
                && isGooglePlayIsAvailable(context)) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        googleID = AdvertisingIdClient.getAdvertisingIdInfo(
                                context).getId();
                    } catch (Exception e) {
                        ZplayDebug.e_m(TAG, "", e, onoff);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return googleID;
    }


    public static final boolean isGooglePlayIsAvailable(Context context) {
        try {
            int available = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context);
            if (available == ConnectionResult.SUCCESS) {
                return true;
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "", e, onoff);
        } catch (Error e) {
            ZplayDebug.e_m(TAG, "", e, onoff);
        }
        return false;
    }
}
