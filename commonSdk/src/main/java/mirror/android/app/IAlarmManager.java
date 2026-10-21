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

public class IAlarmManager {
    public static Class<?> TYPE = RefClass.load(IAlarmManager.class, "android.app.IAlarmManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.IAlarmManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

