/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.Interceptor$Chain
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 */
package com.kook.network.interceptors;

import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.interceptors.BaseInterceptor;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthIntercepter
extends BaseInterceptor {
    private String APP_TAG = "";

    private AuthIntercepter(Builder builder) {
        this.APP_TAG = builder.appTag;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder().addHeader("User-Agent", this.APP_TAG);
        HVLog.d(this.APP_TAG);
        Request request = builder.build();
        Response response = null;
        try {
            response = chain.proceed(request);
            String responseBody = this.getResponse(response);
            HVLog.d("responseBody:" + responseBody);
        }
        catch (IOException e) {
            HVLog.e(e.getMessage());
        }
        return response;
    }

    public static final class Builder {
        private String appTag;

        public Builder getTag() {
            this.appTag = "kooklog";
            return this;
        }

        public AuthIntercepter build() {
            return new AuthIntercepter(this);
        }
    }
}

