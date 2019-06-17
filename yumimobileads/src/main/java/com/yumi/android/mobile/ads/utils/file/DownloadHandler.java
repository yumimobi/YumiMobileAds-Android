package com.yumi.android.mobile.ads.utils.file;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.yumi.android.mobile.ads.utils.Reporter;
import com.yumi.android.mobile.ads.utils.entity.DownloadListItem;
import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.database.DownloadDB;
import com.yumi.android.mobile.ads.utils.device.PackageInfoGetter;
import com.yumi.android.mobile.ads.utils.entity.ReportEntity;
import com.yumi.android.mobile.ads.utils.other.ThirdAppStarter;

import java.io.File;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DownloadHandler {

    private static final boolean onoff = true;
    private final static String TAG = "DownloadHandler";

    public static void startDownload(Activity activity, YumiAdBean response, boolean reload) {
        try {
            // Check whether the version number of the app installed and installed on the current device is not less than the version number of the package to be downloaded. If it is, do not download it. If not, download it.
            String packageName = response.getAppBundle();
            if (packageName == null || "".equals(packageName) || "null".equals(packageName)) {
                packageName = String.valueOf(System.currentTimeMillis());
                response.setAppBundle(packageName);
            }
            String versionCode = response.getAppVer();
            String fileName = response.getDownloadFileName();
            if (fileName == null || "".equals(fileName) || "null".equals(fileName)) {
                fileName = response.getAppBundle() + "_" + versionCode + ".apk";
                response.setDownloadFileName(fileName);
            }
            String url = response.getTargetUrl();
            ZplayDebug.v_m(TAG, "download url" + url, onoff);

            packageName = packageName.trim();
            int installedAppVersion = PackageInfoGetter.getAppVersionCode(activity.getPackageManager(), packageName);
            ZplayDebug.v_m(TAG, "installed app package name：[" + packageName + "] app version：" + installedAppVersion + ",download app version：" + versionCode, onoff);

            // Check if it is in the download list
            DownloadDB db = DownloadDB.getInstance(activity);
            DownloadListItem item = db.selectData("resurl", response.getAppBundle());
            if (item == null) {
                handleDownloadStuff(activity, response);
            } else {
                ZplayDebug.v_m(TAG, "it is in download list ,check it is downloading...", onoff);
                String downloadID = item.getDownloadid();
                DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Query query = new Query();
                query.setFilterById(Long.parseLong(downloadID));

                Cursor cursor = dm.query(query);
                if (cursor.moveToFirst()) {
                    ZplayDebug.v_m(TAG, "it is not in download last，check download status", onoff);
                    String desc = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                    long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    final int PAUSED = DownloadManager.STATUS_PAUSED; // download paused
                    final int PENDING = DownloadManager.STATUS_PENDING; // download pending
                    final int RUNNING = DownloadManager.STATUS_RUNNING; // downloading
                    final int SUCCESSFUL = DownloadManager.STATUS_SUCCESSFUL; // download success

                    final int FAILED = DownloadManager.STATUS_FAILED; // download failed

                    ZplayDebug.v_m(TAG, "download status：<desc>=" + desc + ";<status>=" + status, onoff);
                    switch (status) {
                        case FAILED:
                            if (!reload) {
                                ZplayDebug.v_m(TAG, "download failed，retry download", onoff);
                                dm.remove(id);
                                startDownload(activity, response, true);
                            }
                            return;
                        case PAUSED:
                        case PENDING:
                        case RUNNING:
                            break;
                        case SUCCESSFUL:
                            ZplayDebug.v_m(TAG, "download success，check this apk file is a complete installation package", onoff);
                            String path_apk = item.getPath();
                            File file_apk = new File(path_apk);
                            if (file_apk.exists()) {
                                ZplayDebug.v_m(TAG, "file exists，check is complete", onoff);
                                PackageInfo packageInfo = getPackageInfo(activity, file_apk.getAbsolutePath());
                                if (packageInfo == null) {
                                    ZplayDebug.v_m(TAG, "The package download is incomplete, then delete the incomplete package and then download again...", onoff);
                                    if (file_apk.delete()) {
                                        DownloadDB.getInstance(activity).deleteData("id", String.valueOf(item.getId()));
                                        ZplayDebug.v_m(TAG, "download again...", onoff);
                                        handleDownloadStuff(activity, response);
                                    } else {
                                        ZplayDebug.v_m(TAG, "delete failed，don't download again...", onoff);
                                        Toast.makeText(activity, "Download [" + fileName + "] Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ZplayDebug.v_m(TAG, "start install App", onoff);
                                    ThirdAppStarter.startAppInstall(activity, file_apk.getAbsolutePath());
                                }
                            } else {
                                ZplayDebug.v_m(TAG, "download again", onoff);
                                DownloadDB.getInstance(activity).deleteData("id", String.valueOf(item.getId()));
                                handleDownloadStuff(activity, response);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    if (!reload) {
                        ZplayDebug.v_m(TAG, "not download,download again", onoff);
                        DownloadDB.getInstance(activity).deleteData("id", String.valueOf(item.getId()));
                        startDownload(activity, response, true);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "startDownload error : ", e, onoff);
        }
    }

    private static void handleDownloadStuff(Activity activity, YumiAdBean response) {
        String fileName = response.getDownloadFileName();

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(response.getTargetUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(fileName + " downloading...").setTitle(fileName);
        File apkDir = new File(FileHandler.getSDRootDIR(activity) + Constants.dir.DIR_APK);
        if (apkDir.exists() || apkDir.mkdirs()) {
            File file = new File(apkDir, fileName);
            request.setDestinationUri(Uri.fromFile(file));
            try {
                long downloadID = downloadManager.enqueue(request);

                reportDownloadStart(activity, response);
                Toast.makeText(activity, "Start Download App：" + fileName, Toast.LENGTH_SHORT).show();
                ZplayDebug.v_m(
                        TAG,
                        "Start Download App：" + fileName + "，adID：" + response.getId() + "，App Name：" + fileName + "，Download id"
                                + downloadID, onoff);
                ZplayDebug.v_m(TAG, "Save download completed report url：" + response.getAppDownloadFinishTrackers(), onoff);
                DownloadListItem item = new DownloadListItem(
                        String.valueOf(downloadID),
                        response.getId(),
                        response.getTargetUrl(),
                        file.getPath(),
                        DownloadDB.DOWNLOAD_ING,
                        response.getAppDownloadFinishTrackers()
                );
                long insertData = DownloadDB.getInstance(activity).insertData(item);
                ZplayDebug.w_m(TAG, "DB status code：" + insertData, onoff);
            } catch (Exception e) {
                Toast.makeText(activity, "Download Failed", Toast.LENGTH_SHORT).show();
                ZplayDebug.e_m(TAG, "Download Failed", e, onoff);
            }
        } else {
            Toast.makeText(activity, "Failed to create directory，Download Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Download Start
     *
     * @param activity
     * @param response
     */
    private static void reportDownloadStart(Activity activity, YumiAdBean response) {
        try {
            ReportEntity entity = new ReportEntity(
                    null,
                    null,
                    null);
            ZplayDebug.v_m(TAG, "Report start download event：getClickdownloadTrackerUrl：" + response.getAppDownloadTrackers(), onoff);
            Reporter.reportEvent(activity, response.getAppDownloadTrackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "reportDownloadStartBI error : ", e, onoff);
        }
    }

    /**
     * Download Complete
     * @param context
     * @param downloadID
     */
    public static void handleDownloadComplete(Context context, String downloadID) {
        DownloadListItem item = DownloadDB.getInstance(context).selectData("downloadid", downloadID);
        ZplayDebug.v_m(TAG, "download completed", onoff);
        ReportEntity entity = new ReportEntity(
                null,
                null,
                null);
        ZplayDebug.v_m(TAG, "Report download completed：getDownloadedTrackerUrl：" + item.getDownloadedTrackerUrlS(), onoff);
        Reporter.reportEvent(context, item.getDownloadedTrackerUrl(), entity);

        DownloadDB.getInstance(context).deleteData("id", String.valueOf(item.getId()));
    }

    private static PackageInfo getPackageInfo(Context context, String apkFilePath) {
        PackageInfo pkgInfo = context.getPackageManager()
                .getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        return pkgInfo;

    }


}
