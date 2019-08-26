package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

public class HomeFRBigBackHoder extends BaseMulDataModel {
    private String bigIcon;

    public String getBigIcon() {
        return bigIcon;
    }

    public void setBigIcon(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public HomeFRBigBackHoder(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public HomeFRBigBackHoder() {
    }
}
