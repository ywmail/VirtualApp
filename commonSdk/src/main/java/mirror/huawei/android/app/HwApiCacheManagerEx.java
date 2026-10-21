/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.PackageManager
 */
package mirror.huawei.android.app;

import android.content.pm.PackageManager;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticMethod;

public class HwApiCacheManagerEx {
    public static Class<?> TYPE = RefClass.load(HwApiCacheManagerEx.class, "huawei.android.app.HwApiCacheMangerEx");
    public static RefMethod<Object> disableCache;
    public static RefStaticMethod<Object> getDefault;
    public static RefObject<PackageManager> mPkg;

    public static void disableCache(Object obj) {
        RefMethod<Object> method = disableCache;
        if (method != null) {
            method.call(obj, new Object[0]);
        }
    }
}

