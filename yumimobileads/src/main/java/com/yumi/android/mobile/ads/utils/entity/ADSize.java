package com.yumi.android.mobile.ads.utils.entity;

public class ADSize {

    public static final ADSize BANNER_SIZE_320_50 = new ADSize(320, 50);
    public static final ADSize BANNER_SIZE_728_90 = new ADSize(728, 90);

    public static final ADSize INTERSTITIAL_SIZE_250_300 = new ADSize(250, 300);
    public static final ADSize INTERSTITIAL_SIZE_500_600 = new ADSize(500, 600);
    public static final ADSize INTERSTITIAL_SIZE_640_960 = new ADSize(640, 960);
    private int width;
    private int height;

    public ADSize(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
