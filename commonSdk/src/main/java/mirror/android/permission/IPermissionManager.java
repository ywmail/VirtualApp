/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.permission;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IPermissionManager {
    public static Class<?> TYPE = RefClass.load(IPermissionManager.class, "android.os.IPermissionManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.os.IPermissionManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

