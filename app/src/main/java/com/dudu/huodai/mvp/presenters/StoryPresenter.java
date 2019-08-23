package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.model.DDHomeFRBodyHolder;
import com.dudu.huodai.mvp.model.HomeFRAdvertHolder;
import com.dudu.huodai.mvp.view.StoryImpl;
import com.dudu.model.bean.HttpResult;
import com.dudu.model.bean.StoryInfo;
import com.dudu.model.bean.StoryTable;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StoryPresenter extends BasePresenter<StoryImpl> {
    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    //请求道数据之后更新ui
    public void reqestStoryInfo(int id) {
        HttpMethod.getInstance().requestStotyInfo(id)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<StoryInfo>(this) {
                    @Override
                    public void onSuccess(StoryInfo httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            MyLog.i("我来到了请求DD内容: " + httpResult.toString());
                            getView().refreshUi(httpResult.getStory());
                        } else {
                            showError(httpResult.getResult());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        showError(String.valueOf(e.getMessage()));
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }
}
