package com.dudu.huodai.mvp.view;

import com.dudu.baselib.mvp.IView;
import com.dudu.model.bean.StoryInfo;

public interface StoryImpl extends IView {
    void refreshUi(StoryInfo.StoryBean storyBean);
}
