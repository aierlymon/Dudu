package com.linglong.huodai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.StatusBarUtil;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.LoopViewPager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * createBy ${huanghao}
 * on 2019/7/27
 */
public class SplashActivity extends AppCompatActivity {

    private LoopViewPager loopViewPager;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清

        //这个就是设施沉浸式状态栏的主要方法了
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);

        List<String> list = getIntent().getStringArrayListExtra("urls");
        MyLog.i("SplashActivity list.size: " + list.size());

        initView(list);

    }

    private void initView(List<String> list) {
        //跳过按钮
        Button btn = ((Button) findViewById(R.id.jump));
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMain();
            }
        });

        if(list.size()>0){
            //加载好话筒图
            loopViewPager = ((LoopViewPager) findViewById(R.id.splash_viewpager));
            if (list.size() > 1) {
                loopViewPager.showIndicator(true);
                loopViewPager.setDelayTime(5000/(list.size()*2));
                loopViewPager.startBanner();
                loopViewPager.setIndicatorGravity(LoopViewPager.IndicatorGravity.CENTER);
            }
            loopViewPager.setData(this, list, (view, position1, item) -> {
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                MyLog.i("banner item icon: " + item);
                //加载图片，如gide
                Glide.with(this).load(item).into(view);
            });


          /*  SplashVPAdapter splashVPAdapter = new SplashVPAdapter(this, list);
            splashVPAdapter.setOnItemClickListener(view -> {
                //点击事件
            });*/
          //  mViewPager.setAdapter(splashVPAdapter);
        }



        //开启一个定时器，定时时长为5秒
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toMain();
                    }
                });
            }
        }, 5000);

    }

    public void toMain() {
        if (!isFinishing()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null)
        timer.cancel();
    }

}
