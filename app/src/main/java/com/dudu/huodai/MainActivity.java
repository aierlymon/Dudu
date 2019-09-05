package com.dudu.huodai;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.broadcast.NetWorkStateBroadcast;
import com.dudu.baselib.myapplication.App;
import com.dudu.baselib.utils.CustomToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.RxPermissionUtil;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.baselib.utils.UpdateUtil;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.dudu.huodai.mvp.model.postbean.WebViewBean;
import com.dudu.huodai.mvp.presenters.MainPrsenter;
import com.dudu.huodai.mvp.view.MainViewImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.ui.adapter.MainVPAdapter;
import com.dudu.huodai.ui.fragments.HomeFragment;
import com.dudu.huodai.ui.fragments.LoanFragment;
import com.dudu.huodai.ui.fragments.MyFragment;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.widget.CustomScrollViewPager;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameWinDialog;
import com.dudu.huodai.widget.MediaPlayManager;
import com.dudu.model.bean.LoginCallBackBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends BaseMvpActivity<MainViewImpl, MainPrsenter> implements MainViewImpl {

    @BindView(R.id.viewpager)
    CustomScrollViewPager mViewPager;

    @BindView(R.id.group)
    RadioGroup mGroup;

    @BindView(R.id.main_parent)
    ConstraintLayout mainParent;

    private SharedPreferences preferences;


    private String[] permissions = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
             android.Manifest.permission.REQUEST_INSTALL_PACKAGES,
             android.Manifest.permission.READ_EXTERNAL_STORAGE,
             android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
             android.Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        //StatusBarUtil.setRootViewFitsSystemWindows(this,true);

        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        //这个就是设施沉浸式状态栏的主要方法了
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        init();
    }

    private NetWorkStateBroadcast netWorkStateBroadcast;

    private void init() {


        //拿取字体
        //ApplicationPrams.typeface=Typeface.createFromAsset(getAssets(),"PingFang_Bold.ttf");

        //获取权限
        RxPermissionUtil.getInstance().permission(this, permissions);

        //注册网络检测广播
        netWorkStateBroadcast = new NetWorkStateBroadcast();
        netWorkStateBroadcast = new NetWorkStateBroadcast();
        netWorkStateBroadcast.setmOnNetStateChangListener(new NetWorkStateBroadcast.OnNetStateChangListener() {
            @Override
            public void onNetWorkSuccess() {
            }

            @Override
            public void onNetWorkFail() {
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateBroadcast, filter);


        //判断是否已经登陆过
        preferences = getSharedPreferences("cache", MODE_PRIVATE);
        String obj = preferences.getString("obj", null);
        if (!TextUtils.isEmpty(obj)) {
            Gson gson = new Gson();
            ApplicationPrams.loginCallBackBean = gson.fromJson(obj, LoginCallBackBean.UserBean.class);
            ApplicationPrams.isLogin = true;
        }


        //检查签到
        alertRegister();

        //butterknife的绑定
        ButterKnife.bind(this);
        //重新定义当前drawable的大小
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drawables = ((RadioButton) mGroup.getChildAt(i)).getCompoundDrawables();
            //获取drawables
            //给指定的radiobutton设置drawable边界
            MyLog.i("width: " + drawables[1].getMinimumWidth() * 7 / 12 + "   height: " + drawables[1].getMinimumHeight() * 2 / 3);
            Rect r = new Rect(0, 5, (int) getResources().getDimension(R.dimen.x22), (int) getResources().getDimension(R.dimen.y20));
            //定义一个Rect边界
            drawables[1].setBounds(r);
            //添加限制给控件
            ((RadioButton) mGroup.getChildAt(i)).setCompoundDrawables(null, drawables[1], null, null);
        }


        //初始化页面
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("home"));
        fragments.add(LoanFragment.newInstance("loan"));
        fragments.add(MyFragment.newInstance("mine"));
        MainVPAdapter mainVPagerAdapter = new MainVPAdapter(fragments, getSupportFragmentManager());

        mViewPager.setScrollable(false);
        mViewPager.setAdapter(mainVPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        //检查更新
        mPresenter.checkUpdate(MainActivity.this);

    }


    private AdvertUtil advertUtil;
    AdvertUtil mainadverUtil;

    private void alertRegister() {
        if (!ApplicationPrams.isLogin) {
          /*  GameNewOneDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.newone))
                    .setTitle("+90")
                    .setIconId(R.mipmap.win)
                    .setRightButtonText(getResources().getString(R.string.wx_get))
                    .setOnConfirmClickListener(new GameNewOneDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转微信登录
                        }
                    })
                    .build().shown();*/

         /*   TimeRewardDialog.Builder(this)
                    .setMessage("限时0秒领取")
                    .setOnConfirmClickListener(new TimeRewardDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转微信登录
                            Toast.makeText(MainActivity.this, "点击成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build().shown();*/

            GameWinDialog gameWinDialog = GameWinDialog.Builder(this)
                    .setMessage("欢迎回来，送您")
                    .setTitle("+80")
                    .setIconId(R.mipmap.win)
                    .setLeftButtonText("不翻倍")
                    .setRightButtonText("金豆翻倍")
                    .hasAdvert(true)
                    .isWin(true)
                    .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            mainadverUtil = new AdvertUtil(mainParent, MainActivity.this);
                            mainadverUtil.setVideoType(ApplicationPrams.MainActvity);
                            float[] WH = Utils.getScreenWH(MainActivity.this.getApplicationContext());
                            mainadverUtil.loadVideo(getmTTAdNative(), ApplicationPrams.public_qiandao_jili, (int) WH[0], (int) WH[1]);
                        }
                    })
                    .build();
            gameWinDialog.shown();

            if(Utils.lacksPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
                advertUtil = new AdvertUtil(gameWinDialog.getAdvertLayout(), this);
                advertUtil.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_qiandao_back,
                        (int) ((Utils.getScreenWH(this.getApplicationContext())[0]) * 0.9),
                        (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
            }

        } else {
       /*     GameWinDialog.Builder(this)
                    .setMessage("欢迎回来，送您")
                    .setTitle("+80")
                    .setIconId(R.mipmap.win)
                    .setLeftButtonText("不翻倍")
                    .setRightButtonText("金豆翻倍")
                    .hasAdvert(true)
                    .isWin(true)
                    .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .build().shown();*/
        }
    }


    @Override
    protected MainPrsenter createPresenter() {
        return new MainPrsenter();
    }


    @OnCheckedChanged({R.id.rb_home, R.id.rb_loan, R.id.rb_my})
    public void onCheckChange(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_home:
                if (ischanged) {
                    MyLog.i("我拿去到了颜色:触发  R.id.rb_home");
                    //   StatusBarUtil.setStatusBarDarkTheme(this,false);
                    StatusBarUtil.setTranslucentStatus(this);
                    StatusBarUtil.setStatusBarDarkTheme(this, true);
                    mViewPager.setCurrentItem(0, false);
                }
                break;
            case R.id.rb_loan:
                //展示标题栏
                if (ischanged) {
                    MyLog.i("我拿去到了颜色:触发 R.id.rb_loan");
                    //     StatusBarUtil.setStatusBarDarkTheme(this,true);
                    //    setStatusBarColor(getResources().getColor(R.color.my_login_color));
                    mViewPager.setCurrentItem(1, false);
                }
                break;

            case R.id.rb_my:
                if (ischanged) {
                    //   StatusBarUtil.setStatusBarDarkTheme(this,true);
                    MyLog.i("我拿去到了颜色:触发 R.id.rb_myn");
                    //      setStatusBarColor(getResources().getColor(R.color.my_login_color));
                    mViewPager.setCurrentItem(2, false);
                }
                break;
            default:
                break;

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
        CustomToast.showToast(getApplicationContext(), msg, 2000);
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        MyLog.i("startToAdvert mainActivity oncreate");
    }

    @Override
    protected void startToAdvert(boolean isScreenOn) {
        MyLog.i("startToAdvert mainActivity 我到了跳转加载也面对这里");
        Intent intent = new Intent(this, AdvertSplashActivity.class);
        if (isScreenOn) {
            intent.putExtra(ApplicationPrams.adverId, ApplicationPrams.public_sceenon_advertId);
        } else {
            intent.putExtra(ApplicationPrams.adverId, ApplicationPrams.public_restart_advertId);
        }
        startActivity(intent);
    }

    @Override
    protected void screenOn() {
    }

    @Override
    protected void screenOff() {

    }


    //这个事件总线是检测登录页面的操作，
    // false 跳转到登录页面，这个时候就要清空保存到本地的文件
    //true 已经登录，这个时候就要保存数据到本地文件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginstate(Boolean isState) {
        if (!isState) {
            //因为跳转到登陆。清空shareprefence
            if (!TextUtils.isEmpty(preferences.getString("obj", null))) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("obj", null);
                editor.putString("token", "");
                editor.apply();
                editor.commit();
            }

            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("isAnim", true);
            startActivity(intent);
            return;
        }

        if (ApplicationPrams.loginCallBackBean != null) {
            Gson gson = new Gson();
            String obj = gson.toJson(ApplicationPrams.loginCallBackBean);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("obj", obj);
            editor.putString("token", App.token);
            editor.apply();
            editor.commit();
        }

    }

    //WebActivity页面的跳转
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webState(WebViewBean webViewBean) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", webViewBean.getUrl());
        intent.putExtra("tag", webViewBean.getTag());
        startActivity(intent);
    }


    //这个是跳转到商品页面，并且选择筛选,以后int的时间总线专属LoanFragment
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkRadio(LoanFraTypeBean LoanFraTypeBean) {
        //选中LoanFragment页面
        ((RadioButton) mGroup.getChildAt(1)).setChecked(true);
    }

    AdvertUtil advertFanBeiUtil;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkAdverdialogBean(AdverdialogBean adverdialogBean) {
        //选中LoanFragment页面
        if (adverdialogBean.getType() == ApplicationPrams.MainActvity) {
            GameAdverBackDialog adverBackDialog = GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜您,奖励您")
                    .setTitle("+90")
                    .setIconId(R.mipmap.win)
                    .hasAdvert(true)
                    .setRightButtonText("继续")
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(advertFanBeiUtil!=null&&advertFanBeiUtil.getmTTAd()!=null){
                                advertFanBeiUtil.getmTTAd().destroy();
                            }
                        }
                    })
                    .build();
            adverBackDialog.shown();
            advertFanBeiUtil= new AdvertUtil(adverBackDialog.getAdvertLayout(), MainActivity.this);
            advertFanBeiUtil.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_qiandao_fanbei, (int) ((Utils.getScreenWH(this.getApplicationContext())[0]) * 0.9),
                    (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.i("activity isFinish " + isFinishing());
        //取消注册广播
        if (netWorkStateBroadcast != null)
            unregisterReceiver(netWorkStateBroadcast);
        //取消下载更新
        if (mPresenter != null)
            mPresenter.cancelIUpdate();

        if (advertUtil != null && advertUtil.getmTTAd() != null) {
            advertUtil.getmTTAd().destroy();
        }


        MediaPlayManager.destoryMedaiPlay();
    }

    private ProgressDialog pd;

    @Override
    public void statrUpdateProgress() {
        runOnUiThread(() -> {
            pd = new ProgressDialog(MainActivity.this);
            pd.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            });
            pd.setTitle("请稍等");
            //设置对话进度条样式为水平
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //设置提示信息
            pd.setMessage("正在玩命下载中......");
            //设置对话进度条显示在屏幕顶部（方便截图）
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.setCancelable(false);
            pd.setMax(100);
            pd.show();//调用show方法显示进度条对话框
        });

    }

    @Override
    public void onUpdateProgress(int progress) {
        if (pd != null)
            pd.setProgress(progress);
        MyLog.i("下载进度为: " + progress);

    }

    @Override
    public void onUpdateFail(int errorType, String info) {
        runOnUiThread(() -> {
            switch (errorType) {
                case UpdateUtil.FILE_DOWNLOAD_ERROR:
                    break;
                case UpdateUtil.FILE_IO_ERROR:
                    break;
                case UpdateUtil.FILE_MD5_ERROR:
                    break;
                case UpdateUtil.FILE_NOTFOUND_ERROR:
                    break;
            }
            showError(info);
            if (pd != null)
                pd.dismiss();
        });

    }

    @Override
    public void onUpdateSuccess(Intent intent) {
        pd.dismiss();
        startActivity(intent);
    }


    //intent的跳转动画
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (intent.getBooleanExtra("isAnim", false))
            overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
    }


}
