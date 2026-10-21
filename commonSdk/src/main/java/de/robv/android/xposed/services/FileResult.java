/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed.services;

import java.io.InputStream;

public final class FileResult {
    public final byte[] content;
    public final InputStream stream;
    public final long size;
    public final long mtime;

    public FileResult(long size, long mtime) {
        this.content = null;
        this.stream = null;
        this.size = size;
        this.mtime = mtime;
    }

    public FileResult(byte[] content, long size, long mtime) {
        this.content = content;
        this.stream = null;
        this.size = size;
        this.mtime = mtime;
    }

    public FileResult(InputStream stream, long size, long mtime) {
        this.content = null;
        this.stream = stream;
        this.size = size;
        this.mtime = mtime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.content != null) {
            sb.append("content.length: ");
            sb.append(this.content.length);
            sb.append(", ");
        }
        if (this.stream != null) {
            sb.append("stream: ");
            sb.append(this.stream.toString());
            sb.append(", ");
        }
        sb.append("size: ");
        sb.append(this.size);
        sb.append(", mtime: ");
        sb.append(this.mtime);
        sb.append("}");
        return sb.toString();
    }
}

