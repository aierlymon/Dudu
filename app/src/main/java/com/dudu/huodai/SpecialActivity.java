package com.dudu.huodai;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.SpecialPresenter;
import com.dudu.huodai.mvp.view.SpecialImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialActivity extends BaseTitleActivity<SpecialImpl, SpecialPresenter> implements SpecialImpl {


    @BindView(R.id.special_recyclerview)
    RecyclerView mRecyclerView;
    private List<BaseMulDataModel> baseMulDataModels;
    private HomeFragRevAdapyer fragRevAdapyer;

    @BindView(R.id.special_refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.float_button)
    ImageView floatButton;

    //body的当前刷新页面
    private int currentPage = 1;

    private int pageCount;//总页数

    private int count = 10;//请求数据个数

    private int id;
    private String title;

    @Override
    protected SpecialPresenter createPresenter() {
        return new SpecialPresenter();
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_special;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }


    @Override
    public void initRequest() {
        Intent intent = getIntent();
        id = intent.getIntExtra(ApplicationPrams.key_id, -1);
        title = intent.getStringExtra(ApplicationPrams.key_title);

        //   showLoading();
        //mPresenter.requestHead();//请求banner
     //   mPresenter.requestMenu();//请求菜单
        mPresenter.requestBody(id, currentPage, count);//请求body
    }

    @Override
    public void init() {
        //butterknife的绑定
        ButterKnife.bind(this);

        RequestOptions options = new RequestOptions();
        int size = (int) getResources().getDimension(R.dimen.float_button);
        options.override(size, size); //设置加载的图片大小
        Glide.with(this).load(R.mipmap.take_money).apply(options).into(floatButton);

        setTitle(title);

        MyLog.i("重新加载");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new HomeFragRevAdapyer(this, mPresenter.getList());
      //  mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, (int) getResources().getDimension(R.dimen.y10)));
        mRecyclerView.setAdapter(fragRevAdapyer);
        fragRevAdapyer.setActivityTheme(ApplicationPrams.SpecialActivity);

        //刷新设置
        //刷新设置
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (NetWorkStateBroadcast.isOnline.get()) {
                currentPage = 1;
                refreshLayout.setEnableLoadMore(true);
              //  mPresenter.requestMenu();//请求菜单
                mPresenter.requestBody(id, currentPage, count);//请求body
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
                mPresenter.requestBodyPage(id, currentPage, count);
            } else {
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void startToAdvert(boolean isScreenOn) {
        Intent intent=new Intent(this,AdvertSplashActivity.class);
        if(isScreenOn){
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_sceenon_advertId);
        }else{
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_restart_advertId);
        }
        startActivity(intent);
    }

    @Override
    protected void screenOn() {

    }

    @Override
    protected void screenOff() {

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
        CustomToast.showToast(getApplicationContext(), msg, 2000);
    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list, int total_pages) {
        pageCount = total_pages;
        isNoMore();
        MyLog.i("刷新界面: " + list.size() + "  visable: " + mRecyclerView.getVisibility() + " pageCount: " + pageCount);
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        fragRevAdapyer.setActivityTheme(ApplicationPrams.SpecialActivity);
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
        isNoMore();
    }

    public void isNoMore(){
        if(currentPage>=pageCount){
            refreshLayout.setEnableLoadMore(false);
            return;
        }
    }



}
