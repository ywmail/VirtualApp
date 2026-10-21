/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.PackageManager
 *  android.os.IInterface
 */
package mirror.android.content.pm;

import android.content.pm.PackageManager;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;
import mirror.com.android.internal.os.UserManager;

public class LauncherApps {
    public static Class<?> TYPE = RefClass.load(LauncherApps.class, "android.content.pm.LauncherApps");
    public static RefObject<PackageManager> mPm;
    public static RefObject<IInterface> mService;
    public static RefObject<UserManager> mUserManager;
}

