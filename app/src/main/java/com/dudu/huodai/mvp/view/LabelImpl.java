package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public interface LabelImpl extends IView{
    void refreshHome(List<BaseMulDataModel> list, int total_pages);

    void addPage(List<BaseMulDataModel> list);
}
