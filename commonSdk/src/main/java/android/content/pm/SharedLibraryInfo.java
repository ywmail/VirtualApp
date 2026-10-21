/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.VersionedPackage
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.content.pm;

import android.content.pm.VersionedPackage;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import java.util.List;

public final class SharedLibraryInfo
implements Parcelable {
    public static final int TYPE_BUILTIN = 0;
    public static final int TYPE_DYNAMIC = 1;
    public static final int TYPE_STATIC = 2;
    public static final int VERSION_UNDEFINED = -1;
    public static final String PLATFORM_PACKAGE_NAME = "android";
    public static final Parcelable.Creator<SharedLibraryInfo> CREATOR = new Parcelable.Creator<SharedLibraryInfo>(){

        public SharedLibraryInfo createFromParcel(Parcel source) {
            return null;
        }

        public SharedLibraryInfo[] newArray(int size) {
            return null;
        }
    };

    public SharedLibraryInfo(String path, String packageName, List<String> codePaths, String name, long version, int type, VersionedPackage declaringPackage, List<VersionedPackage> dependentPackages, List<SharedLibraryInfo> dependencies, boolean isNative) {
        throw new RuntimeException("Just reduce stub!");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        throw new RuntimeException("Stub!");
    }
}

