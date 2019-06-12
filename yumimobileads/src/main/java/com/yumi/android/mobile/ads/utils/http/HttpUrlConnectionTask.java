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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
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
    private final int CONNECTION_TIMEOUT = 1000; //建立连接超时时间/毫秒
    private final int READ_TIMEOUT = 4000; //数据传输超时时间/毫秒
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

    ;

    /**
     * 信任所有主机-对于任何证书都不做检查
     */
    private static void trustAllHosts() {
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL", "AndroidOpenSSL");
            sc.init(null, xtmArray, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            result.put("msg", "没有网络连接");
            ZplayDebug.v_m(TAG, "没有网络连接", onoff);
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
            httpUrlCon.setConnectTimeout(CONNECTION_TIMEOUT);// 建立连接超时时间
            httpUrlCon.setReadTimeout(READ_TIMEOUT);//数据传输超时时间，很重要，必须设置。
            httpUrlCon.setDoInput(true); // 向连接中写入数据
            httpUrlCon.setUseCaches(false); // 禁止缓存
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
                    httpUrlCon.setDoOutput(false); // 从连接中读取数据
                    httpUrlCon.setRequestMethod("GET");// 设置请求类型为
                    break;
                case POST:
                    httpUrlCon.setDoOutput(true); // 从连接中读取数据
                    httpUrlCon.setRequestMethod("POST");// 设置请求类型为
                    DataOutputStream out = new DataOutputStream(httpUrlCon.getOutputStream()); // 获取输出流
                    out.write(mParams.getBytes("utf-8"));// 将要传递的数据写入数据输出流,不要使用out.writeBytes(param); 否则中文时会出错
                    out.flush(); // 输出缓存
                    out.close(); // 关闭数据输出流
                    break;
                default:
                    break;

            }
            httpUrlCon.connect();
            //check the result of connection
            ZplayDebug.i_m(TAG, "httpUrlCon.getResponseCode() : " + httpUrlCon.getResponseCode(), onoff);
            if (httpUrlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(httpUrlCon.getInputStream()); // 获得读取的内容
                BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
                String inputLine;
                while ((inputLine = buffer.readLine()) != null) {
                    data.append(inputLine).append("\n");
                }
                in.close(); // 关闭字符输入流
            }

            result.put("status", httpUrlCon.getResponseCode());
            result.put("data", data.toString());
            result.put("msg", "");
//            ZplayDebug.d(TAG, "result_str: " + result,onoff);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "doInBackground error: ", e, onoff);
            //如果需要处理超时，可以在这里写
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
