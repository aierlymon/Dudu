package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.model.DDHomeFRMenuHolder;
import com.dudu.huodai.mvp.view.SubjectImpl;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.HttpResult;
import com.dudu.model.bean.SubjectTable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * createBy ${huanghao}
 * on 2019/8/26
 */
public class SubjectPresenter extends BasePresenter<SubjectImpl> {

    List<BaseMulDataModel> list = new ArrayList<>();

    public List<BaseMulDataModel> getList() {
        return list;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    //请求清单选项卡
    public void requestSubject() {
        HttpMethod.getInstance().requestSubject()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<List<SubjectTable>>>(this) {
                    @Override
                    public void onSuccess(HttpResult<List<SubjectTable>> httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            MyLog.i("requestMenuc成功了");
                            DDHomeFRMenuHolder homeFRMenuHolder = new DDHomeFRMenuHolder();
                            //这个地方因为和商品的筛选是动态的，所以这个也要是动态

                            homeFRMenuHolder.setLoanCategoriesBean(httpResult.getData());
                            list.clear();
                            list.add(homeFRMenuHolder);
                            getView().refreshHome(list);
                            MyLog.i("list.size: "+list.size());
                        } else {
                            showError(httpResult.getMsg() + ":" + httpResult.getStatusCode());
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


    //请求清单选项卡
    public void requestSeries() {
        HttpMethod.getInstance().requestSeries()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<List<SubjectTable>>>(this) {
                    @Override
                    public void onSuccess(HttpResult<List<SubjectTable>> httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            MyLog.i("requestMenuc成功了");
                            DDHomeFRMenuHolder homeFRMenuHolder = new DDHomeFRMenuHolder();
                            //这个地方因为和商品的筛选是动态的，所以这个也要是动态

                            homeFRMenuHolder.setLoanCategoriesBean(httpResult.getData());
                            list.clear();
                            list.add(homeFRMenuHolder);
                            getView().refreshHome(list);
                            MyLog.i("list.size: "+list.size());
                        } else {
                            showError(httpResult.getMsg() + ":" + httpResult.getStatusCode());
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
