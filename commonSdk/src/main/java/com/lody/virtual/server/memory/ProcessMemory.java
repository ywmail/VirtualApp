/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.memory;

import com.lody.virtual.StringFog;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

public class ProcessMemory {
    private int pid;
    private RandomAccessFile memFile;

    public ProcessMemory(int pid) throws IOException {
        this.pid = pid;
        this.memFile = new RandomAccessFile(String.format(Locale.ENGLISH, "/proc/%d/mem", pid), "rw");
    }

    public void write(long offset, byte[] bytes) throws IOException {
        this.memFile.seek(offset);
        this.memFile.write(bytes);
    }

    public int read(long offset, byte[] bytes, int len) throws IOException {
        this.memFile.seek(offset);
        return this.memFile.read(bytes, 0, len);
    }

    public void close() throws IOException {
        this.memFile.close();
    }
}

