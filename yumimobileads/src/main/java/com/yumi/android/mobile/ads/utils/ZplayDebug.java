package com.yumi.android.mobile.ads.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZplayDebug {
    private static final String MOBILE_TAG = "YumiMobileAds";
    private static final String FORMAT = "[class] : %s , [msg] : %s";
    private static final String LEVELLOCFILE = "//29b2e3aa7596f75d0fda1f1f56183907";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT2 = "yyyy_MM_dd_HH";
    private static final int MAX_VALUE = 500;
    private static boolean isSave = true;
    private static ExecutorService log_pool = null;
    private static boolean isReaded = false;
    private static DebugLevel dLevel = DebugLevel.LEVEL_PUBLISHER;
    private static SimpleDateFormat format = null;
    private static SimpleDateFormat format_name = null;

    public static void setSave(boolean isSave) {
        ZplayDebug.isSave = isSave;
    }

    private static String buildLogMsg(String tag, String msg) {
        String log = String.format(FORMAT, tag, msg);
        return log;
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param onoff
     */
    public static void w_m(String tag, String msg, boolean onoff) {
        w(MOBILE_TAG, tag, msg, onoff);
    }

    public static void w(String baseTag, String tag, String msg, boolean onoff) {
        if (onoff) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                try {
                    Log.w(baseTag, buildLogMsg(tag, msg.substring(0, MAX_VALUE)));
                } catch (Exception e) {
                    Log.w(baseTag, buildLogMsg(tag, msg));
                }
                saveLog(tag, msg);
            }
        }
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param onoff
     */
    public static void e_m(String tag, String msg, boolean onoff) {
        e(MOBILE_TAG, tag, msg, onoff);
    }

    public static void e(String baseTag, String tag, String msg, boolean onoff) {
        if (onoff) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                Log.e(baseTag, buildLogMsg(tag, msg + ""));
                saveLog(tag, msg);
            }
        }
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param tr
     * @param onoff
     */
    public static void e_m(String tag, String msg, Throwable tr, boolean onoff) {
        e(MOBILE_TAG, tag, msg, tr, onoff);
    }

    public static void e(String baseTag, String tag, String msg, Throwable tr, boolean onoff) {
        if (onoff && tr != null) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                Log.e(baseTag, buildLogMsg(tag, msg + tr.getMessage()));
                saveLog(tag, msg + "\n" + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param onoff
     */
    public static void d_m(String tag, String msg, boolean onoff) {
        d(MOBILE_TAG, tag, msg, onoff);
    }

    public static void d(String baseTag, String tag, String msg, boolean onoff) {
        if (onoff) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_DEBUG || level == DebugLevel.LEVEL_TECH) {
                try {
                    Log.d(baseTag, buildLogMsg(tag, msg.substring(0, MAX_VALUE)));
                } catch (Exception e) {
                    Log.d(baseTag, buildLogMsg(tag, msg));
                }
                saveLog(tag, msg);
            }
        }
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param onoff
     */
    public static void v_m(String tag, String msg, boolean onoff) {
        v(MOBILE_TAG, tag, msg, onoff);
    }

    public static void v(String baseTag, String tag, String msg, boolean onoff) {
        if (onoff) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                try {
                    Log.v(baseTag, buildLogMsg(tag, msg.substring(0, MAX_VALUE)));
                } catch (Exception e) {
                    Log.v(baseTag, buildLogMsg(tag, msg));
                }
                saveLog(tag, msg);
            }
        }
    }

    /**
     * YumiMobileAds日志 TAG = YumiMobileAds
     *
     * @param tag
     * @param msg
     * @param onoff
     */
    public static void i_m(String tag, String msg, boolean onoff) {
        i(MOBILE_TAG, tag, msg, onoff);
    }

    public static void i(String baseTag, String tag, String msg, boolean onoff) {
        if (onoff) {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                try {
                    Log.i(baseTag, buildLogMsg(tag, msg.substring(0, MAX_VALUE)));
                } catch (Exception e) {
                    Log.i(baseTag, buildLogMsg(tag, msg));
                }
                saveLog(tag, msg);
            }
        }
    }

    public static boolean getIsExportLog() {
        try {
            DebugLevel level = getDebugLevel();
            if (level == DebugLevel.LEVEL_TECH) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static DebugLevel getDebugLevel() {
        if (isReaded && dLevel != null) {
            return dLevel;
        }
        DebugLevel level = DebugLevel.LEVEL_PUBLISHER;
        try {
            //获取SD卡根目录下文件，如果没有SD读取权限会报错
            File file = new File("//mnt//sdcard" + LEVELLOCFILE);
            if (file.exists()) {
                level = DebugLevel.LEVEL_TECH;
            }

        } catch (Exception e) {
        }
        isReaded = true;
        dLevel = level;
        return level;
    }

    private static SimpleDateFormat getFormat() {
        if (format == null) {
            format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        }
        return format;
    }

    private static SimpleDateFormat getFormat_name() {
        if (format_name == null) {
            format_name = new SimpleDateFormat(DATE_FORMAT2, Locale.getDefault());
        }
        return format_name;
    }

    private static void saveLog(final String tag, final String msg) {
        if (isSave) {
            if (log_pool == null) {
                log_pool = Executors.newFixedThreadPool(1);
            }
            Runnable run = new Runnable() {
                public void run() {
                    String sdcard = Environment.getExternalStorageDirectory().getPath();
                    File dir = new File(sdcard + "/.zplayads/log/");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(dir, getFormat_name().format(new Date()) + ".log");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file, true);
                        String date = getFormat().format(new Date());
                        String log = "[" + date + "] " + "TAG:" + tag + " msg:" + msg + "\r\n";
                        byte[] b = log.getBytes("utf-8");
                        fos.write(b);
                        fos.flush();
                    } catch (Exception e) {
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            };
            log_pool.execute(run);
        }
    }


    private static enum DebugLevel {
        LEVEL_PUBLISHER, LEVEL_DEBUG, LEVEL_TECH
    }

}