/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build$VERSION
 *  okhttp3.Interceptor
 *  okhttp3.Interceptor$Chain
 *  okhttp3.MediaType
 *  okhttp3.Request
 *  okhttp3.Response
 *  okhttp3.ResponseBody
 *  okio.Buffer
 *  okio.BufferedSource
 */
package com.kook.network.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.secret.CipherUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class DecryptionInterceptor
implements Interceptor {
    private static String INTERCEPTOR = "interceptor-sp";
    public Context mContext;
    public String mBaseUrl;

    public DecryptionInterceptor(Context context, String baseUrl) {
        this.mContext = context;
        this.mBaseUrl = baseUrl;
    }

    public static int countPrefix(String str1, String str2) {
        int minLength = Math.min(str1.length(), str2.length());
        int count = 0;
        for (int i = 0; i < minLength && str1.charAt(i) == str2.charAt(i); ++i) {
            ++count;
        }
        return count;
    }

    public String removeQuotes(String str) {
        if (str != null && str.length() > 1 && str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    public void setPersistent(String requestUrl, String encryptContent) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(INTERCEPTOR, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(requestUrl, encryptContent);
        HVLog.d("requestUrl:" + requestUrl + "    encryptContent:" + encryptContent);
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        }
    }

    public static String getPersistent(Context context, String requestUrl, boolean encrypt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(INTERCEPTOR, 4);
        String encryptContent = sharedPreferences.getString(requestUrl, null);
        if (encrypt) {
            return CipherUtil.decrypt("Hhj2024.08.06-07", encryptContent);
        }
        return encryptContent;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String requestUrl = request.url().toString();
        Response response = chain.proceed(request);
        int code = response.code();
        HVLog.d("请求响应:" + code);
        if (code == 200) {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            String encryptedString = buffer.clone().readString(StandardCharsets.UTF_8);
            String encryptContent = this.removeQuotes(encryptedString);
            this.setPersistent(requestUrl, encryptContent);
            String desDecrypt = CipherUtil.decrypt("Hhj2024.08.06-07", encryptContent);
            ResponseBody newResponseBody = ResponseBody.create((MediaType)responseBody.contentType(), (String)desDecrypt);
            return response.newBuilder().body(newResponseBody).build();
        }
        ResponseBody responseBody = response.body();
        return response.newBuilder().body(responseBody).build();
    }
}

