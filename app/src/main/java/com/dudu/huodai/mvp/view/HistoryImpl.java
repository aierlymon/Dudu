package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public interface HistoryImpl extends IView {
    void refreshHistory(List<BaseMulDataModel> list);

    void addPage(List<BaseMulDataModel> list);
}
