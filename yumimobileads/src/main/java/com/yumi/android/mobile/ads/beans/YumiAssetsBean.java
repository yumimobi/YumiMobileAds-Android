package com.yumi.android.mobile.ads.beans;

public class YumiAssetsBean {

    private int id;
    private int required;

    private YumiTitleBean title;
    private YumiImgBean img;
    private YumiDataBean data;
    private YumiLinkBean link;

    public int getId() {
        return id;
    }

    public int getRequired() {
        return required;
    }

    public YumiTitleBean getTitle() {
        return title;
    }

    public YumiImgBean getImg() {
        return img;
    }

    public YumiDataBean getData() {
        return data;
    }

    public YumiLinkBean getLink() {
        return link;
    }



    public class YumiTitleBean {
        private String text;

        public String getText() {
            return text;
        }
    }

    public class YumiImgBean {
        private String url;
        private int w;
        private int h;

        public String getUrl() {
            return url;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }


    }

    public class YumiDataBean {
        private String label;
        private String value;

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

    }

}


