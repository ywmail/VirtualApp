/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningServiceInfo
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ProviderInfo
 *  android.net.Uri
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.ConditionVariable
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.Parcelable
 *  android.os.Process
 *  android.os.RemoteException
 *  android.os.SystemClock
 */
package com.lody.virtual.server.am;

import android.app.ActivityManager;
import android.app.IStopUserCallback;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.IVClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.ProviderCall;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.compat.ApplicationThreadCompat;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.compat.PermissionCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.am.ActivityRecord;
import com.lody.virtual.server.am.ActivityStack;
import com.lody.virtual.server.am.ProcessRecord;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IActivityManager;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.VPackageManagerService;
import com.lody.virtual.server.settings.VSettingsProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VActivityManagerService
extends IActivityManager.Stub {
    private static final Singleton<VActivityManagerService> sService = new Singleton<VActivityManagerService>(){

        @Override
        protected VActivityManagerService create() {
            return new VActivityManagerService();
        }
    };
    private static final String TAG = VActivityManagerService.class.getSimpleName();
    private final List<ProcessRecord> mPidsSelfLocked = new ArrayList<ProcessRecord>();
    private final ActivityStack mActivityStack = new ActivityStack(this);
    private final Map<IBinder, IntentSenderData> mIntentSenderMap = new HashMap<IBinder, IntentSenderData>();
    private final Map<String, Boolean> sIdeMap = new HashMap<String, Boolean>();
    private boolean mResult;
    private final Handler mHandler = new Handler();

    private VActivityManagerService() {
        this.mHandler.postDelayed(new Runnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                Map map = VActivityManagerService.this.mIntentSenderMap;
                synchronized (map) {
                    Iterator it = VActivityManagerService.this.mIntentSenderMap.values().iterator();
                    while (it.hasNext()) {
                        IntentSenderData data = (IntentSenderData)it.next();
                        PendingIntent pendingIntent = data.getPendingIntent();
                        if (pendingIntent != null && pendingIntent.getTargetPackage() != null) continue;
                        it.remove();
                    }
                }
                VActivityManagerService.this.mHandler.postDelayed((Runnable)this, 300000L);
            }
        }, 300000L);
    }

    public static VActivityManagerService get() {
        return sService.get();
    }

    @Override
    public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) {
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            try {
                return this.mActivityStack.startActivityLocked(userId, intent, info, resultTo, options, resultWho, requestCode);
            }
            catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int startActivityFromHistory(Intent intent) {
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            return this.mActivityStack.startActivityFromHistoryLocked(intent);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean finishActivityAffinity(int userId, IBinder token) {
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            return this.mActivityStack.finishActivityAffinity(userId, token);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) {
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            ActivityInfo[] infos = new ActivityInfo[intents.length];
            for (int i = 0; i < intents.length; ++i) {
                ActivityInfo ai = VirtualCore.get().resolveActivityInfo(intents[i], userId);
                if (ai == null) {
                    return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
                }
                infos[i] = ai;
            }
            return this.mActivityStack.startActivitiesLocked(userId, intents, infos, token, options);
        }
    }

    @Override
    public int getSystemPid() {
        return Process.myPid();
    }

    @Override
    public int getSystemUid() {
        return Process.myUid();
    }

    @Override
    public int getCurrentUserId() {
        return VUserHandle.myUserId();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onActivityCreated(IBinder record, IBinder token, int taskId) {
        ProcessRecord targetApp;
        int pid = Binder.getCallingPid();
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            targetApp = this.findProcessLocked(pid);
        }
        if (targetApp != null) {
            this.mActivityStack.onActivityCreated(targetApp, token, taskId, (ActivityRecord)record);
        }
    }

    @Override
    public void onActivityResumed(int userId, IBinder token) {
        this.mActivityStack.onActivityResumed(userId, token);
    }

    @Override
    public boolean onActivityDestroyed(int userId, IBinder token) {
        ActivityRecord r = this.mActivityStack.onActivityDestroyed(userId, token);
        return r != null;
    }

    @Override
    public void onActivityFinish(int userId, IBinder token) {
        this.mActivityStack.onActivityFinish(userId, token);
    }

    @Override
    public AppTaskInfo getTaskInfo(int taskId) {
        return this.mActivityStack.getTaskInfo(taskId);
    }

    @Override
    public String getPackageForToken(int userId, IBinder token) {
        return this.mActivityStack.getPackageForToken(userId, token);
    }

    @Override
    public ComponentName getActivityClassForToken(int userId, IBinder token) {
        return this.mActivityStack.getActivityClassForToken(userId, token);
    }

    private void processDied(ProcessRecord record) {
        this.mActivityStack.processDied(record);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public IBinder acquireProviderClient(int userId, ProviderInfo info) {
        ProcessRecord r;
        String processName = info.processName;
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            r = this.startProcessIfNeeded(processName, userId, info.packageName, -1);
        }
        if (r != null) {
            try {
                return r.client.acquireProviderClient(info);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean broadcastFinish(IBinder token) throws RemoteException {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (ProcessRecord r : this.mPidsSelfLocked) {
                if (r.client == null || !r.client.finishReceiver(token)) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addOrUpdateIntentSender(IntentSenderData sender, int userId) {
        if (sender == null || sender.token == null) {
            return;
        }
        Map<IBinder, IntentSenderData> map = this.mIntentSenderMap;
        synchronized (map) {
            IntentSenderData data = this.mIntentSenderMap.get(sender.token);
            if (data == null) {
                this.mIntentSenderMap.put(sender.token, sender);
            } else {
                data.update(sender);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeIntentSender(IBinder token) {
        if (token != null) {
            Map<IBinder, IntentSenderData> map = this.mIntentSenderMap;
            synchronized (map) {
                this.mIntentSenderMap.remove(token);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public IntentSenderData getIntentSender(IBinder token) {
        if (token != null) {
            Map<IBinder, IntentSenderData> map = this.mIntentSenderMap;
            synchronized (map) {
                return this.mIntentSenderMap.get(token);
            }
        }
        return null;
    }

    @Override
    public ComponentName getCallingActivity(int userId, IBinder token) {
        return this.mActivityStack.getCallingActivity(userId, token);
    }

    @Override
    public String getCallingPackage(int userId, IBinder token) {
        return this.mActivityStack.getCallingPackage(userId, token);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public VParceledListSlice<ActivityManager.RunningServiceInfo> getServices(String pkg, int maxNum, int flags, int userId) {
        List<ActivityManager.RunningServiceInfo> infoList = new ArrayList();
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (ProcessRecord r : this.mPidsSelfLocked) {
                if (!r.pkgList.contains(pkg) || !r.client.asBinder().isBinderAlive()) continue;
                try {
                    infoList.addAll(r.client.getServices());
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        if (infoList.size() > maxNum) {
            infoList = infoList.subList(0, maxNum);
        }
        return new VParceledListSlice<ActivityManager.RunningServiceInfo>(infoList);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void processRestarted(String packageName, String processName, int userId) {
        ProcessRecord app;
        int callingPid = VBinder.getCallingPid();
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            app = this.findProcessLocked(callingPid);
        }
        if (app == null) {
            String stubProcessName = this.getProcessName(callingPid);
            if (stubProcessName == null) {
                return;
            }
            int vpid = this.parseVPid(stubProcessName);
            if (vpid != -1) {
                this.startProcessIfNeeded(processName, userId, packageName, vpid);
            }
        }
    }

    private int parseVPid(String stubProcessName) {
        String prefix;
        if (stubProcessName == null) {
            return -1;
        }
        if (stubProcessName.startsWith(StubManifest.EXT_PACKAGE_NAME)) {
            prefix = StubManifest.EXT_PACKAGE_NAME + ":p";
        } else if (stubProcessName.startsWith(StubManifest.PACKAGE_NAME)) {
            prefix = VirtualCore.get().getHostPkg() + ":p";
        } else {
            return -1;
        }
        if (stubProcessName.startsWith(prefix)) {
            try {
                return Integer.parseInt(stubProcessName.substring(prefix.length()));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return -1;
    }

    private String getProcessName(int pid) {
        for (ActivityManager.RunningAppProcessInfo info : VirtualCore.get().getRunningAppProcessesEx()) {
            if (info.pid != pid) continue;
            return info.processName;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void onProcessDied(ProcessRecord record) {
        if (record != null) {
            List<ProcessRecord> list = this.mPidsSelfLocked;
            synchronized (list) {
                this.mPidsSelfLocked.remove((Object)record);
            }
            this.processDied(record);
        }
    }

    @Override
    public int getFreeStubCount() {
        return StubManifest.STUB_COUNT - this.mPidsSelfLocked.size();
    }

    @Override
    public int checkPermission(boolean isExt, String permission2, int pid, int uid, String packageName) {
        if (permission2 == null) {
            return -1;
        }
        if ("android.permission.ACCOUNT_MANAGER".equals(permission2)) {
            return 0;
        }
        if ("android.permission.RECEIVE_BOOT_COMPLETED".equals(permission2)) {
            return 0;
        }
        if ("android.permission.BACKUP".equals(permission2)) {
            return 0;
        }
        if ("android.permission.INTERACT_ACROSS_USERS".equals(permission2) || "android.permission.INTERACT_ACROSS_USERS_FULL".equals(permission2)) {
            return -1;
        }
        if (uid == 0) {
            return 0;
        }
        if ("android.permission.READ_DEVICE_CONFIG".equals(permission2)) {
            return 0;
        }
        if ("android.permission.WRITE_DEVICE_CONFIG".equals(permission2)) {
            return 0;
        }
        if ("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS".equals(permission2)) {
            return 0;
        }
        return VPackageManagerService.get().checkUidPermission(isExt, permission2, uid);
    }

    @Override
    public ClientConfig initProcess(String packageName, String processName, int userId) {
        ProcessRecord r = this.startProcessIfNeeded(processName, userId, packageName, -1);
        if (r != null) {
            return r.getClientConfig();
        }
        return null;
    }

    @Override
    public void appDoneExecuting(String packageName, int userId) {
        int pid = VBinder.getCallingPid();
        ProcessRecord r = this.findProcessLocked(pid);
        if (r != null) {
            r.pkgList.add(packageName);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ProcessRecord startProcessIfNeeded(String processName, int userId, String packageName, int vpid) {
        this.runProcessGC();
        PackageSetting ps = PackageCacheManager.getSetting(packageName);
        boolean isExt = ps.isRunInExtProcess();
        if (isExt && !VirtualCore.get().isExtPackageInstalled()) {
            VLog.e(TAG, "startProcessIfNeeded failed due to ext package not install...packageName:" + packageName);
            return null;
        }
        ApplicationInfo info = VPackageManagerService.get().getApplicationInfo(packageName, 0, userId);
        if (ps == null || info == null) {
            VLog.e(TAG, "startProcessIfNeeded failed due to app not install...packageName:" + packageName);
            return null;
        }
        if (!ps.isLaunched(userId)) {
            ps.setLaunched(userId, true);
            VAppManagerService.get().savePersistenceData();
        }
        int vuid = VUserHandle.getUid(userId, ps.appId);
        VActivityManagerService vActivityManagerService = this;
        synchronized (vActivityManagerService) {
            ProcessRecord app;
            if (vpid != -1) {
                ProcessRecord app2 = new ProcessRecord(info, processName, vuid, vpid, isExt);
                if (this.initProcessLocked(app2)) {
                    List<ProcessRecord> list = this.mPidsSelfLocked;
                    synchronized (list) {
                        this.mPidsSelfLocked.add(app2);
                    }
                    return app2;
                }
                return null;
            }
            List<ProcessRecord> list = this.mPidsSelfLocked;
            synchronized (list) {
                app = this.findProcessLocked(processName, userId);
            }
            if (app != null) {
                return app;
            }
            if (processName.equals("com.google.android.gms.persistent")) {
                Intent intent = new Intent("android.intent.action.GMS_INITIALIZED");
                intent.putExtra("android.intent.extra.user_handle", userId);
                VirtualCore.get().getContext().sendBroadcast(intent);
            }
            HashSet<Integer> blackList = new HashSet<Integer>(3);
            int retryCount = 3;
            while (retryCount-- > 0) {
                vpid = this.queryFreeStubProcess(isExt, blackList);
                if (vpid == -1) {
                    this.killAllApps();
                    VLog.e(TAG, "no free vpid, run GC now...");
                    SystemClock.sleep((long)500L);
                    continue;
                }
                app = new ProcessRecord(info, processName, vuid, vpid, isExt);
                if (this.initProcessLocked(app)) {
                    List<ProcessRecord> list2 = this.mPidsSelfLocked;
                    synchronized (list2) {
                        this.mPidsSelfLocked.add(app);
                    }
                    return app;
                }
                blackList.add(vpid);
            }
            return null;
        }
    }

    private void runProcessGC() {
        if (VActivityManagerService.get().getFreeStubCount() < 10) {
            this.killAllApps();
        }
    }

    private void sendFirstLaunchBroadcast(PackageSetting ps, int userId) {
        Intent intent = new Intent("android.intent.action.PACKAGE_FIRST_LAUNCH", Uri.fromParts((String)"package", (String)ps.packageName, null));
        intent.setPackage(ps.packageName);
        intent.putExtra("android.intent.extra.UID", VUserHandle.getUid(ps.appId, userId));
        intent.putExtra("android.intent.extra.user_handle", userId);
        this.sendBroadcastAsUser(intent, new VUserHandle(userId));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getUidByPid(int pid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            ProcessRecord r = this.findProcessLocked(pid);
            if (r != null) {
                return r.vuid;
            }
        }
        if (pid == Process.myPid()) {
            return 1000;
        }
        return 9000;
    }

    private void startRequestPermissions(boolean isExt, String[] permissions, final ConditionVariable permissionLock) {
        PermissionCompat.startRequestPermissions(VirtualCore.get().getContext(), isExt, permissions, new PermissionCompat.CallBack(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean onResult(int requestCode, String[] permissions, int[] grantResults) {
                try {
                    VActivityManagerService.this.mResult = PermissionCompat.isRequestGranted(grantResults);
                }
                finally {
                    permissionLock.open();
                }
                return VActivityManagerService.this.mResult;
            }
        });
    }

    private boolean initProcessLocked(final ProcessRecord app) {
        this.requestPermissionIfNeed(app);
        Bundle extras = new Bundle();
        extras.putParcelable("_VA_|_client_config_", (Parcelable)app.getClientConfig());
        extras.putInt("_VA_|_core_pid_", Process.myPid());
        Bundle res = ProviderCall.callSafely(app.getProviderAuthority(), "_VA_|_init_process_", null, extras, 0);
        if (res == null) {
            return false;
        }
        app.pid = res.getInt("_VA_|_pid_");
        final IBinder clientBinder = BundleCompat.getBinder(res, "_VA_|_client_");
        IVClient client = IVClient.Stub.asInterface(clientBinder);
        if (client == null) {
            app.kill();
            return false;
        }
        try {
            clientBinder.linkToDeath(new IBinder.DeathRecipient(){

                public void binderDied() {
                    clientBinder.unlinkToDeath((IBinder.DeathRecipient)this, 0);
                    VActivityManagerService.this.onProcessDied(app);
                }
            }, 0);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        app.client = client;
        try {
            app.appThread = ApplicationThreadCompat.asInterface(client.getAppThread());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        VLog.w(TAG, "start new process : " + app.processName + " pid: " + app.pid, new Object[0]);
        return true;
    }

    private void requestPermissionIfNeed(ProcessRecord app) {
        String[] permissions;
        if (PermissionCompat.isCheckPermissionRequired(app.info) && !PermissionCompat.checkPermissions(permissions = VPackageManagerService.get().getDangerousPermissions(app.info.packageName), app.isExt)) {
            ConditionVariable permissionLock = new ConditionVariable();
            this.startRequestPermissions(app.isExt, permissions, permissionLock);
            permissionLock.block();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int queryFreeStubProcess(boolean isExt, Set<Integer> blackList) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (int vpid = 0; vpid < StubManifest.STUB_COUNT; ++vpid) {
                int N = this.mPidsSelfLocked.size();
                boolean skip = false;
                while (N-- > 0) {
                    ProcessRecord r = this.mPidsSelfLocked.get(N);
                    if (blackList.contains(r.vpid)) {
                        skip = true;
                        break;
                    }
                    if (r.vpid != vpid || r.isExt != isExt) continue;
                    skip = true;
                    break;
                }
                if (skip) continue;
                return vpid;
            }
        }
        return -1;
    }

    @Override
    public boolean isAppProcess(String processName) {
        return this.parseVPid(processName) != -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isAppPid(int pid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            return this.findProcessLocked(pid) != null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public String getAppProcessName(int pid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            ProcessRecord r = this.findProcessLocked(pid);
            if (r != null) {
                return r.processName;
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<String> getProcessPkgList(int pid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            ProcessRecord r = this.findProcessLocked(pid);
            if (r != null) {
                return new ArrayList<String>(r.pkgList);
            }
        }
        return Collections.emptyList();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void killAllApps() {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (int i = 0; i < this.mPidsSelfLocked.size(); ++i) {
                ProcessRecord r = this.mPidsSelfLocked.get(i);
                r.kill();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void killAppByPkg(String pkg, int userId) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (ProcessRecord r : this.mPidsSelfLocked) {
                if (userId != -1 && r.userId != userId || !r.pkgList.contains(pkg)) continue;
                r.kill();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isAppRunning(String packageName, int userId, boolean foreground) {
        boolean running = false;
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            int N = this.mPidsSelfLocked.size();
            while (N-- > 0) {
                ProcessRecord r = this.mPidsSelfLocked.get(N);
                if (r.userId != userId || !r.info.packageName.equals(packageName) || foreground && !r.info.processName.equals(packageName)) continue;
                try {
                    running = r.client.isAppRunning();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            return running;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void killApplicationProcess(String processName, int vuid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            for (ProcessRecord r : this.mPidsSelfLocked) {
                if (r.vuid != vuid) continue;
                if (r.isExt) {
                    VExtPackageAccessor.forceStop(new int[]{r.pid});
                    continue;
                }
                r.kill();
            }
        }
    }

    @Override
    public void dump() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public String getInitialPackage(int pid) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            ProcessRecord r = this.findProcessLocked(pid);
            if (r != null) {
                return r.info.packageName;
            }
            return null;
        }
    }

    public ProcessRecord findProcessLocked(int pid) {
        for (ProcessRecord r : this.mPidsSelfLocked) {
            if (r.pid != pid) continue;
            return r;
        }
        return null;
    }

    public ProcessRecord findProcessLocked(String processName, int userId) {
        for (ProcessRecord r : this.mPidsSelfLocked) {
            if (!r.processName.equals(processName) || r.userId != userId) continue;
            return r;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int stopUser(int userHandle, IStopUserCallback.Stub stub) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            int N = this.mPidsSelfLocked.size();
            while (N-- > 0) {
                ProcessRecord r = this.mPidsSelfLocked.get(N);
                if (r.userId != userHandle) continue;
                r.kill();
            }
        }
        try {
            stub.userStopped(userHandle);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void sendOrderedBroadcastAsUser(Intent intent, VUserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        Context context = VirtualCore.get().getContext();
        if (user != null) {
            intent.putExtra("_VA_|_user_id_", user.getIdentifier());
        }
        context.sendOrderedBroadcast(intent, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void sendBroadcastAsUser(Intent intent, VUserHandle user) {
        SpecialComponentList.protectIntent(intent);
        Context context = VirtualCore.get().getContext();
        if (user != null) {
            intent.putExtra("_VA_|_user_id_", user.getIdentifier());
        }
        context.sendBroadcast(intent);
    }

    public void sendBroadcastAsUser(Intent intent, VUserHandle user, String permission2) {
        SpecialComponentList.protectIntent(intent);
        Context context = VirtualCore.get().getContext();
        if (user != null) {
            intent.putExtra("_VA_|_user_id_", user.getIdentifier());
        }
        context.sendBroadcast(intent);
    }

    @Override
    public void notifyBadgerChange(BadgerInfo info) {
        Intent intent = new Intent(Constants.ACTION_BADGER_CHANGE);
        intent.putExtra("userId", info.userId);
        intent.putExtra("packageName", info.packageName);
        intent.putExtra("badgerCount", info.badgerCount);
        VirtualCore.get().getContext().sendBroadcast(intent);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setAppInactive(String packageName, boolean idle, int userId) {
        Map<String, Boolean> map = this.sIdeMap;
        synchronized (map) {
            this.sIdeMap.put(packageName + "@" + userId, idle);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isAppInactive(String packageName, int userId) {
        Map<String, Boolean> map = this.sIdeMap;
        synchronized (map) {
            Boolean idle = this.sIdeMap.get(packageName + "@" + userId);
            return idle != null && idle == false;
        }
    }

    @Override
    public void handleDownloadCompleteIntent(Intent intent) {
        intent.setPackage(null);
        intent.setComponent(null);
        Intent send = ComponentUtils.proxyBroadcastIntent(intent, -1);
        VirtualCore.get().getContext().sendBroadcast(send);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getAppPid(String packageName, int userId, String proccessName) {
        List<ProcessRecord> list = this.mPidsSelfLocked;
        synchronized (list) {
            int N = this.mPidsSelfLocked.size();
            while (N-- > 0) {
                ProcessRecord r = this.mPidsSelfLocked.get(N);
                if (r.userId != userId || !r.info.packageName.equals(packageName)) continue;
                try {
                    if (r.client.isAppRunning() && r.info.processName.equals(proccessName)) {
                        return r.pid;
                    }
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            return -1;
        }
    }

    @Override
    public final void setSettingsProvider(int userId, int tableIndex, String arg, String value) {
        VSettingsProvider.getInstance().setSettingsProvider(userId, tableIndex, arg, value);
    }

    @Override
    public final String getSettingsProvider(int userId, int tableIndex, String arg) {
        return VSettingsProvider.getInstance().getSettingsProvider(userId, tableIndex, arg);
    }
}

