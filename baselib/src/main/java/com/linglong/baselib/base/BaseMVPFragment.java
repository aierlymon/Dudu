package com.linglong.baselib.base;

import com.linglong.baselib.mvp.IPresenter;
import com.linglong.baselib.mvp.IView;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public abstract class BaseMVPFragment<V extends IView,P extends IPresenter> extends BaseFragment implements IView{




    public P mPresenter;

    protected abstract P createPresenter();


    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //这个是父类在oncreate调用的方法
    @Override
    public void init() {
        mPresenter=createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V)this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}
