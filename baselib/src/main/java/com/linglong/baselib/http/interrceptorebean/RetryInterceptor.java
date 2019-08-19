package com.linglong.baselib.http.interrceptorebean;

import com.linglong.baselib.utils.MyLog;

import java.io.IOException;
import java.io.InterruptedIOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * createBy ${huanghao}
 * on 2019/7/20
 */
public class RetryInterceptor implements Interceptor {
    public int executionCount;      // 最大重试次数
    private long retryInterval;     // 重试的间隔


    public RetryInterceptor(int executionCount, long retryInterval) {
        this.executionCount = executionCount;
        this.retryInterval = retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        MyLog.i("RetryInterceptor，请求前");
        Response response = chain.proceed(request);
        int retryNum = 0;
        while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
            MyLog.i("服务器重联中");
            final long nextInterval = retryInterval;
            try {
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            }
            retryNum++;
            response = chain.proceed(request);
        }
        return response;
    }


}