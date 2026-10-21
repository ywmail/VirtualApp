/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 */
package mirror.android.os;

import android.os.IBinder;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class Bundle {
    public static Class<?> TYPE = RefClass.load(Bundle.class, android.os.Bundle.class);
    @MethodParams(value={String.class, IBinder.class})
    public static RefMethod<Void> putIBinder;
    @MethodParams(value={String.class})
    public static RefMethod<IBinder> getIBinder;
}

