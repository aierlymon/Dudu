package com.linglong.huodai.mvp.view;

import android.content.Intent;

import com.linglong.baselib.mvp.IView;

public interface MainViewImpl extends IView {
    void statrUpdateProgress();

    void onUpdateProgress(int progress);

    void onUpdateFail(int errorInfo, String info);

    void onUpdateSuccess(Intent intent);
}
