/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.Interceptor$Chain
 *  okhttp3.Request
 *  okhttp3.Response
 *  okhttp3.ResponseBody
 */
package com.kook.network.interceptors;

import com.kook.network.file.DownloadFileResponseBody;
import com.kook.network.file.IDownloadListener;
import com.kook.network.interceptors.BaseInterceptor;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadInterceptor
extends BaseInterceptor {
    private IDownloadListener mDownloadListener;

    public DownloadInterceptor(IDownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        }
        catch (SocketTimeoutException e) {
            this.handleException(e);
            throw e;
        }
        catch (UnknownHostException e) {
            this.handleException(e);
            throw e;
        }
        catch (IOException e) {
            this.handleException(e);
            throw e;
        }
        return response.newBuilder().body((ResponseBody)new DownloadFileResponseBody(response.body(), this.mDownloadListener)).build();
    }

    private void handleException(Exception e) {
        if (this.mDownloadListener != null) {
            this.mDownloadListener.onDownloadFail(e);
        }
    }
}

