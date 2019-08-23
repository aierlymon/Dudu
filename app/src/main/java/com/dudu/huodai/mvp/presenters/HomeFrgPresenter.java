package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.model.DDHomeFRBodyHolder;
import com.dudu.huodai.mvp.model.DDHomeFRMenuHolder;
import com.dudu.huodai.mvp.model.HomeFRAdvertHolder;
import com.dudu.huodai.mvp.model.HomeFRBannerHolder;
import com.dudu.huodai.mvp.model.postbean.BannerBean;
import com.dudu.huodai.mvp.model.postbean.RecordBean;
import com.dudu.huodai.mvp.view.HomeFrgViewImpl;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.HttpResult;
import com.dudu.model.bean.NewHomeBannerBean;
import com.dudu.model.bean.NewHomeBodyBean;
import com.dudu.model.bean.StoryTable;
import com.dudu.model.bean.SubjectTable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFrgPresenter extends BasePresenter<HomeFrgViewImpl> {

    private int count=4;

    @Override
    public void showError(String msg) {
        getView().showError("连接错误: " + msg);
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
     public void applyRecord(RecordBean recordBean){
        apply(recordBean.getLoanProductId(),recordBean.getUserId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reservedUv(BannerBean bannerBean){
        reservedUv(bannerBean.getBannerId(),bannerBean.getUserId(),bannerBean.getUrl());
    }

    List<BaseMulDataModel> list = new ArrayList<>();

    public List<BaseMulDataModel> getList() {
        return list;
    }

    //这个是banner头部的请求，就是轮播图
    public void requestHead() {
        HttpMethod.getInstance().loadHomeBanner()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<NewHomeBannerBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<NewHomeBannerBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            NewHomeBannerBean homeHeadBean = httpResult.getData();
                            HomeFRBannerHolder homeFRBannerHolder = new HomeFRBannerHolder();
                            homeFRBannerHolder.setNewHomeBannerBean(homeHeadBean);//这个预留出来的page,pageCout而已，其实都一样最好的
                            if(list.size()>=count){
                                list.clear();
                            }

                            list.add(homeFRBannerHolder);

                            MyLog.i("list.size: "+list.size());
                        } else {
                            showError(httpResult.getMsg() + ":" + httpResult.getStatusCode());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        MyLog.i("失败了: " + e.getMessage());
                        //测试用的
                        /*---------------------------------------------------*/
                        HomeFRBannerHolder homeFRBannerHolder = new HomeFRBannerHolder();
                        homeFRBannerHolder.setNewHomeBannerBean(null);//这个预留出来的page,pageCout而已，其实都一样最好的
                        if(list.size()>=count){
                            list.clear();
                        }
                        list.add(homeFRBannerHolder);
                        /*---------------------------------------------------*/
                        showError(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    public void apply(int loanProductId, int id ){
        HttpMethod.getInstance().applyRecords(loanProductId,id)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<String>>(this) {
                    @Override
                    public void onSuccess(HttpResult<String> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
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

    public void  reservedUv(int bannerId,int userId,String url){
        HttpMethod.getInstance().reservedUv(bannerId,userId,url)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<String>>(this) {
                    @Override
                    public void onSuccess(HttpResult<String> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
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
    public void requestMenu() {
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
                            if(list.size()>=count){
                                list.clear();
                            }
                            list.add(homeFRMenuHolder);
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

    //请求Body内容的
    public void requestBody() {
        HttpMethod.getInstance().requestStotyTable()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<List<StoryTable>>>(this) {
                    @Override
                    public void onSuccess(HttpResult<List<StoryTable>> httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            MyLog.i("我来到了请求DD内容: "+httpResult.toString());
                            DDHomeFRBodyHolder homeFRBodyHolder = new DDHomeFRBodyHolder();
                            homeFRBodyHolder.setHomeBodyBeanList(httpResult.getData());
                            if(list.size()>=count){
                                list.clear();
                            }
                            list.add(new HomeFRAdvertHolder());
                            list.add(homeFRBodyHolder);
                            MyLog.i("list.size: "+list.size());
                            getView().refreshHome(list);
                        } else {
                            showError(httpResult.getResult() + ":" + httpResult.getStatusCode());
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


    public void requestBodyPage(int id,int min,int max,int page){

        HttpMethod.getInstance().loadBodyMintoMaxToPage(id,min,max,page)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<NewHomeBodyBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<NewHomeBodyBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {

                        } else {
                            showError(httpResult.getResult()+":"+httpResult.getStatusCode());
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

    public void clear() {
        list.clear();
    }
}
