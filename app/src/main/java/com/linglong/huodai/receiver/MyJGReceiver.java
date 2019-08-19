package com.linglong.huodai.receiver;

import android.content.Context;

import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.myjiguang.TagAliasOperatorHelper;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJGReceiver extends JPushMessageReceiver {
    @Override
    public void onTagOperatorResult(Context context,JPushMessage jPushMessage) {
        MyLog.i("MyJGReceiver onTagOperatorResult");
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        MyLog.i("MyJGReceiver onCheckTagOperatorResult");
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        MyLog.i("MyJGReceiver onAliasOperatorResult");
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        MyLog.i("MyJGReceiver onMobileNumberOperatorResult");
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
