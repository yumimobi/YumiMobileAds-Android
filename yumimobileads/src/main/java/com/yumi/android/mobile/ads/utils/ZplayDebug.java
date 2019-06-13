package com.yumi.android.mobile.ads.utils;

import android.util.Log;

import java.io.File;

public class ZplayDebug {
    private static final String MOBILE_TAG = "YumiMobileAds";
    private static final String FORMAT = "[class] : %s , [msg] : %s";
    private static final String LEVELLOCFILE = "//29b2e3aa7596f75d0fda1f1f56183907";
    private static boolean isReaded = false;
    private static DebugLevel dLevel = DebugLevel.LEVEL_PUBLISHER;
    private static final int MAX_VALUE = 500;

    private static String buildLogMsg(String tag, String msg) {
        String log = String.format(FORMAT, tag, msg);
        return log;
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    /**
     * YumiMobileAds Log TAG = YumiMobileAds
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
            }
        }
    }

    private static DebugLevel getDebugLevel() {
        if (isReaded && dLevel != null) {
            return dLevel;
        }
        DebugLevel level = DebugLevel.LEVEL_PUBLISHER;
        try {
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

    private static enum DebugLevel {
        LEVEL_PUBLISHER, LEVEL_DEBUG, LEVEL_TECH
    }

}