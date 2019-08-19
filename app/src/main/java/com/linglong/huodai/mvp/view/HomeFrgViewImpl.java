package com.linglong.huodai.mvp.view;

import com.linglong.baselib.mvp.IView;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public interface HomeFrgViewImpl extends IView {
    void refreshHome(List<BaseMulDataModel> list);

    void addPage(List<BaseMulDataModel> list);
}
