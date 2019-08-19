package com.linglong.huodai.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.Utils;
import com.linglong.huodai.ApplicationPrams;
import com.linglong.huodai.R;
import com.linglong.huodai.mvp.model.HomeFRBannerHolder;
import com.linglong.huodai.mvp.model.HomeFRBodyHolder;
import com.linglong.huodai.mvp.model.HomeFRBodyHolderFH;
import com.linglong.huodai.mvp.model.HomeFRMenuHolder;
import com.linglong.huodai.mvp.model.postbean.BannerBean;
import com.linglong.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.linglong.huodai.mvp.model.postbean.RecordBean;
import com.linglong.huodai.mvp.model.postbean.WebViewBean;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.huodai.ui.adapter.base.BaseMulViewHolder;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.LoopViewPager;
import com.linglong.model.bean.NewHomeBannerBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragRevAdapyer extends RecyclerView.Adapter<BaseMulViewHolder> {

    List<BaseMulDataModel> modelList;
    private static final int BANNER = 0;
    private static final int MENU = 1;
    private static final int BODY = 2;
    private static final int HISTORY = 3;
    private Activity mContext;
    private WebViewBean webViewBean;//EventBus进行WebActivity页面跳转的实体类
    private BannerHolder bannerHolder;

    public HomeFragRevAdapyer(Activity mContext, List<BaseMulDataModel> modelList) {
        this.modelList = modelList;
        this.mContext = mContext;
        webViewBean = new WebViewBean();
    }


    public void setModelList(List<BaseMulDataModel> modelList) {
        this.modelList = modelList;
    }

    public List<BaseMulDataModel> getModelList() {
        return modelList;
    }

/*    @Override
    public long getItemId(int position){
        return modelList.get(position).hashCode();
    }*/

    @NonNull
    @Override
    public BaseMulViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case BANNER:
                bannerHolder= new BannerHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_banner, viewGroup, false));
                return bannerHolder;
            case MENU:
                return new MenuHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_menu, viewGroup, false));
            case BODY:
                return new BodyHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_body, null));
            case HISTORY:
                return new BodyHolderFH(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_body, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMulViewHolder baseMulViewHolder, int position) {
        MyLog.i("HomeFragRevAdapyer onBindViewHolder次数: " + position + "  modelList.size: " + modelList.size());
        baseMulViewHolder.bindData(modelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (modelList.get(position) instanceof HomeFRBannerHolder) {
            MyLog.i("检测到HomeFRBannerHolder");
            return BANNER;
        } else if (modelList.get(position) instanceof HomeFRMenuHolder) {
            return MENU;
        } else if(modelList.get(position) instanceof HomeFRBodyHolder){
            return BODY;
        }else{
            return HISTORY;
        }
    }

    private RecordBean recordBean = new RecordBean();
    private BannerBean bannerBean=new BannerBean();

    private boolean isBannerStart;//防止定时器多发

    class BannerHolder extends BaseMulViewHolder<HomeFRBannerHolder> {
        @BindView(R.id.banner_viewpager)
        LoopViewPager loopViewPager;

        private List<String> dataList;

        public BannerHolder(View itemView) {
            super(itemView);
            dataList = new ArrayList<>();

        }

        @Override
        public void bindData(HomeFRBannerHolder dataModel, int position) {
            MyLog.i("调用了多少次： " + dataList.size());
            dataList.clear();
            for (NewHomeBannerBean.BannersBean bannersBean : dataModel.getNewHomeBannerBean().getBanners()) {
                dataList.add(HttpConstant.BASE_URL + bannersBean.getIcon());
            }

            if (dataList.size() > 1) {
                loopViewPager.showIndicator(true);
                if(!isBannerStart){
                    loopViewPager.startBanner();
                }else{
                    loopViewPager.setCurrentItem(0);
                }
                isBannerStart=true;
                loopViewPager.setIndicatorGravity(LoopViewPager.IndicatorGravity.RIGHT);
            }

            if (dataList.size() == 0) return;

            loopViewPager.setData(mContext, dataList, (view, position1, item) -> {
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setOnClickListener(view1 -> {
                    MyLog.i("banner 点击之后: " + dataModel.getNewHomeBannerBean().getBanners().get(position1).getUrl());
                    webViewBean.setUrl(dataModel.getNewHomeBannerBean().getBanners().get(position1).getUrl());
                    webViewBean.setTag(null);
                    //记录点击
                    if (ApplicationPrams.loginCallBackBean != null) {
                        MyLog.i("执行了提交后台服务器请求的请求");
                        if(!TextUtils.isEmpty(webViewBean.getUrl())){
                            bannerBean.setBannerId(dataModel.getNewHomeBannerBean().getBanners().get(position1).getId());
                            bannerBean.setUserId(ApplicationPrams.loginCallBackBean.getId());
                            bannerBean.setUrl(dataModel.getNewHomeBannerBean().getBanners().get(position1).getUrl());
                         //   recordBean.setUserId(ApplicationPrams.loginCallBackBean.getId());
                            EventBus.getDefault().post(bannerBean);
                        }
                    }
                    go(view, position1, webViewBean);
                });
                MyLog.i("banner item icon: " + item);
                //加载图片，如gide
                Glide.with(mContext).load(item).into(view);
            });
        }


    }

    class MenuHolder extends BaseMulViewHolder<HomeFRMenuHolder> {
        private  LoanFraTypeBean loanFraTypeBean = new LoanFraTypeBean();
        @BindView(R.id.recv_menu)
        RecyclerView recyclerView;

        @BindView(R.id.tx_ps)
        TextView tx;


        private HomeMenuRevAdapter homeMenuRevAdapter;

        public MenuHolder(View itemView) {
            super(itemView);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            homeMenuRevAdapter = new HomeMenuRevAdapter(mContext, null);

            recyclerView.setAdapter(homeMenuRevAdapter);

        }

        @Override
        public void bindData(HomeFRMenuHolder dataModel, int position) {
            homeMenuRevAdapter.setMulDataModelList(dataModel.getLoanCategoriesBean());
            homeMenuRevAdapter.notifyDataSetChanged();
            homeMenuRevAdapter.setOnItemClickListener((view, position1) -> {
                MyLog.i("MenuHolder id: " + dataModel.getLoanCategoriesBean().get(position1).getId());

                loanFraTypeBean.setId(dataModel.getLoanCategoriesBean().get(position1).getId());
                loanFraTypeBean.setName(dataModel.getLoanCategoriesBean().get(position1).getName());
                go(view, position1, loanFraTypeBean);
            });
        }
    }

    class BodyHolder extends BaseMulViewHolder<HomeFRBodyHolder> {

        @BindView(R.id.recv_body)
        RecyclerView recyclerView;

        public BodyHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(manager);
        }

        @Override
        public void bindData(HomeFRBodyHolder dataModel, int position) {
            MyLog.i("BodyHolder new");
            HomeBodyRevAdapter homeBodyRevAdapter = new HomeBodyRevAdapter(mContext, dataModel.getHomeBodyBeanList());
            homeBodyRevAdapter.setOnItemClickListener((view, position1) -> {
                webViewBean.setUrl(dataModel.getHomeBodyBeanList().get(position1).getUrl());
                webViewBean.setTag(null);

                //这里出发了两次
                if (ApplicationPrams.loginCallBackBean != null) {
                    MyLog.i("执行了提交后台服务器请求的请求: " + position1);
                    recordBean.setLoanProductId(dataModel.getHomeBodyBeanList().get(position1).getId());
                    recordBean.setUserId(ApplicationPrams.loginCallBackBean.getId());
                    EventBus.getDefault().post(recordBean);
                }
                go(view, position1, webViewBean);

            });
            //  recyclerView.addItemDecoration(new SpaceItemDecoration(20,20,1));
            recyclerView.setAdapter(homeBodyRevAdapter);
        }
    }

    class BodyHolderFH extends BaseMulViewHolder<HomeFRBodyHolderFH> {

        @BindView(R.id.recv_body)
        RecyclerView recyclerView;

        public BodyHolderFH(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(manager);
        }

        @Override
        public void bindData(HomeFRBodyHolderFH dataModel, int position) {
            MyLog.i("BodyHolder new");
            HomeBodyHFRevAdapter homeBodyRevAdapter = new HomeBodyHFRevAdapter(mContext, dataModel.getHomeBodyBeanList());
            homeBodyRevAdapter.setOnItemClickListener((view, position1) -> {
                webViewBean.setUrl(dataModel.getHomeBodyBeanList().get(position1).getUrl());
                webViewBean.setTag(null);

                //这里出发了两次
                if (ApplicationPrams.loginCallBackBean != null) {
                    MyLog.i("执行了提交后台服务器请求的请求: " + position1);
                    recordBean.setLoanProductId(dataModel.getHomeBodyBeanList().get(position1).getId());
                    recordBean.setUserId(ApplicationPrams.loginCallBackBean.getId());
                    EventBus.getDefault().post(recordBean);
                }
                go(view, position1, webViewBean);

            });
            //  recyclerView.addItemDecoration(new SpaceItemDecoration(20,20,1));
            recyclerView.setAdapter(homeBodyRevAdapter);
        }


    }

    public <T> void go(View view, int pos, T object) {
        if (Utils.isFastClick()) {
            if (ApplicationPrams.loginCallBackBean == null) {
                MyLog.i("点击了go");
                EventBus.getDefault().post(false);
                return;
            }


            //进行页面跳转
            if (object instanceof WebViewBean) {
                MyLog.i("触发了条阻焊");
                if(!TextUtils.isEmpty(((WebViewBean) object).getUrl()))
                EventBus.getDefault().post(((WebViewBean) object));
            }

            if (object instanceof LoanFraTypeBean) {
                EventBus.getDefault().post(((LoanFraTypeBean) object));
            }
        }
    }


}
