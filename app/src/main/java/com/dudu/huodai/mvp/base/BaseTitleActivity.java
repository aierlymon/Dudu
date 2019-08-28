package com.dudu.huodai.mvp.base;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.mvp.IPresenter;
import com.dudu.baselib.mvp.IView;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;
import com.dudu.huodai.widget.CircleImageView;
import com.dudu.huodai.widget.MediaPlayManager;

import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseTitleActivity<V extends IView, P extends IPresenter<V>> extends BaseMvpActivity<V, P> {


    private View bodyView;
    private TextView txTitle;
    private ImageView backIcon;

    private CircleImageView mediaPlayIcon, littleControl;
    private TextView countTime, refreshTime, txTitleMedia;
    private View MediaView, HeadView;
    private ProgressBar mediaProgress;
    private CircleImageView little_close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        initBase();
        initRequest();
        init();

    }

    private void initBase() {
        HeadView = ((View) findViewById(R.id.myhead));
        //头部的head
        txTitle = ((TextView) HeadView.findViewById(R.id.tx_title));
        backIcon = ((ImageView) HeadView.findViewById(R.id.img_back));
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (hasBackHome()) {
            backIcon.setVisibility(View.VISIBLE);
        }
        //播放器
        //这个是故事播放器的控件名称
        MediaView = findViewById(R.id.story_media);

        mediaPlayIcon = ((CircleImageView) MediaView.findViewById(R.id.title_icon));
        refreshTime = ((TextView) MediaView.findViewById(R.id.refresh_time));
        countTime = ((TextView) MediaView.findViewById(R.id.count_time));
        littleControl = ((CircleImageView) MediaView.findViewById(R.id.story_littel_control));
        txTitleMedia = ((TextView) MediaView.findViewById(R.id.tx_title_media));
        mediaProgress = ((ProgressBar) MediaView.findViewById(R.id.media_prgress));
        little_close = ((CircleImageView) MediaView.findViewById(R.id.story_little_close));

        littleControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutioPlay(!MediaPlayManager.isPlay);
            }
        });

        little_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMediaPlay();
            }
        });

        bodyView = LayoutInflater.from(this).inflate(getBodyLayoutRes(), null);
        ((LinearLayout) findViewById(R.id.base_content_parent)).addView(bodyView);

        //初始化mediaPlay，判断是否开启
        if(MediaPlayManager.getMediaPlayer()!=null&&MediaPlayManager.mediaId!=-1){
            justShowMediaPlay(MediaPlayManager.getMediaPlayer());
        }
    }



    public View getBodyView() {
        return bodyView;
    }

    protected abstract void initRequest();

    public abstract void init();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_basetitle;
    }

    protected abstract int getBodyLayoutRes();

    protected abstract boolean hasBackHome();


    public void setTitle(String titleName) {
        txTitle.setText(titleName);
    }

    public void setTitle(@StringRes int stringRes) {
        txTitle.setText(stringRes);
    }


    private Timer timer;//播放音频的定时器


    //播放音频（启动音频，继续播放）
    public void startAutioPlay(boolean isPlay) {
        MediaPlayer player = MediaPlayManager.createMediaPlay();
        if (isPlay) {
            //点击之后,第一次启动的时候
            if (MediaView.getVisibility() == View.GONE) {
                MyLog.i("我是进来了开始播放: ");
                //启动播放音频
                MediaView.setVisibility(View.VISIBLE);
                //播放音频
                startMediaPlay(player);

            } else {
                MyLog.i("我是进来了可以播放，并且已经显示的情况下");
                //播放音频
                startMediaPlay(player);
            }
        } else {
            pauseMediaPlay(player);
        }
    }

    private void startMediaPlay(MediaPlayer player) {
        //获取时长
        MediaPlayManager.duration = player.getDuration();//获取音乐的播放时间  单位是毫秒
        mediaProgress.setMax(MediaPlayManager.duration);
        countTime.setText(Utils.generateTime(MediaPlayManager.duration));
        //设置图片为播放
        Glide.with(this).load(R.drawable.media_open).into(littleControl);
        //开启定时器,更新当前时间
        startTimer();
        //音频播放了
        player.start();
    }


    public void justShowMediaPlay(MediaPlayer player){
        //启动播放音频
        MediaView.setVisibility(View.VISIBLE);
        //获取时长
        MediaPlayManager.duration = player.getDuration();//获取音乐的播放时间  单位是毫秒
        mediaProgress.setMax(MediaPlayManager.duration);
        countTime.setText(Utils.generateTime(MediaPlayManager.duration));

        setMediaTitleIcon(MediaPlayManager.iconUrl,MediaPlayManager.mediaTitle);

        //设置图片为播放
        if(MediaPlayManager.getMediaPlayer().isPlaying()){
            Glide.with(this).load(R.drawable.media_open).into(littleControl);
        }else{
            Glide.with(this).load(R.drawable.media_pause).into(littleControl);
        }

        //开启定时器,更新当前时间
        startTimer();
    }

    //暂停播放，但是不回收
    public void pauseMediaPlay(MediaPlayer player){
        MyLog.i("我是进来了暂停播放");
        if (player != null && player.isPlaying()) {
            //取消定时器
            stopTimer();
            //暂停播放
            player.pause();
            //图片显示暂停
            Glide.with(this).load(R.drawable.media_pause).into(littleControl);
        }
    }

    public void stopMediaPlay() {
        //停止播放音频
        MediaPlayManager.resetMediaplay();
        //隐藏播放列表
        MediaView.setVisibility(View.GONE);
    }

    private class MyTimerClass extends TimerTask {
        @Override
        public void run() {
            MyLog.i("我是进来定时器---》" + this);
            if (MediaPlayManager.currentProgress >= MediaPlayManager.duration) {
                MyLog.i("我是进来定时器挂了: " + MediaPlayManager.duration);
                //播放完毕(包含了关闭定时器和音频对象了)
                MediaPlayManager.resetMediaplay();
            } else {
                MediaPlayManager.currentProgress = MediaPlayManager.createMediaPlay().getCurrentPosition();
                runOnUiThread(() -> refreshTime.setText(Utils.generateTime(MediaPlayManager.currentProgress)));
            }
            mediaProgress.setProgress(MediaPlayManager.currentProgress);
        }
    }



    public void setMediaTitleIcon(String url, String title) {
        //音频播放器的图标
        Glide.with(this).load(url).into(mediaPlayIcon);
        MediaPlayManager.iconUrl = url;
        //音频播放器的标题
        txTitleMedia.setText(title);
        MediaPlayManager.mediaTitle = title;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        MyLog.i("我是进来了onRestart");
        if(MediaPlayManager.getMediaPlayer()!=null){
            justShowMediaPlay(MediaPlayManager.getMediaPlayer());
        }
        //resetAutioPlay(ApplicationPrams.player);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }



    //关闭定时器
    private void stopTimer() {
        if (timer != null) {
            MyLog.i("我已经关闭的定时器了");
            timer.cancel();
            timer = null;
            MediaPlayManager.mediaTimer = null;
        }
    }

    //启动定时器
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new MyTimerClass(), 0, 1000);
        MediaPlayManager.mediaTimer = timer;
    }
}