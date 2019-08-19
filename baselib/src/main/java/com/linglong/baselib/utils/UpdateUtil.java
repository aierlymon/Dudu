package com.linglong.baselib.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.linglong.baselib.http.HttpConstant;
import com.linglong.baselib.http.HttpMethod;
import com.linglong.baselib.http.MovieService;
import com.linglong.model.bean.UpdateBean;
import com.linglong.baselib.http.interrceptorebean.JsDownloadInterceptor;
import com.linglong.baselib.http.listener.JsDownloadListener;
import com.linglong.baselib.widget.UpdateDialog;

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

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class UpdateUtil implements LifecycleObserver {
    public static final int FILE_NOTFOUND_ERROR=0x01;//没有找到地址
    public static final int FILE_IO_ERROR=0x02;//下载过程错误
    public static final int FILE_MD5_ERROR=0x03;//下载后md5错误
    public static final int FILE_DOWNLOAD_ERROR=0x04;//文件下载失败

    private Context context;
    private Retrofit retrofit;
    private JsDownloadListener jsDownloadListener;
    private CompositeDisposable mCompositeDisposable;
    public UpdateUtil(Context context, JsDownloadListener jsDownloadListener) {
        this.jsDownloadListener=jsDownloadListener;
        this.context = context.getApplicationContext();
        //定义下载client
        mCompositeDisposable=new CompositeDisposable();
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(jsDownloadListener);
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


    private UpdateDialog builder;

    private String appPackName;

    public void setAppPackName(String appPackName) {
        this.appPackName = appPackName;
    }

    public boolean checkUpdate(HttpMethod httpMethod) {
        httpMethod.checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(UpdateBean updateBean) {
                        //拿到新的版本号
                        int newVersionCode = updateBean.getVersionCode();
                        int versionCode = PackageUtils.getVersionCode(context);
                        if (newVersionCode > versionCode) {
                            //弹出更新框
                            showDialog(context, updateBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        clearDisposable();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return false;
    }

    /**
     * 开始下载
     * @param url
     * @param file
     * @param subscriber
     */
    private void download(@NonNull String url, final File file, Observer subscriber) {
        retrofit.create(MovieService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> responseBody.byteStream())
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(inputStream -> writeFile(inputStream, file))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

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
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            clearDisposable();
            jsDownloadListener.onFail(FILE_NOTFOUND_ERROR,"FileNotFoundException");
        } catch (IOException e) {
            MyLog.i("写入数据异常");
            clearDisposable();
            jsDownloadListener.onFail(FILE_IO_ERROR,"IOException");
        }

    }





    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        clearDisposable();
        context = null;
    }

    private void showDialog(Context context, UpdateBean updateBean) {
        builder = UpdateDialog.Builder(context)
                .setTitle("是否更新到版本: " + updateBean.getVersionName())
                .setMessage("更新内容为" + updateBean.getUpdateLog() + "\r\n" + "更新大小为: " + updateBean.getTarget_size())
                .setOnConfirmClickListener("确定", view -> {
                    //开始下载文件
                    File file = new File(getApkPath(),updateBean.getApk_name());
                    download(updateBean.getApkUrl(), file, new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCompositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Object o) {
                            MyLog.i("完成了下载");
                            //md5比较，比较一致的情况下触发安装apk
                            File file1=new File(getApkPath(),updateBean.getApk_name());
                            String down_md5=CipherUtils.getFileMD5(file1);
                            if(updateBean.getNew_md5().equals(down_md5)){
                                //跳转安装apk
                                MyLog.i("md5校验成功");
                                //跳转启动apk有问题
                                //判读版本是否在7.0以上
                                File file = new File(getApkPath(), updateBean.getApk_name());
                                MyLog.i("file存在吗: "+file.exists());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                //判读版本是否在7.0以上
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
                            }else{
                                MyLog.i("md5校验失败");
                                clearDisposable();
                                jsDownloadListener.onFail(FILE_MD5_ERROR,"下载的文件的md5值不一样，请重新下载");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.i("连接资源失败了");
                            clearDisposable();
                            jsDownloadListener.onFail(FILE_DOWNLOAD_ERROR,e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                    builder.dismiss();
                })
                .setOnCancelClickListener("取消", view -> {

                    if (builder != null) {
                        builder.dismiss();
                        this.context = null;
                    }
                })
                .build();
        builder.shown();
    }

    private String getApkPath() {
        String directoryPath="";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            MyLog.i("路径: "+Environment.getExternalStorageDirectory().getPath());
            directoryPath =Environment.getExternalStorageDirectory().getPath();
        }else{//没外部存储就使用内部存储
            MyLog.i("路径: "+context.getFilesDir());
            directoryPath=context.getFilesDir()+File.separator;
        }
        File file = new File(directoryPath);
        if(!file.exists()){//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    public void clearDisposable(){
        if(mCompositeDisposable!=null){
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }


    //测试工具类用的
    public void testUpdate(Context context, UpdateBean updateBean) {
        builder = UpdateDialog.Builder(context)
                .setTitle("是否更新到版本: " + updateBean.getVersionName())
                .setMessage("更新内容为" + updateBean.getUpdateLog() + "\r\n" + "更新大小为: " + updateBean.getTarget_size())
                .setOnConfirmClickListener("确定", view -> {
                    //开始下载文件
                    File file = new File(getApkPath(),updateBean.getApk_name());
                    download(updateBean.getApkUrl(), file, new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCompositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Object o) {
                            MyLog.i("完成了下载");
                            //md5比较，比较一致的情况下触发安装apk
                            File file1=new File(getApkPath(),updateBean.getApk_name());
                            String down_md5=CipherUtils.getFileMD5(file1);
                            if(updateBean.getNew_md5().equals(down_md5)){
                                //跳转安装apk
                                MyLog.i("md5校验成功");
                                //跳转启动apk有问题
                                //判读版本是否在7.0以上
                                File file = new File(getApkPath(), updateBean.getApk_name());
                                MyLog.i("file存在吗: "+file.exists());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //判读版本是否在7.0以上
                                if (Build.VERSION.SDK_INT >= 24) {
                                    //provider authorities
                                    MyLog.i("provider name: "+context.getPackageName()+".fileprovider");
                                    Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
                                    //Granting Temporary Permissions to a URI
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                } else {
                                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                }
                                jsDownloadListener.onDownSuccess(intent);
                            }else{
                                MyLog.i("md5校验失败");
                                clearDisposable();
                                jsDownloadListener.onFail(FILE_MD5_ERROR,"下载的文件的md5值不一样，请重新下载");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            MyLog.i("连接资源失败了");
                            clearDisposable();
                            jsDownloadListener.onFail(FILE_DOWNLOAD_ERROR,e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                    builder.dismiss();
                })
                .setOnCancelClickListener("取消", view -> {

                    if (builder != null) {
                        builder.dismiss();
                        this.context = null;
                    }
                })
                .build();
        builder.shown();
    }
}
