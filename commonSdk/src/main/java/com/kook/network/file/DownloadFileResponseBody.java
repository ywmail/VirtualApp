/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.MediaType
 *  okhttp3.ResponseBody
 *  okio.Buffer
 *  okio.BufferedSource
 *  okio.ForwardingSource
 *  okio.Okio
 *  okio.Source
 */
package com.kook.network.file;

import com.kook.network.file.IDownloadListener;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadFileResponseBody
extends ResponseBody {
    private ResponseBody mResponseBody;
    private IDownloadListener mListener;
    private BufferedSource mBufferedSource;

    public DownloadFileResponseBody(ResponseBody responseBody, IDownloadListener listener) {
        this.mResponseBody = responseBody;
        this.mListener = listener;
    }

    public MediaType contentType() {
        if (this.mResponseBody == null) {
            return null;
        }
        return this.mResponseBody.contentType();
    }

    public long contentLength() {
        if (this.mResponseBody == null) {
            return 0L;
        }
        return this.mResponseBody.contentLength();
    }

    public BufferedSource source() {
        if (this.mBufferedSource == null && this.mResponseBody != null) {
            this.mBufferedSource = Okio.buffer((Source)this.source((Source)this.mResponseBody.source()));
        }
        return this.mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source){
            long totalBytesRead;
            {
                this.totalBytesRead = 0L;
            }

            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.totalBytesRead += bytesRead != -1L ? bytesRead : 0L;
                if (DownloadFileResponseBody.this.mListener != null && bytesRead != -1L) {
                    DownloadFileResponseBody.this.mListener.onProgress((int)(this.totalBytesRead * 100L / DownloadFileResponseBody.this.contentLength()));
                }
                return bytesRead;
            }
        };
    }
}

