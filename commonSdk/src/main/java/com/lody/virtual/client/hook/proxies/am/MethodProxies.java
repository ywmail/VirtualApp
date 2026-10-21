/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.ActivityManager$RecentTaskInfo
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningTaskInfo
 *  android.app.ActivityManager$TaskDescription
 *  android.app.AlarmManager
 *  android.app.Application
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.Intent$ShortcutIconResource
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.IInterface
 *  android.os.Parcelable
 *  android.text.TextUtils
 */
package com.lody.virtual.client.hook.proxies.am;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.IServiceConnection;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.badger.BadgerManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.client.hook.providers.ProviderHook;
import com.lody.virtual.client.hook.secondary.ServiceConnectionProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.ChooserActivity;
import com.lody.virtual.client.stub.IntentBuilder;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import com.lody.virtual.helper.compat.PendingIntentCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.DrawableUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import mirror.android.app.IActivityManager;
import mirror.android.app.LoadedApk;
import mirror.android.content.ContentProviderHolderOreo;
import mirror.android.content.IIntentReceiverJB;
import mirror.android.content.pm.ParceledListSlice;
import mirror.android.content.pm.UserInfo;

public class MethodProxies {

    public static class SetPictureInPictureParams
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "SetPictureInPictureParams";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return null;
        }
    }

    public static class OverridePendingTransition
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "overridePendingTransition";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (!VClient.get().isDynamicApp()) {
                return 0;
            }
            return super.call(who, method, args);
        }
    }

    static class GetPackageProcessState
    extends MethodProxy {
        GetPackageProcessState() {
        }

        @Override
        public String getMethodName() {
            return "getPackageProcessState";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return 4;
        }

        @Override
        public boolean isEnable() {
            return GetPackageProcessState.isAppProcess();
        }
    }

    static class isUserRunning
    extends MethodProxy {
        isUserRunning() {
        }

        @Override
        public String getMethodName() {
            return "isUserRunning";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) {
            int userId = (Integer)args[0];
            for (VUserInfo userInfo : VUserManager.get().getUsers()) {
                if (userInfo.id != userId) continue;
                return true;
            }
            return false;
        }

        @Override
        public boolean isEnable() {
            return isUserRunning.isAppProcess();
        }
    }

    static class CheckGrantUriPermission
    extends MethodProxy {
        CheckGrantUriPermission() {
        }

        @Override
        public String getMethodName() {
            return "checkGrantUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return CheckGrantUriPermission.isAppProcess();
        }
    }

    static class GrantUriPermission
    extends MethodProxy {
        GrantUriPermission() {
        }

        @Override
        public String getMethodName() {
            return "grantUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            GrantUriPermission.replaceLastUserId(args);
            if (args[2] != null && args[2] instanceof Uri) {
                args[2] = ComponentUtils.processOutsideUri(GrantUriPermission.getAppUserId(), VirtualCore.get().isExtPackage(), (Uri)args[2]);
            }
            try {
                method.invoke(who, args);
            }
            catch (Exception exp) {
                if (exp.getCause() != null && exp.getCause() instanceof SecurityException) {
                    for (StackTraceElement element : exp.getStackTrace()) {
                        if (!TextUtils.equals((CharSequence)element.getClassName(), (CharSequence)Intent.class.getName()) || !TextUtils.equals((CharSequence)element.getMethodName(), (CharSequence)"migrateExtraStreamToClipData")) continue;
                        return null;
                    }
                }
                throw exp;
            }
            return null;
        }

        @Override
        public boolean isEnable() {
            return GrantUriPermission.isAppProcess();
        }
    }

    static class GetActivityClassForToken
    extends MethodProxy {
        GetActivityClassForToken() {
        }

        @Override
        public String getMethodName() {
            return "getActivityClassForToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            ComponentName comp = VActivityManager.get().getActivityForToken(token);
            if (comp == null) {
                return method.invoke(who, args);
            }
            return comp;
        }

        @Override
        public boolean isEnable() {
            return GetActivityClassForToken.isAppProcess();
        }
    }

    static class BroadcastIntent
    extends MethodProxy {
        BroadcastIntent() {
        }

        @Override
        public String getMethodName() {
            return "broadcastIntent";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Intent intent = new Intent((Intent)args[1]);
            String type = (String)args[2];
            intent.setDataAndType(intent.getData(), type);
            Intent newIntent = this.handleIntent(intent);
            if (newIntent == null) {
                return 0;
            }
            args[1] = newIntent;
            if (args[7] instanceof String || args[7] instanceof String[]) {
                args[7] = null;
            }
            int index = ArrayUtils.indexOfFirst(args, Boolean.class);
            args[index] = false;
            BroadcastIntent.replaceLastUserId(args);
            try {
                return method.invoke(who, args);
            }
            catch (Throwable e) {
                return 0;
            }
        }

        protected Intent handleIntent(Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.CREATE_SHORTCUT".equals(action) || "com.android.launcher.action.INSTALL_SHORTCUT".equals(action) || "com.aliyun.homeshell.action.INSTALL_SHORTCUT".equals(action)) {
                return BroadcastIntent.getConfig().isAllowCreateShortcut() ? this.handleInstallShortcutIntent(intent) : null;
            }
            if (!"com.android.launcher.action.UNINSTALL_SHORTCUT".equals(action) && !"com.aliyun.homeshell.action.UNINSTALL_SHORTCUT".equals(action)) {
                if ("android.intent.action.MEDIA_SCANNER_SCAN_FILE".equals(action)) {
                    return this.handleMediaScannerIntent(intent);
                }
                if (BadgerManager.handleBadger(intent)) {
                    return null;
                }
                if (intent.getComponent() != null) {
                    try {
                        final ActivityInfo receiverInfo = VirtualCore.get().getPackageManager().getReceiverInfo(intent.getComponent(), 0);
                        if (receiverInfo != null && VirtualCore.get().getProccessInfo(receiverInfo.processName, VClient.get().getVUid()) == null) {
                            VirtualCore.get().getHandlerASyc().post(new Runnable(){

                                @Override
                                public void run() {
                                    try {
                                        ProviderInfo providerInfo = new ProviderInfo();
                                        providerInfo.applicationInfo = receiverInfo.applicationInfo;
                                        providerInfo.packageName = receiverInfo.packageName;
                                        providerInfo.processName = receiverInfo.processName;
                                        providerInfo.authority = "_VA_START_PROCESS";
                                        VActivityManager.get().acquireProviderClient(VUserHandle.myUserId(), providerInfo);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                return ComponentUtils.proxyBroadcastIntent(intent, VUserHandle.myUserId());
            }
            this.handleUninstallShortcutIntent(intent);
            return intent;
        }

        private Intent handleMediaScannerIntent(Intent intent) {
            if (intent == null) {
                return null;
            }
            Uri data = intent.getData();
            if (data == null) {
                return intent;
            }
            String scheme = data.getScheme();
            if (!"file".equalsIgnoreCase(scheme)) {
                return intent;
            }
            String path = data.getPath();
            if (path == null) {
                return intent;
            }
            String newPath = NativeEngine.getRedirectedPath(path);
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                return intent;
            }
            intent.setData(Uri.fromFile((File)newFile));
            return intent;
        }

        private Intent handleInstallShortcutIntent(Intent intent) {
            ComponentName component;
            Intent shortcut = (Intent)intent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
            if (shortcut != null && (component = shortcut.resolveActivity(VirtualCore.getPM())) != null) {
                String pkg = component.getPackageName();
                Intent newShortcutIntent = new Intent();
                newShortcutIntent.addCategory("android.intent.category.DEFAULT");
                newShortcutIntent.setAction(Constants.ACTION_SHORTCUT);
                newShortcutIntent.setPackage(BroadcastIntent.getHostPkg());
                newShortcutIntent.putExtra("_VA_|_intent_", (Parcelable)shortcut);
                newShortcutIntent.putExtra("_VA_|_uri_", shortcut.toUri(0));
                newShortcutIntent.putExtra("_VA_|_user_id_", VUserHandle.myUserId());
                intent.removeExtra("android.intent.extra.shortcut.INTENT");
                intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)newShortcutIntent);
                Intent.ShortcutIconResource icon = (Intent.ShortcutIconResource)intent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
                if (icon != null && !TextUtils.equals((CharSequence)icon.packageName, (CharSequence)BroadcastIntent.getHostPkg())) {
                    try {
                        Drawable iconDrawable;
                        Bitmap newIcon;
                        Resources resources = VirtualCore.get().getResources(pkg);
                        int resId = resources.getIdentifier(icon.resourceName, "drawable", pkg);
                        if (resId > 0 && (newIcon = BitmapUtils.drawableToBitmap(iconDrawable = resources.getDrawable(resId))) != null) {
                            intent.removeExtra("android.intent.extra.shortcut.ICON_RESOURCE");
                            intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)newIcon);
                        }
                    }
                    catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            return intent;
        }

        private void handleUninstallShortcutIntent(Intent intent) {
            ComponentName componentName;
            Intent shortcut = (Intent)intent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
            if (shortcut != null && (componentName = shortcut.resolveActivity(this.getPM())) != null) {
                Intent newShortcutIntent = new Intent();
                newShortcutIntent.putExtra("_VA_|_uri_", shortcut.toUri(0));
                newShortcutIntent.setClassName(BroadcastIntent.getHostPkg(), Constants.SHORTCUT_PROXY_ACTIVITY_NAME);
                newShortcutIntent.removeExtra("android.intent.extra.shortcut.INTENT");
                intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)newShortcutIntent);
            }
        }

        @Override
        public boolean isEnable() {
            return BroadcastIntent.isAppProcess();
        }
    }

    static class BroadcastIntentWithFeature
    extends BroadcastIntent {
        BroadcastIntentWithFeature() {
        }

        @Override
        public String getMethodName() {
            return "broadcastIntentWithFeature";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Intent intent = new Intent((Intent)args[2]);
            String type = (String)args[3];
            intent.setDataAndType(intent.getData(), type);
            Intent newIntent = this.handleIntent(intent);
            if (newIntent != null) {
                args[2] = newIntent;
                if (args[8] instanceof String[]) {
                    args[8] = null;
                }
                int index = ArrayUtils.indexOfFirst(args, Boolean.class);
                args[index] = false;
                BroadcastIntentWithFeature.replaceLastUserId(args);
                try {
                    return method.invoke(who, args);
                }
                catch (Throwable e) {
                    VLog.e("VA", e);
                    return 0;
                }
            }
            return 0;
        }
    }

    static class StartNextMatchingActivity
    extends StartActivity {
        StartNextMatchingActivity() {
        }

        @Override
        public String getMethodName() {
            return "startNextMatchingActivity";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return false;
        }
    }

    public static class StartActivityWithConfig
    extends StartActivity {
        @Override
        public String getMethodName() {
            return "startActivityWithConfig";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return super.call(who, method, args);
        }
    }

    static class StopServiceToken
    extends MethodProxy {
        StopServiceToken() {
        }

        @Override
        public String getMethodName() {
            return "stopServiceToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            ComponentName componentName = (ComponentName)args[0];
            IBinder token = (IBinder)args[1];
            int startId = (Integer)args[2];
            int userId = VUserHandle.myUserId();
            ServiceInfo serviceInfo = VPackageManager.get().getServiceInfo(componentName, 0, userId);
            if (serviceInfo != null) {
                ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
                if (clientConfig == null) {
                    VLog.e("ActivityManager", "failed to initProcess for stopServiceToken");
                    return false;
                }
                Intent intent = IntentBuilder.createStopProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, componentName, userId, startId, token);
                StopServiceToken.getHostContext().startService(intent);
                return true;
            }
            if (StopServiceToken.isOutsidePackage(componentName.getPackageName())) {
                return method.invoke(who, args);
            }
            return false;
        }

        @Override
        public boolean isEnable() {
            return StopServiceToken.isAppProcess();
        }
    }

    @TargetApi(value=21)
    public static class SetTaskDescription
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "setTaskDescription";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            TaskDescriptionDelegate descriptionDelegate;
            Application app;
            ActivityManager.TaskDescription td = (ActivityManager.TaskDescription)args[1];
            String label = td.getLabel();
            Bitmap icon = td.getIcon();
            if ((label == null || icon == null) && (app = VClient.get().getCurrentApplication()) != null) {
                try {
                    Drawable drawable2;
                    if (label == null) {
                        label = app.getApplicationInfo().loadLabel(app.getPackageManager()).toString();
                    }
                    if (icon == null && (drawable2 = app.getApplicationInfo().loadIcon(app.getPackageManager())) != null) {
                        icon = DrawableUtils.drawableToBitMap(drawable2);
                    }
                    td = new ActivityManager.TaskDescription(label, icon, td.getPrimaryColor());
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if ((descriptionDelegate = VirtualCore.get().getTaskDescriptionDelegate()) != null) {
                td = descriptionDelegate.getTaskDescription(td);
            }
            args[1] = td;
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return SetTaskDescription.isAppProcess();
        }
    }

    static class GetContentProvider
    extends MethodProxy {
        GetContentProvider() {
        }

        @Override
        public String getMethodName() {
            return "getContentProvider";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int nameIdx = this.getProviderNameIndex();
            String name = (String)args[nameIdx];
            if (!(name.startsWith(StubManifest.STUB_CP_AUTHORITY) || name.startsWith(StubManifest.EXT_STUB_CP_AUTHORITY) || name.equals(GetContentProvider.getConfig().getExtPackageHelperAuthority()) || name.equals(GetContentProvider.getConfig().getBinderProviderAuthority()))) {
                int pkgIdx;
                VLog.w("VActivityManger", "getContentProvider:%s", name);
                if (BuildCompat.isQ() && args[pkgIdx = nameIdx - 1] instanceof String) {
                    args[pkgIdx] = GetContentProvider.getHostPkg();
                }
                int userId = VUserHandle.myUserId();
                ProviderInfo info = VPackageManager.get().resolveContentProvider(name, 0, userId);
                if (info != null && !info.enabled) {
                    return null;
                }
                if (info != null && this.isAppPkg(info.packageName)) {
                    ClientConfig config = VActivityManager.get().initProcess(info.packageName, info.processName, userId);
                    if (config == null) {
                        VLog.e("ActivityManager", "failed to initProcess for provider: " + name);
                        return null;
                    }
                    args[nameIdx] = StubManifest.getStubAuthority(config.vpid, config.isExt);
                    GetContentProvider.replaceLastUserId(args);
                    Object holder = method.invoke(who, args);
                    if (holder == null) {
                        return null;
                    }
                    boolean maybeLoadingProvider = false;
                    if (BuildCompat.isOreo()) {
                        IInterface provider = ContentProviderHolderOreo.provider.get(holder);
                        if (provider != null) {
                            provider = VActivityManager.get().acquireProviderClient(userId, info);
                            if (BuildCompat.isS() && provider != null) {
                                provider = ProviderHook.createProxy(false, name, provider);
                            }
                        } else {
                            maybeLoadingProvider = true;
                        }
                        if (provider == null) {
                            if (maybeLoadingProvider) {
                                VLog.w("VActivityManager", "Loading provider: " + info.authority + "(" + info.processName + ")", new Object[0]);
                                ContentProviderHolderOreo.info.set(holder, info);
                                return holder;
                            }
                            VLog.e("VActivityManager", "acquireProviderClient fail: " + info.authority + "(" + info.processName + ")");
                            return null;
                        }
                        ContentProviderHolderOreo.provider.set(holder, provider);
                        ContentProviderHolderOreo.info.set(holder, info);
                    } else {
                        IInterface provider2 = IActivityManager.ContentProviderHolder.provider.get(holder);
                        if (provider2 != null) {
                            provider2 = VActivityManager.get().acquireProviderClient(userId, info);
                        } else {
                            maybeLoadingProvider = true;
                        }
                        if (provider2 == null) {
                            if (maybeLoadingProvider) {
                                if (!BuildCompat.isMIUI() || !this.miuiProviderWaitingTargetProcess(holder)) {
                                    return null;
                                }
                                VLog.w("VActivityManager", "miui provider waiting process: " + info.authority + "(" + info.processName + ")", new Object[0]);
                                return null;
                            }
                            VLog.e("VActivityManager", "acquireProviderClient fail: " + info.authority + "(" + info.processName + ")");
                            return null;
                        }
                        IActivityManager.ContentProviderHolder.provider.set(holder, provider2);
                        IActivityManager.ContentProviderHolder.info.set(holder, info);
                    }
                    return holder;
                }
                GetContentProvider.replaceLastUserId(args);
                Object holder2 = method.invoke(who, args);
                if (holder2 != null) {
                    if (BuildCompat.isOreo()) {
                        IInterface provider3 = ContentProviderHolderOreo.provider.get(holder2);
                        ProviderInfo info2 = ContentProviderHolderOreo.info.get(holder2);
                        if (provider3 != null) {
                            provider3 = ProviderHook.createProxy(true, info2.authority, provider3);
                        }
                        ContentProviderHolderOreo.provider.set(holder2, provider3);
                    } else {
                        IInterface provider4 = IActivityManager.ContentProviderHolder.provider.get(holder2);
                        ProviderInfo info3 = IActivityManager.ContentProviderHolder.info.get(holder2);
                        if (provider4 != null) {
                            provider4 = ProviderHook.createProxy(true, info3.authority, provider4);
                        }
                        IActivityManager.ContentProviderHolder.provider.set(holder2, provider4);
                    }
                    return holder2;
                }
                return null;
            }
            GetContentProvider.replaceLastUserId(args);
            return method.invoke(who, args);
        }

        public int getProviderNameIndex() {
            if (BuildCompat.isQ()) {
                return 2;
            }
            return 1;
        }

        @Override
        public boolean isEnable() {
            return GetContentProvider.isAppProcess();
        }

        private boolean miuiProviderWaitingTargetProcess(Object providerHolder) {
            if (providerHolder != null && IActivityManager.ContentProviderHolderMIUI.waitProcessStart != null) {
                return IActivityManager.ContentProviderHolderMIUI.waitProcessStart.get(providerHolder);
            }
            return false;
        }
    }

    static class RegisterReceiver
    extends MethodProxy {
        int IDX_IIntentReceiver = 2;
        int IDX_IntentFilter = 3;
        int IDX_RequiredPermission = 4;
        private WeakHashMap<IBinder, IIntentReceiver> mProxyIIntentReceivers = new WeakHashMap();

        RegisterReceiver() {
        }

        @Override
        public String getMethodName() {
            return "registerReceiver";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            Intent stickyIntent;
            IBinder token;
            IInterface old;
            MethodParameterUtils.replaceFirstAppPkg(args);
            RegisterReceiver.replaceFirstUserId(args);
            args[this.IDX_RequiredPermission] = null;
            IntentFilter filter = (IntentFilter)args[this.IDX_IntentFilter];
            if (filter == null) {
                return method.invoke(who, args);
            }
            if ((filter = new IntentFilter(filter)).hasCategory("__VA__|_static_receiver_")) {
                List<String> categories = mirror.android.content.IntentFilter.mCategories.get(filter);
                categories.remove("__VA__|_static_receiver_");
                return method.invoke(who, args);
            }
            SpecialComponentList.protectIntentFilter(filter);
            args[this.IDX_IntentFilter] = filter;
            if (args.length > this.IDX_IIntentReceiver && args[this.IDX_IIntentReceiver] instanceof IIntentReceiver && !((old = (IInterface)args[this.IDX_IIntentReceiver]) instanceof IIntentReceiverProxy) && (token = old.asBinder()) != null) {
                WeakReference mDispatcher;
                token.linkToDeath(new IBinder.DeathRecipient(){

                    public void binderDied() {
                        token.unlinkToDeath((IBinder.DeathRecipient)this, 0);
                        mProxyIIntentReceivers.remove(token);
                    }
                }, 0);
                IIntentReceiver proxyIIntentReceiver = this.mProxyIIntentReceivers.get(token);
                if (proxyIIntentReceiver == null) {
                    proxyIIntentReceiver = new IIntentReceiverProxy(old, filter);
                    this.mProxyIIntentReceivers.put(token, proxyIIntentReceiver);
                }
                if ((mDispatcher = LoadedApk.ReceiverDispatcher.InnerReceiver.mDispatcher.get(old)) != null) {
                    LoadedApk.ReceiverDispatcher.mIIntentReceiver.set(mDispatcher.get(), proxyIIntentReceiver);
                    args[this.IDX_IIntentReceiver] = proxyIIntentReceiver;
                }
            }
            if ((stickyIntent = (Intent)method.invoke(who, args)) != null) {
                stickyIntent = SpecialComponentList.unprotectIntent(VUserHandle.myUserId(), stickyIntent);
            }
            return stickyIntent;
        }

        @Override
        public boolean isEnable() {
            return RegisterReceiver.isAppProcess();
        }

        private static class IIntentReceiverProxy
        extends IIntentReceiver.Stub {
            IInterface mOld;
            IntentFilter mFilter;

            IIntentReceiverProxy(IInterface old, IntentFilter filter) {
                this.mOld = old;
                this.mFilter = filter;
            }

            @Override
            public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                intent = SpecialComponentList.unprotectIntent(VUserHandle.myUserId(), intent);
                IIntentReceiverJB.performReceive.call(this.mOld, intent, resultCode, data, extras, ordered, sticky, sendingUser);
            }

            public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky) {
                this.performReceive(intent, resultCode, data, extras, ordered, sticky, 0);
            }
        }
    }

    static class RegisterReceiverWithFeature
    extends RegisterReceiver {
        public RegisterReceiverWithFeature() {
            if (BuildCompat.isS()) {
                this.IDX_IIntentReceiver = 4;
                this.IDX_IntentFilter = 5;
                this.IDX_RequiredPermission = 6;
            } else {
                this.IDX_IIntentReceiver = 3;
                this.IDX_IntentFilter = 4;
                this.IDX_RequiredPermission = 5;
            }
        }

        @Override
        public String getMethodName() {
            return "registerReceiverWithFeature";
        }
    }

    static class GetPersistedUriPermissions
    extends MethodProxy {
        GetPersistedUriPermissions() {
        }

        @Override
        public String getMethodName() {
            return "getPersistedUriPermissions";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return GetPersistedUriPermissions.isAppProcess();
        }
    }

    public static class GetTasks
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "getTasks";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = (List)method.invoke(who, args);
            Iterator var5 = runningTaskInfos.iterator();

            while(var5.hasNext()) {
                ActivityManager.RunningTaskInfo info = (ActivityManager.RunningTaskInfo)var5.next();
                AppTaskInfo taskInfo = VActivityManager.get().getTaskInfo(info.id);
                if (taskInfo != null) {
                    info.topActivity = taskInfo.topActivity;
                    info.baseActivity = taskInfo.baseActivity;
                }
            }

            return runningTaskInfos;
        }

        @Override
        public boolean isEnable() {
            return GetTasks.isAppProcess();
        }
    }

    static class HandleIncomingUser
    extends MethodProxy {
        HandleIncomingUser() {
        }

        @Override
        public String getMethodName() {
            return "handleIncomingUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int lastIndex = args.length - 1;
            if (args[lastIndex] instanceof String) {
                args[lastIndex] = HandleIncomingUser.getHostPkg();
            }
            HandleIncomingUser.replaceLastUserId(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return HandleIncomingUser.isAppProcess();
        }
    }

    static class StartActivityAsCaller
    extends StartActivity {
        StartActivityAsCaller() {
        }

        @Override
        public String getMethodName() {
            return "startActivityAsCaller";
        }
    }

    static class CheckPermissionWithToken
    extends MethodProxy {
        CheckPermissionWithToken() {
        }

        @Override
        public String getMethodName() {
            return "checkPermissionWithToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String permission2 = (String)args[0];
            int pid = (Integer)args[1];
            int uid = (Integer)args[2];
            return VActivityManager.get().checkPermission(permission2, pid, uid);
        }

        @Override
        public boolean isEnable() {
            return CheckPermissionWithToken.isAppProcess();
        }
    }

    static class CheckPermission
    extends MethodProxy {
        CheckPermission() {
        }

        @Override
        public String getMethodName() {
            return "checkPermission";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String permission2 = (String)args[0];
            int pid = (Integer)args[1];
            int uid = (Integer)args[2];
            return VActivityManager.get().checkPermission(permission2, pid, uid);
        }

        @Override
        public boolean isEnable() {
            return CheckPermission.isAppProcess();
        }
    }

    public static class StartActivityAsUser
    extends StartActivity {
        @Override
        public String getMethodName() {
            return "startActivityAsUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            StartActivityAsUser.replaceLastUserId(args);
            return super.call(who, method, args);
        }
    }

    static class KillBackgroundProcesses
    extends MethodProxy {
        KillBackgroundProcesses() {
        }

        @Override
        public String getMethodName() {
            return "killBackgroundProcesses";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (args[0] instanceof String) {
                String pkg = (String)args[0];
                VActivityManager.get().killAppByPkg(pkg, KillBackgroundProcesses.getAppUserId());
                return 0;
            }
            KillBackgroundProcesses.replaceLastUserId(args);
            return super.call(who, method, args);
        }
    }

    static class KillApplicationProcess
    extends MethodProxy {
        KillApplicationProcess() {
        }

        @Override
        public String getMethodName() {
            return "killApplicationProcess";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return KillApplicationProcess.isAppProcess();
        }
    }

    static class GetCurrentUser
    extends MethodProxy {
        GetCurrentUser() {
        }

        @Override
        public String getMethodName() {
            return "getCurrentUser";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            try {
                return UserInfo.ctor.newInstance(0, "user", 1);
            }
            catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    static class GetCurrentUserId
    extends MethodProxy {
        GetCurrentUserId() {
        }

        @Override
        public String getMethodName() {
            return "GetCurrentUserId";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return 0;
        }
    }

    public static class GetCallingActivity
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "getCallingActivity";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            return VActivityManager.get().getCallingActivity(token);
        }

        @Override
        public boolean isEnable() {
            return GetCallingActivity.isAppProcess();
        }
    }

    static class SetPackageAskScreenCompat
    extends MethodProxy {
        SetPackageAskScreenCompat() {
        }

        @Override
        public String getMethodName() {
            return "setPackageAskScreenCompat";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (Build.VERSION.SDK_INT >= 15 && args.length > 0 && args[0] instanceof String) {
                args[0] = SetPackageAskScreenCompat.getHostPkg();
            }
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return SetPackageAskScreenCompat.isAppProcess();
        }
    }

    static class GetRunningAppProcesses
    extends MethodProxy {
        GetRunningAppProcesses() {
        }

        @Override
        public String getMethodName() {
            return "getRunningAppProcesses";
        }

        @Override
        public synchronized Object call(Object who, Method method, Object ... args) throws Throwable {
            if (VClient.get().getClientConfig() == null) {
                return method.invoke(who, args);
            }
            List _infoList = (List)method.invoke(who, args);
            if (_infoList == null) {
                return null;
            }
            ArrayList infoList = new ArrayList(_infoList);
            Iterator it = infoList.iterator();
            while (it.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)it.next();
                if (info.uid != GetRunningAppProcesses.getRealUid()) continue;
                if (VActivityManager.get().isAppPid(info.pid)) {
                    int vuid = VActivityManager.get().getUidByPid(info.pid);
                    int userId = VUserHandle.getUserId(vuid);
                    if (userId != GetRunningAppProcesses.getAppUserId()) {
                        it.remove();
                        continue;
                    }
                    List<String> pkgList = VActivityManager.get().getProcessPkgList(info.pid);
                    String processName = VActivityManager.get().getAppProcessName(info.pid);
                    if (processName != null) {
                        info.importanceReasonCode = 0;
                        info.importanceReasonPid = 0;
                        info.importanceReasonComponent = null;
                        info.processName = processName;
                    }
                    info.pkgList = pkgList.toArray(new String[0]);
                    info.uid = vuid;
                    continue;
                }
                if (!info.processName.startsWith(GetRunningAppProcesses.getConfig().getMainPackageName()) && !info.processName.startsWith(GetRunningAppProcesses.getConfig().getExtPackageName())) continue;
                it.remove();
            }
            return infoList;
        }

        @Override
        public boolean isEnable() {
            return GetRunningAppProcesses.isAppProcess();
        }
    }

    public static class StartActivityAndWait
    extends StartActivity {
        @Override
        public String getMethodName() {
            return "startActivityAndWait";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return super.call(who, method, args);
        }
    }

    static class UnbindService
    extends MethodProxy {
        UnbindService() {
        }

        @Override
        public String getMethodName() {
            return "unbindService";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IServiceConnection conn = (IServiceConnection)args[0];
            ServiceConnectionProxy proxy = ServiceConnectionProxy.removeProxy(conn);
            if (proxy != null) {
                args[0] = proxy;
            }
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return UnbindService.isAppProcess() || UnbindService.isServerProcess();
        }
    }

    static class StopService
    extends MethodProxy {
        StopService() {
        }

        @Override
        public String getMethodName() {
            return "stopService";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int userId;
            IInterface caller = (IInterface)args[0];
            Intent service = new Intent((Intent)args[1]);
            String resolvedType = (String)args[2];
            service.setDataAndType(service.getData(), resolvedType);
            ComponentName component = service.getComponent();
            if (component != null && this.isHostPkg(component.getPackageName())) {
                return method.invoke(who, args);
            }
            int n = userId = StopService.isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
                throw new IllegalArgumentException();
            }
            ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
            if (serviceInfo != null && this.isAppPkg(serviceInfo.packageName)) {
                ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
                if (clientConfig == null) {
                    VLog.e("ActivityManager", "failed to initProcess for stopService: " + component);
                    return 0;
                }
                if (component == null) {
                    component = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                }
                Intent intent = IntentBuilder.createStopProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, component, userId, -1, null);
                StopService.getHostContext().startService(intent);
                return 1;
            }
            if (component != null && StopService.isOutsidePackage(component.getPackageName())) {
                StopService.replaceLastUserId(args);
                return method.invoke(who, args);
            }
            return 0;
        }

        @Override
        public boolean isEnable() {
            return StopService.isAppProcess() || StopService.isServerProcess();
        }
    }

    static class BindServiceInstance
    extends BindIsolatedService {
        BindServiceInstance() {
        }

        @Override
        public String getMethodName() {
            return "bindServiceInstance";
        }
    }

    static class PeekService
    extends MethodProxy {
        PeekService() {
        }

        @Override
        public String getMethodName() {
            return "peekService";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int userId;
            Intent service = new Intent((Intent)args[0]);
            String resolvedType = (String)args[1];
            ComponentName component = service.getComponent();
            if (component != null && this.isHostPkg(component.getPackageName())) {
                return method.invoke(who, args);
            }
            int n = userId = PeekService.isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
                throw new IllegalArgumentException();
            }
            service.setDataAndType(service.getData(), resolvedType);
            ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
            if (serviceInfo != null) {
                ClientConfig clientConfig = VClient.get().getClientConfig();
                args[0] = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, 0, userId, null);
                return method.invoke(who, args);
            }
            if (component != null && PeekService.isOutsidePackage(component.getPackageName())) {
                return method.invoke(who, args);
            }
            return null;
        }

        @Override
        public boolean isEnable() {
            return PeekService.isAppProcess() || PeekService.isServerProcess();
        }
    }

    static class BindIsolatedService
    extends BindService {
        BindIsolatedService() {
        }

        @Override
        public String getMethodName() {
            return "bindIsolatedService";
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object ... args) {
            BindIsolatedService.replaceLastUserId(args);
            return super.beforeCall(who, method, args);
        }

        @Override
        protected boolean isIsolated() {
            return true;
        }
    }

    static class BindService
    extends MethodProxy {
        BindService() {
        }

        @Override
        public String getMethodName() {
            return "bindService";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int userId;
            int userId2;
            int callingPkgIdx;
            IInterface iInterface = (IInterface)args[0];
            IBinder iBinder = (IBinder)args[1];
            Intent service = new Intent((Intent)args[2]);
            String resolvedType = (String)args[3];
            IServiceConnection conn = (IServiceConnection)args[4];
            service.setDataAndType(service.getData(), resolvedType);
            ComponentName component = service.getComponent();
            if (component != null && this.isHostPkg(component.getPackageName())) {
                return method.invoke(who, args);
            }
            if (BindService.isHostIntent(service)) {
                return method.invoke(who, args);
            }
            int n = callingPkgIdx = this.isIsolated() ? 7 : 6;
            if (Build.VERSION.SDK_INT >= 23 && args.length >= 8 && args[callingPkgIdx] instanceof String) {
                args[callingPkgIdx] = BindService.getHostPkg();
            }
            long flags = this.getIntOrLongValue(args[5]);
            int n2 = userId2 = BindService.isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId2 == -1) {
                throw new IllegalArgumentException();
            }
            ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId2);
            if (serviceInfo == null) {
                if (component == null || !BindService.isOutsidePackage(component.getPackageName())) {
                    VLog.e("VActivityManager", "Block bindService: " + service);
                    return 0;
                }
                BindService.replaceLastUserId(args);
                return method.invoke(who, args);
            }
            if (this.isIsolated()) {
                args[6] = null;
            }
            if (Build.VERSION.SDK_INT < 24) {
                userId = userId2;
            } else if ((Integer.MIN_VALUE & flags) == 0L) {
                userId = userId2;
            } else if (BuildCompat.isUpsideDownCake()) {
                args[5] = flags & Integer.MAX_VALUE;
                userId = userId2;
            } else {
                userId = userId2;
                args[5] = (int)(flags & Integer.MAX_VALUE);
            }
            int userId3 = userId;
            ClientConfig clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId3);
            if (clientConfig == null) {
                VLog.e("ActivityManager", "failed to initProcess for bindService: " + component);
                return 0;
            }
            args[2] = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, (int)flags, userId3, conn);
            args[4] = ServiceConnectionProxy.getOrCreateProxy(conn);
            return method.invoke(who, args);
        }

        protected boolean isIsolated() {
            return false;
        }

        @Override
        public boolean isEnable() {
            return BindService.isAppProcess() || BindService.isServerProcess();
        }
    }

    static class StartService
    extends MethodProxy {
        StartService() {
        }

        @Override
        public String getMethodName() {
            return "startService";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int userId;
            Intent service = new Intent((Intent)args[1]);
            String resolvedType = (String)args[2];
            ComponentName component = service.getComponent();
            if (component != null && this.isHostPkg(component.getPackageName())) {
                return method.invoke(who, args);
            }
            int n = userId = StartService.isServerProcess() ? service.getIntExtra("_VA_|_user_id_", -1) : VUserHandle.myUserId();
            if (userId == -1) {
                return method.invoke(who, args);
            }
            service.setDataAndType(service.getData(), resolvedType);
            ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
            if (serviceInfo != null) {
                ClientConfig clientConfig;
                if (Build.VERSION.SDK_INT >= 26 && args.length >= 6 && args[3] instanceof Boolean) {
                    args[3] = false;
                }
                if ((clientConfig = VActivityManager.get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId)) == null) {
                    VLog.e("ActivityManager", "failed to initProcess for startService: " + component);
                    return null;
                }
                args[1] = IntentBuilder.createStartProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, userId);
                StartService.replaceLastUserId(args);
                ComponentName res = (ComponentName)method.invoke(who, args);
                if (res != null) {
                    res = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                    return res;
                }
                return null;
            }
            if (!(component != null && StartService.isOutsidePackage(component.getPackageName()) || service.getPackage() == null || StartService.isOutsidePackage(service.getPackage()))) {
                VLog.e("VActivityManager", "Block StartService: " + service);
                return null;
            }
            StartService.replaceLastUserId(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return StartService.isAppProcess() || StartService.isServerProcess();
        }
    }

    public static class GetAppTasks
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "getAppTasks";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return super.call(who, method, args);
        }
    }

    public static class StartActivityIntentSender
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "startActivityIntentSender";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int optionsIndex;
            int flagsValuesIndex;
            int flagsMaskIndex;
            int requestCodeIndex;
            int resultWhoIndex;
            int resultToIndex;
            int intentIndex;
            if (BuildCompat.isOreo()) {
                intentIndex = 3;
                resultToIndex = 5;
                resultWhoIndex = 6;
                requestCodeIndex = 7;
                flagsMaskIndex = 8;
                flagsValuesIndex = 9;
                optionsIndex = 10;
            } else {
                intentIndex = 2;
                resultToIndex = 4;
                resultWhoIndex = 5;
                requestCodeIndex = 6;
                flagsMaskIndex = 7;
                flagsValuesIndex = 8;
                optionsIndex = 9;
            }
            IInterface target = (IInterface)args[1];
            Intent fillIn = (Intent)args[intentIndex];
            IBinder resultTo = (IBinder)args[resultToIndex];
            String resultWho = (String)args[resultWhoIndex];
            int requestCode = (Integer)args[requestCodeIndex];
            Bundle options = (Bundle)args[optionsIndex];
            int flagsMask = (Integer)args[flagsMaskIndex];
            int flagsValues = (Integer)args[flagsValuesIndex];
            if (fillIn == null) {
                fillIn = new Intent();
                args[intentIndex] = fillIn;
            }
            ComponentUtils.parcelActivityIntentSender(fillIn, resultTo, options);
            return super.call(who, method, args);
        }
    }

    static class GetIntentForIntentSender
    extends MethodProxy {
        GetIntentForIntentSender() {
        }

        @Override
        public String getMethodName() {
            return "getIntentForIntentSender";
        }

        @Override
        public Object afterCall(Object who, Method method, Object[] args, Object result) {
            Intent intent = (Intent)result;
            if (intent != null) {
                try {
                    ComponentUtils.IntentSenderInfo info = ComponentUtils.parseIntentSenderInfo(intent, false);
                    if (info != null) {
                        return info.intent;
                    }
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return intent;
        }
    }

    static class IsBackgroundRestricted
    extends MethodProxy {
        IsBackgroundRestricted() {
        }

        @Override
        public String getMethodName() {
            return "isBackgroundRestricted";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    static class GetUidForIntentSender
    extends MethodProxy {
        GetUidForIntentSender() {
        }

        @Override
        public String getMethodName() {
            return "getUidForIntentSender";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IntentSenderData data;
            IInterface sender = (IInterface)args[0];
            if (sender != null && (data = VActivityManager.get().getIntentSender(sender.asBinder())) != null) {
                return VPackageManager.get().getPackageUid(data.targetPkg, data.userId);
            }
            return -1;
        }
    }

    static class UpdateDeviceOwner
    extends MethodProxy {
        UpdateDeviceOwner() {
        }

        @Override
        public String getMethodName() {
            return "updateDeviceOwner";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return UpdateDeviceOwner.isAppProcess();
        }
    }

    static class SetServiceForeground
    extends MethodProxy {
        SetServiceForeground() {
        }

        @Override
        public String getMethodName() {
            return "setServiceForeground";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return 0;
        }

        @Override
        public boolean isEnable() {
            return SetServiceForeground.isAppProcess();
        }
    }

    static class GrantUriPermissionFromOwner
    extends MethodProxy {
        GrantUriPermissionFromOwner() {
        }

        @Override
        public String getMethodName() {
            return "grantUriPermissionFromOwner";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return GrantUriPermissionFromOwner.isAppProcess();
        }
    }

    static class GetServices
    extends MethodProxy {
        GetServices() {
        }

        @Override
        public String getMethodName() {
            return "getServices";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int maxNum = (Integer)args[0];
            int flags = (Integer)args[1];
            return VActivityManager.get().getServices(VClient.get().getCurrentPackage(), maxNum, flags).getList();
        }

        @Override
        public boolean isEnable() {
            return GetServices.isAppProcess();
        }
    }

    static class PublishContentProviders
    extends MethodProxy {
        PublishContentProviders() {
        }

        @Override
        public String getMethodName() {
            return "publishContentProviders";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return PublishContentProviders.isAppProcess();
        }
    }

    static class GetPackageForIntentSender
    extends MethodProxy {
        GetPackageForIntentSender() {
        }

        @Override
        public String getMethodName() {
            return "getPackageForIntentSender";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IntentSenderData data;
            IInterface sender = (IInterface)args[0];
            if (sender != null && (data = VActivityManager.get().getIntentSender(sender.asBinder())) != null) {
                return data.targetPkg;
            }
            return super.call(who, method, args);
        }

        @Override
        public boolean isEnable() {
            return GetPackageForIntentSender.isAppProcess();
        }
    }

    public static class GetCallingPackage
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "getCallingPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            return VActivityManager.get().getCallingPackage(token);
        }

        @Override
        public boolean isEnable() {
            return GetCallingPackage.isAppProcess();
        }
    }

    static class ShouldUpRecreateTask
    extends MethodProxy {
        ShouldUpRecreateTask() {
        }

        @Override
        public String getMethodName() {
            return "shouldUpRecreateTask";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return false;
        }

        @Override
        public boolean isEnable() {
            return ShouldUpRecreateTask.isAppProcess();
        }
    }

    public static class StartActivities
    extends MethodProxy {
        @Override
        public String getMethodName() {
            return "startActivities";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token2;
            new Exception().printStackTrace();
            Intent[] intents = (Intent[])ArrayUtils.getFirst(args, Intent[].class);
            String[] resolvedTypes = (String[])ArrayUtils.getFirst(args, String[].class);
            int tokenIndex = ArrayUtils.indexOfObject(args, IBinder.class, 2);
            IBinder token = tokenIndex == -1 ? null : (token2 = (IBinder)args[tokenIndex]);
            Bundle options = (Bundle)ArrayUtils.getFirst(args, Bundle.class);
            return VActivityManager.get().startActivities(intents, resolvedTypes, token, options, VClient.get().getCurrentPackage(), VUserHandle.myUserId());
        }

        @Override
        public boolean isEnable() {
            return StartActivities.isAppProcess();
        }
    }

    public static class StartActivity
    extends MethodProxy {
        private static final String SCHEME_FILE = "file";
        private static final String SCHEME_PACKAGE = "package";
        private static final String SCHEME_CONTENT = "content";

        @Override
        public String getMethodName() {
            return "startActivity";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            int requestCode;
            String resultWho;
            int intentIndex = ArrayUtils.indexOfObject(args, Intent.class, 1);
            if (intentIndex < 0) {
                return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
            }
            int resultToIndex = ArrayUtils.indexOfObject(args, IBinder.class, 2);
            String resolvedType = (String)args[intentIndex + 1];
            Intent intent = new Intent((Intent)args[intentIndex]);
            intent.setDataAndType(intent.getData(), resolvedType);
            IBinder resultTo = resultToIndex >= 0 ? (IBinder)args[resultToIndex] : null;
            Bundle options = (Bundle)ArrayUtils.getFirst(args, Bundle.class);
            if (resultTo == null) {
                resultWho = null;
                requestCode = 0;
            } else {
                String resultWho2 = (String)args[resultToIndex + 1];
                int requestCode2 = (Integer)args[resultToIndex + 2];
                resultWho = resultWho2;
                requestCode = requestCode2;
            }
            int userId = VUserHandle.myUserId();
            String action = intent.getAction();
            if ("android.intent.action.MAIN".equals(action) && intent.hasCategory("android.intent.category.HOME")) {
                Intent homeIntent = StartActivity.getConfig().onHandleLauncherIntent(intent);
                if (homeIntent != null) {
                    args[intentIndex] = homeIntent;
                }
                return method.invoke(who, args);
            }
            if (!"android.settings.APP_NOTIFICATION_SETTINGS".equals(action) && !"android.settings.CHANNEL_NOTIFICATION_SETTINGS".equals(action)) {
                ActivityInfo activityInfo;
                String pkg;
                if (StartActivity.isHostIntent(intent)) {
                    return method.invoke(who, args);
                }
                if ("android.intent.action.INSTALL_PACKAGE".equals(action) || "android.intent.action.VIEW".equals(action) && "application/vnd.android.package-archive".equals(intent.getType())) {
                    if (this.handleInstallRequest(intent)) {
                        if (resultTo != null && requestCode > 0) {
                            VActivityManager.get().sendCancelActivityResult(resultTo, resultWho, requestCode);
                        }
                        return 0;
                    }
                } else if (("android.intent.action.UNINSTALL_PACKAGE".equals(action) || "android.intent.action.DELETE".equals(action)) && SCHEME_PACKAGE.equals(intent.getScheme()) && this.handleUninstallRequest(intent)) {
                    return 0;
                }
                if ((pkg = intent.getPackage()) != null && !this.isAppPkg(pkg)) {
                    if (BuildCompat.isR() && "android.content.pm.action.REQUEST_PERMISSIONS".equals(action)) {
                        args[intentIndex - 2] = StartActivity.getHostPkg();
                    }
                    return method.invoke(who, args);
                }
                if (ChooserActivity.check(intent)) {
                    Intent intent2 = ComponentUtils.processOutsideIntent(userId, VirtualCore.get().isExtPackage(), new Intent(intent));
                    args[intentIndex] = intent2;
                    Bundle extras = new Bundle();
                    extras.putInt("android.intent.extra.user_handle", userId);
                    extras.putBundle("android.intent.extra.virtual.data", options);
                    extras.putString("android.intent.extra.virtual.who", resultWho);
                    extras.putInt("android.intent.extra.virtual.request_code", requestCode);
                    BundleCompat.putBinder(extras, "_va|ibinder|resultTo", resultTo);
                    intent2.setComponent(new ComponentName(StubManifest.PACKAGE_NAME, ChooserActivity.class.getName()));
                    intent2.setAction(null);
                    intent2.putExtras(extras);
                    return method.invoke(who, args);
                }
                args[intentIndex - 1] = StartActivity.getHostPkg();
                if (intent.getScheme() != null && intent.getScheme().equals(SCHEME_PACKAGE) && intent.getData() != null && action != null && action.startsWith("android.settings.")) {
                    intent.setData(Uri.parse((String)("package:" + StartActivity.getHostPkg())));
                }
                if ((activityInfo = VirtualCore.get().resolveActivityInfo(intent, userId)) == null) {
                    VLog.e("VActivityManager", "Unable to resolve activityInfo : %s", intent);
                    if (intent.getPackage() != null && this.isAppPkg(intent.getPackage())) {
                        return 0;
                    }
                    args[intentIndex] = ComponentUtils.processOutsideIntent(userId, VirtualCore.get().isExtPackage(), intent);
                    ResolveInfo resolveInfo = VirtualCore.get().getHostPackageManager().resolveActivity(intent, 0L);
                    if ((resolveInfo == null || resolveInfo.activityInfo == null) && intent.resolveActivityInfo(VirtualCore.getPM(), 0) != null) {
                        return method.invoke(who, args);
                    }
                    if ("android.intent.action.VIEW".equals(action) || StartActivity.getConfig().isOutsideAction(action) || resolveInfo != null && StartActivity.isOutsidePackage(resolveInfo.activityInfo.packageName)) {
                        return method.invoke(who, args);
                    }
                    return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
                }
                int requestCode3 = requestCode;
                String resultWho3 = resultWho;
                int res = VActivityManager.get().startActivity(intent, activityInfo, resultTo, options, resultWho3, requestCode3, VClient.get().getCurrentPackage(), VUserHandle.myUserId());
                if (res != 0 && resultTo != null && requestCode3 > 0) {
                    VActivityManager.get().sendCancelActivityResult(resultTo, resultWho3, requestCode3);
                }
                return res;
            }
            Intent intent3 = (Intent)args[intentIndex];
            if (BuildCompat.isOreo()) {
                intent3.putExtra("android.provider.extra.APP_PACKAGE", VirtualCore.get().getHostPkg());
            } else {
                intent3.putExtra("app_package", VirtualCore.get().getHostPkg());
            }
            return method.invoke(who, args);
        }

        private boolean handleInstallRequest(Intent intent) {
            VirtualCore.AppRequestListener listener = VirtualCore.get().getAppRequestListener();
            if (listener != null) {
                Uri packageUri = intent.getData();
                if (SCHEME_FILE.equals(packageUri.getScheme())) {
                    File sourceFile = new File(packageUri.getPath());
                    String path = NativeEngine.getRedirectedPath(sourceFile.getAbsolutePath());
                    listener.onRequestInstall(path);
                    return true;
                }
                if (SCHEME_CONTENT.equals(packageUri.getScheme())) {
                    InputStream inputStream = null;
                    FileOutputStream outputStream = null;
                    File sharedFileCopy = new File(StartActivity.getHostContext().getCacheDir(), packageUri.getLastPathSegment());
                    try {
                        try {
                            int count;
                            inputStream = StartActivity.getHostContext().getContentResolver().openInputStream(packageUri);
                            outputStream = new FileOutputStream(sharedFileCopy);
                            byte[] buffer = new byte[1024];
                            while ((count = inputStream.read(buffer)) > 0) {
                                ((OutputStream)outputStream).write(buffer, 0, count);
                            }
                            outputStream.flush();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        FileUtils.closeQuietly(inputStream);
                        FileUtils.closeQuietly(outputStream);
                        listener.onRequestInstall(sharedFileCopy.getPath());
                        return true;
                    }
                    catch (Throwable th) {
                        FileUtils.closeQuietly(inputStream);
                        FileUtils.closeQuietly(outputStream);
                        throw th;
                    }
                }
            }
            return false;
        }

        private boolean handleUninstallRequest(Intent intent) {
            VirtualCore.AppRequestListener listener = VirtualCore.get().getAppRequestListener();
            if (listener != null) {
                Uri packageUri = intent.getData();
                if (SCHEME_PACKAGE.equals(packageUri.getScheme())) {
                    String pkg = packageUri.getSchemeSpecificPart();
                    listener.onRequestUninstall(pkg);
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override
        public boolean isEnable() {
            return StartActivity.isAppProcess();
        }
    }

    static class GetIntentSenderWithSourceToken
    extends GetIntentSender {
        GetIntentSenderWithSourceToken() {
        }

        @Override
        public String getMethodName() {
            return "getIntentSenderWithSourceToken";
        }
    }

    static class GetIntentSender
    extends MethodProxy {
        GetIntentSender() {
        }

        @Override
        public String getMethodName() {
            return "getIntentSender";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String creator = (String)args[1];
            args[1] = GetIntentSender.getHostPkg();
            if (args[args.length - 1] instanceof Integer) {
                args[args.length - 1] = GetIntentSender.getRealUserId();
            }
            int type = (Integer)args[0];
            int intentsIndex = ArrayUtils.indexOfFirst(args, Intent[].class);
            Intent[] intents = (Intent[])args[intentsIndex];
            String[] resolvedTypes = (String[])args[intentsIndex + 1];
            int flags = (Integer)args[intentsIndex + 2];
            int userId = VUserHandle.myUserId();
            if (intents.length > 0) {
                IInterface sender;
                Intent targetIntent;
                Intent intent = intents[intents.length - 1];
                if (resolvedTypes != null && resolvedTypes.length >= intents.length) {
                    intent.setDataAndType(intent.getData(), resolvedTypes[intents.length - 1]);
                }
                if ((targetIntent = ComponentUtils.getProxyIntentSenderIntent(userId, type, creator, intent)) == null) {
                    return null;
                }
                flags &= 0xFFFFFFF7;
                if (Build.VERSION.SDK_INT >= 16) {
                    flags &= 0xFFFFFF7F;
                }
                if ((0x8000000 & flags) != 0) {
                    flags = flags & 0xD7FFFFFF | 0x10000000;
                }
                args[intentsIndex] = new Intent[]{targetIntent};
                args[intentsIndex + 1] = new String[]{null};
                if ((flags & 0x10000000) != 0 && BuildCompat.isSamsung() && Build.VERSION.SDK_INT >= 21) {
                    AlarmManager alarmManager;
                    PendingIntent pendingIntent;
                    args[intentsIndex + 2] = 0x20000000;
                    sender = (IInterface)method.invoke(who, args);
                    if (sender != null && (pendingIntent = PendingIntentCompat.readPendingIntent(sender.asBinder())) != null && (alarmManager = (AlarmManager)GetIntentSender.getHostContext().getSystemService("alarm")) != null) {
                        try {
                            alarmManager.cancel(pendingIntent);
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
                args[intentsIndex + 2] = flags;
                sender = (IInterface)method.invoke(who, args);
                if (sender != null) {
                    IBinder token = sender.asBinder();
                    IntentSenderData data = new IntentSenderData(creator, token, type, userId);
                    VActivityManager.get().addOrUpdateIntentSender(data);
                }
                return sender;
            }
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return GetIntentSender.isAppProcess();
        }
    }

    static class GetIntentSenderWithFeature
    extends GetIntentSender {
        GetIntentSenderWithFeature() {
        }

        @Override
        public String getMethodName() {
            return "getIntentSenderWithFeature";
        }
    }

    static class GetPackageAskScreenCompat
    extends MethodProxy {
        GetPackageAskScreenCompat() {
        }

        @Override
        public String getMethodName() {
            return "getPackageAskScreenCompat";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (Build.VERSION.SDK_INT >= 15 && args.length > 0 && args[0] instanceof String) {
                args[0] = GetPackageAskScreenCompat.getHostPkg();
            }
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return GetPackageAskScreenCompat.isAppProcess();
        }
    }

    static class UnstableProviderDied
    extends MethodProxy {
        UnstableProviderDied() {
        }

        @Override
        public String getMethodName() {
            return "unstableProviderDied";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            if (args[0] == null) {
                return 0;
            }
            return method.invoke(who, args);
        }
    }

    static class StartVoiceActivity
    extends StartActivity {
        StartVoiceActivity() {
        }

        @Override
        public String getMethodName() {
            return "startVoiceActivity";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return super.call(who, method, args);
        }
    }

    static class GetContentProviderExternal
    extends GetContentProvider {
        GetContentProviderExternal() {
        }

        @Override
        public String getMethodName() {
            return "getContentProviderExternal";
        }

        @Override
        public int getProviderNameIndex() {
            if (BuildCompat.isQ()) {
                return 1;
            }
            return 0;
        }

        @Override
        public boolean isEnable() {
            return GetContentProviderExternal.isAppProcess();
        }
    }

    static class GetPackageForToken
    extends MethodProxy {
        GetPackageForToken() {
        }

        @Override
        public String getMethodName() {
            return "getPackageForToken";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            String pkg = VActivityManager.get().getPackageForToken(token);
            if (pkg != null) {
                return pkg;
            }
            return super.call(who, method, args);
        }
    }

    static class AddPackageDependency
    extends MethodProxy {
        AddPackageDependency() {
        }

        @Override
        public String getMethodName() {
            return "addPackageDependency";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return AddPackageDependency.isAppProcess();
        }
    }

    static class CrashApplication
    extends MethodProxy {
        CrashApplication() {
        }

        @Override
        public String getMethodName() {
            return "crashApplication";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return CrashApplication.isAppProcess();
        }
    }

    static class ForceStopPackage
    extends MethodProxy {
        ForceStopPackage() {
        }

        @Override
        public String getMethodName() {
            return "forceStopPackage";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String pkg = (String)args[0];
            int userId = VUserHandle.myUserId();
            VActivityManager.get().killAppByPkg(pkg, userId);
            return 0;
        }

        @Override
        public boolean isEnable() {
            return ForceStopPackage.isAppProcess();
        }
    }

    static class GetRecentTasks
    extends MethodProxy {
        GetRecentTasks() {
        }

        @Override
        public String getMethodName() {
            return "getRecentTasks";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceFirstUserId(args);
            Object _infos = method.invoke(who, args);
            List<ActivityManager.RecentTaskInfo> infos = (List)(ParceledListSliceCompat.isReturnParceledListSlice(method) ? ParceledListSlice.getList.call(_infos, new Object[0]) : _infos);
            Iterator var6 = infos.iterator();

            while(var6.hasNext()) {
                ActivityManager.RecentTaskInfo info = (ActivityManager.RecentTaskInfo)var6.next();
                AppTaskInfo taskInfo = VActivityManager.get().getTaskInfo(info.id);
                if (taskInfo != null) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        try {
                            info.topActivity = taskInfo.topActivity;
                            info.baseActivity = taskInfo.baseActivity;
                        } catch (Throwable var10) {
                        }
                    }

                    try {
                        info.origActivity = taskInfo.baseActivity;
                        info.baseIntent = taskInfo.baseIntent;
                    } catch (Throwable var11) {
                    }
                }
            }

            return _infos;
        }
    }

    static class FinishReceiver
    extends MethodProxy {
        FinishReceiver() {
        }

        @Override
        public String getMethodName() {
            return "finishReceiver";
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            IBinder token = (IBinder)args[0];
            VActivityManager.get().broadcastFinish(token);
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return FinishReceiver.isAppProcess();
        }
    }
}

