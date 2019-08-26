package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.model.bean.AllStoryBean;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public interface AllStoryImpl extends IView {
    void refreshHome(AllStoryBean httpResult);
}
