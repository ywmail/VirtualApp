/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Build$VERSION
 *  android.os.Process
 *  android.util.DisplayMetrics
 */
package com.lody.virtual.helper.compat;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Process;
import android.util.DisplayMetrics;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VUserHandle;
import java.io.File;
import mirror.android.content.pm.PackageParser;
import mirror.android.content.pm.PackageParserJellyBean;
import mirror.android.content.pm.PackageParserJellyBean17;
import mirror.android.content.pm.PackageParserLollipop;
import mirror.android.content.pm.PackageParserLollipop22;
import mirror.android.content.pm.PackageParserMarshmallow;
import mirror.android.content.pm.PackageParserNougat;
import mirror.android.content.pm.PackageParserPie;
import mirror.android.content.pm.PackageParserTiramisu;
import mirror.android.content.pm.PackageUserState;
import mirror.android.content.pm.pkg.FrameworkPackageUserState;

public class PackageParserCompat {
    public static final int[] GIDS = VirtualCore.get().getGids();
    private static final int API_LEVEL = Build.VERSION.SDK_INT;
    private static final int myUserId = VUserHandle.getUserId(Process.myUid());
    private static final Object sUserState = PackageParserCompat.getUserState();

    public static Object getUserState() {
        if (BuildCompat.isTiramisu()) {
            return FrameworkPackageUserState.DEFAULT;
        }
        if (API_LEVEL >= 17) {
            return PackageUserState.ctor.newInstance();
        }
        return null;
    }

    public static android.content.pm.PackageParser createParser(File packageFile) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.ctor.newInstance();
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.ctor.newInstance();
        }
        if (API_LEVEL >= 22) {
            return PackageParserLollipop22.ctor.newInstance();
        }
        if (API_LEVEL >= 21) {
            return PackageParserLollipop.ctor.newInstance();
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.ctor.newInstance(packageFile.getAbsolutePath());
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.ctor.newInstance(packageFile.getAbsolutePath());
        }
        return PackageParser.ctor.newInstance(packageFile.getAbsolutePath());
    }

    public static android.content.pm.PackageParser.Package parsePackage(android.content.pm.PackageParser parser, File packageFile, int flags) throws Throwable {
        if (API_LEVEL >= 33) {
            return (android.content.pm.PackageParser.Package)PackageParserTiramisu.parsePackage.callWithException(parser, new Object[]{packageFile, flags});
        } else if (API_LEVEL >= 23) {
            return (android.content.pm.PackageParser.Package)PackageParserMarshmallow.parsePackage.callWithException(parser, new Object[]{packageFile, flags});
        } else if (API_LEVEL >= 22) {
            return (android.content.pm.PackageParser.Package)PackageParserLollipop22.parsePackage.callWithException(parser, new Object[]{packageFile, flags});
        } else if (API_LEVEL >= 21) {
            return (android.content.pm.PackageParser.Package)PackageParserLollipop.parsePackage.callWithException(parser, new Object[]{packageFile, flags});
        } else if (API_LEVEL >= 17) {
            return (android.content.pm.PackageParser.Package)PackageParserJellyBean17.parsePackage.callWithException(parser, new Object[]{packageFile, null, new DisplayMetrics(), flags});
        } else {
            return API_LEVEL >= 16 ? (android.content.pm.PackageParser.Package)PackageParserJellyBean.parsePackage.callWithException(parser, new Object[]{packageFile, null, new DisplayMetrics(), flags})
                    : (android.content.pm.PackageParser.Package)mirror.android.content.pm.PackageParser.parsePackage.callWithException(parser, new Object[]{packageFile, null, new DisplayMetrics(), flags});
        }
    }

    public static ServiceInfo generateServiceInfo(PackageParser.Service service, int flags) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.generateServiceInfo.call(service, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.generateServiceInfo.call(service, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 22) {
            return PackageParserLollipop22.generateServiceInfo.call(service, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 21) {
            return PackageParserLollipop.generateServiceInfo.call(service, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.generateServiceInfo.call(service, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.generateServiceInfo.call(service, flags, false, 1, myUserId);
        }
        return PackageParser.generateServiceInfo.call(service, flags);
    }

    public static ApplicationInfo generateApplicationInfo(PackageParser.Package p, int flags) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.generateApplicationInfo.call(p, flags, sUserState);
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.generateApplicationInfo.call(p, flags, sUserState);
        }
        if (API_LEVEL >= 22) {
            return PackageParserLollipop22.generateApplicationInfo.call(p, flags, sUserState);
        }
        if (API_LEVEL >= 21) {
            return PackageParserLollipop.generateApplicationInfo.call(p, flags, sUserState);
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.generateApplicationInfo.call(p, flags, sUserState);
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.generateApplicationInfo.call(p, flags, false, 1);
        }
        return PackageParser.generateApplicationInfo.call(p, flags);
    }

    public static ActivityInfo generateActivityInfo(PackageParser.Activity activity, int flags) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.generateActivityInfo.call(activity, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.generateActivityInfo.call(activity, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 22) {
            return PackageParserLollipop22.generateActivityInfo.call(activity, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 21) {
            return PackageParserLollipop.generateActivityInfo.call(activity, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.generateActivityInfo.call(activity, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.generateActivityInfo.call(activity, flags, false, 1, myUserId);
        }
        return PackageParser.generateActivityInfo.call(activity, flags);
    }

    public static ProviderInfo generateProviderInfo(PackageParser.Provider provider, int flags) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.generateProviderInfo.call(provider, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.generateProviderInfo.call(provider, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 22) {
            return PackageParserLollipop22.generateProviderInfo.call(provider, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 21) {
            return PackageParserLollipop.generateProviderInfo.call(provider, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.generateProviderInfo.call(provider, flags, sUserState, myUserId);
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.generateProviderInfo.call(provider, flags, false, 1, myUserId);
        }
        return PackageParser.generateProviderInfo.call(provider, flags);
    }

    public static PackageInfo generatePackageInfo(PackageParser.Package p, int flags, long firstInstallTime, long lastUpdateTime) {
        if (API_LEVEL >= 33) {
            return PackageParserTiramisu.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
        }
        if (API_LEVEL >= 23) {
            return PackageParserMarshmallow.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
        }
        if (API_LEVEL >= 21) {
            if (PackageParserLollipop22.generatePackageInfo != null) {
                return PackageParserLollipop22.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
            }
            return PackageParserLollipop.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
        }
        if (API_LEVEL >= 17) {
            return PackageParserJellyBean17.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
        }
        if (API_LEVEL >= 16) {
            return PackageParserJellyBean.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null);
        }
        return PackageParser.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime);
    }

    public static void collectCertificates(android.content.pm.PackageParser parser, android.content.pm.PackageParser.Package p, int flags) throws Throwable {
        if (BuildCompat.isPie()) {
            PackageParserPie.collectCertificates.callWithException(p, true);
        } else if (API_LEVEL >= 24) {
            PackageParserNougat.collectCertificates.callWithException(p, flags);
        } else if (API_LEVEL >= 23) {
            PackageParserMarshmallow.collectCertificates.callWithException(parser, p, flags);
        } else if (API_LEVEL >= 22) {
            PackageParserLollipop22.collectCertificates.callWithException(parser, p, flags);
        } else if (API_LEVEL >= 21) {
            PackageParserLollipop.collectCertificates.callWithException(parser, p, flags);
        } else if (API_LEVEL >= 17) {
            PackageParserJellyBean17.collectCertificates.callWithException(parser, p, flags);
        } else if (API_LEVEL >= 16) {
            PackageParserJellyBean.collectCertificates.callWithException(parser, p, flags);
        } else {
            PackageParser.collectCertificates.call(parser, p, flags);
        }
    }
}

