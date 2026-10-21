/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.ParcelFileDescriptor
 *  android.os.ParcelFileDescriptor$AutoCloseInputStream
 *  android.os.RemoteException
 */
package com.lody.virtual.client.ipc;

import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.FileInfo;
import com.lody.virtual.server.fs.IFileTransfer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileTransfer {
    private static final FileTransfer sInstance = new FileTransfer();
    private IFileTransfer mTransfer;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public IFileTransfer getService() {
        if (IInterfaceUtils.isAlive(this.mTransfer)) return this.mTransfer;
        Class<FileTransfer> clazz = FileTransfer.class;
        synchronized (FileTransfer.class) {
            Object remote = this.getStubInterface();
            this.mTransfer = LocalProxyUtils.genProxy(IFileTransfer.class, remote);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mTransfer;
        }
    }

    private Object getStubInterface() {
        return IFileTransfer.Stub.asInterface(ServiceManagerNative.getService("file-transfer"));
    }

    public static FileTransfer get() {
        return sInstance;
    }

    public ParcelFileDescriptor openFile(File file) {
        return this.openFile(file.getAbsolutePath());
    }

    public ParcelFileDescriptor openFile(String path) {
        try {
            return this.getService().openFile(path);
        }
        catch (RemoteException e) {
            return (ParcelFileDescriptor)VirtualRuntime.crash(e);
        }
    }

    public void copyFile(File remote, File local) {
        FileUtils.ensureDirCreate(local.getParentFile());
        ParcelFileDescriptor fd = this.openFile(remote);
        if (fd == null) {
            return;
        }
        ParcelFileDescriptor.AutoCloseInputStream is = new ParcelFileDescriptor.AutoCloseInputStream(fd);
        try {
            FileUtils.writeToFile((InputStream)is, local);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FileUtils.closeQuietly((Closeable)is);
    }

    public void copyDirectory(File remoteDir, File localDir) {
        FileInfo[] remoteInfos = this.listFiles(remoteDir);
        if (remoteInfos == null) {
            return;
        }
        FileUtils.ensureDirCreate(localDir);
        for (FileInfo remoteInfo : remoteInfos) {
            File remote = new File(remoteInfo.path);
            File local = new File(localDir, remote.getName());
            if (remoteInfo.isDirectory) {
                this.copyDirectory(remote, local);
                continue;
            }
            this.copyFile(remote, local);
        }
    }

    public FileInfo[] listFiles(File dir) {
        return this.listFiles(dir.getPath());
    }

    public FileInfo[] listFiles(String path) {
        try {
            return this.getService().listFiles(path);
        }
        catch (RemoteException e) {
            return (FileInfo[])VirtualRuntime.crash(e);
        }
    }
}

