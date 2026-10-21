/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.os.IBinder
 */
package mirror.android.app;

import android.content.ComponentName;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IServiceConnectionO {
    public static Class<?> TYPE = RefClass.load(IServiceConnectionO.class, "android.app.IServiceConnection");
    @MethodParams(value={ComponentName.class, IBinder.class, boolean.class})
    public static RefMethod<Void> connected;
}

