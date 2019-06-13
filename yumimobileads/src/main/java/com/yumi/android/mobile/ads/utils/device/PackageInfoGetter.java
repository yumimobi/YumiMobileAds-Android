package com.yumi.android.mobile.ads.utils.device;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * get app package info
 * @author glzlaohuai
 */
public final class PackageInfoGetter {

    /**
     * App verName
     *
     * @param pm
     * @param pkg
     * @return
     */
    public static String getAppVersionName(PackageManager pm, String pkg) {
        try {
            PackageInfo packinfo = pm.getPackageInfo(pkg, 0);
            return packinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * App verCode
     *
     * @param pm
     * @param pkg
     * @return
     */
    public static int getAppVersionCode(PackageManager pm, String pkg) {
        try {
            PackageInfo packinfo = pm.getPackageInfo(pkg, 0);
            return packinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
