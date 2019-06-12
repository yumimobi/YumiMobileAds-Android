package com.yumi.android.mobile.ads.utils.other;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.io.File;
import java.util.List;

/**
 * 启动常用的第三方的app，eg：浏览器、联系人、短信
 *
 * @author glzlaohuai
 * @date 2014-6-9
 */
public class ThirdAppStarter {

    private static final String TAG = "ThirdAppStarter";

    /**
     * 是否有应用能处理这个intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isSafeToHandleThisIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

    /**
     * 启动浏览器
     *
     * @param context
     * @param url
     */
    public static boolean startBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isSafeToHandleThisIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }


    /**
     * 启动app安装应用
     *
     * @param context
     * @return
     */
    public static boolean startAppInstall(Context context, String apkFilePath) {
        try {
            Log.e(TAG, "startAppInstall start");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                ZplayDebug.i_m(TAG, "Android版本小于等于7.0，直接安装", true);
                data = Uri.fromFile(new File(apkFilePath));
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                ZplayDebug.i_m(TAG, "当前安装文件地址：" + apkFilePath, true);
                context.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "startAppInstall apkFilePath error : ", e);
        }
        return false;
    }

    /**
     * 启动app安装应用
     *
     * @param context
     * @return
     */
    public static boolean startAppInstall(Context context, Uri dataUri) {
        try {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(dataUri, "application/vnd.android.package-archive");
            // 下载apk时没有使用.apk作为后缀会导致下载包不可安装，抛出异常
            context.startActivity(install);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "startAppInstall Uri error : ", e);
        }
        return false;
    }

    public static void startApp(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "startApp error : ", e);
        }
    }

}
