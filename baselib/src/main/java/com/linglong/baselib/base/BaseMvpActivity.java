package com.linglong.baselib.base;

import com.linglong.baselib.mvp.IPresenter;
import com.linglong.baselib.mvp.IView;
/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
//这个才是加载mvp的activity，和继承BaseActivity的类分开,没有title的
public abstract class BaseMvpActivity<V extends IView,P extends IPresenter<V>> extends BaseActivity implements IView{

    protected P mPresenter;




    protected abstract P createPresenter();



    @Override
     void initView() {
        mPresenter=createPresenter();
        if(mPresenter!=null)
        mPresenter.attachView((V) this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
        if(mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
    }
}
