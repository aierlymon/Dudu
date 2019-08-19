package com.linglong.huodai.widget.myGlide;

import android.content.Context;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.EmptySignature;
import com.linglong.baselib.utils.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * createBy ${huanghao}
 * on 2019/7/27
 */
public class GlideUtil {
    static File file;
    public static File getCacheFile2(String url, Context context) {
        DataCacheKey dataCacheKey = new DataCacheKey(new GlideUrl(url), EmptySignature.obtain());
        SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
        String safeKey = safeKeyGenerator.getSafeKey(dataCacheKey);
        try {
            int cacheSize = 100 * 1000 * 1000;
            file = new File(context.getApplicationContext().getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);

            DiskLruCache diskLruCache = DiskLruCache.open(file, 1, 1, cacheSize);
            DiskLruCache.Value value = diskLruCache.get(safeKey);

            MyLog.i("我找到了GlideUtil file: " + file.exists() + "  value: " + value + "  file path: " + file.toString());
            if (value != null) {
                MyLog.i("缓存不为null");
                return value.getFile(0);
            } else {
                //下载图片到本地缓存
                new Thread(() -> {
                    try {
                        DiskLruCache.Editor editor = diskLruCache.edit(safeKey);
                        if (editor != null) {
                            file = editor.getFile(0);
                            if (downloadUrlToFile(url, file)) {
                                if (file.exists()) {
                                    editor.commit();
                                } else {
                                    editor.abort();
                                }
                            } else {
                                editor.abort();
                            }
                        }
                        diskLruCache.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static boolean downloadUrlToFile(String urlString, File file) throws IOException {
        OutputStream outputStream = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        InputStream inputStream = null;
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            outputStream = new FileOutputStream(file);
            Response response = call.execute();
            inputStream = response.body().byteStream();
            byte[] array = new byte[1024]; // 定义数组，传递流之间的数据
            int count = 0;
            outputStream = new FileOutputStream(file);
            while ((count = inputStream.read(array)) != -1) {
                outputStream.write(array, 0, count);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
        return false;
    }


}
