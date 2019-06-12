package com.yumi.android.mobile.ads.utils.report;

import android.content.Context;
import android.net.Uri;

import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.utils.ZplayDebug;
import com.yumi.android.mobile.ads.utils.entity.ReportEntity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Administrator on 2017/6/1.
 */

public class ReportRequest {

    private static final boolean onoff = true;
    private final static String TAG = "ReportRequest";
    private static Executor mExecutor = Executors.newFixedThreadPool(4);

    /**
     * 上报
     *
     * @return
     */
    public static void startEventReport(final Context context, final String[] urls, ReportEntity entity) {
        try {
//            if (urls == null && entity != null) {
//                int adMode = entity.getAdMode();
//                if (ADResponse.AD_MODE_MA == adMode) {
//                    reportMA(context, entity);
//                } else {
//                    reportBI(context, entity);
//                }
//            } else {
            Map<String, String> clickArea = null;
            if (entity != null) {
                clickArea = entity.getClickArea();
            }
            report(urls, clickArea);
//            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "startEventReport error:", e, onoff);
        }
    }


    /***
     * 第三方上报，替换宏变量
     * @param urls
     * @param clickArea
     */
    private static void report(String[] urls, Map<String, String> clickArea) {
        for (String url : urls) {
            try {
                String reportUrl = url;
                if (clickArea != null && !clickArea.isEmpty()) {
                    reportUrl = reportUrl.replace(Constants.macro.CLICK_DOWN_X, clickArea.get("downX"))
                            .replace(Constants.macro.CLICK_DOWN_Y, clickArea.get("downY"))
                            .replace(Constants.macro.CLICK_UP_X, clickArea.get("clickX"))
                            .replace(Constants.macro.CLICK_UP_Y, clickArea.get("clickY"));
                } else {
                    reportUrl = reportUrl.replace(Constants.macro.CLICK_DOWN_X, "-999")
                            .replace(Constants.macro.CLICK_DOWN_Y, "-999").replace(Constants.macro.CLICK_UP_X, "-999")
                            .replace(Constants.macro.CLICK_UP_Y, "-999");
                }
                ZplayDebug.v_m(TAG, "上报:" + reportUrl, onoff);
                requestThird(reportUrl);
            } catch (Exception e) {
                ZplayDebug.e_m(TAG, "report error url:" + url + "  ", e, onoff);
            }
        }
    }

    // ADSERVICE_CLICK_DOWN_X 点击落下X坐标，客户端将改字符串替换为真实值即可
    // ADSERVICE_CLICK_DOWN_Y 点击落下Y坐标，客户端将改字符串替换为真实值即可
    // ADSERVICE_CLICK_UP_X 点击离开X坐标，客户端将改字符串替换为真实值即可
    // ADSERVICE_CLICK_UP_Y 点击离开Y坐标，客户端将改字符串替换为真实值即可

    /**
     * 上报第三方
     *
     * @param thirdurl
     */
    private static void requestThird(final String thirdurl) {
        ZplayDebug.v_m(TAG, "准备上报第三方", onoff);
        mExecutor.execute(new Runnable() {
            //            @SuppressLint("TrulyRandom")
            @Override
            public void run() {
                int flag = 0;
                try {
                    ZplayDebug.i_m(TAG, "flag=" + flag + " 第三方监播地址:" + thirdurl, onoff);
                    URL url = new URL(thirdurl);
                    Uri uri = Uri.parse(thirdurl);
                    String scheme = uri.getScheme();
                    boolean isHttps = "https".equals(scheme);
                    if (isHttps) {

                        TrustManager myX509TrustManager = new X509TrustManager() {

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType)
                                    throws CertificateException {
                            }
                        };
                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                        kmf.init(trustStore, "password".toCharArray());
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        // 设置忽略证书
                        conn.setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
                        // 设置SSLContext
                        SSLContext sslcontext = SSLContext.getInstance("SSL", "AndroidOpenSSL");
                        sslcontext.init(kmf.getKeyManagers(), new TrustManager[]
                                {
                                        myX509TrustManager
                                }, new java.security.SecureRandom());

                        // 设置套接工厂
                        conn.setSSLSocketFactory(sslcontext.getSocketFactory());

                        conn.setInstanceFollowRedirects(true);
                        conn.setConnectTimeout(2000);
                        conn.setDoInput(false);
                        conn.getContentLength();
                        conn.disconnect();
                    } else {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setInstanceFollowRedirects(true);
                        conn.setConnectTimeout(2000);
                        conn.setDoInput(false);
                        conn.getContentLength();
                        conn.disconnect();
                    }

                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "flag=" + flag + "第三方监播异常:", e, onoff);
                }
            }
        });
//        new Thread(run).start();
    }

    public interface Callback {
        public void onCallBack(String result);
    }

}
