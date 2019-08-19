package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.huodai.mvp.view.RecomViewImpl;
import com.dudu.model.bean.RecommandStateBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecomFrgPresenter extends BasePresenter<RecomViewImpl> {
    @Override
    public void showError(String msg) {
        getView().showError(msg);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    public void loadRecomand() {
        HttpMethod.getInstance().loadRecomStaet()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<RecommandStateBean>(this) {
                    @Override
                    public void onSuccess(RecommandStateBean recommandStateBean) {
                        //成功之后要
                        getView().refreshTitle(recommandStateBean.getMsgOne()+":"+recommandStateBean.getMsgTwo());
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