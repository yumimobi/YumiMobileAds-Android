package com.yumi.android.mobile.ads.utils.display;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.utils.DeeplinkUtils;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.device.WindowSizeUtils;
import com.yumi.android.mobile.ads.utils.file.DownloadHandler;
import com.yumi.android.mobile.ads.utils.file.FileHandler;
import com.yumi.android.mobile.ads.utils.other.ThirdAppStarter;
import com.yumi.android.mobile.ads.utils.ui.MyExplorer;

/**
 * Browser
 *
 * @author glzlaohuai
 */
@SuppressLint("DefaultLocale")
public class DownloadAPkBrowserBuilder {

    private final static String TAG = "DownloadAPkBrowserHandler";
    private static final boolean onoff = true;
    private static View progress_cont;
    private static int screenWidth;

    @SuppressLint("RtlHardcoded")
    public static void openDownloadAPKBrowser(final Activity activity, final YumiAdBean responseBid, final OnDismissListener onDismissListener) {
        final Dialog dialog = FullDialogBuilder.buildFullDialog(activity);
        dialog.setCancelable(false);
        final FrameLayout contentView = new FrameLayout(activity);
        final MyExplorer explorer = new MyExplorer(activity);

        FrameLayout.LayoutParams params_web = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        contentView.addView(explorer, params_web);

        FrameLayout progress_bg = new FrameLayout(activity);
        FrameLayout.LayoutParams params_pgs = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                WindowSizeUtils.dip2px(activity, 5));
        progress_cont = new View(activity);
        progress_cont.setBackgroundColor(0xff1fbaf2);
        FrameLayout.LayoutParams params_gtsc = new FrameLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params_gtsc.gravity = Gravity.CENTER | Gravity.LEFT;

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;

        progress_bg.addView(progress_cont, params_gtsc);
        contentView.addView(progress_bg, params_pgs);

        dialog.setContentView(contentView);
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    contentView.removeAllViews();
                    explorer.removeAllViews();
                    explorer.destroy();
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "", e, onoff);
                }
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(dialog);
                }
            }
        });

        explorer.setWebViewClient(new MyExplorer.MyWebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            }

            @Override
            public void onLoadResource(WebView view, String url) {
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ZplayDebug.v_m(TAG, "shouldOverrideUrlLoading url: " + url, onoff);
                if (DeeplinkUtils.isDeepLink(url)) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (DeeplinkUtils.deviceCanHandleIntent(activity, intent)) {
                        ThirdAppStarter.startBrowser(activity, url);
                        return true;
                    } else {
                        ZplayDebug.i_m(TAG, "request system browser " + url, onoff);
                        if (!TextUtils.isEmpty(url)) {
                            Intent intentBrowser = new Intent();
                            intentBrowser.setAction("android.intent.action.VIEW");
                            intentBrowser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri content_url = Uri.parse(url);
                            intentBrowser.setData(content_url);
                            PackageManager pm = activity.getPackageManager();
                            if (pm.queryIntentActivities(intent, 0).size() > 0) {
                                activity.startActivity(intent);
                            }
                        }
                        return true;
                    }
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });

        explorer.setmOnHandleClose(new MyExplorer.OnHandleClose() {
            @Override
            public void onCloseClick() {
                dialog.dismiss();
            }
        });

        explorer.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                // ZplayDebug.v(TAG, "progress:"+newProgress);
                if (newProgress < 100) {
                    progress_cont.setVisibility(View.VISIBLE);
                } else {
                    progress_cont.setVisibility(View.GONE);
                }
                int progress_width = newProgress * screenWidth / 100;
                LayoutParams layoutParams = (LayoutParams) progress_cont.getLayoutParams();
                layoutParams.width = progress_width;
                progress_cont.setLayoutParams(layoutParams);
                super.onProgressChanged(view, newProgress);
            }
        });
        explorer.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                ZplayDebug.v_m(TAG, "click download url：" + url, onoff);
                String extension = FileHandler.getFileExtensionName(url);
                ZplayDebug.v_m(TAG, "The file suffix pointed to by the link is：" + extension, onoff);
                if (extension.trim().toLowerCase().equals("apk")) {
                            ZplayDebug.v_m(TAG, "is apk file，start download...", onoff);
                            responseBid.setTargetUrl(url);
                            DownloadHandler.startDownload(activity, responseBid, false);
                            dialog.dismiss();
                } else {
                    ZplayDebug.v_m(TAG, "not is apk file，not work...", onoff);
                }
            }
        });
        explorer.loadUrl(responseBid.getTargetUrl());
        dialog.show();
    }





}
