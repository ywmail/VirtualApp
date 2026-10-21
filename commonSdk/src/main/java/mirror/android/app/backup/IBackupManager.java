/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package mirror.android.app.backup;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IBackupManager {
    public static Class<?> TYPE = RefClass.load(IBackupManager.class, "android.app.backup.IBackupManager");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.backup.IBackupManager$Stub");
        @MethodParams(value={IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}

