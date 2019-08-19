package com.linglong.huodai.mvp.presenters;

import com.linglong.baselib.http.HttpMethod;
import com.linglong.baselib.myapplication.App;
import com.linglong.model.bean.HttpResult;
import com.linglong.baselib.http.myrxsubcribe.MySubscriber;
import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.Utils;
import com.linglong.huodai.ApplicationPrams;
import com.linglong.huodai.mvp.view.LoginViewimpl;
import com.linglong.model.bean.LoginCallBackBean;
import com.google.gson.JsonObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginViewimpl> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        MyLog.i("!!!msg: " + msg);
        getView().showError("连接错误: " + msg);
    }

    public boolean checkTelPhoneNumber(String number) {
        number.replaceAll(" ", "");
        return Utils.isPhoneNumber(number);
    }

    public void checkVerificationCode(String number) {
        if (checkTelPhoneNumber(number)) {
            //发起请求
            //让验证码的textView进入倒计时更新
            startTime();
            HttpMethod.getInstance().getVerificationCode(number)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySubscriber<HttpResult<JsonObject>>(this) {
                        @Override
                        public void onSuccess(HttpResult<JsonObject> httpResult) {
                            if (httpResult.getStatusCode() == 200) {
                                //拿去到了验证吗
                            } else {
                                showError(httpResult.getMsg() + ":" + httpResult.getStatusCode());
                            }
                        }

                        @Override
                        public void onFail(Throwable e) {
                            showError(e.getMessage());
                        }

                        @Override
                        public void onCompleted() {

                        }
                    });
        } else {
            getView().showError("手机号码输入不正确");
        }
    }

    private void startTime() {
        getView().startTime();
    }

    public void requeseLogin(String number, String code) {
        number = number.replaceAll(" ", "");
        code = code.replaceAll(" ", "");
        MyLog.i("电话:  " + number + " 验证码: " + code);
        HttpMethod.getInstance().requestLogin(number, code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<LoginCallBackBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<LoginCallBackBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            //拿去到了验证吗
                            ApplicationPrams.loginCallBackBean = httpResult.getData().getUser();
                            App.token = httpResult.getData().getToken();
                            getView().loginSuccess();
                        } else {
                            showError(httpResult.getMsg() + ":" + httpResult.getStatusCode());
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        MyLog.i("我失败了: " + e.getMessage());
                        showError(e.getMessage());
                        getView().showError("验证码不正确");
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }
}
