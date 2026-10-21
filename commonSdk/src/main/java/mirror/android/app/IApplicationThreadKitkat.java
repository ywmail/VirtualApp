/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.content.pm.ServiceInfo
 *  android.os.IBinder
 */
package mirror.android.app;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.android.content.res.CompatibilityInfo;

public class IApplicationThreadKitkat {
    public static Class<?> TYPE = RefClass.load(IApplicationThreadKitkat.class, "android.app.IApplicationThread");
    @MethodReflectParams(value={"android.content.Intent", "android.content.pm.ActivityInfo", "android.content.res.CompatibilityInfo", "int", "java.lang.String", "android.os.Bundle", "boolean", "int", "int"})
    public static RefMethod<Void> scheduleReceiver;
    @MethodParams(value={IBinder.class, ServiceInfo.class, CompatibilityInfo.class, int.class})
    public static RefMethod<Void> scheduleCreateService;
    @MethodParams(value={IBinder.class, Intent.class, boolean.class, int.class})
    public static RefMethod<Void> scheduleBindService;
}

