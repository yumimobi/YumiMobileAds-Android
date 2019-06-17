package com.yumi.android.mobile.ads.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.beans.YumiResponseBean;
import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.publish.enumbean.ErrorCode;
import com.yumi.android.mobile.ads.publish.enumbean.InterstitialAdSize;
import com.yumi.android.mobile.ads.publish.listener.YumiInterstitialListener;
import com.yumi.android.mobile.ads.utils.Reporter;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.ZplayHandlerClickActionUtils;
import com.yumi.android.mobile.ads.utils.device.PhoneInfoGetter;
import com.yumi.android.mobile.ads.utils.device.WindowSizeUtils;
import com.yumi.android.mobile.ads.utils.display.FullDialogBuilder;
import com.yumi.android.mobile.ads.utils.display.WebViewUtils;
import com.yumi.android.mobile.ads.utils.entity.ADSize;
import com.yumi.android.mobile.ads.utils.entity.ReportEntity;
import com.yumi.android.mobile.ads.utils.other.NullCheckUtils;
import com.yumi.android.mobile.ads.utils.other.ThirdAppStarter;
import com.yumi.android.mobile.ads.utils.template.YumiTemplate;
import com.yumi.android.mobile.ads.utils.ui.MyWebView;
import com.yumi.android.mobile.ads.utils.ui.ResFactory;
import com.yumi.android.mobile.ads.utils.views.YumiWebviewClient;

import java.util.HashMap;
import java.util.Map;

public class YumiInterstitialAd extends Control {
    private static String TAG = "YumiInterstitialAd";
    private static boolean onoff = true;
    private static final int BUTTON_DEFAULT_SIZE = 25;

    private YumiInterstitialListener listener;
    private InterstitialAdSize interstitialAdSize;

    private FrameLayout interstitialDialogView;
    private WebView wv;
    private Dialog interstitialDialog;
    private boolean isHtmlLoadFinish;
    private InterstitialStatus status;

    private Handler handler = new Handler();

    public YumiInterstitialAd(Activity activity, String sspToken, String appId, String placementID, InterstitialAdSize interstitialAdSize, YumiInterstitialListener interstitialListener) {
        super(activity, sspToken, appId, placementID);

        this.listener = interstitialListener;
        this.interstitialAdSize = interstitialAdSize;
        this.status = InterstitialStatus.NO_ACTION;

    }

    public void requestInterstitial() {

        if (status == InterstitialStatus.PREPARED) {
            ZplayDebug.v_m(TAG, "interstitial is ready", onoff);
            if (listener != null) {
                listener.onInterstitialPrepared();
            }
            return;
        }
        if (status == InterstitialStatus.REQUESTING) {
            ZplayDebug.v_m(TAG, "interstitial is readying", onoff);
            if (listener != null) {
                AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                adError.setErrorMessage("request interstitial failed, interstitial is loding");
                listener.onInterstitialPreparedFailed(adError);
            }
            return;
        }
        if (activity == null) {
            status = InterstitialStatus.NO_ACTION;
            if (listener != null) {
                AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                adError.setErrorMessage("request interstitial failed, activity is null");
                listener.onInterstitialPreparedFailed(adError);
            }
            return;
        }

        status = InterstitialStatus.REQUESTING;

        ADSize adSize;
        if (interstitialAdSize == InterstitialAdSize.INTERSTITIAL_SIZE_250X300) {
            adSize = ADSize.INTERSTITIAL_SIZE_250_300;
        } else if (interstitialAdSize == InterstitialAdSize.INTERSTITIAL_SIZE_500X600) {
            adSize = ADSize.INTERSTITIAL_SIZE_500_600;
        } else {
            adSize = ADSize.INTERSTITIAL_SIZE_640_960;
        }

        requestAd(AdType.TYPE_INTERSTITIAL, adSize, new Response.Listener<YumiResponseBean>() {
            @Override
            public void onResponse(YumiResponseBean response) {
                setResponse(response);

                if (response == null || response.getFirstAd() == null) {
                    status = InterstitialStatus.NO_ACTION;
                    if (listener != null) {
                        AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                        adError.setErrorMessage("request interstitial failed , response is null");
                        listener.onInterstitialPreparedFailed(adError);
                    }
                    return;
                }

                if (response.getResult() < 0 || NullCheckUtils.isEmptyCollection(response.getAds())) {
                    status = InterstitialStatus.NO_ACTION;
                    if (listener != null) {
                        AdError adError = new AdError(ErrorCode.ERROR_NO_FILL);
                        adError.setErrorMessage("request interstitial failed no fill");
                        listener.onInterstitialPreparedFailed(adError);
                    }
                    return;
                }

                createView(response.getFirstAd());

                // 设定关闭事件
                interstitialDialog.setOnDismissListener(newDismissListener());
                interstitialDialog.setOnShowListener(newShowListener());

                ZplayDebug.w_m(TAG, "Preload in advance", onoff);
                wv.setTag(response);

                String html = YumiTemplate.getYumiInterstitialTemplate(activity, response.getFirstAd());
                ZplayDebug.v_m(TAG, "html=" + html, onoff);

                if (html == null) {
                    status = InterstitialStatus.NO_ACTION;
                    if (listener != null) {
                        AdError adError = new AdError(ErrorCode.CODE_FAILED);
                        adError.setErrorMessage("request interstitial failed, html is null");
                        listener.onInterstitialPreparedFailed(adError);
                    }
                    return;
                }

                wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                adError.setErrorMessage("request interstitial failed, response error : " + error.toString());
                if (listener != null) {
                    listener.onInterstitialPreparedFailed(adError);
                }
            }
        });
    }

    /**
     * 展示插屏广告
     */
    public void showInterstitial() {
        if (status != InterstitialStatus.PREPARED) {
            return;
        }

        interstitialDialog.show();
    }

    private DialogInterface.OnShowListener newShowListener() {
        return new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                onInterstitialShow();
            }
        };
    }

    private OnDismissListener newDismissListener() {
        return new OnDismissListener() {
            public void onDismiss(DialogInterface dialog1) {
                status = InterstitialStatus.NO_ACTION;
                ZplayDebug.v_m(TAG, "Interstitial closeed", onoff);
                try {
                    if (interstitialDialogView != null) {
                        interstitialDialogView.removeAllViews();
                        if (wv != null) {
                            wv.destroy();
                        }
                    }
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "", e, onoff);
                }
                onInterstitialClose();
            }
        };
    }

    /**
     * create interstitial ad view
     */
    @SuppressLint("RtlHardcoded")
    @SuppressWarnings("deprecation")
    private void createView(YumiAdBean responseAd) {
        try {
            // create interstitial container
            interstitialDialogView = new FrameLayout(activity);
            interstitialDialogView.setLayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            interstitialDialogView.setBackgroundColor(0x88000000);
            // create interstitial house
            FrameLayout interstitialDialogHouse = new FrameLayout(activity);
            FrameLayout.LayoutParams house_param = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            house_param.gravity = Gravity.CENTER;
            interstitialDialogView.addView(interstitialDialogHouse, house_param);

            // create WebView
            int width = WindowSizeUtils.dip2px(activity, responseAd.getW());
            int height = WindowSizeUtils.dip2px(activity, responseAd.getH());
            int[] displayMetrics = PhoneInfoGetter.getDisplayMetrics(activity);
            ADSize intersititialSize = getBenefitSize(width, height,
                    (int) (displayMetrics[0] * 0.8f),
                    (int) (displayMetrics[1] * 0.8f));
            ZplayDebug.v_m(TAG, "interstitial size==" + intersititialSize.getWidth() + ":"
                    + intersititialSize.getHeight(), onoff);
            interstitialDialogHouse.getLayoutParams().width = intersititialSize.getWidth();
            interstitialDialogHouse.getLayoutParams().height = intersititialSize.getHeight();

            wv = WebViewUtils.buildWebViewWithTransparentBackground(activity);
            wv.setWebViewClient(new MyWebViewClient());

            //wv.setId(ID_WEBVIEW);
            FrameLayout.LayoutParams web_param = new FrameLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            interstitialDialogHouse.addView(wv, web_param);

            Button closeBtn = new Button(activity);
            closeBtn.setBackgroundDrawable(
                    ResFactory.getDrawableByAssets("zplayadx_btn_close", activity));

            FrameLayout.LayoutParams closeParam;
            closeParam = new FrameLayout.LayoutParams(
                    WindowSizeUtils.dip2px(activity, BUTTON_DEFAULT_SIZE),
                    WindowSizeUtils.dip2px(activity, BUTTON_DEFAULT_SIZE));
            FrameLayout closeBtnFucker = new FrameLayout(activity);
            FrameLayout.LayoutParams closeBtnFuckerParam = new FrameLayout.LayoutParams(
                    WindowSizeUtils.dip2px(activity, BUTTON_DEFAULT_SIZE),
                    WindowSizeUtils.dip2px(activity, BUTTON_DEFAULT_SIZE));
            closeBtn.setClickable(false);

            closeBtnFuckerParam.gravity = Gravity.TOP | Gravity.RIGHT;
            closeParam.gravity = Gravity.TOP | Gravity.RIGHT;

            closeBtn.setLayoutParams(closeParam);
            closeBtnFucker.setLayoutParams(closeBtnFuckerParam);
            interstitialDialogHouse.addView(closeBtn);
            interstitialDialogHouse.addView(closeBtnFucker);

            // set close button click
            closeBtnFucker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interstitialDialog.cancel();
                }
            });

            interstitialDialog = FullDialogBuilder.buildFullDialog(activity);
            interstitialDialog.setContentView(interstitialDialogView);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, e.getMessage(), e, onoff);
        }
    }

    private ADSize getBenefitSize(int width, int height, int maxWidth, int maxHeight) {
        if (width <= maxWidth && height <= maxHeight) {
            return new ADSize(width, height);
        } else {
            float scale = width * 1.0f / height;
            // Change the height of the picture to the same height as the screen
            int scaledWidth = (int) (scale * maxHeight);
            if (scaledWidth > maxWidth) {
                int scaleHeight = (int) (maxWidth / scale);
                return new ADSize(maxWidth, scaleHeight);
            } else {
                return new ADSize(scaledWidth, maxHeight);
            }

        }
    }


    private class MyWebViewClient extends YumiWebviewClient {

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            interstitialDialog.cancel();
            final YumiResponseBean response = (YumiResponseBean) view.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            if (responseAd == null) {
                return false;
            }

            if (responseAd.getAction() == Constants.actiontype.ACTION_TYPE_DEEPLINK) {
                String deeplinkUrl = responseAd.getTargetUrl();
                if (!TextUtils.isEmpty(deeplinkUrl)) {
                    boolean handleDeeplink = ThirdAppStarter.startBrowser(activity, deeplinkUrl);
                    if (!handleDeeplink) {
                        responseAd.setTargetUrl(responseAd.getFallbackUrl());
                        ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
                    }
                } else {
                    responseAd.setTargetUrl(responseAd.getFallbackUrl());
                    ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
                }
            } else {
                if (TextUtils.isEmpty(responseAd.getTargetUrl()) || responseAd.getTargetUrl().equals("")) {
                    responseAd.setTargetUrl(url);
                }
                ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
            }
            onInterstitialClick();
            return true;
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {

            int progress = view.getProgress();
            ZplayDebug.v_m(TAG, "interstitial progress=" + progress + "%", onoff);
            if (progress >= 100 && !isHtmlLoadFinish) {
                isHtmlLoadFinish = true;
                onInterstitialPrepare();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ZplayDebug.v_m(TAG, "Check the progress again after 1 second", onoff);
                        onPageFinished(view, url);
                    }
                }, 1000);
            }
        }

    }

    private void onInterstitialPrepare() {
        if (listener != null && isHtmlLoadFinish) {
            ZplayDebug.i_m(TAG, "onInterstitialPrepare", onoff);
            status = InterstitialStatus.PREPARED;
            if (listener != null) {
                listener.onInterstitialPrepared();
            }
        }
    }


    public enum InterstitialStatus {
        NO_ACTION, REQUESTING, PREPARED
    }


    private void onInterstitialClick() {
        if (listener != null) {
            listener.onInterstitialClicked();
        }
        try {
            YumiResponseBean response = (YumiResponseBean) wv.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            LayoutParams params = wv.getLayoutParams();
            Map<String, String> touchArea = new HashMap<String, String>();

            int showAreaWidth = params.width;
            int showAreaHeight = params.height;
            if (showAreaWidth <= 0) {
                showAreaWidth = ((MyWebView) wv).getWidth();
            }
            if (showAreaHeight <= 0) {
                showAreaHeight = ((MyWebView) wv).getHeight();
            }

            touchArea.put("showAreaWidth", String.valueOf(showAreaWidth));
            touchArea.put("showAreaHeight", String.valueOf(showAreaHeight));

            float[] area = ((MyWebView) wv).getLastTouchArea();
            touchArea.put("clickX", String.valueOf(area[0]));
            touchArea.put("clickY", String.valueOf(area[1]));

            float[] downarea = ((MyWebView) wv).getLastDownArea();
            touchArea.put("downX", String.valueOf(downarea[0]));
            touchArea.put("downY", String.valueOf(downarea[1]));

            ReportEntity entity = new ReportEntity(
                    null,
                    responseAd.getClickTrackers(),
                    touchArea);
            Reporter.reportEvent(activity, responseAd.getClickTrackers(), entity);
        } catch (Exception e) {
            ZplayDebug.v_m(TAG, "onInterstitialClick report error", onoff);
        }
    }

    private void onInterstitialShow() {
        if (listener != null) {
            listener.onInterstitialExposure();
        }
        try {
            YumiResponseBean response = (YumiResponseBean) wv.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            ReportEntity entity = new ReportEntity(
                    responseAd.getImpTrackers(),
                    null,
                    null);
            Reporter.reportEvent(activity, responseAd.getImpTrackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "onInterstitialShow report error", e, onoff);
        }
    }

    private void onInterstitialClose() {
        if (listener != null) {
            listener.onInterstitialClosed();
        }
        try {
            YumiResponseBean response = (YumiResponseBean) wv.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            ReportEntity entity = new ReportEntity(
                    null,
                    null,
                    null);
            Reporter.reportEvent(activity, responseAd.getCloseTrackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "onInterstitialClose report error", e, onoff);
        }
    }
}
