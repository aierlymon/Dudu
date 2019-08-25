package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeBodyBean;
import com.dudu.model.bean.StoryTable;
import com.dudu.model.bean.SubjectInfo;

import java.util.List;

public class DDHomeFRBodyHolder  extends BaseMulDataModel {
    List<StoryTable> homeBodyBeanList;

    public List<StoryTable> getHomeBodyBeanList() {
        return homeBodyBeanList;
    }

    public void setHomeBodyBeanList(List<StoryTable> homeBodyBeanList) {
        this.homeBodyBeanList = homeBodyBeanList;
    }

    List<SubjectInfo.DataBean> dataBeanList;


    public List<SubjectInfo.DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<SubjectInfo.DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }
}
