package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeMenuBean;

import java.util.List;

public class HomeFRMenuHolder extends BaseMulDataModel {
    private List<NewHomeMenuBean.LoanCategoriesBean> loanCategoriesBean;

    public List<NewHomeMenuBean.LoanCategoriesBean> getLoanCategoriesBean() {
        return loanCategoriesBean;
    }

    public void setLoanCategoriesBean(List<NewHomeMenuBean.LoanCategoriesBean> loanCategoriesBean) {
        this.loanCategoriesBean = loanCategoriesBean;
    }

    private NewHomeMenuBean newHomeMenuBean;

    public NewHomeMenuBean getNewHomeMenuBean() {
        return newHomeMenuBean;
    }

    public void setNewHomeMenuBean(NewHomeMenuBean newHomeMenuBean) {
        this.newHomeMenuBean = newHomeMenuBean;
    }
}
