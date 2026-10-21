/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.app;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ILocaleManager {
    public static Class<?> TYPE = RefClass.load(ILocaleManager.class, "android.app.ILocaleManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.ILocaleManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

