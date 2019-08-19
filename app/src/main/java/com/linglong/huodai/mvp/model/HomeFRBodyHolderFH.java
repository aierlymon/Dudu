package com.linglong.huodai.mvp.model;


import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.model.bean.HistoryBean;

import java.util.List;

//这个因为其实和HomeFRBodyHolder一样的效果，但是就是返回数据的数据结构变了，所以要多加一个model
public class HomeFRBodyHolderFH extends BaseMulDataModel {
    List<HistoryBean> homeBodyBeanList;

    public List<HistoryBean> getHomeBodyBeanList() {
        return homeBodyBeanList;
    }

    public void setHomeBodyBeanList(List<HistoryBean> homeBodyBeanList) {
        this.homeBodyBeanList = homeBodyBeanList;
    }
}
