package com.dudu.huodai.ui.fragments;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.otherpackage.TToast;
import com.dudu.baselib.otherpackage.config.TTAdManagerHolder;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.base.BaseTitleFragment;
import com.dudu.huodai.mvp.model.HomeOtherAdvertHolder;
import com.dudu.huodai.mvp.presenters.HomeFrgPresenter;
import com.dudu.huodai.mvp.view.HomeFrgViewImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.HomeFragRevAdapyer;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.huodai.widget.DislikeDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

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
    private TTNativeExpressAd mTTAD;

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

        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getContext().getApplicationContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());



        //初始化第三方库
        //requesetOtherAdvert();

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
                requesetOtherAdvert();
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

    //初始化第三方包
    private TTAdNative mTTAdNative;
    private void requesetOtherAdvert() {

        float[] WH=Utils.getScreenWH(getContext());
       int width= Utils.px2dip(getContext(),WH[0]);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ApplicationPrams.public_firstpage_advertId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(width,1020) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(100,200) //这个参数设置即可，不影响个性化模板广告的size
                .build();

        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                MyLog.i("load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAD=ads.get(0);
                bindAdListener(mTTAD);
                mTTAD.render();//调用render开始渲染广告
                MyLog.i("ad是 广告信息: "+"  view: "+ads.get(0).getExpressAdView());

            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {

            }

            @Override
            public void onAdShow(View view, int type) {

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                MyLog.i("我渲染成功了: "+view);
                //返回view的宽高 单位 dp
                mPresenter.getList().add(2,new HomeOtherAdvertHolder(view));
                fragRevAdapyer.notifyDataSetChanged();
            }
        });
        bindDislike(ad,true);
    }

    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(getContext(), words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {

                    //用户选择不喜欢原因后，移除广告展示
                    mPresenter.getList().remove(2);
                    fragRevAdapyer.notifyDataSetChanged();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //用户选择不喜欢原因后，移除广告展示
                mPresenter.getList().remove(2);
                fragRevAdapyer.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                TToast.show(getContext(), "点击取消 ");
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTTAD!=null)
        mTTAD.destroy();
    }
}
