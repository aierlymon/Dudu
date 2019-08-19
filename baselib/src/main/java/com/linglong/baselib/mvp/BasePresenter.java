package com.linglong.baselib.mvp;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 * data
 * ps:在这里添加eventbus是了以后再实现presenter那里可以进行数据传递
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {


    public abstract void showError(String msg);

    //视图接口类
    protected WeakReference<V> mView;

    //retrofit的订阅事件管理类,防止内存泄漏
    private CompositeDisposable mCompositeDisposable;


    protected abstract boolean isUseEventBus();

    @Override
    public void attachView(V view) {
        //判断是否注册事件总线
        if (isUseEventBus())
            EventBus.getDefault().register(this);


        mView = new WeakReference<>(view);

    }

    public V getView() {
        return mView.get();
    }

    //这线程被调用
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDispose() {
        if (mCompositeDisposable != null && mCompositeDisposable.size() > 0) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    @Override
    public void detachView() {
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        //取消所有订阅事件，防止内存泄漏
        unDispose();

        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }


}
