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
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IApplicationThread {
    public static Class<?> TYPE = RefClass.load(IApplicationThread.class, "android.app.IApplicationThread");
    @MethodParams(value={List.class, IBinder.class})
    public static RefMethod<Void> scheduleNewIntent;
    @MethodParams(value={IBinder.class, ServiceInfo.class})
    public static RefMethod<Void> scheduleCreateService;
    @MethodParams(value={IBinder.class, Intent.class, boolean.class})
    public static RefMethod<Void> scheduleBindService;
    @MethodParams(value={IBinder.class, Intent.class})
    public static RefMethod<Void> scheduleUnbindService;
    @MethodParams(value={IBinder.class, int.class, int.class, Intent.class})
    public static RefMethod<Void> scheduleServiceArgs;
    @MethodParams(value={IBinder.class})
    public static RefMethod<Void> scheduleStopService;
}

