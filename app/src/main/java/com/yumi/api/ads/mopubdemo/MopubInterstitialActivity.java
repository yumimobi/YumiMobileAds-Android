package com.yumi.api.ads.mopubdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.yumi.android.mobile.ads.publish.AdError;
import com.yumi.android.mobile.ads.publish.YumiInterstitialAd;
import com.yumi.android.mobile.ads.publish.enumbean.InterstitialAdSize;
import com.yumi.android.mobile.ads.publish.listener.YumiInterstitialListener;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.api.ads.R;

import static com.yumi.api.ads.MainActivity.MOPUB_UNIT_ID_INTERSTITIAL;


public class MopubInterstitialActivity extends AppCompatActivity implements OnClickListener {
    private static String TAG = "MopubInterstitialActivity";
    private static boolean onoff = true;

    private TextView info;
    private Button loadAd;
    private Button show;

    private MoPubInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_mopub_interstitial);

        setAcTitle("MopubInterstitial");
        info = (TextView) findViewById(R.id.info);
        loadAd = (Button) findViewById(R.id.loadAd);
        show = (Button) findViewById(R.id.show);
        loadAd.setOnClickListener(this);
        show.setOnClickListener(this);
        createInterstitial();
    }

// banner
    private void createInterstitial() {

        mInterstitial = new MoPubInterstitial(this, MOPUB_UNIT_ID_INTERSTITIAL);
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                ZplayDebug.i_m(TAG,"onInterstitialLoaded", onoff);
                setInfo("onInterstitialLoaded");
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                ZplayDebug.i_m(TAG,"onInterstitialFailed MoPubErrorCode : " + errorCode, onoff);
                setInfo("onInterstitialFailed MoPubErrorCode : " + errorCode);
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
                ZplayDebug.i_m(TAG,"onInterstitialShown", onoff);
                setInfo("onInterstitialShown");
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                ZplayDebug.i_m(TAG,"onInterstitialClicked", onoff);
                setInfo("onInterstitialClicked");
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                ZplayDebug.i_m(TAG,"onInterstitialDismissed", onoff);
                setInfo("onInterstitialDismissed");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadAd:
                if (mInterstitial != null) {
                    mInterstitial.load();
                }
                break;
            case R.id.show:
                if (mInterstitial != null && mInterstitial.isReady()) {
                    mInterstitial.show();
                }
                break;
            default:
                break;
        }
    }


    private void setInfo(final String msg) {
        Log.e("mikoto", "set info  thread " + Thread.currentThread().getId());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (info != null) {
                    info.append(msg + "\n");
                }
            }
        });
    }

    public void setAcTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.tv_title);
        if (tv != null) {
            tv.setText(title);
            tv.setVisibility(View.VISIBLE);
        }
    }
}
