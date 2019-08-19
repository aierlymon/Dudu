package com.linglong.huodai.mvp.presenters;

import com.linglong.baselib.http.HttpMethod;
import com.linglong.baselib.http.myrxsubcribe.MySubscriber;
import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.huodai.mvp.view.RecomViewImpl;
import com.linglong.model.bean.RecommandStateBean;

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