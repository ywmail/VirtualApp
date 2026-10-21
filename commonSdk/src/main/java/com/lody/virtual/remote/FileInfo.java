/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.File;

public class FileInfo
implements Parcelable {
    public boolean isDirectory;
    public String path;
    public static final Parcelable.Creator<FileInfo> CREATOR = new Parcelable.Creator<FileInfo>(){

        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };

    private FileInfo() {
    }

    public FileInfo(File file) {
        this.isDirectory = file.isDirectory();
        this.path = file.getPath();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isDirectory ? (byte)1 : 0);
        dest.writeString(this.path);
    }

    protected FileInfo(Parcel in) {
        this.isDirectory = in.readByte() != 0;
        this.path = in.readString();
    }
}

