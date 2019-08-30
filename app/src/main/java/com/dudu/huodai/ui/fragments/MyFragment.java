package com.dudu.huodai.ui.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.base.BaseMVPFragment;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.ApplicationPrams;
import com.dudu.huodai.HistoryActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.base.BaseTitleFragment;
import com.dudu.huodai.mvp.model.MyFRFunctionHolder;
import com.dudu.huodai.mvp.model.postbean.WebViewBean;
import com.dudu.huodai.mvp.presenters.MyFrgPresenter;
import com.dudu.huodai.mvp.view.MyViewImpl;
import com.dudu.huodai.ui.adapter.MyRevAdapter;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.huodai.ui.adapter.decoration.SpaceItemDecoration;
import com.dudu.huodai.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseTitleFragment<MyViewImpl, MyFrgPresenter> implements MyViewImpl {


    private WebViewBean webViewBean;

    private int[] name = {R.string.like, R.string.message, R.string.service};
    private int[] icon = {R.mipmap.like, R.mipmap.message, R.mipmap.service};
    private String[] urls = {"0", "help.html", "about.html", "treaty.html"};

    @BindView(R.id.recv_my)
    RecyclerView mRecyclerView;

    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;

    @BindView(R.id.nologin_layout)
    LinearLayout noLoginLayout;


    public static MyFragment newInstance(String info) {
        MyFragment fragment = new MyFragment();
        fragment.setTitle(info);
        return fragment;
    }


    @Override
    protected MyFrgPresenter createPresenter() {
        return new MyFrgPresenter();
    }


    @Override
    protected int getBodyLayoutRes() {
        return R.layout.fra_my;
    }

    @Override
    protected boolean hasBackHome() {
        return false;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        setTitleText(R.string.my);
        webViewBean = new WebViewBean();
      /*  txTitle.setTypeface(ApplicationPrams.typeface);
        txUsername.setTypeface(ApplicationPrams.typeface);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<BaseMulDataModel> myFRFunctionHolders = new ArrayList<>();
        for (int i = 0; i < icon.length; i++) {
            MyFRFunctionHolder myFRFunctionHolder = new MyFRFunctionHolder();
            myFRFunctionHolder.setF_icon(icon[i]);
            myFRFunctionHolder.setUrl(HttpConstant.MINE_BASE_URL + urls[i]);
            myFRFunctionHolder.setF_name(getResources().getString(name[i]));
            myFRFunctionHolders.add(myFRFunctionHolder);
        }
        MyRevAdapter myRevAdapter = new MyRevAdapter(myFRFunctionHolders, getContext());
        myRevAdapter.setOnItemClickListener((view, position) -> {
            //检测是否登录
            if (ApplicationPrams.loginCallBackBean == null) {
                EventBus.getDefault().post(false);
                return;
            }
            if (position == 0) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            } else {
                webViewBean.setUrl(((MyFRFunctionHolder) myFRFunctionHolders.get(position)).getUrl());
                webViewBean.setTag(getString(name[position]));
                EventBus.getDefault().post(webViewBean);
            }
        });
        mRecyclerView.setAdapter(myRevAdapter);

        if (ApplicationPrams.isLogin) {
            loginSuceesee();
        }
    }

    @Override
    protected void initBefore() {
        super.initBefore();
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


    private void noLogin() {
        ApplicationPrams.isLogin = false;

    }


    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public void loginSuceesee() {

    }

    @OnClick({R.id.btn_wx_login, R.id.tx_try})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wx_login:
            case R.id.tx_try:
                loginLayout.setVisibility(View.VISIBLE);
                noLoginLayout.setVisibility(View.GONE);
                break;
        }
    }
}
