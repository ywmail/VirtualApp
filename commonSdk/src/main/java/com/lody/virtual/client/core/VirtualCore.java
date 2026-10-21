/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RecentTaskInfo
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningTaskInfo
 *  android.app.Application
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.content.pm.ShortcutInfo
 *  android.content.pm.ShortcutInfo$Builder
 *  android.content.pm.ShortcutManager
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Icon
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.ConditionVariable
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.IBinder$DeathRecipient
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.os.Process
 *  android.os.RemoteException
 *  com.kook.librelease.R$string
 */
package com.lody.virtual.client.core;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import com.kook.librelease.R;
import com.lody.virtual.PineXposed;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.CrashHandler;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.LaunchCallBack;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.client.env.LocalPackageCache;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.BinderProvider;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IAppManager;
import com.lody.virtual.server.interfaces.IPackageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mirror.android.app.ActivityThread;

public final class VirtualCore {
    public static final int GET_HIDDEN_APP = 1;
    private static final String TAG = VirtualCore.class.getSimpleName();
    @SuppressLint(value={"StaticFieldLeak"})
    private static VirtualCore gCore = new VirtualCore();
    private final int myUid = Process.myUid();
    private int remoteUid = -1;
    private HostPackageManager hostPackageManager;
    private String hostPkgName;
    private Object mainThread;
    private Context context;
    private String mainProcessName;
    private String processName;
    private ProcessType processType;
    private boolean isMainPackage;
    private IAppManager mService;
    private boolean isStartUp;
    private PackageInfo mHostPkgInfo;
    private ConditionVariable mInitLock;
    private AppCallback mAppCallback;
    private TaskDescriptionDelegate mTaskDescriptionDelegate;
    private SettingConfig mConfig;
    private AppRequestListener mAppRequestListener;
    private LaunchCallBack launchDelegate;
    private Handler mHandlerASyc;
    private final BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent) {
            VLog.w("DownloadManager", "receive download completed brodcast: " + intent, new Object[0]);
            if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                VActivityManager.get().handleDownloadCompleteIntent(intent);
            }
        }
    };
    boolean scanned = false;

    private VirtualCore() {
        HandlerThread handlerThread = new HandlerThread("mHandlerASyc");
        handlerThread.start();
        this.mHandlerASyc = new Handler(handlerThread.getLooper());
    }

    public Handler getHandlerASyc() {
        return this.mHandlerASyc;
    }

    public static SettingConfig getConfig() {
        return VirtualCore.get().mConfig;
    }

    public static void setConfig(SettingConfig config) {
        VirtualCore.get().mConfig = config;
    }

    public static VirtualCore get() {
        return gCore;
    }

    public static PackageManager getPM() {
        return VirtualCore.get().getPackageManager();
    }

    public static Object mainThread() {
        return VirtualCore.get().mainThread;
    }

    public ConditionVariable getInitLock() {
        return this.mInitLock;
    }

    public int myUid() {
        return this.myUid;
    }

    public int remoteUid() {
        return this.remoteUid;
    }

    public int myUserId() {
        return VUserHandle.getUserId(this.myUid);
    }

    public AppCallback getAppCallback() {
        return this.mAppCallback == null ? AppCallback.EMPTY : this.mAppCallback;
    }

    public void setAppCallback(AppCallback callback) {
        this.mAppCallback = callback;
    }

    public void setCrashHandler(CrashHandler handler) {
        VClient.get().setCrashHandler(handler);
    }

    public TaskDescriptionDelegate getTaskDescriptionDelegate() {
        return this.mTaskDescriptionDelegate;
    }

    public void setTaskDescriptionDelegate(TaskDescriptionDelegate mTaskDescriptionDelegate) {
        this.mTaskDescriptionDelegate = mTaskDescriptionDelegate;
    }

    public LaunchCallBack getLaunchDelegate() {
        return this.launchDelegate;
    }

    public void setLaunchDelegate(LaunchCallBack launchDelegate) {
        this.launchDelegate = launchDelegate;
    }

    public int[] getGids() {
        return this.mHostPkgInfo.gids;
    }

    public ApplicationInfo getHostApplicationInfo() {
        return this.mHostPkgInfo.applicationInfo;
    }

    public Context getContext() {
        return this.context;
    }

    public PackageManager getPackageManager() {
        return this.context.getPackageManager();
    }

    public boolean isSystemApp() {
        ApplicationInfo applicationInfo = this.getContext().getApplicationInfo();
        return (applicationInfo.flags & 1) != 0 || (applicationInfo.flags & 0x80) != 0;
    }

    public String getHostPkg() {
        return this.hostPkgName;
    }

    public int getTargetSdkVersion() {
        return this.context.getApplicationInfo().targetSdkVersion;
    }

    public HostPackageManager getHostPackageManager() {
        return this.hostPackageManager;
    }

    public boolean checkSelfPermission(String permission2, boolean isExt) {
        if (isExt) {
            return 0 == this.hostPackageManager.checkPermission(permission2, StubManifest.EXT_PACKAGE_NAME);
        }
        return 0 == this.hostPackageManager.checkPermission(permission2, StubManifest.PACKAGE_NAME);
    }

    public void waitStartup() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        if (this.mInitLock != null) {
            this.mInitLock.block();
        }
    }

    public int getUidForSharedUser(String sharedUserName) {
        try {
            return this.getService().getUidForSharedUser(sharedUserName);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) {
        try {
            return this.getService().installPackage(uri, params);
        }
        catch (RemoteException e) {
            return (VAppInstallerResult)VirtualRuntime.crash(e);
        }
    }


    public void startup(Application application, Context context, SettingConfig config) throws Throwable {
        if (!this.isStartUp) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("VirtualCore.startup() must called in main thread.");
            }

            if (!context.getPackageName().equals(config.getMainPackageName()) && !context.getPackageName().equals(config.getExtPackageName())) {
                throw new IllegalArgumentException("Neither the main package nor the extension package, you seem to have configured the wrong package name, expected " + config.getMainPackageName() + " or " + config.getExtPackageName() + ", but got " + context.getPackageName());
            }

            this.mInitLock = new ConditionVariable();
            this.mConfig = config;
            String packageName = config.getMainPackageName();
            String ext_packageName = config.getExtPackageName();
            Constants.ACTION_SHORTCUT = packageName + Constants.ACTION_SHORTCUT;
            Constants.ACTION_BADGER_CHANGE = packageName + Constants.ACTION_BADGER_CHANGE;
            StubManifest.STUB_CP_AUTHORITY = packageName + ".virtual_stub_";
            StubManifest.PROXY_CP_AUTHORITY = packageName + ".provider_proxy";
            File externalFilesDir = context.getExternalFilesDir(config.getVirtualSdcardAndroidDataName());
            if (!externalFilesDir.exists()) {
                externalFilesDir.mkdirs();
            }

            if (ext_packageName == null) {
                ext_packageName = "NO_EXT";
            }

            StubManifest.PACKAGE_NAME = packageName;
            StubManifest.EXT_PACKAGE_NAME = ext_packageName;
            StubManifest.EXT_STUB_CP_AUTHORITY = "com.carlos.multiapp.virtual_stub_ext_";
            StubManifest.EXT_PROXY_CP_AUTHORITY = "com.carlos.multiapp.provider_proxy_ext";
            this.context = context;
            this.isMainPackage = context.getPackageName().equals(StubManifest.PACKAGE_NAME);
            NativeEngine.bypassHiddenAPIEnforcementPolicyIfNeeded();
            this.hostPackageManager = HostPackageManager.init();
            this.mHostPkgInfo = this.hostPackageManager.getPackageInfo(packageName, 256L);
            this.detectProcessType();
            if (this.isVAppProcess()) {
                this.mainThread = ActivityThread.currentActivityThread.call(new Object[0]);
                if (this.mainThread != null && BuildCompat.isT()) {
                    VLog.e("HV-", "applicationContext :" + application);
                    ActivityThread.mInitialApplication.set(this.mainThread, application);
                }

                LocalPackageCache.init();
            }

            ApplicationInfo info;
            if (this.isExtPackage()) {
                try {
                    info = this.getHostPackageManager().getApplicationInfo(packageName, 0L);
                    if (info != null) {
                        this.remoteUid = info.uid;
                    }
                } catch (PackageManager.NameNotFoundException var11) {
                }
            } else {
                try {
                    info = this.getHostPackageManager().getApplicationInfo(ext_packageName, 0L);
                    if (info != null) {
                        this.remoteUid = info.uid;
                    }
                } catch (PackageManager.NameNotFoundException var10) {
                }
            }

            if (this.isVAppProcess() || this.isExtHelperProcess()) {
                ServiceManagerNative.linkToDeath(new IBinder.DeathRecipient() {
                    public void binderDied() {
                        VLog.e(VirtualCore.TAG, "Server was dead, kill process: " + VirtualCore.this.processType.name());
                        Process.killProcess(Process.myPid());
                    }
                });
            }

            if (this.isServerProcess() || this.isExtHelperProcess()) {
                VLog.w("DownloadManager", "Listening DownloadManager action  in process: " + this.processType, new Object[0]);
                IntentFilter filter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");

                try {
                    context.registerReceiver(this.mDownloadCompleteReceiver, filter);
                } catch (Throwable var9) {
                    var9.printStackTrace();
                }
            }

            InvocationStubManager invocationStubManager = InvocationStubManager.getInstance();
            invocationStubManager.init();
            invocationStubManager.injectAll();
            this.isStartUp = true;
            this.mInitLock.open();
        }

    }

    public void waitForEngine() {
        ServiceManagerNative.ensureServerStarted();
    }

    public boolean isEngineLaunched() {
        if (this.isExtPackage()) {
            return true;
        }
        if (!BinderProvider.scanApps) {
            this.scanApps();
        }
        ActivityManager am = (ActivityManager)this.context.getSystemService("activity");
        String engineProcessName = this.getEngineProcessName();
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (!info.processName.endsWith(engineProcessName)) continue;
            return true;
        }
        return false;
    }

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessesEx() {
        ActivityManager am = (ActivityManager)this.context.getSystemService("activity");
        ArrayList<ActivityManager.RunningAppProcessInfo> list = new ArrayList<ActivityManager.RunningAppProcessInfo>(am.getRunningAppProcesses());
        if (!VirtualCore.get().isSharedUserId()) {
            List<ActivityManager.RunningAppProcessInfo> list64 = VExtPackageAccessor.getRunningAppProcesses();
            list.addAll(list64);
        }
        return list;
    }

    public ActivityManager.RunningAppProcessInfo getProccessInfo(String processName, int uid) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessesEx = this.getRunningAppProcessesEx();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessesEx) {
            if (!info.processName.equals(processName) || info.uid != uid) continue;
            return info;
        }
        return null;
    }

    public List<ActivityManager.RunningTaskInfo> getRunningTasksEx(int maxNum) {
        ActivityManager am = (ActivityManager)this.context.getSystemService("activity");
        ArrayList<ActivityManager.RunningTaskInfo> list = new ArrayList<ActivityManager.RunningTaskInfo>(am.getRunningTasks(maxNum));
        if (!VirtualCore.get().isSharedUserId()) {
            List<ActivityManager.RunningTaskInfo> list64 = VExtPackageAccessor.getRunningTasks(maxNum);
            list.addAll(list64);
        }
        return list;
    }

    public List<ActivityManager.RecentTaskInfo> getRecentTasksEx(int maxNum, int flags) {
        ActivityManager am = (ActivityManager)this.context.getSystemService("activity");
        ArrayList<ActivityManager.RecentTaskInfo> list = new ArrayList<ActivityManager.RecentTaskInfo>(am.getRecentTasks(maxNum, flags));
        if (!VirtualCore.get().isSharedUserId()) {
            List<ActivityManager.RecentTaskInfo> list64 = VExtPackageAccessor.getRecentTasks(maxNum, flags);
            list.addAll(list64);
        }
        return list;
    }

    public String getEngineProcessName() {
        return this.context.getString(R.string.server_process_name);
    }

    public void initialize(VirtualInitializer initializer) {
        if (initializer == null) {
            throw new IllegalStateException("Initializer = NULL");
        }
        switch (this.processType) {
            case Main: {
                initializer.onMainProcess();
                break;
            }
            case VAppClient: {
                if (Build.VERSION.SDK_INT >= 21) {
                    PineXposed.init();
                }
                initializer.onVirtualProcess();
                break;
            }
            case Server: {
                initializer.onServerProcess();
                break;
            }
            case CHILD: {
                initializer.onChildProcess();
            }
        }
    }

    private static String getProcessName(Context context) {
        int pid = Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid != pid) continue;
            processName = info.processName;
            break;
        }
        if (processName == null) {
            throw new RuntimeException("processName = null");
        }
        return processName;
    }

    private void detectProcessType() {
        this.hostPkgName = this.context.getApplicationInfo().packageName;
        this.mainProcessName = this.context.getApplicationInfo().processName;
        this.processName = VirtualCore.getProcessName(this.context);
        this.processType = this.processName.equals(this.mainProcessName) ? ProcessType.Main : (this.processName.endsWith(Constants.SERVER_PROCESS_NAME) ? ProcessType.Server : (this.processName.endsWith(Constants.HELPER_PROCESS_NAME) ? ProcessType.Helper : (VActivityManager.get().isAppProcess(this.processName) ? ProcessType.VAppClient : ProcessType.CHILD)));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private IAppManager getService() {
        if (!IInterfaceUtils.isAlive(this.mService)) {
            VirtualCore virtualCore = this;
            synchronized (virtualCore) {
                Object remote = this.getStubInterface();
                this.mService = LocalProxyUtils.genProxy(IAppManager.class, remote);
            }
        }
        return this.mService;
    }

    private Object getStubInterface() {
        return IAppManager.Stub.asInterface(ServiceManagerNative.getService("app"));
    }

    public boolean isVAppProcess() {
        return ProcessType.VAppClient == this.processType;
    }

    public boolean isExtHelperProcess() {
        return ProcessType.Helper == this.processType;
    }

    public boolean isMainProcess() {
        return ProcessType.Main == this.processType;
    }

    public boolean isMainPackage() {
        return this.isMainPackage;
    }

    public boolean isExtPackage() {
        return !this.isMainPackage;
    }

    public boolean isChildProcess() {
        return ProcessType.CHILD == this.processType;
    }

    public boolean isServerProcess() {
        return ProcessType.Server == this.processType;
    }

    public String getProcessName() {
        return this.processName;
    }

    public String getMainProcessName() {
        return this.mainProcessName;
    }

    public boolean isAppRunning(String packageName, int userId, boolean foreground) {
        return VActivityManager.get().isAppRunning(packageName, userId, foreground);
    }

    public boolean isAppInstalled(String pkg) {
        try {
            return this.getService().isAppInstalled(pkg);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public boolean isPackageLaunchable(String packageName) {
        InstalledAppInfo info = this.getInstalledAppInfo(packageName, 0);
        return info != null && this.getLaunchIntent(packageName, info.getInstalledUsers()[0]) != null;
    }

    public Intent getLaunchIntent(String packageName, int userId) {
        VPackageManager pm = VPackageManager.get();
        Intent intentToResolve = new Intent("android.intent.action.MAIN");
        intentToResolve.addCategory("android.intent.category.INFO");
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(this.context), 0, userId);
        if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory("android.intent.category.INFO");
            intentToResolve.addCategory("android.intent.category.LAUNCHER");
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(this.context), 0, userId);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get((int)0).activityInfo.packageName, ris.get((int)0).activityInfo.name);
        return intent;
    }

    public boolean createShortcut(int userId, String packageName, OnEmitShortcutListener listener) {
        return this.createShortcut(userId, packageName, null, listener);
    }

    public boolean createShortcut(int userId, String packageName, Intent splash, OnEmitShortcutListener listener) {
        Intent targetIntent;
        Bitmap icon;
        String name;
        InstalledAppInfo setting = this.getInstalledAppInfo(packageName, 0);
        if (setting == null) {
            return false;
        }
        ApplicationInfo appInfo = setting.getApplicationInfo(userId);
        PackageManager pm = this.context.getPackageManager();
        try {
            CharSequence sequence = appInfo.loadLabel(pm);
            name = sequence.toString();
            icon = BitmapUtils.drawableToBitmap(appInfo.loadIcon(pm));
        }
        catch (Throwable e) {
            return false;
        }
        if (listener != null) {
            Bitmap newIcon;
            String newName = listener.getName(name);
            if (newName != null) {
                name = newName;
            }
            if ((newIcon = listener.getIcon(icon)) != null) {
                icon = newIcon;
            }
        }
        if ((targetIntent = this.getLaunchIntent(packageName, userId)) == null) {
            return false;
        }
        Intent shortcutIntent = this.wrapperShortcutIntent(targetIntent, splash, packageName, userId);
        if (Build.VERSION.SDK_INT >= 26) {
            ShortcutInfo likeShortcut = new ShortcutInfo.Builder(this.getContext(), packageName + "@" + userId).setLongLabel((CharSequence)name).setShortLabel((CharSequence)name).setIcon(Icon.createWithBitmap((Bitmap)icon)).setIntent(shortcutIntent).build();
            ShortcutManager shortcutManager = (ShortcutManager)this.getContext().getSystemService(ShortcutManager.class);
            if (shortcutManager != null) {
                try {
                    shortcutManager.requestPinShortcut(likeShortcut, PendingIntent.getActivity((Context)this.getContext(), (int)(packageName.hashCode() + userId), (Intent)shortcutIntent, (int)0x8000000).getIntentSender());
                }
                catch (Throwable e) {
                    return false;
                }
            }
        } else {
            Intent addIntent = new Intent();
            addIntent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)shortcutIntent);
            addIntent.putExtra("android.intent.extra.shortcut.NAME", name);
            addIntent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)BitmapUtils.warrperIcon(icon, 256, 256));
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            try {
                this.context.sendBroadcast(addIntent);
            }
            catch (Throwable e) {
                return false;
            }
        }
        return true;
    }

    public boolean removeShortcut(int userId, String packageName, Intent splash, OnEmitShortcutListener listener) {
        Intent targetIntent;
        String newName;
        String name;
        InstalledAppInfo setting = this.getInstalledAppInfo(packageName, 0);
        if (setting == null) {
            return false;
        }
        ApplicationInfo appInfo = setting.getApplicationInfo(userId);
        PackageManager pm = this.context.getPackageManager();
        try {
            CharSequence sequence = appInfo.loadLabel(pm);
            name = sequence.toString();
        }
        catch (Throwable e) {
            return false;
        }
        if (listener != null && (newName = listener.getName(name)) != null) {
            name = newName;
        }
        if ((targetIntent = this.getLaunchIntent(packageName, userId)) == null) {
            return false;
        }
        Intent shortcutIntent = this.wrapperShortcutIntent(targetIntent, splash, packageName, userId);
        if (Build.VERSION.SDK_INT < 26) {
            Intent addIntent = new Intent();
            addIntent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)shortcutIntent);
            addIntent.putExtra("android.intent.extra.shortcut.NAME", name);
            addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            this.context.sendBroadcast(addIntent);
        }
        return true;
    }

    public Intent wrapperShortcutIntent(Intent intent, Intent splash, String packageName, int userId) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.addCategory("android.intent.category.DEFAULT");
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);
        shortcutIntent.setPackage(this.getHostPkg());
        if (splash != null) {
            shortcutIntent.putExtra("_VA_|_splash_", splash.toUri(0));
        }
        shortcutIntent.putExtra("_VA_|_pkg_", packageName);
        shortcutIntent.putExtra("_VA_|_uri_", intent.toUri(0));
        shortcutIntent.putExtra("_VA_|_user_id_", userId);
        return shortcutIntent;
    }

    public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) {
        try {
            return this.getService().getInstalledAppInfo(pkg, flags);
        }
        catch (RemoteException e) {
            return (InstalledAppInfo)VirtualRuntime.crash(e);
        }
    }

    public int getInstalledAppCount() {
        try {
            return this.getService().getInstalledAppCount();
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public boolean isStartup() {
        return this.isStartUp;
    }

    public boolean uninstallPackageAsUser(String pkgName, int userId) {
        try {
            return this.getService().uninstallPackageAsUser(pkgName, userId);
        }
        catch (RemoteException remoteException) {
            return false;
        }
    }

    public boolean uninstallPackage(String pkgName) {
        try {
            return this.getService().uninstallPackage(pkgName);
        }
        catch (RemoteException remoteException) {
            return false;
        }
    }

    public Resources getResources(String pkg) throws Resources.NotFoundException {
        InstalledAppInfo installedAppInfo = this.getInstalledAppInfo(pkg, 0);
        if (installedAppInfo != null) {
            AssetManager assets = mirror.android.content.res.AssetManager.ctor.newInstance();
            mirror.android.content.res.AssetManager.addAssetPath.call(assets, installedAppInfo.getApkPath());
            Resources hostRes = this.context.getResources();
            return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
        }
        throw new Resources.NotFoundException(pkg);
    }

    public synchronized ActivityInfo resolveActivityInfo(Intent intent, int userId) {
        if (SpecialComponentList.shouldBlockIntent(intent)) {
            return null;
        }
        ActivityInfo activityInfo = null;
        if (intent.getComponent() == null) {
            ResolveInfo resolveInfo = VPackageManager.get().resolveIntent(intent, intent.getType(), 0, userId);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                activityInfo = resolveInfo.activityInfo;
                intent.setClassName(activityInfo.packageName, activityInfo.name);
            }
        } else {
            activityInfo = this.resolveActivityInfo(intent.getComponent(), userId);
        }
        return activityInfo;
    }

    public ActivityInfo resolveActivityInfo(ComponentName componentName, int userId) {
        return VPackageManager.get().getActivityInfo(componentName, 0, userId);
    }

    public ServiceInfo resolveServiceInfo(Intent intent, int userId) {
        if (SpecialComponentList.shouldBlockIntent(intent)) {
            return null;
        }
        ServiceInfo serviceInfo = null;
        ResolveInfo resolveInfo = VPackageManager.get().resolveService(intent, intent.getType(), 0, userId);
        if (resolveInfo != null) {
            serviceInfo = resolveInfo.serviceInfo;
        }
        return serviceInfo;
    }

    public void killApp(String pkg, int userId) {
        VActivityManager.get().killAppByPkg(pkg, userId);
    }

    public void killAllApps() {
        VActivityManager.get().killAllApps();
    }

    public List<InstalledAppInfo> getInstalledApps(int flags) {
        try {
            return this.getService().getInstalledApps(flags);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) {
        try {
            return this.getService().getInstalledAppsAsUser(userId, flags);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public void scanApps() {
        if (this.scanned) {
            return;
        }
        try {
            this.getService().scanApps();
            this.scanned = true;
        }
        catch (RemoteException remoteException) {
            // empty catch block
        }
    }

    public AppRequestListener getAppRequestListener() {
        return this.mAppRequestListener;
    }

    public void setAppRequestListener(AppRequestListener listener) {
        this.mAppRequestListener = listener;
    }

    public boolean isPackageLaunched(int userId, String packageName) {
        try {
            return this.getService().isPackageLaunched(userId, packageName);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public void setPackageHidden(int userId, String packageName, boolean hidden) {
        try {
            this.getService().setPackageHidden(userId, packageName, hidden);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean installPackageAsUser(int userId, String packageName) {
        try {
            return this.getService().installPackageAsUser(userId, packageName);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public boolean isAppInstalledAsUser(int userId, String packageName) {
        try {
            return this.getService().isAppInstalledAsUser(userId, packageName);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int[] getPackageInstalledUsers(String packageName) {
        try {
            return this.getService().getPackageInstalledUsers(packageName);
        }
        catch (RemoteException e) {
            return (int[])VirtualRuntime.crash(e);
        }
    }

    public List<String> getInstalledSplitNames(String packageName) {
        try {
            return this.getService().getInstalledSplitNames(packageName);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public void registerObserver(IPackageObserver observer) {
        try {
            this.getService().registerObserver(observer);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void unregisterObserver(IPackageObserver observer) {
        try {
            this.getService().unregisterObserver(observer);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public boolean isOutsideInstalled(String packageName) {
        if (packageName == null) {
            return false;
        }
        try {
            this.hostPackageManager.getApplicationInfo(packageName, 0L);
            return true;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return false;
        }
    }

    public boolean isExtPackageInstalled() {
        return this.isOutsideInstalled(StubManifest.EXT_PACKAGE_NAME);
    }

    public boolean isRunInExtProcess(String packageName) {
        try {
            return this.getService().isRunInExtProcess(packageName);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public boolean cleanPackageData(String pkg, int userId) {
        try {
            return this.getService().cleanPackageData(pkg, userId);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int getAppPid(String pkg, int userId, String proccessName) {
        return VActivityManager.get().getAppPid(pkg, userId, proccessName);
    }

    public boolean isSharedUserId() {
        return this.myUid() == this.remoteUid();
    }

    public static abstract class VirtualInitializer {
        public void onMainProcess() {
        }

        public void onVirtualProcess() {
        }

        public void onServerProcess() {
        }

        public void onChildProcess() {
        }
    }

    public static interface OnEmitShortcutListener {
        public Bitmap getIcon(Bitmap var1);

        public String getName(String var1);
    }

    public static interface AppRequestListener {
        public void onRequestInstall(String var1);

        public void onRequestUninstall(String var1);
    }

    private static enum ProcessType {
        Server,
        VAppClient,
        Main,
        Helper,
        CHILD;

    }

    public static abstract class PackageObserver
    extends IPackageObserver.Stub {
    }
}

