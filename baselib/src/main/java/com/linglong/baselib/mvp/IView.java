package com.linglong.baselib.mvp;

/*
* author hh
* createby 2019-6-25
* */
public interface IView {
    /**
     * 显示加载
     */
     void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();


    /**
     * 显示错误信息
     */
    void showError(String msg);

}
