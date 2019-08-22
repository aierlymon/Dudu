package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.StoryImpl;

public class StoryPresenter extends BasePresenter<StoryImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
