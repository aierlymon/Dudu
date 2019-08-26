package com.dudu.huodai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.myapplication.App;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.presenters.StoryPresenter;
import com.dudu.huodai.mvp.view.StoryImpl;
import com.dudu.huodai.widget.CircleImageView;
import com.dudu.huodai.widget.SharePopWindow;
import com.dudu.model.bean.StoryInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//IUiListener是qq的
public class StoryActivity extends BaseMvpActivity<StoryImpl, StoryPresenter> implements StoryImpl, IUiListener {
    @BindView(R.id.story_state)
    View viewState;

    @BindView(R.id.story_useful)
    View viewUseful;


    @BindView(R.id.story_content)
    View viewConetnt;

    @BindView(R.id.share)
    Button btnShare;

    @BindView(R.id.tx_story_state)
    TextView txStoryState;

    @BindView(R.id.tx_story_userful)
    TextView txStoryUserful;

    @BindView(R.id.story_bigback)
    ImageView imageView;

    @BindView(R.id.tx_story_content)
    TextView txStoryContent;

    @BindView(R.id.parent_content)
    LinearLayout parentContent;

    @BindView(R.id.tx_story_resource)
    TextView txStoryReSource;


    @BindView(R.id.story_media)
    View view;

    @BindView(R.id.story_control)
    ImageView storyControl;

    @BindView(R.id.tx_title)
    TextView txTitle;

    @BindView(R.id.img_back)
    ImageView imgBack;

    private IWXAPI api;

    private static MediaPlayer player;

    @BindView(R.id.media_prgress)
    ProgressBar mediaProgress;
    private CircleImageView mediaPlayIcon;
    private TextView refreshTime;
    private TextView countTime;
    private CircleImageView littleControl;
    private TextView txTitleMedia;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_story;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        initRequest(id);
        init();

    }

    private void initRequest(int id) {
        mPresenter.reqestStoryInfo(id);
    }


    private void init() {
        //注册微信分享
        regToWx();

        ButterKnife.bind(this);
        imgBack.setVisibility(View.VISIBLE);

        //设置故事名称
        ((TextView) viewState.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_state));
        ((TextView) viewUseful.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_userful));
        ((TextView) viewConetnt.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_content));
        //设置分享按钮可见
        btnShare.setVisibility(View.VISIBLE);

        //这个是故事播放器的控件名称
        mediaPlayIcon = ((CircleImageView) view.findViewById(R.id.title_icon));
        refreshTime = ((TextView) view.findViewById(R.id.refresh_time));
        countTime = ((TextView) view.findViewById(R.id.count_time));
        littleControl = ((CircleImageView) view.findViewById(R.id.story_littel_control));
        txTitleMedia = ((TextView) view.findViewById(R.id.tx_title_media));
    }

    @Override
    protected StoryPresenter createPresenter() {
        return new StoryPresenter();
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


    private int currentTime;//这个是当前播放时间
    private Timer timer;//播放音频的定时器
    private int duration;//音频时长
    private boolean isPlay;//当前的播放状态

    @OnClick({R.id.button_showcontent, R.id.button_story_bigshare, R.id.share, R.id.story_control,R.id.story_littel_control,R.id.story_little_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_showcontent:
                v.setVisibility(View.GONE);
                parentContent.setVisibility(View.VISIBLE);
                break;
            case R.id.button_story_bigshare:
             /*   //初始化一个WXWebpageObject，填写url
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "网页url";

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "网页标题 ";
                msg.description = "网页描述";
                Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.lllogo);
                msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);

//构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline ; ;
                req.userOpenId = App.APP_ID;

//调用api接口，发送数据到微信
                api.sendReq(req);*/

                //qq分享
                Tencent mTencent = Tencent.createInstance("1109804834", StoryActivity.this);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                mTencent.shareToQQ(StoryActivity.this, params, this);


                break;
            case R.id.share:
                SharePopWindow sharePopWindow = new SharePopWindow(this);
                sharePopWindow.showAtLocation(findViewById(R.id.parent_view), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.story_littel_control:
            case R.id.story_control:
                startAutioPlay();
                break;
            case R.id.story_little_close:
                stopAutioPlay();
                break;
        }
    }

    private void stopAutioPlay() {
        //关闭定时器
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        //定制播放音频
        if(player!=null){
            player.stop();
            player=null;
        }
        ////图片变为初始化状态
        Glide.with(this).load(R.drawable.media_pause).into(littleControl);
        Glide.with(this).load(R.drawable.media_pause).into(storyControl);
        //隐藏播放列表
        view.setVisibility(View.GONE);
        //把当前是否启动播放设置为false
        isPlay=false;
        //当前的进度条currentTime
        currentTime=0;
        mediaProgress.setProgress(0);
    }

    //是否播放音频
    private void startAutioPlay() {
        isPlay = !isPlay;
        if (isPlay) {
            //点击之后,第一次启动的时候
            if (view.getVisibility() == View.GONE) {
                if (player == null)
                    player = MediaPlayer.create(StoryActivity.this.getApplicationContext(), Uri.parse(HttpConstant.PIC_BASE_URL + url));
                //启动播放音频
                duration = player.getDuration();//获取音乐的播放时间  单位是毫秒
                mediaProgress.setMax(duration);
                countTime.setText(Utils.generateTime(duration));
                view.setVisibility(View.VISIBLE);
                //设置图片为播放
                Glide.with(this).load(R.drawable.media_open).into(littleControl);
                Glide.with(this).load(R.drawable.media_open).into(storyControl);
                //开启定时器
                timer = new Timer();
                timer.schedule(new MyTimerClass(), 10, 1000);
                player.start();
                return;
            } else {
                //设置图片为播放
                Glide.with(this).load(R.drawable.media_open).into(littleControl);
                Glide.with(this).load(R.drawable.media_open).into(storyControl);
                //启动定时器
                timer = new Timer();
                timer.schedule(new MyTimerClass(), 10, 1000);
                //继续播放
                if (player != null) {
                    player.start();
                }
            }
        } else {
            //暂停播放，但是不回收
            if (player != null && player.isPlaying()) {
                //取消定时器
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                //暂停播放
                player.pause();
                //图片显示暂停
                Glide.with(this).load(R.drawable.media_pause).into(littleControl);
                Glide.with(this).load(R.drawable.media_pause).into(storyControl);
            }
        }

    }

    private String url;

    @Override
    public void refreshUi(StoryInfo.StoryBean storyBean) {
        //故事的概括
        txStoryState.setText(storyBean.getDescription());
        //故事的意义
        txStoryUserful.setText(storyBean.getSignificance());
        //故事的内容
        txStoryContent.setText(storyBean.getContent());
        //故事的来源
        txStoryReSource.setText(storyBean.getSource());
        //音频播放器的图标
        Glide.with(this).load(HttpConstant.PIC_BASE_URL+storyBean.getPicture()).into(mediaPlayIcon);
        //音频播放器的标题
        txTitleMedia.setText(storyBean.getTitle());
        //头标题
        txTitle.setText(storyBean.getTitle());
        url = storyBean.getAudio();
        Glide.with(this).load(HttpConstant.PIC_BASE_URL + storyBean.getPicture()).into(imageView);
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, App.APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(App.APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(App.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    class MyTimerClass extends TimerTask {
        @Override
        public void run() {
            if (currentTime >= duration) {
                //播放完毕
                player.stop();
                player = null;
                this.cancel();
            } else {
                currentTime += 1000;
                runOnUiThread(() -> refreshTime.setText(Utils.generateTime(currentTime)));
            }
            mediaProgress.setProgress(currentTime);
        }
    }

}
