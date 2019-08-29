package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.baselib.mvp.IView;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.view.GameCaiImpl;

public class GameCaiPresenter extends BasePresenter<GameCaiImpl>  {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
