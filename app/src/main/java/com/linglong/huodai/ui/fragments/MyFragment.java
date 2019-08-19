package com.linglong.huodai.ui.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.baselib.base.BaseMVPFragment;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.utils.Utils;
import com.linglong.huodai.ApplicationPrams;
import com.linglong.huodai.HistoryActivity;
import com.linglong.huodai.R;
import com.linglong.huodai.mvp.model.MyFRFunctionHolder;
import com.linglong.huodai.mvp.model.postbean.WebViewBean;
import com.linglong.huodai.mvp.presenters.MyFrgPresenter;
import com.linglong.huodai.mvp.view.MyViewImpl;
import com.linglong.huodai.ui.adapter.MyRevAdapter;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.huodai.ui.adapter.decoration.SpaceItemDecoration;
import com.linglong.huodai.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseMVPFragment<MyViewImpl, MyFrgPresenter> implements MyViewImpl {

    @BindView(R.id.recv_my)
    RecyclerView recyclerView;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.user_icon)
    CircleImageView imgIcon;

    @BindView(R.id.tx_username)
    TextView txUsername;

    @BindView(R.id.exit)
    TextView txExit;

    @BindView(R.id.tx_title)
    TextView txTitle;

    private WebViewBean webViewBean;

    private int[] name = {R.string.history,R.string.any_question, R.string.about_us, R.string.serice_content};
    private int[] icon = {R.mipmap.hitstory, R.drawable.group, R.drawable.about, R.drawable.fuwu};
    private String[] urls = {"0", "help.html", "about.html", "treaty.html"};

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
    protected int getLayoutRes() {
        return R.layout.fra_my;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        webViewBean = new WebViewBean();
      /*  txTitle.setTypeface(ApplicationPrams.typeface);
        txUsername.setTypeface(ApplicationPrams.typeface);*/

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(2));
        List<BaseMulDataModel> myFRFunctionHolders = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
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
        recyclerView.setAdapter(myRevAdapter);

        if (ApplicationPrams.isLogin) {
            loginSuceesee();
        }
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

    @OnClick({R.id.btn_login, R.id.exit})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_login:
                if (Utils.isFastClick())
                    EventBus.getDefault().post(false);
              /*  intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);*/
                break;
            case R.id.exit:
                noLogin();
                EventBus.getDefault().post(false);
                ApplicationPrams.loginCallBackBean = null;
                break;
        }

    }

    private void noLogin() {
        ApplicationPrams.isLogin = false;
        btnLogin.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.GONE);
        txUsername.setVisibility(View.GONE);
        txUsername.setText(ApplicationPrams.loginCallBackBean.getPhone());
        txExit.setVisibility(View.GONE);
    }


    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public void loginSuceesee() {
        btnLogin.setVisibility(View.GONE);
        imgIcon.setVisibility(View.VISIBLE);
        txUsername.setVisibility(View.VISIBLE);
        txUsername.setText(ApplicationPrams.loginCallBackBean.getPhone());
        txExit.setVisibility(View.VISIBLE);
    }
}
