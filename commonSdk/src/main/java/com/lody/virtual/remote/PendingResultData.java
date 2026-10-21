/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver$PendingResult
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.remote;


import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import mirror.android.content.BroadcastReceiver;

public class PendingResultData
implements Parcelable {
    public static final Parcelable.Creator<PendingResultData> CREATOR = new Parcelable.Creator<PendingResultData>(){

        public PendingResultData createFromParcel(Parcel source) {
            return new PendingResultData(source);
        }

        public PendingResultData[] newArray(int size) {
            return new PendingResultData[size];
        }
    };
    public int mType;
    public boolean mOrderedHint;
    public boolean mInitialStickyHint;
    public IBinder mToken;
    public int mSendingUser;
    public int mFlags;
    public int mResultCode;
    public String mResultData;
    public Bundle mResultExtras;
    public boolean mAbortBroadcast;
    public boolean mFinished;

    public PendingResultData(android.content.BroadcastReceiver.PendingResult result) {
        if (BroadcastReceiver.PendingResultMNC.ctor != null) {
            this.mType = BroadcastReceiver.PendingResultMNC.mType.get(result);
            this.mOrderedHint = BroadcastReceiver.PendingResultMNC.mOrderedHint.get(result);
            this.mInitialStickyHint = BroadcastReceiver.PendingResultMNC.mInitialStickyHint.get(result);
            this.mToken = BroadcastReceiver.PendingResultMNC.mToken.get(result);
            this.mSendingUser = BroadcastReceiver.PendingResultMNC.mSendingUser.get(result);
            this.mFlags = BroadcastReceiver.PendingResultMNC.mFlags.get(result);
            this.mResultCode = BroadcastReceiver.PendingResultMNC.mResultCode.get(result);
            this.mResultData = BroadcastReceiver.PendingResultMNC.mResultData.get(result);
            this.mResultExtras = BroadcastReceiver.PendingResultMNC.mResultExtras.get(result);
            this.mAbortBroadcast = BroadcastReceiver.PendingResultMNC.mAbortBroadcast.get(result);
            this.mFinished = BroadcastReceiver.PendingResultMNC.mFinished.get(result);
        } else if (BroadcastReceiver.PendingResultJBMR1.ctor != null) {
            this.mType = BroadcastReceiver.PendingResultJBMR1.mType.get(result);
            this.mOrderedHint = BroadcastReceiver.PendingResultJBMR1.mOrderedHint.get(result);
            this.mInitialStickyHint = BroadcastReceiver.PendingResultJBMR1.mInitialStickyHint.get(result);
            this.mToken = BroadcastReceiver.PendingResultJBMR1.mToken.get(result);
            this.mSendingUser = BroadcastReceiver.PendingResultJBMR1.mSendingUser.get(result);
            this.mResultCode = BroadcastReceiver.PendingResultJBMR1.mResultCode.get(result);
            this.mResultData = BroadcastReceiver.PendingResultJBMR1.mResultData.get(result);
            this.mResultExtras = BroadcastReceiver.PendingResultJBMR1.mResultExtras.get(result);
            this.mAbortBroadcast = BroadcastReceiver.PendingResultJBMR1.mAbortBroadcast.get(result);
            this.mFinished = BroadcastReceiver.PendingResultJBMR1.mFinished.get(result);
        } else {
            this.mType = BroadcastReceiver.PendingResult.mType.get(result);
            this.mOrderedHint = BroadcastReceiver.PendingResult.mOrderedHint.get(result);
            this.mInitialStickyHint = BroadcastReceiver.PendingResult.mInitialStickyHint.get(result);
            this.mToken = BroadcastReceiver.PendingResult.mToken.get(result);
            this.mResultCode = BroadcastReceiver.PendingResult.mResultCode.get(result);
            this.mResultData = BroadcastReceiver.PendingResult.mResultData.get(result);
            this.mResultExtras = BroadcastReceiver.PendingResult.mResultExtras.get(result);
            this.mAbortBroadcast = BroadcastReceiver.PendingResult.mAbortBroadcast.get(result);
            this.mFinished = BroadcastReceiver.PendingResult.mFinished.get(result);
        }
    }

    protected PendingResultData(Parcel in) {
        this.mType = in.readInt();
        this.mOrderedHint = in.readByte() != 0;
        this.mInitialStickyHint = in.readByte() != 0;
        this.mToken = in.readStrongBinder();
        this.mSendingUser = in.readInt();
        this.mFlags = in.readInt();
        this.mResultCode = in.readInt();
        this.mResultData = in.readString();
        this.mResultExtras = in.readBundle();
        this.mAbortBroadcast = in.readByte() != 0;
        this.mFinished = in.readByte() != 0;
    }

    public android.content.BroadcastReceiver.PendingResult build() {
        if (BroadcastReceiver.PendingResultMNC.ctor != null) {
            return mirror.android.content.BroadcastReceiver.PendingResultMNC.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken, this.mSendingUser, this.mFlags);
        }
        if (BroadcastReceiver.PendingResultJBMR1.ctor != null) {
            return BroadcastReceiver.PendingResultJBMR1.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken, this.mSendingUser);
        }
        return BroadcastReceiver.PendingResult.ctor.newInstance(this.mResultCode, this.mResultData, this.mResultExtras, this.mType, this.mOrderedHint, this.mInitialStickyHint, this.mToken);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeByte(this.mOrderedHint ? (byte)1 : 0);
        dest.writeByte(this.mInitialStickyHint ? (byte)1 : 0);
        dest.writeStrongBinder(this.mToken);
        dest.writeInt(this.mSendingUser);
        dest.writeInt(this.mFlags);
        dest.writeInt(this.mResultCode);
        dest.writeString(this.mResultData);
        dest.writeBundle(this.mResultExtras);
        dest.writeByte(this.mAbortBroadcast ? (byte)1 : 0);
        dest.writeByte(this.mFinished ? (byte)1 : 0);
    }

    public void finish() {
        try {
            this.build().finish();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

