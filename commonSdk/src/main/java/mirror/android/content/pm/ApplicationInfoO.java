/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 *  android.util.SparseArray
 */
package mirror.android.content.pm;

import android.content.pm.ApplicationInfo;
import android.util.SparseArray;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class ApplicationInfoO {
    public static Class<?> TYPE = RefClass.load(ApplicationInfoO.class, ApplicationInfo.class);
    public static RefObject<SparseArray<int[]>> splitDependencies;
    public static RefObject<String[]> splitNames;
    public static RefObject<String[]> splitClassLoaderNames;
    public static RefObject<String[]> resourceDirs;
    public static RefObject<String[]> overlayPaths;
    public static RefMethod<String[]> getAllApkPaths;
}

