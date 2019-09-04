package com.dudu.huodai;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameCirclePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;
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

    @BindView(R.id.myhead)
    View toolbarParent;

    @BindView(R.id.menu_icon_image)
    ImageView menuIconImage;

    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.bottom_parent)
    RelativeLayout bottomParent;

    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle("幸运大轮盘");
        setTitleColor(Color.WHITE);
        toolbarParent.setBackgroundColor(getResources().getColor(R.color.game_circle_back));
        menuIconImage.setBackgroundColor(getResources().getColor(R.color.game_circle_back));
        Glide.with(this).load(R.drawable.jiantou_left_white).into(imgBack);

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
    protected void startToAdvert(boolean isScreenOn) {
        Intent intent=new Intent(this,AdvertSplashActivity.class);
        if(isScreenOn){
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_sceenon_advertId);
        }else{
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_restart_advertId);
        }
        startActivity(intent);
    }

    @Override
    protected void screenOn() {

    }

    @Override
    protected void screenOff() {

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

    AdvertUtil advertUtil;
    private boolean isFirst;
    @Override
    protected void onResume() {
        super.onResume();
        // advertUtil  =new AdvertUtil(bottomParent,this);
        MyLog.i("onResume: aaaaaaaaaaaa");
        if(isFirst){
            advertUtil=new AdvertUtil(bottomParent,this);
            advertUtil.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_cai_bottom, bottomParent.getWidth(), bottomParent.getHeight());
        }

        isFirst=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (advertUtil != null && advertUtil.getmTTAd() != null) {
            advertUtil.getmTTAd().destroy();
        }
    }
}
