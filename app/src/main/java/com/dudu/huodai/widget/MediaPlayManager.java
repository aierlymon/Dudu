package com.dudu.huodai.widget;

import android.media.MediaPlayer;

import com.dudu.baselib.utils.MyLog;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class MediaPlayManager {

    public static boolean isPlay;
    public static int duration;
    public static String iconUrl;
    public static String mediaTitle;
    public static int currentProgress;
    private static  AtomicInteger mediaId=new AtomicInteger(-1);
    public static Timer mediaTimer;

    private static final String key = "key";

    private MediaPlayManager() {
    }

    private  volatile static MediaPlayer mediaPlayer;

    public static MediaPlayer createMediaPlay() {

        if (mediaPlayer == null) {
            synchronized (key) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                }
            }
        }
        return mediaPlayer;
    }



    //充值
    public static void resetMediaplay() {
        //1.先把之前的页面的播放器停掉
        if (mediaTimer != null) {
            MyLog.i("我是进来了停止了历史定时器");
            mediaTimer.cancel();
            mediaTimer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        isPlay = false;
        duration = 0;
        currentProgress = 0;
        mediaId.set(-1);
    }

    public  static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void destoryMedaiPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static AtomicInteger getMediaId() {
        return mediaId;
    }

    public static void setMediaId(int id) {
        mediaId.set(id);
    }
}
