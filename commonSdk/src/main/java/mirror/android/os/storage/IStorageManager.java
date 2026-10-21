/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.os.storage;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IStorageManager {
    public static Class<?> Class = RefClass.load(IStorageManager.class, "android.os.storage.IStorageManager");

    public static class Stub {
        public static Class<?> Class = RefClass.load(Stub.class, "android.os.storage.IStorageManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

