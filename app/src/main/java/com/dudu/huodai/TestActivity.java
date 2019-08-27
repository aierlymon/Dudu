package com.dudu.huodai;

import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.HomeFrgPresenter;
import com.dudu.huodai.mvp.view.HomeFrgViewImpl;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

import java.util.List;

public class TestActivity extends BaseTitleActivity<HomeFrgViewImpl, HomeFrgPresenter> implements HomeFrgViewImpl {
    @Override
    protected void initRequest() {
        //网络请求
    }

    @Override
    public void initView() {
        //初始化view
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }


    @Override
    protected HomeFrgPresenter createPresenter() {
        return new HomeFrgPresenter();
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {

    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }
}
