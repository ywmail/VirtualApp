/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.net.wifi;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IWifiManager {
    public static Class<?> TYPE = RefClass.load(IWifiManager.class, "android.net.wifi.IWifiManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.net.wifi.IWifiManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

