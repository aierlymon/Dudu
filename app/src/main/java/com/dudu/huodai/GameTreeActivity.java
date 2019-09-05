package com.dudu.huodai;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.AdverdialogBean;
import com.dudu.huodai.mvp.presenters.GameTreePresenter;
import com.dudu.huodai.mvp.view.GameTreeImpl;
import com.dudu.huodai.params.ApplicationPrams;
import com.dudu.huodai.utils.AdvertUtil;
import com.dudu.huodai.utils.SoundUtils;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.dudu.huodai.widget.GameWinDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameTreeActivity extends BaseTitleActivity<GameTreeImpl, GameTreePresenter> implements GameTreeImpl {


    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;
    private static final int UPTATE_INTERVAL_TIME = 50;
    private static final int SPEED_SHRESHOLD = 30;//这个值调节灵敏度
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long currentTime=-1;
    private boolean isGo=true;

    @BindView(R.id.tree_bottom_parent)
    RelativeLayout bottomParent;

    @BindView(R.id.game_tree_parent)
    RelativeLayout gameTreeParent;

    private AdvertUtil gameTreeFanBeiAdvert;
    private AdvertUtil gameTreeBackAdvert;
    private AdvertUtil gameTreeDialogBottonAdvert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(!isGo)return;
            long currentUpdateTime = System.currentTimeMillis();
            long timeInterval = currentUpdateTime - lastUpdateTime;
            if (timeInterval < UPTATE_INTERVAL_TIME) {
                return;
            }
            lastUpdateTime = currentUpdateTime;
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;


            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval) * 100;
            if (speed >= SPEED_SHRESHOLD) {
                vibrator.vibrate(100);
                MyLog.i("我监听到摇动");
                if(currentTime==-1){
                    currentTime= System.currentTimeMillis();
                    MyLog.i("执行一次弹窗");
                    showWin();
                }

               // showWin();
                if(System.currentTimeMillis()-currentTime>1000){
                    currentTime=-1;
                }
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void showWin() {
        isGo=false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SoundUtils.playSound(GameTreeActivity.this,R.raw.yao);
              GameWinDialog gameWinDialog=  GameWinDialog.Builder(GameTreeActivity.this)
                        .setMessage("恭喜您，奖励您")
                        .setTitle("+80")
                        .setIconId(R.mipmap.win)
                        .setLeftButtonText("继续摇")
                        .setRightButtonText("金豆翻倍")
                        .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                            @Override
                            public void onClick(View view) {
                                isGo=true;

                            }
                        })
                        .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                            @Override
                            public void onClick(View view) {
                                gameTreeBackAdvert = new AdvertUtil(gameTreeParent, GameTreeActivity.this);
                                float[] WH = Utils.getScreenWH(GameTreeActivity.this.getApplicationContext());
                                gameTreeBackAdvert.setVideoType(ApplicationPrams.GameTree);
                                gameTreeBackAdvert.setSuccess(true);
                                gameTreeBackAdvert.loadVideo(getmTTAdNative(), ApplicationPrams.public_game_tree_jili, (int) WH[0], (int) WH[1]);
                            }
                        })
                        .build().shown();

                gameTreeDialogBottonAdvert = new AdvertUtil(gameWinDialog.getAdvertLayout(), GameTreeActivity.this);
                gameTreeDialogBottonAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_tree_back_dialog_bottom,
                        (int) ((Utils.getScreenWH(GameTreeActivity.this.getApplicationContext())[0]) * 0.9),
                        (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
                MyLog.i("执行完弹窗");
            }
        });
    }

    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.game_tree));
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_gametree;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameTreePresenter createPresenter() {
        return new GameTreePresenter();
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void startToAdvert(boolean isScreenOn) {
        Intent intent=new Intent(this,AdvertSplashActivity.class);
        if(isScreenOn){
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_sceenon_advertId);
        }else{
            intent.putExtra(ApplicationPrams.adverId,ApplicationPrams.public_restart_advertId);
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    public void startToAdvertisement(int requestCode){
        Intent intent=new Intent(this,AdvertisementActivity.class);
        startActivityForResult(intent,requestCode);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkAdverdialogBean(AdverdialogBean adverdialogBean) {
        //选中LoanFragment页面
        if (adverdialogBean.getType() == ApplicationPrams.GameTree) {
                GameAdverBackDialog adverBackDialog = GameAdverBackDialog.Builder(this)
                        .setMessage("恭喜你,奖励您")
                        .setTitle("+160")
                        .setIconId(R.mipmap.win)
                        .hasAdvert(true)
                        .setRightButtonText("继续摇")
                        .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                            @Override
                            public void onClick(View view) {
                                isGo=true;
                            }
                        }).build();
                adverBackDialog.shown();
                gameTreeFanBeiAdvert = new AdvertUtil(adverBackDialog.getAdvertLayout(), GameTreeActivity.this);
                gameTreeFanBeiAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_cai_back_dialog_bottom, (int) ((Utils.getScreenWH(this.getApplicationContext())[0]) * 0.9),
                        (int) getResources().getDimension(R.dimen.game_win_dialog_advert_height));
        }

    }


    AdvertUtil gameTreeFirstAdvert;
    private boolean isFirst;
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor != null) {
            sensorManager.registerListener(sensorEventListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_GAME);//这里选择感应频率
        }
        // caiBottomAdvert  =new AdvertUtil(bottomParent,this);
        if(isFirst){
            gameTreeFirstAdvert =new AdvertUtil(bottomParent,GameTreeActivity.this);
            gameTreeFirstAdvert.loadNativeExpressAd(getmTTAdNative(), ApplicationPrams.public_game_tree_bottom, bottomParent.getWidth(), bottomParent.getHeight());
        }

        isFirst=true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTreeFirstAdvert != null && gameTreeFirstAdvert.getmTTAd() != null) {
            gameTreeFirstAdvert.getmTTAd().destroy();
        }

        if(gameTreeBackAdvert !=null&& gameTreeBackAdvert.getmTTAd()!=null){
            gameTreeBackAdvert.getmTTAd().destroy();
        }

        if (gameTreeDialogBottonAdvert != null && gameTreeDialogBottonAdvert.getmTTAd() != null) {
            gameTreeDialogBottonAdvert.getmTTAd().destroy();
        }

        if(gameTreeFanBeiAdvert !=null&& gameTreeFanBeiAdvert.getmTTAd()!=null){
            gameTreeFanBeiAdvert.getmTTAd().destroy();
        }

    }
}
