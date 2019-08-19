package com.linglong.model.bean;

import java.util.List;

public class NewHomeBannerBean {


    private List<BannersBean> banners;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * id : 3
         * title : 快速下款
         * icon : /group1/default/20190630/22/32/8/微信图片_20190417112246.png
         * url : http://tang.rontloan.cn/yuan?inviteCode=xiaxia1123
         * open : true
         * createTime : 1561905152
         */

        private int id;
        private String title;
        private String icon;
        private String url;
        private boolean open;
        private int createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }

}
