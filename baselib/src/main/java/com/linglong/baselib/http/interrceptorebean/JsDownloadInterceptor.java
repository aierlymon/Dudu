package com.linglong.baselib.http.interrceptorebean;

import com.linglong.baselib.http.JsResponseBody;
import com.linglong.baselib.http.listener.JsDownloadListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class JsDownloadInterceptor implements Interceptor {
    private JsDownloadListener downloadListener;
    public JsDownloadInterceptor(JsDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}
