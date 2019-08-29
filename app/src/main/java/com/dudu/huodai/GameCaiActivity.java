package com.dudu.huodai;

import android.widget.CheckBox;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.base.BaseTitleActivity;
import com.dudu.huodai.mvp.model.postbean.GameCaiSelectBean;
import com.dudu.huodai.mvp.presenters.GameCaiPresenter;
import com.dudu.huodai.mvp.view.GameCaiImpl;
import com.dudu.huodai.ui.adapter.GameItemAdapter;
import com.dudu.huodai.widget.MoneyDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class GameCaiActivity extends BaseTitleActivity<GameCaiImpl, GameCaiPresenter> implements GameCaiImpl {


    String[] strs = {"大", "吉", "大", "里", "的", "大", "吉", "大", "里", "利"};

    @BindView(R.id.recyclerview_item)
    RecyclerView recyclerView;


    @BindView(R.id.ck_first)
    CheckBox ck1;

    @BindView(R.id.ck_second)
    CheckBox ck2;

    @BindView(R.id.ck_third)
    CheckBox ck3;

    @BindView(R.id.ck_four)
    CheckBox ck4;

    private int currentIndex = 0;//当前要填写的ck索引
    List<Boolean> list;

    @Override
    protected void initRequest() {

    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGameBean(GameCaiSelectBean gameBean) {
        MyLog.i("触发了这里： "+currentIndex);
        switch (currentIndex) {
            case 0:
                refreshCK(ck1, gameBean,0);
                break;
            case 1:
                refreshCK(ck2, gameBean, 1);
                break;
            case 2:
                refreshCK(ck3, gameBean, 2);
                break;
            case 3:
                refreshCK(ck4, gameBean, 3);
                //跳出最后的结果判断
                judegeResult();
                break;
        }
    }

    private void judegeResult() {
        String str = ck1.getText().toString() + ck2.getText().toString() + ck3.getText().toString() + ck4.getText().toString();
        if (str.equals("大吉大利")) {
            MoneyDialog.Builder(this).setMessage("恭喜你答对了").setTitle("+45").build().shown();
        } else {
            MoneyDialog.Builder(this).setMessage("很抱歉你答错了").setTitle("+45").build().shown();
        }

    }


    public void refreshCK(CheckBox view, GameCaiSelectBean gameBean, int currentIndex) {

        view.setChecked(true);
        view.setEnabled(true);
        view.setTag(gameBean.getCheckBox());
        view.setText(gameBean.getText());
        list.set(currentIndex,true);
        MyLog.i("更细CK----2");
        getCurrentIndex(4);
    }

    @Override
    public void init() {

        list =new ArrayList<>();
        for(int i=0;i<4;i++){
            list.add(false);
        }


        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.game_caicai));

        ck1.setEnabled(false);
        ck2.setEnabled(false);
        ck3.setEnabled(false);
        ck4.setEnabled(false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            itemList.add(strs[i]);
        }
        GameItemAdapter gameItemAdapter = new GameItemAdapter(itemList, this);
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

    @OnCheckedChanged({R.id.ck_first, R.id.ck_second, R.id.ck_third, R.id.ck_four})
    public void onCheckChange(CheckBox v) {

        switch (v.getId()) {
            case R.id.ck_first:
                resetCK(v,0,1);
                break;
            case R.id.ck_second:

                resetCK(v,1,2);
                break;
            case R.id.ck_third:
                resetCK(v,2,3);
                break;
            case R.id.ck_four:
                resetCK(v,3,4);
                break;
        }
    }

    private void resetCK(CheckBox v, int index, int size) {
        if (!v.isChecked()) {
            ((CheckBox) v.getTag()).setChecked(false);
            ((CheckBox) v.getTag()).setEnabled(true);
            v.setText("");
            list.set(index,false);
            v.setEnabled(false);
        }
        getCurrentIndex(size);
    }

    private void getCurrentIndex(int size) {
        MyLog.i("我进了getCurrentIndex: "+size);
        for(int i=0;i<size;i++){
            MyLog.i("我进了getCurrentIndex list.get(i) "+i+" : "+list.get(i).booleanValue());
          if(!list.get(i).booleanValue()){
              MyLog.i("判断当前应该索引是: "+i);
              currentIndex=i;
              break;
          }
        }
    }
}
