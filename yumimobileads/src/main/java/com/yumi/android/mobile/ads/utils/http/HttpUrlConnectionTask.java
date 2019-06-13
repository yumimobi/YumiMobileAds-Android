package com.yumi.android.mobile.ads.utils.http;

import android.content.Context;
import android.os.AsyncTask;

import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.network.NetworkStatusHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by hjl on 2017/7/25.
 */
public class HttpUrlConnectionTask extends AsyncTask<String, Void, Map<String, Object>> {

    private static final boolean onoff = true;
    private static final String TAG = "HttpUrlConnectionTask";
    static TrustManager[] xtmArray = new MytmArray[]{new MytmArray()};
    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    };
    private final int CONNECTION_TIMEOUT = 1000;
    private final int READ_TIMEOUT = 4000;
    private IHttpCallback mIHttpCallback = null;
    private HTTP_TYPE mType = HTTP_TYPE.GET;
    private PROTOCOL_TYPE mProtocolType = PROTOCOL_TYPE.HTTP;
    private String mParams = "";
    private Map<String, String> mHeaders = null;
    private Context mContext;

    public HttpUrlConnectionTask(Context context, IHttpCallback callback, HTTP_TYPE type, PROTOCOL_TYPE protocolType, String params) {
        super();
        mIHttpCallback = callback;
        mType = type;
        mParams = params;
        mProtocolType = protocolType;
        mContext = context;
    }

    public HttpUrlConnectionTask(Context context, IHttpCallback callback, HTTP_TYPE type, PROTOCOL_TYPE protocolType, String params, Map<String, String> headers) {
        super();
        mIHttpCallback = callback;
        mType = type;
        mParams = params;
        mProtocolType = protocolType;
        mHeaders = headers;
        mContext = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, Object> doInBackground(String... urls) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!NetworkStatusHandler.isNetWorkAvaliable(mContext)) {
            result.put("status", -1);
            result.put("data", "");
            result.put("msg", "NetWork Error");
            ZplayDebug.v_m(TAG, "NetWork Error", onoff);
            return result;
        }
        if (urls == null || urls.length == 0) {
            result.put("status", -2);
            result.put("data", "");
            result.put("msg", "urls == null");
            return result;
        }
        StringBuilder data = new StringBuilder();
        HttpURLConnection httpUrlCon = null;
        try {
            // new  a  url connection
            URL httpUrl = new URL(urls[0]);
            httpUrlCon = (HttpURLConnection) httpUrl.openConnection();
            // set  http  configure
            httpUrlCon.setConnectTimeout(CONNECTION_TIMEOUT);
            httpUrlCon.setReadTimeout(READ_TIMEOUT);
            httpUrlCon.setDoInput(true);
            httpUrlCon.setUseCaches(false);
            httpUrlCon.setInstanceFollowRedirects(true);
            httpUrlCon.setRequestProperty("Charset", "UTF-8");
            if (mHeaders == null) {
                httpUrlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                try {
                    for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
                        httpUrlCon.setRequestProperty(entry.getKey(), entry.getValue());
                        ZplayDebug.i_m(TAG, "for each setRequestProperty : " + "Key = " + entry.getKey() + ", Value = " + entry.getValue(), onoff);
                    }
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "for each mHeaders error: ", e, onoff);
                }

            }

            switch (mType) {
                case GET:
                    httpUrlCon.setDoOutput(false);
                    httpUrlCon.setRequestMethod("GET");
                    break;
                case POST:
                    httpUrlCon.setDoOutput(true);
                    httpUrlCon.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(httpUrlCon.getOutputStream());
                    out.write(mParams.getBytes("utf-8"));
                    out.flush();
                    out.close();
                    break;
                default:
                    break;

            }
            httpUrlCon.connect();
            //check the result of connection
            ZplayDebug.i_m(TAG, "httpUrlCon.getResponseCode() : " + httpUrlCon.getResponseCode(), onoff);
            if (httpUrlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(httpUrlCon.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputLine;
                while ((inputLine = buffer.readLine()) != null) {
                    data.append(inputLine).append("\n");
                }
                in.close();
            }

            result.put("status", httpUrlCon.getResponseCode());
            result.put("data", data.toString());
            result.put("msg", "");
//            ZplayDebug.d(TAG, "result_str: " + result,onoff);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "doInBackground error: ", e, onoff);
        } finally {
            if (httpUrlCon != null) {
                httpUrlCon.disconnect(); // 断开连接
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        super.onPostExecute(result);
        mIHttpCallback.onResponse(result);
    }

    static class MytmArray implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }
}
