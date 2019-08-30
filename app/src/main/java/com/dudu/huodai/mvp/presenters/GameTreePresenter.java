package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.GameTreeImpl;

public class GameTreePresenter extends BasePresenter<GameTreeImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
