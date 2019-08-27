package com.dudu.huodai;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.AllStoryPresenter;
import com.dudu.huodai.mvp.view.AllStoryImpl;
import com.dudu.huodai.ui.adapter.AllStoryRevAdapter;
import com.dudu.huodai.ui.adapter.decoration.SpaceItemDecoration;
import com.dudu.model.bean.AllStoryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class AllStoryActivity extends BaseTitleActivity<AllStoryImpl, AllStoryPresenter> implements AllStoryImpl {

    @BindView(R.id.recyclerview_all)
    RecyclerView recycyclerView;

    private AllStoryRevAdapter allStoryRevAdapter;



    @Override
    public void initRequest() {
        mPresenter.requestAllStroy();
    }


    List<AllStoryBean.ClassificationBean> classificationBeans;


    @Override
    public void init() {
        ButterKnife.bind(this);
        classificationBeans=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected AllStoryPresenter createPresenter() {
        return new AllStoryPresenter();
    }



    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_all_story;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void refreshHome(AllStoryBean httpResult) {
        MyLog.i("我已经更新了所有故事: "+httpResult.getClassification().size());
        List<AllStoryBean.ClassificationBean> classificationBeans=httpResult.getClassification();

        AllStoryBean.ClassificationBean classificationBean1=new AllStoryBean.ClassificationBean();
        classificationBean1.setName("专题故事");
        List<AllStoryBean.ClassificationBean.TagListBean> tagListBeans1=new ArrayList<>();
        classificationBean1.setTag_list(tagListBeans1);

        AllStoryBean.ClassificationBean classificationBean2=new AllStoryBean.ClassificationBean();
        classificationBean2.setName("连载故事");
        List<AllStoryBean.ClassificationBean.TagListBean> tagListBeans2=new ArrayList<>();
        classificationBean2.setTag_list(tagListBeans2);

        classificationBeans.add(0,classificationBean1);
        classificationBeans.add(1,classificationBean2);
        allStoryRevAdapter=new AllStoryRevAdapter(this,classificationBeans);
        recycyclerView.addItemDecoration(new SpaceItemDecoration(0,0,(int) getResources().getDimension(R.dimen.x20)));
        recycyclerView.setAdapter(allStoryRevAdapter);
    }
}
