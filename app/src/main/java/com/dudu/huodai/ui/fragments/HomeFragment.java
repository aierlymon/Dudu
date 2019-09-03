package com.dudu.huodai.ui.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.base.BaseMVPFragment;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.base.BaseTitleFragment;
import com.dudu.huodai.mvp.presenters.HomeFrgPresenter;
import com.dudu.huodai.mvp.view.HomeFrgViewImpl;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;

public class HomeFragment extends BaseTitleFragment<HomeFrgViewImpl, HomeFrgPresenter> implements HomeFrgViewImpl {

    @BindView(R.id.home_recyclerview)
    RecyclerView mRecyclerView;

    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.float_button)
    ImageView floatButton;

    //body的当前刷新页面
    private int currentPage = 1;

    private int pageCount;

    private int count=10;//请求数据个数

    public static HomeFragment newInstance(String info) {
        HomeFragment fragment = new HomeFragment();
        fragment.setTitle(info);
        return fragment;
    }


    @Override
    protected HomeFrgPresenter createPresenter() {
        return new HomeFrgPresenter();
    }


    @Override
    protected int getBodyLayoutRes() {
        return R.layout.fra_home;
    }

    @Override
    protected boolean hasBackHome() {
        return false;
    }

    /*@Override
    protected int getLayoutRes() {
        return R.layout.fra_home;
    }*/




    @Override
    protected void lazyLoadData() {
        //   showLoading();
        mPresenter.requestHead();//请求banner
        mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody(currentPage,count);//请求body
    }


    @Override
    protected void initView() {
        MyLog.i("重新加载");
        setTitleText(R.string.home);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        RequestOptions options = new RequestOptions();
        int size = (int) getResources().getDimension(R.dimen.float_button);
        options.override(size, size); //设置加载的图片大小
        Glide.with(getContext()).load(R.mipmap.take_money).apply(options).into(floatButton);

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

    @Override
    protected void initBefore() {
        super.initBefore();
    }

    @Override
    public void showLoading() {
        //  LoadDialogUtil.getInstance(getActivity(), "正在加载", CustomDialog.DoubleBounce).show();
    }


    @Override
    public void hideLoading() {
        //   LoadDialogUtil.getInstance(getActivity(), "正在加载", CustomDialog.DoubleBounce).cancel();
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
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {
        pageCount=total_pages;
        isNoMore();
        MyLog.i("刷新界面: "+list.size()+"  visable: "+mRecyclerView.getVisibility());
        if(mRecyclerView.getVisibility()==View.GONE){
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {
        isNoMore();
        fragRevAdapyer.notifyDataSetChanged();
        refreshLayout.finishLoadMore();
    }

    public void isNoMore(){
        if(currentPage>=pageCount){
            refreshLayout.setEnableLoadMore(false);
            return;
        }
    }

}
