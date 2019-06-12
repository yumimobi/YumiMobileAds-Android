package com.yumi.android.mobile.ads.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yumi.android.mobile.ads.utils.ZplayDebug;

/**
 * Description:
 * <p>
 * Created by lgd on 2019/4/16.
 */
public abstract class YumiRequest<T> extends Request<T> {
    private static final String TAG = "YumiRequest";
    private final Object mLock = new Object();

    private Response.Listener<T> mSuccessListener;
    private Response.ErrorListener mErrorListener;
    private boolean onoff = true;

    public YumiRequest(int method, String url, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        super(method, url, null);
        mSuccessListener = successListener;
        mErrorListener = errorListener;
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mSuccessListener = null;
            mErrorListener = null;
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        ZplayDebug.d_m(TAG, "request error: " + error, onoff);
        Response.ErrorListener errorListener;
        synchronized (mLock) {
            errorListener = mErrorListener;
        }
        if (errorListener != null) {
            errorListener.onErrorResponse(error);
        }
    }

    @Override
    protected void deliverResponse(T response) {
        Response.Listener<T> successListener;
        synchronized (mLock) {
            successListener = mSuccessListener;
        }
        if (successListener != null) {
            successListener.onResponse(response);
        }
    }
}
