package com.linglong.huodai.mvp.model;

import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.model.bean.NewHomeBannerBean;

public class HomeFRBannerHolder extends BaseMulDataModel {


    private NewHomeBannerBean newHomeBannerBean;

    public NewHomeBannerBean getNewHomeBannerBean() {
        return newHomeBannerBean;
    }

    public void setNewHomeBannerBean(NewHomeBannerBean newHomeBannerBean) {
        this.newHomeBannerBean = newHomeBannerBean;
    }
}
