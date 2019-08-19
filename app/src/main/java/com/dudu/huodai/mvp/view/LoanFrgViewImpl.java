package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeMenuBean;

import java.util.List;

public interface LoanFrgViewImpl extends IView {
    void refreshHome(List<BaseMulDataModel> list);

    void refreshTypeFliter(List<NewHomeMenuBean.LoanCategoriesBean> loanCategories);

    void addPage(List<BaseMulDataModel> list);
}
