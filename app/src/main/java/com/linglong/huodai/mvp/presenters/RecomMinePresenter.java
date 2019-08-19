package com.linglong.huodai.mvp.presenters;

import com.linglong.baselib.http.HttpMethod;
import com.linglong.baselib.http.myrxsubcribe.MySubscriber;
import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.huodai.mvp.view.RecomMineImpl;
import com.linglong.model.bean.HttpResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecomMinePresenter extends BasePresenter<RecomMineImpl> {
    @Override
    public void showError(String msg) {
        getView().showError(msg);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }


    public void nextStep(int id, String phoneNumber, String name, String whoName){
        HttpMethod.getInstance().editUserMsg(String.valueOf(id),phoneNumber,name,whoName)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<String>>(this) {
                    @Override
                    public void onSuccess(HttpResult<String> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            getView().nextStep();
                        } else {
                            showError(httpResult.getMsg()+":"+httpResult.getStatusCode());
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
    }

}
