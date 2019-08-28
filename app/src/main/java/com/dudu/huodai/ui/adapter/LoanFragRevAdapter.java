package com.dudu.huodai.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.R;
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
import com.dudu.model.bean.StoryTable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LoanFragRevAdapter extends RecyclerView.Adapter<BaseMulViewHolder> {
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

    public LoanFragRevAdapter(Activity mContext, List<BaseMulDataModel> modelList) {
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
                        .inflate(R.layout.fra_loan_recy_banner, viewGroup, false));
                return bannerHolder;
            case MENU:
                return new MenuHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_loan_recy_menu, viewGroup, false));
            case BODY:
                return new BodyHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_loan_recy_body, viewGroup, false));
            case HISTORY:
                return new BodyHolderFH(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_loan_recy_body, null));
            case ADVERT:
                return new AdvertHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.fra_loan_recy_task, viewGroup, false));
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

        @BindView(R.id.my_encourage)
        TextView myEncourage;

        @BindView(R.id.btn_exchange)
        Button btnExchanger;

        @BindView(R.id.tx_my_dou)
        TextView txMydou;

        @BindView(R.id.tx_mytoday_dou)
        TextView myToDayDou;

        @BindView(R.id.tx_time_getmoney)
        TextView txTimegetmoney;

        @BindView(R.id.tx_signin)
        TextView txSignin;

        public BannerHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(HomeFRBannerHolder dataModel, int position) {

        }


    }

    class MenuHolder extends BaseMulViewHolder<DDHomeFRMenuHolder> {

        @BindView(R.id.recyclerview_loan_menu)
        RecyclerView mRecyclerview;

        @BindView(R.id.tx_title)
        TextView txTitle;

        private LoadMenuRevAdapter loadMenuRevAdapter;

        List<String> urlList;

        public MenuHolder(View itemView) {
            super(itemView);
            txTitle.setText(mContext.getResources().getString(R.string.crazy));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3);

            mRecyclerview.setLayoutManager(gridLayoutManager);
            urlList = new ArrayList<>();
            loadMenuRevAdapter = new LoadMenuRevAdapter(urlList, mContext);
            mRecyclerview.setAdapter(loadMenuRevAdapter);
        }

        @Override
        public void bindData(DDHomeFRMenuHolder dataModel, int position) {
            dataModel.getLoanCategoriesBean().get(position).getPicture();
            for (int i = 0; i < dataModel.getLoanCategoriesBean().size(); i++) {
                MyLog.i("loan MenuHolder pic: " + dataModel.getLoanCategoriesBean().get(i).getPicture());
                urlList.add(HttpConstant.PIC_BASE_URL + dataModel.getLoanCategoriesBean().get(i).getPicture());
            }
            loadMenuRevAdapter.notifyDataSetChanged();
        }
    }

    List<StoryTable> testTableList=new ArrayList<>();
    private LoanBodyRevAdapter testBodyRevAdapter;
    class BodyHolder extends BaseMulViewHolder<DDHomeFRBodyHolder> {
        @BindView(R.id.tx_title)
        TextView txTitle;

        @BindView(R.id.recyclerview_loan_body)
        RecyclerView mRecyclerview;

        private LoanBodyRevAdapter loanBodyRevAdapter;

        public BodyHolder(View itemView) {
            super(itemView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerview.setLayoutManager(linearLayoutManager);
            txTitle.setText(mContext.getResources().getString(R.string.daybenefits));
        }

        @Override
        public void bindData(DDHomeFRBodyHolder dataModel, int position) {
            testTableList=dataModel.getHomeBodyBeanList();
            loanBodyRevAdapter=new LoanBodyRevAdapter(mContext,testTableList);
            mRecyclerview.setAdapter(loanBodyRevAdapter);


            //没用的
            testBodyRevAdapter.notifyDataSetChanged();
        }
    }

    class AdvertHolder extends BaseMulViewHolder<HomeFRAdvertHolder> {

        @BindView(R.id.recyclerview_loan_task)
        RecyclerView recyclerView;

        @BindView(R.id.tx_title)
        TextView txTitle;



        public AdvertHolder(View itemView) {
            super(itemView);
            txTitle.setText(mContext.getResources().getString(R.string.daytask));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            txTitle.setText(mContext.getResources().getString(R.string.daytask));
        }

        @Override
        public void bindData(HomeFRAdvertHolder dataModel, int position) {
            testBodyRevAdapter=new LoanBodyRevAdapter(mContext, testTableList);
            recyclerView.setAdapter(testBodyRevAdapter);
        }
    }

    class BigBackHolder extends BaseMulViewHolder<HomeFRBigBackHoder> {

        public BigBackHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void bindData(HomeFRBigBackHoder dataModel, int position) {

        }
    }

    class BodyHolderFH extends BaseMulViewHolder<HomeFRBodyHolderFH> {


        public BodyHolderFH(View itemView) {
            super(itemView);


        }

        @Override
        public void bindData(HomeFRBodyHolderFH dataModel, int position) {
            MyLog.i("BodyHolder new");
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
