package com.linglong.model.bean;

import java.util.List;

public class NewHomeBodyBean {

    private List<LoanProductBean> loanProduct;

    public List<LoanProductBean> getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(List<LoanProductBean> loanProduct) {
        this.loanProduct = loanProduct;
    }

    public static class LoanProductBean {
        /**
         * id : 2
         * name : 花卡小贷
         * icon : /group1/default/20190630/20/27/8/WX20190513-153520@2x.png
         * categoryId : 1
         * profile : 花卡小贷的就是好
         * tags : null
         * limitL : 5000
         * limitH : 10000
         * period : 期限 3个月-12个月
         * interest : 日息 0.05%
         * applyNum : 3500
         * applyCond : 身份证就可以申请
         * applyProc :
         * specDesc :
         * sucProb : 50%
         * speed : 最快当天下款
         * todayUse :
         * sortNum : 1
         * top : false
         * online : 1
         * url : https://api.lianlianhua.wang/h5/register.html?id=d7221f90-5ece-474a-b17f-51e10d240c64
         * allowClient : 3
         * createTime : 1561904793
         */

        private int id;
        private String name;
        private String icon;
        private int categoryId;
        private String profile;
        private Object tags;
        private int limitL;
        private int limitH;
        private String period;
        private String interest;
        private int applyNum;
        private String applyCond;
        private String applyProc;
        private String specDesc;
        private String sucProb;
        private String speed;
        private String todayUse;
        private int sortNum;
        private boolean top;
        private int online;
        private String url;
        private int allowClient;
        private int createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public Object getTags() {
            return tags;
        }

        public void setTags(Object tags) {
            this.tags = tags;
        }

        public int getLimitL() {
            return limitL;
        }

        public void setLimitL(int limitL) {
            this.limitL = limitL;
        }

        public int getLimitH() {
            return limitH;
        }

        public void setLimitH(int limitH) {
            this.limitH = limitH;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public int getApplyNum() {
            return applyNum;
        }

        public void setApplyNum(int applyNum) {
            this.applyNum = applyNum;
        }

        public String getApplyCond() {
            return applyCond;
        }

        public void setApplyCond(String applyCond) {
            this.applyCond = applyCond;
        }

        public String getApplyProc() {
            return applyProc;
        }

        public void setApplyProc(String applyProc) {
            this.applyProc = applyProc;
        }

        public String getSpecDesc() {
            return specDesc;
        }

        public void setSpecDesc(String specDesc) {
            this.specDesc = specDesc;
        }

        public String getSucProb() {
            return sucProb;
        }

        public void setSucProb(String sucProb) {
            this.sucProb = sucProb;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getTodayUse() {
            return todayUse;
        }

        public void setTodayUse(String todayUse) {
            this.todayUse = todayUse;
        }

        public int getSortNum() {
            return sortNum;
        }

        public void setSortNum(int sortNum) {
            this.sortNum = sortNum;
        }

        public boolean isTop() {
            return top;
        }

        public void setTop(boolean top) {
            this.top = top;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getAllowClient() {
            return allowClient;
        }

        public void setAllowClient(int allowClient) {
            this.allowClient = allowClient;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }
}
