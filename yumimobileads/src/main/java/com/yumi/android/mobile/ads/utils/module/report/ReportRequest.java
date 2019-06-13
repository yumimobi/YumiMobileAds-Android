package com.yumi.android.mobile.ads.utils.module.report;

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
     * report
     *
     * @return
     */
    public static void startEventReport(final Context context, final String[] urls, ReportEntity entity) {
        try {
            Map<String, String> clickArea = null;
            if (entity != null) {
                clickArea = entity.getClickArea();
            }
            report(urls, clickArea);
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "startEventReport error:", e, onoff);
        }
    }


    /***
     * change macro
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
                ZplayDebug.v_m(TAG, "report:" + reportUrl, onoff);
                requestThird(reportUrl);
            } catch (Exception e) {
                ZplayDebug.e_m(TAG, "report error url:" + url + "  ", e, onoff);
            }
        }
    }

    /**
     * report third
     *
     * @param thirdurl
     */
    private static void requestThird(final String thirdurl) {
        ZplayDebug.v_m(TAG, "start report third", onoff);
        mExecutor.execute(new Runnable() {
            //            @SuppressLint("TrulyRandom")
            @Override
            public void run() {
                int flag = 0;
                try {
                    ZplayDebug.i_m(TAG, "flag=" + flag + " thirdurl:" + thirdurl, onoff);
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
                        conn.setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
                        SSLContext sslcontext = SSLContext.getInstance("SSL", "AndroidOpenSSL");
                        sslcontext.init(kmf.getKeyManagers(), new TrustManager[]
                                {
                                        myX509TrustManager
                                }, new java.security.SecureRandom());

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
    }

    public interface Callback {
        public void onCallBack(String result);
    }

}
