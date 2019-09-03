package com.dudu.baselib.myapplication;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.otherpackage.config.TTAdManagerHolder;
import com.dudu.baselib.utils.Utils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class App extends Application {

    private RefWatcher mRefWatcher;
    public static String token="";

    public static final String APP_ID = "wxd930ea5d5a258f4f";
    public static  final  String APP_WB_KEY="wxd930ea5d5a258f4f";

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = setupLeakCanary();
        // 主要是添加下面这句代码
        MultiDex.install(this);



        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        //获取注册的推送ID
      /*  String registrationId = JPushInterface.getRegistrationID(this);
        MyLog.i("极光推送注册ID："+registrationId);*/

        HttpConstant.context = this.getApplicationContext();
        NetWorkStateBroadcast.isOnline.set(Utils.isNetworkConnected(this));

        //微博
        WbSdk.install(this,new AuthInfo(this, APP_WB_KEY, "http://www.sina.com", SCOPE));

      //  TTAdManager.requestPermissionIfNecessary(this);
        //穿山甲SDK初始化
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    private RefWatcher setupLeakCanary() {
        RefWatcher refWatcher = null;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher = RefWatcher.DISABLED;
        } else {
            refWatcher = LeakCanary.install(this);
        }
        return refWatcher;
    }

    public RefWatcher getRefWatcher(Context context) {
        return mRefWatcher;
    }
}
