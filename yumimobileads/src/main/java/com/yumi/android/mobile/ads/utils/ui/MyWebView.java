package com.yumi.android.mobile.ads.utils.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {

    private float lastDownX;
    private float lastDownY;
    private float lastX;
    private float lastY;

    public MyWebView(Context context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            return false;
        }
        if (action == MotionEvent.ACTION_DOWN) {
            lastDownX = ev.getX();
            lastDownY = ev.getY();
        } else if (action == MotionEvent.ACTION_UP) {
            lastX = ev.getX();
            lastY = ev.getY();
        }
        return super.onTouchEvent(ev);
    }

    /**
     * @return [X, Y]
     */
    public float[] getLastTouchArea() {
        return new float[]
                {
                        lastX,
                        lastY
                };
    }

    /**
     * @return [DownX, DownY]
     */
    public float[] getLastDownArea() {
        return new float[]
                {
                        lastDownX,
                        lastDownY
                };
    }
}
