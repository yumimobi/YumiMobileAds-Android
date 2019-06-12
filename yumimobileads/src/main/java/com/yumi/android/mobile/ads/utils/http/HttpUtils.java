package com.yumi.android.mobile.ads.utils.http;

import android.content.Context;
import android.os.AsyncTask;


import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.net.URL;
import java.net.URLEncoder;
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

        // 判断是http请求还是https请求
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

    /**
     * 构造形如"params1=values1&params2=values2..."形式的字符串
     *
     * @param keys   参数名
     * @param values 参数值
     * @return
     */
    public static String buildPostParams(String[] keys, String[] values) {
        StringBuilder sb = new StringBuilder();
        try {
            if (keys != null && keys.length > 0) {
                for (int i = 0; i < keys.length; i++) {
                    sb.append(keys[i]).append("=").append(URLEncoder.encode(values[i], "utf-8")).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildParams error: ", e, onoff);
        }
        return sb.toString();
    }

    /**
     * 构造形如"?params1=values1&params2=values2..."形式的字符串
     *
     * @param keys   参数名
     * @param values 参数值
     * @return
     */
    public static String buildGetParams(String[] keys, String[] values) {
        StringBuilder sb = new StringBuilder();
        try {
            if (keys != null && keys.length > 0) {
                // sb2中数据应该是URLEncode以后的"?a=1&b=2&c=3"
                for (int i = 0; i < keys.length; i++) {
                    sb.append(keys[i]).append("=").append(URLEncoder.encode(values[i], "utf-8")).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildParams error: ", e, onoff);
        }
        return sb.toString();
    }

    /**
     * 构造形如"?params1=values1&params2=values2..."形式的字符串, 参数value不编码
     *
     * @param keys   参数名
     * @param values 参数值
     * @return
     */
    public static String buildGetParamsNotEncode(String[] keys, String[] values) {
        StringBuilder sb = new StringBuilder();
        try {
            if (keys != null && keys.length > 0) {
                // sb2中数据应该是URLEncode以后的"?a=1&b=2&c=3"
                for (int i = 0; i < keys.length; i++) {
                    sb.append(keys[i]).append("=").append(values[i]).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "buildParams error: ", e, onoff);
        }
        return sb.toString();
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

    public static String getResultMsg(final Map<String, Object> result) {
        try {
            return (String) result.get("msg");
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "getResultStatus error: ", e, onoff);
        }
        return "";
    }

    /**
     * 执行Get请求
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
     * 执行Post请求
     */
    public void executeHttpPost() {
        mType = HTTP_TYPE.POST;
        httpAccess(null);
    }

    /**
     * 添加Headers 并执行Get请求
     *
     * @param headers 请求头
     */
    public void executeHttpGet(Map<String, String> headers) {
        mType = HTTP_TYPE.GET;
        if (mStringParams != null) {
            if (!mUrl.contains("?")) {
                mUrl = mUrl + "?" + mStringParams;
            } else if (mUrl.substring(mUrl.length() - 1).equals("?")) {
                mUrl = mUrl + mStringParams;
            }
        }
        httpAccess(headers);
    }

    /**
     * 添加Headers 并执行Post请求
     *
     * @param headers 请求头
     */
    public void executeHttpPost(Map<String, String> headers) {
        mType = HTTP_TYPE.POST;
        httpAccess(headers);
    }

    /**
     * 开始异步网络请求
     *
     * @param headers 请求头
     */
    private void httpAccess(Map<String, String> headers) {
        try {
            new HttpUrlConnectionTask(mContext, mCallback, mType, mProtocolType, mStringParams, headers).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{mUrl});
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "httpAccess error: ", e, onoff);
        }
    }
}
