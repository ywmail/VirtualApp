/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager
 */
package mirror.android.app;

import android.content.Context;
import android.content.pm.PackageManager;
import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class ContextImpl {
    public static Class<?> TYPE = RefClass.load(ContextImpl.class, "android.app.ContextImpl");
    @MethodParams(value={Context.class})
    public static RefObject<String> mBasePackageName;
    public static RefObject<Object> mPackageInfo;
    public static RefObject<PackageManager> mPackageManager;
    public static RefMethod<Context> getReceiverRestrictedContext;
    @MethodParams(value={Context.class})
    public static RefMethod<Void> setOuterContext;
    public static RefMethod<Object> getAttributionSource;
    public static RefObject<Object> mAttributionSource;
    public static RefMethod<File> getNoBackupFilesDir;
    public static RefMethod<File> getCodeCacheDir;
    public static RefMethod<File> getCacheDir;
    public static RefMethod<File> getExternalCacheDir;
    public static RefMethod<Integer> checkCallingPermission;

    public static Object getAttributionSource(Object obj) {
        RefMethod<Object> field = getAttributionSource;
        if (field != null) {
            return field.call(obj, new Object[0]);
        }
        return null;
    }

    public static Object mAttributionSource(Object obj) {
        RefObject<Object> field = mAttributionSource;
        if (field != null) {
            return field.get(obj);
        }
        return null;
    }

    public static void mAttributionSource(Object obj, Object obj2) {
        RefObject<Object> field = mAttributionSource;
        if (field != null) {
            field.set(obj, obj2);
        }
    }
}

