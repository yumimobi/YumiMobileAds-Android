package com.yumi.android.mobile.ads.utils.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.device.WindowSizeUtils;

import java.util.Map;

public class MyExplorer extends RelativeLayout {
    private static final boolean onoff = true;
    private static final String TAG = "MyExplorer";
    private static final int id_tv_title = 0x20005;
    private static final int id_tv_bottom = 0x20006;
    private WebView wv;
    private Activity context;
    private WebViewClient mClient;
    private OnClickListener btnClickListener;
    //    private WebStatus mStatus;
    private String html;
    private Handler handler;
    private OnHandleClose mOnHandleClose;
    private ImageView btn_web_back;
    private ImageView btn_web_forward;
    private ImageView btn_web_reflush;
    private ImageView btn_close;

    public MyExplorer(Activity context) {
        super(context);
        init(context);
    }

    public MyExplorer(Activity context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyExplorer(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Activity context) {
        this.context = context;
        this.btnClickListener = new MyOnClickListener();
        handler = new Handler();
        setBottom();
        setWebView();
        checkBtnStatus();
//        setLoadingAnim();TODO 不要动画了

        checkCloseStatus(false);

        // 两秒后激活关闭按钮
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkCloseStatus(true);
            }
        }, 2000);
    }

    /**
     * 添加主页面
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        wv = buildWebView(context);
        wv.setBackgroundColor(0xffffffff);

        RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, id_tv_title);
        params.addRule(RelativeLayout.ABOVE, id_tv_bottom);
        wv.setLayoutParams(params);
        this.addView(wv);
    }

    /**
     * 添加底部栏
     */
    private void setBottom() {
        // 后退按钮
        btn_web_back = getBelowBtn(ResFactory.getStateListDrawable("zplayadx_btn_web_goback", context));
        // 前进按钮
        btn_web_forward = getBelowBtn(ResFactory.getStateListDrawable("zplayadx_btn_web_goforward", context));
        // 刷新按钮
        btn_web_reflush = getBelowBtn(ResFactory.getStateListDrawable("zplayadx_btn_web_reflush", context));
        // 关闭按钮
        btn_close = getBelowBtn(ResFactory.getStateListDrawable("zplayadx_btn_web_close", context));

        btn_web_back.setOnClickListener(btnClickListener);
        btn_web_forward.setOnClickListener(btnClickListener);
        btn_web_reflush.setOnClickListener(btnClickListener);
        btn_close.setOnClickListener(btnClickListener);

        // 底部容器
        LinearLayout ll = new LinearLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                dip2px(context, 50));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ll.setBackgroundColor(0xffdddddd);
        ll.setGravity(Gravity.CENTER);
        ll.setId(id_tv_bottom);
        ll.setLayoutParams(params);

        int line = WindowSizeUtils.dip2px(context, 40);
        LinearLayout.LayoutParams param_btn = new LinearLayout.LayoutParams(0, line);
        param_btn.weight = 1;

        ll.addView(btn_web_back, param_btn);
        ll.addView(btn_web_forward, param_btn);
        ll.addView(btn_web_reflush, param_btn);
        ll.addView(btn_close, param_btn);
        this.addView(ll);
    }

    private ImageView getBelowBtn(Drawable drawable) {
        ImageView btn = new ImageView(context);
        btn.setImageDrawable(drawable);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        btn.setBackgroundColor(0);
        params.weight = 1;
        btn.setLayoutParams(params);
        return btn;
    }


    /**
     * 根据页面状态设置前进、后退按钮状态
     */
    private void checkBtnStatus() {
        if (wv.canGoBack()) {
            btn_web_back.setImageDrawable(ResFactory.getStateListDrawable("zplayadx_btn_web_goback", context));
        } else {
            btn_web_back.setImageDrawable(ResFactory.getDrawableByAssets("zplayadx_btn_web_goback_lass", context));
        }
        if (wv.canGoForward()) {
            btn_web_forward.setImageDrawable(ResFactory.getStateListDrawable("zplayadx_btn_web_goforward", context));
        } else {
            btn_web_forward.setImageDrawable(ResFactory.getDrawableByAssets("zplayadx_btn_web_goforward_lass", context));
        }

    }

    /**
     * 根据页面状态设置关闭按钮状态
     */
    private void checkCloseStatus(boolean isCanClose) {

        if (!isCanClose) {
            btn_close.setEnabled(false);
            btn_close.setImageDrawable(ResFactory.getDrawableByAssets("zplayadx_btn_web_close_lass", context));
            ZplayDebug.v_m(TAG, "关闭按钮失效", onoff);
        } else {
            btn_close.setEnabled(true);
            btn_close.setOnClickListener(btnClickListener);
            btn_close.setImageDrawable(ResFactory.getStateListDrawable("zplayadx_btn_web_close", context));
            ZplayDebug.v_m(TAG, "关闭按钮激活", onoff);
        }

    }

    /**
     * Loads the given URL.
     *
     * @param url the URL of the resource to load
     */
    public void loadUrl(String url) {
        wv.loadUrl(url);
    }

    /**
     * Loads the given URL with the specified additional HTTP headers.
     *
     * @param url          the URL of the resource to load
     * @param extraHeaders the additional headers to be used in the HTTP request for this
     *                     URL, <br/>
     *                     specified as a map from name to value. Note that if this map
     *                     contains any of the <br/>
     *                     headers that are set by default by this WebView, such as those
     *                     controlling caching,<br/>
     *                     accept types or the User-Agent, their values may be overriden
     *                     by this WebView's <br/>
     *                     defaults.
     */
    public void loadUrl(String url, Map<String, String> extraHeaders) {
        wv.loadUrl(url, extraHeaders);
    }

    /**
     * @param data
     * @param mimeType
     * @param encoding
     */
    public void loadData(String data, String mimeType, String encoding) {
        wv.loadData(data, mimeType, encoding);
        html = data;
    }

    public void loadData(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        wv.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
        html = data;
    }

    public void setDownloadListener(final DownloadListener listener) {
        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                if (listener != null) {
                    listener.onDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
                }
            }
        });
    }

    public void setWebViewClient(final MyWebViewClient client) {
        mClient = new WebViewClient() {

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
                if (client != null) {
                    return client.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                mStatus = WebStatus.LOADING;
//                startLoadingAnim();TODO 不要动画了
                if (client != null) {
                    client.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 加载完成后激活关闭按钮
//                mStatus = WebStatus.FINISH;
//                stopLoadingAnim();TODO 不要动画了
                if (client != null) {
                    client.onPageFinished(view, url);
                }
                checkBtnStatus();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 加载错误时激活关闭按钮
//                mStatus = WebStatus.FINISH;
                if (client != null) {
                    client.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                if (client != null) {
                    client.onFormResubmission(view, dontResend, resend);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (client != null) {
                    client.onLoadResource(view, url);
                }
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                if (client != null) {
                    client.onReceivedHttpAuthRequest(view, handler, host, realm);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                final SslErrorHandler sslHadnler = handler;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sslHadnler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sslHadnler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                if (client != null) {
                    client.onScaleChanged(view, oldScale, newScale);
                }
            }

        };
        wv.setWebViewClient(mClient);
    }

    public boolean canGoBack() {
        return wv.canGoBack();
    }

    public void goBack() {
        wv.goBack();
    }

    public boolean canGoForward() {
        return wv.canGoForward();
    }

    public void goForward() {
        wv.goForward();
    }

    public void setVerticalScrollbarOverlay(boolean overlay) {
        wv.setVerticalScrollbarOverlay(overlay);
    }

    @SuppressLint("NewApi")
    public void evokeJS(String js) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wv.evaluateJavascript(js, null);
        } else {
            wv.loadUrl(js);
        }
    }

    /**
     * 创建webView
     *
     * @param context activity
     * @return
     */
    @SuppressLint({"NewApi", "SetJavaScriptEnabled"})
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private WebView buildWebView(Context context) {
        WebView webView = new WebView(context);
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setSupportZoom(false);
            webSettings.setBuiltInZoomControls(false);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setHorizontalScrollBarEnabled(false);
            webSettings.setDomStorageEnabled(true);//DOM Storage
            //如果网页链接是https的但是内容里面比如图片是http的，android 4.4以后webview会阻塞链接的打开，需要做以下处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        } catch (Exception e) {
        }
        return webView;
    }

    private int dip2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

    public void setmOnHandleClose(OnHandleClose onHandleClose) {
        this.mOnHandleClose = onHandleClose;
    }

    //    private enum WebStatus
//    {
//        LOADING, FINISH;
//    }
//
    public void destroy() {
        if (wv != null) {
            wv.destroy();
        }
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        wv.setWebChromeClient(webChromeClient);
    }

    public interface MyWebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url);

        public void onPageStarted(WebView view, String url, Bitmap favicon);

        public void onPageFinished(WebView view, String url);

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

        public void onFormResubmission(WebView view, Message dontResend, Message resend);

        public void onLoadResource(WebView view, String url);

        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm);

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

        public void onScaleChanged(WebView view, float oldScale, float newScale);
    }

    public interface OnHandleClose {
        public void onCloseClick();
    }

    public class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btn_web_back) {
                if (canGoBack()) {
                    wv.goBack();
                }
            } else if (v == btn_web_forward) {
                if (canGoForward()) {
                    goForward();
                }
            } else if (v == btn_web_reflush) {
                String url = wv.getUrl();
                if (url != null && !"".equals(url)) {
                    wv.reload();
                } else {
                    wv.loadData(html, "text/html", "UTF-8");
                }
            } else if (v == btn_close) {
                if (mOnHandleClose != null) {
                    mOnHandleClose.onCloseClick();
                }

            }
        }

    }

}
