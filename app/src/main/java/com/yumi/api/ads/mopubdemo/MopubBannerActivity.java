package com.yumi.api.ads.mopubdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mopub.common.DataKeys;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.api.ads.R;

import java.util.HashMap;
import java.util.Map;

import static com.yumi.api.ads.MainActivity.MOPUB_UNIT_ID_BANNER;


public class MopubBannerActivity extends AppCompatActivity {
    private static String TAG = "MopubBannerActivity";
    private static boolean onoff = true;

    private TextView text;

    private MoPubView moPubView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mopub_banner);
        setAcTitle("MopubBanner");
        initView();
        createBanner();
    }


    public void initView() {
        text = (TextView) findViewById(R.id.textView2);

    }

    private void createBanner() {
        moPubView = new MoPubView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        addContentView(moPubView, params);


        moPubView.setAdUnitId(MOPUB_UNIT_ID_BANNER);

        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                ZplayDebug.i_m(TAG,"onBannerLoaded", onoff);
                setInfo("onBannerLoaded");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                ZplayDebug.i_m(TAG,"onBannerFailed MoPubErrorCode :" + errorCode, onoff);
                setInfo("onBannerFailed MoPubErrorCode :" + errorCode);
            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                ZplayDebug.i_m(TAG,"onBannerClicked", onoff);
                setInfo("onBannerClicked");
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {
                ZplayDebug.i_m(TAG,"onBannerExpanded", onoff);
                setInfo("onBannerExpanded");
            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {
                ZplayDebug.i_m(TAG,"onBannerCollapsed", onoff);
                setInfo("onBannerCollapsed");
            }
        });
    }

    private void setInfo(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (text != null) {
                    text.append(msg + "\n");
                }

            }
        });
    }

    public void loadAd(View view) {
        if(moPubView != null){
            moPubView.loadAd();
        }
    }


    public void setAcTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.tv_title);
        if (tv != null) {
            tv.setText(title);
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(moPubView != null){
            moPubView.destroy();
        }
    }
}
