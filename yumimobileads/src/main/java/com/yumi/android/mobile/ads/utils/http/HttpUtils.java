package com.yumi.android.mobile.ads.utils.http;

import android.content.Context;
import android.os.AsyncTask;


import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */
public class HttpUtils {
    private static final boolean onoff = true;
    private static final String TAG = "HttpUtils";
    private static HTTP_TYPE mType = HTTP_TYPE.GET;
    private static PROTOCOL_TYPE mProtocolType = PROTOCOL_TYPE.HTTP;
    public IHttpCallback mCallback = null;
    private String mUrl = null;
    private String mStringParams = null;
    private Context mContext;

    private HttpUtils(Context context, final String url, final String stringParams, IHttpCallback callback) {
        mUrl = url;
        mCallback = callback;
        mStringParams = stringParams;
        mContext = context;

        // check is http or https
        try {
            URL httpUrl = new URL(mUrl);
            if (httpUrl.getProtocol().toLowerCase().equals("https")) {
                mProtocolType = PROTOCOL_TYPE.HTTPS;
            } else {
                mProtocolType = PROTOCOL_TYPE.HTTP;
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "HttpUtils error:", e, onoff);
        }

        if (callback == null) {
            ZplayDebug.e_m(TAG, "callback is null", onoff);
        }

    }

    public static HttpUtils getHttpUtil(Context context, final String url, final String stringParams, IHttpCallback callback) {
        return new HttpUtils(context, url, stringParams, callback);
    }

    public static String getResultData(final Map<String, Object> result) {
        try {
            return (String) result.get("data");
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getResultData error: ", e, onoff);
        }
        return null;
    }

    public static int getResultStatus(final Map<String, Object> result) {
        try {
            return (int) result.get("status");
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getResultStatus error: ", e, onoff);
        }
        return 0;
    }

    /**
     * execute Get request
     */
    public void executeHttpGet() {
        mType = HTTP_TYPE.GET;
        if (mStringParams != null) {
            if (!mUrl.contains("?")) {
                mUrl = mUrl + "?" + mStringParams;
            } else if (mUrl.substring(mUrl.length() - 1).equals("?")) {
                mUrl = mUrl + mStringParams;
            }
        }
        httpAccess(null);
    }


    /**
     * start request
     *
     * @param headers
     */
    private void httpAccess(Map<String, String> headers) {
        try {
            new HttpUrlConnectionTask(mContext, mCallback, mType, mProtocolType, mStringParams, headers).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{mUrl});
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "httpAccess error: ", e, onoff);
        }
    }
}
