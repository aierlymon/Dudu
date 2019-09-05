package com.dudu.huodai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.MainThread;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.model.postbean.AdvertSplashBean;
import com.dudu.huodai.mvp.presenters.AdvertisePresenter;
import com.dudu.huodai.mvp.view.AdvertiseImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.WeakHandler;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 开屏广告Activity示例
 */
public class AdvertSplashActivity extends BaseMvpActivity<AdvertiseImpl, AdvertisePresenter> implements WeakHandler.IHandler {
    private static final String TAG = "SplashActivity";
    private TTAdNative mTTAdNative;
    private FrameLayout mSplashContainer;


    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private static final int MSG_GO_MAIN = 1;
    //开屏广告是否已经加载
    private boolean mHasLoaded;

    private String adverId;
    private int index;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_advertsplash;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void startToAdvert(boolean isScreenOn) {

    }

    @Override
    protected void screenOn() {

    }

    @Override
    protected void screenOff() {

    }


    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        //StatusBarUtil.setRootViewFitsSystemWindows(this,true);

        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        //这个就是设施沉浸式状态栏的主要方法了
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }


        mSplashContainer = (FrameLayout) findViewById(R.id.splash_container);
        //step2:创建TTAdNative对象
        mTTAdNative = getmTTAdNative();
        //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
        //在开屏时候申请不太合适，因为该页面倒计时结束或者请求超时会跳转，在该页面申请权限，体验不好
        // TTAdManagerHolder.getInstance(this).requestPermissionIfNecessary(this);
        //定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);

        loadSplashAd();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSplashTiao(AdvertSplashBean advertSplashBean) {
        MyLog.i("我到了这里advertSplashBean: " + advertSplashBean.getType());
        switch (advertSplashBean.getType()) {
            case ApplicationPrams.GameCai:
                goToCai();
                break;
            case ApplicationPrams.GameCiccle:
                goToGameCircle();
                break;
            case ApplicationPrams.GameGoldEgg:
                goToGameEgg();
                break;
            case ApplicationPrams.GameTree:
                goToGameTree();
                break;
        }
    }

    @Override
    protected void onResume() {
        //判断是否该跳转到主页面
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * 加载开屏广告
     * "828300203"
     */

    private void loadSplashAd() {
        Intent intent = getIntent();
        adverId = intent.getStringExtra(ApplicationPrams.adverId);
        index = intent.getIntExtra(ApplicationPrams.key_tiaozhuan, -1);
        MyLog.i("loadSplashAd " + index);
        float[] WH = Utils.getScreenWH(this.getApplicationContext());
        if (index != -1) {
            AdvertUtil advertUtil = null;
            advertUtil = new AdvertUtil(mSplashContainer, this);
            advertUtil.loadSplashAd(mTTAdNative, adverId, (int) WH[0], (int) WH[1], AD_TIME_OUT, index);
        } else {
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(adverId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize((int) WH[0], (int) WH[1])
                    .build();

            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            if (TextUtils.isEmpty(adverId)) {
                adverId = "828300203";
            }

            MyLog.i("我进来了广告id为: " + adverId);
            //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
            mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                @MainThread
                public void onError(int code, String message) {
                    Log.d(TAG, message);
                    mHasLoaded = true;
                    showToast(message);
                    goToMainActivity();
                }

                @Override
                @MainThread
                public void onTimeout() {
                    mHasLoaded = true;
                    showToast("开屏广告加载超时");
                    goToMainActivity();
                }

                @Override
                @MainThread
                public void onSplashAdLoad(TTSplashAd ad) {
                    Log.d(TAG, "开屏广告请求成功");
                    mHasLoaded = true;
                    mHandler.removeCallbacksAndMessages(null);
                    if (ad == null) {
                        return;
                    }
                    //获取SplashView
                    View view = ad.getSplashView();
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();

                    //设置SplashView的交互监听器
                    ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {
                            Log.d(TAG, "onAdClicked");
                            showToast("开屏广告点击");
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            Log.d(TAG, "onAdShow");
                            showToast("开屏广告展示");
                        }

                        @Override
                        public void onAdSkip() {
                            Log.d(TAG, "onAdSkip");
                            showToast("开屏广告跳过");

                            if (adverId.equals("828300203")) {
                                goToMainActivity();
                            } else {
                                finish();
                            }

                        }

                        @Override
                        public void onAdTimeOver() {
                            Log.d(TAG, "onAdTimeOver");
                            showToast("开屏广告倒计时结束");
                            if (adverId.equals("828300203")) {
                                goToMainActivity();
                            } else {
                                close();
                            }
                        }
                    });
                }
            }, AD_TIME_OUT);
        }


    }

    private void close() {
        mSplashContainer.removeAllViews();
        this.finish();
    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(AdvertSplashActivity.this, MainActivity.class);
        startActivity(intent);
        close();
    }


    /**
     * 跳转到主页面
     */
    private void goToGameTree() {
        Intent intent = new Intent(AdvertSplashActivity.this, GameTreeActivity.class);
        startActivity(intent);
        close();
    }


    /**
     * 跳转到主页面
     */
    private void goToGameCircle() {
        Intent intent = new Intent(AdvertSplashActivity.this, GameCircleActivity.class);
        startActivity(intent);
        close();
    }


    /**
     * 跳转到主页面
     */
    private void goToGameEgg() {
        Intent intent = new Intent(AdvertSplashActivity.this, GameSmashEggActivity.class);
        startActivity(intent);
        close();
    }


    /**
     * 跳转到主页面
     */
    private void goToCai() {
        Intent intent = new Intent(AdvertSplashActivity.this, GameCaiActivity.class);
        startActivity(intent);
        close();
    }


    @Override
    public void handleMsg(Message msg) {
    }

    @Override
    protected AdvertisePresenter createPresenter() {
        return new AdvertisePresenter();
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
