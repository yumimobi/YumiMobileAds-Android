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
        } catch (Exception e) {
        }
    }

    /**
     * download complete
     *
     * @param context
     * @param intent
     */
    @SuppressLint("NewApi")
    private void onDownload(Context context, Intent intent) {
        Log.e(TAG, "onDownload success");
        ZplayDebug.v_m(TAG, "Have download complete broadcast", onoff);
        try {
            long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            ZplayDebug.v_m(TAG, "downloadID：" + downloadID, onoff);
            DownloadListItem item = DownloadDB.getInstance(context).selectData("downloadid", String.valueOf(downloadID));
            if (item == null) {
                ZplayDebug.v_m(TAG, "downloadID not exist，don't work", onoff);
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
}
