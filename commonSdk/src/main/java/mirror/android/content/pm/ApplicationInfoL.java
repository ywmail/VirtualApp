/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 */
package mirror.android.content.pm;

import android.content.pm.ApplicationInfo;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefObject;

public class ApplicationInfoL {
    public static Class<?> TYPE = RefClass.load(ApplicationInfoL.class, ApplicationInfo.class);
    public static RefObject<String> primaryCpuAbi;
    public static RefObject<String> scanPublicSourceDir;
    public static RefObject<String> scanSourceDir;
    public static RefObject<String> secondaryCpuAbi;
    public static RefObject<String> secondaryNativeLibraryDir;
    public static RefObject<String> nativeLibraryRootDir;
    public static RefBoolean nativeLibraryRootRequiresIsa;
    public static RefObject<String[]> splitPublicSourceDirs;
    public static RefObject<String[]> splitSourceDirs;
}

