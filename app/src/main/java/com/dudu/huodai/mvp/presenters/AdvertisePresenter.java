package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.AdvertiseImpl;

public class AdvertisePresenter extends BasePresenter<AdvertiseImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
