package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.AllStoryImpl;
import com.dudu.model.bean.AllStoryBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class AllStoryPresenter extends BasePresenter<AllStoryImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    //请求Body内容的
    //请求Body内容的
    public void requestAllStroy() {
        HttpMethod.getInstance().requestAllStory()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<AllStoryBean>(this) {
                    @Override
                    public void onSuccess(AllStoryBean httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            getView().refreshHome(httpResult);
                        } else {
                            showError(httpResult.getResult());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        showError(String.valueOf(e.getMessage()));
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }
}
