package com.linglong.huodai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linglong.baselib.base.BaseMvpActivity;
import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.utils.CustomToast;
import com.linglong.baselib.utils.IdCardUtil;
import com.linglong.baselib.utils.StatusBarUtil;
import com.linglong.huodai.mvp.model.postbean.RecomBean;
import com.linglong.huodai.mvp.model.postbean.WebViewBean;
import com.linglong.huodai.mvp.presenters.RecomMinePresenter;
import com.linglong.huodai.mvp.view.RecomMineImpl;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecomMineActivity extends BaseMvpActivity<RecomMineImpl, RecomMinePresenter> implements RecomMineImpl {

    private RecomBean recomBean;

    @BindView(R.id.edit_phone_number)
    EditText edPhomeNum;

    @BindView(R.id.edit_name)
    EditText edName;

    @BindView(R.id.edit_whonum)
    EditText edWhoName;

    @BindView(R.id.tx_title)
    TextView tx;

    @BindView(R.id.img_back)
    ImageView back;

    @BindView(R.id.isAgree)
    CheckBox checkAgree;

    private SharedPreferences preferences;

    private boolean hasLogin;

    private String phone;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recommand_mine;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

        //这个就是设施沉浸式状态栏的主要方法了
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        //黄油刀
        ButterKnife.bind(this);

        //拿到了intent，然后拿到值存起来准备用来下一步
        recomBean = new RecomBean();
        Intent intent = getIntent();
        int[] moneys = intent.getIntArrayExtra("moneys");
        int[] dates = intent.getIntArrayExtra("dates");
        recomBean.setMoneyMin(moneys[0]);
        recomBean.setMoneyMax(moneys[1]);
        recomBean.setDateMin(dates[0]);
        recomBean.setDateMax(dates[1]);

        //拿到shareperf
        preferences = getSharedPreferences("cache", MODE_PRIVATE);

        //拿到本地存储的phone
        phone = preferences.getString("phone", null);

        //如果phone不为空，就证明以前提交过注册申请
        if (!TextUtils.isEmpty(phone) || ApplicationPrams.loginCallBackBean != null) {
            //万一用户后来改了号码登录，这里以最新登录的为主
            edPhomeNum.setText(ApplicationPrams.loginCallBackBean.getPhone());
            edPhomeNum.setEnabled(false);
        }
        String name = preferences.getString("name", null);
        String whoName = preferences.getString("whoName", null);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(whoName)) {
            if (ApplicationPrams.loginCallBackBean != null && !ApplicationPrams.loginCallBackBean.getPhone().equals(phone)) {
                edName.setEnabled(true);
                edWhoName.setEnabled(true);
            } else {
                //这个时候填上到editText，并且editText变为不可填写
                edName.setText(name);
                edWhoName.setText(whoName);
                edName.setEnabled(false);
                edWhoName.setEnabled(false);
            }

            hasLogin = true;
        }

        if (!ApplicationPrams.loginCallBackBean.getPhone().equals(phone)) {
            edName.setEnabled(true);
            edWhoName.setEnabled(true);
        }


        //设置头部
        tx.setText(getString(R.string.recommand_title));
        back.setVisibility(View.VISIBLE);
    }


    @Override
    protected RecomMinePresenter createPresenter() {
        return new RecomMinePresenter();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        CustomToast.showToast(getApplicationContext(),  msg, 2000, Gravity.CENTER);
    }

    @OnClick({R.id.btn_mine_next, R.id.img_back, R.id.tx_mine_service})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_mine_next:
                if(!checkAgree.isChecked()){
                    showError("未同意用户协议，无法智能推荐！");
                    return;
                }
                String phoneNumber = edPhomeNum.getText().toString().trim();
                String name = edName.getText().toString().trim();
                String whoName = edWhoName.getText().toString();
                //确认登录后，确认信息是否填写
                if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(whoName)) {
                    if (IdCardUtil.isValidatedAllIdcard(whoName)) {
                        //是身份证，进行提交身份认证申请
                        if (hasLogin) {
                            nextStep();
                        } else {
                            mPresenter.nextStep(ApplicationPrams.loginCallBackBean.getId(), phoneNumber, name, whoName);
                        }
                    } else {
                        showError("请填写正确的身份证号");
                    }
                } else {
                    showError("请填写姓名和身份证号");
                }

                //先提交身份注册按钮
                break;
            case R.id.tx_mine_service:
                WebViewBean webViewBean = new WebViewBean();
                webViewBean.setUrl(HttpConstant.MINE_BASE_URL +"treaty.html");
                webViewBean.setTag(getResources().getString(R.string.serice_content));
                EventBus.getDefault().post(webViewBean);
                break;
        }
    }

    @Override
    public void nextStep() {
        //这里要保存信息，下次就不用填写了
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone", edPhomeNum.getText().toString().trim());
        editor.putString("name", edName.getText().toString().trim());
        editor.putString("whoName", edWhoName.getText().toString().trim());
        editor.apply();
        editor.commit();
        //跳转到筛选完的页面
        Intent intent = new Intent(this, FilterActivity.class);

        int[] moneys = new int[2];
        int[] dates = new int[2];
        moneys[0] = recomBean.getMoneyMin();
        moneys[1] = recomBean.getMoneyMax();
        dates[0] = recomBean.getDateMin();
        dates[1] = recomBean.getDateMax();

        intent.putExtra("moneys", moneys);
        intent.putExtra("dates", dates);

        startActivity(intent);
        finish();
    }

}
