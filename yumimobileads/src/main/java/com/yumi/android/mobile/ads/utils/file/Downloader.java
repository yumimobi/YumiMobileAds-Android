package com.yumi.android.mobile.ads.utils.file;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.yumi.android.mobile.ads.utils.ZplayDebug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Downloader {

    private static final boolean onoff = true;
    private static final String TAG = "Downloader";
    private DownloadListener listener;
    private Handler handler;

    public Downloader(DownloadListener listener) {
        this.listener = listener;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                int flag = data.getInt("flag");
                boolean success = data.getBoolean("success");
                String path = data.getString("path");
                if (Downloader.this.listener != null) {
                    Downloader.this.listener.onDownloadComplate(success, flag, path);
                }
            }
        };
    }

    /**
     * @param url
     * @param path
     * @return
     */
    public int startDownload(final String url, final String path) {
        Random dom = new Random();
        final int flag = dom.nextInt(0x30001);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success = downloadFile(url, path);
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putInt("flag", flag);
                    data.putBoolean("success", success);
                    data.putString("path", path);
                    message.setData(data);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "startDownload error : ", e, onoff);
                }
            }
        }).start();
        return flag;
    }

    private boolean downloadFile(String url, String path) {
        FileOutputStream fos = null;
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            boolean isSsl = false;
            if ("https".equals(scheme)) {
                isSsl = true;
            }
            URL u = new URL(url);
            if (isSsl) {
                TrustManager myX509TrustManager = new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                };
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                kmf.init(trustStore, "password".toCharArray());
                HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
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
                conn.setDoInput(true);
                conn.setConnectTimeout(1000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                fos = new FileOutputStream(path);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                return true;
            } else {

                File parentFile = new File(path).getParentFile();
                if (!parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                    if (!mkdirs) {
                        return false;
                    }
                }
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setDoInput(true);
                conn.setConnectTimeout(1000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                fos = new FileOutputStream(path);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                return true;
            }

        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "downloadFile error : ", e, onoff);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    ZplayDebug.e_m(TAG, e.getMessage(), e, onoff);
                }
            }
        }
        return false;
    }

    public interface DownloadListener {
        public void onDownloadComplate(boolean success, int flag, String path);
    }

}
