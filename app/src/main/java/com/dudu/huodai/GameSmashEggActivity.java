package com.dudu.huodai;

import android.content.Context;
import android.content.res.Resources;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameSmashPresenter;
import com.dudu.huodai.mvp.view.GameSmashImpl;
import com.dudu.huodai.ui.adapter.GameSmashAdapter;
import com.tencent.connect.avatar.QQAvatar;

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

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle("砸金蛋");

        list=new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(""+i);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        GameSmashAdapter gameSmashAdapter=new GameSmashAdapter(this,list);
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
}
