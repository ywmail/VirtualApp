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

public class ActivityThreadQ {
    public static Class<?> Class = RefClass.load(ActivityThreadQ.class, "android.app.ActivityThread");
    @MethodParams(value={IBinder.class, List.class})
    public static RefMethod<Void> handleNewIntent;
}

