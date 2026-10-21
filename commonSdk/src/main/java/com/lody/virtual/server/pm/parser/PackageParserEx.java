//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.pm.parser;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.SparseArray;
import com.lody.virtual.GmsSupport;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.fixer.ComponentFixer;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.PackageParserCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.RefObjUtil;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.server.pm.ComponentStateManager;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;
import com.lody.virtual.server.pm.PackageUserState;
import com.lody.virtual.server.pm.VAppManagerService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import mirror.android.content.pm.ApplicationInfoL;
import mirror.android.content.pm.ApplicationInfoN;
import mirror.android.content.pm.ApplicationInfoO;
import mirror.android.content.pm.PackageInfoPie;
import mirror.android.content.pm.SigningInfo;
import mirror.android.content.pm.SigningInfoT;
import mirror.android.content.pm.PackageParser.Package;
import mirror.android.content.pm.PackageParser.SigningDetails;

public class PackageParserEx {
    public static final int GET_SIGNING_CERTIFICATES = 134217728;
    public static final String ORG_APACHE_HTTP_LEGACY = "org.apache.http.legacy";
    private static final String TAG = PackageParserEx.class.getSimpleName();
    static SparseArray sparseArray = new SparseArray();

    public PackageParserEx() {
    }

    public static VPackage parsePackage(File packageFile) throws Throwable {
        PackageParser parser = PackageParserCompat.createParser(packageFile);
        if (BuildCompat.isQ()) {
            parser.setCallback(new PackageParser.CallbackImpl(VirtualCore.getPM()));
        }

        PackageParser.Package p = PackageParserCompat.parsePackage(parser, packageFile, 0);
        if (p.requestedPermissions.contains("android.permission.FAKE_PACKAGE_SIGNATURE") && p.mAppMetaData != null && p.mAppMetaData.containsKey("fake-signature")) {
            String sig = p.mAppMetaData.getString("fake-signature");
            buildSignature(p, new Signature[]{new Signature(sig)});
            VLog.d(TAG, "Using fake-signature feature on : " + p.packageName, new Object[0]);
        } else {
            try {
                int flag = 0;
                if (BuildCompat.isPie()) {
                    flag |= 16;
                } else {
                    flag |= 1;
                }

                PackageParserCompat.collectCertificates(parser, p, flag);
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }

        return buildPackageCache(p);
    }

    private static void buildSignature(PackageParser.Package p, Signature[] signatures) {
        if (BuildCompat.isPie()) {
            Object signingDetails = Package.mSigningDetails.get(p);
            SigningDetails.pastSigningCertificates.set(signingDetails, signatures);
            SigningDetails.signatures.set(signingDetails, signatures);
        } else {
            p.mSignatures = signatures;
        }
    }

    public static VPackage readPackageCache(String packageName) throws FileNotFoundException {
        Parcel p = Parcel.obtain();
        try {
            File cacheFile = VEnvironment.getPackageCacheFile(packageName);
            FileInputStream is = new FileInputStream(cacheFile);
            byte[] bytes = FileUtils.toByteArray(is);
            is.close();
            p.unmarshall(bytes, 0, bytes.length);
            p.setDataPosition(0);
            if (p.readInt() == 4) {
                VPackage pkg = new VPackage(p);
                PackageParserEx.addOwner(pkg);
                VPackage vPackage = pkg;
                return vPackage;
            }
            try {
                throw new IllegalStateException("Invalid version.");
            }
            catch (Exception e) {
                e.printStackTrace();
                p.recycle();
                VPackage vPackage = null;
                return vPackage;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            p.recycle();
        }
    }

    public static void readSignature(VPackage pkg) {
        File signatureFile = VEnvironment.getSignatureFile(pkg.packageName);
        if (signatureFile.exists()) {
            Parcel p = Parcel.obtain();

            try {
                try {
                    FileInputStream fis = new FileInputStream(signatureFile);
                    byte[] bytes = FileUtils.toByteArray(fis);
                    fis.close();
                    p.unmarshall(bytes, 0, bytes.length);
                    p.setDataPosition(0);
                    if (BuildCompat.isPie()) {
                        try {
                            PackageParser.SigningDetails sigDetail = (PackageParser.SigningDetails)((Parcelable.Creator)SigningDetails.CREATOR.get()).createFromParcel(p);
                            pkg.mSigningDetails = sigDetail;
                            if (sigDetail.pastSigningCertificates != null && sigDetail.pastSigningCertificates.length > 0) {
                                pkg.mSignatures = new Signature[1];
                                pkg.mSignatures[0] = sigDetail.pastSigningCertificates[0];
                            } else {
                                pkg.mSignatures = sigDetail.signatures;
                            }
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }

                    if (pkg.mSigningDetails == null || pkg.mSignatures == null) {
                        p.setDataPosition(0);
                        pkg.mSignatures = (Signature[])p.createTypedArray(Signature.CREATOR);
                        pkg.mSigningDetails = null;
                    }
                } finally {
                    p.recycle();
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

        }
    }

    public static void savePackageCache(VPackage pkg) {
        String packageName = pkg.packageName;
        File cacheFile = VEnvironment.getPackageCacheFile(packageName);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }

        File signatureFile = VEnvironment.getSignatureFile(packageName);
        if (signatureFile.exists()) {
            signatureFile.delete();
        }

        Parcel p = Parcel.obtain();

        try {
            p.writeInt(4);
            pkg.writeToParcel(p, 0);
            FileOutputStream fos = new FileOutputStream(cacheFile);
            fos.write(p.marshall());
            fos.close();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        p.recycle();
        Signature[] signatures = pkg.mSignatures;
        Object obj = pkg.mSigningDetails;
        Object sig = obj == null ? signatures : obj;
        if (sig != null) {
            if (signatureFile.exists() && !signatureFile.delete()) {
                VLog.w(TAG, "Unable to delete the signatures of " + packageName, new Object[0]);
            }

            p = Parcel.obtain();

            try {
                if (sig instanceof PackageParser.SigningDetails) {
                    SigningDetails.writeToParcel.call(obj, new Object[]{p, 0});
                } else {
                    p.writeTypedArray(signatures, 0);
                }

                FileUtils.writeParcelToFile(p, signatureFile);
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

    }

    private static VPackage buildPackageCache(PackageParser.Package p) {
        VPackage cache = new VPackage();
        cache.activities = new ArrayList(p.activities.size());
        cache.services = new ArrayList(p.services.size());
        cache.receivers = new ArrayList(p.receivers.size());
        cache.providers = new ArrayList(p.providers.size());
        cache.instrumentation = new ArrayList(p.instrumentation.size());
        cache.permissions = new ArrayList(p.permissions.size());
        cache.permissionGroups = new ArrayList(p.permissionGroups.size());
        Iterator it = p.activities.iterator();

        while(it.hasNext()) {
            PackageParser.Activity activity = (PackageParser.Activity)it.next();
            cache.activities.add(new VPackage.ActivityComponent(activity));
        }

        Iterator it2 = p.services.iterator();

        while(it2.hasNext()) {
            PackageParser.Service service = (PackageParser.Service)it2.next();
            cache.services.add(new VPackage.ServiceComponent(service));
        }

        Iterator it3 = p.receivers.iterator();

        while(it3.hasNext()) {
            PackageParser.Activity receiver = (PackageParser.Activity)it3.next();
            cache.receivers.add(new VPackage.ActivityComponent(receiver));
        }

        Iterator it4 = p.providers.iterator();

        while(it4.hasNext()) {
            PackageParser.Provider provider = (PackageParser.Provider)it4.next();
            cache.providers.add(new VPackage.ProviderComponent(provider));
        }

        Iterator it5 = p.instrumentation.iterator();

        while(it5.hasNext()) {
            PackageParser.Instrumentation instrumentation = (PackageParser.Instrumentation)it5.next();
            cache.instrumentation.add(new VPackage.InstrumentationComponent(instrumentation));
        }

        Iterator it6 = p.permissions.iterator();

        while(it6.hasNext()) {
            PackageParser.Permission permission = (PackageParser.Permission)it6.next();
            cache.permissions.add(new VPackage.PermissionComponent(permission));
        }

        Iterator it7 = p.permissionGroups.iterator();

        while(it7.hasNext()) {
            PackageParser.PermissionGroup permissionGroup = (PackageParser.PermissionGroup)it7.next();
            cache.permissionGroups.add(new VPackage.PermissionGroupComponent(permissionGroup));
        }

        cache.requestedPermissions = new ArrayList(p.requestedPermissions.size());
        cache.requestedPermissions.addAll(p.requestedPermissions);
        List protectedBroadcasts;
        if (Package.protectedBroadcasts != null && (protectedBroadcasts = (List)Package.protectedBroadcasts.get(p)) != null) {
            cache.protectedBroadcasts = new ArrayList(protectedBroadcasts);
            cache.protectedBroadcasts.addAll(protectedBroadcasts);
        }

        cache.applicationInfo = p.applicationInfo;
        if (BuildCompat.isPie()) {
            cache.mSigningDetails = p.mSigningDetails;
            if (p.mSigningDetails.pastSigningCertificates != null && p.mSigningDetails.pastSigningCertificates.length > 0) {
                cache.mSignatures = new Signature[1];
                cache.mSignatures[0] = p.mSigningDetails.pastSigningCertificates[0];
            } else {
                cache.mSignatures = p.mSigningDetails.signatures;
            }
        } else {
            cache.mSignatures = p.mSignatures;
        }

        cache.mAppMetaData = p.mAppMetaData;
        cache.packageName = p.packageName;
        cache.mPreferredOrder = p.mPreferredOrder;
        cache.mVersionName = p.mVersionName;
        cache.mSharedUserId = p.mSharedUserId;
        cache.mSharedUserLabel = p.mSharedUserLabel;
        cache.usesLibraries = p.usesLibraries;
        cache.usesOptionalLibraries = p.usesOptionalLibraries;
        cache.mVersionCode = p.mVersionCode;
        cache.configPreferences = p.configPreferences;
        cache.reqFeatures = p.reqFeatures;
        fixSignatureAsSystem(cache);
        addOwner(cache);
        injectXposedModuleInfo(cache);
        updatePackageApache(cache);
        return cache;
    }

    private static void injectXposedModuleInfo(VPackage vPackage) {
        if (vPackage.mAppMetaData != null && vPackage.mAppMetaData.containsKey("xposedmodule")) {
            VPackage.XposedModule module = new VPackage.XposedModule();
            Object descriptionRaw = vPackage.mAppMetaData.get("xposeddescription");
            String descriptionTmp = null;
            if (descriptionRaw instanceof String) {
                descriptionTmp = ((String)descriptionRaw).trim();
            } else if (descriptionRaw instanceof Integer) {
                try {
                    int resId = (Integer)descriptionRaw;
                    if (resId != 0) {
                        descriptionTmp = VirtualCore.getPM().getResourcesForApplication(vPackage.applicationInfo).getString(resId).trim();
                    }
                } catch (Exception var5) {
                }
            }

            module.desc = descriptionTmp != null ? descriptionTmp : "";
            Object minVersionRaw = vPackage.mAppMetaData.get("xposedminversion");
            if (minVersionRaw instanceof Integer) {
                module.minVersion = (Integer)minVersionRaw;
            } else if (minVersionRaw instanceof String) {
                module.minVersion = extractIntPart((String)minVersionRaw);
            } else {
                module.minVersion = 0;
            }

            vPackage.xposedModule = module;
        }
    }

    private static int extractIntPart(String str) {
        int result = 0;
        int length = str.length();

        for(int offset = 0; offset < length; ++offset) {
            char c = str.charAt(offset);
            if ('0' > c || c > '9') {
                break;
            }

            result = result * 10 + (c - 48);
        }

        return result;
    }

    public static void initApplicationInfoBase(PackageSetting ps, VPackage p) {
        ApplicationInfo ai = p.applicationInfo;
        if (TextUtils.isEmpty(ai.processName)) {
            ai.processName = ai.packageName;
        }

        ai.enabled = true;
        ai.uid = ps.appId;
        ai.name = ComponentFixer.fixComponentClassName(ps.packageName, ai.name);
    }

    private static void initApplicationAsUser(ApplicationInfo ai, int userId, boolean isExt) {
        PackageSetting ps = PackageCacheManager.getSetting(ai.packageName);
        VPackage pkg = PackageCacheManager.get(ai.packageName);
        if (ps == null) {
            throw new IllegalStateException("failed to getSetting for:" + ai.packageName);
        } else {
            ApplicationInfo outsideAppInfo = null;

            try {
                outsideAppInfo = VirtualCore.get().getPackageManager().getApplicationInfo(ai.packageName, 0);
            } catch (Throwable var13) {
            }

            SettingConfig config = VirtualCore.getConfig();
            String splitName;
            if (isExt && !ps.dynamic) {
                ai.sourceDir = VEnvironment.getPackageFileExt(ai.packageName).getPath();
                ai.publicSourceDir = ai.sourceDir;
                File nativeLibraryRootDir = VEnvironment.getDataAppLibDirectoryExt(ai.packageName);
                File nativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(ps.primaryCpuAbi));
                ai.nativeLibraryDir = nativeLibraryDir.getPath();
                if (ps.secondaryCpuAbi != null) {
                    File secondaryNativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(ps.secondaryCpuAbi));
                    ApplicationInfoL.secondaryNativeLibraryDir.set(ai, secondaryNativeLibraryDir.getPath());
                }

                splitName = VEnvironment.getDataAppPackageDirectoryExt(ai.packageName).getAbsolutePath();
                ApplicationInfoL.scanSourceDir.set(ai, splitName);
                ApplicationInfoL.scanPublicSourceDir.set(ai, splitName);
            }

            if (!ps.dynamic && pkg.splitNames != null && !pkg.splitNames.isEmpty()) {
                if (VERSION.SDK_INT >= 26) {
                    ai.splitNames = (String[])pkg.splitNames.toArray(new String[0]);
                }

                List<String> splitSourceDirs = new ArrayList();
                Iterator it = pkg.splitNames.iterator();

                while(it.hasNext()) {
                    splitName = (String)it.next();
                    File file = isExt ? VEnvironment.getSplitPackageFileExt(ai.packageName, splitName) : VEnvironment.getSplitPackageFile(ai.packageName, splitName);
                    splitSourceDirs.add(file.getPath());
                }

                pkg.applicationInfo.splitSourceDirs = (String[])splitSourceDirs.toArray(new String[0]);
                pkg.applicationInfo.splitPublicSourceDirs = (String[])splitSourceDirs.toArray(new String[0]);
            }

            if (VERSION.SDK_INT >= 26 && outsideAppInfo != null && ps.dynamic) {
                ai.splitNames = outsideAppInfo.splitNames;
                SparseArray<int[]> splitDependencies = (SparseArray)RefObjUtil.getRefObjectValue(ApplicationInfoO.splitDependencies, outsideAppInfo);
                SparseArray<int[]> splitDependencies2 = (SparseArray)RefObjUtil.getRefObjectValue(ApplicationInfoO.splitDependencies, ai);
                if (splitDependencies != null && splitDependencies2 == null) {
                    RefObjUtil.setRefObjectValue(ApplicationInfoO.splitDependencies, ai, splitDependencies);
                }
            }

            String path;
            if (isExt) {
                path = VEnvironment.getDataUserPackageDirectoryExt(userId, ai.packageName).getPath();
            } else {
                path = VEnvironment.getDataUserPackageDirectory(userId, ai.packageName).getPath();
            }

            ai.dataDir = path;
            if (VERSION.SDK_INT >= 24) {
                String deDataDir;
                if (isExt) {
                    deDataDir = VEnvironment.getDeDataUserPackageDirectoryExt(userId, ai.packageName).getPath();
                } else {
                    deDataDir = VEnvironment.getDeDataUserPackageDirectory(userId, ai.packageName).getPath();
                }

                if (ApplicationInfoN.deviceEncryptedDataDir != null) {
                    ApplicationInfoN.deviceEncryptedDataDir.set(ai, deDataDir);
                }

                if (ApplicationInfoN.credentialEncryptedDataDir != null) {
                    ApplicationInfoN.credentialEncryptedDataDir.set(ai, ai.dataDir);
                }

                if (ApplicationInfoN.deviceProtectedDataDir != null) {
                    ApplicationInfoN.deviceProtectedDataDir.set(ai, deDataDir);
                }

                if (ApplicationInfoN.credentialProtectedDataDir != null) {
                    ApplicationInfoN.credentialProtectedDataDir.set(ai, ai.dataDir);
                }
            }

            if (config.isEnableIORedirect()) {
                if (config.isUseRealDataDir(ai.packageName)) {
                    ai.dataDir = "/data/data/" + ai.packageName + "/";
                }

                if (config.isUseRealLibDir(ai.packageName)) {
                    String secondaryCpuAbi;
                    if (isExt) {
                        ai.nativeLibraryDir = outsideAppInfo.nativeLibraryDir;
                        secondaryCpuAbi = (String)ApplicationInfoL.primaryCpuAbi.get(outsideAppInfo);
                        if (!TextUtils.isEmpty(secondaryCpuAbi)) {
                            ApplicationInfoL.primaryCpuAbi.set(ai, secondaryCpuAbi);
                        }
                    } else {
                        secondaryCpuAbi = outsideAppInfo.nativeLibraryDir;
                        if (secondaryCpuAbi != null) {
                            ai.nativeLibraryDir = secondaryCpuAbi;
                        }
                    }

                    secondaryCpuAbi = (String)RefObjUtil.getRefObjectValue(ApplicationInfoL.secondaryCpuAbi, outsideAppInfo);
                    if (!TextUtils.isEmpty(secondaryCpuAbi)) {
                        RefObjUtil.setRefObjectValue(ApplicationInfoL.secondaryCpuAbi, ai, secondaryCpuAbi);
                    }

                    String secondaryNativeLibraryDir = (String)RefObjUtil.getRefObjectValue(ApplicationInfoL.secondaryNativeLibraryDir, outsideAppInfo);
                    if (!TextUtils.isEmpty(secondaryNativeLibraryDir)) {
                        RefObjUtil.setRefObjectValue(ApplicationInfoL.secondaryNativeLibraryDir, ai, secondaryNativeLibraryDir);
                    }
                }
            }

        }
    }

    private static void addOwner(VPackage p) {
        Iterator it = p.activities.iterator();

        Iterator it5;
        while(it.hasNext()) {
            VPackage.ActivityComponent activity = (VPackage.ActivityComponent)it.next();
            activity.owner = p;

            VPackage.ActivityIntentInfo info;
            for(it5 = activity.intents.iterator(); it5.hasNext(); info.activity = activity) {
                info = (VPackage.ActivityIntentInfo)it5.next();
            }
        }

        Iterator it3 = p.services.iterator();

        Iterator it7;
        while(it3.hasNext()) {
            VPackage.ServiceComponent service = (VPackage.ServiceComponent)it3.next();
            service.owner = p;

            VPackage.ServiceIntentInfo info2;
            for(it7 = service.intents.iterator(); it7.hasNext(); info2.service = service) {
                info2 = (VPackage.ServiceIntentInfo)it7.next();
            }
        }

        it5 = p.receivers.iterator();

        Iterator it9;
        while(it5.hasNext()) {
            VPackage.ActivityComponent receiver = (VPackage.ActivityComponent)it5.next();
            receiver.owner = p;

            VPackage.ActivityIntentInfo info3;
            for(it9 = receiver.intents.iterator(); it9.hasNext(); info3.activity = receiver) {
                info3 = (VPackage.ActivityIntentInfo)it9.next();
            }
        }

        it7 = p.providers.iterator();

        Iterator it10;
        while(it7.hasNext()) {
            VPackage.ProviderComponent provider = (VPackage.ProviderComponent)it7.next();
            provider.owner = p;

            VPackage.ProviderIntentInfo info4;
            for(it10 = provider.intents.iterator(); it10.hasNext(); info4.provider = provider) {
                info4 = (VPackage.ProviderIntentInfo)it10.next();
            }
        }

        VPackage.InstrumentationComponent instrumentation;
        for(it9 = p.instrumentation.iterator(); it9.hasNext(); instrumentation.owner = p) {
            instrumentation = (VPackage.InstrumentationComponent)it9.next();
        }

        VPackage.PermissionComponent permission;
        for(it10 = p.permissions.iterator(); it10.hasNext(); permission.owner = p) {
            permission = (VPackage.PermissionComponent)it10.next();
        }

        VPackage.PermissionGroupComponent group;
        for(Iterator it11 = p.permissionGroups.iterator(); it11.hasNext(); group.owner = p) {
            group = (VPackage.PermissionGroupComponent)it11.next();
        }

        int flags = 4;
        if (GmsSupport.isGoogleService(p.packageName)) {
            flags = 12;
        }

        ApplicationInfo var10000 = p.applicationInfo;
        var10000.flags |= flags;
    }

    public static PackageInfo generatePackageInfo(VPackage p, PackageSetting ps, int flags, long firstInstallTime, long lastUpdateTime, PackageUserState state, int userId, boolean isExt) {
        if (!checkUseInstalledOrHidden(state, flags)) {
            return null;
        } else {
            if (p.mSignatures == null && p.mSigningDetails == null) {
                readSignature(p);
            }

            PackageInfo pi = new PackageInfo();
            pi.packageName = p.packageName;
            pi.versionCode = p.mVersionCode;
            pi.sharedUserLabel = p.mSharedUserLabel;
            pi.versionName = p.mVersionName;
            pi.sharedUserId = p.mSharedUserId;
            pi.applicationInfo = generateApplicationInfo(p, flags, state, userId, isExt);
            pi.firstInstallTime = firstInstallTime;
            pi.lastUpdateTime = lastUpdateTime;
            if (p.requestedPermissions != null && !p.requestedPermissions.isEmpty()) {
                String[] requestedPermissions = new String[p.requestedPermissions.size()];
                p.requestedPermissions.toArray(requestedPermissions);
                pi.requestedPermissions = requestedPermissions;
            }

            if ((flags & 256) != 0) {
                pi.gids = PackageParserCompat.GIDS;
            }

            int num;
            int N6;
            if ((flags & 16384) != 0) {
                N6 = p.configPreferences != null ? p.configPreferences.size() : 0;
                if (N6 > 0) {
                    pi.configPreferences = new ConfigurationInfo[N6];
                    p.configPreferences.toArray(pi.configPreferences);
                }

                num = p.reqFeatures != null ? p.reqFeatures.size() : 0;
                if (num > 0) {
                    pi.reqFeatures = new FeatureInfo[num];
                    p.reqFeatures.toArray(pi.reqFeatures);
                }
            }

            N6 = flags & 1;
            int N5;
            int num3;
            ActivityInfo activityInfo2;
            if (N6 != 0 && (N5 = p.activities.size()) > 0) {
                num = 0;
                ActivityInfo[] res = new ActivityInfo[N5];

                for(num3 = 0; num3 < N5; N5 = N5) {
                    VPackage.ActivityComponent a = (VPackage.ActivityComponent)p.activities.get(num3);
                    if (ps.isEnabledAndMatchLPr(a.info, flags, userId)) {
                        activityInfo2 = generateActivityInfo(a, flags, state, userId, isExt);
                        activityInfo2.applicationInfo = pi.applicationInfo;
                        res[num] = activityInfo2;
                        ++num;
                    }

                    ++num3;
                }

                pi.activities = (ActivityInfo[])ArrayUtils.trimToSize(res, num);
            }

            num = flags & 2;
            int N4;
            int num2;
            int num4;
            if (num != 0 && (N4 = p.receivers.size()) > 0) {
                num2 = 0;
                ActivityInfo[] res2 = new ActivityInfo[N4];

                for(num4 = 0; num4 < N4; ++num4) {
                    VPackage.ActivityComponent a2 = (VPackage.ActivityComponent)p.receivers.get(num4);
                    if (ps.isEnabledAndMatchLPr(a2.info, flags, userId)) {
                        activityInfo2 = generateActivityInfo(a2, flags, state, userId, isExt);
                        activityInfo2.applicationInfo = pi.applicationInfo;
                        res2[num2] = activityInfo2;
                        ++num2;
                    }
                }

                pi.receivers = (ActivityInfo[])ArrayUtils.trimToSize(res2, num2);
            }

            num2 = flags & 4;
            int N3;
            int i5;
            if (num2 != 0 && (N3 = p.services.size()) > 0) {
                num3 = 0;
                ServiceInfo[] res3 = new ServiceInfo[N3];

                for(i5 = 0; i5 < N3; ++i5) {
                    VPackage.ServiceComponent s = (VPackage.ServiceComponent)p.services.get(i5);
                    if (ps.isEnabledAndMatchLPr(s.info, flags, userId)) {
                        ServiceInfo serviceInfo = generateServiceInfo(s, flags, state, userId, isExt);
                        serviceInfo.applicationInfo = pi.applicationInfo;
                        res3[num3] = serviceInfo;
                        ++num3;
                    }
                }

                pi.services = (ServiceInfo[])ArrayUtils.trimToSize(res3, num3);
            }

            num3 = flags & 8;
            int N2;
            int N15;
            if (num3 != 0 && (N2 = p.providers.size()) > 0) {
                num4 = 0;
                ProviderInfo[] res4 = new ProviderInfo[N2];

                for(N15 = 0; N15 < N2; ++N15) {
                    VPackage.ProviderComponent pr = (VPackage.ProviderComponent)p.providers.get(N15);
                    if (ps.isEnabledAndMatchLPr(pr.info, flags, userId)) {
                        ProviderInfo providerInfo = generateProviderInfo(pr, flags, state, userId, isExt);
                        providerInfo.applicationInfo = pi.applicationInfo;
                        res4[num4] = providerInfo;
                        ++num4;
                    }
                }

                pi.providers = (ProviderInfo[])ArrayUtils.trimToSize(res4, num4);
            }

            num4 = flags & 16;
            int N;
            if (num4 != 0 && (N = p.instrumentation.size()) > 0) {
                pi.instrumentation = new InstrumentationInfo[N];

                for(i5 = 0; i5 < N; ++i5) {
                    pi.instrumentation[i5] = generateInstrumentationInfo((VPackage.InstrumentationComponent)p.instrumentation.get(i5), flags);
                }
            }

            i5 = flags & 4096;
            int N16;
            if (i5 != 0) {
                N15 = p.permissions.size();
                if (N15 > 0) {
                    pi.permissions = new PermissionInfo[N15];

                    for(N16 = 0; N16 < N15; ++N16) {
                        pi.permissions[N16] = generatePermissionInfo((VPackage.PermissionComponent)p.permissions.get(N16), flags);
                    }
                }

                N16 = p.requestedPermissions == null ? 0 : p.requestedPermissions.size();
                if (N16 > 0) {
                    pi.requestedPermissions = new String[N16];
                    pi.requestedPermissionsFlags = new int[N16];
                    List<String> hostRequestedPermissions = null;
                    int[] hostRequestedPermissionsFlags = null;

                    try {
                        VAppManagerService.get().isRunInExtProcess(p.packageName);
                        String hostPkg = StubManifest.EXT_PACKAGE_NAME;
                        PackageInfo hostInfo = VirtualCore.get().getContext().getPackageManager().getPackageInfo(hostPkg, 4096);
                        hostRequestedPermissions = Arrays.asList(hostInfo.requestedPermissions);
                        hostRequestedPermissionsFlags = hostInfo.requestedPermissionsFlags;
                    } catch (Exception var30) {
                        var30.printStackTrace();
                    }

                    for(int i7 = 0; i7 < N16; ++i7) {
                        String perm = (String)p.requestedPermissions.get(i7);
                        pi.requestedPermissions[i7] = perm;
                        if (hostRequestedPermissions != null) {
                            int permissionIndex = hostRequestedPermissions.indexOf(perm);
                            if (permissionIndex >= 0) {
                                pi.requestedPermissionsFlags[i7] = hostRequestedPermissionsFlags[permissionIndex];
                            } else {
                                pi.requestedPermissionsFlags[i7] = -1;
                            }
                        }
                    }
                }
            }

            N15 = flags & 64;
            if (N15 != 0) {
                N16 = p.mSignatures != null ? p.mSignatures.length : 0;
                if (N16 <= 0) {
                    try {
                        PackageInfo outInfo = VirtualCore.get().getHostPackageManager().getPackageInfo(p.packageName, 64L);
                        pi.signatures = outInfo.signatures;
                    } catch (PackageManager.NameNotFoundException var29) {
                        var29.printStackTrace();
                    }
                } else {
                    pi.signatures = new Signature[N16];
                    System.arraycopy(p.mSignatures, 0, pi.signatures, 0, N16);
                }
            }

            if (BuildCompat.isPie() && (134217728 & flags) != 0) {
                if (p.mSigningDetails != null) {
                    if (BuildCompat.isTiramisu()) {
                        Object signingInfo = SigningInfoT.createSigningInfo(p.mSigningDetails);
                        PackageInfoPie.signingInfo.set(pi, signingInfo);
                    } else {
                        PackageInfoPie.signingInfo.set(pi, SigningInfo.ctor.newInstance(new Object[]{p.mSigningDetails}));
                    }

                    pi.signatures = p.mSigningDetails.signatures;
                } else if (p.mSignatures != null) {
                    PackageParser.SigningDetails details = new PackageParser.SigningDetails();
                    SigningDetails.pastSigningCertificates.set(details, p.mSignatures);
                    SigningDetails.signatures.set(details, p.mSignatures);
                    PackageInfoPie.signingInfo.set(pi, SigningInfo.ctor.newInstance(new Object[]{details}));
                    pi.signatures = p.mSigningDetails.signatures;
                }
            }

            VPackage vPackage = new VPackage();
            vPackage.packageName = pi.packageName;
            fixSignatureAsSystem(vPackage);
            if (vPackage.mSignatures != null) {
                pi.signatures = vPackage.mSignatures;
            }

            return pi;
        }
    }

    public static void fixSignatureAsSystem(VPackage vPackage) {
        try {
            PackageInfo outsideInfo = VirtualCore.get().getPackageManager().getPackageInfo(vPackage.packageName, 134217792);
            vPackage.mSignatures = outsideInfo.signatures;
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public static ApplicationInfo generateApplicationInfo(VPackage p, int flags, PackageUserState state, int userId, boolean isExt) {
        if (p != null && checkUseInstalledOrHidden(state, flags)) {
            ApplicationInfo ai = new ApplicationInfo(p.applicationInfo);
            if ((flags & 128) != 0) {
                ai.metaData = p.mAppMetaData;
            }

            initApplicationAsUser(ai, userId, isExt);
            return ai;
        } else {
            return null;
        }
    }

    public static ActivityInfo generateActivityInfo(VPackage.ActivityComponent a, int flags, PackageUserState state, int userId, boolean isExt) {
        if (a != null && checkUseInstalledOrHidden(state, flags)) {
            ActivityInfo ai = new ActivityInfo(a.info);
            if ((flags & 128) != 0 && a.metaData != null) {
                ai.metaData = a.metaData;
            }

            ai.enabled = isEnabledLPr(a.info, 0, userId);
            ai.applicationInfo = generateApplicationInfo(a.owner, flags, state, userId, isExt);
            return ai;
        } else {
            return null;
        }
    }

    public static boolean isEnabledLPr(ComponentInfo componentInfo, int flags, int userId) {
        ComponentName component = ComponentUtils.toComponentName(componentInfo);
        int state = ComponentStateManager.user(userId).get(component);
        if (state == 0) {
            return componentInfo.enabled;
        } else {
            return state != 2 && state != 4 && state != 3;
        }
    }

    public static ServiceInfo generateServiceInfo(VPackage.ServiceComponent s, int flags, PackageUserState state, int userId, boolean isExt) {
        if (s != null && checkUseInstalledOrHidden(state, flags)) {
            ServiceInfo si = new ServiceInfo(s.info);
            if ((flags & 128) != 0 && s.metaData != null) {
                si.metaData = s.metaData;
            }

            si.enabled = isEnabledLPr(s.info, 0, userId);
            si.applicationInfo = generateApplicationInfo(s.owner, flags, state, userId, isExt);
            return si;
        } else {
            return null;
        }
    }

    public static ProviderInfo generateProviderInfo(VPackage.ProviderComponent p, int flags, PackageUserState state, int userId, boolean isExt) {
        if (p != null && checkUseInstalledOrHidden(state, flags)) {
            ProviderInfo pi = new ProviderInfo(p.info);
            if ((flags & 128) != 0 && p.metaData != null) {
                pi.metaData = p.metaData;
            }

            if ((flags & 2048) == 0) {
                pi.uriPermissionPatterns = null;
            }

            pi.enabled = isEnabledLPr(p.info, 0, userId);
            pi.applicationInfo = generateApplicationInfo(p.owner, flags, state, userId, isExt);
            return pi;
        } else {
            return null;
        }
    }

    public static InstrumentationInfo generateInstrumentationInfo(VPackage.InstrumentationComponent i, int flags) {
        if (i == null) {
            return null;
        } else if ((flags & 128) == 0) {
            return i.info;
        } else {
            InstrumentationInfo ii = new InstrumentationInfo(i.info);
            ii.metaData = i.metaData;
            return ii;
        }
    }

    public static PermissionInfo generatePermissionInfo(VPackage.PermissionComponent p, int flags) {
        if (p == null) {
            return null;
        } else if ((flags & 128) == 0) {
            return p.info;
        } else {
            PermissionInfo pi = new PermissionInfo(p.info);
            pi.metaData = p.metaData;
            return pi;
        }
    }

    public static PermissionGroupInfo generatePermissionGroupInfo(VPackage.PermissionGroupComponent pg, int flags) {
        if (pg == null) {
            return null;
        } else if ((flags & 128) == 0) {
            return pg.info;
        } else {
            PermissionGroupInfo pgi = new PermissionGroupInfo(pg.info);
            pgi.metaData = pg.metaData;
            return pgi;
        }
    }

    private static boolean checkUseInstalledOrHidden(PackageUserState state, int flags) {
        return state.installed && !state.hidden || (flags & 8192) != 0;
    }

    private static void updatePackageApache(VPackage pkg) {
        if (pkg.usesLibraries == null) {
            pkg.usesLibraries = new ArrayList();
        }

        if (pkg.usesOptionalLibraries == null) {
            pkg.usesOptionalLibraries = new ArrayList();
        }

        ArrayList usesLibraries2;
        ArrayList usesOptionalLibraries2;
        boolean alreadyPresent2;
        if (pkg.applicationInfo != null && pkg.applicationInfo.targetSdkVersion < 28) {
            usesLibraries2 = pkg.usesLibraries;
            usesOptionalLibraries2 = pkg.usesOptionalLibraries;
            alreadyPresent2 = isLibraryPresent(usesLibraries2, usesOptionalLibraries2, ORG_APACHE_HTTP_LEGACY);
            if (!alreadyPresent2) {
                pkg.usesLibraries.add(0, ORG_APACHE_HTTP_LEGACY);
            }
        }

        if (pkg.applicationInfo != null) {
            usesLibraries2 = pkg.usesLibraries;
            usesOptionalLibraries2 = pkg.usesOptionalLibraries;
            alreadyPresent2 = isLibraryPresent(usesLibraries2, usesOptionalLibraries2, "android.test.base");
            if (!alreadyPresent2) {
                pkg.usesLibraries.add(0, "android.test.base");
            }
        }

    }

    private static boolean isLibraryPresent(List<String> usesLibraries, List<String> usesOptionalLibraries, String apacheHttpLegacy) {
        Iterator var3;
        String libName2;
        if (usesLibraries != null) {
            var3 = usesLibraries.iterator();

            while(var3.hasNext()) {
                libName2 = (String)var3.next();
                if (libName2.equals(apacheHttpLegacy)) {
                    return true;
                }
            }
        }

        if (usesOptionalLibraries != null) {
            var3 = usesOptionalLibraries.iterator();

            do {
                if (!var3.hasNext()) {
                    return false;
                }

                libName2 = (String)var3.next();
            } while(!libName2.equals(apacheHttpLegacy));

            return true;
        } else {
            return false;
        }
    }

    private static void changeApplicationInfoPathToReal(ApplicationInfo applicationInfo) {
        String vaPath = "/" + VirtualCore.get().getContext().getPackageName() + "/virtual";
        Field[] var3 = applicationInfo.getClass().getDeclaredFields();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];

            try {
                field.setAccessible(true);
                Object value = field.get(applicationInfo);
                if (value != null && value instanceof String) {
                    String content = (String)value;
                    if (content.contains(vaPath)) {
                        String newContent = content.substring(content.indexOf(vaPath) + vaPath.length());
                        field.set(applicationInfo, newContent);
                    }
                }
            } catch (Throwable var10) {
                var10.printStackTrace();
            }
        }

    }

    private static boolean isVAppCalling(Context context) {
        String mainProcessName = context.getApplicationInfo().processName;
        int pid = Binder.getCallingPid();
        String callingProcessName = null;
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        Iterator<ActivityManager.RunningAppProcessInfo> it = am.getRunningAppProcesses().iterator();

        while(it.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)it.next();
            if (info.pid == pid) {
                callingProcessName = info.processName;
                break;
            }
        }

        return !callingProcessName.equals(mainProcessName) && !callingProcessName.endsWith(Constants.SERVER_PROCESS_NAME) && !callingProcessName.endsWith(Constants.HELPER_PROCESS_NAME) && VActivityManager.get().isAppProcess(callingProcessName);
    }
}
