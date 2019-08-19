package com.linglong.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.http.MovieService;
import com.linglong.baselib.http.interrceptorebean.JsDownloadInterceptor;
import com.linglong.baselib.http.listener.JsDownloadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownUtil {
    public static final int FILE_NOTFOUND_ERROR = 0x01;//没有找到地址
    public static final int FILE_IO_ERROR = 0x02;//下载过程错误
    public static final int FILE_DOWNLOAD_ERROR = 0x04;//文件下载失败

    private Retrofit retrofit;
    private CompositeDisposable mCompositeDisposable;
    private JsDownloadListener jsDownloadListener;
    private Context context;

    public DownUtil(Context context, JsDownloadListener jsDownloadListener) {
        this.jsDownloadListener = jsDownloadListener;
        this.context = context;
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(jsDownloadListener);
        mCompositeDisposable = new CompositeDisposable();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(HttpConstant.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void download(@NonNull String url, final File file) {
        retrofit.create(MovieService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> responseBody.byteStream())
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(inputStream -> writeFile(inputStream, file))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InputStream>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= 24) {
                            //provider authorities
                            Uri apkUri = FileProvider.getUriForFile(context, "com.linglong.huodai.fileprovider", file);
                            //Granting Temporary Permissions to a URI
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        } else {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        }
                        jsDownloadListener.onDownSuccess(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        jsDownloadListener.onFail(FILE_DOWNLOAD_ERROR, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            clearDisposable();
            jsDownloadListener.onFail(FILE_NOTFOUND_ERROR, "FileNotFoundException");
        } catch (IOException e) {
            MyLog.i("写入数据异常");
            clearDisposable();
            jsDownloadListener.onFail(FILE_IO_ERROR, "IOException");
        }

    }

    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }


}
