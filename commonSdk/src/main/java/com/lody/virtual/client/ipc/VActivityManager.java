/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.SharedPreferences
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.RemoteException
 *  android.text.TextUtils
 */
package com.lody.virtual.client.ipc;

import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.text.TextUtils;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.secondary.ServiceConnectionProxy;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.IntentBuilder;
import com.lody.virtual.client.stub.WindowPreviewActivity;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.IBinderProxyService;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IActivityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mirror.android.app.Activity;
import mirror.android.app.ActivityThread;
import mirror.android.content.ContentProviderNative;

public class VActivityManager {
    private static final VActivityManager sAM;
    private IActivityManager mService;
    private static final Map<ServiceConnection, ServiceConnectionDelegate> sServiceConnectionDelegates;
    private static final String FILE_NAME;
    private static SharedPreferences mSharedPreferences;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public IActivityManager getService() {
        if (IInterfaceUtils.isAlive(this.mService)) return this.mService;
        Class<VActivityManager> clazz = VActivityManager.class;
        synchronized (VActivityManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = LocalProxyUtils.genProxy(IActivityManager.class, remote);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return this.mService;
        }
    }

    private Object getRemoteInterface() {
        return IActivityManager.Stub.asInterface(ServiceManagerNative.getService("activity"));
    }

    public static VActivityManager get() {
        return sAM;
    }

    public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) {
        if (info == null && (info = VirtualCore.get().resolveActivityInfo(intent, userId)) == null) {
            return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
        }
        try {
            return this.getService().startActivity(intent, info, resultTo, options, resultWho, requestCode, callingPkg, userId);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) {
        try {
            return this.getService().startActivities(intents, resolvedTypes, token, options, callingPkg, userId);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int startActivityFromHistory(Intent intent) {
        try {
            return this.getService().startActivityFromHistory(intent);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int startActivity(Intent intent, int userId) {
        if (userId < 0) {
            return ActivityManagerCompat.START_NOT_CURRENT_USER_ACTIVITY;
        }
        ActivityInfo info = VirtualCore.get().resolveActivityInfo(intent, userId);
        if (info == null) {
            return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
        }
        return this.startActivity(intent, info, null, null, null, -1, null, userId);
    }

    public void appDoneExecuting(String packageName) {
        try {
            this.getService().appDoneExecuting(packageName, VUserHandle.myUserId());
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void onActivityCreate(IBinder record, IBinder token, int taskId) {
        try {
            this.getService().onActivityCreated(record, token, taskId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResumed(IBinder token) {
        try {
            this.getService().onActivityResumed(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean onActivityDestroy(IBinder token) {
        try {
            return this.getService().onActivityDestroyed(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public AppTaskInfo getTaskInfo(int taskId) {
        try {
            return this.getService().getTaskInfo(taskId);
        }
        catch (RemoteException e) {
            return (AppTaskInfo)VirtualRuntime.crash(e);
        }
    }

    public ComponentName getCallingActivity(IBinder token) {
        try {
            return this.getService().getCallingActivity(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            return (ComponentName)VirtualRuntime.crash(e);
        }
    }

    public String getCallingPackage(IBinder token) {
        try {
            return this.getService().getCallingPackage(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public String getPackageForToken(IBinder token) {
        try {
            return this.getService().getPackageForToken(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public ComponentName getActivityForToken(IBinder token) {
        try {
            return this.getService().getActivityClassForToken(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            return (ComponentName)VirtualRuntime.crash(e);
        }
    }

    public void processRestarted(String packageName, String processName, int userId) {
        try {
            this.getService().processRestarted(packageName, processName, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getAppProcessName(int pid) {
        try {
            return this.getService().getAppProcessName(pid);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public String getInitialPackage(int pid) {
        try {
            return this.getService().getInitialPackage(pid);
        }
        catch (RemoteException e) {
            return (String)VirtualRuntime.crash(e);
        }
    }

    public boolean isAppProcess(String processName) {
        try {
            return this.getService().isAppProcess(processName);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public void killAllApps() {
        try {
            this.getService().killAllApps();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void killApplicationProcess(String procName, int vuid) {
        try {
            this.getService().killApplicationProcess(procName, vuid);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void killAppByPkg(String pkg, int userId) {
        try {
            this.getService().killAppByPkg(pkg, userId);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<String> getProcessPkgList(int pid) {
        try {
            return this.getService().getProcessPkgList(pid);
        }
        catch (RemoteException e) {
            return (List)VirtualRuntime.crash(e);
        }
    }

    public boolean isAppPid(int pid) {
        try {
            return this.getService().isAppPid(pid);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int getUidByPid(int pid) {
        try {
            return this.getService().getUidByPid(pid);
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int getSystemPid() {
        try {
            return this.getService().getSystemPid();
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public int getSystemUid() {
        try {
            return this.getService().getSystemUid();
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public void sendCancelActivityResult(IBinder resultTo, String resultWho, int requestCode) {
        this.sendActivityResult(resultTo, resultWho, requestCode, null, 0);
    }

    public void sendActivityResult(IBinder resultTo, String resultWho, int requestCode, Intent data, int resultCode) {
        android.app.Activity activity = this.findActivityByToken(resultTo);
        if (activity != null) {
            Object mainThread = VirtualCore.mainThread();
            ActivityThread.sendActivityResult.call(mainThread, resultTo, resultWho, requestCode, data, resultCode);
        }
    }

    public IInterface acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
        IBinder binder = this.getService().acquireProviderClient(userId, info);
        if (binder != null) {
            return ContentProviderNative.asInterface.call(binder);
        }
        return null;
    }

    public void addOrUpdateIntentSender(IntentSenderData sender) throws RemoteException {
        this.getService().addOrUpdateIntentSender(sender, VUserHandle.myUserId());
    }

    public void removeIntentSender(IBinder token) throws RemoteException {
        this.getService().removeIntentSender(token);
    }

    public IntentSenderData getIntentSender(IBinder token) {
        try {
            return this.getService().getIntentSender(token);
        }
        catch (RemoteException e) {
            return (IntentSenderData)VirtualRuntime.crash(e);
        }
    }

    public android.app.Activity findActivityByToken(IBinder token) {
        Object r = ActivityThread.mActivities.get(VirtualCore.mainThread()).get(token);
        if (r != null) {
            return ActivityThread.ActivityClientRecord.activity.get(r);
        }
        return null;
    }

    public void finishActivity(IBinder token) {
        android.app.Activity parent;
        android.app.Activity activity = this.findActivityByToken(token);
        if (activity == null) {
            VLog.e("VActivityManager", "finishActivity fail : activity = null");
            return;
        }
        while ((parent = Activity.mParent.get(activity)) != null) {
            activity = parent;
        }
        int resultCode = Activity.mResultCode.get(activity);
        Intent resultData = Activity.mResultData.get(activity);
        ActivityManagerCompat.finishActivity(token, resultCode, resultData);
        Activity.mFinished.set(activity, true);
    }

    public boolean isAppRunning(String packageName, int userId, boolean foreground) {
        try {
            return this.getService().isAppRunning(packageName, userId, foreground);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int getUid() {
        return VClient.get().getVUid();
    }

    public ClientConfig initProcess(String packageName, String processName, int userId) {
        try {
            return this.getService().initProcess(packageName, processName, userId);
        }
        catch (RemoteException e) {
            return (ClientConfig)VirtualRuntime.crash(e);
        }
    }

    public void sendBroadcast(Intent intent, int userId) {
        Intent newIntent = ComponentUtils.proxyBroadcastIntent(intent, userId);
        if (newIntent != null) {
            VirtualCore.get().getContext().sendBroadcast(newIntent);
        }
    }

    public void notifyBadgerChange(BadgerInfo info) {
        try {
            this.getService().notifyBadgerChange(info);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public void setAppInactive(String packageName, boolean idle, int userId) {
        try {
            this.getService().setAppInactive(packageName, idle, userId);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public boolean isAppInactive(String packageName, int userId) {
        try {
            return this.getService().isAppInactive(packageName, userId);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public boolean launchApp(int userId, String packageName) {
        return this.launchApp(userId, packageName, true);
    }

    int getPersistentValueToInt(String key) {
        StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
        VPersistent persistent = storagePersistenceServices.getVPersistent();
        Map<String, String> buildAllConfig = persistent.buildAllConfig;
        String value = buildAllConfig.get(key);
        if (!TextUtils.isEmpty((CharSequence)value)) {
            try {
                return Integer.parseInt(value);
            }
            catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getInt(Context context, String keyname, int def) {
        SharedPreferences shared = VActivityManager.getSharedPreferences(context);
        int v = shared.getInt(keyname, def);
        if (v == def) {
            return def;
        }
        return v;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
        }
        return mSharedPreferences;
    }

    public boolean launchApp(final int userId, String packageName, boolean preview) {
        /*
        int channelLimit = this.getPersistentValueToInt("channelLimit");
        int channelStatus = this.getPersistentValueToInt("channelStatus");
        int channelLimitLocal = VActivityManager.getInt(VirtualCore.get().getContext(), "channelLimit", 0);
        if (channelLimit <= channelLimitLocal) {
            return false;
        }
        if (channelStatus == 0) {
            return false;
        }
         */
        if (VirtualCore.get().isRunInExtProcess(packageName) && !VExtPackageAccessor.hasExtPackageBootPermission()) {
            return false;
        }
        Context context = VirtualCore.get().getContext();
        VPackageManager pm = VPackageManager.get();
        Intent intentToResolve = new Intent("android.intent.action.MAIN");
        intentToResolve.addCategory("android.intent.category.INFO");
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
        if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory("android.intent.category.INFO");
            intentToResolve.addCategory("android.intent.category.LAUNCHER");
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
        }
        if (ris == null || ris.size() <= 0) {
            return false;
        }
        ActivityInfo info = ris.get((int)0).activityInfo;
        final Intent intent = new Intent(intentToResolve);
        intent.setFlags(0x10000000);
        intent.setClassName(info.packageName, info.name);
        if (!preview || VActivityManager.get().isAppRunning(info.packageName, userId, true)) {
            VActivityManager.get().startActivity(intent, userId);
        } else {
            intent.addFlags(65536);
            WindowPreviewActivity.previewActivity(userId, info);
            VirtualRuntime.getUIHandler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    VActivityManager.get().startActivity(intent, userId);
                }
            }, 400L);
        }
        return true;
    }

    public void onFinishActivity(IBinder token) {
        try {
            this.getService().onActivityFinish(VUserHandle.myUserId(), token);
        }
        catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }

    public int checkPermission(String permission2, int pid, int uid) {
        try {
            return this.getService().checkPermission(VirtualCore.get().isExtPackage(), permission2, pid, uid, VClient.get().getCurrentPackage());
        }
        catch (RemoteException e) {
            return (Integer)VirtualRuntime.crash(e);
        }
    }

    public void handleDownloadCompleteIntent(Intent intent) {
        try {
            this.getService().handleDownloadCompleteIntent(intent);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean finishActivityAffinity(int userId, IBinder token) {
        try {
            return this.getService().finishActivityAffinity(userId, token);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public ComponentName startService(Context context, Intent service, int userId) {
        if (VirtualCore.get().isServerProcess()) {
            service.putExtra("_VA_|_user_id_", userId);
            return context.startService(service);
        }
        ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
        if (serviceInfo != null) {
            ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
            Intent intent = IntentBuilder.createStartProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, userId);
            return context.startService(intent);
        }
        return null;
    }

    public ServiceConnectionDelegate getDelegate(ServiceConnection conn) {
        ServiceConnectionDelegate delegate = sServiceConnectionDelegates.get(conn);
        if (delegate == null) {
            delegate = new ServiceConnectionDelegate(conn);
            sServiceConnectionDelegates.put(conn, delegate);
        }
        return delegate;
    }

    public ServiceConnection removeDelegate(ServiceConnection conn) {
        Iterator<ServiceConnectionDelegate> it = sServiceConnectionDelegates.values().iterator();
        while (it.hasNext()) {
            ServiceConnectionDelegate delegate = it.next();
            if (conn != delegate) continue;
            it.remove();
            return delegate;
        }
        return conn;
    }

    public boolean bindService(Context context, Intent service, ServiceConnection connection, int flags, int userId) {
        if (VirtualCore.get().isServerProcess()) {
            service.putExtra("_VA_|_user_id_", userId);
            return context.bindService(service, connection, flags);
        }
        connection = this.getDelegate(connection);
        ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
        if (serviceInfo != null) {
            ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
            IServiceConnection conn = ServiceConnectionProxy.getDispatcher(context, connection, flags);
            Intent intent = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, flags, userId, conn);
            return context.bindService(intent, connection, flags);
        }
        return false;
    }

    public void unbindService(Context context, ServiceConnection connection) {
        connection = this.removeDelegate(connection);
        context.unbindService(connection);
    }

    public VParceledListSlice getServices(String pkg, int maxNum, int flags) {
        try {
            return this.getService().getServices(pkg, maxNum, flags, VUserHandle.myUserId());
        }
        catch (RemoteException e) {
            return (VParceledListSlice)VirtualRuntime.crash(e);
        }
    }

    public boolean broadcastFinish(IBinder token) {
        try {
            return this.getService().broadcastFinish(token);
        }
        catch (RemoteException e) {
            return (Boolean)VirtualRuntime.crash(e);
        }
    }

    public int getAppPid(String pkg, int userId, String proccessName) {
        try {
            return this.getService().getAppPid(pkg, userId, proccessName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public final void setSettingsProvider(int tableIndex, String arg, String value) {
        try {
            this.getService().setSettingsProvider(VUserHandle.myUserId(), tableIndex, arg, value);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public final String getSettingsProvider(int tableIndex, String arg) {
        try {
            return this.getService().getSettingsProvider(VUserHandle.myUserId(), tableIndex, arg);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    static {
        FILE_NAME = "sharedPreferences";
        sAM = new VActivityManager();
        sServiceConnectionDelegates = new HashMap<ServiceConnection, ServiceConnectionDelegate>();
    }

    private static class ServiceConnectionDelegate
    implements ServiceConnection {
        private ServiceConnection mBase;

        public ServiceConnectionDelegate(ServiceConnection base) {
            this.mBase = base;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            IBinderProxyService proxy = IBinderProxyService.Stub.asInterface(service);
            if (proxy != null) {
                try {
                    this.mBase.onServiceConnected(proxy.getComponent(), proxy.getService());
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                this.mBase.onServiceConnected(name, service);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            this.mBase.onServiceDisconnected(name);
        }
    }
}

