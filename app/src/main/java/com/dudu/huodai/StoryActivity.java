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
import com.dudu.huodai.mvp.base.BaseTitleActivity;
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
public class StoryActivity extends BaseTitleActivity<StoryImpl, StoryPresenter> implements StoryImpl, IUiListener {


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


    @BindView(R.id.tx_story_content)
    TextView txStoryContent;

    @BindView(R.id.story_bigback)
    ImageView storyBigback;

    @BindView(R.id.parent_content)
    LinearLayout parentContent;

    @BindView(R.id.tx_story_resource)
    TextView txStoryReSource;

    @BindView(R.id.story_control)
    ImageView storyControl;

    View viewState;


    private IWXAPI api;

    private MediaPlayer player;

    @Override
    protected void initRequest() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        mPresenter.reqestStoryInfo(id);
    }


    @Override
    public void init() {
        ButterKnife.bind(this);
        //注册微信分享
        regToWx();
        //设置故事名称
        viewState = ((View) findViewById(R.id.story_state));
        MyLog.i("viewState view: " + viewState);
        ((TextView) getBodyView().findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_state));
        ((TextView) viewUseful.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_userful));
        ((TextView) viewConetnt.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_content));
        //设置分享按钮可见
        btnShare.setVisibility(View.VISIBLE);
    }


    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_story;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
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


    @OnClick({R.id.button_showcontent, R.id.button_story_bigshare, R.id.share, R.id.story_control, R.id.story_littel_control, R.id.story_little_close})
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
            case R.id.story_control:

            case R.id.story_littel_control:
                if (ApplicationPrams.player == null) {
                    player = MediaPlayer.create(StoryActivity.this.getApplicationContext(), Uri.parse(HttpConstant.PIC_BASE_URL + url));
                    ApplicationPrams.player = player;
                }
                startAutioPlay(player);
                if (isPlay()) {
                    Glide.with(this).load(R.drawable.media_open).into(storyControl);
                }else{
                    Glide.with(this).load(R.drawable.media_pause).into(storyControl);
                }
                break;
            case R.id.story_little_close:
                stopAutioPlay();
                Glide.with(this).load(R.drawable.media_pause).into(storyControl);
                break;
        }
    }


    private String url;

    @Override
    public void refreshUi(StoryInfo.StoryBean storyBean) {
        //故事的概括
        txStoryState.setText(storyBean.getDescription());
        //故事的意义
        MyLog.i("故事的意义: " + storyBean.getSignificance() + "  故事的内容: " + storyBean.getContent());
        txStoryUserful.setText(storyBean.getSignificance());
        //故事的内容
        txStoryContent.setText(storyBean.getContent());
        //故事的来源
        txStoryReSource.setText(storyBean.getSource());
        //故事的大图
        Glide.with(this).load(HttpConstant.PIC_BASE_URL + storyBean.getPicture()).into(storyBigback);

        url = storyBean.getAudio();
        //设置音频的图标
        setMediaTitleIcon(HttpConstant.PIC_BASE_URL + storyBean.getPicture(), storyBean.getTitle());
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
