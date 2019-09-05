package com.dudu.huodai;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.model.postbean.GameCircleBoxBean;
import com.dudu.huodai.mvp.presenters.GameCirclePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.CirclePanView;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameProgressBar;
import com.dudu.huodai.widget.GameWinDialog;
import com.dudu.huodai.widget.TimeRewardDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

    @BindView(R.id.game_circle_parent)
    RelativeLayout gameCircleParent;

    @BindView(R.id.bottom_parent)
    RelativeLayout bottomParent;
    private AdvertUtil circleDialogBottomAdvert;
    private AdvertUtil circleFanBeiAdvert;
    private AdvertUtil circlefbBackAdvert;

    private int count = 0;

    @BindView(R.id.game_circle_clock)
    ImageView btnClock;

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
                if (count == 4 || count == 8) {
                    showPrefect();
                } else {
                    win();
                }
                isRunning = false;
                btnClock.setClickable(true);
            }
        });
    }

    private Timer timr;
    private int closeTime;//关闭按钮显示的事件

    private void showPrefect() {


        TimeRewardDialog gameWinDialog = TimeRewardDialog.Builder(GameCircleActivity.this)
                .setMessage("恭喜您，奖励您")
                /* .setOnConfirmClickListener(new TimeRewardDialog.onConfirmClickListener() {
                     @Override
                     public void onClick(View view) {
                      *//*   circleFanBeiAdvert = new AdvertUtil(gameCircleParent, GameCircleActivity.this);
                        float[] WH = Utils.getScreenWH(GameCircleActivity.this.getApplicationContext());
                        circleFanBeiAdvert.setVideoType(ApplicationPrams.GameCiccle);
                        circleFanBeiAdvert.setSuccess(true);
                        circleFanBeiAdvert.loadVideo(getmTTAdNative(), ApplicationPrams.public_game_circle_jili, (int) WH[0], (int) WH[1]);*//*
                    }
                })*/
                .build().shown();

        timr = new Timer();
        timr.schedule(new TimerTask() {
            @Override
            public void run() {
                if (closeTime == 3) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeTime = 0;
                            gameWinDialog.getClose().setVisibility(View.VISIBLE);
                        }
                    });
                    timr.cancel();
                    timr = null;
                }
                closeTime += 1;
            }
        }, 0, 1000);

        if (circleDialogBottomAdvert != null && circleDialogBottomAdvert.getmTTAd() != null) {
            circleDialogBottomAdvert.getmTTAd().destroy();
        }
        circleDialogBottomAdvert = new AdvertUtil(gameWinDialog.getTimerRewardParent(), GameCircleActivity.this);
        circleDialogBottomAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_circle_dialog_bottom,
                (int) ((Utils.getScreenWH(GameCircleActivity.this.getApplicationContext())[0]) * 0.9),
                (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
    }


    private void win() {
        GameWinDialog gameWinDialog = GameWinDialog.Builder(GameCircleActivity.this)
                .setMessage("恭喜您，奖励您")
                .setTitle("+80")
                .setIconId(R.mipmap.win)
                .setLeftButtonText("继续摇")
                .setRightButtonText("金豆翻倍")
                .hasAdvert(true)
                .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                    @Override
                    public void onClick(View view) {

                        MyLog.i("我这个时候的次数: " + count);
                        if (count == 5 || count == 9) {
                            //弹出全屏广告,并且不能触发轮盘转
                            full();
                            return;
                        }

                        if (auToCheck.isChecked()) {
                            route();
                        }


                    }
                })
                .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                    @Override
                    public void onClick(View view) {
                        circleFanBeiAdvert = new AdvertUtil(gameCircleParent, GameCircleActivity.this);
                        float[] WH = Utils.getScreenWH(GameCircleActivity.this.getApplicationContext());
                        circleFanBeiAdvert.setVideoType(ApplicationPrams.GameCiccle);
                        circleFanBeiAdvert.setSuccess(true);
                        circleFanBeiAdvert.loadVideo(getmTTAdNative(), ApplicationPrams.public_game_circle_jili, (int) WH[0], (int) WH[1]);
                    }
                })
                .build().shown();

        circleDialogBottomAdvert = new AdvertUtil(gameWinDialog.getAdvertLayout(), GameCircleActivity.this);
        circleDialogBottomAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_circle_dialog_bottom,
                (int) ((Utils.getScreenWH(GameCircleActivity.this.getApplicationContext())[0]) * 0.9),
                (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
    }

    private void full() {
        MyLog.i("进来了全屏加载");
        AdvertUtil advertUtil = new AdvertUtil(gameCircleParent, GameCircleActivity.this);
        advertUtil.setVideoType(ApplicationPrams.GameCiccle);
        float[] WH = Utils.getScreenWH(GameCircleActivity.this.getApplicationContext());
        advertUtil.loadFullVideo(getmTTAdNative(), ApplicationPrams.public_game_circle_full_video, (int) WH[0], (int) WH[1], TTAdConstant.VERTICAL);
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
        Intent intent = new Intent(this, AdvertSplashActivity.class);
        if (isScreenOn) {
            intent.putExtra(ApplicationPrams.adverId, ApplicationPrams.public_sceenon_advertId);
        } else {
            intent.putExtra(ApplicationPrams.adverId, ApplicationPrams.public_restart_advertId);
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

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @OnClick({R.id.game_circle_clock})
    public void onClick(View v) {
        if (!isRunning) {
            MyLog.i("转了多少次: " + count);
            route();
        }

    }

    public void route() {
        isRunning = true;
        btnClock.setClickable(false);
        count++;
        gameProgressBar.setCurrentPorgress(gameProgressBar.getCurrentPorgress() + 15);
        Random random = new Random();
        if (count == 4 || count == 8) {
            //摇到宝箱
            int i = (random.nextInt(3)) * 2;
            circlePanView.rotate(i);
        } else {
            //摇到金币
            int i = (2 * random.nextInt(2)) + 1;
            MyLog.i("默认转到的位置是: " + i);

            circlePanView.rotate(i);
        }
    }

 /*   @OnCheckedChanged({R.id.game_auto_check})
    public void onCheckChanger(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            //自动
            if (!isRunning) {
                //如果没有转，那么就开始转
                Random random = new Random();
                circlePanView.rotate(random.nextInt(5));
            }
        }
    }*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkAdverdialogBean(AdverdialogBean adverdialogBean) {
        //选中LoanFragment页面
        if (adverdialogBean.getType() == ApplicationPrams.GameCiccle) {

            GameAdverBackDialog adverBackDialog = GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜你,奖励您")
                    .setTitle("+160")
                    .setIconId(R.mipmap.win)
                    .hasAdvert(true)
                    .setRightButtonText("继续摇")
                    .setOnCancelClickListener(new GameAdverBackDialog.onCancelClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (auToCheck.isChecked()) {
                                route();
                            }
                        }
                    })
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (auToCheck.isChecked()) {
                                route();
                            }
                        }
                    }).build();
            adverBackDialog.shown();
            circlefbBackAdvert = new AdvertUtil(adverBackDialog.getAdvertLayout(), GameCircleActivity.this);
            circlefbBackAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_cai_back_dialog_bottom, (int) ((Utils.getScreenWH(this.getApplicationContext())[0]) * 0.9),
                    (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));

        }
    }

    AdvertUtil circleBottomAdvert;
    private boolean isFirst;

    @Override
    protected void onResume() {
        super.onResume();
        // caiBottomAdvert  =new AdvertUtil(bottomParent,this);
        if (isFirst) {
            circleBottomAdvert = new AdvertUtil(bottomParent, this);
            circleBottomAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_circle_bottom, bottomParent.getWidth(), bottomParent.getHeight());
        }

        isFirst = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timr != null) {
            timr.cancel();
            timr = null;
        }

        if (circleBottomAdvert != null && circleBottomAdvert.getmTTAd() != null) {
            circleBottomAdvert.getmTTAd().destroy();
        }

        if (circleDialogBottomAdvert != null && circleDialogBottomAdvert.getmTTAd() != null) {
            circleDialogBottomAdvert.getmTTAd().destroy();
        }

        if (circleFanBeiAdvert != null && circleFanBeiAdvert.getmTTAd() != null) {
            circleFanBeiAdvert.getmTTAd().destroy();
        }

        if (circlefbBackAdvert != null && circlefbBackAdvert.getmTTAd() != null) {
            circlefbBackAdvert.getmTTAd().destroy();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkGameCircleBoxBean(GameCircleBoxBean gameCircleBoxBean) {
        //选中LoanFragment页面
        MyLog.i("isRunning: "+isRunning);
        if(!isRunning){
            gameProgressBar.getHasDomap().put(gameCircleBoxBean.getType(),true);
            win();

        }
    }

}
