package com.yumi.android.mobile.ads.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ADDB extends SQLiteOpenHelper {
    private static final String TAG = "ADDB";
    private static final String DB_NAME = "zplayadmobileadx"; //TODO 在数据库发生变化的情况下，不需要升级数据库了，直接修改一个新的库名
    private static final int DB_VERSION = 1;

    private final static String SQL_CREATE_DOWNLOADLIST =
            "CREATE TABLE \"main\".\"downloadlistadx\" ("
                    + "\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT, "
                    + "\"downloadid\" VARCHAR , "
                    + "\"adid\" VARCHAR, "
                    + "\"resurl\" VARCHAR, "
                    + "\"path\" VARCHAR, "
                    + "\"status\" INTEGER, "
                    + "\"downloadedTrackerUrl\" VARCHAR "
                    + ")";


    private static ADDB instance;

    public ADDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static ADDB buildInstance(Context context) {
        if (instance == null) {
            instance = new ADDB(context, context.getPackageName() + "." + DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOWNLOADLIST);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
