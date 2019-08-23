package com.dudu.huodai.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.ApplicationPrams;
import com.dudu.huodai.LabelActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.SpecialActivity;
import com.dudu.huodai.StoryActivity;
import com.dudu.huodai.mvp.model.HomeFRAdvertHolder;
import com.dudu.huodai.mvp.model.HomeFRBannerHolder;
import com.dudu.huodai.mvp.model.HomeFRBigBackHoder;
import com.dudu.huodai.mvp.model.HomeFRBodyHolder;
import com.dudu.huodai.mvp.model.HomeFRBodyHolderFH;
import com.dudu.huodai.mvp.model.HomeFRMenuHolder;
import com.dudu.huodai.mvp.model.postbean.BannerBean;
import com.dudu.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.dudu.huodai.mvp.model.postbean.RecordBean;
import com.dudu.huodai.mvp.model.postbean.WebViewBean;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.huodai.ui.adapter.base.BaseMulViewHolder;
import com.dudu.huodai.widget.MoneyDialog;
import com.dudu.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.LoopViewPager;
import com.dudu.model.bean.NewHomeBannerBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragRevAdapyer extends RecyclerView.Adapter<BaseMulViewHolder> {

    List<BaseMulDataModel> modelList;
    private static final int BANNER = 0;//头部
    private static final int MENU = 1;//主题
    private static final int BODY = 2;//正文最多的内容
    private static final int HISTORY = 3;//历史
    private static final int ADVERT=4;//广告
    private static final int BIGBACK=5;//大标题

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
                bannerHolder = new BannerHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_banner, viewGroup, false));
                return bannerHolder;
            case MENU:
                return new MenuHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.common_body_recy, viewGroup, false));
            case BODY:
                return new BodyHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.common_body_recy, viewGroup, false));
            case HISTORY:
                return new BodyHolderFH(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_recy_body, null));
            case ADVERT:
                return new AdvertHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.advert, viewGroup, false));
            case BIGBACK:
                return new BigBackHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_home_bigback, viewGroup, false));
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
        } else if (modelList.get(position) instanceof HomeFRBodyHolder) {
            return BODY;
        } else if(modelList.get(position) instanceof HomeFRAdvertHolder){
            return ADVERT;
        } else if(modelList.get(position) instanceof HomeFRBigBackHoder){
            return BIGBACK;
        }else {
            return HISTORY;
        }
    }

    private RecordBean recordBean = new RecordBean();
    private BannerBean bannerBean = new BannerBean();

    private boolean isBannerStart;//防止定时器多发

    class BannerHolder extends BaseMulViewHolder<HomeFRBannerHolder> {
        @BindView(R.id.banner_recyclerview)
        RecyclerView recyclerView;

        //这个到时候肯定从网络那边获取的
        private String[] iconNames = {"睡前故事", "童话故事", "成语故事", "故事大全", "签到赚钱", "接龙赚钱", "答题赚钱", "每天抽奖"};
        private int[] icons = {R.mipmap.bedtimestory, R.mipmap.fairytales, R.mipmap.idiomstory, R.mipmap.storycomplete,
                R.mipmap.checkmoney, R.mipmap.makemoney, R.mipmap.answeringmoney, R.mipmap.drawlottery};
        private HomeBannerRevAdapter homeBannerRevAdapter;


        public BannerHolder(View itemView) {
            super(itemView);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
            homeBannerRevAdapter=new HomeBannerRevAdapter(mContext,iconNames,icons);
            recyclerView.setAdapter(homeBannerRevAdapter);
        }

        @Override
        public void bindData(HomeFRBannerHolder dataModel, int position) {
            //因为不是网上获取。所以暂时不处理
            homeBannerRevAdapter.setOnItemClickListener((view, position1) -> {
                MyLog.i("标签选中: "+position1);
                Intent intent=new Intent(mContext, LabelActivity.class);
                mContext.startActivity(intent);
            });
        }


    }

    class MenuHolder extends BaseMulViewHolder<HomeFRMenuHolder> {
        private LoanFraTypeBean loanFraTypeBean = new LoanFraTypeBean();
        @BindView(R.id.recv_menu)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;


        private HomeMenuRevAdapter homeMenuRevAdapter;

        public MenuHolder(View itemView) {
            super(itemView);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(manager);
            homeMenuRevAdapter = new HomeMenuRevAdapter(mContext, null);

            recyclerView.setAdapter(homeMenuRevAdapter);
            txTitle.setText(mContext.getResources().getString(R.string.recommendedtopics));
        }

        @Override
        public void bindData(HomeFRMenuHolder dataModel, int position) {
            homeMenuRevAdapter.setMulDataModelList(dataModel.getLoanCategoriesBean());
            homeMenuRevAdapter.notifyDataSetChanged();
            homeMenuRevAdapter.setOnItemClickListener((view, position1) -> {
                MyLog.i("MenuHolder id: " + dataModel.getLoanCategoriesBean().get(position1).getId());

             /*   loanFraTypeBean.setId(dataModel.getLoanCategoriesBean().get(position1).getId());
                loanFraTypeBean.setName(dataModel.getLoanCategoriesBean().get(position1).getName());
                go(view, position1, loanFraTypeBean);*/

             Intent intent=new Intent(mContext, SpecialActivity.class);
             mContext.startActivity(intent);
            });
        }
    }

    class BodyHolder extends BaseMulViewHolder<HomeFRBodyHolder> {

        @BindView(R.id.recv_menu)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;
        public BodyHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(manager);
            txTitle.setText(mContext.getResources().getString(R.string.recommendationstory));
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

                Intent intent=new Intent(mContext, StoryActivity.class);
                mContext.startActivity(intent);

            });
            //  recyclerView.addItemDecoration(new SpaceItemDecoration(20,20,1));
            recyclerView.setAdapter(homeBodyRevAdapter);
        }
    }

    class AdvertHolder extends BaseMulViewHolder<HomeFRAdvertHolder>{
        @BindView(R.id.advert_loopviewpager)
        LoopViewPager loopViewPager;

        //http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg
        private List<String> dataList;
        public AdvertHolder(View itemView) {
            super(itemView);
            dataList=new ArrayList<>();
            dataList.add("http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg");
        }

        @Override
        public void bindData(HomeFRAdvertHolder dataModel, int position) {


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
                    //记录点击
                    if (ApplicationPrams.loginCallBackBean != null) {
                        MyLog.i("执行了提交后台服务器请求的请求");
                    }
                });
                MyLog.i("banner item icon: " + item);
                //加载图片，如gide
                Glide.with(mContext).load(item).into(view);
            });
        }
    }

    class BigBackHolder extends BaseMulViewHolder<HomeFRBigBackHoder>{


        Button btnBigshare;

        public BigBackHolder(View itemView) {
            super(itemView);
            btnBigshare= itemView.findViewById(R.id.button_bigshare);
            btnBigshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyLog.i("点击了大的分享了");
                    MoneyDialog.Builder(mContext).setMessage("感谢分享").setTitle("+45").build().shown();
                }
            });
        }

        @Override
        public void bindData(HomeFRBigBackHoder dataModel, int position) {

        }
    }

    class BodyHolderFH extends BaseMulViewHolder<HomeFRBodyHolderFH> {

        @BindView(R.id.recv_body)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;

        public BodyHolderFH(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(manager);
            txTitle.setText(mContext.getResources().getString(R.string.recommendationstory));
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
      /*  if (Utils.isFastClick()) {
            if (ApplicationPrams.loginCallBackBean == null) {
                MyLog.i("点击了go");
                EventBus.getDefault().post(false);
                return;
            }


            //进行页面跳转
            if (object instanceof WebViewBean) {
                MyLog.i("触发了条阻焊");
                if (!TextUtils.isEmpty(((WebViewBean) object).getUrl()))
                    EventBus.getDefault().post(((WebViewBean) object));
            }

            if (object instanceof LoanFraTypeBean) {
                EventBus.getDefault().post(((LoanFraTypeBean) object));
            }
        }*/
    }


}
