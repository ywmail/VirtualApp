/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  okhttp3.MediaType
 *  okhttp3.RequestBody
 *  okhttp3.internal.Util
 *  okio.BufferedSink
 *  okio.Okio
 *  okio.Source
 */
package com.carlos.common.clouddisk.http;

import android.util.Log;
import com.kook.librelease.StringFog;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class FileProgressRequestBody
extends RequestBody {
    private int segmentSize = 128;
    protected File file;
    protected ProgressListener listener;
    protected String contentType;
    private long fileLength;

    public FileProgressRequestBody(String contentType, File file, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
        this.fileLength = this.contentLength();
        this.segmentSize = 2048;
        Log.d((String)"789", (String)(this.fileLength + ":" + this.segmentSize));
    }

    protected FileProgressRequestBody() {
    }

    public long contentLength() {
        return this.file.length();
    }

    public MediaType contentType() {
        return MediaType.parse((String)this.contentType);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            long read;
            source = Okio.source((File)this.file);
            long total = 0L;
            while ((read = source.read(sink.buffer(), (long)this.segmentSize)) != -1L) {
                sink.flush();
                this.listener.transferred((double)(total += read) / (double)this.fileLength * 100.0);
            }
        }
        finally {
            Util.closeQuietly((Closeable)source);
        }
    }

    public static interface ProgressListener {
        public void transferred(double var1);
    }
}

