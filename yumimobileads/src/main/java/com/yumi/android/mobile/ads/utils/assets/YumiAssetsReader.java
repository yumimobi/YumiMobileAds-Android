package com.yumi.android.mobile.ads.utils.assets;

import android.content.Context;

import com.yumi.android.mobile.ads.utils.ZplayDebug;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class YumiAssetsReader {

    private static final String TAG = "YumiAssetsReader";
    private static final boolean onoff = true;

    public final static String getFromAssets(Context context, String fileName) {
        BufferedReader bufReader = null;
        String line = "";
        StringBuffer result = new StringBuffer();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getApplicationContext().getResources().getAssets().open(fileName));
            bufReader = new BufferedReader(inputReader);
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            ZplayDebug.e_m(TAG, "", e, onoff);
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
            } catch (Exception e) {
            }
        }
        return result.toString();
    }

}
