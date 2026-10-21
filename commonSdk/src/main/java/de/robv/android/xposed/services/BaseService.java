/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed.services;

import de.robv.android.xposed.services.FileResult;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class BaseService {
    public static final int R_OK = 4;
    public static final int W_OK = 2;
    public static final int X_OK = 1;
    public static final int F_OK = 0;

    public boolean hasDirectFileAccess() {
        return false;
    }

    public abstract boolean checkFileAccess(String var1, int var2);

    public boolean checkFileExists(String filename) {
        return this.checkFileAccess(filename, 0);
    }

    public abstract FileResult statFile(String var1) throws IOException;

    public long getFileSize(String filename) throws IOException {
        return this.statFile((String)filename).size;
    }

    public long getFileModificationTime(String filename) throws IOException {
        return this.statFile((String)filename).mtime;
    }

    public abstract byte[] readFile(String var1) throws IOException;

    public abstract FileResult readFile(String var1, long var2, long var4) throws IOException;

    public abstract FileResult readFile(String var1, int var2, int var3, long var4, long var6) throws IOException;

    public InputStream getFileInputStream(String filename) throws IOException {
        return new ByteArrayInputStream(this.readFile(filename));
    }

    public FileResult getFileInputStream(String filename, long previousSize, long previousTime) throws IOException {
        FileResult result = this.readFile(filename, previousSize, previousTime);
        if (result.content == null) {
            return result;
        }
        return new FileResult(new ByteArrayInputStream(result.content), result.size, result.mtime);
    }

    BaseService() {
    }

    static void ensureAbsolutePath(String filename) {
        if (!filename.startsWith("/")) {
            throw new IllegalArgumentException("Only absolute filenames are allowed: " + filename);
        }
    }

    static void throwCommonIOException(int errno, String errorMsg, String filename, String defaultText) throws IOException {
        switch (errno) {
            case 1: 
            case 13: {
                throw new FileNotFoundException(errorMsg != null ? errorMsg : "Permission denied: " + filename);
            }
            case 2: {
                throw new FileNotFoundException(errorMsg != null ? errorMsg : "No such file or directory: " + filename);
            }
            case 12: {
                throw new OutOfMemoryError(errorMsg);
            }
            case 21: {
                throw new FileNotFoundException(errorMsg != null ? errorMsg : "Is a directory: " + filename);
            }
        }
        throw new IOException(errorMsg != null ? errorMsg : "Error " + errno + defaultText + filename);
    }
}

