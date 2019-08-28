package com.dudu.huodai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.LabelPresenter;
import com.dudu.huodai.mvp.view.LabelImpl;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.huodai.ui.adapter.decoration.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LabelActivity extends BaseTitleActivity<LabelImpl, LabelPresenter> implements LabelImpl {

    @BindView(R.id.label_recyclerview)
    RecyclerView mRecyclerView;
    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    @BindView(R.id.label_refreshLayout)
    SmartRefreshLayout refreshLayout;

    //body的当前刷新页面
    private int currentPage = 1;

    private int pageCount;

    private int count=10;//请求数据个数


    @Override
    protected LabelPresenter createPresenter() {
        return new LabelPresenter();
    }



    @Override
    protected int getBodyLayoutRes() {
      return   R.layout.activity_label;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRequest();
        init();
    }

    private int id;
    private String mTitle;

    @Override
    public void initRequest() {
        Intent intent=getIntent();
        id=intent.getIntExtra(ApplicationPrams.key_id,-1);
        mTitle=intent.getStringExtra(ApplicationPrams.key_title);
        //   showLoading();
        //mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody(id,currentPage,count);//请求body
    }



    @Override
    public void init() {
        //butterknife的绑定
        ButterKnife.bind(this);

        MyLog.i("重新加载");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new HomeFragRevAdapyer(this, mPresenter.getList());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0,0,(int) getResources().getDimension(R.dimen.y10)));
        mRecyclerView.setAdapter(fragRevAdapyer);

        //刷新设置
        //刷新设置
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage = 1;
                refreshLayout.setEnableLoadMore(true);
                mPresenter.requestHead();//请求banner
                mPresenter.requestMenu();//请求菜单
                mPresenter.requestBody(id,currentPage,count);//请求body
            }else{
                if (this.refreshLayout.isRefreshing()) {
                    showError("没有网络");
                    refreshLayout.finishRefresh();
                }
            }

        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了2   currentPage: "+currentPage+"   pageCount: "+pageCount);
            isNoMore();
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage++;
                mPresenter.requestBodyPage(id,currentPage, count);
            }else{
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });

        setTitle(mTitle);
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
        if (refreshLayout!=null&&refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
        if(refreshLayout!=null&&refreshLayout.isLoading()){
            refreshLayout.finishLoadMore();
        }
        CustomToast.showToast(getApplicationContext(), msg, 2000);
    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {
        pageCount=total_pages;
        isNoMore();
        MyLog.i("刷新界面: "+list.size()+"  visable: "+mRecyclerView.getVisibility()+" pageCount: "+pageCount);
        if(mRecyclerView.getVisibility()== View.GONE){
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.setActivityTheme(ApplicationPrams.LabelActivity);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {
        fragRevAdapyer.notifyDataSetChanged();
        refreshLayout.finishLoadMore();
        isNoMore();
    }

    public void isNoMore(){
        if(currentPage>=pageCount){
            refreshLayout.setEnableLoadMore(false);
            return;
        }
    }
}
