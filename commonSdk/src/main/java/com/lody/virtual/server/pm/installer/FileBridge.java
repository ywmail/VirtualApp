/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.system.ErrnoException
 *  android.system.Os
 *  android.system.OsConstants
 *  android.util.Log
 */
package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteOrder;

@TargetApi(value=21)
public class FileBridge
extends Thread {
    private static final String TAG = "FileBridge";
    private static final int MSG_LENGTH = 8;
    private static final int CMD_WRITE = 1;
    private static final int CMD_FSYNC = 2;
    private static final int CMD_CLOSE = 3;
    private FileDescriptor mTarget;
    private final FileDescriptor mServer = new FileDescriptor();
    private final FileDescriptor mClient = new FileDescriptor();
    private volatile boolean mClosed;

    public FileBridge() {
        try {
            Os.socketpair((int)OsConstants.AF_UNIX, (int)OsConstants.SOCK_STREAM, (int)0, (FileDescriptor)this.mServer, (FileDescriptor)this.mClient);
        }
        catch (ErrnoException e) {
            throw new RuntimeException("Failed to create bridge");
        }
    }

    public boolean isClosed() {
        return this.mClosed;
    }

    public void forceClose() {
        FileBridge.closeQuietly(this.mTarget);
        FileBridge.closeQuietly(this.mServer);
        FileBridge.closeQuietly(this.mClient);
        this.mClosed = true;
    }

    public void setTargetFile(FileDescriptor target) {
        this.mTarget = target;
    }

    public FileDescriptor getClientSocket() {
        return this.mClient;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        byte[] temp = new byte[8192];
        try {
            while (FileBridge.read(this.mServer, temp, 0, 8) == 8) {
                int cmd = FileUtils.peekInt(temp, 0, ByteOrder.BIG_ENDIAN);
                if (cmd == 1) {
                    int n;
                    for (int len = FileUtils.peekInt(temp, 4, ByteOrder.BIG_ENDIAN); len > 0; len -= n) {
                        n = FileBridge.read(this.mServer, temp, 0, Math.min(temp.length, len));
                        if (n == -1) {
                            throw new IOException("Unexpected EOF; still expected " + len + " bytes");
                        }
                        FileBridge.write(this.mTarget, temp, 0, n);
                    }
                    continue;
                }
                if (cmd == 2) {
                    Os.fsync((FileDescriptor)this.mTarget);
                    FileBridge.write(this.mServer, temp, 0, 8);
                    continue;
                }
                if (cmd != 3) continue;
                Os.fsync((FileDescriptor)this.mTarget);
                Os.close((FileDescriptor)this.mTarget);
                this.mClosed = true;
                FileBridge.write(this.mServer, temp, 0, 8);
                break;
            }
        }
        catch (ErrnoException | IOException e) {
            Log.wtf((String)TAG, (String)"Failed during bridge", (Throwable)e);
        }
        finally {
            this.forceClose();
        }
    }

    public static void closeQuietly(FileDescriptor fd) {
        if (fd != null && fd.valid()) {
            try {
                Os.close((FileDescriptor)fd);
            }
            catch (ErrnoException e) {
                e.printStackTrace();
            }
        }
    }

    public static int read(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws IOException {
        ArrayUtils.checkOffsetAndCount(bytes.length, byteOffset, byteCount);
        if (byteCount == 0) {
            return 0;
        }
        try {
            int readCount = Os.read((FileDescriptor)fd, (byte[])bytes, (int)byteOffset, (int)byteCount);
            if (readCount == 0) {
                return -1;
            }
            return readCount;
        }
        catch (ErrnoException errnoException) {
            if (errnoException.errno == OsConstants.EAGAIN) {
                return 0;
            }
            throw new IOException(errnoException);
        }
    }

    public static void write(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws IOException {
        ArrayUtils.checkOffsetAndCount(bytes.length, byteOffset, byteCount);
        if (byteCount == 0) {
            return;
        }
        try {
            while (byteCount > 0) {
                int bytesWritten = Os.write((FileDescriptor)fd, (byte[])bytes, (int)byteOffset, (int)byteCount);
                byteCount -= bytesWritten;
                byteOffset += bytesWritten;
            }
        }
        catch (ErrnoException errnoException) {
            throw new IOException(errnoException);
        }
    }
}

