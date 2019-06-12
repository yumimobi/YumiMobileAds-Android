package com.yumi.android.mobile.ads.utils.module.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.database.DownloadDB;
import com.yumi.android.mobile.ads.utils.entity.DownloadListItem;
import com.yumi.android.mobile.ads.utils.file.DownloadHandler;
import com.yumi.android.mobile.ads.utils.file.FileHandler;
import com.yumi.android.mobile.ads.utils.other.ThirdAppStarter;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class ADReceiver extends BroadcastReceiver {

    private static final boolean onoff = true;
    private final static String TAG = "ADReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                onDownload(context, intent);
            }
           /* else if (Intent.ACTION_PACKAGE_ADDED.contentEquals(action)) {
                onInstall(context, intent);
            }*/
        } catch (Exception e) {
        }
    }

    /**
     * 下载完成处理
     *
     * @param context
     * @param intent
     */
    @SuppressLint("NewApi")
    private void onDownload(Context context, Intent intent) {
        Log.e(TAG, "onDownload start");
        ZplayDebug.v_m(TAG, "接收到了DownloadComplete的广播...", onoff);
        try {
            long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            ZplayDebug.v_m(TAG, "该次下载任务ID：" + downloadID, onoff);
            DownloadListItem item = DownloadDB.getInstance(context).selectData("downloadid", String.valueOf(downloadID));
            if (item == null) {
                ZplayDebug.v_m(TAG, "非本应用下载，无操作", onoff);
            } else {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                if (downloadManager == null) {
                    ZplayDebug.e_m(TAG, "onDownload: download manage is null", onoff);
                    return;
                }
                Uri dataUri;
                if (Build.VERSION.SDK_INT > 23) {
                    dataUri = downloadManager.getUriForDownloadedFile(downloadID);
                } else {
                    File f = FileHandler.getDownloadFile(downloadManager, downloadID);
                    if (f == null) {
                        ZplayDebug.e_m(TAG, "onDownload apk file not found", onoff);
                        return;
                    }
                    dataUri = Uri.fromFile(f);
                }
                DownloadHandler.handleDownloadComplete(context, String.valueOf(downloadID));
                ThirdAppStarter.startAppInstall(context, dataUri);
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "onDownload error:", e, onoff);
        }
    }


//    /**
//     * 安装完成处理
//     *
//     * @param context
//     * @param intent
//     */
//    private void onInstall(Context context, Intent intent) {
//        try {
//            ZplayDebug.v_s(TAG, "接收到了InstallComplete的广播...", onoff);
//            String pkg = intent.getDataString().split(":")[1];
//            ZplayDebug.v_s(TAG, "监听到有应用安装完成，应用包名:" + pkg + "，启交给ReportService进行处理...", onoff);
//            Intent serviceIntent = new Intent(context, YumiAdsEventService.class);
//            serviceIntent.setAction(YumiConstants.ACTION_REPORT_SELF);
//            serviceIntent.putExtra("pkg", pkg);
//            serviceIntent.putExtra(YumiAdsEventService.EXTRA_NAME_COMMAND, YumiAdsEventService.COMMAND_OPENPKG);
//            context.startService(serviceIntent);
//        } catch (Exception e) {
//        }
//    }

}
