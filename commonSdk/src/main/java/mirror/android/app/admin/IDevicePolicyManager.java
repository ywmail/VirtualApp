/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.app.admin;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IDevicePolicyManager {
    public static Class<?> TYPE = RefClass.load(IDevicePolicyManager.class, "android.app.admin.IDevicePolicyManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.admin.IDevicePolicyManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

