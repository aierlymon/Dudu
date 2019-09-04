package com.dudu.huodai;

import android.content.Intent;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.dudu.baselib.myapplication.App;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.AdvertisePresenter;
import com.dudu.huodai.mvp.view.AdvertiseImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertisementActivity extends BaseTitleActivity<AdvertiseImpl, AdvertisePresenter> implements AdvertiseImpl {

    @BindView(R.id.jili_video)
    FrameLayout frameLayout;


    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        AdvertUtil advertUtil = new AdvertUtil(frameLayout, this);
        float[] WH = Utils.getScreenWH(this.getApplicationContext());
        advertUtil.loadVideo(getmTTAdNative(), ApplicationPrams.public_qiandao_jili, (int) WH[0], (int) WH[1]);
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected boolean hasBackHome() {
        return false;
    }

    @Override
    protected AdvertisePresenter createPresenter() {
        return new AdvertisePresenter();
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
    public void onBackPressed() {
        super.onBackPressed();
        MyLog.i("我反悔了我出发了回退");
        Intent i = new Intent();
        i.putExtra("result", "返回了");
        setResult(3, i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
