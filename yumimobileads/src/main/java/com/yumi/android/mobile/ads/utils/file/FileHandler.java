package com.yumi.android.mobile.ads.utils.file;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileHandler {

    public static String getFileExtensionName(String file) {
        int i = file.lastIndexOf('.');
        int leg = file.length();
        return (i > 0 ? (i + 1) == leg ? " " : file.substring(i + 1,
                file.length()) : " ");
    }

    /**
     * isSDAvaliable
     *
     * @return
     */
    public final static boolean isSDAvaliable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * get SDRootDIR
     *
     * @return
     */
    public final static String getSDRootDIR(Context context) {
        String cachePath = "";
        try {
            if (context != null) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                    cachePath = context.getExternalCacheDir().getPath();
                } else {
                    cachePath = context.getCacheDir().getPath();
                }
            }
        } catch (Exception e) {
            Log.e("YumiMobi", "getSDRootDIR error", e);
        }
        return cachePath;
    }


    public static File getDownloadFile(DownloadManager dm, long downloadId) {
        File result = null;
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = dm.query(query);
        query.setFilterById(downloadId);
        String filePath;
        if (cursor.moveToNext()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                result = new File(filePath);
                if (!result.exists()) {
                    result = null;
                }
            }
        }
        cursor.close();
        return result;
    }
}
