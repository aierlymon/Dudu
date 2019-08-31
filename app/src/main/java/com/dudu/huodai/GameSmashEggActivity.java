package com.dudu.huodai;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.GameSmashBean;
import com.dudu.huodai.mvp.presenters.GameSmashPresenter;
import com.dudu.huodai.mvp.view.GameSmashImpl;
import com.dudu.huodai.ui.adapter.GameSmashAdapter;
import com.dudu.huodai.widget.GameAdverBackDialog;
import com.tencent.connect.avatar.QQAvatar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameSmashEggActivity extends BaseTitleActivity<GameSmashImpl, GameSmashPresenter> implements GameSmashImpl {

    @BindView(R.id.recyclerview_smash)
    RecyclerView mRecyclerView;

    List<String> list;


    @Override
    protected void initRequest() {

    }
    GameSmashAdapter gameSmashAdapter;
    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle("砸金蛋");

        list=new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(""+i);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        gameSmashAdapter=new GameSmashAdapter(this,list);
        mRecyclerView.setAdapter(gameSmashAdapter);
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_game_smashegg;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameSmashPresenter createPresenter() {
        return new GameSmashPresenter();
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
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

    public int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SmashEvent(GameSmashBean smashBean){
        Intent intent=new Intent(this,AdvertisementActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyLog.i("onActivityResult我反悔了: "+requestCode+"  ;   "+resultCode);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1) {
            //弹窗
            GameAdverBackDialog.Builder(this)
                    .setMessage("恭喜你,奖励您")
                    .setTitle("+160")
                    .setIconId(R.mipmap.win)
                    .setRightButtonText("继续砸")
                    .setOnConfirmClickListener(new GameAdverBackDialog.onConfirmClickListener() {
                        @Override
                        public void onClick(View view) {
                            gameSmashAdapter.notifyDataSetChanged();
                        }
                    })
                    .build().shown();
        }

    }
}
