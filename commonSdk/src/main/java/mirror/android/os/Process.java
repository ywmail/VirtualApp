/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Process
 */
package mirror.android.os;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class Process {
    public static Class<?> TYPE = RefClass.load(Process.class, android.os.Process.class);
    @MethodParams(value={String.class})
    public static RefStaticMethod<Void> setArgV0;
}

