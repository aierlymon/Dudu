package com.dudu.huodai.mvp.model;

import android.view.View;

import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;

public class HomeOtherAdvertHolder extends BaseMulDataModel {
  /*  private TTFeedAd ttFeedAd;

    public TTFeedAd getTtFeedAd() {
        return ttFeedAd;
    }

    public void setTtFeedAd(TTFeedAd ttFeedAd) {
        this.ttFeedAd = ttFeedAd;
    }

    public HomeFRAdvertHolder(TTFeedAd ttFeedAd) {
        this.ttFeedAd = ttFeedAd;
    }*/


  private View view;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public HomeOtherAdvertHolder(View view) {
        this.view = view;
    }
}
