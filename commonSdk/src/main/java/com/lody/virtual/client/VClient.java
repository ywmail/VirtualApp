/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.AlarmManager
 *  android.app.Application
 *  android.app.Instrumentation
 *  android.app.Service
 *  android.content.BroadcastReceiver
 *  android.content.BroadcastReceiver$PendingResult
 *  android.content.ComponentName
 *  android.content.ContentProviderClient
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ServiceInfo
 *  android.content.res.Configuration
 *  android.graphics.Canvas
 *  android.os.Binder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ConditionVariable
 *  android.os.Environment
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Process
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.os.StrictMode$ThreadPolicy$Builder
 *  android.util.Log
 *  android.view.autofill.AutofillManager
 *  androidx.annotation.RequiresApi
 */
package com.lody.virtual.client;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Binder;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Process;
import android.os.StrictMode;
import android.security.net.config.ApplicationConfig;
import android.util.Log;
import android.view.autofill.AutofillManager;
import androidx.annotation.RequiresApi;
import com.lody.virtual.PineXposed;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.DexOverride;
import com.lody.virtual.client.IVClient;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.core.CrashHandler;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.LaunchCallBack;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.instruments.InstrumentationVirtualApp;
import com.lody.virtual.client.hook.providers.ProviderHook;
import com.lody.virtual.client.hook.providers.SettingsProviderHook;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.hook.proxies.view.AutoFillManagerStub;
import com.lody.virtual.client.hook.secondary.ProxyServiceFactory;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.ipc.VirtualStorageManager;
import com.lody.virtual.client.service.VServiceRuntime;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.ComposeClassLoader;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.StorageManagerCompat;
import com.lody.virtual.helper.compat.StrictModeCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.oem.EmuiHelper;
import com.lody.virtual.oem.apps.WeChat;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.receiver.StaticReceiverSystem;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.secondary.FakeIdentityBinder;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityThread;
import mirror.android.app.ActivityThreadNMR1;
import mirror.android.app.ActivityThreadQ;
import mirror.android.app.AlarmManager;
import mirror.android.app.ContextImpl;
import mirror.android.app.ContextImplKitkat;
import mirror.android.app.IActivityManager;
import mirror.android.app.LoadedApk;
import mirror.android.app.LoadedApkKitkat;
import mirror.android.app.Service;
import mirror.android.content.BroadcastReceiver;
import mirror.android.content.ContentProviderHolderOreo;
import mirror.android.content.res.CompatibilityInfo;
import mirror.android.os.Message;
import mirror.android.providers.Settings;
import mirror.android.renderscript.RenderScriptCacheDir;
import mirror.android.view.DisplayAdjustments;
import mirror.android.view.HardwareRenderer;
import mirror.android.view.RenderScript;
import mirror.android.view.ThreadedRenderer;
import mirror.com.android.internal.content.ReferrerIntent;
import mirror.dalvik.system.VMRuntime;
import mirror.java.lang.ThreadGroupN;

public final class VClient extends IVClient.Stub {
    private static final int NEW_INTENT = 11;
    private static final int RECEIVER = 12;
    private static final int FINISH_ACTIVITY = 13;
    private static final String TAG = "HV-" + VClient.class.getSimpleName();
    @SuppressLint(value={"StaticFieldLeak"})
    private static final VClient gClient = new VClient();
    private final H mH = new H();
    private Instrumentation mInstrumentation;
    private ClientConfig clientConfig;
    private int corePid;
    private AppBindData mBoundApplication;
    private Application mInitialApplication;
    private CrashHandler crashHandler;
    private InstalledAppInfo mAppInfo;
    private final Map<String, Application> mAllApplications = new HashMap<String, Application>(1);
    private Set<String> mExportedVApiPkgs = new HashSet<String>();
    private static boolean CheckJunitClazz = false;

    public int getCorePid() {
        return this.corePid;
    }

    public void setCorePid(int corePid) {
        this.corePid = corePid;
    }

    private VClient() {
    }

    public synchronized void addExportedVApiPkg(String pkg) {
        this.mExportedVApiPkgs.add(pkg);
    }

    public InstalledAppInfo getAppInfo() {
        return this.mAppInfo;
    }

    public static VClient get() {
        return gClient;
    }

    public boolean isDynamicApp() {
        InstalledAppInfo appInfo = this.getAppInfo();
        return appInfo != null && appInfo.dynamic;
    }

    public VDeviceConfig getDeviceConfig() {
        return VDeviceManager.get().getDeviceConfig(VUserHandle.getUserId(this.getVUid()));
    }

    public Application getCurrentApplication() {
        return this.mInitialApplication;
    }

    public String getCurrentPackage() {
        return this.mBoundApplication != null ? this.mBoundApplication.appInfo.packageName : VPackageManager.get().getNameForUid(this.getVUid());
    }

    public ApplicationInfo getCurrentApplicationInfo() {
        return this.mBoundApplication != null ? this.mBoundApplication.appInfo : null;
    }

    public CrashHandler getCrashHandler() {
        return this.crashHandler;
    }

    public void setCrashHandler(CrashHandler crashHandler) {
        this.crashHandler = crashHandler;
    }

    public int getVUid() {
        if (this.clientConfig == null) {
            return 0;
        }
        return this.clientConfig.vuid;
    }

    public int getVUserHandle() {
        if (this.clientConfig == null) {
            return 0;
        }
        return VUserHandle.getUserId(this.clientConfig.vuid);
    }

    public int getVpid() {
        if (this.clientConfig == null) {
            return 0;
        }
        return this.clientConfig.vpid;
    }

    public int getBaseVUid() {
        if (this.clientConfig == null) {
            return 0;
        }
        return VUserHandle.getAppId(this.clientConfig.vuid);
    }

    public ClassLoader getClassLoader(ApplicationInfo appInfo) {
        Context context = this.createPackageContext(appInfo.packageName);
        return context.getClassLoader();
    }

    private void sendMessage(int what, Object obj) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        msg.obj = obj;
        this.mH.sendMessage(msg);
    }

    @Override
    public IBinder getAppThread() {
        return ActivityThread.getApplicationThread.call(VirtualCore.mainThread(), new Object[0]);
    }

    @Override
    public IBinder getToken() {
        if (this.clientConfig == null) {
            return null;
        }
        return this.clientConfig.token;
    }

    public ClientConfig getClientConfig() {
        return this.clientConfig;
    }

    @Override
    public boolean isAppRunning() {
        return this.mInitialApplication != null;
    }

    public boolean isProcessBound() {
        return this.clientConfig != null;
    }

    public void initProcess(ClientConfig clientConfig) {
        if (this.clientConfig != null) {
            throw new RuntimeException("reject init process " + clientConfig.vpid + " : " + clientConfig.processName + ", this process is : " + this.clientConfig.processName);
        }
        this.clientConfig = clientConfig;
    }

    private void handleNewIntent(NewIntentData data) {
        Intent intent;
        ComponentUtils.unpackFillIn(data.intent, VClient.get().getClassLoader());
        Intent intent2 = intent = Build.VERSION.SDK_INT >= 22 ? ReferrerIntent.ctor.newInstance(data.intent, data.creator) : data.intent;
        if (ActivityThread.performNewIntents != null) {
            ActivityThread.performNewIntents.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent));
        } else if (ActivityThreadNMR1.performNewIntents != null) {
            ActivityThreadNMR1.performNewIntents.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent), true);
        } else if (BuildCompat.isS()) {
            Object obj = ActivityThread.mActivities.get(VirtualCore.mainThread()).get(data.token);
            if (obj != null) {
                ActivityThread.handleNewIntent(obj, Collections.singletonList(intent));
            }
        } else {
            ActivityThreadQ.handleNewIntent.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void bindApplication(final String packageName, final String processName) {
        Map<String, Application> map = this.mAllApplications;
        synchronized (map) {
            if (this.mAllApplications.containsKey(packageName)) {
                return;
            }
        }
        if (this.clientConfig == null) {
            throw new RuntimeException("Unrecorded process: " + processName);
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            final ConditionVariable cond = new ConditionVariable();
            VirtualRuntime.getUIHandler().post(new Runnable(){

                @Override
                public void run() {
                    VClient.this.bindApplicationMainThread(packageName, processName, cond);
                    cond.open();
                }
            });
            cond.block();
        } else {
            this.bindApplicationMainThread(packageName, processName, null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @RequiresApi(api=26)
    private void bindApplicationMainThread(String packageName, String processName, ConditionVariable cond) {
        Application app;
        InstalledAppInfo info;
        boolean isInitialApp;
        block66: {
            Object loadedApk;
            Object boundApp;
            List<ProviderInfo> providers;
            boolean isTargetGame;
            Map<String, Application> map = this.mAllApplications;
            synchronized (map) {
                if (this.mAllApplications.containsKey(packageName)) {
                    return;
                }
            }
            VirtualCore.getConfig();
            Log.e((String)"va== config 0", (String)(SettingConfig.isUseNativeEngine2(packageName) + ""));
            if (VirtualCore.get().isExtHelperProcess()) {
                VExtPackageAccessor.syncPackages();
            }
            isInitialApp = this.mInitialApplication == null;
            Binder.clearCallingIdentity();
            if (processName == null) {
                processName = packageName;
            }
            try {
                this.setupUncaughtHandler();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
            int userId = VUserHandle.getUserId(this.getVUid());
            try {
                this.fixInstalledProviders();
                SettingsProviderHook.passSettingsProviderPermissionCheck(packageName);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
            VDeviceConfig deviceConfig = this.getDeviceConfig();
            VDeviceManager.get().applyBuildProp(deviceConfig);
            ActivityThread.mInitialApplication.set(VirtualCore.mainThread(), null);
            AppBindData data = new AppBindData();
            info = VirtualCore.get().getInstalledAppInfo(packageName, 0);
            if (info == null) {
                new Exception("app not exist").printStackTrace();
                Process.killProcess((int)0);
                System.exit(0);
            }
            if (isInitialApp) {
                this.mAppInfo = info;
            }
            data.appInfo = VPackageManager.get().getApplicationInfo(packageName, 0, userId);
            data.processName = processName;
            data.providers = VPackageManager.get().queryContentProviders(processName, this.getVUid(), 128);
            Iterator<ProviderInfo> iterator = data.providers.iterator();
            while (iterator.hasNext()) {
                ProviderInfo providerInfo = iterator.next();
                if (providerInfo.enabled) continue;
                iterator.remove();
            }
            boolean isExt = VirtualCore.get().isExtPackage();
            VLog.i(TAG, "Binding application %s (%s [%d])", data.appInfo.packageName, data.processName, Process.myPid());
            if (isInitialApp) {
                VirtualCore.getConfig();
                this.mBoundApplication = data;
                VirtualRuntime.setupRuntime(data.processName, data.appInfo);
                this.mInstrumentation = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
                int targetSdkVersion = data.appInfo.targetSdkVersion;
                if (targetSdkVersion < 9) {
                    StrictMode.ThreadPolicy newPolicy = new StrictMode.ThreadPolicy.Builder(StrictMode.getThreadPolicy()).permitNetwork().build();
                    StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)newPolicy);
                }
                if (Build.VERSION.SDK_INT >= 24 && VirtualCore.get().getTargetSdkVersion() >= 24 && targetSdkVersion < 24) {
                    StrictModeCompat.disableDeathOnFileUriExposure();
                }
                if (targetSdkVersion < 21) {
                    Message.updateCheckRecycle.call(targetSdkVersion);
                }
                android.app.AlarmManager alarmManager = (android.app.AlarmManager)VirtualCore.get().getContext().getSystemService("alarm");
                if (AlarmManager.mTargetSdkVersion != null) {
                    try {
                        AlarmManager.mTargetSdkVersion.set(alarmManager, targetSdkVersion);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isExt) {
                    System.setProperty("java.io.tmpdir", new File(VEnvironment.getDataUserPackageDirectoryExt(userId, info.packageName), "cache").getAbsolutePath());
                } else {
                    System.setProperty("java.io.tmpdir", new File(VEnvironment.getDataUserPackageDirectory(userId, info.packageName), "cache").getAbsolutePath());
                }
                VLog.i(TAG + "kooklog", "launchEngine    getContext():" + VirtualCore.get().getContext(), new Object[0]);
                NativeEngine.launchEngine(VirtualCore.get().getContext(), packageName);
                if (VirtualCore.getConfig().isEnableIORedirect()) {
                    this.mountVirtualFS(info, isExt);
                }
            }
            VirtualCore.getConfig();
            Object mainThread = VirtualCore.mainThread();
            this.initDataStorage(isExt, userId, packageName);
            Context context = this.createPackageContext(data.appInfo.packageName);
            if (isInitialApp) {
                VLog.i(TAG, "startDexOverride", new Object[0]);
                NativeEngine.startDexOverride();
                StaticReceiverSystem.get().attach(packageName, VirtualCore.get().getContext(), data.appInfo, userId);
                File codeCacheDir = Build.VERSION.SDK_INT >= 23 ? context.getCodeCacheDir() : context.getCacheDir();
                if (Build.VERSION.SDK_INT < 24) {
                    if (HardwareRenderer.setupDiskCache != null) {
                        HardwareRenderer.setupDiskCache.call(codeCacheDir);
                    }
                } else if (ThreadedRenderer.setupDiskCache != null) {
                    ThreadedRenderer.setupDiskCache.call(codeCacheDir);
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (RenderScriptCacheDir.setupDiskCache != null) {
                        RenderScriptCacheDir.setupDiskCache.call(codeCacheDir);
                    }
                } else if (RenderScript.setupDiskCache != null) {
                    RenderScript.setupDiskCache.call(codeCacheDir);
                }
                this.mBoundApplication.info = ContextImpl.mPackageInfo.get(context);
                Object boundApp2 = ActivityThread.mBoundApplication.get(mainThread);
                ActivityThread.AppBindData.appInfo.set(boundApp2, data.appInfo);
                ActivityThread.AppBindData.processName.set(boundApp2, data.processName);
                ActivityThread.AppBindData.instrumentationName.set(boundApp2, new ComponentName(data.appInfo.packageName, Instrumentation.class.getName()));
                ActivityThread.AppBindData.info.set(boundApp2, data.info);
                ActivityThread.AppBindData.providers.set(boundApp2, data.providers);
                if (LoadedApk.mSecurityViolation != null) {
                    LoadedApk.mSecurityViolation.set(this.mBoundApplication.info, false);
                }
                VMRuntime.setTargetSdkVersion.call(VMRuntime.getRuntime.call(new Object[0]), data.appInfo.targetSdkVersion);
                Configuration configuration = context.getResources().getConfiguration();
                Object compatInfo = null;
                if (CompatibilityInfo.ctor != null) {
                    compatInfo = CompatibilityInfo.ctor.newInstance(data.appInfo, configuration.screenLayout, configuration.smallestScreenWidthDp, false);
                }
                if (CompatibilityInfo.ctorLG != null) {
                    compatInfo = CompatibilityInfo.ctorLG.newInstance(data.appInfo, configuration.screenLayout, configuration.smallestScreenWidthDp, false, 0);
                }
                if (compatInfo != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        DisplayAdjustments.setCompatibilityInfo.call(ContextImplKitkat.mDisplayAdjustments.get(context), compatInfo);
                    }
                    DisplayAdjustments.setCompatibilityInfo.call(LoadedApkKitkat.mDisplayAdjustments.get(this.mBoundApplication.info), compatInfo);
                }
                if (Build.VERSION.SDK_INT >= 30) {
                    ApplicationConfig.setDefaultInstance(null);
                }
                VLog.i(TAG, "初始化hook框架", new Object[0]);
                PineXposed.initForXposed(context, processName);
                this.fixSystem();
                VirtualCore.get().getAppCallback().beforeStartApplication(packageName, processName, context);
                if (this.mExportedVApiPkgs.contains(packageName) && LoadedApk.mClassLoader != null) {
                    LoadedApk.mClassLoader.set(data.info, new ComposeClassLoader(VClient.class.getClassLoader(), LoadedApk.getClassLoader.call(data.info, new Object[0])));
                }
            }
            if (CheckJunitClazz && BuildCompat.isR() && data.appInfo.targetSdkVersion < 30) {
                ClassLoader cl = LoadedApk.getClassLoader.call(data.info, new Object[0]);
                if (Build.VERSION.SDK_INT >= 30) {
                    Reflect.on(cl).set("parent", new ClassLoader(){

                        @Override
                        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                            if (name.startsWith("junit")) {
                                return VClient.class.getClassLoader().loadClass(name);
                            }
                            return super.loadClass(name, resolve);
                        }
                    });
                }
            }
            try {
                if (VirtualCore.getConfig().resumeInstrumentationInMakeApplication(packageName) && this.mInstrumentation instanceof InstrumentationVirtualApp) {
                    InstrumentationVirtualApp vaIns = (InstrumentationVirtualApp)this.mInstrumentation;
                    this.mInstrumentation = vaIns.getBaseInstrumentation();
                    ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this.mInstrumentation);
                    app = LoadedApk.makeApplication.call(data.info, false, vaIns);
                    this.mInstrumentation = vaIns;
                    ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this.mInstrumentation);
                } else {
                    app = LoadedApk.makeApplication.call(data.info, false, null);
                }
            }
            catch (Throwable e) {
                throw new RuntimeException("Unable to makeApplication", e);
            }
            ContextFixer.fixContext((Context)app, data.appInfo.packageName);
            WeChat.disableBinderHook(packageName, app);
            if (isInitialApp) {
                this.mInitialApplication = app;
                ActivityThread.mInitialApplication.set(mainThread, app);
            }
            VLog.e("kook", "isInitialAppL" + isInitialApp + "    mInitialApplication:" + this.mInitialApplication);
            boolean bl = isTargetGame = packageName.equals("com.wemade.mirm") || packageName.equals("com.kakaogames.odin") || packageName.equals("com.kakaogames.archewar");
            if (isTargetGame && (providers = ActivityThread.AppBindData.providers.get(boundApp = ActivityThread.mBoundApplication.get(mainThread))) != null && !providers.isEmpty()) {
                this.installContentProviders((Context)app, providers);
            }
            if (LoadedApk.mApplication != null && (loadedApk = ContextImpl.mPackageInfo.get(context)) != null) {
                LoadedApk.mApplication.set(loadedApk, app);
            }
            if (Build.VERSION.SDK_INT >= 24 && "com.tencent.mm:recovery".equals(processName)) {
                this.fixWeChatRecovery(this.mInitialApplication);
            }
            if ("com.android.vending".equals(packageName)) {
                try {
                    context.getSharedPreferences("vending_preferences", 0).edit().putBoolean("notify_updates", false).putBoolean("notify_updates_completion", false).apply();
                    context.getSharedPreferences("finsky", 0).edit().putBoolean("auto_update_enabled", false).apply();
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            Map<String, Application> e = this.mAllApplications;
            synchronized (e) {
                this.mAllApplications.put(packageName, app);
            }
            if (!isTargetGame && (providers = ActivityThread.AppBindData.providers.get(boundApp = ActivityThread.mBoundApplication.get(mainThread))) != null && !providers.isEmpty()) {
                VLog.d("HV-", "providers:" + providers.size() + "    app:" + app, new Object[0]);
                this.installContentProviders((Context)app, providers);
            }
            if (isInitialApp) {
                VirtualCore.get().getAppCallback().beforeApplicationCreate(packageName, processName, app);
                try {
                    XposedHelpers.findAndHookMethod(Binder.class, "getCallingUid", new XC_MethodReplacement(){

                        @Override
                        protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            int ret = (Integer)XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                            return NativeEngine.onGetCallingUid(ret);
                        }
                    });
                }
                catch (Throwable th) {
                    VLog.i(TAG, "getCallingUid error:" + th.getMessage(), new Object[0]);
                    th.printStackTrace();
                }
            }
            if (cond != null) {
                cond.open();
            }
            try {
                Application createdApp;
                this.mInstrumentation.callApplicationOnCreate(this.mInitialApplication);
                InvocationStubManager.getInstance().checkEnv(HCallbackStub.class);
                if (isInitialApp && (createdApp = ActivityThread.mInitialApplication.get(mainThread)) != null) {
                    this.mInitialApplication = createdApp;
                }
            }
            catch (Exception e2) {
                if (this.mInstrumentation.onException((Object)app, (Throwable)e2)) break block66;
                throw new RuntimeException("Unable to create application " + data.appInfo.name + ": " + e2.toString(), e2);
            }
        }
        if (!packageName.contains("com.huawei.hwid")) {
            LaunchCallBack launchDelegate = VirtualCore.get().getLaunchDelegate();
            VLog.i(TAG, "LaunchCallBack", new Object[0]);
            if (launchDelegate != null) {
                launchDelegate.onLaunch(packageName, VirtualCore.get().getHostPkg(), VirtualCore.get().getContext(), app);
            }
        }
        if (isInitialApp) {
            VirtualCore.get().getAppCallback().afterApplicationCreate(packageName, processName, app);
        }
        VLog.d("HV-", "----------------------- com.lody.virtual.sandxposed.SandXposed.injectXposedModule ----------------", new Object[0]);
        VActivityManager.get().appDoneExecuting(info.packageName);
    }

    private void initDataStorage(boolean isExt, int userId, String pkg) {
        if (isExt) {
            FileUtils.ensureDirCreate(VEnvironment.getDataUserPackageDirectoryExt(userId, pkg));
            FileUtils.ensureDirCreate(VEnvironment.getDeDataUserPackageDirectoryExt(userId, pkg));
        } else {
            FileUtils.ensureDirCreate(VEnvironment.getDataUserPackageDirectory(userId, pkg));
            FileUtils.ensureDirCreate(VEnvironment.getDeDataUserPackageDirectory(userId, pkg));
        }
    }

    private void fixWeChatRecovery(Application app) {
        try {
            Field field = app.getClassLoader().loadClass("com.tencent.recovery.Recovery").getField("context");
            field.setAccessible(true);
            if (field.get(null) != null) {
                return;
            }
            field.set(null, app.getBaseContext());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint(value={"NewApi"})
    private void fixSystem() {
        if (BuildCompat.isS()) {
            try {
                Reflect.on(Canvas.class).call("setCompatibilityVersion", 26);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (BuildCompat.isQ() && BuildCompat.isEMUI()) {
            XposedBridge.hookAllMethods(AutofillManager.class, "notifyViewEntered", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    AutoFillManagerStub.disableAutoFill(param.thisObject);
                }
            });
        }
        EmuiHelper.disableCache();
    }

    private void setupUncaughtHandler() {
        ThreadGroup root;
        for(root = Thread.currentThread().getThreadGroup(); root.getParent() != null; root = root.getParent()) {
        }

        ThreadGroup newRoot = new RootThreadGroup(root);
        if (Build.VERSION.SDK_INT < 24) {
            List<ThreadGroup> groups = (List)mirror.java.lang.ThreadGroup.groups.get(root);
            synchronized(groups) {
                List<ThreadGroup> newGroups = new ArrayList(groups);
                newGroups.remove(newRoot);
                mirror.java.lang.ThreadGroup.groups.set(newRoot, newGroups);
                groups.clear();
                groups.add(newRoot);
                mirror.java.lang.ThreadGroup.groups.set(root, groups);
                Iterator var6 = newGroups.iterator();

                while(var6.hasNext()) {
                    ThreadGroup group = (ThreadGroup)var6.next();
                    if (group != newRoot) {
                        mirror.java.lang.ThreadGroup.parent.set(group, newRoot);
                    }
                }
            }
        } else {
            ThreadGroup[] groups = (ThreadGroup[])ThreadGroupN.groups.get(root);
            synchronized(groups) {
                ThreadGroup[] newGroups = (ThreadGroup[])groups.clone();
                ThreadGroupN.groups.set(newRoot, newGroups);
                ThreadGroupN.groups.set(root, new ThreadGroup[]{newRoot});
                ThreadGroup[] var15 = newGroups;
                int var16 = newGroups.length;

                for(int var8 = 0; var8 < var16; ++var8) {
                    Object group = var15[var8];
                    if (group != null && group != newRoot) {
                        ThreadGroupN.parent.set(group, newRoot);
                    }
                }

                ThreadGroupN.ngroups.set(root, 1);
            }
        }

    }

    @SuppressLint(value={"SdCardPath"})
    private void mountVirtualFS(InstalledAppInfo info, boolean isExt) {
        File wifiMacAddressFile;
        String libPath;
        String de_dataDir;
        String dataDir;
        String packageName = info.packageName;
        int userId = VUserHandle.myUserId();
        if (isExt) {
            dataDir = VEnvironment.getDataUserPackageDirectoryExt(userId, packageName).getPath();
            de_dataDir = VEnvironment.getDeDataUserPackageDirectoryExtRoot(userId).getPath();
            libPath = VEnvironment.getDataAppLibDirectoryExt(packageName).getAbsolutePath();
        } else {
            dataDir = VEnvironment.getDataUserPackageDirectory(userId, packageName).getPath();
            de_dataDir = VEnvironment.getDeDataUserPackageDirectoryRoot(userId).getPath();
            libPath = VEnvironment.getDataAppLibDirectory(packageName).getAbsolutePath();
        }
        VDeviceConfig deviceConfig = this.getDeviceConfig();
        if (deviceConfig.enable && (wifiMacAddressFile = this.getDeviceConfig().getWifiFile(userId, isExt)) != null && wifiMacAddressFile.exists()) {
            String wifiMacAddressPath = wifiMacAddressFile.getPath();
            NativeEngine.redirectFile("/sys/class/net/wlan0/address", wifiMacAddressPath);
            NativeEngine.redirectFile("/sys/class/net/eth0/address", wifiMacAddressPath);
            NativeEngine.redirectFile("/sys/class/net/wifi/address", wifiMacAddressPath);
        }
        this.forbidHost();
        String cache = new File(dataDir, "cache").getAbsolutePath();
        NativeEngine.redirectDirectory("/tmp/", cache);
        NativeEngine.redirectDirectory("/data/data/" + packageName, dataDir);
        int realUserId = VUserHandle.realUserId();
        NativeEngine.redirectDirectory("/data/user/" + realUserId + "/" + packageName, dataDir);
        if (Build.VERSION.SDK_INT >= 24) {
            NativeEngine.redirectDirectory("/data/user_de/" + realUserId + "/", de_dataDir);
        }
        NativeEngine.whitelist(libPath);
        if (info.dynamic) {
            NativeEngine.whitelist("/data/user/" + realUserId + "/" + packageName + "/lib/");
        } else {
            NativeEngine.redirectDirectory("/data/data/" + packageName + "/lib/", libPath);
            NativeEngine.redirectDirectory("/data/user/" + realUserId + "/" + packageName + "/lib/", libPath);
        }
        File userLibDir = VEnvironment.getUserAppLibDirectory(userId, packageName);
        NativeEngine.redirectDirectory(userLibDir.getPath(), libPath);
        VirtualStorageManager vsManager = VirtualStorageManager.get();
        String vsPath = vsManager.getVirtualStorage(info.packageName, userId);
        boolean enable = vsManager.isVirtualStorageEnable(info.packageName, userId);
        if (enable && vsPath != null) {
            File vsDirectory = new File(vsPath);
            if (vsDirectory.exists() || vsDirectory.mkdirs()) {
                HashSet<String> mountPoints = this.getMountPoints();
                for (String mountPoint : mountPoints) {
                    NativeEngine.redirectDirectory(mountPoint, vsPath);
                }
            }
        } else {
            this.redirectSdcard(info);
        }
        if (!info.dynamic && new File(info.getApkPath(isExt)).exists()) {
            NativeEngine.redirectFile(VEnvironment.getPackageFileStub(packageName), info.getApkPath(isExt));
            if (Build.VERSION.SDK_INT == 27) {
                DexOverride override = new DexOverride(VEnvironment.getPackageFileStub(packageName), info.getApkPath(isExt), null, null);
                NativeEngine.addDexOverride(override);
            }
        }
        if (VirtualCore.getConfig().isEnableIORedirect()) {
            if (VirtualCore.getConfig().isDisableTinker(packageName)) {
                NativeEngine.forbid("/data/data/" + packageName + "/tinker/", false);
                NativeEngine.forbid("/data/data/" + packageName + "/tinker_server/", false);
                NativeEngine.forbid("/data/data/" + packageName + "/tinker_temp/", false);
                NativeEngine.forbid("/data/user/" + realUserId + "/" + packageName + "/tinker/", false);
                NativeEngine.forbid("/data/user/" + realUserId + "/" + packageName + "/tinker_server/", false);
                NativeEngine.forbid("/data/user/" + realUserId + "/" + packageName + "/tinker_temp/", false);
            }
            NativeEngine.enableIORedirect(info);
        }
    }

    private void forbidHost() {
        ActivityManager am = (ActivityManager)VirtualCore.get().getContext().getSystemService("activity");
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == Process.myPid() || info.uid != VirtualCore.get().myUid() || VActivityManager.get().isAppPid(info.pid) || !info.processName.startsWith(StubManifest.PACKAGE_NAME) && (StubManifest.EXT_PACKAGE_NAME == null || !info.processName.startsWith(StubManifest.EXT_PACKAGE_NAME))) continue;
            NativeEngine.forbid("/proc/" + info.pid + "/maps", false);
            NativeEngine.forbid("/proc/" + info.pid + "/cmdline", false);
        }
    }

    @SuppressLint(value={"SdCardPath"})
    private HashSet<String> getMountPoints() {
        HashSet<String> mountPoints = new HashSet<String>(3);
        mountPoints.add("/mnt/sdcard/");
        mountPoints.add("/sdcard/");
        mountPoints.add("/storage/emulated/" + VUserHandle.realUserId() + "/");
        mountPoints.add("storage/emulated/" + VUserHandle.realUserId() + "/");
        String[] points = StorageManagerCompat.getAllPoints(VirtualCore.get().getContext());
        if (points != null) {
            for (String point : points) {
                if (point.endsWith("/")) {
                    mountPoints.add(point);
                    continue;
                }
                mountPoints.add(point + "/");
            }
        }
        return mountPoints;
    }

    private Context createPackageContext(String packageName) {
        try {
            Context hostContext = VirtualCore.get().getContext();
            Context packageContext = hostContext.createPackageContext(packageName, 3);
            PackageManager packageManager = packageContext.getPackageManager();
            VLog.d("HV-", " HostContext :" + hostContext + "   contextimpl:" + packageContext + "    packageName:" + packageName, new Object[0]);
            return packageContext;
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
            VirtualRuntime.crash(var5);
            throw new RuntimeException();
        }
    }
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void installContentProviders(Context app, List<ProviderInfo> providers) {
        long origId = Binder.clearCallingIdentity();
        Object mainThread = VirtualCore.mainThread();
        try {
            for (ProviderInfo cpi : providers) {
                try {
                    ActivityThread.installProvider(mainThread, app, cpi, null);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            Binder.restoreCallingIdentity((long)origId);
        }
    }

    @Override
    public IBinder acquireProviderClient(ProviderInfo info) {
        this.bindApplication(info.packageName, info.processName);
        IInterface provider = null;
        String[] authorities = info.authority.split(";");
        String authority = authorities.length == 0 ? info.authority : authorities[0];
        ContentResolver resolver = VirtualCore.get().getContext().getContentResolver();
        ContentProviderClient client = null;
        try {
            client = resolver.acquireUnstableContentProviderClient(authority);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (client != null) {
            provider = mirror.android.content.ContentProviderClient.mContentProvider.get(client);
            client.release();
        }
        VLog.e(TAG, "acquireProviderClient " + info + " result: " + provider + " process: " + VirtualRuntime.getProcessName() + "    authority:" + authority);
        return provider != null ? provider.asBinder() : null;
    }

    private void fixInstalledProviders() {
        this.clearSettingProvider();
        Map<Object, Object> clientMap = (Map)ActivityThread.mProviderMap.get(VirtualCore.mainThread());
        Iterator var2 = clientMap.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<Object, Object> e = (Map.Entry)var2.next();
            Object clientRecord = e.getValue();
            IInterface provider;
            Object holder;
            ProviderInfo info;
            if (BuildCompat.isOreo()) {
                provider = (IInterface) ActivityThread.ProviderClientRecordJB.mProvider.get(clientRecord);
                holder = ActivityThread.ProviderClientRecordJB.mHolder.get(clientRecord);
                if (holder != null) {
                    info = (ProviderInfo)ContentProviderHolderOreo.info.get(holder);
                    if (!info.authority.startsWith(StubManifest.STUB_CP_AUTHORITY)) {
                        provider = ProviderHook.createProxy(true, info.authority, provider);
                        ActivityThread.ProviderClientRecordJB.mProvider.set(clientRecord, provider);
                        ContentProviderHolderOreo.provider.set(holder, provider);
                    }
                }
            } else {
                provider = (IInterface) ActivityThread.ProviderClientRecordJB.mProvider.get(clientRecord);
                holder = ActivityThread.ProviderClientRecordJB.mHolder.get(clientRecord);
                if (holder != null) {
                    info = (ProviderInfo) IActivityManager.ContentProviderHolder.info.get(holder);
                    if (!info.authority.startsWith(StubManifest.STUB_CP_AUTHORITY)) {
                        provider = ProviderHook.createProxy(true, info.authority, provider);
                        ActivityThread.ProviderClientRecordJB.mProvider.set(clientRecord, provider);
                        IActivityManager.ContentProviderHolder.provider.set(holder, provider);
                    }
                }
            }
        }

    }


    private void clearSettingProvider() {
        Object cache = Settings.System.sNameValueCache.get();
        if (cache != null) {
            VClient.clearContentProvider(cache);
        }
        if ((cache = Settings.Secure.sNameValueCache.get()) != null) {
            VClient.clearContentProvider(cache);
        }
        if (Settings.Global.TYPE != null && (cache = Settings.Global.sNameValueCache.get()) != null) {
            VClient.clearContentProvider(cache);
        }
    }

    private static void clearContentProvider(Object cache) {
        if (BuildCompat.isOreo()) {
            Object holder = Settings.NameValueCacheOreo.mProviderHolder.get(cache);
            if (holder != null) {
                Settings.ContentProviderHolder.mContentProvider.set(holder, null);
            }
        } else {
            Settings.NameValueCache.mContentProvider.set(cache, null);
        }
    }

    @Override
    public void finishActivity(IBinder token) {
        this.sendMessage(13, token);
    }

    @Override
    public void scheduleNewIntent(String creator, IBinder token, Intent intent) {
        NewIntentData data = new NewIntentData();
        data.creator = creator;
        data.token = token;
        data.intent = intent;
        this.sendMessage(11, data);
    }

    public void scheduleReceiver(String processName, ComponentName component, Intent intent, android.content.BroadcastReceiver.PendingResult pendingResult) {
        ReceiverData receiverData = new ReceiverData();
        receiverData.pendingResult = pendingResult;
        receiverData.intent = intent;
        receiverData.component = component;
        receiverData.processName = processName;
        receiverData.stacktrace = new Exception();
        this.sendMessage(12, receiverData);
    }


    private void handleReceiver(ReceiverData data) {
        android.content.BroadcastReceiver.PendingResult result = data.pendingResult;

        try {
            Context context = this.mInitialApplication.getBaseContext();
            Context receiverContext = (Context)ContextImpl.getReceiverRestrictedContext.call(context, new Object[0]);
            ContextFixer.fixContext(receiverContext, data.component.getPackageName());
            String className = data.component.getClassName();
            ClassLoader classLoader = (ClassLoader)LoadedApk.getClassLoader.call(this.mBoundApplication.info, new Object[0]);
            android.content.BroadcastReceiver receiver = (android.content.BroadcastReceiver)classLoader.loadClass(className).newInstance();
            mirror.android.content.BroadcastReceiver.setPendingResult.call(receiver, new Object[]{result});
            data.intent.setExtrasClassLoader(context.getClassLoader());
            ComponentUtils.unpackFillIn(data.intent, classLoader);
            if (data.intent.getComponent() == null) {
                data.intent.setComponent(data.component);
            }

            FakeIdentityBinder.setSystemIdentity();
            receiver.onReceive(receiverContext, data.intent);
            if (mirror.android.content.BroadcastReceiver.getPendingResult.call(receiver, new Object[0]) != null) {
                IBinder token = (IBinder) BroadcastReceiver.PendingResult.mToken.get(result);
                if (!VActivityManager.get().broadcastFinish(token)) {
                    result.finish();
                }
            }

        } catch (Exception var9) {
            data.stacktrace.printStackTrace();
            throw new RuntimeException("Unable to start receiver " + data.component + ": " + var9.toString(), var9);
        }
    }


    public ClassLoader getClassLoader() {
        return LoadedApk.getClassLoader.call(this.mBoundApplication.info, new Object[0]);
    }

    public android.app.Service createService(ServiceInfo info, IBinder token) {
        android.app.Service service;
        this.bindApplication(info.packageName, info.processName);
        ClassLoader classLoader = LoadedApk.getClassLoader.call(this.mBoundApplication.info, new Object[0]);
        try {
            service = (android.app.Service)classLoader.loadClass(info.name).newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to instantiate service " + info.name + ": " + e.toString(), e);
        }
        try {
            Context context = VirtualCore.get().getContext().createPackageContext(info.packageName, Context.CONTEXT_INCLUDE_CODE);
            ContextImpl.setOuterContext.call(context, service);
            Service.attach.call(service, context, VirtualCore.mainThread(), info.name, token, this.mInitialApplication, ActivityManagerNative.getDefault.call(new Object[0]));
            ContextFixer.fixContext(context, info.packageName);
            service.onCreate();
            return service;
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to create service " + info.name + ": " + e.toString(), e);
        }
    }

    @Override
    public IBinder createProxyService(ComponentName component, IBinder binder) {
        return ProxyServiceFactory.getProxyService((Context)this.getCurrentApplication(), component, binder);
    }

    @Override
    public String getDebugInfo() {
        return VirtualRuntime.getProcessName();
    }

    @Override
    public boolean finishReceiver(IBinder token) {
        return StaticReceiverSystem.get().broadcastFinish(token);
    }

    @Override
    public List<ActivityManager.RunningServiceInfo> getServices() {
        return VServiceRuntime.getInstance().getServices();
    }

    private void redirectSdcardAndroidData(InstalledAppInfo info) {
        SettingConfig config = VirtualCore.getConfig();
        HashSet<String> mountPoints = this.getMountPoints();
        String[] dirs = new String[]{"/Android/data/", "/Android/media/", "/Android/obb/"};
        File replace = VirtualCore.get().getContext().getExternalFilesDir(config.getVirtualSdcardAndroidDataName() + "/" + VUserHandle.myUserId() + "/");
        if (info.packageName.equals("com.nexon.hit2") && "samsung".equals(Build.BRAND) && Build.VERSION.SDK_INT == 29) {
            VLog.e("HV-", "由于 com.nexon.hit2 游戏在android 10 上重定向出现问题,这里将重定向的问题修复掉 " + info.packageName);
            return;
        }
        if (info.packageName.equals("com.nexon.er") && "samsung".equals(Build.BRAND) && Build.VERSION.SDK_INT == 29) {
            VLog.e("HV-", "由于 com.nexon.er 游戏在android 10 上重定向出现问题,这里将重定向的问题修复掉 " + info.packageName);
            return;
        }
        if (!replace.exists() && !replace.mkdirs()) {
            VLog.e(TAG, "failed to create dir: " + replace);
        }
        for (String mountPoint : mountPoints) {
            for (String dir : dirs) {
                File origin = new File(mountPoint + dir);
                File target = new File(replace.getPath() + dir);
                if (!target.exists() && !target.mkdirs()) {
                    VLog.e(TAG, "failed to create dir: " + target);
                }
                NativeEngine.redirectDirectory(origin.getPath(), replace.getPath() + dir);
            }
        }
    }

    private void redirectSdcard(InstalledAppInfo info) {
        SettingConfig config = VirtualCore.getConfig();
        this.redirectSdcardAndroidData(info);
        if (BuildCompat.isR() && VirtualCore.get().getTargetSdkVersion() >= 30) {
            int userId = VUserHandle.myUserId();
            ApplicationInfo applicationInfo = info.getApplicationInfo(userId);
            if (applicationInfo == null) {
                return;
            }
            int targetSdkVersion = applicationInfo.targetSdkVersion;
            if (targetSdkVersion < 30) {
                HashSet<String> mountPoints = this.getMountPoints();
                File replace = VirtualCore.get().getContext().getExternalFilesDir(config.getVirtualSdcardAndroidDataName() + "/" + VUserHandle.myUserId() + "/");
                if (VirtualCore.get().isSharedUserId()) {
                    replace = new File(replace.toString().replace(StubManifest.EXT_PACKAGE_NAME, StubManifest.PACKAGE_NAME));
                }
                if (!replace.exists() && !replace.mkdirs()) {
                    VLog.e(TAG, "failed to create dir: " + replace);
                }
                for (String mountPoint : mountPoints) {
                    File origin = new File(mountPoint + "/");
                    NativeEngine.redirectDirectory(origin.getPath(), replace.getPath());
                }
                for (String mountPoint : mountPoints) {
                    try {
                        String[] standardDirectories;
                        for (String directory : standardDirectories = (String[])Reflect.on(Environment.class).field("STANDARD_DIRECTORIES").get()) {
                            String standardPath = NativeEngine.pathCat(mountPoint, directory);
                            NativeEngine.whitelist(standardPath);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressLint(value={"HandlerLeak"})
    private class H
    extends Handler {
        private H() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 11: {
                    VClient.this.handleNewIntent((NewIntentData)msg.obj);
                    break;
                }
                case 12: {
                    VClient.this.handleReceiver((ReceiverData)msg.obj);
                    break;
                }
                case 13: {
                    VActivityManager.get().finishActivity((IBinder)msg.obj);
                }
            }
        }
    }

    private static final class ReceiverData {
        android.content.BroadcastReceiver.PendingResult pendingResult;
        Intent intent;
        ComponentName component;
        String processName;
        Throwable stacktrace;

        private ReceiverData() {
        }
    }

    private static final class AppBindData {
        String processName;
        ApplicationInfo appInfo;
        List<ProviderInfo> providers;
        Object info;

        private AppBindData() {
        }
    }

    private static final class NewIntentData {
        String creator;
        IBinder token;
        Intent intent;

        private NewIntentData() {
        }
    }

    private static class RootThreadGroup
    extends ThreadGroup {
        RootThreadGroup(ThreadGroup parent) {
            super(parent, "VA");
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            CrashHandler handler = gClient.crashHandler;
            if (handler != null) {
                handler.handleUncaughtException(t, e);
            } else {
                VLog.e("uncaught", e);
            }
        }
    }
}

