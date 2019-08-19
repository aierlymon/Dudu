package com.linglong.baselib.http;

import com.linglong.model.bean.HistoryBean;
import com.linglong.model.bean.HttpResult;
import com.linglong.baselib.http.interrceptorebean.LoggingInterceptor;
import com.linglong.baselib.http.interrceptorebean.RetryInterceptor;
import com.linglong.model.bean.LoginCallBackBean;
import com.linglong.model.bean.NewHomeBannerBean;
import com.linglong.model.bean.NewHomeBodyBean;
import com.linglong.model.bean.NewHomeMenuBean;
import com.linglong.model.bean.RecommandStateBean;
import com.linglong.model.bean.SplashBean;
import com.linglong.model.bean.UpdateBean;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.linglong.baselib.http.HttpConstant.DEFAULT_TIME_OUT;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 以后所有请求的调用方法写在这里,然后所有请求路径和方式放到MovieService,由一个MovieService同意调用
 */
public class HttpMethod {
    public static Retrofit mRetrofit;
    //以后所有请求的调用方法写在这里,然后所有请求路径和方式放到MovieService,由一个MovieService同意调用
    private MovieService mMovieService;

    private static HttpMethod httpMethods;

    //获取单例
    public static HttpMethod getInstance() {
        if (httpMethods == null) {
            httpMethods = new HttpMethod();
        }
        return httpMethods;
    }


    public HttpMethod() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接 超时时间
            builder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
            builder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//读操作 超时时间


            File cacheFile = new File(HttpConstant.context.getCacheDir(), HttpConstant.cacheFileName);//缓存文件路径
            // if(!cacheFile.exists())cacheFile.mkdirs();

            int cacheSize = 10 * 1024 * 1024;//设置缓存文件大小为10M
            Cache cache = new Cache(cacheFile, cacheSize);
            builder.cache(cache);

            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);




            //缓存拦截器
            builder.addInterceptor(new LoggingInterceptor());
            // 错误重连拦截器
            builder.addInterceptor(new RetryInterceptor(3, DEFAULT_TIME_OUT));
            //公共参数拦截器


         /*   if (BuildConfig.DEBUG) {
            }*/
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    //设置基础地址
                    .baseUrl(HttpConstant.BASE_API_URL)
                    //这个把返回的数据转换为gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //这个为了支持rxjva
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

            mMovieService = mRetrofit.create(MovieService.class);
        }

    }


    //正式更新用的，但是url地址到时候自己改
    public Observable<UpdateBean> checkUpdate() {
        return mMovieService.checkUpdate();
    }


    public Observable<HttpResult<NewHomeBannerBean>> loadHomeBanner() {
        return mMovieService.loadHomeBanner();
    }

    public Observable<HttpResult<NewHomeMenuBean>> loadHomeMenu() {
        return mMovieService.loadHomeMenu(1);
    }



    public Observable<HttpResult<NewHomeBodyBean>> loadBody() {
        return mMovieService.loadHomeBody(1);
    }

    public Observable<HttpResult<NewHomeBodyBean>> loadBody(int id) {
        return mMovieService.loadHomeBody(1,id);
    }

    //最小金额不大于
    public Observable<HttpResult<NewHomeBodyBean>> loadBodyLimitLit(int id,int limit) {
        return mMovieService.loadHomeBodyLimitLlte(1,id,limit);
    }

    //最小金额不小于
    public Observable<HttpResult<NewHomeBodyBean>> loadBodyLimitHigh(int id,int max) {
        return mMovieService.loadHomeBodyLimitLgte(1,id,max);
    }

    //最小到最大值(后来值是反过来的)
    public Observable<HttpResult<NewHomeBodyBean>> loadBodyMintoMax(int id,int min,int max) {
        return mMovieService.loadHomeBodyLimToLgt(1,id,min,max);
    }


    //加一个页数加载
    public Observable<HttpResult<NewHomeBodyBean>> loadBodyMintoMaxToPage(int id,int min,int max,int page) {
        return mMovieService.loadHomeBodyLimToLgtToPage(1,id,min,max,page);
    }


    public Observable<HttpResult<JsonObject>> getVerificationCode(String number) {
        JSONObject root = new JSONObject();
        try {
            root.put("phone", number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString());
        return mMovieService.getVerificationCode(requestBody);
    }

    public Observable<HttpResult<LoginCallBackBean>> requestLogin(String number, String code) {
        JSONObject root = new JSONObject();
        try {
            root.put("code", code);
            root.put("phone", number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString());
        return mMovieService.requestLogin(requestBody);
    }

    public Observable<HttpResult<String>> applyRecords(int loanProductId, int id) {
        return mMovieService.applyRecords(loanProductId,id);
    }

    public Observable<HttpResult<String>> reservedUv(int loanProductId, int id,String url) {
        return mMovieService.reservedUv(loanProductId,id,url);
    }

    public Observable<SplashBean> loadSplash() {
        return mMovieService.loadSplash();
    }

    public Observable<RecommandStateBean> loadRecomStaet(){
        return mMovieService.loadRecomnStaet();
    }

    public Observable<HttpResult<String>> editUserMsg(String id,String phoneNum,String name,String whoNum){
        JSONObject root = new JSONObject();
        try {
            root.put("userId", id);
            root.put("phone", phoneNum);
            root.put("name", name);
            root.put("card", whoNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString());
        return mMovieService.editUserMsg(requestBody);
    }

    public  Observable<HttpResult<List<HistoryBean>>> userApplyRecordsList(int id){
        return mMovieService.userApplyRecordsList(1,id);
    }
}
