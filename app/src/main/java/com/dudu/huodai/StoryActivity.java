package com.dudu.huodai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.myapplication.App;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.huodai.mvp.presenters.StoryPresenter;
import com.dudu.huodai.mvp.view.StoryImpl;
import com.dudu.huodai.utils.WXUtil;
import com.dudu.model.bean.StoryInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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

    @BindView(R.id.seekbar_story)
    SeekBar seekBar;

    private IWXAPI api;


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

        //设置故事名称
        ((TextView) viewState.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_state));
        ((TextView) viewUseful.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_userful));
        ((TextView) viewConetnt.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_content));
        //设置分享按钮可见
        btnShare.setVisibility(View.VISIBLE);


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

    @OnClick({R.id.button_showcontent, R.id.button_story_bigshare,R.id.button_story_mp3})
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
                Tencent mTencent=Tencent.createInstance("1109804834",StoryActivity.this);
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
            case R.id.button_story_mp3:
                if(!TextUtils.isEmpty(url)){
                    MediaPlayer player= MediaPlayer.create(StoryActivity.this, Uri.parse(HttpConstant.PIC_BASE_URL+url));
                    int max = player.getDuration();//获取音乐的播放时间  单位是毫秒
                    player.start();
                    seekBar.setMax(max);
                }

                break;
        }
    }

    private String url;
    @Override
    public void refreshUi(StoryInfo.StoryBean storyBean) {
        txStoryState.setText(storyBean.getDescription());
        txStoryUserful.setText(storyBean.getSignificance());
        txStoryContent.setText(storyBean.getContent());
        txStoryReSource.setText(storyBean.getSource());
        url= storyBean.getAudio();
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
}
