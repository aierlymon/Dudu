package com.dudu.huodai;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
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
public class AllStoryActivity extends BaseMvpActivity<AllStoryImpl, AllStoryPresenter> implements AllStoryImpl {

    @BindView(R.id.recyclerview_all)
    RecyclerView recycyclerView;

    private AllStoryRevAdapter allStoryRevAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

        //这个就是设施沉浸式状态栏的主要方法了
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        initRequest();
        init();
    }

    private void initRequest() {
        mPresenter.requestAllStroy();
    }
    List<AllStoryBean.ClassificationBean> classificationBeans;
    private void init() {
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
    protected int getLayoutRes() {
        return R.layout.activity_all_story;
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
