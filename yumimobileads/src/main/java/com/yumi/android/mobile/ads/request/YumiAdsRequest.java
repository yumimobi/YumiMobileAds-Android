package com.yumi.android.mobile.ads.request;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.yumi.android.mobile.ads.beans.YumiResponseBean;
import com.yumi.android.mobile.ads.constants.YumiMobileAPIList;
import com.yumi.android.mobile.ads.publish.enumbean.AdType;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.device.PhoneInfoGetter;
import com.yumi.android.mobile.ads.utils.entity.ADSize;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Description:
 * <p>
 * Created by lgd on 2019/4/16.
 */
public class YumiAdsRequest extends YumiRequest<YumiResponseBean> {
    private static final String TAG = "YumiAdsRequest";
    private static boolean onoff = true;

    private RequestInfo mRequestInfo;


    public YumiAdsRequest(RequestInfo requestInfo, Response.Listener<YumiResponseBean> successListener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, YumiMobileAPIList.REQUEST_ADS_URL(), successListener, errorListener);
        mRequestInfo = requestInfo;
        setShouldCache(false);
    }

    @Override
    protected Response<YumiResponseBean> parseNetworkResponse(NetworkResponse response) {

        boolean isLocalConfig = false;
        String responseData = null;
        if (response.statusCode != 200) {

        } else {
            responseData = new String(response.data);
        }

        if (TextUtils.isEmpty(responseData)) {
            ZplayDebug.d_m(TAG, "parseNetworkResponse: result responseData is empty.", onoff);
            return Response.error(new VolleyError("cannot parse config response.", new Throwable()));
        }
        ZplayDebug.i_m(TAG, "responseData = " + responseData, onoff);
        YumiResponseBean responseBean = new Gson().fromJson(responseData, YumiResponseBean.class);
        if (responseBean == null) {
            ZplayDebug.d_m(TAG, "parseNetworkResponse: cannot parse config response.  Data: " + new String(response.data), onoff);
            return Response.error(new VolleyError("cannot parse config response.", new Throwable()));
        }

        return Response.success(responseBean, HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    public byte[] getBody() {

        try {
            JSONObject parme = new JSONObject();
            parme.put("ver", "1.1"); // 唯一ID，需要由聚合SDK提供，等同于聚合的pid
            parme.put("ssp_token", mRequestInfo.ssp_token);
            parme.put("app", RequestParamsBuilder.buildAppParam(mRequestInfo.activity, mRequestInfo.id));
            parme.put("device", RequestParamsBuilder.buildDeviceParam(mRequestInfo.activity));
            parme.put("ads", RequestParamsBuilder.buildAdsParam(mRequestInfo.activity, mRequestInfo.adType, mRequestInfo.placementID, mRequestInfo.adSize));
            parme.put("user", RequestParamsBuilder.buildUserParam());
            parme.put("regs", RequestParamsBuilder.buildRegsParam());
            String requestParams = parme.toString();
            return requestParams.getBytes();
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "requset data error ", e, onoff);
            return new byte[0];
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("X-Forwarded-For", mRequestInfo.ip);
        headers.put("User-Agent", PhoneInfoGetter.getUserAgent(mRequestInfo.activity));
        return headers;
    }


    public static final class RequestInfo {
        final Activity activity;
        final String ssp_token;
        final String id;
        final AdType adType;
        final String placementID;
        final ADSize adSize;
        final String ip;

        public RequestInfo(Activity activity, String ssp_token, AdType adType, String id, String placementID, ADSize adSize, String ip) {
            this.activity = activity;
            this.ssp_token = ssp_token;
            this.adType = adType;
            this.id = id;
            this.placementID = placementID;
            this.adSize = adSize;
            this.ip = ip;
        }
    }

    public void deliverError(VolleyError error) {
        if (error.networkResponse != null
                && error.networkResponse.statusCode != 200) {
            ZplayDebug.d_m(TAG, "request statusCode: " + error.networkResponse.statusCode, onoff);
            Response<?> mResponse = parseNetworkResponse(new NetworkResponse(error.networkResponse.statusCode, null, false, 0, Collections.<Header>emptyList()));
            if (mResponse.isSuccess()) {
                Response response = mResponse;
                deliverResponse((YumiResponseBean) response.result);
                return;
            }
        }
        super.deliverError(error);
    }
}
