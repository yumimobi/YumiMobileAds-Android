package com.yumi.api.ads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.yumi.api.ads.check.CheckSelfPermissionUtils;
import com.yumi.api.ads.mopubdemo.MopubBannerActivity;
import com.yumi.api.ads.mopubdemo.MopubInterstitialActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button bannerAd;
    private Button interstitialAd;

    public static final String MOPUB_UNIT_ID_BANNER = "cd457c5812c84bffaf0d0b328f0af8db";
    public static final String MOPUB_UNIT_ID_INTERSTITIAL = "6fc41aae3e704bac9aacd95ef3081ab5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerAd = (Button) findViewById(R.id.bannerAd);
        interstitialAd = (Button) findViewById(R.id.interstitialAd);
        bannerAd.setOnClickListener(this);
        interstitialAd.setOnClickListener(this);

        final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(MOPUB_UNIT_ID_BANNER);

        MoPub.initializeSdk(this, configBuilder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
            }
        });
        //When the targetSdkVersion of your app is 23 or above, you can choose the following method to check permission and prompt for user authorization
        CheckSelfPermissionUtils.CheckSelfPermission(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bannerAd:
                intent.setClass(MainActivity.this, MopubBannerActivity.class);
                break;
            case R.id.interstitialAd:
                intent.setClass(MainActivity.this, MopubInterstitialActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
