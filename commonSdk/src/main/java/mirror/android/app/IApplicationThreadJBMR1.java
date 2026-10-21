/*
 * Decompiled with CFR 0.152.
 */
package mirror.android.app;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IApplicationThreadJBMR1 {
    public static Class<?> TYPE = RefClass.load(IApplicationThreadJBMR1.class, "android.app.IApplicationThread");
    @MethodReflectParams(value={"android.content.Intent", "android.content.pm.ActivityInfo", "android.content.res.CompatibilityInfo", "int", "java.lang.String", "android.os.Bundle", "boolean", "int"})
    public static RefMethod<Void> scheduleReceiver;
}

