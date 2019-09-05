package com.dudu.huodai;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.model.postbean.GameSmashBean;
import com.dudu.huodai.mvp.presenters.GameSmashPresenter;
import com.dudu.huodai.mvp.view.GameSmashImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.GameSmashAdapter;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameWinDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameSmashEggActivity extends BaseTitleActivity<GameSmashImpl, GameSmashPresenter> implements GameSmashImpl {

    @BindView(R.id.recyclerview_smash)
    RecyclerView mRecyclerView;

    List<String> list;

    @BindView(R.id.myhead)
    View toolbarParent;

    @BindView(R.id.menu_icon_image)
    ImageView menuIconImage;

    @BindView(R.id.img_back)
    ImageView imgBack;


    @BindView(R.id.bottom_parent)
    RelativeLayout bottomParent;

    private AdvertUtil eggDialogBottomAdvert;
    private AdvertUtil eggFanBeiAdvert;
    private AdvertUtil eggfbBackAdvert;


    @BindView(R.id.game_egg_parent)
    RelativeLayout gameEggParent;

    @Override
    protected void initRequest() {

    }

    GameSmashAdapter gameSmashAdapter;

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle("砸金蛋");

        setTitleColor(Color.WHITE);
        toolbarParent.setBackgroundColor(getResources().getColor(R.color.game_smash_back));
        menuIconImage.setBackgroundColor(getResources().getColor(R.color.game_smash_back));
        Glide.with(this).load(R.drawable.jiantou_left_white).into(imgBack);

        list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add("" + i);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        gameSmashAdapter = new GameSmashAdapter(this, list);
        mRecyclerView.setAdapter(gameSmashAdapter);
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_game_smashegg;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameSmashPresenter createPresenter() {
        return new GameSmashPresenter();
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

    public int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SmashEvent(GameSmashBean smashBean) {
        //弹窗
        GameWinDialog gameWinDialog = GameWinDialog.Builder(this)
                .setMessage("恭喜您，奖励您")
                .setTitle("+80")
                .setIconId(R.mipmap.win)
                .setLeftButtonText("继续砸")
                .setRightButtonText("金豆翻倍")
                .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameSmashAdapter.notifyDataSetChanged();
                    }
                })
                .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                    @Override
                    public void onClick(View view) {
                        eggFanBeiAdvert = new AdvertUtil(gameEggParent, GameSmashEggActivity.this);
                        float[] WH = Utils.getScreenWH(GameSmashEggActivity.this.getApplicationContext());
                        eggFanBeiAdvert.setVideoType(ApplicationPrams.GameGoldEgg);
                        eggFanBeiAdvert.setSuccess(true);
                        eggFanBeiAdvert.loadVideo(getmTTAdNative(), ApplicationPrams.public_game_gold_jili, (int) WH[0], (int) WH[1]);
                    }
                })
                .build().shown();
        eggDialogBottomAdvert = new AdvertUtil(gameWinDialog.getAdvertLayout(), GameSmashEggActivity.this);
        eggDialogBottomAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_gold_dialog_bottom,
                (int) ((Utils.getScreenWH(GameSmashEggActivity.this.getApplicationContext())[0]) * 0.9),
                (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.i("onActivityResult我反悔了: " + requestCode + "  ;   " + resultCode);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1) {
            //弹窗
            GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜你,奖励您")
                    .setTitle("+160")
                    .setIconId(R.mipmap.win)
                    .setRightButtonText("继续砸")
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            gameSmashAdapter.notifyDataSetChanged();
                        }
                    })
                    .build().shown();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkAdverdialogBean(AdverdialogBean adverdialogBean) {
        //选中LoanFragment页面
        if (adverdialogBean.getType() == ApplicationPrams.GameGoldEgg) {

            GameAdverBackDialog adverBackDialog = GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜你,奖励您")
                    .setTitle("+160")
                    .setIconId(R.mipmap.win)
                    .hasAdvert(true)
                    .setRightButtonText("继续砸")
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            gameSmashAdapter.notifyDataSetChanged();
                        }
                    }).build();
            adverBackDialog.shown();
            eggfbBackAdvert = new AdvertUtil(adverBackDialog.getAdvertLayout(), GameSmashEggActivity.this);
            eggfbBackAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_gold_back_dialog_bottom, (int) ((Utils.getScreenWH(this.getApplicationContext())[0]) * 0.9),
                    (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));

        }
    }

    AdvertUtil gameFirstAdvert;
    private boolean isFirst;

    @Override
    protected void onResume() {
        super.onResume();
        // caiBottomAdvert  =new AdvertUtil(bottomParent,this);
        MyLog.i("onResume: aaaaaaaaaaaa");
        if (isFirst) {
            gameFirstAdvert = new AdvertUtil(bottomParent, this);
            gameFirstAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_gold_bottom, bottomParent.getWidth(), bottomParent.getHeight());
        }

        isFirst = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameFirstAdvert != null && gameFirstAdvert.getmTTAd() != null) {
            gameFirstAdvert.getmTTAd().destroy();
        }

        if (eggDialogBottomAdvert != null && eggDialogBottomAdvert.getmTTAd() != null) {
            eggDialogBottomAdvert.getmTTAd().destroy();
        }

        if (eggFanBeiAdvert != null && eggFanBeiAdvert.getmTTAd() != null) {
            eggFanBeiAdvert.getmTTAd().destroy();
        }

        if (eggfbBackAdvert != null && eggfbBackAdvert.getmTTAd() != null) {
            eggfbBackAdvert.getmTTAd().destroy();
        }
    }

}
