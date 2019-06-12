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
     * sd卡是否可用
     *
     * @return
     */
    public final static boolean isSDAvaliable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡的根目录
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

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     */
    public static void RecursionDeleteFile(final File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    RecursionDeleteFile(f);
                }
                file.delete();
            }
        } catch (Exception e) {
            Log.e("YumiMobi", "RecursionDeleteFile error", e);
        }
    }


    /**
     * 删除文件夹下所有文件
     *
     * @param filePath      文件夹地址
     * @param exceptionPath 不想要被删除的文件地址
     */
    public static boolean DeleteFileInDirectory(final String filePath, final String exceptionPath) {
        try {
            File file_dir = new File(filePath);
            if (file_dir.isDirectory()) {
                File[] childFile = file_dir.listFiles();
                if (childFile != null || childFile.length > 0) {
                    for (File file : childFile) {
                        if (exceptionPath == null || "".equals(exceptionPath)) {
                            file.delete();
                        } else if (!file.getPath().equals(exceptionPath)) {
                            file.delete();
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("YumiMobi", "DeleteFileInDirectory error", e);
        }
        return false;
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
