package com.yumi.android.mobile.ads.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.utils.display.DownloadAPkBrowserBuilder;
import com.yumi.android.mobile.ads.utils.file.DownloadHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/9/21.
 */

public class ZplayHandlerClickActionUtils {
    private static String TAG = "ZplayHandlerClickActionUtils";
    private static boolean onoff = true;

    public static void handleClickAction(YumiAdBean responseAd, Activity activity, View view, AdType type) {
        handleClickAction(responseAd, activity, view, type, null);
    }

    public static void handleClickAction(final YumiAdBean responseAd, final Activity activity, final View view, final AdType type, final DialogInterface.OnDismissListener onDismissListener) {
        int clickType = responseAd.getAction();

        switch (clickType) {
            case Constants.actiontype.ACTION_TYPE_DOWNLOAD: // 点击下载
                ZplayDebug.v_m(TAG, "点击下载", onoff);
                handleClickDownload(responseAd, activity, type);
                break;
            case Constants.actiontype.ACTION_TYPE_EXPLORER: // 打开浏览器
                ZplayDebug.v_m(TAG, "点击打开网页", onoff);
                DownloadAPkBrowserBuilder.openDownloadAPKBrowser(activity, responseAd, onDismissListener);
                break;
            case Constants.actiontype.ACTION_TYPE_OPEN_APP_STORE:
                // 需求：判断 downloadPackageName 包名是否有值，有值打开 appStorePackage 代表的应用市场，如果该市场无法打开，则用默认应用市场打开，
                // 均失败的话，使用使用系统浏览器打开应用
                if (!TextUtils.isEmpty(responseAd.getStore_bundle()) && !TextUtils.isEmpty(responseAd.getApp_bundle())) {
                    if (openAppMarket(activity, responseAd.getStore_bundle(), responseAd.getApp_bundle())) {
                        ZplayDebug.v_m(TAG, "already opened app store", onoff);
                        break;
                    }
                }
                ZplayDebug.v_m(TAG, String.format("open app store failed, appStorePackage: %s, package: %s, delivery to default operation.", responseAd.getStore_bundle(), responseAd.getApp_bundle()), onoff);
            default:
                ZplayDebug.v_m(TAG, "点击打开系统浏览器", onoff);
                // 打开系统浏览器
                Uri uri = Uri.parse(responseAd.getTarget_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                PackageManager manager = activity.getPackageManager();
                List<ResolveInfo> activities = manager.queryIntentActivities(
                        intent, PackageManager.MATCH_DEFAULT_ONLY);
                ZplayDebug.v_m(TAG, "总共有" + activities.size() + "个打浏览器可以打开 ", onoff);
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    ZplayDebug.v_m(TAG, "无法找到系统浏览器，使用内置浏览器", onoff);
                    DownloadAPkBrowserBuilder.openDownloadAPKBrowser(activity, responseAd, onDismissListener);
                }
                break;
        }
    }

    private static void handleClickDownload(final YumiAdBean responseBid, final Activity activity, final AdType adtype) {
            ZplayDebug.v_m(TAG, "点击下载", onoff);
            DownloadHandler.startDownload(activity, responseBid, false);
    }



    /**
     * 根据投放服务传回的应用包名与url打开应用市场
     *
     * @param context        context
     * @param marketPackages 应用市场包列表
     * @param url            携带应用包名的链接
     * @return 如果打开成功则返回true
     */
    public static boolean openAppMarket(Context context, List<String> marketPackages, String url) {
        final String appPackage = getPackageNameByUrl(url);
        if (TextUtils.isEmpty(appPackage)) {
            ZplayDebug.d_m(TAG, "cannot found app package name", onoff);
            return false;
        }
        final String appDetailsUri = "market://details?id=" + appPackage;
        if (marketPackages != null) {
            for (String market : marketPackages) {
                try {
                    // 使用指定应用市场打开应用
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appDetailsUri));
                    intent.setPackage(market);
                    context.startActivity(intent);
                    ZplayDebug.d_m(TAG, "opened app with " + market + " to " + appPackage, onoff);
                    return true;
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "open app market failed: marketPackage: " + market + " package: " + appPackage, e, onoff);
                }
            }
        }
        try {
            // 使用默认应用市场打开应用
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appDetailsUri));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "open app market jump with location error", e, onoff);
            return false;
        }

    }

    public static boolean openAppMarket(Context context, String appMarketPackage, String appPackage) {
        if (TextUtils.isEmpty(appPackage)) {
            ZplayDebug.d_m(TAG, "app package is null", onoff);
            return false;
        }

        final List<String> appMarketList = new ArrayList<>(2);
        if (!TextUtils.isEmpty(appMarketPackage)) {
            appMarketList.add(appMarketPackage);
        }
        return openAppMarket(context, appMarketList, "id=" + appPackage);
    }

    private static String getPackageNameByUrl(String url) {
        try {
            return url.substring(url.indexOf("=") + 1).trim();
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getPackageNameByUrl error:", e, onoff);
        }
        return "";
    }
}
