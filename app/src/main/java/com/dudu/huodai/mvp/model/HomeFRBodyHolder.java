package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeBodyBean;

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
