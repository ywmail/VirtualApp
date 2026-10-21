/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  androidx.annotation.Nullable
 */
package com.carlos.common.imagepicker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class AspectRatio
implements Parcelable {
    @Nullable
    private final String mAspectRatioTitle;
    private final float mAspectRatioX;
    private final float mAspectRatioY;
    public static final Parcelable.Creator<AspectRatio> CREATOR = new Parcelable.Creator<AspectRatio>(){

        public AspectRatio createFromParcel(Parcel in) {
            return new AspectRatio(in);
        }

        public AspectRatio[] newArray(int size) {
            return new AspectRatio[size];
        }
    };

    public AspectRatio(@Nullable String aspectRatioTitle, float aspectRatioX, float aspectRatioY) {
        this.mAspectRatioTitle = aspectRatioTitle;
        this.mAspectRatioX = aspectRatioX;
        this.mAspectRatioY = aspectRatioY;
    }

    protected AspectRatio(Parcel in) {
        this.mAspectRatioTitle = in.readString();
        this.mAspectRatioX = in.readFloat();
        this.mAspectRatioY = in.readFloat();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAspectRatioTitle);
        dest.writeFloat(this.mAspectRatioX);
        dest.writeFloat(this.mAspectRatioY);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public String getAspectRatioTitle() {
        return this.mAspectRatioTitle;
    }

    public float getAspectRatioX() {
        return this.mAspectRatioX;
    }

    public float getAspectRatioY() {
        return this.mAspectRatioY;
    }
}

