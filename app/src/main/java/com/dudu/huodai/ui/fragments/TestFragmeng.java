package com.dudu.huodai.ui.fragments;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.base.BaseTitleFragment;
import com.dudu.huodai.mvp.presenters.HomeFrgPresenter;
import com.dudu.huodai.mvp.view.HomeFrgViewImpl;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class TestFragmeng extends BaseTitleFragment<HomeFrgViewImpl,HomeFrgPresenter> implements HomeFrgViewImpl{

    @BindView(R.id.tx)
    TextView mTx;

    @BindView(R.id.home_recyclerview)
    RecyclerView mRecyclerView;

    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    //body的当前刷新页面
    private int currentPage = 1;

    private int pageCount;

    private int count=10;//请求数据个数

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected HomeFrgPresenter createPresenter() {
        return new HomeFrgPresenter();
    }

    @Override
    protected void lazyLoadData() {
        mPresenter.requestHead();//请求banner
        mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody(1,10);//请求body
    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {

    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {

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
        CustomToast.showToast(getContext().getApplicationContext(), msg, 2000);
    }

    @Override
    public void initView() {
        mTx.setText("这又成功了");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new HomeFragRevAdapyer(getActivity(), mPresenter.getList());
        mRecyclerView.setAdapter(fragRevAdapyer);

        //刷新设置
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage = 1;
                refreshLayout.setEnableLoadMore(true);
                mPresenter.requestHead();//请求banner
                mPresenter.requestMenu();//请求菜单
                mPresenter.requestBody(currentPage,count);//请求body
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
                mPresenter.requestBodyPage(currentPage, count);
            }else{
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    public void isNoMore(){
        if(currentPage>=pageCount){
            refreshLayout.setEnableLoadMore(false);
            return;
        }
    }
}
