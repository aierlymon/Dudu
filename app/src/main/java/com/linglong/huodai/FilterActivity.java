package com.linglong.huodai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.baselib.base.BaseMvpActivity;
import com.linglong.baselib.broadcast.NetWorkStateBroadcast;
import com.linglong.baselib.utils.CustomToast;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.StatusBarUtil;
import com.linglong.huodai.mvp.model.postbean.RecomBean;
import com.linglong.huodai.mvp.presenters.FliterPresenter;
import com.linglong.huodai.mvp.view.FliterImpl;
import com.linglong.huodai.ui.adapter.HomeFragRevAdapyer;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends BaseMvpActivity<FliterImpl, FliterPresenter> implements FliterImpl {

    //过滤选项的实体类
    private RecomBean recomBean;

    //toolbar的标题
    @BindView(R.id.tx_title)
    TextView tx;

    //返回按钮
    @BindView(R.id.img_back)
    ImageView back;

    @BindView(R.id.filter_recv)
    RecyclerView mRecyclerView;

    @BindView(R.id.filter_refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    //body的当前刷新页面
    private int currentPage = 1;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_filter;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

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

        //根据条件加载内容
        recomBean = new RecomBean();
        Intent intent = getIntent();
        int[] moneys = intent.getIntArrayExtra("moneys");
        int[] dates = intent.getIntArrayExtra("dates");
        recomBean.setMoneyMin(moneys[0]);
        recomBean.setMoneyMax(moneys[1]);
        recomBean.setDateMin(dates[0]);
        recomBean.setDateMax(dates[1]);

        super.onCreate(savedInstanceState);

        //黄油刀
        ButterKnife.bind(this);
        //设置标题和返回键
        //设置头部
        tx.setText(getString(R.string.recommand_title));
        back.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        baseMulDataModels = new ArrayList<>();
        fragRevAdapyer = new HomeFragRevAdapyer(this, baseMulDataModels);
        mRecyclerView.setAdapter(fragRevAdapyer);

        //进行筛选请求
        mPresenter.requestBody(0, recomBean.getMoneyMin(), recomBean.getMoneyMax());

        //刷新按钮的刷新是按
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            MyLog.i("我触发了上拉刷新");
            mPresenter.requestBody(0, recomBean.getMoneyMin(), recomBean.getMoneyMax());
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了上拉加载更多: "+recomBean.getMoneyMin()+"  "+recomBean.getMoneyMax());
            if(NetWorkStateBroadcast.isOnline.get()){
                currentPage++;
                mPresenter.requestBodyPage(0, recomBean.getMoneyMin(), recomBean.getMoneyMax(), currentPage);
            }else{
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    protected FliterPresenter createPresenter() {
        return new FliterPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        CustomToast.showToast(getApplicationContext(), msg, 2000);
        if (refreshLayout!=null&&refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
        if(refreshLayout!=null&&refreshLayout.isLoading()){
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void refreshFilter(List<BaseMulDataModel> list) {
        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing())
            refreshLayout.finishRefresh();
    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadMore();
        }
    }

    @OnClick({R.id.img_back})
    public void OnClick(View v) {
        finish();
    }
}
