package com.dudu.huodai;

import android.content.Intent;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.AdvertisePresenter;
import com.dudu.huodai.mvp.view.AdvertiseImpl;

public class AdvertisementActivity extends BaseTitleActivity<AdvertiseImpl, AdvertisePresenter> implements AdvertiseImpl {
    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {

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
