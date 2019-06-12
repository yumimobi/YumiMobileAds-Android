package com.yumi.android.mobile.ads.publish.listener;


import com.yumi.android.mobile.ads.publish.AdError;
import com.yumi.android.mobile.ads.utils.views.YumiBannerView;

/**
 * <p> Banner request status callback
 *
 * @author Mikoto
 */
public interface YumiBannerListener {

    /**
     * <p> Invoke when the banner prepared.
     */
    public void onBannerPrepared(YumiBannerView bannerView);

    /**
     * <p>Invoke when the banner prepared failed.
     */
    public void onBannerPreparedFailed(AdError adError);

    /**
     * <p> Invoke when the banner ad has been clicked.
     */
    public void onBannerClicked();

}
