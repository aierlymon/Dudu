package com.linglong.huodai;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linglong.baselib.base.BaseMvpActivity;
import com.linglong.baselib.broadcast.NetWorkStateBroadcast;
import com.linglong.baselib.utils.CustomToast;
import com.linglong.baselib.utils.StatusBarUtil;
import com.linglong.baselib.utils.Utils;
import com.linglong.huodai.mvp.presenters.LoginPresenter;
import com.linglong.huodai.mvp.view.LoginViewimpl;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginViewimpl, LoginPresenter> implements LoginViewimpl {

    @BindView(R.id.img_back)
    ImageView backIcon;

    @BindView(R.id.tx_title)
    TextView titleName;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.edit_num)
    EditText editNumber;

    @BindView(R.id.check)
    TextView txCheck;

    @BindView(R.id.edit_check)
    EditText editCheck;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarColor(getResources().getColor(R.color.my_login_color));
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        // titleName.setTypeface(ApplicationPrams.typeface);

        backIcon.setVisibility(View.VISIBLE);
        Utils.setEditTextHintSize(editCheck, getString(R.string.chek_num), 15);
        Utils.setEditTextHintSize(editNumber, getString(R.string.phone_num), 15);
        titleName.setText(getResources().getString(R.string.logintxt));
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        CustomToast.showToast(getApplicationContext(), msg, 2000);
    }

    @OnClick({R.id.btn_login, R.id.img_back, R.id.check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!NetWorkStateBroadcast.isOnline.get()) {
                    showError(getString(R.string.nonet));
                    break;
                }
                if (!mPresenter.checkTelPhoneNumber(editNumber.getText().toString()) || TextUtils.isEmpty(editCheck.getText().toString())) {
                    showError(getResources().getString(R.string.numberorcheck_error));
                    break;
                }
                if (TextUtils.isEmpty(editCheck.getText().toString())) {
                    showError("请输入验证码");
                    break;
                }
                mPresenter.requeseLogin(editNumber.getText().toString(), editCheck.getText().toString());
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.check:
                if (!NetWorkStateBroadcast.isOnline.get()) {
                    showError(getString(R.string.nonet));
                    break;
                }
                //发起验证码请求
                mPresenter.checkVerificationCode(editNumber.getText().toString());
                break;
        }
    }

    private CountDownTimer timer;


    private CountDownTimer countDownTimer;

    @Override
    public void startTime() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                txCheck.setText("还剩" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                txCheck.setText(getResources().getString(R.string.getcheck));
            }
        };
        countDownTimer.start();
    }

    @Override
    public void loginSuccess() {
        EventBus.getDefault().post(true);
        //跳转回到
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


}
