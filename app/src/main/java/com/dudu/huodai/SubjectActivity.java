package com.dudu.huodai;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.SubjectPresenter;
import com.dudu.huodai.mvp.view.SubjectImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class SubjectActivity extends BaseTitleActivity<SubjectImpl, SubjectPresenter> implements SubjectImpl {
    @BindView(R.id.recyclerview_subject)
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
    protected SubjectPresenter createPresenter() {
        return new SubjectPresenter();
    }



    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_subject;
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

    }


    private int id;
    private String title;


    @Override
    public void initRequest() {
        Intent intent = getIntent();
        id = intent.getIntExtra(ApplicationPrams.key_id, -1);
        title=intent.getStringExtra(ApplicationPrams.key_title);



        if(id==0){
            mPresenter.requestSubject();//请求菜单
        }else if(id==1){
            mPresenter.requestSeries();//请求菜单
        }

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle(title);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);


        fragRevAdapyer = new HomeFragRevAdapyer(this, mPresenter.getList());
       // mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, (int) getResources().getDimension(R.dimen.y10)));
        mRecyclerView.setAdapter(fragRevAdapyer);


        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (NetWorkStateBroadcast.isOnline.get()) {
                currentPage = 1;
                initRequest();//请求数据
            } else {
                if (this.refreshLayout.isRefreshing()) {
                    showError("没有网络");
                    refreshLayout.finishRefresh();
                }
            }

        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了2");
            if (NetWorkStateBroadcast.isOnline.get()) {
                currentPage++;
                // mPresenter.requestBodyPage(0, 0, 0, currentPage);

                /*调试阶段*/
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            } else {
                if (refreshLayout.isLoading()) {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }




    @Override
    public void refreshHome(List<BaseMulDataModel> list) {
        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.setActivityTheme(ApplicationPrams.SubjctActivity);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
    }
}
