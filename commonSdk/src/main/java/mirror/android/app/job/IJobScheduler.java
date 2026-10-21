/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.app.job;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IJobScheduler {
    public static Class<?> TYPE = RefClass.load(IJobScheduler.class, "android.app.job.IJobScheduler");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.job.IJobScheduler$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

