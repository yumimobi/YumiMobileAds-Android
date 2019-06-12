package com.yumi.android.mobile.ads.utils.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public final class WindowSizeUtils {

    public static boolean isPortrait(Context context) {
        int[] displayMetrics = PhoneInfoGetter.getDisplayMetrics(context);
        return displayMetrics[0] <= displayMetrics[1];
    }

    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (px / scale + 0.5f));
    }

    public static int dip2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }


    /***
     * 获取屏幕宽高 px
     */
    @SuppressWarnings("deprecation")
    public static int[] getRealSize(Activity activity) {
        //TODO 增加系统版本判断
        if (Build.VERSION.SDK_INT >= 17) {
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            return new int[]{point.x, point.y};
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            return new int[]{display.getWidth(), display.getHeight()};
        }

    }

    public static boolean isTablet(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            if(windowManager == null){
                return isApproximateTablet(context.getApplicationContext());
            }
            Point point = new Point();
            windowManager.getDefaultDisplay().getRealSize(point);
            float density = context.getResources().getDisplayMetrics().density;
            double inch = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / (160 * density);
            return inch >= 8.0d;
        }
        return isApproximateTablet(context.getApplicationContext());
    }

    public static boolean isApproximateTablet(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        float density = displayMetrics.density;
        double inch = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / (160 * density);
        return inch >= 8.0d;
    }

    /**
     * 获取屏幕宽高dp值
     *
     * @param context
     */
    public static int[] getAndroidScreenProperty(Context context) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;// 屏幕宽度（像素）
            int height = dm.heightPixels; // 屏幕高度（像素）
            float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
            int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
            //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            int screenWidth = (int) (width / density);//屏幕宽度(dp)
            int screenHeight = (int) (height / density);//屏幕高度(dp)
            return new int[]{screenWidth, screenHeight};
        } catch (Exception ignored) {
        }
        return new int[]{0, 0};
    }

    /**
     * 获取屏幕宽度，单位为 px
     */
    public static int getAndroidScreenWidth(Context context) {
        int widthDp = getAndroidScreenProperty(context)[0];
        return dip2px(context, widthDp);
    }
}
