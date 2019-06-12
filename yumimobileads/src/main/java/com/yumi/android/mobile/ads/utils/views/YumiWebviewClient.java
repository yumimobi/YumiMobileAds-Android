package com.yumi.android.mobile.ads.utils.views;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yumi.android.mobile.ads.utils.ZplayDebug;


/**
 * Description:
 * <p>
 * Created by yfb on 2019/03/19.
 * 消灭星星全新版 Eclipse 项目正式签名apk在oppo r11 ，Android 7.1.1设备上出现点击插屏和视频不跳转的问题，
 * 原因是因为在7.0设备运行时webview的shouldOverrideUrlLoading(WebView view, WebResourceRequest request)接口会返回false，
 * 所以构建一个继承自WebViewClient的YumiWebviewClient，
 * 重写shouldOverrideUrlLoading(WebView view, WebResourceRequest request)接口.
 */

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
