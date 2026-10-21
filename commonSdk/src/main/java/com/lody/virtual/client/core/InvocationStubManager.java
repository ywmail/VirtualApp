/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.core;

import android.os.Build;
import android.os.StatsManagerServiceStub;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.OemInjectManager;
import com.lody.virtual.client.core.OtherInjectManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.instruments.InstrumentationVirtualApp;
import com.lody.virtual.client.hook.proxies.accessibility.AccessibilityManagerStub;
import com.lody.virtual.client.hook.proxies.account.AccountManagerStub;
import com.lody.virtual.client.hook.proxies.alarm.AlarmManagerStub;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.hook.proxies.app.ActivityClientControllerStub;
import com.lody.virtual.client.hook.proxies.app.LocaleManagerStub;
import com.lody.virtual.client.hook.proxies.appops.AppOpsManagerStub;
import com.lody.virtual.client.hook.proxies.appops.FlymePermissionServiceStub;
import com.lody.virtual.client.hook.proxies.appops.SmtOpsManagerStub;
import com.lody.virtual.client.hook.proxies.appwidget.AppWidgetManagerStub;
import com.lody.virtual.client.hook.proxies.atm.ActivityTaskManagerStub;
import com.lody.virtual.client.hook.proxies.audio.AudioManagerStub;
import com.lody.virtual.client.hook.proxies.backup.BackupManagerStub;
import com.lody.virtual.client.hook.proxies.battery_stats.BatteryStatsHub;
import com.lody.virtual.client.hook.proxies.bluetooth.BluetoothStub;
import com.lody.virtual.client.hook.proxies.clipboard.ClipBoardStub;
import com.lody.virtual.client.hook.proxies.clipboard.SemClipBoardStub;
import com.lody.virtual.client.hook.proxies.connectivity.ConnectivityStub;
import com.lody.virtual.client.hook.proxies.content.ContentServiceStub;
import com.lody.virtual.client.hook.proxies.content.integrity.AppIntegrityManagerStub;
import com.lody.virtual.client.hook.proxies.context_hub.ContextHubServiceStub;
import com.lody.virtual.client.hook.proxies.cross_profile.CrossProfileAppsStub;
import com.lody.virtual.client.hook.proxies.dev_identifiers_policy.DeviceIdentifiersPolicyServiceHub;
import com.lody.virtual.client.hook.proxies.device.DeviceIdleControllerStub;
import com.lody.virtual.client.hook.proxies.devicepolicy.DevicePolicyManagerStub;
import com.lody.virtual.client.hook.proxies.display.DisplayStub;
import com.lody.virtual.client.hook.proxies.dropbox.DropBoxManagerStub;
import com.lody.virtual.client.hook.proxies.fingerprint.FingerprintManagerStub;
import com.lody.virtual.client.hook.proxies.graphics.GraphicsStatsStub;
import com.lody.virtual.client.hook.proxies.imms.MmsStub;
import com.lody.virtual.client.hook.proxies.input.InputMethodManagerStub;
import com.lody.virtual.client.hook.proxies.isms.ISmsStub;
import com.lody.virtual.client.hook.proxies.isub.ISubStub;
import com.lody.virtual.client.hook.proxies.job.JobServiceStub;
import com.lody.virtual.client.hook.proxies.libcore.LibCoreStub;
import com.lody.virtual.client.hook.proxies.location.LocationManagerStub;
import com.lody.virtual.client.hook.proxies.media.router.MediaRouterServiceStub;
import com.lody.virtual.client.hook.proxies.media.session.SessionManagerStub;
import com.lody.virtual.client.hook.proxies.mount.MountServiceStub;
import com.lody.virtual.client.hook.proxies.network.NetworkManagementStub;
import com.lody.virtual.client.hook.proxies.network.TetheringConnectorStub;
import com.lody.virtual.client.hook.proxies.notification.NotificationManagerStub;
import com.lody.virtual.client.hook.proxies.permissionmgr.PermissionManagerStub;
import com.lody.virtual.client.hook.proxies.persistent_data_block.PersistentDataBlockServiceStub;
import com.lody.virtual.client.hook.proxies.phonesubinfo.PhoneSubInfoStub;
import com.lody.virtual.client.hook.proxies.pm.PackageManagerStub;
import com.lody.virtual.client.hook.proxies.power.PowerManagerStub;
import com.lody.virtual.client.hook.proxies.restriction.RestrictionStub;
import com.lody.virtual.client.hook.proxies.role.RoleManagerStub;
import com.lody.virtual.client.hook.proxies.search.SearchManagerStub;
import com.lody.virtual.client.hook.proxies.shortcut.ShortcutServiceStub;
import com.lody.virtual.client.hook.proxies.slice.SliceManagerStub;
import com.lody.virtual.client.hook.proxies.system.LockSettingsStub;
import com.lody.virtual.client.hook.proxies.system.SystemUpdateStub;
import com.lody.virtual.client.hook.proxies.system.WifiScannerStub;
import com.lody.virtual.client.hook.proxies.telecom.TelecomManagerStub;
import com.lody.virtual.client.hook.proxies.telephony.HwTelephonyStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyRegistryStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyStub;
import com.lody.virtual.client.hook.proxies.textservices.TextServicesManagerServiceStub;
import com.lody.virtual.client.hook.proxies.uri_grants.UriGrantsManagerStub;
import com.lody.virtual.client.hook.proxies.usage.UsageStatsManagerStub;
import com.lody.virtual.client.hook.proxies.user.UserManagerStub;
import com.lody.virtual.client.hook.proxies.vibrator.VibratorStub;
import com.lody.virtual.client.hook.proxies.view.AutoFillManagerStub;
import com.lody.virtual.client.hook.proxies.wallpaper.WallpaperManagerStub;
import com.lody.virtual.client.hook.proxies.wifi.WifiManagerStub;
import com.lody.virtual.client.hook.proxies.window.WindowManagerStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.helper.compat.BuildCompat;
import java.util.HashMap;
import java.util.Map;
import mirror.android.os.IDeviceIdleController;
import mirror.com.android.internal.app.ISmtOpsService;
import mirror.com.android.internal.telephony.IHwTelephony;
import mirror.oem.IFlymePermissionService;

public final class InvocationStubManager {
    private static InvocationStubManager sInstance = new InvocationStubManager();
    private static boolean sInit;
    private final Map<Class<?>, IInjector> mInjectors = new HashMap(20);

    private InvocationStubManager() {
    }

    public static InvocationStubManager getInstance() {
        return sInstance;
    }

    void injectAll() throws Throwable {
        for (IInjector injector : this.mInjectors.values()) {
            injector.inject();
        }
    }

    public boolean isInit() {
        return sInit;
    }

    public void init() throws Throwable {
        if (this.isInit()) {
            throw new IllegalStateException("InvocationStubManager Has been initialized.");
        }
        this.injectInternal();
        if (VirtualCore.get().isVAppProcess()) {
            this.addInjector(InstrumentationVirtualApp.getDefault());
        }
        sInit = true;
    }

    private void injectInternal() throws Throwable {
        if (VirtualCore.get().isMainProcess()) {
            return;
        }
        if (VirtualCore.get().isServerProcess()) {
            this.addInjector(new ActivityManagerStub());
            return;
        }
        if (VirtualCore.get().isVAppProcess()) {
            this.addInjector(new LibCoreStub());
            this.addInjector(new ActivityManagerStub());
            this.addInjector(new PackageManagerStub());
            this.addInjector(HCallbackStub.getDefault());
            this.addInjector(new ISmsStub());
            this.addInjector(new ISubStub());
            this.addInjector(new DropBoxManagerStub());
            this.addInjector(new NotificationManagerStub());
            this.addInjector(new LocationManagerStub());
            this.addInjector(new WindowManagerStub());
            this.addInjector(new ClipBoardStub());
            this.addInjector(new SemClipBoardStub());
            this.addInjector(new MountServiceStub());
            this.addInjector(new BackupManagerStub());
            this.addInjector(new TelephonyStub());
            this.addInjector(new AccessibilityManagerStub());
            if (BuildCompat.isOreo() && IHwTelephony.TYPE != null) {
                this.addInjector(new HwTelephonyStub());
            }
            this.addInjector(new TelephonyRegistryStub());
            this.addInjector(new PhoneSubInfoStub());
            this.addInjector(new PowerManagerStub());
            this.addInjector(new AppWidgetManagerStub());
            this.addInjector(new AccountManagerStub());
            this.addInjector(new AudioManagerStub());
            this.addInjector(new SearchManagerStub());
            this.addInjector(new ContentServiceStub());
            this.addInjector(new ConnectivityStub());
            this.addInjector(new BluetoothStub());
            this.addInjector(new VibratorStub());
            this.addInjector(new WifiManagerStub());
            this.addInjector(new ContextHubServiceStub());
            this.addInjector(new UserManagerStub());
            this.addInjector(new WallpaperManagerStub());
            this.addInjector(new DisplayStub());
            this.addInjector(new PersistentDataBlockServiceStub());
            this.addInjector(new InputMethodManagerStub());
            this.addInjector(new MmsStub());
            this.addInjector(new SessionManagerStub());
            this.addInjector(new JobServiceStub());
            this.addInjector(new RestrictionStub());
            this.addInjector(new TelecomManagerStub());
            this.addInjector(new AlarmManagerStub());
            this.addInjector(new AppOpsManagerStub());
            this.addInjector(new MediaRouterServiceStub());
            if (ISmtOpsService.TYPE != null) {
                this.addInjector(new SmtOpsManagerStub());
            }
            if (Build.VERSION.SDK_INT >= 22) {
                this.addInjector(new GraphicsStatsStub());
                this.addInjector(new UsageStatsManagerStub());
            }
            if (Build.VERSION.SDK_INT >= 23) {
                this.addInjector(new FingerprintManagerStub());
                this.addInjector(new NetworkManagementStub());
            }
            if (Build.VERSION.SDK_INT >= 24) {
                this.addInjector(new WifiScannerStub());
                this.addInjector(new ShortcutServiceStub());
                this.addInjector(new DevicePolicyManagerStub());
                this.addInjector(new BatteryStatsHub());
            }
            if (BuildCompat.isOreo() && !BuildCompat.isTiramisu()) {
                this.addInjector(new AutoFillManagerStub());
            }
            if (BuildCompat.isPie()) {
                this.addInjector(new SystemUpdateStub());
                this.addInjector(new LockSettingsStub());
                this.addInjector(new CrossProfileAppsStub());
                this.addInjector(new SliceManagerStub());
            }
            if (IFlymePermissionService.TYPE != null) {
                this.addInjector(new FlymePermissionServiceStub());
            }
            if (BuildCompat.isQ()) {
                this.addInjector(new ActivityTaskManagerStub());
                this.addInjector(new DeviceIdentifiersPolicyServiceHub());
                this.addInjector(new UriGrantsManagerStub());
                this.addInjector(new RoleManagerStub());
                this.addInjector(new TextServicesManagerServiceStub());
            }
            if (BuildCompat.isR()) {
                this.addInjector(new PermissionManagerStub());
                this.addInjector(new AppIntegrityManagerStub());
                this.addInjector(new StatsManagerServiceStub());
                this.addInjector(new TetheringConnectorStub());
            }
            if (BuildCompat.isS()) {
                this.addInjector(new ActivityClientControllerStub());
            }
            if (BuildCompat.isTiramisu()) {
                this.addInjector(new LocaleManagerStub());
            }
            if (IDeviceIdleController.TYPE != null) {
                this.addInjector(new DeviceIdleControllerStub());
            }
            OemInjectManager.oemInject(this);
            OtherInjectManager.otherInjectManager(this);
        }
    }

    public void addInjector(IInjector IInjector2) {
        this.mInjectors.put(IInjector2.getClass(), IInjector2);
    }

    public <T extends IInjector> T findInjector(Class<T> clazz) {
        return (T)this.mInjectors.get(clazz);
    }

    public <T extends IInjector> void checkEnv(Class<T> clazz) {
        T IInjector2 = this.findInjector(clazz);
        if (IInjector2 != null && IInjector2.isEnvBad()) {
            try {
                IInjector2.inject();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void checkAllEnv() {
        for (IInjector injector : this.mInjectors.values()) {
            if (!injector.isEnvBad()) continue;
            try {
                injector.inject();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends IInjector, H extends MethodInvocationStub> H getInvocationStub(Class<T> injectorClass) {
        T injector = this.findInjector(injectorClass);
        if (injector instanceof MethodInvocationProxy) {
            return (H)((MethodInvocationProxy)injector).getInvocationStub();
        }
        return null;
    }
}

