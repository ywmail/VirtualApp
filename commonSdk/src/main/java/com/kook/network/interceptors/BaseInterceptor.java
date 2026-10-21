/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.FormBody
 *  okhttp3.HttpUrl
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

import com.kook.network.StringFog;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public abstract class BaseInterceptor
implements Interceptor {
    protected LinkedHashMap<String, String> getUrlParameters(Interceptor.Chain chain) {
        HttpUrl url = chain.request().url();
        int size = url.querySize();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        for (int i = 0; i < size; ++i) {
            params.put(url.queryParameterName(i), url.queryParameterValue(i));
        }
        return params;
    }

    protected String getUrlParameters(Interceptor.Chain chain, String key) {
        Request request = chain.request();
        return request.url().queryParameter(key);
    }

    protected LinkedHashMap<String, String> getBodyParameters(Interceptor.Chain chain) {
        FormBody formBody = (FormBody)chain.request().body();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        int size = 0;
        if (formBody != null) {
            size = formBody.size();
        }
        for (int i = 0; i < size; ++i) {
            params.put(formBody.name(i), formBody.value(i));
        }
        return params;
    }

    protected String getBodyParameters(Interceptor.Chain chain, String key) {
        return this.getBodyParameters(chain).get(key);
    }

    protected String getResponse(Response response) {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String body = buffer.clone().readString(charset);
            return body;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

