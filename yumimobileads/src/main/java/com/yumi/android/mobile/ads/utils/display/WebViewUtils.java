package com.yumi.android.mobile.ads.utils.display;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yumi.android.mobile.ads.utils.device.PhoneInfoGetter;
import com.yumi.android.mobile.ads.utils.ui.MyWebView;


public class WebViewUtils {

    @SuppressLint("NewApi")
    public static void evokeJS(WebView webView, String js) {
        if (PhoneInfoGetter.getAndroidVersionCode() >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl(js);
        }
    }

    /**
     * create webView
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint(
            {
                    "SetJavaScriptEnabled"
            })
    public static WebView buildWebView(Context context) {
        WebView webView = new MyWebView(context);
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setSupportZoom(false);
            webSettings.setBuiltInZoomControls(false);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setHorizontalScrollBarEnabled(false);

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= 11) {
                webSettings.setDisplayZoomControls(false);
            }
            webSettings.setLoadWithOverviewMode(true);

            webSettings.setDomStorageEnabled(true);//DOM Storage
            //如果网页链接是https的但是内容里面比如图片是http的，android 4.4以后webview会阻塞链接的打开，需要做以下处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        } catch (Exception e) {
        }
        return webView;
    }

    @SuppressLint(
            {
                    "NewApi",
                    "SetJavaScriptEnabled"
            })
    public static WebView buildWebViewWithTransparentBackground(Context context) {
        WebView webView = new MyWebView(context);
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setSupportZoom(false);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setLoadWithOverviewMode(true);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setHorizontalScrollbarOverlay(true);
            webView.setBackgroundColor(0);

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= 11) {
                webSettings.setDisplayZoomControls(false);
            }
            webSettings.setLoadWithOverviewMode(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }

            if (PhoneInfoGetter.getAndroidVersionCode() >= Build.VERSION_CODES.HONEYCOMB) {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webSettings.setDomStorageEnabled(true);//DOM Storage
            //如果网页链接是https的但是内容里面比如图片是http的，android 4.4以后webview会阻塞链接的打开，需要做以下处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        } catch (Exception e) {
        }
        return webView;
    }

    @SuppressLint(
            {
                    "NewApi",
                    "SetJavaScriptEnabled"
            })
    public static WebView buildInSetWebViewWithTransparentBackground(Context context) {
        WebView webView = new MyWebView(context);
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setSupportZoom(false);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setAllowFileAccess(true); // 允许访问文件
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= 11) {
                webSettings.setDisplayZoomControls(false);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }

            webSettings.setDomStorageEnabled(true);//DOM Storage
            //如果网页链接是https的但是内容里面比如图片是http的，android 4.4以后webview会阻塞链接的打开，需要做以下处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }


            webView.setHorizontalScrollbarOverlay(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setHorizontalScrollbarOverlay(true);
            webView.setBackgroundColor(0);
            webView.setWebChromeClient(new MyWebChromeClient());
        } catch (Exception e) {
        }
        return webView;
    }

    private static class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            super.onConsoleMessage(consoleMessage);
            return false;
        }
    }

}
