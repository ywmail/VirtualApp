/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.os.mount;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IMountService {
    public static Class<?> TYPE = RefClass.load(IMountService.class, "android.os.storage.IMountService");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.os.storage.IMountService$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

