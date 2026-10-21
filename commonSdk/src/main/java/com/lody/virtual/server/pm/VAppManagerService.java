//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.DexOptimizer;
import com.lody.virtual.helper.PackageCleaner;
import com.lody.virtual.helper.collection.IntArray;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.NativeLibraryHelperCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.sandxposed.XposedModuleProfile;
import com.lody.virtual.server.accounts.VAccountManagerService;
import com.lody.virtual.server.am.UidSystem;
import com.lody.virtual.server.am.VActivityManagerService;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IAppManager;
import com.lody.virtual.server.interfaces.IPackageObserver;
import com.lody.virtual.server.notification.VNotificationManagerService;
import com.lody.virtual.server.pm.parser.PackageParserEx;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mirror.android.content.pm.ApplicationInfoL;
import mirror.android.content.pm.ApplicationInfoP;

public class VAppManagerService extends IAppManager.Stub {
    private final String ANDROID_TEST_BASE = "android.test.base";
    private final String ANDROID_TEST_RUNNER = "android.test.runner";
    private final String ORG_APACHE_HTTP_LEGACY = "org.apache.http.legacy";
    private static final String TAG = "HV-" + VAppManagerService.class.getSimpleName();
    private static final Singleton<VAppManagerService> sService = new Singleton<VAppManagerService>() {
        protected VAppManagerService create() {
            return new VAppManagerService();
        }
    };
    private final UidSystem mUidSystem = new UidSystem();
    private final SystemConfig mSystemConfig = new SystemConfig();
    private final PackagePersistenceLayer mPersistenceLayer = new PackagePersistenceLayer(this);
    private volatile boolean mScanning;
    private RemoteCallbackList<IPackageObserver> mRemoteCallbackList = new RemoteCallbackList();
    private BroadcastReceiver appEventReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!VAppManagerService.this.mScanning) {
                BroadcastReceiver.PendingResult result = this.goAsync();
                String action = intent.getAction();
                if (action != null) {
                    Uri data = intent.getData();
                    if (data != null) {
                        String pkg = data.getSchemeSpecificPart();
                        if (pkg != null) {
                            if (pkg.equals(StubManifest.EXT_PACKAGE_NAME)) {
                                VExtPackageAccessor.syncPackages();
                            }

                            PackageSetting ps = PackageCacheManager.getSetting(pkg);
                            if (ps != null && ps.dynamic) {
                                VActivityManagerService.get().killAppByPkg(pkg, -1);
                                if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
                                    ApplicationInfo outInfo = null;

                                    try {
                                        outInfo = VirtualCore.getPM().getApplicationInfo(pkg, 0);
                                    } catch (PackageManager.NameNotFoundException var11) {
                                        var11.printStackTrace();
                                    }

                                    if (outInfo == null) {
                                        return;
                                    }

                                    VAppInstallerParams params = new VAppInstallerParams(2, 1);
                                    VAppInstallerResult res = VAppManagerService.this.installPackageInternal(Uri.parse("package:" + pkg), params);
                                    VLog.e(VAppManagerService.TAG, "Update package %s status: %d", new Object[]{res.packageName, res.status});
                                } else if (action.equals("android.intent.action.PACKAGE_REMOVED") && intent.getBooleanExtra("android.intent.extra.DATA_REMOVED", false)) {
                                    VLog.e(VAppManagerService.TAG, "Removing package %s", new Object[]{ps.packageName});
                                    VAppManagerService.this.uninstallPackageFully(ps, true);
                                }

                                result.finish();
                            }
                        }
                    }
                }
            }
        }
    };

    public VAppManagerService() {
    }

    public static VAppManagerService get() {
        return (VAppManagerService)sService.get();
    }

    public static void systemReady() {
        VEnvironment.systemReady();
        if (BuildCompat.isPie() && !BuildCompat.isQ()) {
            get().extractApacheFrameworksForPie();
        }

        get().startup();
    }

    private void startup() {
        this.mSystemConfig.load();
        this.mUidSystem.initUidList();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        VirtualCore.get().getContext().registerReceiver(this.appEventReceiver, filter);
    }

    private void extractApacheFrameworksForPie() {
        String frameworkName = "org.apache.http.legacy.boot";
        File dex = VEnvironment.getOptimizedFrameworkFile(frameworkName);
        if (!dex.exists()) {
            try {
                FileUtils.copyFileFromAssets(VirtualCore.get().getContext(), frameworkName, dex);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }

    public void scanApps() {
        if (!this.mScanning) {
            synchronized(this) {
                this.mScanning = true;
                this.mPersistenceLayer.read();
                if (this.mPersistenceLayer.changed) {
                    this.mPersistenceLayer.changed = false;
                    this.mPersistenceLayer.save();
                    VLog.w(TAG, "Package PersistenceLayer updated.", new Object[0]);
                }

                List<VUserInfo> userHandles = VUserManagerService.get().getUsers(true);
                Iterator var3 = SpecialComponentList.getPreInstallPackages().iterator();

                label51:
                while(var3.hasNext()) {
                    String preInstallPkg = (String)var3.next();

                    try {
                        VirtualCore.get().getHostPackageManager().getApplicationInfo(preInstallPkg, 0L);
                    } catch (PackageManager.NameNotFoundException var9) {
                        continue;
                    }

                    Iterator var5 = userHandles.iterator();

                    while(true) {
                        while(true) {
                            if (!var5.hasNext()) {
                                continue label51;
                            }

                            VUserInfo userInfo = (VUserInfo)var5.next();
                            if (!this.isAppInstalled(preInstallPkg) && userInfo.id == 0) {
                                VAppInstallerParams params = new VAppInstallerParams(10, 1);
                                this.installPackageInternal(Uri.parse("package:" + preInstallPkg), params);
                            } else if (!this.isAppInstalledAsUser(userInfo.id, preInstallPkg)) {
                                this.installPackageAsUser(userInfo.id, preInstallPkg);
                            }
                        }
                    }
                }

                VAccountManagerService.get().refreshAuthenticatorCache((String)null);
                this.mScanning = false;
            }
        }
    }

    private void cleanUpResidualFiles(PackageSetting ps) {
        VLog.e(TAG, "cleanup residual files for : %s", new Object[]{ps.packageName});
        this.uninstallPackageFully(ps, false);
    }

    public void onUserCreated(VUserInfo userInfo) {
        FileUtils.ensureDirCreate(VEnvironment.getDataUserDirectory(userInfo.id));
    }

    synchronized boolean loadPackage(PackageSetting setting) {
        if (!this.loadPackageInnerLocked(setting)) {
            this.cleanUpResidualFiles(setting);
            return false;
        } else {
            return true;
        }
    }

    private boolean loadPackageInnerLocked(PackageSetting ps) {
        boolean dynamic = ps.dynamic;
        if (dynamic && !VirtualCore.get().isOutsideInstalled(ps.packageName)) {
            return false;
        } else {
            File cacheFile = VEnvironment.getPackageCacheFile(ps.packageName);
            VPackage pkg = null;

            try {
                pkg = PackageParserEx.readPackageCache(ps.packageName);
            } catch (Throwable var9) {
                var9.printStackTrace();
            }

            if (pkg != null && pkg.packageName != null) {
                VEnvironment.chmodPackageDictionary(cacheFile);
                PackageCacheManager.put(pkg, ps);
                if (dynamic) {
                    try {
                        PackageInfo outInfo = VirtualCore.get().getHostPackageManager().getPackageInfo(ps.packageName, 0L);
                        boolean isVersionCodeChange = pkg.mVersionCode != outInfo.versionCode;
                        boolean isPathChange = !(new File(pkg.applicationInfo.publicSourceDir)).exists();
                        if (isVersionCodeChange || isPathChange) {
                            VLog.d(TAG, "app (" + ps.packageName + ") has changed version, update it.", new Object[0]);
                            VAppInstallerParams params = new VAppInstallerParams(10, 1);
                            this.installPackageInternal(Uri.parse("package:" + ps.packageName), params);
                        }
                    } catch (PackageManager.NameNotFoundException var10) {
                        var10.printStackTrace();
                        return false;
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public int getUidForSharedUser(String sharedUserName) {
        return sharedUserName == null ? -1 : this.mUidSystem.getUid(sharedUserName);
    }

    public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) {
        synchronized(this) {
            VAppInstallerResult var10000;
            try {
                var10000 = this.installPackageInternal(uri, params);
            } catch (Throwable var6) {
                var6.printStackTrace();
                throw new RuntimeException(var6);
            }

            return var10000;
        }
    }

    private VAppInstallerResult installPackageInternal(Uri uri, VAppInstallerParams params) {
        long installTime = System.currentTimeMillis();
        int installFlags = params.getInstallFlags();
        int resultFlags = 0;
        if (uri != null && uri.getScheme() != null) {
            String scheme = uri.getScheme();
            if (!scheme.equals("package") && !scheme.equals("file")) {
                return VAppInstallerResult.create(4);
            } else if (scheme.equals("package") && uri.getSchemeSpecificPart() == null) {
                return VAppInstallerResult.create(4);
            } else if (scheme.equals("file") && uri.getPath() == null) {
                return VAppInstallerResult.create(4);
            } else {
                ApplicationInfo outApplicationInfo = null;
                File packageFile;
                if (uri.getScheme().equals("package")) {
                    String packageName = uri.getSchemeSpecificPart();

                    try {
                        outApplicationInfo = HostPackageManager.get().getApplicationInfo(packageName, 1024L);
                    } catch (PackageManager.NameNotFoundException var39) {
                        var39.printStackTrace();
                    }

                    if (outApplicationInfo == null) {
                        return VAppInstallerResult.create(packageName, 7);
                    }

                    packageFile = new File(outApplicationInfo.publicSourceDir);
                } else {
                    packageFile = new File(uri.getPath());
                }

                VLog.d("HV-", "packageFile:" + packageFile, new Object[0]);
                if (packageFile.exists() && packageFile.isFile()) {
                    PackageParser.ApkLite apkLite;
                    try {
                        apkLite = PackageParser.parseApkLite(packageFile, 0);
                    } catch (PackageParser.PackageParserException var38) {
                        return VAppInstallerResult.create(4);
                    }

                    if (apkLite.splitName != null) {
                        return this.installSplitPackageInternal(packageFile, apkLite, params);
                    } else {
                        VPackage pkg = null;

                        try {
                            pkg = PackageParserEx.parsePackage(packageFile);
                        } catch (Throwable var37) {
                            var37.printStackTrace();
                        }

                        if (pkg != null && pkg.packageName != null) {
                            VPackage existOne = PackageCacheManager.get(pkg.packageName);
                            if (existOne != null) {
                                if ((installFlags & 4) != 0) {
                                    return VAppInstallerResult.create(pkg.packageName, 3);
                                }

                                if ((installFlags & 2) == 0 && existOne.mVersionCode >= pkg.mVersionCode) {
                                    return VAppInstallerResult.create(pkg.packageName, 5);
                                }

                                resultFlags |= 2;
                                if ((installFlags & 8) == 0) {
                                    VActivityManagerService.get().killAppByPkg(pkg.packageName, -1);
                                }
                            }

                            VAppInstallerResult res = new VAppInstallerResult();
                            res.packageName = pkg.packageName;
                            res.flags = resultFlags;
                            File appDir = VEnvironment.getDataAppPackageDirectory(pkg.packageName);
                            VLog.e(TAG, "create app dir: " + appDir);
                            if (!FileUtils.ensureDirCreate(appDir)) {
                                VLog.e(TAG, "failed to create app dir: " + appDir);
                                res.flags = 6;
                                return res;
                            } else {
                                String cpuAbiOverride = params.getCpuAbiOverride();
                                ArrayList<Object> sharedLibraryInfoList = new ArrayList();
                                String primaryCpuAbi;
                                String secondaryCpuAbi;
                                File nativeLibraryRootDir;
                                boolean nativeLibraryRootRequiresIsa;
                                File nativeLibraryDir;
                                File secondaryNativeLibraryDir;
                                String scanSourcePath;
                                String instructionSet;
                                if (cpuAbiOverride != null) {
                                    primaryCpuAbi = cpuAbiOverride;
                                    secondaryCpuAbi = null;
                                    nativeLibraryRootRequiresIsa = true;
                                    nativeLibraryRootDir = VEnvironment.getDataAppLibDirectory(pkg.packageName);
                                    nativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(cpuAbiOverride));
                                    secondaryNativeLibraryDir = null;
                                } else {
                                    String supported64bitAbi;
                                    if (outApplicationInfo != null) {
                                        primaryCpuAbi = (String)ApplicationInfoL.primaryCpuAbi.get(outApplicationInfo);
                                        secondaryCpuAbi = (String)ApplicationInfoL.secondaryCpuAbi.get(outApplicationInfo);
                                        scanSourcePath = outApplicationInfo.nativeLibraryDir;
                                        if (scanSourcePath != null) {
                                            nativeLibraryDir = new File(scanSourcePath);
                                        } else {
                                            nativeLibraryDir = null;
                                        }

                                        String secondaryNativeLibraryPath = (String)ApplicationInfoL.secondaryNativeLibraryDir.get(outApplicationInfo);
                                        if (secondaryNativeLibraryPath != null) {
                                            secondaryNativeLibraryDir = new File(secondaryNativeLibraryPath);
                                        } else {
                                            secondaryNativeLibraryDir = null;
                                        }

                                        supported64bitAbi = (String)ApplicationInfoL.nativeLibraryRootDir.get(outApplicationInfo);
                                        nativeLibraryRootDir = new File(supported64bitAbi);
                                        nativeLibraryRootRequiresIsa = ApplicationInfoL.nativeLibraryRootRequiresIsa.get(outApplicationInfo);
                                    } else {
                                        boolean isUse32bitAbi = false;
                                        if (VERSION.SDK_INT >= 24) {
                                            try {
                                                isUse32bitAbi = apkLite.use32bitAbi;
                                            } catch (Throwable var36) {
                                                var36.printStackTrace();
                                            }
                                        }

                                        Set<String> abis = NativeLibraryHelperCompat.getPackageAbiList(packageFile.getPath());
                                        supported64bitAbi = NativeLibraryHelperCompat.findSupportedAbi(Build.SUPPORTED_64_BIT_ABIS, abis);
                                        instructionSet = NativeLibraryHelperCompat.findSupportedAbi(Build.SUPPORTED_32_BIT_ABIS, abis);
                                        String defaultAbi = Build.SUPPORTED_ABIS[0];
                                        if (!VirtualCore.get().isExtPackageInstalled()) {
                                            isUse32bitAbi = false;
                                            defaultAbi = isUse32bitAbi ? "armeabi-v7a" : "arm64-v8a";
                                        }

                                        if (instructionSet != null && (isUse32bitAbi || supported64bitAbi == null)) {
                                            primaryCpuAbi = instructionSet;
                                            secondaryCpuAbi = supported64bitAbi;
                                        } else if (supported64bitAbi == null || isUse32bitAbi && instructionSet != null) {
                                            primaryCpuAbi = defaultAbi;
                                            secondaryCpuAbi = null;
                                        } else {
                                            primaryCpuAbi = supported64bitAbi;
                                            secondaryCpuAbi = instructionSet;
                                        }

                                        Set<String> sharedLibraryFiles = new HashSet();
                                        if (pkg.usesLibraries == null) {
                                            pkg.usesLibraries = new ArrayList();
                                        }

                                        if (pkg.usesOptionalLibraries == null) {
                                            pkg.usesOptionalLibraries = new ArrayList();
                                        }

                                        if (pkg.applicationInfo.targetSdkVersion < 23 && !pkg.usesLibraries.contains(this.ORG_APACHE_HTTP_LEGACY) && !pkg.usesOptionalLibraries.contains(this.ORG_APACHE_HTTP_LEGACY)) {
                                            pkg.usesLibraries.add(this.ORG_APACHE_HTTP_LEGACY);
                                        }

                                        boolean needCheckTestBaseLib = pkg.usesLibraries.contains(this.ANDROID_TEST_RUNNER) || pkg.usesOptionalLibraries.contains(this.ANDROID_TEST_RUNNER);
                                        if ((needCheckTestBaseLib || BuildCompat.isR() && pkg.applicationInfo.targetSdkVersion < 30) && !pkg.usesLibraries.contains(this.ANDROID_TEST_BASE) && !pkg.usesOptionalLibraries.contains(this.ANDROID_TEST_BASE)) {
                                            pkg.usesLibraries.add(this.ANDROID_TEST_BASE);
                                        }

                                        Iterator var30 = pkg.usesOptionalLibraries.iterator();

                                        String name;
                                        SystemConfig.SharedLibraryEntry entry;
                                        SharedLibraryInfo sharedLibraryInfo;
                                        while(var30.hasNext()) {
                                            name = (String)var30.next();
                                            entry = this.mSystemConfig.getSharedLibrary(name);
                                            if (entry == null) {
                                                VLog.e(TAG, "skip optional shared library: " + name);
                                            } else {
                                                sharedLibraryFiles.add(entry.path);
                                                if (BuildCompat.isS()) {
//                                                    sharedLibraryInfo = new SharedLibraryInfo(entry.path, (String)null, (List)null, entry.name, -1L, 0, new VersionedPackage("android", 0L), (List)null, (List)null, false);
//                                                    sharedLibraryInfoList.add(sharedLibraryInfo);

                                                    try {
                                                        // 构造函数参数类型
                                                        Constructor<SharedLibraryInfo> constructor = SharedLibraryInfo.class.getDeclaredConstructor(
                                                                String.class,                     // path
                                                                String.class,                     // packageName
                                                                List.class,                       // codePaths
                                                                String.class,                     // name
                                                                long.class,                       // version
                                                                int.class,                        // type
                                                                VersionedPackage.class,           // declaringPackage
                                                                List.class,                       // dependentPackages
                                                                List.class,                       // dependencies
                                                                boolean.class                     // isNative
                                                        );
                                                        // 绕过限制
                                                        constructor.setAccessible(true);

                                                        SharedLibraryInfo info = constructor.newInstance(
                                                                entry.path,
                                                                null,                                // packageName
                                                                null,                                // codePaths
                                                                entry.name,
                                                                -1L,                                 // version
                                                                0,                                   // type
                                                                new VersionedPackage("android", 0L),
                                                                null,                                // dependentPackages
                                                                null,                                // dependencies
                                                                false                                // isNative
                                                        );

                                                        sharedLibraryInfoList.add(info);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }

                                        var30 = pkg.usesLibraries.iterator();

                                        while(var30.hasNext()) {
                                            name = (String)var30.next();
                                            entry = this.mSystemConfig.getSharedLibrary(name);
                                            if (entry == null) {
                                                VLog.e(TAG, "skip required shared library: " + name);
                                            } else {
                                                sharedLibraryFiles.add(entry.path);
                                                if (BuildCompat.isS()) {
//                                                    sharedLibraryInfo = new SharedLibraryInfo(entry.path, (String)null, (List)null, entry.name, -1L, 0, new VersionedPackage("android", 0L), (List)null, (List)null, false);
//                                                    sharedLibraryInfoList.add(sharedLibraryInfo);

                                                    try {
                                                        Constructor<SharedLibraryInfo> constructor =
                                                                SharedLibraryInfo.class.getDeclaredConstructor(
                                                                        String.class,
                                                                        String.class,
                                                                        List.class,
                                                                        String.class,
                                                                        long.class,
                                                                        int.class,
                                                                        VersionedPackage.class,
                                                                        List.class,
                                                                        List.class,
                                                                        boolean.class
                                                                );
                                                        constructor.setAccessible(true);

                                                        sharedLibraryInfo = constructor.newInstance(
                                                                entry.path,
                                                                null,
                                                                null,
                                                                entry.name,
                                                                -1L,
                                                                0,
                                                                new VersionedPackage(
                                                                        "android",
                                                                        0L
                                                                ),
                                                                null,
                                                                null,
                                                                false
                                                        );

                                                        sharedLibraryInfoList.add(sharedLibraryInfo);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }

                                        if (!sharedLibraryFiles.isEmpty()) {
                                            pkg.applicationInfo.sharedLibraryFiles = (String[])sharedLibraryFiles.toArray(new String[0]);
                                        }

                                        nativeLibraryRootRequiresIsa = true;
                                        nativeLibraryRootDir = VEnvironment.getDataAppLibDirectory(pkg.packageName);
                                        nativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(primaryCpuAbi));
                                        if (secondaryCpuAbi != null) {
                                            secondaryNativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(secondaryCpuAbi));
                                        } else {
                                            secondaryNativeLibraryDir = null;
                                        }
                                    }
                                }

                                ApplicationInfoL.primaryCpuAbi.set(pkg.applicationInfo, primaryCpuAbi);
                                ApplicationInfoL.secondaryCpuAbi.set(pkg.applicationInfo, secondaryCpuAbi);
                                if (nativeLibraryRootDir != null) {
                                    ApplicationInfoL.nativeLibraryRootDir.set(pkg.applicationInfo, nativeLibraryRootDir.getAbsolutePath());
                                }

                                ApplicationInfoL.nativeLibraryRootRequiresIsa.set(pkg.applicationInfo, nativeLibraryRootRequiresIsa);
                                if (nativeLibraryDir != null) {
                                    pkg.applicationInfo.nativeLibraryDir = nativeLibraryDir.getAbsolutePath();
                                }

                                if (secondaryNativeLibraryDir != null) {
                                    ApplicationInfoL.secondaryNativeLibraryDir.set(pkg.applicationInfo, secondaryNativeLibraryDir.getAbsolutePath());
                                }

                                if (outApplicationInfo != null) {
                                    pkg.applicationInfo.publicSourceDir = outApplicationInfo.publicSourceDir;
                                    pkg.applicationInfo.sourceDir = outApplicationInfo.sourceDir;
                                    pkg.applicationInfo.sharedLibraryFiles = outApplicationInfo.sharedLibraryFiles;
                                    if (VERSION.SDK_INT >= 26) {
                                        pkg.applicationInfo.splitNames = outApplicationInfo.splitNames;
                                    }

                                    pkg.applicationInfo.splitSourceDirs = outApplicationInfo.splitSourceDirs;
                                    pkg.applicationInfo.splitPublicSourceDirs = outApplicationInfo.splitPublicSourceDirs;
                                    if (ApplicationInfoP.sharedLibraryInfos != null) {
                                        List sharedLibraryInfos = ApplicationInfoP.sharedLibraryInfos(outApplicationInfo);
                                        ApplicationInfoP.sharedLibraryInfos(pkg.applicationInfo, sharedLibraryInfos);
                                    }
                                } else {
                                    SettingConfig config = VirtualCore.getConfig();
                                    if (config.isEnableIORedirect() && config.isUseRealApkPath(pkg.packageName)) {
                                        scanSourcePath = VEnvironment.getPackageFileStub(pkg.packageName);
                                    } else {
                                        scanSourcePath = VEnvironment.getPackageFile(pkg.packageName).getPath();
                                    }

                                    pkg.applicationInfo.publicSourceDir = scanSourcePath;
                                    pkg.applicationInfo.sourceDir = scanSourcePath;
                                    if (ApplicationInfoP.sharedLibraryInfos != null) {
                                        ApplicationInfoP.sharedLibraryInfos(pkg.applicationInfo, sharedLibraryInfoList);
                                    }
                                }

                                scanSourcePath = VEnvironment.getDataAppPackageDirectory(pkg.packageName).getAbsolutePath();
                                ApplicationInfoL.scanSourceDir.set(pkg.applicationInfo, scanSourcePath);
                                ApplicationInfoL.scanPublicSourceDir.set(pkg.applicationInfo, scanSourcePath);
                                NativeLibraryHelperCompat nativeLibraryHelper = new NativeLibraryHelperCompat(packageFile);
                                VLog.d("HV-", "nativeLibraryRootDir:" + nativeLibraryRootDir, new Object[0]);
                                if (outApplicationInfo == null) {
                                    if (!FileUtils.ensureDirCreate(nativeLibraryRootDir)) {
                                        VLog.e(TAG, "failed to create native lib root dir: " + nativeLibraryRootDir);
                                    }

                                    if (FileUtils.ensureDirCreate(nativeLibraryDir)) {
                                        nativeLibraryHelper.copyNativeBinaries(nativeLibraryDir, primaryCpuAbi);
                                    } else {
                                        VLog.e(TAG, "failed to create native lib dir: " + nativeLibraryDir);
                                    }

                                    if (secondaryCpuAbi != null) {
                                        if (FileUtils.ensureDirCreate(secondaryNativeLibraryDir)) {
                                            nativeLibraryHelper.copyNativeBinaries(secondaryNativeLibraryDir, secondaryCpuAbi);
                                        } else {
                                            VLog.e(TAG, "failed to create secondary native lib dir: " + secondaryNativeLibraryDir);
                                        }
                                    }
                                }

                                PackageSetting ps = existOne != null ? (PackageSetting)existOne.mExtras : new PackageSetting();
                                ps.primaryCpuAbi = primaryCpuAbi;
                                ps.secondaryCpuAbi = secondaryCpuAbi;
                                ps.is64bitPackage = NativeLibraryHelperCompat.is64bitAbi(ps.primaryCpuAbi);
                                VLog.e(TAG, "outApplicationInfo 603 : " + outApplicationInfo);
                                if (outApplicationInfo == null) {
                                    File privatePackageFile = VEnvironment.getPackageFile(pkg.packageName);
                                    VLog.e(TAG, "packageFile: " + packageFile + "   privatePackageFile:" + privatePackageFile);
                                    boolean copied = false;

                                    try {
                                        FileUtils.copyFile(packageFile, privatePackageFile);
                                        copied = true;
                                    } catch (IOException var35) {
                                        VLog.e(TAG, "failed to copy file: " + privatePackageFile);
                                        var35.printStackTrace();
                                    }

                                    if (!copied) {
                                        FileUtils.deleteDir(VEnvironment.getDataAppPackageDirectory(pkg.packageName));
                                        res.status = 6;
                                        return res;
                                    }

                                    packageFile = privatePackageFile;
                                    VEnvironment.chmodPackageDictionary(privatePackageFile);
                                }

                                ps.dynamic = outApplicationInfo != null;
                                ps.packageName = pkg.packageName;
                                ps.libPath = nativeLibraryDir.getPath();
                                ps.appId = VUserHandle.getAppId(this.mUidSystem.getOrCreateUid(pkg));
                                if ((resultFlags & 2) != 0) {
                                    ps.lastUpdateTime = installTime;
                                } else {
                                    ps.firstInstallTime = installTime;
                                    ps.lastUpdateTime = installTime;
                                    int[] var48 = VUserManagerService.get().getUserIds();
                                    int var51 = var48.length;

                                    for(int var50 = 0; var50 < var51; ++var50) {
                                        int userId = var48[var50];
                                        boolean installed = userId == 0;
                                        ps.setUserState(userId, false, false, installed);
                                    }
                                }

                                PackageParserEx.savePackageCache(pkg);
                                PackageCacheManager.put(pkg, ps);
                                if (!this.mScanning) {
                                    this.mPersistenceLayer.save();
                                }

                                if (outApplicationInfo == null && !ps.isRunInExtProcess()) {
                                    instructionSet = VirtualRuntime.getCurrentInstructionSet();
                                    File oatFile = VEnvironment.getOatFile(ps.packageName, instructionSet);
                                    if (!oatFile.exists() || (params.getInstallFlags() & 16) == 0) {
                                        try {
                                            DexOptimizer.dex2oat(packageFile.getPath(), oatFile.getPath());
                                        } catch (Throwable var34) {
                                            var34.printStackTrace();
                                        }
                                    }
                                }

                                VAccountManagerService.get().refreshAuthenticatorCache((String)null);
                                if (!this.mScanning) {
                                    VExtPackageAccessor.syncPackages();
                                }

                                if ((installFlags & 1) != 0) {
                                    this.notifyAppInstalled(ps, -1);
                                }

                                res.status = 0;
                                return res;
                            }
                        } else {
                            return VAppInstallerResult.create(7);
                        }
                    }
                } else {
                    return VAppInstallerResult.create(4);
                }
            }
        } else {
            return VAppInstallerResult.create(4);
        }
    }

    private VAppInstallerResult installSplitPackageInternal(File splitPackageFile, PackageParser.ApkLite apkLite, VAppInstallerParams params) {
        int flags = 1;
        VPackage basePackage = PackageCacheManager.get(apkLite.packageName);
        if (basePackage == null) {
            return VAppInstallerResult.create(apkLite.packageName, 8);
        } else {
            PackageSetting ps = (PackageSetting)basePackage.mExtras;
            if (basePackage.splitNames == null) {
                basePackage.splitNames = new ArrayList();
            } else if (basePackage.splitNames.contains(apkLite.splitName)) {
                if ((params.getInstallFlags() & 2) == 0) {
                    return VAppInstallerResult.create(apkLite.packageName, 5);
                }

                basePackage.splitNames.remove(apkLite.splitName);
                flags |= 2;
            }

            if ((params.getInstallFlags() & 8) == 0) {
                VActivityManagerService.get().killAppByPkg(apkLite.packageName, -1);
            }

            basePackage.splitNames.add(apkLite.splitName);
            File privateSplitPackage = VEnvironment.getSplitPackageFile(apkLite.packageName, apkLite.splitName);

            try {
                FileUtils.copyFile(splitPackageFile, privateSplitPackage);
            } catch (IOException var13) {
                var13.printStackTrace();
            }

            String[] abis = (String[])NativeLibraryHelperCompat.getPackageAbiList(splitPackageFile.getPath()).toArray(new String[0]);
            if (abis.length > 0) {
                String primaryCpuAbi = abis[0];
                File nativeLibraryRootDir = VEnvironment.getDataAppLibDirectory(basePackage.packageName);
                File nativeLibraryDir = new File(nativeLibraryRootDir, VirtualRuntime.getInstructionSet(primaryCpuAbi));
                ps.primaryCpuAbi = primaryCpuAbi;
                ps.is64bitPackage = NativeLibraryHelperCompat.is64bitAbi(primaryCpuAbi);
                NativeLibraryHelperCompat nativeLibraryHelper = new NativeLibraryHelperCompat(splitPackageFile);
                if (FileUtils.ensureDirCreate(nativeLibraryDir)) {
                    nativeLibraryHelper.copyNativeBinaries(nativeLibraryDir, primaryCpuAbi);
                } else {
                    VLog.e(TAG, "failed to create native lib dir: " + nativeLibraryDir);
                }
            }

            PackageParserEx.savePackageCache(basePackage);
            if (!this.mScanning) {
                VExtPackageAccessor.syncPackages();
            }

            return new VAppInstallerResult(apkLite.packageName, 0, flags);
        }
    }

    public synchronized boolean installPackageAsUser(int userId, String packageName) {
        if (VUserManagerService.get().exists(userId)) {
            PackageSetting ps = PackageCacheManager.getSetting(packageName);
            if (ps != null) {
                if (!ps.isInstalled(userId)) {
                    ps.setInstalled(userId, true);
                    VExtPackageAccessor.syncPackages();
                    this.notifyAppInstalled(ps, userId);
                    this.mPersistenceLayer.save();
                    return true;
                }

                return true;
            }
        }

        return false;
    }

    public synchronized boolean uninstallPackage(String packageName) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        if (ps != null) {
            this.uninstallPackageFully(ps, true);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean uninstallPackageAsUser(String packageName, int userId) {
        if (!VUserManagerService.get().exists(userId)) {
            return false;
        } else {
            PackageSetting ps = PackageCacheManager.getSetting(packageName);
            if (ps != null) {
                int[] userIds = this.getPackageInstalledUsers(packageName);
                if (!ArrayUtils.contains(userIds, userId)) {
                    return false;
                } else {
                    if (userIds.length <= 1) {
                        this.uninstallPackageFully(ps, true);
                    } else {
                        this.cleanPackageData(packageName, userId);
                        ps.setInstalled(userId, false);
                        this.mPersistenceLayer.save();
                        this.notifyAppUninstalled(ps, userId);
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public boolean cleanPackageData(String packageName, int userId) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        if (ps == null) {
            return false;
        } else {
            VActivityManagerService.get().killAppByPkg(packageName, userId);
            VNotificationManagerService.get().cancelAllNotification(ps.packageName, userId);
            FileUtils.deleteDir(VEnvironment.getDataUserPackageDirectory(userId, packageName));
            FileUtils.deleteDir(VEnvironment.getDeDataUserPackageDirectory(userId, packageName));
            VExtPackageAccessor.cleanPackageData(new int[]{userId}, ps.packageName);
            ComponentStateManager.user(userId).clear(packageName);
            return true;
        }
    }

    private void uninstallPackageFully(PackageSetting ps, boolean notify) {
        String packageName = ps.packageName;
        VActivityManagerService.get().killAppByPkg(packageName, -1);
        PackageCacheManager.remove(packageName);
        FileUtils.deleteDir(VEnvironment.getDataAppPackageDirectory(packageName));
        FileUtils.deleteDir(VEnvironment.getOatDirectory(packageName));
        PackageCleaner.cleanAllUserPackage(VEnvironment.getDataUserDirectory(), packageName);
        Iterator var4 = VUserManager.get().getUsers().iterator();

        while(var4.hasNext()) {
            VUserInfo info = (VUserInfo)var4.next();
            VNotificationManagerService.get().cancelAllNotification(ps.packageName, info.id);
            ComponentStateManager.user(info.id).clear(packageName);
        }

        if (notify) {
            this.notifyAppUninstalled(ps, -1);
        }

        if (!this.mScanning) {
            VExtPackageAccessor.syncPackages();
        }

        VAccountManagerService.get().refreshAuthenticatorCache((String)null);
    }

    public int[] getPackageInstalledUsers(String packageName) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        if (ps != null) {
            IntArray installedUsers = new IntArray(5);
            int[] userIds = VUserManagerService.get().getUserIds();
            int[] var5 = userIds;
            int var6 = userIds.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                int userId = var5[var7];
                if (ps.readUserState(userId).installed) {
                    installedUsers.add(userId);
                }
            }

            return installedUsers.getAll();
        } else {
            return new int[0];
        }
    }

    public List<InstalledAppInfo> getInstalledApps(int flags) {
        List<InstalledAppInfo> infoList = new ArrayList(this.getInstalledAppCount());
        boolean filterXposedModules = (flags & 268435456) != 0;
        boolean excludeXposedModules = (flags & 536870912) != 0;
        boolean enabledXposedModules = (flags & 1073741824) != 0;
        synchronized(PackageCacheManager.PACKAGE_CACHE) {
            Iterator var7 = PackageCacheManager.PACKAGE_CACHE.values().iterator();

            label55:
            while(true) {
                while(true) {
                    if (!var7.hasNext()) {
                        break label55;
                    }

                    VPackage p = (VPackage)var7.next();
                    PackageSetting setting;
                    if (excludeXposedModules) {
                        if (p.xposedModule == null) {
                            setting = (PackageSetting)p.mExtras;
                            infoList.add(setting.getAppInfo());
                        }
                    } else if ((!filterXposedModules || p.xposedModule != null) && (!enabledXposedModules || XposedModuleProfile.isXposedEnable() && XposedModuleProfile.isModuleEnable(p.packageName))) {
                        setting = (PackageSetting)p.mExtras;
                        infoList.add(setting.getAppInfo());
                    }
                }
            }
        }

        VLog.d("HV-", " 查看当前安装的数量 ：" + infoList.size(), new Object[0]);
        return infoList;
    }

    public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) {
        List<InstalledAppInfo> infoList = new ArrayList(this.getInstalledAppCount());
        synchronized(PackageCacheManager.PACKAGE_CACHE) {
            Iterator var5 = PackageCacheManager.PACKAGE_CACHE.values().iterator();

            while(var5.hasNext()) {
                VPackage p = (VPackage)var5.next();
                PackageSetting setting = (PackageSetting)p.mExtras;
                boolean visible = setting.isInstalled(userId);
                if ((flags & 1) == 0 && setting.isHidden(userId)) {
                    visible = false;
                }

                if (visible) {
                    infoList.add(setting.getAppInfo());
                }
            }

            return infoList;
        }
    }

    public List<String> getInstalledSplitNames(String packageName) {
        Class var2 = PackageCacheManager.class;
        synchronized(PackageCacheManager.class) {
            if (packageName != null) {
                VPackage pkg = PackageCacheManager.get(packageName);
                if (pkg != null && pkg.splitNames != null) {
                    return pkg.splitNames;
                }
            }

            return Collections.emptyList();
        }
    }

    public int getInstalledAppCount() {
        return PackageCacheManager.size();
    }

    public boolean isAppInstalled(String packageName) {
        return packageName != null && PackageCacheManager.contain(packageName);
    }

    public boolean isAppInstalledAsUser(int userId, String packageName) {
        if (packageName != null && VUserManagerService.get().exists(userId)) {
            PackageSetting setting = PackageCacheManager.getSetting(packageName);
            return setting == null ? false : setting.isInstalled(userId);
        } else {
            return false;
        }
    }

    private void notifyAppInstalled(PackageSetting setting, int userId) {
        String pkg = setting.packageName;
        int N = this.mRemoteCallbackList.beginBroadcast();

        while(N-- > 0) {
            try {
                if (userId == -1) {
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageInstalled(pkg);
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageInstalledAsUser(0, pkg);
                } else {
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageInstalledAsUser(userId, pkg);
                }
            } catch (RemoteException var6) {
                var6.printStackTrace();
            }
        }

        this.sendInstalledBroadcast(pkg, new VUserHandle(userId));
        this.mRemoteCallbackList.finishBroadcast();
    }

    private void notifyAppUninstalled(PackageSetting setting, int userId) {
        String pkg = setting.packageName;
        int N = this.mRemoteCallbackList.beginBroadcast();

        while(N-- > 0) {
            try {
                if (userId == -1) {
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageUninstalled(pkg);
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageUninstalledAsUser(0, pkg);
                } else {
                    ((IPackageObserver)this.mRemoteCallbackList.getBroadcastItem(N)).onPackageUninstalledAsUser(userId, pkg);
                }
            } catch (RemoteException var6) {
                var6.printStackTrace();
            }
        }

        this.sendUninstalledBroadcast(pkg, new VUserHandle(userId));
        this.mRemoteCallbackList.finishBroadcast();
    }

    private void sendInstalledBroadcast(String packageName, VUserHandle user) {
        Intent intent = new Intent("android.intent.action.PACKAGE_ADDED");
        intent.setData(Uri.parse("package:" + packageName));
        VActivityManagerService.get().sendBroadcastAsUser(intent, user);
    }

    private void sendUninstalledBroadcast(String packageName, VUserHandle user) {
        Intent intent = new Intent("android.intent.action.PACKAGE_REMOVED");
        intent.setData(Uri.parse("package:" + packageName));
        VActivityManagerService.get().sendBroadcastAsUser(intent, user);
    }

    public void registerObserver(IPackageObserver observer) {
        try {
            this.mRemoteCallbackList.register(observer);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void unregisterObserver(IPackageObserver observer) {
        try {
            this.mRemoteCallbackList.unregister(observer);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public InstalledAppInfo getInstalledAppInfo(String packageName, int flags) {
        Class var3 = PackageCacheManager.class;
        synchronized(PackageCacheManager.class) {
            if (packageName != null) {
                PackageSetting setting = PackageCacheManager.getSetting(packageName);
                if (setting != null) {
                    return setting.getAppInfo();
                }
            }

            return null;
        }
    }

    public boolean isRunInExtProcess(String packageName) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        return ps != null && ps.isRunInExtProcess();
    }

    public boolean isPackageLaunched(int userId, String packageName) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        return ps != null && ps.isLaunched(userId);
    }

    public void setPackageHidden(int userId, String packageName, boolean hidden) {
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        if (ps != null && VUserManagerService.get().exists(userId)) {
            ps.setHidden(userId, hidden);
            this.mPersistenceLayer.save();
        }

    }

    void restoreFactoryState() {
        VLog.w(TAG, "Warning: Restore the factory state...", new Object[0]);
        FileUtils.deleteDir(VEnvironment.getRoot());
        VEnvironment.systemReady();
    }

    public void savePersistenceData() {
        this.mPersistenceLayer.save();
    }
}
