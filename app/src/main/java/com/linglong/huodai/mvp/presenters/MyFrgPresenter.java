package com.linglong.huodai.mvp.presenters;

import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.mvp.view.MyViewImpl;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MyFrgPresenter extends BasePresenter<MyViewImpl> {
    @Override
    protected boolean isUseEventBus() {
        return true;
    }


    @Override
    public void showError(String msg) {
        getView().showError("连接错误: " + msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginstate(Boolean isState){
        MyLog.i("收到了已经登陆");
        if(isState){
            getView().loginSuceesee();
        }
    }


}
