package com.yumi.android.mobile.ads.utils.ui;

import android.content.Context;
import android.view.View;

public class ViewSeed extends View {

    public ViewSeed(Context context) {
        super(context);
    }

    static int[] getPressed() {
        return PRESSED_STATE_SET;
    }

    static int[] getEmpty() {
        return EMPTY_STATE_SET;
    }

}
