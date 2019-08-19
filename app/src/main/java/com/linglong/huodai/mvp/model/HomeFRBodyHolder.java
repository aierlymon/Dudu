package com.linglong.huodai.mvp.model;

import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.model.bean.NewHomeBodyBean;

import java.util.List;

public class HomeFRBodyHolder extends BaseMulDataModel {
    List<NewHomeBodyBean.LoanProductBean> homeBodyBeanList;

    public List<NewHomeBodyBean.LoanProductBean> getHomeBodyBeanList() {
        return homeBodyBeanList;
    }

    public void setHomeBodyBeanList(List<NewHomeBodyBean.LoanProductBean> homeBodyBeanList) {
        this.homeBodyBeanList = homeBodyBeanList;
    }
}
