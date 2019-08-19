package com.linglong.baselib.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.linglong.baselib.R;
import com.linglong.baselib.mvp.IPresenter;
import com.linglong.baselib.mvp.IView;
/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public abstract class BaseMvpTitleActivity<V extends IView,P extends IPresenter<V>> extends BaseMvpActivity<V,P>{

    private Toolbar toolbar;
    private TextView mToolBarTitle;
    private View bodyView;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_title;
    }

    protected abstract int getBodyLayoutRes();

    protected abstract boolean hasBackHome();

    protected abstract boolean isShowToolbar();

    public void showToolbar(){
        if(toolbar!=null){
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideToolbar(){
        if(toolbar!=null){
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
   public void initView() {
        super.initView();
        toolbar = ((Toolbar) findViewById(R.id.base_toolbar));
        if(isShowToolbar()){
            showToolbar();
        }
        toolbar.setTitle("");
        mToolBarTitle = ((TextView) findViewById(R.id.base_title_tv));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackHome());
        bodyView=LayoutInflater.from(this).inflate(getBodyLayoutRes(),null);
        ((FrameLayout) findViewById(R.id.base_container)).addView(bodyView);
    }

    public View getBodyView() {
        return bodyView;
    }

    public void setTitle(String titleName){
        mToolBarTitle.setText(titleName);
    }

    public void setTitle(@StringRes int stringRes ){
        mToolBarTitle.setText(stringRes);
    }

    public void setTitleColor(@ColorInt int colorRes){
        mToolBarTitle.setTextColor(colorRes);
    }

    public void setTitleBackground(@ColorInt int colorRes){
        toolbar.setBackgroundColor(colorRes);
    }
}
