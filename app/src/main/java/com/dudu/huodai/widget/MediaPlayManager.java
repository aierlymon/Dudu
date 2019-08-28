package com.dudu.huodai.widget;

import android.media.MediaPlayer;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.ApplicationPrams;

import java.util.Timer;

public class MediaPlayManager {

    public static boolean isPlay;
    public static int duration;
    public static String iconUrl;
    public static String mediaTitle;
    public static int currentProgress;
    public static int mediaId = -1;
    public static Timer mediaTimer;

    private static final String key = "key";

    private MediaPlayManager() {
    }

    private static MediaPlayer mediaPlayer;

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

    public static void StopMediaPlay() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
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
        mediaId = -1;
    }

    public synchronized static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void destoryMedaiPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
