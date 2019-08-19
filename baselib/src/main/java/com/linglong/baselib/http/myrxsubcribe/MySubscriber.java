package com.linglong.baselib.http.myrxsubcribe;


import com.linglong.baselib.mvp.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 */
public abstract class MySubscriber<T> implements Observer<T> {

    private BasePresenter mPresenter;

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);

    public abstract void onCompleted();

    public MySubscriber(BasePresenter mPresenter){
        this.mPresenter=mPresenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mPresenter.addDisposable(d);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onComplete() {
        onCompleted();
    }
}
