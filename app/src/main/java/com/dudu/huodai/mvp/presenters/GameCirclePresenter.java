package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;

public class GameCirclePresenter extends BasePresenter<GameCirclmpl>{

    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
