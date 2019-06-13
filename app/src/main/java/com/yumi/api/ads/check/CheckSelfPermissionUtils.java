package com.yumi.api.ads.check;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * android 6.0 check app permission
 *
 * @author Administrator
 */
public class CheckSelfPermissionUtils {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;

    /**
     * When the targetSdkVersion of your app is 23 or above, you can choose the following method to check permission and prompt for user authorization
     *
     * @param context
     */
    public static void CheckSelfPermission(Activity context) {
        try {

            if (android.os.Build.VERSION.SDK_INT < 23) {
                return;
            }
            List<String> denyPermissions = findDeniedPermissions(context, Manifest.permission.READ_PHONE_STATE);

            if (denyPermissions != null && denyPermissions.size() > 0) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions(context, (String[]) denyPermissions.toArray(new String[denyPermissions.size()]), MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }

        } catch (Exception e) {
        }
    }

    private static List<String> findDeniedPermissions(Activity activity, String... permission) {
        try {
            List<String> denyPermissions = new ArrayList<>();
            for (String value : permission) {
                if (ContextCompat.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            }
            return denyPermissions;
        } catch (Exception e) {
        }
        return null;
    }
}
