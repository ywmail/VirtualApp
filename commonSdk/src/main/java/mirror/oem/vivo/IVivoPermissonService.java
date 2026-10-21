/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.oem.vivo;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IVivoPermissonService {
    public static Class<?> TYPE = RefClass.load(IVivoPermissonService.class, "vivo.app.security.IVivoPermissionService");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "vivo.app.security.IVivoPermissionService$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

