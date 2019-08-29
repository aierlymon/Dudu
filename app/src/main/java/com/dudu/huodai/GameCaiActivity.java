package com.dudu.huodai;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.presenters.GameCaiPresenter;
import com.dudu.huodai.mvp.view.GameCaiImpl;
import com.dudu.huodai.ui.adapter.GameItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameCaiActivity extends BaseTitleActivity<GameCaiImpl, GameCaiPresenter> implements GameCaiImpl {


    String[] strs={"大","吉","大","里","的","大","吉","大","里","利"};

    @BindView(R.id.recyclerview_item)
    RecyclerView recyclerView;

    @Override
    protected void initRequest() {

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.game_caicai));

        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        List<String> itemList=new ArrayList<>();
        for(int i=0;i<strs.length;i++){
            itemList.add(strs[i]);
        }
        GameItemAdapter gameItemAdapter=new GameItemAdapter(itemList,this);
        recyclerView.setAdapter(gameItemAdapter);
    }

    @Override
    protected int getBodyLayoutRes() {
        return R.layout.game_caicaicai;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    protected GameCaiPresenter createPresenter() {
        return new GameCaiPresenter();
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
}
