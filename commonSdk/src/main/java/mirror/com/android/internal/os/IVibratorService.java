/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IVibratorService {
    public static Class<?> TYPE = RefClass.load(IVibratorService.class, "android.os.IVibratorService");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.os.IVibratorService$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

