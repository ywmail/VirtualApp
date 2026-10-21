/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
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
package com.kook.network.creator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kook.network.https.HttpsUtils;
import com.kook.network.interceptors.AuthIntercepter;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FileUploadCreator {
    private static final int DEFAULT_TIMEOUT = 10;
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder().addInterceptor((Interceptor)new AuthIntercepter.Builder().getTag().build()).sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager()).connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).build();

    public static Retrofit getRetrofitClient(String url) {
        Retrofit retrofit = new Retrofit.Builder().client(OK_HTTP_CLIENT).addConverterFactory((Converter.Factory)GsonConverterFactory.create((Gson)new GsonBuilder().create())).addCallAdapterFactory((CallAdapter.Factory)RxJava2CallAdapterFactory.create()).baseUrl(url).build();
        return retrofit;
    }
}

