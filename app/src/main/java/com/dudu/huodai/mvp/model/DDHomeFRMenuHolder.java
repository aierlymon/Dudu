package com.dudu.huodai.mvp.model;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeMenuBean;
import com.dudu.model.bean.SubjectTable;

import java.util.List;

public class DDHomeFRMenuHolder extends BaseMulDataModel {
    private List<SubjectTable> loanCategoriesBean;

    public List<SubjectTable> getLoanCategoriesBean() {
        return loanCategoriesBean;
    }

    public void setLoanCategoriesBean(List<SubjectTable> loanCategoriesBean) {
        this.loanCategoriesBean = loanCategoriesBean;
    }

}
