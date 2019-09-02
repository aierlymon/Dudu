package com.dudu.huodai;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameCirclePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;
import com.dudu.huodai.widget.CirclePanView;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameProgressBar;
import com.dudu.huodai.widget.GameWinDialog;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GameCircleActivity extends BaseTitleActivity<GameCirclmpl, GameCirclePresenter> implements GameCirclmpl {

    @BindView(R.id.game_circle_pan)
    CirclePanView circlePanView;
    private boolean isRunning;


    @BindView(R.id.game_progress)
    GameProgressBar gameProgressBar;

    @BindView(R.id.game_auto_check)
    CheckBox auToCheck;

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
                isRunning = false;
                GameWinDialog.Builder(GameCircleActivity.this)
                        .setMessage("恭喜您，奖励您")
                        .setTitle("+80")
                        .setIconId(R.mipmap.win)
                        .setLeftButtonText("继续摇")
                        .setRightButtonText("金豆翻倍")
                        .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(auToCheck.isChecked()){
                                    Random random = new Random();
                                    circlePanView.rotate(random.nextInt(6));
                                }
                            }
                        })
                        .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                            @Override
                            public void onClick(View view) {
                                startToAdvertisement(1);
                            }
                        })
                        .build().shown();
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
    public void onClick(View v) {
        if (!isRunning) {
            Random random = new Random();
            circlePanView.rotate(random.nextInt(6));
            gameProgressBar.setCurrentPorgress(gameProgressBar.getCurrentPorgress() + 15);
            //  pieView.rotate(1);
        }
        isRunning = true;
    }

    @OnCheckedChanged({R.id.game_auto_check})
    public void onCheckChanger(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            //自动
            if (!isRunning) {
                //如果没有转，那么就开始转
                Random random = new Random();
                circlePanView.rotate(random.nextInt(6));
            }
        }
    }

    public void startToAdvertisement(int requestCode) {
        Intent intent = new Intent(this, AdvertisementActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.i("onActivityResult我反悔了: "+requestCode+"  ;   "+resultCode);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1) {
            //弹窗
            GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜你,奖励您")
                    .setTitle("+160")
                    .setIconId(R.mipmap.win)
                    .setRightButtonText("继续摇")
                    .setOnConfirmClickListener(view -> {
                        if(auToCheck.isChecked()){
                            Random random = new Random();
                            circlePanView.rotate(random.nextInt(6));
                        }
                    })
                    .build().shown();
        }

    }
}
