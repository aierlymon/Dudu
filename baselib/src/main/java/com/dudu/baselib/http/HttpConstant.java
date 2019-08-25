package com.dudu.baselib.http;

import android.content.Context;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data http的一些常量值
 */
public abstract class HttpConstant {
    public static int DEFAULT_TIME_OUT = 8;//超时时间 单位（s）秒

    //https://www.dudugushi.com/anonymous/story/recommendation/
    public static String BASE_URL = "https://www.dudugushi.com/";
    // public static String BASE_URL = "http://47.112.217.160/";

    //http://tuershiting.com/api/
    // public static String BASE_API_URL = BASE_URL+"api/";

    public static String BASE_API_URL = BASE_URL + "anonymous/";


    public static String PIC_BASE_URL="https://dudu-image-1253462148.image.myqcloud.com/story/";

    public static Context context;

    public static final String cacheFileName = "httpcaches";

    public static String MINE_BASE_URL = "http://ihoufeng.com/app/html/apphtml/";




    //http://tuershiting.com/api/banners?filter[where][open]=true
}
