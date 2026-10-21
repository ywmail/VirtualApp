/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package com.lody.virtual.os;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VBinder;
import java.io.PrintWriter;

public final class VUserHandle
implements Parcelable {
    public static final int PER_USER_RANGE = 100000;
    public static final int USER_ALL = -1;
    public static final VUserHandle ALL = new VUserHandle(-1);
    public static final int USER_CURRENT = -2;
    public static final VUserHandle CURRENT = new VUserHandle(-2);
    public static final int USER_CURRENT_OR_SELF = -3;
    public static final VUserHandle CURRENT_OR_SELF = new VUserHandle(-3);
    public static final int USER_NULL = -10000;
    public static final int USER_OWNER = 0;
    public static final VUserHandle OWNER = new VUserHandle(0);
    public static final boolean MU_ENABLED = true;
    public static final int FIRST_SHARED_APPLICATION_GID = 50000;
    public static final int LAST_SHARED_APPLICATION_GID = 59999;
    public static final int FIRST_ISOLATED_UID = 99000;
    public static final int LAST_ISOLATED_UID = 99999;
    public static final Parcelable.Creator<VUserHandle> CREATOR = new Parcelable.Creator<VUserHandle>(){

        public VUserHandle createFromParcel(Parcel in) {
            return new VUserHandle(in);
        }

        public VUserHandle[] newArray(int size) {
            return new VUserHandle[size];
        }
    };
    private static final SparseArray<VUserHandle> userHandles = new SparseArray();
    final int mHandle;

    public VUserHandle(int h) {
        this.mHandle = h;
    }

    public VUserHandle(Parcel in) {
        this.mHandle = in.readInt();
    }

    public static boolean isSameUser(int uid1, int uid2) {
        return VUserHandle.getUserId(uid1) == VUserHandle.getUserId(uid2);
    }

    public static boolean accept(int userId) {
        return userId == -1 || userId == VUserHandle.myUserId();
    }

    public static final boolean isSameApp(int uid1, int uid2) {
        return VUserHandle.getAppId(uid1) == VUserHandle.getAppId(uid2);
    }

    public static final boolean isIsolated(int uid) {
        if (uid > 0) {
            int appId = VUserHandle.getAppId(uid);
            return appId >= 99000 && appId <= 99999;
        }
        return false;
    }

    public static boolean isApp(int uid) {
        if (uid > 0) {
            int appId = VUserHandle.getAppId(uid);
            return appId >= 10000 && appId <= 19999;
        }
        return false;
    }

    public static int getUserId(int uid) {
        if (uid < 0) {
            return 0;
        }
        return uid / 100000;
    }

    public static int getCallingUserId() {
        return VUserHandle.getUserId(VBinder.getCallingUid());
    }

    public static VUserHandle getCallingUserHandle() {
        int userId = getUserId(VBinder.getCallingUid());
        VUserHandle userHandle = (VUserHandle)userHandles.get(userId);
        if (userHandle == null) {
            userHandle = new VUserHandle(userId);
            userHandles.put(userId, userHandle);
        }

        return userHandle;
    }

    public static int getUid(int userId, int appId) {
        return userId * 100000 + appId % 100000;
    }

    public static int getAppId(int uid) {
        return uid % 100000;
    }

    public static int myAppId() {
        return VUserHandle.getAppId(VClient.get().getVUid());
    }

    public static int getAppIdFromSharedAppGid(int gid) {
        int noUserGid = VUserHandle.getAppId(gid);
        if (noUserGid < 50000 || noUserGid > 59999) {
            throw new IllegalArgumentException(Integer.toString(gid) + " is not a shared app gid");
        }
        return noUserGid + 10000 - 50000;
    }

    public static void formatUid(StringBuilder sb, int uid) {
        if (uid < 10000) {
            sb.append(uid);
        } else {
            sb.append('u');
            sb.append(VUserHandle.getUserId(uid));
            int appId = VUserHandle.getAppId(uid);
            if (appId >= 99000 && appId <= 99999) {
                sb.append('i');
                sb.append(appId - 99000);
            } else if (appId >= 10000) {
                sb.append('a');
                sb.append(appId - 10000);
            } else {
                sb.append('s');
                sb.append(appId);
            }
        }
    }

    public static String formatUid(int uid) {
        StringBuilder sb = new StringBuilder();
        VUserHandle.formatUid(sb, uid);
        return sb.toString();
    }

    public static void formatUid(PrintWriter pw, int uid) {
        if (uid < 10000) {
            pw.print(uid);
        } else {
            pw.print('u');
            pw.print(VUserHandle.getUserId(uid));
            int appId = VUserHandle.getAppId(uid);
            if (appId >= 99000 && appId <= 99999) {
                pw.print('i');
                pw.print(appId - 99000);
            } else if (appId >= 10000) {
                pw.print('a');
                pw.print(appId - 10000);
            } else {
                pw.print('s');
                pw.print(appId);
            }
        }
    }

    public static int myUserId() {
        return VUserHandle.getUserId(VClient.get().getVUid());
    }

    public static int realUserId() {
        if (Build.VERSION.SDK_INT < 17) {
            return 0;
        }
        return VUserHandle.getUserId(VirtualCore.get().myUid());
    }

    public static void writeToParcel(VUserHandle h, Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(-10000);
        }
    }

    public static VUserHandle readFromParcel(Parcel in) {
        int h = in.readInt();
        return h != -10000 ? new VUserHandle(h) : null;
    }

    public static VUserHandle myUserHandle() {
        return new VUserHandle(VUserHandle.myUserId());
    }

    public final boolean isOwner() {
        return this.equals(OWNER);
    }

    public int getIdentifier() {
        return this.mHandle;
    }

    public String toString() {
        return "VUserHandle{" + this.mHandle + "}";
    }

    public boolean equals(Object obj) {
        try {
            if (obj != null) {
                VUserHandle other = (VUserHandle)obj;
                return this.mHandle == other.mHandle;
            }
        }
        catch (ClassCastException classCastException) {
            // empty catch block
        }
        return false;
    }

    public int hashCode() {
        return this.mHandle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mHandle);
    }
}

