/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.bluetooth;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IBluetooth {
    public static Class<?> TYPE = RefClass.load(IBluetooth.class, "android.bluetooth.IBluetooth");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.bluetooth.IBluetooth$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

