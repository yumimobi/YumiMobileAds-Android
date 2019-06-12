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
import android.util.Log;
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
            // 检查当前设备上是否已经有安装该app&已经安装的app版本号不小于当前要下载的包的版本号，如果是的话，不进行下载，如果不是的话，进行下载
            String packageName = response.getApp_bundle();
            if (packageName == null || "".equals(packageName) || "null".equals(packageName)) {
                packageName = String.valueOf(System.currentTimeMillis());
                response.setApp_bundle(packageName);
            }
            String versionCode = response.getApp_ver();
            String fileName = response.getDownload_file_name();
            if (fileName == null || "".equals(fileName) || "null".equals(fileName)) {
                fileName = response.getApp_bundle() + "_" + versionCode + ".apk";
                response.setDownload_file_name(fileName);
            }
            String url = response.getTarget_url();
            ZplayDebug.v_m(TAG, "下载地址为" + url, onoff);

            packageName = packageName.trim();
            int installedAppVersion = PackageInfoGetter.getAppVersionCode(activity.getPackageManager(), packageName);
            ZplayDebug.v_m(TAG, "当前已经安装的包：[" + packageName + "]的版本号是：" + installedAppVersion + ",准备下载的包版本号是：" + versionCode, onoff);
//            if (installedAppVersion >= versionCode) {
//                ZplayDebug.v_m(TAG, "安装包版本过旧，不下载", onoff);
//                ThirdAppStarter.startApp(activity, packageName);
////                Toast.makeText(activity, "您已经安装[" + fileName + "]的最新版本，不进行下载...", Toast.LENGTH_SHORT).show();
//            } else {
            ZplayDebug.v_m(TAG, "发现新版本安装包", onoff);
            // 首先检查是否在下载表中
            DownloadDB db = DownloadDB.getInstance(activity);
            DownloadListItem item = db.selectData("resurl", response.getApp_bundle());
            if (item == null) {
                handleDownloadStuff(activity, response);
            } else {
                ZplayDebug.v_m(TAG, "广告下载列表中发现该项，检查是否真的正在下载...", onoff);
                String downloadID = item.getDownloadid();
                DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Query query = new Query();
                query.setFilterById(Long.parseLong(downloadID));

                Cursor cursor = dm.query(query);
                if (cursor.moveToFirst()) {
                    ZplayDebug.v_m(TAG, "系统当前下载列表中发现该项，查看状态", onoff);
                    String desc = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                    long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    final int PAUSED = DownloadManager.STATUS_PAUSED; // 暂停
                    final int PENDING = DownloadManager.STATUS_PENDING; // 等待下载
                    final int RUNNING = DownloadManager.STATUS_RUNNING; // 正在下载
                    final int SUCCESSFUL = DownloadManager.STATUS_SUCCESSFUL; // 完成

                    final int FAILED = DownloadManager.STATUS_FAILED; // 失败

                    ZplayDebug.v_m(TAG, "下载状态为：<desc>=" + desc + ";<status>=" + status, onoff);
                    switch (status) {
                        case FAILED:
                            if (!reload) {
                                ZplayDebug.v_m(TAG, "下载失败，重新下载", onoff);
                                dm.remove(id);
                                startDownload(activity, response, true);
                            }
                            return;
                        case PAUSED:
                        case PENDING:
                        case RUNNING:
                            break;
                        case SUCCESSFUL:
                            ZplayDebug.v_m(TAG, "下载完成，检查是否存在完整的安装包", onoff);
                            String path_apk = item.getPath();
                            File file_apk = new File(path_apk);
                            if (file_apk.exists()) {
                                ZplayDebug.v_m(TAG, "文件存在，检查完整性", onoff);
                                PackageInfo packageInfo = getPackageInfo(activity, file_apk.getAbsolutePath());
                                if (packageInfo == null) {
                                    ZplayDebug.v_m(TAG, "该包下载不完整，那么删除该不完整的包，然后重新进行下载...", onoff);
                                    if (file_apk.delete()) {
                                        DownloadDB.getInstance(activity).deleteData("id", String.valueOf(item.getId()));
                                        ZplayDebug.v_m(TAG, "破损包删除完成，开始重新进行下载...", onoff);
                                        handleDownloadStuff(activity, response);
                                    } else {
                                        ZplayDebug.v_m(TAG, "破损包删除失败，不进行下载...", onoff);
                                        Toast.makeText(activity, "下载[" + fileName + "]失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ZplayDebug.v_m(TAG, "包完整，准备安装", onoff);
                                    ThirdAppStarter.startAppInstall(activity, file_apk.getAbsolutePath());
                                }
                            } else {
                                ZplayDebug.v_m(TAG, "文件不存在，重新下载", onoff);
                                DownloadDB.getInstance(activity).deleteData("id", String.valueOf(item.getId()));
                                handleDownloadStuff(activity, response);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    if (!reload) {
                        ZplayDebug.v_m(TAG, "系统当前下载列表中未发现该项，确定为未下载!!!", onoff);
                        // 无下载项，重新下载
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
        String fileName = response.getDownload_file_name();

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(response.getTarget_url());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(fileName + "下载中...").setTitle(fileName);
        File apkDir = new File(FileHandler.getSDRootDIR(activity) + Constants.dir.DIR_APK);
        if (apkDir.exists() || apkDir.mkdirs()) {
            File file = new File(apkDir, fileName);
            request.setDestinationUri(Uri.fromFile(file));
            try {
                long downloadID = downloadManager.enqueue(request);

                reportDownloadStart(activity, response);
                Toast.makeText(activity, "开始进行下载：" + fileName, Toast.LENGTH_SHORT).show();
                ZplayDebug.v_m(
                        TAG,
                        "开始进行下载任务：" + fileName + "，adID：" + response.getId() + "，显示的名称：" + fileName + "，下载ID"
                                + downloadID, onoff);
                Log.i("TempTest", "开始进行下载任务downloadID = " + downloadID);
                DownloadListItem item = new DownloadListItem(
                        String.valueOf(downloadID),
                        response.getId(),
                        response.getTarget_url(),
                        file.getPath(),
                        DownloadDB.DOWNLOAD_ING,
                        response.getApp_download_trackers()
                );
                long insertData = DownloadDB.getInstance(activity).insertData(item);
                ZplayDebug.w_m(TAG, "数据库状态码：" + insertData, onoff);
            } catch (Exception e) {
                Toast.makeText(activity, "下载受限", Toast.LENGTH_SHORT).show();
                ZplayDebug.e_m(TAG, "下载受限", e, onoff);
            }
        } else {
            Toast.makeText(activity, "创建目录失败，下载失败...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上报开始下载
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
            ZplayDebug.v_m(TAG, "上报开始下载：getClickdownloadTrackerUrl：" + response.getApp_download_trackers(), onoff);
            Reporter.reportEvent(activity, response.getApp_download_trackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "reportDownloadStartBI error : ", e, onoff);
        }
    }

    /**
     * 处理下载完成之后需要处理的事：
     * <p>
     * <li>将该条数据从下载中列表中移除
     * <li>加入下载完成待上报表中
     * <li>加入到等待安装表中
     * <li>弹出安装界面
     *
     * @param context
     * @param downloadID
     */
    public static void handleDownloadComplete(Context context, String downloadID) {
        DownloadListItem item = DownloadDB.getInstance(context).selectData("downloadid", downloadID);
        ZplayDebug.v_m(TAG, "上报下载完成的事件...", onoff);
        ReportEntity entity = new ReportEntity(
                null,
                null,
                null);
        ZplayDebug.v_m(TAG, "上报下载完成：getDownloadedTrackerUrl：" + item.getDownloadedTrackerUrlS(), onoff);
        Reporter.reportEvent(context, item.getDownloadedTrackerUrl(), entity);


        //334版本改为不在自动打开安装好的应用，所以改为直接删除数据库记录
        DownloadDB.getInstance(context).deleteData("id", String.valueOf(item.getId()));
    }

    private static PackageInfo getPackageInfo(Context context, String apkFilePath) {
        PackageInfo pkgInfo = context.getPackageManager()
                .getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        return pkgInfo;

    }


}
