package com.yumi.android.mobile.ads.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class DeeplinkUtils {
    public static boolean isDeepLink(final String url) {
        return !isHttpUrl(url);
    }

    public static boolean deviceCanHandleIntent(final Context context, final Intent intent) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            return !activities.isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isHttpUrl(final String url) {
        if (url == null) {
            return false;
        }
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return true;
        }
        return false;
    }
}
