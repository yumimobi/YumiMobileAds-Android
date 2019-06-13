package com.yumi.android.mobile.ads.utils.views;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yumi.android.mobile.ads.utils.ZplayDebug;


public class YumiWebviewClient extends WebViewClient {
    private String TAG = "YumiWebviewClient";
    private boolean onoff = true;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        ZplayDebug.d_m(TAG, "shouldOverrideUrlLoading: request = " + request, onoff);
        if (request != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && request.getUrl() != null) {
                shouldOverrideUrlLoading(view, request.getUrl().toString());
                return true;
            }
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        ZplayDebug.d_m(TAG, "shouldOverrideUrlLoading", onoff);
        return super.shouldOverrideUrlLoading(view, url);
    }
}
