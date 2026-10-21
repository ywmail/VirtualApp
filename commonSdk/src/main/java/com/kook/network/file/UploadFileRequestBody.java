/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.MediaType
 *  okhttp3.MultipartBody$Part
 *  okhttp3.RequestBody
 *  okhttp3.ResponseBody
 *  okio.Buffer
 *  okio.BufferedSink
 *  okio.ForwardingSink
 *  okio.Okio
 *  okio.Sink
 */
package com.kook.network.file;

import com.kook.network.StringFog;
import com.kook.network.file.FileUploadObserver;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadFileRequestBody
extends RequestBody {
    private final RequestBody mRequestBody;
    private FileUploadObserver<ResponseBody> mFileUploadObserver;

    public UploadFileRequestBody(File file, FileUploadObserver<ResponseBody> fileUploadObserver) {
        this.mRequestBody = RequestBody.create((MediaType)MediaType.parse((String)"multipart/form-data"), (File)file);
        MultipartBody.Part body = MultipartBody.Part.createFormData((String)"file", (String)file.getName(), (RequestBody)this.mRequestBody);
        this.mFileUploadObserver = fileUploadObserver;
    }

    public MediaType contentType() {
        return this.mRequestBody.contentType();
    }

    public long contentLength() throws IOException {
        return this.mRequestBody.contentLength();
    }

    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink((Sink)sink);
        BufferedSink bufferedSink = Okio.buffer((Sink)countingSink);
        this.mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink
    extends ForwardingSink {
        private long bytesWritten;

        public CountingSink(Sink delegate) {
            super(delegate);
            this.bytesWritten = 0L;
        }

        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            this.bytesWritten += byteCount;
            if (UploadFileRequestBody.this.mFileUploadObserver != null) {
                UploadFileRequestBody.this.mFileUploadObserver.onProgressChange(this.bytesWritten, UploadFileRequestBody.this.contentLength());
            }
        }
    }
}

