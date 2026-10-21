/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.pm.ActivityInfo
 */
package com.lody.virtual.client.stub;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.stub.ResolverActivity;
import com.lody.virtual.client.stub.ShadowActivity;
import com.lody.virtual.client.stub.ShadowContentProvider;
import com.lody.virtual.client.stub.ShadowDialogActivity;
import com.lody.virtual.client.stub.ShadowJobService;
import com.lody.virtual.client.stub.ShadowService;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.pm.VPackageManagerService;
import java.util.Locale;

public class StubManifest {
    public static String PACKAGE_NAME = null;
    public static String EXT_PACKAGE_NAME = null;
    public static final boolean BLOCK_GMS_CHIMERA = false;
    public static String STUB_ACTIVITY = ShadowActivity.class.getName();
    public static String STUB_DIALOG = ShadowDialogActivity.class.getName();
    public static String STUB_CP = ShadowContentProvider.class.getName();
    public static String STUB_JOB = ShadowJobService.class.getName();
    public static String STUB_SERVICE = ShadowService.class.getName();
    public static String RESOLVER_ACTIVITY = ResolverActivity.class.getName();
    public static String STUB_CP_AUTHORITY = null;
    public static String EXT_STUB_CP_AUTHORITY = null;
    public static String PROXY_CP_AUTHORITY = null;
    public static String EXT_PROXY_CP_AUTHORITY = null;
    public static int STUB_COUNT = 100;

    public static boolean isFixedOrientationLandscape(ActivityInfo activityInfo) {
        return activityInfo.screenOrientation == 0 || activityInfo.screenOrientation == 6 || activityInfo.screenOrientation == 8 || activityInfo.screenOrientation == 11;
    }

    public static String getStubActivityName(int index, ActivityInfo targetInfo) {
        try {
            ComponentName name = new ComponentName(targetInfo.packageName, targetInfo.name);
            ActivityInfo info = VPackageManagerService.get().getActivityInfo(name, 0, 0);
            boolean isFixedOrientationLandscape = StubManifest.isFixedOrientationLandscape(info);
            VLog.d("VA-", "getStubActivityName isFixedOrientationLandscape:" + isFixedOrientationLandscape + ",info:" + info + ",index:" + index, new Object[0]);
            if (isFixedOrientationLandscape) {
                return String.format(Locale.ENGLISH, "%s$P%d_Land", STUB_ACTIVITY, index);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(Locale.ENGLISH, "%s$P%d", STUB_ACTIVITY, index);
    }

    public static String getStubDialogName(int index, ActivityInfo targetInfo) {
        try {
            ComponentName name = new ComponentName(targetInfo.packageName, targetInfo.name);
            ActivityInfo info = VPackageManagerService.get().getActivityInfo(name, 0, 0);
            boolean isFixedOrientationLandscape = StubManifest.isFixedOrientationLandscape(info);
            VLog.d("VA-", "getStubDialogName isFixedOrientationLandscape:" + isFixedOrientationLandscape + ",info:" + info + ",index:" + index, new Object[0]);
            if (isFixedOrientationLandscape) {
                return String.format(Locale.ENGLISH, "%s$P%d_Land", STUB_DIALOG, index);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(Locale.ENGLISH, "%s$P%d", STUB_DIALOG, index);
    }

    public static String getStubContentProviderName(int index) {
        return String.format(Locale.ENGLISH, "%s$P%d", STUB_CP, index);
    }

    public static String getStubServiceName(int index) {
        return String.format(Locale.ENGLISH, "%s$P%d", STUB_SERVICE, index);
    }

    public static String getStubAuthority(int index, boolean isExt) {
        return String.format(Locale.ENGLISH, "%s%d", isExt ? EXT_STUB_CP_AUTHORITY : STUB_CP_AUTHORITY, index);
    }

    public static String getProxyAuthority(boolean isExt) {
        return isExt ? EXT_PROXY_CP_AUTHORITY : PROXY_CP_AUTHORITY;
    }

    public static String getStubPackageName(boolean isExt) {
        return isExt ? EXT_PACKAGE_NAME : PACKAGE_NAME;
    }

    public static boolean isHostPackageName(String packageName) {
        return PACKAGE_NAME.equals(packageName) || EXT_PACKAGE_NAME.equals(packageName);
    }
}

