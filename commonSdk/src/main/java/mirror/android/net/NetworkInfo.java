/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$DetailedState
 *  android.net.NetworkInfo$State
 */
package mirror.android.net;

import mirror.MethodParams;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;
import mirror.RefObject;

public class NetworkInfo {
    public static Class<?> TYPE = RefClass.load(NetworkInfo.class, android.net.NetworkInfo.class);
    @MethodParams(value={int.class, int.class, String.class, String.class})
    public static RefConstructor<android.net.NetworkInfo> ctor;
    @MethodParams(value={int.class})
    public static RefConstructor<android.net.NetworkInfo> ctorOld;
    public static RefInt mNetworkType;
    public static RefObject<String> mTypeName;
    public static RefObject<android.net.NetworkInfo.State> mState;
    public static RefObject<android.net.NetworkInfo.DetailedState> mDetailedState;
    public static RefBoolean mIsAvailable;
}

