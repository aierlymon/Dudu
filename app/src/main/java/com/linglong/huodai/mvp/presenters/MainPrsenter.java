package com.linglong.huodai.mvp.presenters;

import android.app.Activity;
import android.content.Intent;

import com.linglong.baselib.http.listener.JsDownloadListener;
import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.baselib.utils.MyLog;
import com.linglong.baselib.utils.UpdateUtil;
import com.linglong.huodai.mvp.view.MainViewImpl;
import com.linglong.model.bean.UpdateBean;

public class MainPrsenter extends BasePresenter<MainViewImpl> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    public void showError(String msg) {
        getView().showError("连接错误: " + msg);
    }

    private UpdateUtil updateUtil;

    public void checkUpdate(Activity context){
        updateUtil = new UpdateUtil(context, new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {
                MyLog.i("我已经来到了开始下载了");
                getView().statrUpdateProgress();
            }

            @Override
            public void onProgress(int progress) {
                getView().onUpdateProgress(progress);

            }

            @Override
            public void onFail(int errorType, String errorInfo) {
                getView().onUpdateFail(errorType,errorInfo);
            }

            @Override
            public void onDownSuccess(Intent intent) {
                getView().onUpdateSuccess(intent);

            }


        });

        UpdateBean updateBean = new UpdateBean();
        updateBean.setApk_name("huodai.apk");
        updateBean.setApkUrl("http://192.168.1.103:8080/huodai.apk");
        updateBean.setVersionCode(2);
        updateBean.setNew_md5("cbbc391b297986987a5811b9f9ff8ed6");
        updateBean.setTarget_size("3M");
        updateBean.setVersionName("2.0.0");
        updateBean.setUpdateLog("测试用例");

        //updateUtil.setAppPackName("com.example.huodai");
       // updateUtil.testUpdate(context,updateBean);
    }

    public void cancelIUpdate(){
        if(updateUtil!=null){
            updateUtil.clearDisposable();
        }
    }
}
