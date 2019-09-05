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
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.presenters.GameCirclePresenter;
import com.dudu.huodai.mvp.view.GameCirclmpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.CirclePanView;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameProgressBar;
import com.dudu.huodai.widget.GameWinDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.game_circle_parent)
    RelativeLayout gameCircleParent;

    @BindView(R.id.bottom_parent)
    RelativeLayout bottomParent;
    private AdvertUtil circleDialogBottomAdvert;
    private AdvertUtil circleFanBeiAdvert;
    private AdvertUtil circlefbBackAdvert;

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
                                if (auToCheck.isChecked()) {
                                    Random random = new Random();
                                    circlePanView.rotate(random.nextInt(6));
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
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (auToCheck.isChecked()) {
                                Random random = new Random();
                                circlePanView.rotate(random.nextInt(6));
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
        MyLog.i("onResume: aaaaaaaaaaaa");
        if (isFirst) {
            circleBottomAdvert = new AdvertUtil(bottomParent, this);
            circleBottomAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_circle_bottom, bottomParent.getWidth(), bottomParent.getHeight());
        }

        isFirst = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
