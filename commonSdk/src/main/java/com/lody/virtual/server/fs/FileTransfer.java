/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.ParcelFileDescriptor
 */
package com.lody.virtual.server.fs;

import android.os.ParcelFileDescriptor;
import com.lody.virtual.remote.FileInfo;
import com.lody.virtual.server.fs.IFileTransfer;
import java.io.File;

public class FileTransfer
extends IFileTransfer.Stub {
    private static final FileTransfer sInstance = new FileTransfer();

    public static FileTransfer get() {
        return sInstance;
    }

    @Override
    public FileInfo[] listFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return null;
        }
        FileInfo[] fileInfos = new FileInfo[files.length];
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            fileInfos[i] = new FileInfo(file);
        }
        return fileInfos;
    }

    @Override
    public ParcelFileDescriptor openFile(String path) {
        try {
            return ParcelFileDescriptor.open((File)new File(path), (int)0x10000000);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

