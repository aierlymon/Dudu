package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public interface HomeFrgViewImpl extends IView {
    void refreshHome(List<BaseMulDataModel> list);

    void addPage(List<BaseMulDataModel> list);
}
