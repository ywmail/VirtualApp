/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 */
package mirror.android.app;

import android.os.IBinder;
import com.lody.virtual.StringFog;
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class ActivityThreadNMR1 {
    public static Class<?> Class = RefClass.load(ActivityThreadNMR1.class, "android.app.ActivityThread");
    @MethodParams(value={IBinder.class, List.class, boolean.class})
    public static RefMethod<Void> performNewIntents;
}

