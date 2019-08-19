package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public interface FliterImpl extends IView {
    void refreshFilter(List<BaseMulDataModel> list);

    void addPage(List<BaseMulDataModel> list);
}
