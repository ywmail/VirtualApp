/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.os;

import android.annotation.TargetApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;

@TargetApi(value=26)
public final class ParcelableException
extends RuntimeException
implements Parcelable {
    public static final Parcelable.Creator<ParcelableException> CREATOR = new Parcelable.Creator<ParcelableException>(){

        public ParcelableException createFromParcel(Parcel source) {
            return new ParcelableException(ParcelableException.readFromParcel(source));
        }

        public ParcelableException[] newArray(int size) {
            return new ParcelableException[size];
        }
    };

    public ParcelableException(Throwable t) {
        super(t);
    }

    public <T extends Throwable> void maybeRethrow(Class<T> clazz) throws T {
        throw new RuntimeException("Stub!");
    }

    public static Throwable readFromParcel(Parcel in) {
        throw new RuntimeException("Stub!");
    }

    public static void writeToParcel(Parcel out, Throwable t) {
        throw new RuntimeException("Stub!");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}

