package com.dudu.huodai;

import android.app.AlertDialog;
import android.view.View;

import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameCirclePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;
import com.dudu.huodai.widget.CirclePanView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameCircleActivity extends BaseTitleActivity<GameCirclmpl, GameCirclePresenter> implements GameCirclmpl {

    @BindView(R.id.game_circle_pan)
    CirclePanView circlePanView;
    private boolean isRunning;


    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle("幸运大轮盘");

        circlePanView.setListener(new CirclePanView.RotateListener() {
            @Override
            public void value(String s) {
                isRunning=false;
                new AlertDialog.Builder(GameCircleActivity.this)
                        .setTitle("鹿死谁手呢？")
                        .setMessage(s)
                        .setIcon(R.mipmap.lllogo)
                        .setNegativeButton("退出",null)
                        .show();
            }
        });
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_gamecircle;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameCirclePresenter createPresenter() {
        return new GameCirclePresenter();
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

    @OnClick({R.id.game_circle_clock})
    public void onClick(View v){
        if(!isRunning){
            Random random = new Random();
            circlePanView.rotate(random.nextInt(6));
            //  pieView.rotate(1);
        }
        isRunning=true;
    }
}
