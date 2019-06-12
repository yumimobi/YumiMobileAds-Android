package com.yumi.api.ads.mopubadapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;
import com.yumi.android.mobile.ads.publish.AdError;
import com.yumi.android.mobile.ads.publish.YumiInterstitialAd;
import com.yumi.android.mobile.ads.publish.enumbean.ErrorCode;
import com.yumi.android.mobile.ads.publish.enumbean.InterstitialAdSize;
import com.yumi.android.mobile.ads.publish.listener.YumiInterstitialListener;
import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.util.Map;

public class YumiMopubAdxInterstitial extends CustomEventInterstitial {
    private static String TAG = "YumiMopubAdxInterstitial";
    private static boolean onoff = true;

    private YumiInterstitialAd interstitialAd;
    private String SSPTOKEN_KEY = "sspToken";
    private String APPID_KEY = "appId";
    private String PLACEMENT_ID_KEY = "placementId";

    @Override
    protected void loadInterstitial(Context context, final CustomEventInterstitialListener interstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (!(context instanceof Activity)) {
            if (interstitialListener != null) {
                interstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            ZplayDebug.e_m(TAG, "Interstitial Load Failed , context not instanceof Activity", onoff);
            return;
        }

        String sspToken;
        String appId;
        final String placementId;
        if (serverExtrasAreValid(serverExtras)) {
            sspToken = serverExtras.get(SSPTOKEN_KEY);
            appId = serverExtras.get(APPID_KEY);
            placementId = serverExtras.get(PLACEMENT_ID_KEY);
            ZplayDebug.i_m(TAG, "Interstitial Load sspToken : " + sspToken , onoff);
            ZplayDebug.i_m(TAG, "Interstitial Load appId : " + appId , onoff);
            ZplayDebug.i_m(TAG, "Interstitial Load placementId : " + placementId , onoff);
        } else {
            ZplayDebug.i_m(TAG, "Interstitial Load Failed,server Extras Are not Valid", onoff);
            if (interstitialListener != null) {
                interstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            return;
        }

        interstitialAd = new YumiInterstitialAd((Activity) context, sspToken, appId, placementId, InterstitialAdSize.INTERSTITIAL_SIZE_640X960, new YumiInterstitialListener() {
            @Override
            public void onInterstitialPrepared() {
                ZplayDebug.i_m(TAG, "onInterstitialPrepared", onoff);
                if (interstitialListener != null) {
                    interstitialListener.onInterstitialLoaded();
                }
            }

            @Override
            public void onInterstitialPreparedFailed(AdError adError) {
                ZplayDebug.i_m(TAG, "onInterstitialPreparedFailed" + adError, onoff);

                if (adError.getErrorCode() == ErrorCode.ERROR_NO_FILL) {
                    if(interstitialListener != null) {
                        interstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
                    }
                } else if (adError.getErrorCode() == ErrorCode.ERROR_INTERNAL) {
                    if(interstitialListener != null) {
                        interstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
                    }
                } else {
                    if(interstitialListener != null) {
                        interstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
                    }
                }
            }

            @Override
            public void onInterstitialExposure() {
                ZplayDebug.i_m(TAG, "onInterstitialExposure", onoff);
                if(interstitialListener != null) {
                    interstitialListener.onInterstitialImpression();
                }
            }

            @Override
            public void onInterstitialClicked() {
                ZplayDebug.i_m(TAG, "onInterstitialClicked", onoff);
                if(interstitialListener != null) {
                    interstitialListener.onInterstitialClicked();
                }
            }

            @Override
            public void onInterstitialClosed() {
                ZplayDebug.i_m(TAG, "onInterstitialClosed", onoff);
                if(interstitialListener != null) {
                    interstitialListener.onInterstitialDismissed();
                }
            }
        });

        interstitialAd.requestInterstitial();
    }

    @Override
    protected void showInterstitial() {
        ZplayDebug.i_m(TAG, "showInterstitial", onoff);
        if (interstitialAd != null) {
            interstitialAd.showInterstitial();
        }
    }

    @Override
    protected void onInvalidate() {
        ZplayDebug.i_m(TAG, "onInvalidate", onoff);
        if (interstitialAd != null) {
            interstitialAd = null;
        }
    }

    private boolean serverExtrasAreValid(final Map<String, String> serverExtras) {
        final String sspToken = serverExtras.get(SSPTOKEN_KEY);
        final String appId = serverExtras.get(APPID_KEY);
        final String placementId = serverExtras.get(PLACEMENT_ID_KEY);

        return !TextUtils.isEmpty(sspToken) && !TextUtils.isEmpty(appId) && !TextUtils.isEmpty(placementId);
    }

}
