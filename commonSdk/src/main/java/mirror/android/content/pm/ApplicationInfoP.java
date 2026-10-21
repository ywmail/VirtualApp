/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 */
package mirror.android.content.pm;

import android.content.pm.ApplicationInfo;
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefObject;

public class ApplicationInfoP {
    public static Class<?> TYPE = RefClass.load(ApplicationInfoP.class, ApplicationInfo.class);
    @MethodParams(value={int.class})
    public static RefObject<List<Object>> sharedLibraryInfos;

    public static List sharedLibraryInfos(ApplicationInfo applicationInfo) {
        RefObject<List<Object>> field = sharedLibraryInfos;
        if (field != null) {
            return field.get(applicationInfo);
        }
        return null;
    }

    public static void sharedLibraryInfos(ApplicationInfo applicationInfo, List list) {
        RefObject<List<Object>> field = sharedLibraryInfos;
        if (field != null) {
            field.set(applicationInfo, list);
        }
    }
}

