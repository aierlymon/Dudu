package com.dudu.baselib.base;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.dudu.baselib.myapplication.App;
import com.dudu.baselib.otherpackage.TToast;
import com.dudu.baselib.otherpackage.config.TTAdManagerHolder;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.KeyBoardUtil;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/*
 * create by hh
 * date 2019-6-25
 * */

/*
 * 1.使用rxpermissions
 * 2.EventBus(使用要重写isUseEventBus 和 在子类 实@Subscrice)
 * 3.StatusBarColor
 * 4.KeyBoard键盘和输入法的弹出和销毁
 * 5.LeakCanary的监听
 * */

public abstract class BaseActivity extends AppCompatActivity {

    private RxPermissions mRxPermissions;
    private final int duration = 2000;
    private TTAdNative mTTAdNative;


    //初始化视图
    abstract void initView();

    protected abstract int getLayoutRes();


    //是否使用事件总线
    public boolean isUseEventBus() {
        return false;
    }

    public abstract boolean isUseLayoutRes();

    protected abstract void startToAdvert(boolean isScreenOn);

    protected abstract void screenOn();

    protected abstract void screenOff();


    private  BroadcastReceiver mBatInfoReceiver;

    private boolean isScreenOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
        // 强制竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(isUseLayoutRes())
        setContentView(getLayoutRes());


        //有注册事件总线,BasePresenter里面也注册了事件总线
        if (isUseEventBus()) {
            EventBus.getDefault().register(this);
        }


        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    MyLog.i("isRunningForeground(): "+isCurrentRunningForeground);
                    if(!isCurrentRunningForeground){
                        MyLog.i("调用了屏幕,点亮屏幕的接口");
                       // screenOn();
                        isScreenOn=true;
                    }
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    screenOff();
                }
            }
        };

        registerReceiver(mBatInfoReceiver, filter);

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        initView();


    }

    public TTAdNative getmTTAdNative() {
        return mTTAdNative;
    }

    //获取RxPermissions,进行权限的动态设置
    public RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            synchronized (this) {
                if (mRxPermissions == null) {
                    mRxPermissions = new RxPermissions(this);
                } else {
                    return mRxPermissions;
                }
            }
        }
        return mRxPermissions;
    }

    //设置状态栏的背景颜色
    public void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setStatusBarColor(BaseActivity.this, color);
    }

    /**
     * 设置状态栏图标的颜色
     *
     * @param dark true: 黑色  false: 白色
     */
    public void setStatusBarIcon(Boolean dark) {
        StatusBarUtil.setStatusBarDarkTheme(this,dark);
    }


    //是否弹出输入框
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isShouldHideKeyboard(v, ev)) {
                KeyBoardUtil.closeKeybord(v, this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //取消事件总线
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        //解决输入法引起的内存泄漏
        KeyBoardUtil.fixInputMethodManagerLeak(this);

        ((App) getApplication()).getRefWatcher(this).watch(this);

        if(mBatInfoReceiver!=null)
        unregisterReceiver(mBatInfoReceiver);

    }



    public void showToast(String msg) {
        CustomToast.showToast(this.getApplicationContext(), msg, duration);
    }


    private boolean isCurrentRunningForeground = true;




    @Override
    protected void onRestart() {
        super.onRestart();
      //  isCurrentRunningForeground=isRunningForeground();
        MyLog.i("startToAdvert 我进入了onRestart: 是否运行在前台: "+isCurrentRunningForeground+"  是否运行在后台: "+isRunBackground(this));
        if (!isCurrentRunningForeground) {
            isCurrentRunningForeground=true;
            //处理跳转到广告页逻辑
            startToAdvert(isScreenOn);
            isCurrentRunningForeground=true;
        }
    }




    public static boolean isRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 表明程序在后台运行
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }



    public boolean isCurrentRunningForeground() {
        return isCurrentRunningForeground;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground= isRunningForeground();
        MyLog.i("startToAdvert onStop isRunningForeground(): "+isCurrentRunningForeground);
    }

    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程,查看该应用是否在运行
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }



    public AdSlot loadAd(String codeId,int width,int height) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .build();

        return adSlot;
    }

}
