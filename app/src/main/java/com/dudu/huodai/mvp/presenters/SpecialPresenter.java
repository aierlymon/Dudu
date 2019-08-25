package com.dudu.huodai.mvp.presenters;

import com.dudu.baselib.http.HttpMethod;
import com.dudu.baselib.http.myrxsubcribe.MySubscriber;
import com.dudu.baselib.mvp.BasePresenter;
import com.dudu.baselib.utils.MyLog;
import com.dudu.huodai.mvp.model.DDHomeFRBodyHolder;
import com.dudu.huodai.mvp.model.DDHomeFRMenuHolder;
import com.dudu.huodai.mvp.model.HomeFRAdvertHolder;
import com.dudu.huodai.mvp.model.HomeFRBannerHolder;
import com.dudu.huodai.mvp.model.HomeFRBigBackHoder;
import com.dudu.huodai.mvp.model.HomeFRBodyHolder;
import com.dudu.huodai.mvp.model.HomeFRMenuHolder;
import com.dudu.huodai.mvp.view.SpecialImpl;
import com.dudu.huodai.ui.adapter.base.BaseMulDataModel;
import com.dudu.model.bean.HttpResult;
import com.dudu.model.bean.NewHomeBannerBean;
import com.dudu.model.bean.NewHomeBodyBean;
import com.dudu.model.bean.NewHomeMenuBean;
import com.dudu.model.bean.StoryTable;
import com.dudu.model.bean.SubjectInfo;
import com.dudu.model.bean.SubjectTable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SpecialPresenter extends BasePresenter<SpecialImpl> {
    private int count=4;

    @Override
    public void showError(String msg) {
        getView().showError("连接错误: " + msg);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
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
                            HomeFRBigBackHoder homeFRBigBackHoder = new HomeFRBigBackHoder();
                            if(list.size()>=count){
                                list.clear();
                            }

                            list.add(homeFRBigBackHoder);

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
    //请求Body内容的
    public void requestBody(int id,int page,int pagecount) {
        HttpMethod.getInstance().requestSubjectInfo(id,page,pagecount)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<SubjectInfo>(this) {
                    @Override
                    public void onSuccess(SubjectInfo httpResult) {
                        if (httpResult.getResult().equals("success")) {
                            MyLog.i("我来到了请求DD内容: "+httpResult.toString());
                            DDHomeFRBodyHolder homeFRBodyHolder = new DDHomeFRBodyHolder();
                            List<StoryTable> tableArrayList=new ArrayList<>();
                            for(int i=0;i<httpResult.getData().size();i++){
                                String str=new Gson().toJson(httpResult.getData().get(i));
                                MyLog.i("json： "+str);
                                tableArrayList.add(new Gson().fromJson(str,StoryTable.class));
                            }
                            MyLog.i("tableArrayList.size: "+tableArrayList.size());
                            homeFRBodyHolder.setHomeBodyBeanList(tableArrayList);
                            if(list.size()>=count){
                                list.clear();
                            }
                            list.add(new HomeFRAdvertHolder());
                            list.add(homeFRBodyHolder);
                            MyLog.i("list.size: "+list.size());
                            getView().refreshHome(list);
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


    /*
    *    List<SubjectInfo.DataBean> subscribe=httpResult.getData();
                            List<StoryTable> storyTables=new ArrayList<>();
                            for(int i=0;i<subscribe.size();i++){
                                StoryTable storyTable=new StoryTable();
                                storyTable.setAudio(subscribe.get(i).getAudio());
                                storyTable.setGuide(subscribe.get(i).getGuide());
                                storyTable.setId(subscribe.get(i).getId());
                                storyTable.setLike_num(subscribe.get(i).getLike_num());
                                storyTable.setPicture(subscribe.get(i).getPicture());
                                List<SubjectInfo.DataBean.TagsBean> tagsBeans=subscribe.get(i).getTags();
                                List<StoryTable.TagsBean> tagsBeanList=new ArrayList<>();
                                for(int j=0;j<tagsBeans.size();j++){
                                    StoryTable.TagsBean tagsBean=new StoryTable.TagsBean();
                                    tagsBean.setName(tagsBeans.get(i).getName());
                                    tagsBean.setId(tagsBeans.get(i).getId());
                                    tagsBeanList.add(tagsBean);
                                }
                                storyTable.setTags(tagsBeanList);
                                storyTable.setTitle(subscribe.get(i).getTitle());
                                storyTables.add(storyTable);
                            }
    * */

}
