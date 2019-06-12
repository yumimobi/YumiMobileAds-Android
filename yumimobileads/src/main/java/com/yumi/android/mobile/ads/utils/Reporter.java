package com.yumi.android.mobile.ads.utils;

import android.content.Context;

import com.yumi.android.mobile.ads.constants.Constants;
import com.yumi.android.mobile.ads.utils.entity.ReportEntity;
import com.yumi.android.mobile.ads.utils.report.ReportRequest;

import java.util.List;


/**
 * 自主上报处理类
 */
public class Reporter {

    public static final String TAG = "Reporter";


    public static void reportEvent(Context context, List<String> urls, ReportEntity entity) {
        if (urls != null) {
            int urlLength = urls.size();
            String[] newUrls = new String[urlLength];
            for (int i = 0; i < urlLength; i++) {
                String url = urls.get(i);
                try {
                    url = url.replaceFirst(Constants.macro.UNIX_ORIGIN_TIME, System.currentTimeMillis() + "");
                    url = url.replaceFirst(Constants.macro.YUMI_SELF_CURRENT_TIME, entity.getVideoPosition() + "");
                    url = url.replaceFirst(Constants.macro.YUMI_SELF_START_TIME, entity.getVideoPosition() + "");
                } catch (Exception e) {
                    ZplayDebug.e_m(TAG, "reportEvent error url:" + url + "  ", e, true);
                }
                newUrls[i] = url;
            }
            ReportRequest.startEventReport(context, newUrls, entity);
        }
    }
}
