package com.linglong.huodai.ui.fragments;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.baselib.base.BaseMVPFragment;
import com.linglong.baselib.broadcast.NetWorkStateBroadcast;
import com.linglong.baselib.utils.CustomToast;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.R;
import com.linglong.huodai.mvp.presenters.HomeFrgPresenter;
import com.linglong.huodai.mvp.view.HomeFrgViewImpl;
import com.linglong.huodai.ui.adapter.HomeFragRevAdapyer;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseMVPFragment<HomeFrgViewImpl, HomeFrgPresenter> implements HomeFrgViewImpl {

    @BindView(R.id.home_recyclerview)
    RecyclerView mRecyclerView;
    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    //body的当前刷新页面
    private int currentPage = 1;

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
    protected int getLayoutRes() {
        return R.layout.fra_home;
    }

    @Override
    protected void lazyLoadData() {
        //   showLoading();
        mPresenter.requestHead();//请求banner
        mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody();//请求body
    }

    @Override
    protected void initView() {
        MyLog.i("重新加载");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new HomeFragRevAdapyer(getActivity(), mPresenter.getList());
        mRecyclerView.setAdapter(fragRevAdapyer);

        //刷新设置
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage = 1;
                mPresenter.requestHead();//请求banner
                mPresenter.requestMenu();//请求菜单
                mPresenter.requestBody();//请求body
            }else{
                if (this.refreshLayout.isRefreshing()) {
                    showError("没有网络");
                    refreshLayout.finishRefresh();
                }
            }

        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了2");
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage++;
                mPresenter.requestBodyPage(0, 0, 0, currentPage);
            }else{
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
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
    public void refreshHome(List<BaseMulDataModel> list) {
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
        fragRevAdapyer.notifyDataSetChanged();
        refreshLayout.finishLoadMore();
    }

}
