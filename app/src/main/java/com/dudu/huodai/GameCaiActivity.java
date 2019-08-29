package com.dudu.huodai;

import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameCaiPresenter;
import com.dudu.huodai.mvp.view.GameCaiImpl;

import butterknife.ButterKnife;

public class GameCaiActivity extends BaseTitleActivity<GameCaiImpl, GameCaiPresenter> implements GameCaiImpl {

    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.game_caicai));
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.game_caicaicai;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameCaiPresenter createPresenter() {
        return new GameCaiPresenter();
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
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
