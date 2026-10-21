/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.location.GpsStatus
 */
package mirror.android.location;

import android.location.GpsStatus;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class GpsStatusL {
    public static Class<?> TYPE = RefClass.load(GpsStatusL.class, GpsStatus.class);
    @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, int[].class, int[].class, int[].class})
    public static RefMethod<Void> setStatus;
}

