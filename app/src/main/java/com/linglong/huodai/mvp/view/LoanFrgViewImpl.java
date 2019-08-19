package com.linglong.huodai.mvp.view;

import com.linglong.baselib.mvp.IView;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.model.bean.NewHomeMenuBean;

import java.util.List;

public interface LoanFrgViewImpl extends IView {
    void refreshHome(List<BaseMulDataModel> list);

    void refreshTypeFliter(List<NewHomeMenuBean.LoanCategoriesBean> loanCategories);

    void addPage(List<BaseMulDataModel> list);
}
