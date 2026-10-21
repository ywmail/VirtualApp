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

public class IActivityTaskManager {
    public static Class<?> TYPE = RefClass.load(IActivityTaskManager.class, "android.app.IActivityTaskManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.IActivityTaskManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

