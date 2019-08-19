package com.linglong.huodai.ui.fragments;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.baselib.base.BaseMVPFragment;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.utils.CustomToast;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.Utils;
import com.linglong.huodai.R;
import com.linglong.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.linglong.huodai.mvp.model.postbean.LoanMoneyBean;
import com.linglong.huodai.mvp.presenters.LoanFrgPresenter;
import com.linglong.huodai.mvp.view.LoanFrgViewImpl;
import com.linglong.huodai.ui.adapter.HomeFragRevAdapyer;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.huodai.widget.LoanFraPopWindow;
import com.linglong.model.bean.NewHomeMenuBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LoanFragment extends BaseMVPFragment<LoanFrgViewImpl, LoanFrgPresenter> implements LoanFrgViewImpl {

    @BindView(R.id.recv_loan)
    RecyclerView mRecyclerView;

    @BindView(R.id.tx_refrsh)
    TextView refreshTx;

    @BindView(R.id.noOnline)
    RelativeLayout relativeLayout;

    @BindView(R.id.tx_title)
    TextView txTitle;

    @BindView(R.id.spinner_layout)
    LinearLayout layout;

    @BindView(R.id.spinner_type_text)
    TextView bannerFirst;

    @BindView(R.id.spinner_loannum_text)
    TextView bannerSecond;


    @BindView(R.id.spinner_type_img)
    CheckBox bannerCheckType;

    @BindView(R.id.spinner_loannum_img)
    CheckBox bannerCheckMoney;

    @BindView(R.id.tx_all)
    TextView txAll;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    //商品查询的条件
    private int typeId = 0;
    private int moneyMaxLit = 0;
    private int monetLitmitHigh = 0;

    //body的当前刷新页面
    private int currentPage = 1;

    private List<BaseMulDataModel> list;
    private HomeFragRevAdapyer fragRevAdapyer;
    private List<BaseMulDataModel> baseMulDataModels;
    private LoanFraPopWindow mPopWindow;


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

    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    //过滤类型所做的操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cFliterMoney(LoanMoneyBean loanMoneyBean) {
        closeOpen();
        bannerSecond.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));

        //把checkbox设置为false
        bannerCheckMoney.setChecked(false);
        //标记当前内容在当前总页数是第几页
        currentPage = 1;
        //获取最大最小值区间
        monetLitmitHigh = loanMoneyBean.getLimit();
        moneyMaxLit = loanMoneyBean.getMax();
        //判断是否是点击了全部的选项，是的话最大最小值无效，并且重新刷新网页
        if (loanMoneyBean.getName().trim().equals(getString(R.string.loan_all))) {
            //moneyMaxLit,monetLitmitHigh
            moneyMaxLit = 0;
            monetLitmitHigh = 0;
            mPresenter.requestBody(typeId);
        }
        mPresenter.requestBody(typeId, monetLitmitHigh, moneyMaxLit);

        //触文字颜色改变时间
        changeItemNameAndColor(LoanFraPopWindow.LOAN, loanMoneyBean.getName());

        //修改字体颜色

    }

    private void changeItemNameAndColor(int type, String name) {
        switch (type) {
            case LoanFraPopWindow.TYPE:
                //把选中选项的文本显示在当前的选项里展示
                bannerFirst.setText(name);
                break;
            case LoanFraPopWindow.LOAN:
                //把选中选项的文本显示在当前的选项里展示
                bannerSecond.setText(name);
                break;
        }
    }


    //过滤金额所作的操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cFliterType(LoanFraTypeBean loanFraTypeBean) {
        closeOpen();
        bannerFirst.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        // 把当前选项的checkbox设置为false
        bannerCheckType.setChecked(false);
        //标记当前内容在当前总页数是第几页
        currentPage = 1;
        //通过ID，选中类型，发起类型请求
        typeId = loanFraTypeBean.getId();
        MyLog.i("我接受到了eventbus: " + loanFraTypeBean.toString());
        //这里请求body数据
        mPresenter.requestBody(typeId, monetLitmitHigh, moneyMaxLit);

        //更新选项item
        bannerFirst.setText(loanFraTypeBean.getName());
        //然后留出一个全局的id，方便做和money做联合查询
        typeId = loanFraTypeBean.getId();

        changeItemNameAndColor(LoanFraPopWindow.TYPE, loanFraTypeBean.getName());

    }


    @Override
    protected void initView() {

        //初始化选项卡弹窗
        mPopWindow = new LoanFraPopWindow(getContext());
        mPopWindow.setCancelListener(new LoanFraPopWindow.CancelListener() {
            @Override
            public void cancel() {
                closeOpen();

                bannerCheckType.setChecked(false);
                bannerCheckMoney.setChecked(false);

                bannerSecond.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                bannerFirst.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
            }
        });

        //设置弹窗的触摸事件
        mPopWindow.setTouchInterceptor((view, motionEvent) -> {
            int[] xys = mPopWindow.getAnchorLoc();
            //因为只能在下部分的半透明区隐藏，所以价格判断限制
            if (xys[1] != 0 && motionEvent.getRawY() >= (xys[1] + layout.getHeight())) {
                return false;
            }
            //然后这里设置的是，因为不能让popwindow消失，所以在可隐藏区域外要拦截点击事件
            //然后再通过区域判断是否在按钮内来更新数据
            Point point = new Point();
            point.set((int) motionEvent.getRawX(), (int) motionEvent.getRawY());


            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                MyLog.i("来到了触摸区域");

                //点击到类型区域
                if (Utils.isContaint(bannerFirst, point) || Utils.isContaint(bannerCheckType, point)) {
                    MyLog.i("bannerCheckType.isChecked(): " + bannerCheckType.isChecked());
                    selectItem(LoanFraPopWindow.TYPE);
                }

                //点击到金额区域
                if (Utils.isContaint(bannerSecond, point) || Utils.isContaint(bannerCheckMoney, point)) {
                    MyLog.i("bannerCheckMoney.isChecked(): " + bannerCheckMoney.isChecked());
                    selectItem(LoanFraPopWindow.LOAN);
                }

                //点击外部阴影区域
                if (Utils.isContaint(txAll, point)) {
                    clearAll();
                }
            }


            return false;
        });
        //不把它放到懒加载
        mPresenter.requestBody(0);

        //设置商品选项卡列表的
        txTitle.setText(getResources().getString(R.string.loan_num));
        baseMulDataModels = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        fragRevAdapyer = new HomeFragRevAdapyer(getActivity(), baseMulDataModels);
        // fragRevAdapyer.setHasStableIds(true);
        mRecyclerView.setAdapter(fragRevAdapyer);

        //刷新按钮的刷新是按
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            MyLog.i("我触发了上拉刷新");
            mPresenter.requestBody(typeId, monetLitmitHigh, moneyMaxLit);
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了上拉加载更多");
            currentPage++;
            mPresenter.requestBodyPage(typeId, monetLitmitHigh, moneyMaxLit, currentPage);
        });
    }

    private void animOpen(View view) {
        //开始一个旋转动画
        //创建AninationSet 对象
        AnimationSet animationSet = new AnimationSet(true);
        //创建 RotateAnimation 对象
        RotateAnimation rotateAnimation = new RotateAnimation(0f, +180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画持续
        rotateAnimation.setDuration(150);
        //动画插入器
        rotateAnimation.setInterpolator(getActivity(), android.R.anim.linear_interpolator);
        //添加到 AnimationSet
        animationSet.addAnimation(rotateAnimation);
        //动画播放完后是否停留在最后的状态
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

    private void animClose(View view) {
        //开始一个旋转动画
        //创建AninationSet 对象
        AnimationSet animationSet = new AnimationSet(true);
        //创建 RotateAnimation 对象
        RotateAnimation rotateAnimation = new RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画持续
        rotateAnimation.setDuration(150);
        //动画插入器
        rotateAnimation.setInterpolator(getActivity(), android.R.anim.linear_interpolator);
        //添加到 AnimationSet
        animationSet.addAnimation(rotateAnimation);
        //动画播放完后是否停留在最后的状态
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        mRecyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        CustomToast.showToast(getContext().getApplicationContext(), msg, 2000);
        if (refreshLayout.isRefreshing())
            refreshLayout.finishRefresh();
        if(refreshLayout.isLoading())
            refreshLayout.finishLoadMore();
    }

    @Override
    public void refreshHome(List<BaseMulDataModel> list) {

        fragRevAdapyer.setModelList(list);
        fragRevAdapyer.notifyDataSetChanged();
        if (refreshLayout.isRefreshing())
            refreshLayout.finishRefresh();
    }

    @Override
    public void addPage(List<BaseMulDataModel> list) {
        fragRevAdapyer.notifyDataSetChanged();
        if(refreshLayout.isLoading())
        refreshLayout.finishLoadMore();
    }


    @Override
    public void refreshTypeFliter(List<NewHomeMenuBean.LoanCategoriesBean> loanCategories) {
        //这里类型筛选的item的时候了
        list = new ArrayList<>();
        LoanFraTypeBean allloanFraTypeBean = new LoanFraTypeBean();
        allloanFraTypeBean.setName(getString(R.string.loan_all));
        allloanFraTypeBean.setId(0);
        list.add(allloanFraTypeBean);
        for (NewHomeMenuBean.LoanCategoriesBean loanCategoriesBean : loanCategories) {
            LoanFraTypeBean loanFraTypeBean = new LoanFraTypeBean();
            loanFraTypeBean.setId(loanCategoriesBean.getId());
            loanFraTypeBean.setName(loanCategoriesBean.getName());
            list.add(loanFraTypeBean);
        }
        mPopWindow.selectType(LoanFraPopWindow.TYPE, list);
        if (!mPopWindow.isShowing())
            mPopWindow.showAsDropDown(layout, getContext().getApplicationContext());
    }

    //下拉选项时候的判断
    public boolean selectItem(int typeId) {
        MyLog.i("type: " + typeId);
        switch (typeId) {
            case LoanFraPopWindow.TYPE:
                if (!bannerCheckType.isChecked()) {
                    if (mPopWindow.isShowing())
                        animClose(bannerCheckMoney);
                    bannerCheckType.setChecked(true);
                    bannerCheckMoney.setChecked(false);
                    animOpen(bannerCheckType);

                } else if (mPopWindow.getCurrentItem() == LoanFraPopWindow.TYPE) {
                    bannerCheckType.setChecked(false);
                    bannerFirst.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    animClose(bannerCheckType);
                    mPopWindow.dismiss();
                }
                break;
            case LoanFraPopWindow.LOAN:
                if (!bannerCheckMoney.isChecked()) {
                    if (mPopWindow.isShowing())
                        animClose(bannerCheckType);
                    bannerCheckMoney.setChecked(true);
                    bannerCheckType.setChecked(false);
                    animOpen(bannerCheckMoney);
                } else if (mPopWindow.getCurrentItem() == LoanFraPopWindow.LOAN) {
                    bannerCheckMoney.setChecked(false);
                    bannerSecond.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    animClose(bannerCheckMoney);
                    mPopWindow.dismiss();
                }
                break;
        }
        mPopWindow.setCurrentItem(typeId);
        return false;
    }

    //点击事件
    @OnClick({R.id.tx_refrsh, R.id.spinner_type_text, R.id.spinner_loannum_text, R.id.tx_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tx_refrsh:
                File cacheFile = new File(HttpConstant.context.getCacheDir(), HttpConstant.cacheFileName);//缓存文件路径
                mPresenter.requestBody(typeId);//请求body
                if (cacheFile.exists()) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.spinner_type_text:
                selectItem(LoanFraPopWindow.TYPE);
                break;
            case R.id.spinner_loannum_text:
                selectItem(LoanFraPopWindow.LOAN);
                break;
            case R.id.tx_all:
                clearAll();
                break;
        }
    }

    public void closeOpen() {
        if (mPopWindow.isShowing()) {
            if (mPopWindow.getCurrentItem() == LoanFraPopWindow.TYPE) {
                animClose(bannerCheckType);
            } else {
                animClose(bannerCheckMoney);
            }
        }
    }

    public void clearAll() {
        closeOpen();

        //全部原本的记录预留用来联合查询的id，min,max清空,相当于全部重置了
        typeId = 0;
        monetLitmitHigh = 0;
        moneyMaxLit = 0;

        bannerFirst.setText(getString(R.string.type));
        bannerSecond.setText(getString(R.string.money));
        mPresenter.requestBody(0);

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
        bannerCheckMoney.setChecked(false);
        bannerCheckType.setChecked(false);
        bannerSecond.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        bannerFirst.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));


    }


    //选中事件
    @OnCheckedChanged({R.id.spinner_type_img, R.id.spinner_loannum_img,})
    public void onCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.spinner_type_img:
                if (ischanged) {
                    animOpen(bannerCheckType);
                    bannerFirst.setTextColor(getResources().getColor(R.color.my_login_color));
                    bannerSecond.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    if (list != null) {
                        mPopWindow.selectType(LoanFraPopWindow.TYPE, list);
                        if (!mPopWindow.isShowing())
                            mPopWindow.showAsDropDown(layout, getContext().getApplicationContext());
                    } else {
                        mPresenter.requestMenu();
                    }
                }
                break;
            case R.id.spinner_loannum_img:
                if (ischanged) {
                    animOpen(bannerCheckMoney);
                    bannerSecond.setTextColor(getResources().getColor(R.color.my_login_color));
                    bannerFirst.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    mPopWindow.selectType(LoanFraPopWindow.LOAN, null);
                    if (!mPopWindow.isShowing())
                        mPopWindow.showAsDropDown(layout, getContext().getApplicationContext());
                }
                break;


        }
    }
}
