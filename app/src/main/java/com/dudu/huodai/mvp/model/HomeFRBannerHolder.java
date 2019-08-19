package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeBannerBean;

public class HomeFRBannerHolder extends BaseMulDataModel {


    private NewHomeBannerBean newHomeBannerBean;

    public NewHomeBannerBean getNewHomeBannerBean() {
        return newHomeBannerBean;
    }

    public void setNewHomeBannerBean(NewHomeBannerBean newHomeBannerBean) {
        this.newHomeBannerBean = newHomeBannerBean;
    }
}
