/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 */
package mirror.android.content.res;

import android.content.pm.ApplicationInfo;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefStaticObject;

public class CompatibilityInfo {
    public static Class<?> TYPE = RefClass.load(CompatibilityInfo.class, "android.content.res.CompatibilityInfo");
    @MethodParams(value={ApplicationInfo.class, int.class, int.class, boolean.class})
    public static RefConstructor ctor;
    @MethodParams(value={ApplicationInfo.class, int.class, int.class, boolean.class, int.class})
    public static RefConstructor ctorLG;
    public static RefStaticObject<Object> DEFAULT_COMPATIBILITY_INFO;
}

