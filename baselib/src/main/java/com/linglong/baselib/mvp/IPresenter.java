package com.linglong.baselib.mvp;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public interface IPresenter<V extends IView> {
   void attachView(V view);

   void detachView();
}
