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
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.GameSmashBean;
import com.dudu.huodai.mvp.presenters.GameSmashPresenter;
import com.dudu.huodai.mvp.view.GameSmashImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.GameSmashAdapter;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.GameAdverBackDialog;

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

        list=new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(""+i);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        gameSmashAdapter=new GameSmashAdapter(this,list);
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
    public void SmashEvent(GameSmashBean smashBean){
        Intent intent=new Intent(this,AdvertisementActivity.class);
        startActivityForResult(intent,1);
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
