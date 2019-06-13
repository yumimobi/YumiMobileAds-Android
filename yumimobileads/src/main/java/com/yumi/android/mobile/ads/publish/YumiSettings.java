package com.yumi.android.mobile.ads.publish;

public class YumiSettings {

    // Indicates the GDPR requirement of the user. If it's 1, the user's subject to the GDPR laws.
    // Default is 0.
    private static int gdpr = 0;
    // Your user's consent string. In this case, the user has given consent to store and process personal information.
    // Default is true.
    private static boolean consent = true;

    // Indicates the coppa requirement of the user. If it's 1, the user's subject to the coppa laws.
    // Default is 1.
    private static int coppa = 1;

    public static int getGdpr() {
        return gdpr;
    }

    /**
     * Indicates the GDPR requirement of the user.
     * @param gdpr If it's 1, the user's subject to the GDPR laws. Default is 0.
     */
    public static void setGdpr(int gdpr) {
        YumiSettings.gdpr = gdpr;
    }

    public static boolean isConsent() {
        return consent;
    }

    /**
     * Your user's consent string.
     * @param consent In this case, the user has given consent to store and process personal information.Default is true.
     */
    public static void setConsent(boolean consent) {
        YumiSettings.consent = consent;
    }

    public static int getCoppa() {
        return coppa;
    }

    /**
     * Indicates the coppa requirement of the user.
     * @param coppa If it's 1, the user's subject to the coppa laws.Default is 1.
     */
    public static void setCoppa(int coppa) {
        YumiSettings.coppa = coppa;
    }


}
