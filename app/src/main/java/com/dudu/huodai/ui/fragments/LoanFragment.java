package com.dudu.huodai.ui.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.base.BaseMVPFragment;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.presenters.LoanFrgPresenter;
import com.dudu.huodai.mvp.view.LoanFrgViewImpl;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.LoanFragRevAdapter;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.NewHomeMenuBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class LoanFragment extends BaseMVPFragment<LoanFrgViewImpl, LoanFrgPresenter> implements LoanFrgViewImpl {

    @BindView(R.id.recv_loan)
    RecyclerView mRecyclerView;


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.tx_title)
    TextView txTitle;

    //body的当前刷新页面
    private int currentPage = 1;

    private int pageCount;

    private int count = 10;//请求数据个数

    private LoanFragRevAdapter fragRevAdapyer;


    public static LoanFragment newInstance(String info) {
        LoanFragment fragment = new LoanFragment();
        fragment.setTitle(info);
        return fragment;
    }

    @Override
    protected LoanFrgPresenter createPresenter() {
        return new LoanFrgPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fra_loan;
    }

    @Override
    protected void lazyLoadData() {
        //   showLoading();
        mPresenter.requestHead();//请求banner
        mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody(currentPage, count);//请求body
    }

    @Override
    protected void initView() {
        MyLog.i("重新加载");
        txTitle.setText(getResources().getString(R.string.loan));

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new LoanFragRevAdapter(getActivity(), mPresenter.getList());
        mRecyclerView.setAdapter(fragRevAdapyer);

        //刷新设置
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (NetWorkStateBroadcast.isOnline.get()) {
                currentPage = 1;
                refreshLayout.setEnableLoadMore(true);
                mPresenter.requestHead();//请求banner
                mPresenter.requestMenu();//请求菜单
                mPresenter.requestBody(currentPage, count);//请求body
            } else {
                if (this.refreshLayout.isRefreshing()) {
                    showError("没有网络");
                    refreshLayout.finishRefresh();
                }
            }

        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了2   currentPage: " + currentPage + "   pageCount: " + pageCount);
            isNoMore();
            if (NetWorkStateBroadcast.isOnline.get()) {
                currentPage++;
                mPresenter.requestBodyPage(currentPage, count);
            } else {
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    protected void initBefore() {

    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {
        pageCount = total_pages;
        isNoMore();
        MyLog.i("刷新界面: " + list.size() + "  visable: " + mRecyclerView.getVisibility());
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
    }

    @Override
    public void refreshTypeFliter(List<NewHomeMenuBean.LoanCategoriesBean> loanCategories) {

    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {
        isNoMore();
        fragRevAdapyer.notifyDataSetChanged();
        refreshLayout.finishLoadMore();
    }

    public void isNoMore() {
        if (currentPage >= pageCount) {
            refreshLayout.setEnableLoadMore(false);
            return;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
        if (refreshLayout != null && refreshLayout.isLoading()) {
            refreshLayout.finishLoadMore();
        }
        CustomToast.showToast(getContext().getApplicationContext(), msg, 2000);
    }
}
