package com.dudu.huodai.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
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
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.AllStoryActivity;
import com.dudu.huodai.ApplicationPrams;
import com.dudu.huodai.GameCaiActivity;
import com.dudu.huodai.GameCircleActivity;
import com.dudu.huodai.GameSmashEggActivity;
import com.dudu.huodai.GameTreeActivity;
import com.dudu.huodai.LabelActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.SpecialActivity;
import com.dudu.huodai.StoryActivity;
import com.dudu.huodai.mvp.model.DDHomeFRBodyHolder;
import com.dudu.huodai.mvp.model.DDHomeFRMenuHolder;
import com.dudu.huodai.mvp.model.HomeFRAdvertHolder;
import com.dudu.huodai.mvp.model.HomeFRBannerHolder;
import com.dudu.huodai.mvp.model.HomeFRBigBackHoder;
import com.dudu.huodai.mvp.model.HomeFRBodyHolderFH;
import com.dudu.huodai.mvp.model.postbean.BannerBean;
import com.dudu.huodai.mvp.model.postbean.RecordBean;
import com.dudu.huodai.mvp.model.postbean.WebViewBean;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.huodai.ui.adapter.base.BaseMulViewHolder;
import com.dudu.huodai.widget.SharePopWindow;
import com.dudu.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.LoopViewPager;

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
    private static final int ADVERT = 4;//广告
    private static final int BIGBACK = 5;//大标题


    private Activity mContext;
    private WebViewBean webViewBean;//EventBus进行WebActivity页面跳转的实体类
    private BannerHolder bannerHolder;
    private int ActivityType;//判断是哪个activity

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
        } else if (modelList.get(position) instanceof DDHomeFRMenuHolder) {
            return MENU;
        } else if (modelList.get(position) instanceof DDHomeFRBodyHolder) {
            return BODY;
        } else if (modelList.get(position) instanceof HomeFRAdvertHolder) {
            return ADVERT;
        } else if (modelList.get(position) instanceof HomeFRBigBackHoder) {
            return BIGBACK;
        } else {
            return HISTORY;
        }
    }

    private RecordBean recordBean = new RecordBean();
    private BannerBean bannerBean = new BannerBean();

    private boolean isBannerStart;//防止定时器多发

    //type=1 SpecialActivity 专题页面的调用
    public void setActivityTheme(int type) {
        this.ActivityType = type;
    }

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
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            homeBannerRevAdapter = new HomeBannerRevAdapter(mContext, iconNames, icons);
            recyclerView.setAdapter(homeBannerRevAdapter);
        }

        @Override
        public void bindData(HomeFRBannerHolder dataModel, int position) {
            //因为不是网上获取。所以暂时不处理
            homeBannerRevAdapter.setOnItemClickListener((view, position1) -> {
                MyLog.i("标签选中: " + position1);
                if (position1 != 3) {
                    if (position1 > 3) {
                        switch (position1) {
                            case 4:
                                Intent gameTree = new Intent(mContext, GameTreeActivity.class);
                                mContext.startActivity(gameTree);
                                break;
                            case 5:
                                Intent gameSmash = new Intent(mContext, GameSmashEggActivity.class);
                                mContext.startActivity(gameSmash);
                                break;
                            case 6:
                                Intent gameCai = new Intent(mContext, GameCaiActivity.class);
                                mContext.startActivity(gameCai);
                                break;
                            case 7:
                                Intent gammeCircle = new Intent(mContext, GameCircleActivity.class);
                                mContext.startActivity(gammeCircle);
                                break;
                        }
                    } else {
                        Intent intent = new Intent(mContext, LabelActivity.class);
                        switch (position1) {
                            case 0:
                                intent.putExtra(ApplicationPrams.key_id, 28);
                                break;
                            case 1:
                                intent.putExtra(ApplicationPrams.key_id, 73);
                                break;
                            case 2:
                                intent.putExtra(ApplicationPrams.key_id, 9);
                                break;
                        }
                        intent.putExtra(ApplicationPrams.key_title, iconNames[position1]);
                        mContext.startActivity(intent);
                    }

                } else {
                    Intent intent = new Intent(mContext, AllStoryActivity.class);
                    intent.putExtra(ApplicationPrams.key_id, position1);
                    intent.putExtra(ApplicationPrams.key_title, iconNames[position1]);
                    mContext.startActivity(intent);
                }

            });
        }


    }

    class MenuHolder extends BaseMulViewHolder<DDHomeFRMenuHolder> {
        @BindView(R.id.recv_menu)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;

        @BindView(R.id.common_title)
        View view;

        @BindView(R.id.bottom_inditator)
        View bottom_inditator;

        private HomeMenuRevAdapter homeMenuRevAdapter;

        public MenuHolder(View itemView) {
            super(itemView);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(manager);
            homeMenuRevAdapter = new HomeMenuRevAdapter(mContext, null);

            recyclerView.setAdapter(homeMenuRevAdapter);

            switch (ActivityType) {
                case ApplicationPrams.SpecialActivity:
                    view.setVisibility(View.GONE);
                    break;
                case ApplicationPrams.SubjctActivity:
                    view.setVisibility(View.GONE);
                    bottom_inditator.setVisibility(View.GONE);
                    break;
                case ApplicationPrams.LabelActivity:
                    txTitle.setText(mContext.getResources().getString(R.string.story_before_sleep));
                    break;
                default:
                    txTitle.setText(mContext.getResources().getString(R.string.recommendedtopics));
                    break;
            }


        }

        @Override
        public void bindData(DDHomeFRMenuHolder dataModel, int position) {
            homeMenuRevAdapter.setMulDataModelList(dataModel.getLoanCategoriesBean());
            homeMenuRevAdapter.notifyDataSetChanged();
            homeMenuRevAdapter.setOnItemClickListener((view, position1) -> {
                MyLog.i("MenuHolder id: " + dataModel.getLoanCategoriesBean().get(position1).getId());

             /*   loanFraTypeBean.setId(dataModel.getLoanCategoriesBean().get(position1).getId());
                loanFraTypeBean.setName(dataModel.getLoanCategoriesBean().get(position1).getName());
                go(view, position1, loanFraTypeBean);*/

                Intent intent = new Intent(mContext, SpecialActivity.class);
                intent.putExtra("id", dataModel.getLoanCategoriesBean().get(position1).getId());
                mContext.startActivity(intent);
            });
        }
    }

    class BodyHolder extends BaseMulViewHolder<DDHomeFRBodyHolder> {

        @BindView(R.id.recv_menu)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;

        @BindView(R.id.common_title)
        View view;

        @BindView(R.id.bottom_inditator)
        View bottom;

        public BodyHolder(View itemView) {
            super(itemView);
            bottom.setVisibility(View.GONE);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(manager);
            // txTitle.setText(mContext.getResources().getString(R.string.recommendationstory));

            switch (ActivityType) {
                case ApplicationPrams.SpecialActivity:
                case ApplicationPrams.LabelActivity:
                case ApplicationPrams.SubjctActivity:
                    view.setVisibility(View.GONE);
                    break;
                default:
                    txTitle.setText(mContext.getResources().getString(R.string.recommendationstory));
                    break;
            }
        }

        @Override
        public void bindData(DDHomeFRBodyHolder dataModel, int position) {
            MyLog.i("BodyHolder new");
            HomeBodyRevAdapter homeBodyRevAdapter = new HomeBodyRevAdapter(mContext, dataModel.getHomeBodyBeanList());
            homeBodyRevAdapter.setOnItemClickListener((view, position1) -> {

                //这里出发了两次
                if (ApplicationPrams.loginCallBackBean != null) {
                    MyLog.i("执行了提交后台服务器请求的请求: " + position1);
                    recordBean.setLoanProductId(dataModel.getHomeBodyBeanList().get(position1).getId());
                    recordBean.setUserId(ApplicationPrams.loginCallBackBean.getId());
                    EventBus.getDefault().post(recordBean);
                }


                Intent intent = new Intent(mContext, StoryActivity.class);
                intent.putExtra(ApplicationPrams.key_id, dataModel.getHomeBodyBeanList().get(position1).getId());
                intent.putExtra(ApplicationPrams.key_title, dataModel.getHomeBodyBeanList().get(position1).getTitle());
                mContext.startActivity(intent);

            });
            //  recyclerView.addItemDecoration(new SpaceItemDecoration(20,20,1));
            recyclerView.setAdapter(homeBodyRevAdapter);
        }
    }

    class AdvertHolder extends BaseMulViewHolder<HomeFRAdvertHolder> {
        @BindView(R.id.advert_loopviewpager)
        LoopViewPager loopViewPager;

        //http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg
        private List<String> dataList;

        @BindView(R.id.view_indirator)
        View view_indirator;

        @BindView(R.id.little_view_indirator)
        View view_little_indirator;

        public AdvertHolder(View itemView) {
            super(itemView);
            dataList = new ArrayList<>();
            dataList.add("http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg");
            if(ActivityType==ApplicationPrams.SpecialActivity){
                view_indirator.setVisibility(View.GONE);
                view_little_indirator.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void bindData(HomeFRAdvertHolder dataModel, int position) {


            if (dataList.size() > 1) {
                loopViewPager.showIndicator(true);
                if (!isBannerStart) {
                    loopViewPager.startBanner();
                } else {
                    loopViewPager.setCurrentItem(0);
                }
                isBannerStart = true;
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

    class BigBackHolder extends BaseMulViewHolder<HomeFRBigBackHoder> {


        Button btnBigshare;
        ImageView specialBigicon;

        public BigBackHolder(View itemView) {
            super(itemView);
            btnBigshare = itemView.findViewById(R.id.button_bigshare);
            btnBigshare.setVisibility(View.GONE);

            btnBigshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharePopWindow sharePopWindow = new SharePopWindow(mContext);
                    sharePopWindow.showAtLocation(itemView.findViewById(R.id.fra_home_bigback_parent), Gravity.BOTTOM, 0, 0);
                    // GameWinDialog.Builder(mContext).setMessage("感谢分享").setTitle("+45").build().shown();
                }
            });
            specialBigicon = itemView.findViewById(R.id.special_bigicon);
        }

        @Override
        public void bindData(HomeFRBigBackHoder dataModel, int position) {
            Glide.with(mContext).load(dataModel.getBigIcon()).into(specialBigicon);
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
