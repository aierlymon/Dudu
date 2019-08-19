package com.linglong.huodai.mvp.presenters;

import com.linglong.baselib.http.HttpMethod;
import com.linglong.baselib.http.myrxsubcribe.MySubscriber;
import com.linglong.baselib.mvp.BasePresenter;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.mvp.model.HomeFRBannerHolder;
import com.linglong.huodai.mvp.model.HomeFRBodyHolder;
import com.linglong.huodai.mvp.model.HomeFRMenuHolder;
import com.linglong.huodai.mvp.model.postbean.BannerBean;
import com.linglong.huodai.mvp.model.postbean.RecordBean;
import com.linglong.huodai.mvp.view.HomeFrgViewImpl;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;
import com.linglong.model.bean.HttpResult;
import com.linglong.model.bean.NewHomeBannerBean;
import com.linglong.model.bean.NewHomeBodyBean;
import com.linglong.model.bean.NewHomeMenuBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFrgPresenter extends BasePresenter<HomeFrgViewImpl> {


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
                            if(list.size()>=3){
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
        HttpMethod.getInstance().loadHomeMenu()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<NewHomeMenuBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<NewHomeMenuBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            MyLog.i("requestMenuc成功了");
                            HomeFRMenuHolder homeFRMenuHolder = new HomeFRMenuHolder();
                            //这个地方因为和商品的筛选是动态的，所以这个也要是动态

                            homeFRMenuHolder.setLoanCategoriesBean(httpResult.getData().getLoanCategories());
                            homeFRMenuHolder.setNewHomeMenuBean(httpResult.getData());//这个预留出来的page,pageCout而已，其实都只要上面那个就够了这个
                            if(list.size()>=3){
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
        HttpMethod.getInstance().loadBody()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<NewHomeBodyBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<NewHomeBodyBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            HomeFRBodyHolder homeFRBodyHolder = new HomeFRBodyHolder();
                            homeFRBodyHolder.setHomeBodyBeanList(httpResult.getData().getLoanProduct());
                            if(list.size()>=3){
                                list.clear();
                            }
                            list.add(homeFRBodyHolder);
                            MyLog.i("list.size: "+list.size());
                            getView().refreshHome(list);
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

    public void requestBodyPage(int id,int min,int max,int page){

        HttpMethod.getInstance().loadBodyMintoMaxToPage(id,min,max,page)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<HttpResult<NewHomeBodyBean>>(this) {
                    @Override
                    public void onSuccess(HttpResult<NewHomeBodyBean> httpResult) {
                        if (httpResult.getStatusCode() == 200) {
                            HomeFRBodyHolder homeFRBodyHolder = new HomeFRBodyHolder();
                            homeFRBodyHolder.setHomeBodyBeanList(httpResult.getData().getLoanProduct());
                            for(int i=0;i<list.size();i++){
                                if(list.get(i) instanceof HomeFRBodyHolder){
                                    ((HomeFRBodyHolder) list.get(i)).getHomeBodyBeanList().addAll(homeFRBodyHolder.getHomeBodyBeanList());
                                }
                            }
                            getView().addPage(list);
                            MyLog.i("requestBody(x) end : "+list.size());
                        } else {
                            showError(httpResult.getMsg()+":"+httpResult.getStatusCode());
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
