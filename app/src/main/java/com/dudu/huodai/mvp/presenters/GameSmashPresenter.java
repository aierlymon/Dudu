package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.GameSmashImpl;

public class GameSmashPresenter extends BasePresenter<GameSmashImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
