package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public interface SubjectImpl extends IView {
    void refreshHome(List<BaseMulDataModel> list);
}
