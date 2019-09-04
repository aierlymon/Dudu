package com.dudu.huodai;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.otherpackage.TToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.StoryPresenter;
import com.dudu.huodai.mvp.view.StoryImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.widget.MediaPlayManager;
import com.dudu.huodai.widget.SharePopWindow;
import com.dudu.model.bean.StoryInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
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

    @BindView(R.id.button_story_bigshare)
    Button bigShare;

    @BindView(R.id.float_button)
    ImageView floatButton;

    @BindView(R.id.big_back_parent)
    RelativeLayout bigBackParent;

    @BindView(R.id.story_info_parent)
    RelativeLayout storyInfoParent;

    private TTAdNative mTTAdNative;


    private WeakReference<MediaPlayer> player;

    private String title;
    private TTNativeExpressAd mTTAd;

    private int bigBackWidth, bigBckHeight;
    private int storyInfoWidth,storyInfoHeight;
    private TTNativeExpressAd mTTAD;

    @Override
    protected void initRequest() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(ApplicationPrams.key_id, -1);
        title = intent.getStringExtra(ApplicationPrams.key_title);
        mPresenter.reqestStoryInfo(id);

    }


    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle(title);
        bigShare.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);




        startTimeToMoney();


        //注册微信分享
        regToWx();
        //设置故事名称
        viewState = ((View) findViewById(R.id.story_state));
        MyLog.i("viewState view: " + viewState);
        ((TextView) getBodyView().findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_state));
        ((TextView) viewUseful.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_userful));
        ((TextView) viewConetnt.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_content));
        //设置分享按钮可见


    }


    private Timer timerToMoney;
    private int currentTime = 0;

    private void startTimeToMoney() {

        RequestOptions options = new RequestOptions();
        int size = (int) getResources().getDimension(R.dimen.float_button);
        options.override(size, size); //设置加载的图片大小
        Glide.with(this).load(R.mipmap.time_to_money).apply(options).into(floatButton);

        timerToMoney = new Timer();
        timerToMoney.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentTime == 20 * 1000) {
                    cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(StoryActivity.this).load(R.mipmap.time_end).apply(options).into(floatButton);
                        }
                    });
                }
                currentTime += 1000;
            }
        }, 0, 1000);


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
    protected void startToAdvert(boolean isScreenOn) {
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


                SharePopWindow sharePopWindow2 = new SharePopWindow(this);
                sharePopWindow2.showAtLocation(findViewById(R.id.parent_view), Gravity.BOTTOM, 0, 0);


                //qq分享
             /*   Tencent mTencent = Tencent.createInstance("1109804834", StoryActivity.this);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                mTencent.shareToQQ(StoryActivity.this, params, this);*/
                break;
            case R.id.share:
                SharePopWindow sharePopWindow = new SharePopWindow(this);
                sharePopWindow.showAtLocation(findViewById(R.id.parent_view), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.story_control:


                MyLog.i("StoryActivity player: " + player);
                //判断当前页面是不是正在播放的页面
                if (MediaPlayManager.getMediaId().get() != media_id) {
                    player = new WeakReference<>(MediaPlayManager.createMediaPlay());
                    loadBannerAd(ApplicationPrams.public_storybanner_advertId, bigBackWidth, bigBckHeight);
                    //不是的话，重新播放
                    try {
                        player.get().setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.get().reset();
                        player.get().setDataSource(StoryActivity.this, Uri.parse(HttpConstant.PIC_BASE_URL + url));
                        MyLog.i("mediaMp3: " + HttpConstant.PIC_BASE_URL + url);
                        // 通过异步的方式装载媒体资源
                        player.get().prepareAsync();
                        //保存标题和图标
                        setMediaTitleIcon(icon_url, medai_title);
                        MediaPlayManager.setMediaId(media_id);

                        player.get().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // 装载完毕回调,允许卜凡
                                MediaPlayManager.isPlay = true;
                                //播放
                                startAutioPlay(MediaPlayManager.isPlay);
                                MyLog.i("我是进来StoryActivity： MediaPlayManager.isPlay： " + MediaPlayManager.isPlay);

                                if (MediaPlayManager.getMediaId().get() == media_id) {
                                    if (MediaPlayManager.isPlay) {
                                        Glide.with(StoryActivity.this).load(R.drawable.media_open).into(storyControl);
                                    } else {
                                        Glide.with(StoryActivity.this).load(R.drawable.media_pause).into(storyControl);
                                    }
                                }
                            }
                        });

                        player.get().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                if (!isFinishing()){
                                    Glide.with(StoryActivity.this).load(R.drawable.media_pause).into(storyControl);
                                    bigBackParent.removeView(bigBackParent.getChildAt(bigBackParent.getChildCount()-1));
                                }
                            }
                        });
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                } else {
                    //是的话(每次等于暂停和播放)
                    MediaPlayManager.isPlay = !MediaPlayManager.isPlay;
                    startAutioPlay(MediaPlayManager.isPlay);
                    if (MediaPlayManager.getMediaId().get() == media_id) {
                        if (MediaPlayManager.isPlay) {
                            Glide.with(StoryActivity.this).load(R.drawable.media_open).into(storyControl);
                        } else {
                            Glide.with(StoryActivity.this).load(R.drawable.media_pause).into(storyControl);
                        }
                    }
                }
                break;
            case R.id.story_littel_control:
                MediaPlayManager.isPlay = !MediaPlayManager.isPlay;
                startAutioPlay(MediaPlayManager.isPlay);
                if (MediaPlayManager.getMediaId().get() == media_id) {
                    if (MediaPlayManager.isPlay) {
                        Glide.with(StoryActivity.this).load(R.drawable.media_open).into(storyControl);
                    } else {
                        Glide.with(StoryActivity.this).load(R.drawable.media_pause).into(storyControl);
                    }
                }
                break;
            case R.id.story_little_close:
                stopMediaPlay();
                Glide.with(this).load(R.drawable.media_pause).into(storyControl);
                bigBackParent.removeView(bigBackParent.getChildAt(bigBackParent.getChildCount()-1));
                break;
        }
    }


    private String url;
    private String icon_url;
    private String medai_title;
    private int media_id;

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
        MyLog.i("storyBean.getAudio(): " + storyBean.getAudio());
        if (!TextUtils.isEmpty(storyBean.getAudio()) && storyBean.getAudio().lastIndexOf(".mp3") != -1) {
            storyControl.setVisibility(View.VISIBLE);
        }
        icon_url = HttpConstant.PIC_BASE_URL + storyBean.getPicture();
        medai_title = storyBean.getTitle();

        media_id = storyBean.getId();
        if (MediaPlayManager.createMediaPlay().isPlaying() && MediaPlayManager.getMediaId().get() == media_id) {
            Glide.with(this).load(R.drawable.media_open).into(storyControl);
        }
    }

    Intent wiIntent;
    BroadcastReceiver wxReceiver;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        //  api = WXAPIFactory.createWXAPI(this, App.APP_ID, true);

        // 将应用的appId注册到微信
        //api.registerApp(App.APP_ID);

        //建议动态监听微信启动广播进行注册到微信
      /*  wxReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(App.APP_ID);
            }
        };
        wiIntent = registerReceiver(wxReceiver, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));*/

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

    @Override
    protected void onStop() {
        super.onStop();
        if (wiIntent != null) {
            PackageManager pm = getPackageManager();
            List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(wiIntent, 0);
            if (resolveInfos != null && !resolveInfos.isEmpty()) {
//查询到相应的BroadcastReceiver
                unregisterReceiver(wxReceiver);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bigBackWidth =Utils.px2dip(StoryActivity.this.getApplicationContext(), bigBackParent.getWidth());
        bigBckHeight =Utils.px2dip(StoryActivity.this, (int) (bigBackParent.getHeight()-getResources().getDimension(R.dimen.y60)/2+getResources().getDimension(R.dimen.left_right_marigin)));
        storyInfoWidth =Utils.px2dip(StoryActivity.this.getApplicationContext(), Utils.getScreenWH(this)[0]);
        storyInfoHeight =Utils.px2dip(StoryActivity.this.getApplicationContext(), getResources().getDimension(R.dimen.y85));
        MyLog.i("storyInfoWidth: "+storyInfoWidth+"  storyInfoHeight:  "+storyInfoHeight);
        //请求
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ApplicationPrams.public_firstpage_advertId) //广告位id
                .setSupportDeepLink(true)

                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(storyInfoWidth,storyInfoHeight) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(storyInfoWidth,storyInfoHeight) //这个参数设置即可，不影响个性化模板广告的size
                .build();


        getmTTAdNative().loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                MyLog.i("load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAD=ads.get(0);
                bindAdListener(mTTAD);
                mTTAD.render();//调用render开始渲染广告
                MyLog.i("ad是 广告信息: "+"  view: "+ads.get(0).getExpressAdView());
            }
        });
    }

    private void loadBannerAd(String codeId, int width, int height) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        //  AdSlot adSort=loadAd(codeId, width, height);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(width,height) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(width, height)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        getmTTAdNative().loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                TToast.show(StoryActivity.this, "load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);//设置轮播间隔 ms,不调用则不进行轮播展示
                bindAdListener(mTTAd);
                mTTAd.render();//调用render开始渲染广告
            }
        });


    }

    private boolean mHasShowDownloadActive = false;

    private void bindAdListener(TTNativeExpressAd ad) {

        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                TToast.show(StoryActivity.this, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                TToast.show(StoryActivity.this, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                TToast.show(StoryActivity.this, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {

                //返回view的宽高 单位 dp
                MyLog.i("渲染成功: " + width + "   height: " + height+"  agetView: height"+bigBckHeight+"   view.getid: "+view.getId());
                TToast.show(StoryActivity.this, "渲染成功");
                //在渲染成功回调时展示广告，提升体验
                if(bigBckHeight==height){
                    bigBackParent.removeView(view);
                    bigBackParent.addView(view);
                }else{
                    storyInfoParent.removeAllViews();
                    storyInfoParent.addView(view);
                }


            }
        });
        //dislike设置
        //  bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        //可选，下载监听设置
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                TToast.show(StoryActivity.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    TToast.show(StoryActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                TToast.show(StoryActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                TToast.show(StoryActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                TToast.show(StoryActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                TToast.show(StoryActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.clear();
            player = null;
        }

        if (timerToMoney != null) {
            timerToMoney.cancel();
        }

        if (mTTAd != null) {
            mTTAd.destroy();
        }

        if(mTTAD!=null){
            mTTAD.destroy();
        }

    }
}
