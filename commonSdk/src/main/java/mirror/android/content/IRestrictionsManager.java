/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IRestrictionsManager {
    public static Class<?> TYPE = RefClass.load(IRestrictionsManager.class, "android.content.IRestrictionsManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.content.IRestrictionsManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

