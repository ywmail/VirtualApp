/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;

public final class LocationRequest
implements Parcelable {
    public static final Parcelable.Creator<LocationRequest> CREATOR = new Parcelable.Creator<LocationRequest>(){

        public LocationRequest createFromParcel(Parcel in) {
            throw new RuntimeException("Stub!");
        }

        public LocationRequest[] newArray(int size) {
            throw new RuntimeException("Stub!");
        }
    };

    public LocationRequest setProvider(String provider) {
        throw new RuntimeException("Stub!");
    }

    public String getProvider() {
        throw new RuntimeException("Stub!");
    }

    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }
}

