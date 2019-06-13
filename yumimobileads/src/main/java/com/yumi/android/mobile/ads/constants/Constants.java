package com.yumi.android.mobile.ads.constants;


public class Constants {


    public static final class actiontype {
        /**
         * open internal explorer
         */
        public final static int ACTION_TYPE_EXPLORER = 1;
        /**
         * open system explorer
         */
        public final static int ACTION_TYPE_SYSTEM_EXPLORER = 2;
        /**
         * download
         */
        public final static int ACTION_TYPE_DOWNLOAD = 6;

        /**
         * deeplink
         */
        public final static int ACTION_TYPE_DEEPLINK = 7;

        /**
         * open ass store
         */
        public static final int ACTION_TYPE_OPEN_APP_STORE = 8;
    }

    public static final class inventorytype {
        /**
         * image
         */
        public final static int INVENTORY_TYPE_IMAGE = 1;
        /**
         * image and text
         */
        public final static int INVENTORY_TYPE_IMAGE_TEXT = 2;
        /**
         * html5
         */
        public final static int INVENTORY_TYPE_HTML = 4;

        /**
         * text
         */
        public final static int INVENTORY_TYPE_TEXT = 5;

    }

    /**
     * dir
     *
     * @author menghui
     */
    public static final class dir {
        private final static String HOST_DIR = "/.zplaymobile/zplayAdxAD/";
        public final static String DIR_APK = HOST_DIR + "apk/";
    }

    public static final class macro {
        public static final String CLICK_DOWN_X = "YUMI_ADSERVICE_CLICK_DOWN_X";
        public static final String CLICK_DOWN_Y = "YUMI_ADSERVICE_CLICK_DOWN_Y";
        public static final String CLICK_UP_X = "YUMI_ADSERVICE_CLICK_UP_X";
        public static final String CLICK_UP_Y = "YUMI_ADSERVICE_CLICK_UP_Y";

        public static final String UNIX_ORIGIN_TIME = "YUMI_ADSERVICE_UNIX_ORIGIN_TIME";

        public static final String YUMI_SELF_CURRENT_TIME = "YUMI_ADSERVICE_CUR_TIME";
        public static final String YUMI_SELF_START_TIME = "YUMI_ADSERVICE_START_TIME";

    }
}
