package com.dudu.huodai.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.dudu.baselib.otherpackage.TToast;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.model.postbean.AdvertSplashBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AdvertUtil implements TTAppDownloadListener {
    private boolean mHasShowDownloadActive = false;

    private TTNativeExpressAd mTTAd;

    private ViewGroup viewGroup;

    private Activity activity;

    private boolean isCirecle;
    private TTRewardVideoAd mttRewardVideoAd;

    private boolean isSuccess;//只给猜谜用的

    private int videoType;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public void setCirecle(boolean cirecle) {
        isCirecle = cirecle;
    }

    public AdvertUtil(ViewGroup viewGroup, Activity activity) {
        this.viewGroup = viewGroup;
        this.activity = activity;
    }




    public void loadBannerAd(TTAdNative mTTAdNative, String codeId, int width, int height) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        //  AdSlot adSort=loadAd(codeId, width, height);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(width, height) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(width, height)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                TToast.show(activity, "load error : " + code + ", " + message);
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


    public void loadNativeExpressAd(TTAdNative mTTAdNative, String codeId, int width, int height) {
        width = Utils.px2dip(activity.getApplicationContext(), width);
        height = Utils.px2dip(activity.getApplicationContext(), height);
        MyLog.i("onRenderSuccess loadNativeExpressAd: " + "  view.getWidth(): " + width + "  view.getHeight() " + height);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(width, height) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(width, height) //这个参数设置即可，不影响个性化模板广告的size
                .build();

        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                MyLog.i("load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                mTTAd.render();//调用render开始渲染广告
            }
        });

    }


    private void bindAdListener(TTNativeExpressAd ad) {

        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                TToast.show(activity, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                TToast.show(activity, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                TToast.show(activity, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                MyLog.i("onRenderSuccess: " + viewGroup + "  view.getWidth(): " + width + "  view.getHeight() " + height);
                view.setBackgroundColor(Color.BLUE);
                viewGroup.removeView(view);
                viewGroup.addView(view);
            }
        });
        //dislike设置
        //  bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        //可选，下载监听设置
        ad.setDownloadListener(this);

    }


    public void loadVideo(TTAdNative mTTAdNative, String codeId, int width, int height) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                TToast.show(activity, message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                mttRewardVideoAd.showRewardVideoAd(activity);

                TToast.show(activity, "rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                TToast.show(activity, "rewardVideoAd loaded");
                mttRewardVideoAd = ad;

//                mttRewardVideoAd.setShowDownLoadBar(false);
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        MyLog.i("rewardVideoAd show");
                        TToast.show(activity, "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        MyLog.i("rewardVideoAd bar click");
                        TToast.show(activity, "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        MyLog.i("rewardVideoAd close");
                        TToast.show(activity, "rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        MyLog.i("rewardVideoAd complete");
                        AdverdialogBean adverdialogBean = new AdverdialogBean();
                        adverdialogBean.setType(videoType);
                        if(isSuccess){
                            adverdialogBean.setSuccess(true);
                        }else{
                            adverdialogBean.setSuccess(false);
                        }
                        EventBus.getDefault().post(adverdialogBean);
                        TToast.show(activity, "rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        MyLog.i("rewardVideoAd error");
                        TToast.show(activity, "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        TToast.show(activity, "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName);
                    }

                    @Override
                    public void onSkippedVideo() {
                        TToast.show(activity, "rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        TToast.show(activity, "点击开始下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadActive(long l, long l1, String s, String s1) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            TToast.show(activity, "下载中，点击暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadFailed(long l, long l1, String s, String s1) {
                        TToast.show(activity, "下载失败，点击重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long l, String s, String s1) {
                        TToast.show(activity, "点击安装", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String s, String s1) {
                        TToast.show(activity, "安装完成，点击图片打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });

    }

    public TTNativeExpressAd getmTTAd() {
        return mTTAd;
    }

    @Override
    public void onIdle() {
        TToast.show(activity, "点击开始下载", Toast.LENGTH_LONG);

    }

    @Override
    public void onDownloadActive(long l, long l1, String s, String s1) {
        if (!mHasShowDownloadActive) {
            mHasShowDownloadActive = true;
            TToast.show(activity, "下载中，点击暂停", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onDownloadPaused(long l, long l1, String s, String s1) {
        TToast.show(activity, "下载暂停，点击继续", Toast.LENGTH_LONG);
    }

    @Override
    public void onDownloadFailed(long l, long l1, String s, String s1) {
        TToast.show(activity, "下载失败，点击重新下载", Toast.LENGTH_LONG);

    }

    @Override
    public void onDownloadFinished(long l, String s, String s1) {
        TToast.show(activity, "点击安装", Toast.LENGTH_LONG);

    }

    @Override
    public void onInstalled(String s, String s1) {
        TToast.show(activity, "安装完成，点击图片打开", Toast.LENGTH_LONG);
    }


    private int index = -1;

    public void loadSplashAd(TTAdNative mTTAdNative, String codeId, int width, int height, int timeout, int index) {
        this.index = index;
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(width, height)
                .build();

        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                MyLog.i("loadSplashAd onError "+code);
            }

            @Override
            @MainThread
            public void onTimeout() {

            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                viewGroup.removeAllViews();
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                viewGroup.addView(view);
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        MyLog.i("loadSplashAd 广告跳过");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        MyLog.i("loadSplashAd 广告跳过");
                    }

                    @Override
                    public void onAdSkip() {
                        MyLog.i("loadSplashAd 广告跳过");
                        AdvertSplashBean advertSplashBean = new AdvertSplashBean();
                        advertSplashBean.setType(AdvertUtil.this.index);
                        EventBus.getDefault().post(advertSplashBean);
                    }

                    @Override
                    public void onAdTimeOver() {
                        MyLog.i("loadSplashAd 广告结束");
                        AdvertSplashBean advertSplashBean = new AdvertSplashBean();
                        advertSplashBean.setType(AdvertUtil.this.index);
                        EventBus.getDefault().post(advertSplashBean);
                    }
                });
            }
        }, timeout);
    }


}
