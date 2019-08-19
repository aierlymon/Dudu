package com.linglong.huodai.mvp.model;

import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;

public class MyFRFunctionHolder extends BaseMulDataModel {
    private String f_name;
    private int f_icon;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public int getF_icon() {
        return f_icon;
    }

    public void setF_icon(int f_icon) {
        this.f_icon = f_icon;
    }
}
