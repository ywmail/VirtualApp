/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  io.reactivex.android.schedulers.AndroidSchedulers
 *  io.reactivex.functions.Consumer
 *  io.reactivex.schedulers.Schedulers
 *  okhttp3.Interceptor
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  retrofit2.CallAdapter$Factory
 *  retrofit2.Converter$Factory
 *  retrofit2.Retrofit
 *  retrofit2.Retrofit$Builder
 *  retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
 *  retrofit2.converter.gson.GsonConverterFactory
 */
package com.kook.network.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.creator.IApiService;
import com.kook.network.exception.ErrorAction;
import com.kook.network.file.IDownloadListener;
import com.kook.network.interceptors.DownloadInterceptor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileDownloadService {
    private static FileDownloadService mInstance = null;
    private static final int DEFAULT_TIMEOUT = 5;
    private final IApiService mApiService;
    private final IDownloadListener mListener;

    private FileDownloadService(IDownloadListener listener) {
        this.mListener = listener;
        OkHttpClient OkhttpClient = new OkHttpClient.Builder().addInterceptor((Interceptor)new DownloadInterceptor(listener)).connectTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS).writeTimeout(5L, TimeUnit.SECONDS).build();
        Retrofit retrofitClient = new Retrofit.Builder().client(OkhttpClient).addConverterFactory((Converter.Factory)GsonConverterFactory.create((Gson)new GsonBuilder().create())).addCallAdapterFactory((CallAdapter.Factory)RxJava2CallAdapterFactory.create()).baseUrl("http://www.kookcore.site:8038/dataserver/").build();
        this.mApiService = (IApiService)retrofitClient.create(IApiService.class);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static FileDownloadService getInstance(IDownloadListener listener) {
        if (null != mInstance) return mInstance;
        Class<FileDownloadService> clazz = FileDownloadService.class;
        synchronized (FileDownloadService.class) {
            if (null != mInstance) return mInstance;
            mInstance = new FileDownloadService(listener);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return mInstance;
        }
    }

    public void download(String url, String filePath) {
        this.mApiService.download(url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(responseBody -> responseBody.byteStream()).doOnError((Consumer)new ErrorAction()).doOnNext(inputStream -> this.saveFile((InputStream)inputStream, filePath)).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
            if (this.mListener != null) {
                this.mListener.onDownloadSuccess();
                HVLog.d("成功处理");
            }
        }, throwable -> HVLog.d(" 错误处理"));
    }

    public void download(String url, Map<String, String> headers, String filePath) {
        this.mApiService.download(url, headers).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(responseBody -> responseBody.byteStream()).doOnError((Consumer)new ErrorAction()).doOnNext(inputStream -> this.saveFile((InputStream)inputStream, filePath)).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
            if (this.mListener != null) {
                this.mListener.onDownloadSuccess();
                HVLog.d("成功处理");
            }
        }, throwable -> HVLog.d(" 错误处理"));
    }

    private void saveFile(InputStream inputString, String filePath) {
        block4: {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                int len;
                file.createNewFile();
                fos = new FileOutputStream(file);
                byte[] b = new byte[10240];
                while ((len = inputString.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                inputString.close();
                fos.close();
                this.mListener.onDownloadSuccess();
            }
            catch (Exception exception) {
                HVLog.e("异常信息:" + filePath);
                this.mListener.onDownloadFail(exception);
                if (!file.exists()) break block4;
                file.deleteOnExit();
            }
        }
    }
}

