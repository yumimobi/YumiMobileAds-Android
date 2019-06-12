package com.yumi.api.ads.mopubadapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mopub.common.DataKeys;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;
import com.yumi.android.mobile.ads.publish.AdError;
import com.yumi.android.mobile.ads.publish.YumiBannerAd;
import com.yumi.android.mobile.ads.publish.enumbean.BannerAdSize;
import com.yumi.android.mobile.ads.publish.enumbean.ErrorCode;
import com.yumi.android.mobile.ads.publish.listener.YumiBannerListener;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.views.YumiBannerView;

import java.util.Map;


public class YumiMopubAdxBanner extends CustomEventBanner {
    private static String TAG = "YumiMopubAdxBanner";
    private static boolean onoff = true;

    private YumiBannerAd bannerAd;

    private String SSPTOKEN_KEY = "sspToken";
    private String APPID_KEY = "appId";
    private String PLACEMENT_ID_KEY = "placementId";

    @Override
    protected void loadBanner(Context context, final CustomEventBannerListener mBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {

        if (!(context instanceof Activity)) {
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            ZplayDebug.e_m(TAG, "Banner Load Failed , context not instanceof Activity", onoff);
            return;
        }

        int width;
        int height;
        if (localExtrasAreValid(localExtras)) {
            width = (Integer) localExtras.get(DataKeys.AD_WIDTH);
            height = (Integer) localExtras.get(DataKeys.AD_HEIGHT);
            ZplayDebug.i_m(TAG, "Banner Load size width : " + width + ", height : "  + height, onoff);
        } else {
            ZplayDebug.i_m(TAG, "Banner Load Failed,local Extras Are not Valid", onoff);
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            return;
        }


        String sspToken;
        String appId;
        final String placementId;
        if (serverExtrasAreValid(serverExtras)) {
            sspToken = serverExtras.get(SSPTOKEN_KEY);
            appId = serverExtras.get(APPID_KEY);
            placementId = serverExtras.get(PLACEMENT_ID_KEY);
            ZplayDebug.i_m(TAG, "Banner Load sspToken : " + sspToken , onoff);
            ZplayDebug.i_m(TAG, "Banner Load appId : " + appId , onoff);
            ZplayDebug.i_m(TAG, "Banner Load placementId : " + placementId , onoff);
        } else {
            ZplayDebug.i_m(TAG, "Banner Load Failed,server Extras Are not Valid", onoff);
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            return;
        }

        bannerAd = new YumiBannerAd((Activity) context, sspToken, appId, placementId, calculateAdSize(width, height), new YumiBannerListener() {
            @Override
            public void onBannerPrepared(YumiBannerView bannerView) {
                ZplayDebug.i_m(TAG, "onBannerPrepared", onoff);
                if (mBannerListener != null) {
                    mBannerListener.onBannerLoaded(bannerView);
                }
            }

            @Override
            public void onBannerPreparedFailed(AdError adError) {
                ZplayDebug.i_m(TAG, "onBannerPreparedFailed errorCode:" + adError, onoff);

                if (adError.getErrorCode() == ErrorCode.ERROR_NO_FILL) {
                    if (mBannerListener != null) {
                        mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
                    }
                } else if (adError.getErrorCode() == ErrorCode.ERROR_INTERNAL) {
                    if (mBannerListener != null) {
                        mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                    }
                } else {
                    if (mBannerListener != null) {
                        mBannerListener.onBannerFailed(MoPubErrorCode.UNSPECIFIED);
                    }
                }
            }

            @Override
            public void onBannerClicked() {
                ZplayDebug.i_m(TAG, "onBannerPreparedFailed click", onoff);
                if (mBannerListener != null) {
                    mBannerListener.onBannerClicked();
                }
            }
        });

        bannerAd.requestBanner();
    }

    @Override
    protected void onInvalidate() {
        ZplayDebug.i_m(TAG, "onInvalidate", onoff);
        if (bannerAd != null) {
            bannerAd = null;
        }
    }


    private boolean serverExtrasAreValid(final Map<String, String> serverExtras) {
        final String sspToken = serverExtras.get(SSPTOKEN_KEY);
        final String appId = serverExtras.get(APPID_KEY);
        final String placementId = serverExtras.get(PLACEMENT_ID_KEY);

        return !TextUtils.isEmpty(sspToken) && !TextUtils.isEmpty(appId) && !TextUtils.isEmpty(placementId);
    }


    private boolean localExtrasAreValid(@NonNull final Map<String, Object> localExtras) {
        return localExtras.get(DataKeys.AD_WIDTH) instanceof Integer
                && localExtras.get(DataKeys.AD_HEIGHT) instanceof Integer;
    }


    private BannerAdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        if (width == 320 && height == 50) {
            return BannerAdSize.BANNER_SIZE_320X50;
        } else if (width == 728 && height == 90) {
            return BannerAdSize.BANNER_SIZE_728X90;
        } else {
            return BannerAdSize.BANNER_SIZE_320X50;
        }
    }
}
