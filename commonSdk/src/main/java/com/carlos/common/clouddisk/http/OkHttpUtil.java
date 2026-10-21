/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.CookieJar
 *  okhttp3.FormBody
 *  okhttp3.FormBody$Builder
 *  okhttp3.Headers
 *  okhttp3.Headers$Builder
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 */
package com.carlos.common.clouddisk.http;

import android.os.Handler;
import android.os.Looper;
import com.carlos.common.clouddisk.http.MyCookieJar;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
    private static OkHttpUtil mHttpUtil;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private MyCookieJar cookieJar = new MyCookieJar();

    private OkHttpUtil() {
        this.mOkHttpClientBuilder = new OkHttpClient.Builder();
        this.mOkHttpClientBuilder.cookieJar((CookieJar)this.cookieJar);
        this.mOkHttpClientBuilder.sslSocketFactory(OkHttpUtil.createSSLSocketFactory(), (X509TrustManager)new TrustAllCerts());
        this.mOkHttpClientBuilder.hostnameVerifier((HostnameVerifier)new TrustAllHostnameVerifier());
        this.mOkHttpClient = this.mOkHttpClientBuilder.build();
        this.mDelivery = new Handler(Looper.getMainLooper());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static OkHttpUtil getInstance() {
        if (mHttpUtil != null) return mHttpUtil;
        Class<OkHttpUtil> clazz = OkHttpUtil.class;
        synchronized (OkHttpUtil.class) {
            if (mHttpUtil != null) return mHttpUtil;
            mHttpUtil = new OkHttpUtil();
            // ** MonitorExit[var0] (shouldn't be in output)
            return mHttpUtil;
        }
    }

    private OkHttpClient getmOkHttpClient() {
        return this.mOkHttpClient;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return ssfFactory;
    }

    private Response _getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        HVLog.d("url:" + url);
        Call call = this.mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    private Response _getSync(String url, RequestData ... headers) throws IOException {
        if (headers == null) {
            headers = new RequestData[]{};
        }
        Request.Builder builder = new Request.Builder().url(url);
        for (RequestData header : headers) {
            if (header == null) continue;
            builder.addHeader(header.key, header.value);
        }
        Request request = builder.build();
        Call call = this.mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    private String _getSyncString(String url) throws IOException {
        Response response = this._getSync(url);
        return response.body().string();
    }

    private void _getAsync(String url, ResultCallback callback) {
        Request request = new Request.Builder().url(url).build();
        this.deliveryResult(callback, request);
    }

    private Response _postSync(String url, RequestData[] params, RequestData ... headers) throws IOException {
        Request request = this.buildPostRequest(url, params, headers);
        Response response = this.mOkHttpClient.newCall(request).execute();
        return response;
    }

    private String _postSyncString(String url, RequestData[] params, RequestData ... headers) throws IOException {
        Response response = this._postSync(url, params, headers);
        return response.body().string();
    }

    private void _postAsync(String url, ResultCallback callback, RequestData[] params, RequestData ... headers) {
        Request request = this.buildPostRequest(url, params, headers);
        this.deliveryResult(callback, request);
    }

    private void _postAsync(String url, ResultCallback callback, Map<String, String> params, Map<String, String> headers) {
        RequestData[] paramsArr = this.mapToRequestDatas(params);
        RequestData[] headersArr = this.mapToRequestDatas(headers);
        Request request = this.buildPostRequest(url, paramsArr, headersArr);
        this.deliveryResult(callback, request);
    }

    private RequestData[] mapToRequestDatas(Map<String, String> params) {
        int index = 0;
        if (params == null) {
            return new RequestData[0];
        }
        int size = params.size();
        RequestData[] res = new RequestData[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            res[index++] = new RequestData(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private Request buildPostRequest(String url, RequestData[] params, RequestData ... headers) {
        if (headers == null) {
            headers = new RequestData[]{};
        }
        Headers.Builder headersBuilder = new Headers.Builder();
        for (RequestData header : headers) {
            if (header == null) continue;
            headersBuilder.add(header.key, header.value);
        }
        Headers requestHeaders = headersBuilder.build();
        if (params == null) {
            params = new RequestData[]{};
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (RequestData param : params) {
            formBodyBuilder.add(param.key, param.value);
        }
        FormBody requestBody = formBodyBuilder.build();
        return new Request.Builder().url(url).headers(requestHeaders).post((RequestBody)requestBody).build();
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        this.mOkHttpClient.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                OkHttpUtil.this.sendFailedStringCallback(call, e, callback);
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    switch (response.code()) {
                        case 200: {
                            byte[] bytes = response.body().bytes();
                            OkHttpUtil.this.sendSuccessResultCallback(bytes, callback);
                            break;
                        }
                        case 500: {
                            OkHttpUtil.this.sendSuccessResultCallback(null, callback);
                            break;
                        }
                        default: {
                            throw new IOException();
                        }
                    }
                }
                catch (IOException e) {
                    OkHttpUtil.this.sendFailedStringCallback(call, e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Call call, final Exception e, final ResultCallback callback) {
        this.mDelivery.post(new Runnable(){

            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(call, e);
                }
            }
        });
    }

    private void sendSuccessResultCallback(final byte[] bytes, final ResultCallback callback) {
        this.mDelivery.post(new Runnable(){

            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(bytes);
                }
            }
        });
    }

    public static OkHttpClient getmOkHttpClient2() {
        return OkHttpUtil.getInstance().getmOkHttpClient();
    }

    public static Response getSync(String url) throws IOException {
        return OkHttpUtil.getInstance()._getSync(url);
    }

    public static Response getSync(String url, RequestData ... headers) throws IOException {
        return OkHttpUtil.getInstance()._getSync(url, headers);
    }

    public static String getSyncString(String url) throws IOException {
        return OkHttpUtil.getInstance()._getSyncString(url);
    }

    public static void getAsync(String url, ResultCallback callback) {
        OkHttpUtil.getInstance()._getAsync(url, callback);
    }

    public static Response postSync(String url, RequestData[] params, RequestData ... headers) throws IOException {
        return OkHttpUtil.getInstance()._postSync(url, params, headers);
    }

    public static String postSyncString(String url, RequestData[] params, RequestData ... headers) throws IOException {
        return OkHttpUtil.getInstance()._postSyncString(url, params, headers);
    }

    public static void postAsync(String url, ResultCallback callback, RequestData[] params, RequestData ... headers) {
        OkHttpUtil.getInstance()._postAsync(url, callback, params, headers);
    }

    public static void postAsync(String url, ResultCallback callback, Map<String, String> params, Map<String, String> headers) {
        OkHttpUtil.getInstance()._postAsync(url, callback, params, headers);
    }

    public static class RequestData {
        public String key;
        public String value;

        public RequestData() {
        }

        public RequestData(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static abstract class ResultCallback {
        public abstract void onError(Call var1, Exception var2);

        public abstract void onResponse(byte[] var1);
    }

    public static class TrustAllHostnameVerifier
    implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static class TrustAllCerts
    implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}

