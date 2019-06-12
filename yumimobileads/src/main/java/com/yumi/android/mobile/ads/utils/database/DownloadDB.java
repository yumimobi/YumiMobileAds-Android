package com.yumi.android.mobile.ads.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yumi.android.mobile.ads.utils.entity.DownloadListItem;
import com.yumi.android.mobile.ads.utils.ZplayDebug;


public class DownloadDB {
    public static final int DOWNLOAD_ING = 1;
    public static final int DOWNLOAD_COMPLETE = 2;
    public static final int INSTALL_COMPLETE = 3;
    private final static String TAG = "DownloadDB";
    private static DownloadDB instance;
    private final String tablename = "downloadlistadx";
    private SQLiteDatabase db;

    private DownloadDB(Context context) {
        try {
            ADDB helper = ADDB.buildInstance(context);
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "DownloadDB error : ", e, true);
        }
    }

    public static DownloadDB getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadDB(context);
        }
        return instance;
    }

    public long insertData(DownloadListItem item) {
        long insert = -1;
        ContentValues values = putValue(item);
//		long insert = db.insert(tablename, null, values);
        try {
            insert = db.insertOrThrow(tablename, null, values);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "insertData error : ", e, true);
        }
        return insert;
    }

    public DownloadListItem selectData(String key, String value) {
        try {
            Cursor cursor = db.query(tablename, null, key + "=?", new String[]
                    {
                            value
                    }, null, null, null);
            if (cursor.moveToNext()) {
                DownloadListItem item = getObj(cursor);
                return item;
            }
            cursor.close();
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "selectData [key:" + key + "][value:" + value + "] error : ", e, true);
        }
        return null;
    }

    public void deleteData(String key, String value) {
        try {
            db.delete(tablename, key + "=?", new String[]{value});
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "deleteData [key:" + key + "][value:" + value + "] error : ", e, true);
        }
    }

    public void updateData(DownloadListItem item) {
        try {
            db.update(tablename, putValue(item), "id=?", new String[]{String.valueOf(item.getId())});
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "updateData  error : ", e, true);
        }
    }

    private DownloadListItem getObj(Cursor cursor) {
        DownloadListItem item = new DownloadListItem();
        try {
            item.setId(cursor.getString(cursor.getColumnIndex("id")));
            item.setDownloadid(cursor.getString(cursor.getColumnIndex("downloadid")));
            item.setResurl(cursor.getString(cursor.getColumnIndex("resurl")));
            item.setPath(cursor.getString(cursor.getColumnIndex("path")));
            item.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            item.setDownloadedTrackerUrlS(cursor.getString(cursor.getColumnIndex("downloadedTrackerUrl")));
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getObj error : ", e, true);
        }
        return item;
    }


    private ContentValues putValue(DownloadListItem item) {
        ContentValues values = new ContentValues();
		values.put("id", item.getId());
        values.put("downloadid", item.getDownloadid());
        values.put("resurl", item.getResurl());
        values.put("path", item.getPath());
        values.put("status", item.getStatus());
        values.put("downloadedTrackerUrl", item.getDownloadedTrackerUrlS());
        return values;
    }


}
