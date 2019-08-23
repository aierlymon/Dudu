package com.dudu.huodai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dudu.baselib.base.BaseMvpActivity;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.StatusBarUtil;
import com.dudu.huodai.mvp.presenters.StoryPresenter;
import com.dudu.huodai.mvp.view.StoryImpl;
import com.dudu.model.bean.StoryInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoryActivity extends BaseMvpActivity<StoryImpl, StoryPresenter> implements StoryImpl {
    @BindView(R.id.story_state)
    View viewState;

    @BindView(R.id.story_useful)
    View viewUseful;


    @BindView(R.id.story_content)
    View viewConetnt;

    @BindView(R.id.share)
    Button btnShare;

    @BindView(R.id.tx_story_state)
    TextView txStoryState;

    @BindView(R.id.tx_story_userful)
    TextView txStoryUserful;

    @BindView(R.id.story_bigback)
    ImageView imageView;

    @BindView(R.id.tx_story_content)
    TextView txStoryContent;

    @BindView(R.id.parent_content)
    LinearLayout parentContent;

    @BindView(R.id.tx_story_resource)
    TextView txStoryReSource;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_story;
    }

    @Override
    public boolean isUseLayoutRes() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        initRequest(id);
        init();

    }

    private void initRequest(int id) {
        mPresenter.reqestStoryInfo(id);
    }


    private void init() {
        ButterKnife.bind(this);

        //设置故事名称
        ((TextView) viewState.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_state));
        ((TextView) viewUseful.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_userful));
        ((TextView) viewConetnt.findViewById(R.id.tx_title)).setText(getResources().getString(R.string.story_content));
        //设置分享按钮可见
        btnShare.setVisibility(View.VISIBLE);


    }

    @Override
    protected StoryPresenter createPresenter() {
        return new StoryPresenter();
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

    @OnClick({R.id.button_showcontent})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_showcontent:
                v.setVisibility(View.GONE);
                parentContent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void refreshUi(StoryInfo.StoryBean storyBean) {
        txStoryState.setText(storyBean.getDescription());
        txStoryUserful.setText(storyBean.getSignificance());
        txStoryContent.setText(storyBean.getContent());
        txStoryReSource.setText(storyBean.getSource());
        Glide.with(this).load(HttpConstant.PIC_BASE_URL + storyBean.getPicture()).into(imageView);
    }
}
