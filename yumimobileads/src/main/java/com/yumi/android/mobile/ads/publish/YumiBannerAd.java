package com.yumi.android.mobile.ads.publish;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yumi.android.mobile.ads.beans.YumiAdBean;
import com.yumi.android.mobile.ads.beans.YumiResponseBean;
import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.publish.enumbean.BannerAdSize;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.publish.enumbean.ErrorCode;
import com.yumi.android.mobile.ads.publish.listener.YumiBannerListener;
import com.yumi.android.mobile.ads.utils.Reporter;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.ZplayHandlerClickActionUtils;
import com.yumi.android.mobile.ads.utils.device.WindowSizeUtils;
import com.yumi.android.mobile.ads.utils.display.WebViewUtils;
import com.yumi.android.mobile.ads.utils.entity.ADSize;
import com.yumi.android.mobile.ads.utils.entity.ReportEntity;
import com.yumi.android.mobile.ads.utils.other.NullCheckUtils;
import com.yumi.android.mobile.ads.utils.other.ThirdAppStarter;
import com.yumi.android.mobile.ads.utils.template.YumiTemplate;
import com.yumi.android.mobile.ads.utils.ui.MyWebView;
import com.yumi.android.mobile.ads.utils.views.YumiBannerView;
import com.yumi.android.mobile.ads.utils.views.YumiWebviewClient;

import java.util.HashMap;
import java.util.Map;

public class YumiBannerAd extends Control {
    private static String TAG = "YumiBannerAd";
    private static boolean onoff = true;

    private YumiBannerListener bannerListener;
    private WebView bannerView;
    private BannerAdSize bannerAdSize;

    private int banner_width;
    private int banner_height;
    private YumiBannerView bannerBox;
    private Handler handler = new Handler();

    public YumiBannerAd(Activity activity, String sspToken, String appId, String placementID, BannerAdSize bannerAdSize, YumiBannerListener bannerListener) {
        super(activity, sspToken, appId, placementID);

        this.bannerListener = bannerListener;
        this.bannerAdSize = bannerAdSize;

        ADSize adSize = ADSize.BANNER_SIZE_320_50;
        if (bannerAdSize == BannerAdSize.BANNER_SIZE_728X90) {
            adSize = ADSize.BANNER_SIZE_728_90;
        }

        banner_width = WindowSizeUtils.dip2px(activity, adSize.getWidth());
        banner_height = WindowSizeUtils.dip2px(activity, adSize.getHeight());
        bannerBox = new YumiBannerView(activity);
    }

    public void requestBanner() {

        bannerView = WebViewUtils
                .buildWebViewWithTransparentBackground(activity);
        bannerView.setWebViewClient(new MyWebViewClient());
        ADSize adSize = ADSize.BANNER_SIZE_320_50;
        if (bannerAdSize == BannerAdSize.BANNER_SIZE_728X90) {
            adSize = ADSize.BANNER_SIZE_728_90;
        }
        requestAd(AdType.TYPE_BANNER, adSize, new Response.Listener<YumiResponseBean>() {
            @Override
            public void onResponse(YumiResponseBean response) {
                setResponse(response);

                if (response == null || response.getFirstAd() == null) {
                    AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                    adError.setErrorMessage("request banner failed , response is null");
                    if(bannerListener != null){
                        bannerListener.onBannerPreparedFailed(adError);
                    }
                    return;
                }

                if (response.getResult() < 0 || NullCheckUtils.isEmptyCollection(response.getAds())) {
                    AdError adError = new AdError(ErrorCode.ERROR_NO_FILL);
                    adError.setErrorMessage("request banner failed no fill");
                    if(bannerListener != null){
                        bannerListener.onBannerPreparedFailed(adError);
                    }
                    return;
                }

                FrameLayout.LayoutParams params_box = new FrameLayout.LayoutParams(
                        banner_width, banner_height);
                params_box.gravity = Gravity.CENTER;
                bannerBox.setLayoutParams(params_box);

                ADSize webSize = handleBannerSize(
                        response.getFirstAd().getW(),
                        response.getFirstAd().getH());
                final FrameLayout.LayoutParams params_web = new FrameLayout.LayoutParams(
                        webSize.getWidth(), webSize.getHeight());
                params_web.gravity = Gravity.CENTER;

                bannerView.setLayoutParams(params_web);
                bannerView.setTag(response);

                String html = YumiTemplate.getYumiBannerTemplate(activity, response.getFirstAd());

                if (html == null) {
                    AdError adError = new AdError(ErrorCode.CODE_FAILED);
                    adError.setErrorMessage("request banner failed ,html is null");
                    if(bannerListener != null){
                        bannerListener.onBannerPreparedFailed(adError);
                    }
                    return;
                }
                ZplayDebug.v_m(TAG, "html=" + html, onoff);
                bannerView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdError adError = new AdError(ErrorCode.ERROR_INTERNAL);
                adError.setErrorMessage("request banner failed, response error : " + error.toString());
                if(bannerListener != null) {
                    bannerListener.onBannerPreparedFailed(adError);
                }
            }
        });
    }


    private class MyWebViewClient extends YumiWebviewClient {

        private boolean isFinish;

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            isFinish = false;

        }

        @Override
        public void onPageFinished(final WebView view, final String url) {

            int progress = view.getProgress();

            ZplayDebug.v_m(TAG, "Banner进度=" + progress + "%", onoff);
            if (progress >= 100 && !isFinish) {
                ZplayDebug.v_m(TAG, "banner加载成功", onoff);
                bannerBox.addView(view);
                if(bannerListener != null) {
                    bannerListener.onBannerPrepared(bannerBox);
                }
                onBannerShow();
                isFinish = true;
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ZplayDebug.v_m(TAG, "过1秒后再次检测进度", onoff);
                        if (url != null) {
                            MyWebViewClient.this.onPageFinished(view, url);
                        }
                    }
                }, 1000);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            final YumiResponseBean response = (YumiResponseBean) view.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            if(responseAd == null){
                return false;
            }

            if (responseAd.getAction() == Constants.actiontype.ACTION_TYPE_DEEPLINK) {
                String deeplinkUrl = responseAd.getTarget_url();
                if (!TextUtils.isEmpty(deeplinkUrl)) {
                    boolean handleDeeplink = ThirdAppStarter.startBrowser(activity, deeplinkUrl);
                    if (!handleDeeplink) {
                        responseAd.setTarget_url(responseAd.getFallback_url());
                        ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
                    }
                } else {
                    responseAd.setTarget_url(responseAd.getFallback_url());
                    ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
                }
            } else {
                if (TextUtils.isEmpty(responseAd.getTarget_url()) || responseAd.getTarget_url().equals("")) {
                    responseAd.setTarget_url(url);
                }
                ZplayHandlerClickActionUtils.handleClickAction(responseAd, activity, view, AdType.TYPE_BANNER);
            }
            onBannerClick();
            ZplayDebug.v_m(TAG, "banner点击", onoff);
            return true;
        }
    }

    /**
     * 将banner尺寸控制在容器内
     *
     * @param img_width
     * @param img_height
     * @return
     */
    private ADSize handleBannerSize(float img_width, float img_height) {
        ZplayDebug.w_m(TAG, "处理前的banner尺寸为:" + img_width + ":" + img_height, onoff);
        float c_width = banner_width;
        float c_height = banner_height;

        float f_w = 0;
        float f_h = 0;
        // 超出的等比缩小
        if (img_width > c_width) {
            float pro = c_width / img_width;
            f_h = img_height * pro;
            f_w = c_width;
        }

        if (f_h > c_height) {
            float pro = c_height / f_h;
            f_w = f_w * pro;
            f_h = c_height;
        }

        /*
         * if (img_height>c_height) { float pro = c_height/img_height; f_w =
         * img_width*pro; f_h = c_height; }
         */

        // 较小的等比放大
        if (f_w == 0 && f_h == 0) {
            float c_pro = c_width / c_height;
            float i_pro = img_width / img_height;
            if (c_pro > i_pro) {
                f_h = (int) c_height;
                f_w = (int) (c_height * i_pro);
            } else {
                f_w = (int) c_width;
                f_h = (int) (c_width / i_pro);
            }
        }
        ZplayDebug.w_m(TAG, "处理后的banner尺寸为:" + f_w + ":" + f_h, onoff);
        return new ADSize((int) f_w, (int) f_h);
    }

    private void onBannerShow() {
        try {
            YumiResponseBean response = (YumiResponseBean) bannerView.getTag();
            YumiAdBean responseAd = response.getFirstAd();

            ReportEntity entity = new ReportEntity(
                    responseAd.getImp_trackers(),
                    null,
                    null);
            Reporter.reportEvent(activity, responseAd.getImp_trackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "onBannerShow report error", e, onoff);
        }
    }


    // @Override
    private void onBannerClick() {
        if(bannerListener != null){
            bannerListener.onBannerClicked();
        }
        try {
            YumiResponseBean response = (YumiResponseBean) bannerView.getTag();
            YumiAdBean responseAd = response.getFirstAd();
            ViewGroup.LayoutParams params = bannerView.getLayoutParams();
            Map<String, String> touchArea = new HashMap<String, String>();
            int showAreaWidth = params.width;
            int showAreaHeight = params.height;
            if (showAreaWidth <= 0) {
                showAreaWidth = bannerView.getWidth();
            }
            if (showAreaHeight <= 0) {
                showAreaHeight = bannerView.getHeight();
            }
            touchArea.put("showAreaWidth", String.valueOf(showAreaWidth));
            touchArea.put("showAreaHeight", String.valueOf(showAreaHeight));

            float[] area = ((MyWebView) bannerView).getLastTouchArea();
            touchArea.put("clickX", String.valueOf(area[0]));
            touchArea.put("clickY", String.valueOf(area[1]));

            float[] downarea = ((MyWebView) bannerView).getLastDownArea();
            touchArea.put("downX", String.valueOf(downarea[0]));
            touchArea.put("downY", String.valueOf(downarea[1]));

            ReportEntity entity = new ReportEntity(
                    null,
                    responseAd.getClick_trackers(),
                    touchArea);
            Reporter.reportEvent(activity, responseAd.getClick_trackers(), entity);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "onBannerClick report error", e, onoff);
        }
    }
}
