package com.yumi.android.mobile.ads.utils.display;

import android.app.Activity;
import android.app.Dialog;

import com.yumi.android.mobile.ads.utils.device.PhoneInfoGetter;

public class FullDialogBuilder {

    /**
     * create Dialog
     *
     * @param activity
     * @return
     */
    public static Dialog buildFullDialog(Activity activity) {
        boolean isfullScreen = PhoneInfoGetter.isFullScreen(activity);
        Dialog dialog = null;
        if (isfullScreen) {
            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        } else {
            dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }
}
