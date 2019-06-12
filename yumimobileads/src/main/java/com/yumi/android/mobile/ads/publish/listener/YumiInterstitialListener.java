package com.yumi.android.mobile.ads.publish.listener;

import com.yumi.android.mobile.ads.publish.AdError;

public interface YumiInterstitialListener {
    /**
     * <p> Invoke when the interstitial prepared.
     */
    public void onInterstitialPrepared();

    /**
     * <p>Invoke when the interstitial prepared failed.
     */
    public void onInterstitialPreparedFailed(AdError adError);

    /**
     * <p> Invoke when the interstitial exposure on screen.
     */
    public void onInterstitialExposure();

    /**
     * <p> Invoke when the interstitial ad has been clicked.
     */
    public void onInterstitialClicked();

    /**
     * <p> Invoke when the interstitial ad closed.
     *
     */
    public void onInterstitialClosed();
}
