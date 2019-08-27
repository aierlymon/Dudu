package com.dudu.huodai.mvp.base;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.dudu.baselib.base.BaseFragment;
import com.dudu.baselib.base.BaseMVPFragment;
import com.dudu.baselib.mvp.IPresenter;
import com.dudu.baselib.mvp.IView;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.ApplicationPrams;
import com.dudu.huodai.R;
import com.dudu.huodai.widget.CircleImageView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public abstract class BaseTitleFragment<V extends IView, P extends IPresenter> extends BaseMVPFragment<V, P> implements IView {

    private View bodyView;
    private TextView txTitle;
    private ImageView backIcon;
    private CircleImageView mediaPlayIcon, littleControl;
    private TextView countTime, refreshTime, txTitleMedia;
    private View MediaView, HeadView;
    private ProgressBar mediaProgress;
    private CircleImageView little_close;


    @Override
    protected void initBefore() {
        bodyView= LayoutInflater.from(getActivity()).inflate(getBodyLayoutRes(),null);
        ((LinearLayout)getParentview().findViewById(R.id.base_content_parent)).addView(bodyView);

        HeadView = ((View) getParentview().findViewById(R.id.myhead));
        MyLog.i("BaseTitleFragment HeadView: " + HeadView);
        //头部的head
        txTitle = ((TextView) HeadView.findViewById(R.id.tx_title));
        backIcon = ((ImageView) HeadView.findViewById(R.id.img_back));
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        if (hasBackHome()) {
            backIcon.setVisibility(View.VISIBLE);
        }
        //播放器
        //这个是故事播放器的控件名称
        MediaView = getParentview().findViewById(R.id.story_media);

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

            }
        });

        little_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击了关闭", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initBase(View view) {



    }

    public View getBodyView() {
        return bodyView;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_basetitle;
    }

    protected abstract int getBodyLayoutRes();

    protected abstract boolean hasBackHome();


    public void setTitleText(String titleName) {
        txTitle.setText(titleName);
    }

    public void setTitleText(@StringRes int stringRes) {
        txTitle.setText(stringRes);
    }


    private int currentTime;//这个是当前播放时间
    private Timer timer;//播放音频的定时器
    private int duration;//音频时长
    private boolean isPlay;//当前的播放状态
    private MediaPlayer player;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.player = mediaPlayer;
    }

    public boolean isPlay() {
        return isPlay;
    }

    //是否播放音频
    public void startAutioPlay(MediaPlayer player) {
        this.player = player;
        if (player == null) return;
        isPlay = !isPlay;
        if (isPlay) {
            //点击之后,第一次启动的时候
            if (MediaView.getVisibility() == View.GONE) {
                //启动播放音频
                duration = player.getDuration();//获取音乐的播放时间  单位是毫秒
                mediaProgress.setMax(duration);
                countTime.setText(Utils.generateTime(duration));
                MediaView.setVisibility(View.VISIBLE);
                //设置图片为播放
                Glide.with(this).load(R.drawable.media_open).into(littleControl);
                //开启定时器
                timer = new Timer();
                timer.schedule(new MyTimerClass(), 10, 1000);
                player.start();
                return;
            } else {
                //设置图片为播放
                Glide.with(this).load(R.drawable.media_open).into(littleControl);
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
            }
        }

    }

    public void stopAutioPlay() {
        //关闭定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        //定制播放音频
        if (player != null) {
            player.stop();
            player = null;
            ApplicationPrams.player = null;
        }
        ////图片变为初始化状态
        Glide.with(this).load(R.drawable.media_pause).into(littleControl);
        //隐藏播放列表
        MediaView.setVisibility(View.GONE);
        //把当前是否启动播放设置为false
        isPlay = false;
        //当前的进度条currentTime
        currentTime = 0;
        mediaProgress.setProgress(0);
    }

    private class MyTimerClass extends TimerTask {
        @Override
        public void run() {
            if (currentTime >= duration) {
                //播放完毕
                player.stop();
                player = null;
                this.cancel();
            } else {
                currentTime += 1000;
                getActivity().runOnUiThread(() -> refreshTime.setText(Utils.generateTime(currentTime)));
            }
            mediaProgress.setProgress(currentTime);
        }
    }

    public void setMediaTitleIcon(String url, String title) {
        //音频播放器的图标
        Glide.with(this).load(url).into(mediaPlayIcon);
        //音频播放器的标题
        txTitleMedia.setText(title);
    }
}
