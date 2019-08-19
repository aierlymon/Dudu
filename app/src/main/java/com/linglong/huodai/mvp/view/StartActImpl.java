package com.linglong.huodai.mvp.view;

import com.linglong.baselib.mvp.IView;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/7/27
 */
public interface StartActImpl extends IView {
    void startSplash(List<String> urls);

    void startMain();

    void requestComplice();
}
