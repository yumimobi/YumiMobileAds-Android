package com.yumi.android.mobile.ads.publish;

import android.app.Activity;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.yumi.android.mobile.ads.beans.YumiResponseBean;
import com.yumi.android.mobile.ads.constants.YumiMobileAPIList;
import com.yumi.android.mobile.ads.publish.enumbean.BannerAdSize;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.request.YumiAdsRequest;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.entity.ADSize;
import com.yumi.android.mobile.ads.utils.http.HttpUtils;
import com.yumi.android.mobile.ads.utils.http.IHttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public abstract class Control {

    protected static final boolean onoff = true;
    private static final String TAG = "Control";
    protected Activity activity;
    protected Context mContext;
    static RequestQueue sConfigRequestQueue;
    private YumiResponseBean response;

    private String sspToken;
    private String appId;
    private String placementID;
    private String ip = "127.0.0.1";


    protected Control(Activity activity, String sspToken, String appId, String placementID) {
        this.activity = activity;
        this.sspToken = sspToken;
        this.appId = appId;
        this.placementID = placementID;
        this.mContext = activity.getApplicationContext();
        if (sConfigRequestQueue == null) {
            sConfigRequestQueue = Volley.newRequestQueue(mContext);
        }

        HttpUtils.getHttpUtil(activity, YumiMobileAPIList.REQUEST_GET_IP(), null, new IHttpCallback() {
            @Override
            public void onResponse(Map<String, Object> resule) {
                int status = HttpUtils.getResultStatus(resule);
                String data = HttpUtils.getResultData(resule);
                ZplayDebug.i_m(TAG, "get ip status: " + status + ", data : " + data, onoff);
                if (status == 200 && data != null) {
                    try {
                        JSONObject ipData = new JSONObject(data);
                        ip = ipData.getString("IP");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).executeHttpGet();

    }


    public void requestAd(AdType adType, ADSize adSize, Response.Listener<YumiResponseBean> successListener, Response.ErrorListener errorListener) {


        YumiAdsRequest.RequestInfo requestInfo = new YumiAdsRequest.RequestInfo(activity, sspToken, adType, appId, placementID, adSize, ip);
        YumiAdsRequest configRequest = new YumiAdsRequest(requestInfo, successListener, errorListener);
        configRequest.setTag(this);
        sConfigRequestQueue.add(configRequest);
    }

    public YumiResponseBean getResponse() {
        return response;
    }

    public void setResponse(YumiResponseBean response) {
        this.response = response;
    }
}
