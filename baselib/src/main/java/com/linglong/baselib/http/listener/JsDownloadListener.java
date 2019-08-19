package com.linglong.baselib.http.listener;

import android.content.Intent;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public interface JsDownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(int errorType,String errorInfo);
    void onDownSuccess(Intent intent);
}
