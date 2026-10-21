/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.IBinder
 */
package mirror.android.app;

import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IActivityManagerL {
    public static Class<?> TYPE = RefClass.load(IActivityManagerL.class, "android.app.IActivityManager");
    @MethodParams(value={IBinder.class, int.class, Intent.class, boolean.class})
    public static RefMethod<Boolean> finishActivity;
}

