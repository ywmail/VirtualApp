/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.FormBody
 *  okhttp3.FormBody$Builder
 *  okhttp3.Headers
 *  okhttp3.Headers$Builder
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 */
package com.carlos.common.download;

import com.kook.librelease.StringFog;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private OkHttpClient mOkHttpClient;
    private static HttpUtil mInstance;
    private static final long CONNECT_TIMEOUT = 60L;
    private static final long READ_TIMEOUT = 60L;
    private static final long WRITE_TIMEOUT = 60L;
    static Headers.Builder builder;

    public void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) throws IOException {
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex).addHeader("Accept-Encoding", "identity").url(url).build();
        this.doAsync(request, callback);
    }

    public void getContentLength(String url, Callback callback) throws IOException {
        Headers headers = builder.set("referer", url).build();
        FormBody formBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).headers(headers).build();
        this.doAsync(request, callback);
    }

    private void doAsync(Request request, Callback callback) throws IOException {
        Call call = this.mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    private Response doSync(Request request) throws IOException {
        Call call = this.mOkHttpClient.newCall(request);
        return call.execute();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static HttpUtil getInstance() {
        if (null != mInstance) return mInstance;
        Class<HttpUtil> clazz = HttpUtil.class;
        synchronized (HttpUtil.class) {
            if (null != mInstance) return mInstance;
            mInstance = new HttpUtil();
            // ** MonitorExit[var0] (shouldn't be in output)
            return mInstance;
        }
    }

    public HttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS);
        this.mOkHttpClient = builder.build();
    }

    static {
        builder = new Headers.Builder();
        builder.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        builder.add("Accept-Encoding", "gzip, deflate");
        builder.add("Upgrade-Insecure-Requests", "1");
        builder.add("accept-language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6,ja;q=0.5");
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
    }
}

