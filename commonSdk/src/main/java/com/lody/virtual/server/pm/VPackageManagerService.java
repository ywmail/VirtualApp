//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.pm;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ComponentFixer;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.compat.PermissionCompat;
import com.lody.virtual.helper.utils.SignaturesUtils;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.ReceiverInfo;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.interfaces.IPackageManager;
import com.lody.virtual.server.pm.installer.VPackageInstallerService;
import com.lody.virtual.server.pm.parser.PackageParserEx;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VPackageManagerService extends IPackageManager.Stub {
    static final String TAG = "PackageManager";
    static final Comparator<ResolveInfo> sResolvePrioritySorter = new Comparator<ResolveInfo>() {
        public int compare(ResolveInfo r1, ResolveInfo r2) {
            int v1 = r1.priority;
            int v2 = r2.priority;
            if (v1 != v2) {
                return v1 > v2 ? -1 : 1;
            } else {
                v1 = r1.preferredOrder;
                v2 = r2.preferredOrder;
                if (v1 != v2) {
                    return v1 > v2 ? -1 : 1;
                } else if (r1.isDefault != r2.isDefault) {
                    return r1.isDefault ? -1 : 1;
                } else {
                    v1 = r1.match;
                    v2 = r2.match;
                    if (v1 != v2) {
                        return v1 > v2 ? -1 : 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    };
    private static final Singleton<VPackageManagerService> gService = new Singleton<VPackageManagerService>() {
        protected VPackageManagerService create() {
            return new VPackageManagerService();
        }
    };
    private static final Comparator<ProviderInfo> sProviderInitOrderSorter = new Comparator<ProviderInfo>() {
        public int compare(ProviderInfo p1, ProviderInfo p2) {
            int v1 = p1.initOrder;
            int v2 = p2.initOrder;
            return Integer.compare(v2, v1);
        }
    };
    private final ActivityIntentResolver mActivities;
    private final ServiceIntentResolver mServices;
    private final ActivityIntentResolver mReceivers;
    private final ProviderIntentResolver mProviders;
    private final HashMap<ComponentName, VPackage.ProviderComponent> mProvidersByComponent;
    private final HashMap<String, VPackage.PermissionComponent> mPermissions;
    private final HashMap<String, VPackage.PermissionGroupComponent> mPermissionGroups;
    private final HashMap<String, VPackage.ProviderComponent> mProvidersByAuthority;
    private final Map<String, VPackage> mPackages;
    private final Map<String, String[]> mDangerousPermissions;

    private VPackageManagerService() {
        this.mActivities = new ActivityIntentResolver();
        this.mServices = new ServiceIntentResolver();
        this.mReceivers = new ActivityIntentResolver();
        this.mProviders = new ProviderIntentResolver();
        this.mProvidersByComponent = new HashMap();
        this.mPermissions = new HashMap();
        this.mPermissionGroups = new HashMap();
        this.mProvidersByAuthority = new HashMap();
        this.mPackages = PackageCacheManager.PACKAGE_CACHE;
        this.mDangerousPermissions = new HashMap();
    }

    public static void systemReady() {
        new VUserManagerService(VirtualCore.get().getContext(), get(), new char[0], get().mPackages);
    }

    public static VPackageManagerService get() {
        return (VPackageManagerService)gService.get();
    }

    void analyzePackageLocked(VPackage pkg) {
        int N = pkg.activities.size();

        int i;
        VPackage.ActivityComponent a;
        for(i = 0; i < N; ++i) {
            a = (VPackage.ActivityComponent)pkg.activities.get(i);
            if (a.info.processName == null) {
                a.info.processName = a.info.packageName;
            }

            this.mActivities.addActivity(a, "activity");
        }

        N = pkg.services.size();

        for(i = 0; i < N; ++i) {
            VPackage.ServiceComponent aa = (VPackage.ServiceComponent)pkg.services.get(i);
            if (aa.info.processName == null) {
                aa.info.processName = aa.info.packageName;
            }

            this.mServices.addService(aa);
        }

        N = pkg.receivers.size();

        for(i = 0; i < N; ++i) {
            a = (VPackage.ActivityComponent)pkg.receivers.get(i);
            if (a.info.processName == null) {
                a.info.processName = a.info.packageName;
            }

            this.mReceivers.addActivity(a, "receiver");
        }

        N = pkg.providers.size();

        for(i = 0; i < N; ++i) {
            VPackage.ProviderComponent p = (VPackage.ProviderComponent)pkg.providers.get(i);
            if (p.info.processName == null) {
                p.info.processName = p.info.packageName;
            }

            this.mProviders.addProvider(p);
            String[] names = p.info.authority.split(";");
            synchronized(this.mProvidersByAuthority) {
                String[] var7 = names;
                int var8 = names.length;
                int var9 = 0;

                while(true) {
                    if (var9 >= var8) {
                        break;
                    }

                    String name = var7[var9];
                    if (!this.mProvidersByAuthority.containsKey(name)) {
                        this.mProvidersByAuthority.put(name, p);
                    }

                    ++var9;
                }
            }

            this.mProvidersByComponent.put(p.getComponentName(), p);
        }

        N = pkg.permissions.size();

        for(i = 0; i < N; ++i) {
            VPackage.PermissionComponent permission = (VPackage.PermissionComponent)pkg.permissions.get(i);
            this.mPermissions.put(permission.info.name, permission);
        }

        N = pkg.permissionGroups.size();

        for(i = 0; i < N; ++i) {
            VPackage.PermissionGroupComponent group = (VPackage.PermissionGroupComponent)pkg.permissionGroups.get(i);
            this.mPermissionGroups.put(group.className, group);
        }

        synchronized(this.mDangerousPermissions) {
            this.mDangerousPermissions.put(pkg.packageName, PermissionCompat.findDangerousPermissions(pkg.requestedPermissions));
        }
    }

    public String[] getDangerousPermissions(String packageName) {
        synchronized(this.mDangerousPermissions) {
            return (String[])this.mDangerousPermissions.get(packageName);
        }
    }

    void deletePackageLocked(VPackage pkg) {
        if (pkg != null) {
            int N = pkg.activities.size();

            int i;
            VPackage.ActivityComponent a;
            for(i = 0; i < N; ++i) {
                a = (VPackage.ActivityComponent)pkg.activities.get(i);
                this.mActivities.removeActivity(a, "activity");
            }

            N = pkg.services.size();

            for(i = 0; i < N; ++i) {
                VPackage.ServiceComponent aa = (VPackage.ServiceComponent)pkg.services.get(i);
                this.mServices.removeService(aa);
            }

            N = pkg.receivers.size();

            for(i = 0; i < N; ++i) {
                a = (VPackage.ActivityComponent)pkg.receivers.get(i);
                this.mReceivers.removeActivity(a, "receiver");
            }

            N = pkg.providers.size();

            for(i = 0; i < N; ++i) {
                VPackage.ProviderComponent p = (VPackage.ProviderComponent)pkg.providers.get(i);
                this.mProviders.removeProvider(p);
                String[] names = p.info.authority.split(";");
                synchronized(this.mProvidersByAuthority) {
                    String[] var7 = names;
                    int var8 = names.length;
                    int var9 = 0;

                    while(true) {
                        if (var9 >= var8) {
                            break;
                        }

                        String name = var7[var9];
                        this.mProvidersByAuthority.remove(name);
                        ++var9;
                    }
                }

                this.mProvidersByComponent.remove(p.getComponentName());
            }

            N = pkg.permissions.size();

            for(i = 0; i < N; ++i) {
                VPackage.PermissionComponent permission = (VPackage.PermissionComponent)pkg.permissions.get(i);
                this.mPermissions.remove(permission.className);
            }

            N = pkg.permissionGroups.size();

            for(i = 0; i < N; ++i) {
                VPackage.PermissionGroupComponent group = (VPackage.PermissionGroupComponent)pkg.permissionGroups.get(i);
                this.mPermissionGroups.remove(group.className);
            }

        }
    }

    public List<String> getSharedLibraries(String packageName) {
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            return p != null ? p.usesLibraries : null;
        }
    }

    public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
        this.checkUserId(userId);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                return this.generatePackageInfo(p, ps, flags, userId);
            } else {
                return null;
            }
        }
    }

    private PackageInfo generatePackageInfo(VPackage p, PackageSetting ps, int flags, int userId) {
        flags = this.updateFlagsNought(flags);
        return PackageParserEx.generatePackageInfo(p, ps, flags, ps.firstInstallTime, ps.lastUpdateTime, ps.readUserState(userId), userId, ps.isRunInExtProcess());
    }

    private int updateFlagsNought(int flags) {
        if (VERSION.SDK_INT < 24) {
            return flags;
        } else {
            if ((flags & 786432) == 0) {
                flags |= 786432;
            }

            return flags;
        }
    }

    private void checkUserId(int userId) {
        if (!VUserManagerService.get().exists(userId)) {
            throw new SecurityException("Invalid userId " + userId);
        }
    }

    public ActivityInfo getActivityInfo(ComponentName component, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(component.getPackageName());
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                VPackage.ActivityComponent a = (VPackage.ActivityComponent)this.mActivities.mActivities.get(component);
                if (a != null) {
                    ActivityInfo activityInfo = PackageParserEx.generateActivityInfo(a, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                    ComponentFixer.fixComponentInfo(activityInfo);
                    return activityInfo;
                }
            }

            return null;
        }
    }

    public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) {
        synchronized(this.mPackages) {
            VPackage.ActivityComponent a = (VPackage.ActivityComponent)this.mActivities.mActivities.get(component);
            if (a == null) {
                return false;
            } else {
                for(int i = 0; i < a.intents.size(); ++i) {
                    if (((VPackage.ActivityIntentInfo)a.intents.get(i)).filter.match(intent.getAction(), resolvedType, intent.getScheme(), intent.getData(), intent.getCategories(), TAG) >= 0) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public ActivityInfo getReceiverInfo(ComponentName component, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(component.getPackageName());
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                VPackage.ActivityComponent a = (VPackage.ActivityComponent)this.mReceivers.mActivities.get(component);
                if (a != null && ps.isEnabledAndMatchLPr(a.info, flags, userId)) {
                    ActivityInfo receiverInfo = PackageParserEx.generateActivityInfo(a, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                    ComponentFixer.fixComponentInfo(receiverInfo);
                    return receiverInfo;
                }
            }

            return null;
        }
    }

    public ServiceInfo getServiceInfo(ComponentName component, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(component.getPackageName());
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                VPackage.ServiceComponent s = (VPackage.ServiceComponent)this.mServices.mServices.get(component);
                if (s != null) {
                    ServiceInfo serviceInfo = PackageParserEx.generateServiceInfo(s, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                    ComponentFixer.fixComponentInfo(serviceInfo);
                    return serviceInfo;
                }
            }

            return null;
        }
    }

    public ProviderInfo getProviderInfo(ComponentName component, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(component.getPackageName());
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                VPackage.ProviderComponent provider = (VPackage.ProviderComponent)this.mProvidersByComponent.get(component);
                if (provider != null && ps.isEnabledAndMatchLPr(provider.info, flags, userId)) {
                    ProviderInfo providerInfo = PackageParserEx.generateProviderInfo(provider, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                    ComponentFixer.fixComponentInfo(providerInfo);
                    return providerInfo;
                }
            }

            return null;
        }
    }

    public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        List<ResolveInfo> query = this.queryIntentActivities(intent, resolvedType, flags, userId);
        return this.chooseBestActivity(intent, resolvedType, flags, query);
    }

    private ResolveInfo chooseBestActivity(Intent intent, String resolvedType, int flags, List<ResolveInfo> query) {
        if (query != null) {
            int N = query.size();
            if (N == 1) {
                return (ResolveInfo)query.get(0);
            }

            if (N > 1) {
                ResolveInfo r0 = (ResolveInfo)query.get(0);
                ResolveInfo r1 = (ResolveInfo)query.get(1);
                if (r0.priority == r1.priority && r0.preferredOrder == r1.preferredOrder && r0.isDefault == r1.isDefault) {
                    ResolveInfo ri = this.findPreferredActivity(intent, resolvedType, flags, query, r0.priority);
                    if (ri != null) {
                        return ri;
                    }

                    return (ResolveInfo)query.get(0);
                }

                return (ResolveInfo)query.get(0);
            }
        }

        return null;
    }

    private ResolveInfo findPreferredActivity(Intent intent, String resolvedType, int flags, List<ResolveInfo> query, int priority) {
        return null;
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ComponentName comp = intent.getComponent();
        if (comp == null && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }

        if (comp != null) {
            List<ResolveInfo> list = new ArrayList(1);
            ActivityInfo ai = this.getActivityInfo(comp, flags, userId);
            if (ai != null) {
                ResolveInfo ri = new ResolveInfo();
                ri.activityInfo = ai;
                list.add(ri);
            }

            return list;
        } else {
            synchronized(this.mPackages) {
                String pkgName = intent.getPackage();
                if (pkgName == null) {
                    return this.mActivities.queryIntent(intent, resolvedType, flags, userId);
                } else {
                    VPackage pkg = (VPackage)this.mPackages.get(pkgName);
                    return pkg != null ? this.mActivities.queryIntentForPackage(intent, resolvedType, flags, pkg.activities, userId) : Collections.emptyList();
                }
            }
        }
    }

    public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ComponentName comp = intent.getComponent();
        if (comp == null && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }

        if (comp != null) {
            List<ResolveInfo> list = new ArrayList(1);
            ActivityInfo ai = this.getReceiverInfo(comp, flags, userId);
            if (ai != null) {
                ResolveInfo ri = new ResolveInfo();
                ri.activityInfo = ai;
                list.add(ri);
            }

            return list;
        } else {
            synchronized(this.mPackages) {
                String pkgName = intent.getPackage();
                if (pkgName == null) {
                    return this.mReceivers.queryIntent(intent, resolvedType, flags, userId);
                } else {
                    VPackage pkg = (VPackage)this.mPackages.get(pkgName);
                    return pkg != null ? this.mReceivers.queryIntentForPackage(intent, resolvedType, flags, pkg.receivers, userId) : Collections.emptyList();
                }
            }
        }
    }

    public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        List<ResolveInfo> query = this.queryIntentServices(intent, resolvedType, flags, userId);
        return query != null && query.size() >= 1 ? (ResolveInfo)query.get(0) : null;
    }

    public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ComponentName comp = intent.getComponent();
        if (comp == null && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }

        if (comp != null) {
            List<ResolveInfo> list = new ArrayList(1);
            ServiceInfo si = this.getServiceInfo(comp, flags, userId);
            if (si != null) {
                ResolveInfo ri = new ResolveInfo();
                ri.serviceInfo = si;
                list.add(ri);
            }

            return list;
        } else {
            synchronized(this.mPackages) {
                String pkgName = intent.getPackage();
                if (pkgName == null) {
                    return this.mServices.queryIntent(intent, resolvedType, flags, userId);
                } else {
                    VPackage pkg = (VPackage)this.mPackages.get(pkgName);
                    return pkg != null ? this.mServices.queryIntentForPackage(intent, resolvedType, flags, pkg.services, userId) : Collections.emptyList();
                }
            }
        }
    }

    @TargetApi(19)
    public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ComponentName comp = intent.getComponent();
        if (comp == null && intent.getSelector() != null) {
            intent = intent.getSelector();
            comp = intent.getComponent();
        }

        if (comp != null) {
            List<ResolveInfo> list = new ArrayList(1);
            ProviderInfo pi = this.getProviderInfo(comp, flags, userId);
            if (pi != null) {
                ResolveInfo ri = new ResolveInfo();
                ri.providerInfo = pi;
                list.add(ri);
            }

            return list;
        } else {
            synchronized(this.mPackages) {
                String pkgName = intent.getPackage();
                if (pkgName == null) {
                    return this.mProviders.queryIntent(intent, resolvedType, flags, userId);
                } else {
                    VPackage pkg = (VPackage)this.mPackages.get(pkgName);
                    return pkg != null ? this.mProviders.queryIntentForPackage(intent, resolvedType, flags, pkg.providers, userId) : Collections.emptyList();
                }
            }
        }
    }

    public VParceledListSlice<ProviderInfo> queryContentProviders(String processName, int vuid, int flags) {
        int userId = VUserHandle.getUserId(vuid);
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ArrayList<ProviderInfo> finalList = new ArrayList(3);
        synchronized(this.mPackages) {
            Iterator var7 = this.mProvidersByAuthority.values().iterator();

            while(true) {
                if (!var7.hasNext()) {
                    break;
                }

                VPackage.ProviderComponent p = (VPackage.ProviderComponent)var7.next();
                PackageSetting ps = (PackageSetting)p.owner.mExtras;
                if (ps.isEnabledAndMatchLPr(p.info, flags, userId) && (processName == null || ps.appId == VUserHandle.getAppId(vuid) && p.info.processName.equals(processName))) {
                    ProviderInfo providerInfo = PackageParserEx.generateProviderInfo(p, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                    finalList.add(providerInfo);
                }
            }
        }

        if (!finalList.isEmpty()) {
            Collections.sort(finalList, sProviderInitOrderSorter);
        }

        return new VParceledListSlice(finalList);
    }

    public VParceledListSlice<PackageInfo> getInstalledPackages(int flags, int userId) {
        this.checkUserId(userId);
        ArrayList<PackageInfo> pkgList = new ArrayList(this.mPackages.size());
        synchronized(this.mPackages) {
            Iterator var5 = this.mPackages.values().iterator();

            while(var5.hasNext()) {
                VPackage p = (VPackage)var5.next();
                PackageSetting ps = (PackageSetting)p.mExtras;
                PackageInfo info = this.generatePackageInfo(p, ps, flags, userId);
                if (info != null) {
                    pkgList.add(info);
                }
            }

            return new VParceledListSlice(pkgList);
        }
    }

    public VParceledListSlice<ApplicationInfo> getInstalledApplications(int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        ArrayList<ApplicationInfo> list = new ArrayList(this.mPackages.size());
        synchronized(this.mPackages) {
            Iterator var5 = this.mPackages.values().iterator();

            while(var5.hasNext()) {
                VPackage p = (VPackage)var5.next();
                PackageSetting ps = (PackageSetting)p.mExtras;
                ApplicationInfo info = PackageParserEx.generateApplicationInfo(p, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                if (info != null) {
                    list.add(info);
                }
            }

            return new VParceledListSlice(list);
        }
    }

    public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) {
        List<ReceiverInfo> list = new ArrayList();
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            if (p == null) {
                return Collections.emptyList();
            } else {
                PackageSetting ps = (PackageSetting)p.mExtras;
                Iterator var8 = p.receivers.iterator();

                while(true) {
                    VPackage.ActivityComponent receiver;
                    do {
                        do {
                            if (!var8.hasNext()) {
                                return list;
                            }

                            receiver = (VPackage.ActivityComponent)var8.next();
                        } while(!ps.isEnabledAndMatchLPr(receiver.info, 0, userId));
                    } while(!receiver.info.processName.equals(processName));

                    List<IntentFilter> filters = new ArrayList();
                    Iterator var11 = receiver.intents.iterator();

                    while(var11.hasNext()) {
                        VPackage.ActivityIntentInfo intentInfo = (VPackage.ActivityIntentInfo)var11.next();
                        filters.add(intentInfo.filter);
                    }

                    list.add(new ReceiverInfo(receiver.info, filters));
                }
            }
        }
    }

    public PermissionInfo getPermissionInfo(String name, int flags) {
        synchronized(this.mPackages) {
            VPackage.PermissionComponent p = (VPackage.PermissionComponent)this.mPermissions.get(name);
            return p != null ? new PermissionInfo(p.info) : null;
        }
    }

    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) {
        List<PermissionInfo> infos = new ArrayList();
        if (group != null) {
            synchronized(this.mPackages) {
                Iterator var5 = this.mPermissions.values().iterator();

                while(var5.hasNext()) {
                    VPackage.PermissionComponent p = (VPackage.PermissionComponent)var5.next();
                    if (p.info.group.equals(group)) {
                        infos.add(p.info);
                    }
                }
            }
        }

        return infos;
    }

    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) {
        synchronized(this.mPackages) {
            VPackage.PermissionGroupComponent p = (VPackage.PermissionGroupComponent)this.mPermissionGroups.get(name);
            return p != null ? new PermissionGroupInfo(p.info) : null;
        }
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
        synchronized(this.mPackages) {
            int N = this.mPermissionGroups.size();
            ArrayList<PermissionGroupInfo> out = new ArrayList(N);
            Iterator var5 = this.mPermissionGroups.values().iterator();

            while(var5.hasNext()) {
                VPackage.PermissionGroupComponent pg = (VPackage.PermissionGroupComponent)var5.next();
                out.add(new PermissionGroupInfo(pg.info));
            }

            return out;
        }
    }

    public ProviderInfo resolveContentProvider(String name, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        VPackage.ProviderComponent provider;
        synchronized(this.mProvidersByAuthority) {
            provider = (VPackage.ProviderComponent)this.mProvidersByAuthority.get(name);
        }

        if (provider != null) {
            PackageSetting ps = (PackageSetting)provider.owner.mExtras;
            ProviderInfo providerInfo = PackageParserEx.generateProviderInfo(provider, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
            if (providerInfo != null) {
                if (!ps.isEnabledAndMatchLPr(providerInfo, flags, userId)) {
                    return null;
                }

                ComponentFixer.fixComponentInfo(providerInfo);
                return providerInfo;
            }
        }

        return null;
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
        this.checkUserId(userId);
        flags = this.updateFlagsNought(flags);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                return PackageParserEx.generateApplicationInfo(p, flags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
            } else {
                return null;
            }
        }
    }

    public String[] getPackagesForUid(int uid) {
        int userId = VUserHandle.getUserId(uid);
        this.checkUserId(userId);
        synchronized(this) {
            List<String> pkgList = new ArrayList(2);
            Iterator var5 = this.mPackages.values().iterator();

            while(true) {
                VPackage p;
                PackageSetting settings;
                do {
                    if (!var5.hasNext()) {
                        if (pkgList.isEmpty()) {
                            VLog.e(TAG, "getPackagesForUid return an empty result.");
                            return null;
                        }

                        return (String[])pkgList.toArray(new String[0]);
                    }

                    p = (VPackage)var5.next();
                    settings = (PackageSetting)p.mExtras;
                } while(VUserHandle.getUid(userId, settings.appId) != uid && uid != 9001);

                pkgList.add(p.packageName);
            }
        }
    }

    public int getPackageUid(String packageName, int userId) {
        this.checkUserId(userId);
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            if (p != null) {
                PackageSetting ps = (PackageSetting)p.mExtras;
                return VUserHandle.getUid(userId, ps.appId);
            } else {
                return -1;
            }
        }
    }

    public String getNameForUid(int uid) {
        int appId = VUserHandle.getAppId(uid);
        synchronized(this.mPackages) {
            Iterator var4 = this.mPackages.values().iterator();

            PackageSetting ps;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                VPackage p = (VPackage)var4.next();
                ps = (PackageSetting)p.mExtras;
            } while(ps.appId != appId);

            return ps.packageName;
        }
    }

    public List<String> querySharedPackages(String packageName) {
        synchronized(this.mPackages) {
            VPackage p = (VPackage)this.mPackages.get(packageName);
            if (p != null && p.mSharedUserId != null) {
                ArrayList<String> list = new ArrayList();
                Iterator var5 = this.mPackages.values().iterator();

                while(var5.hasNext()) {
                    VPackage one = (VPackage)var5.next();
                    if (TextUtils.equals(one.mSharedUserId, p.mSharedUserId)) {
                        list.add(one.packageName);
                    }
                }

                return list;
            } else {
                return Collections.EMPTY_LIST;
            }
        }
    }

    public IBinder getPackageInstaller() {
        return VPackageInstallerService.get();
    }

    public void setComponentEnabledSetting(ComponentName component, int newState, int flags, int userId) {
        VLog.e(TAG, "setComponentEnabledSetting " + component + " newState: " + newState + " flags: " + flags);
        if (component != null) {
            this.checkUserId(userId);
            synchronized(this.mPackages) {
                ComponentStateManager.user(userId).set(component, newState);
            }
        }
    }

    public int getComponentEnabledSetting(ComponentName component, int userId) {
        if (component == null) {
            return 0;
        } else {
            this.checkUserId(userId);
            synchronized(this.mPackages) {
                return ComponentStateManager.user(userId).get(component);
            }
        }
    }

    void createNewUser(int userId, File userPath) {
        Iterator var3 = this.mPackages.values().iterator();

        while(var3.hasNext()) {
            VPackage p = (VPackage)var3.next();
            PackageSetting setting = (PackageSetting)p.mExtras;
            setting.modifyUserState(userId);
        }

    }

    void cleanUpUser(int userId) {
        Iterator var2 = this.mPackages.values().iterator();

        while(var2.hasNext()) {
            VPackage p = (VPackage)var2.next();
            PackageSetting ps = (PackageSetting)p.mExtras;
            ps.removeUser(userId);
        }

        ComponentStateManager.user(userId).clearAll();
    }

    private PermissionInfo findPermission(String permission) {
        synchronized(this.mPackages) {
            Iterator var3 = this.mPackages.values().iterator();

            while(true) {
                ArrayList permissions;
                do {
                    if (!var3.hasNext()) {
                        return null;
                    }

                    VPackage pkg = (VPackage)var3.next();
                    permissions = pkg.permissions;
                } while(permissions == null);

                Iterator var6 = permissions.iterator();

                while(var6.hasNext()) {
                    VPackage.PermissionComponent component = (VPackage.PermissionComponent)var6.next();
                    if (component.info != null && TextUtils.equals(permission, component.info.name)) {
                        return component.info;
                    }
                }
            }
        }
    }

    private boolean hasRequestedPermission(String permission, String packageName) {
        VPackage pkg;
        synchronized(this.mPackages) {
            pkg = (VPackage)this.mPackages.get(packageName);
        }

        return pkg != null && pkg.requestedPermissions != null ? pkg.requestedPermissions.contains(permission) : false;
    }

    public int checkPermission(boolean isExt, String permission, String pkgName, int userId) {
        if (!"android.permission.INTERACT_ACROSS_USERS".equals(permission) && !"android.permission.INTERACT_ACROSS_USERS_FULL".equals(permission)) {
            PermissionInfo permissionInfo = this.getPermissionInfo(permission, 0);
            return permissionInfo != null ? 0 : VirtualCore.getPM().checkPermission(permission, StubManifest.getStubPackageName(isExt));
        } else {
            return -1;
        }
    }

    public int checkSignatures(String pkg1, String pkg2) {
        if (TextUtils.equals(pkg1, pkg2)) {
            return 0;
        } else {
            PackageInfo pkgOne = this.getPackageInfo(pkg1, 64, 0);
            PackageInfo pkgTwo = this.getPackageInfo(pkg2, 64, 0);
            if (pkgOne == null) {
                try {
                    pkgOne = VirtualCore.get().getHostPackageManager().getPackageInfo(pkg1, 64L);
                } catch (PackageManager.NameNotFoundException var7) {
                    return -4;
                }
            }

            if (pkgTwo == null) {
                try {
                    pkgTwo = VirtualCore.get().getHostPackageManager().getPackageInfo(pkg2, 64L);
                } catch (PackageManager.NameNotFoundException var6) {
                    return -4;
                }
            }

            return SignaturesUtils.compareSignatures(pkgOne.signatures, pkgTwo.signatures);
        }
    }

    public int checkUidPermission(boolean isExt, String permission, int uid) {
        PermissionInfo info = this.getPermissionInfo(permission, 0);
        return info != null ? 0 : VirtualCore.getPM().checkPermission(permission, StubManifest.getStubPackageName(isExt));
    }

    private static final class ServiceIntentResolver extends IntentResolver<VPackage.ServiceIntentInfo, ResolveInfo> {
        private final HashMap<ComponentName, VPackage.ServiceComponent> mServices;
        private int mFlags;

        private ServiceIntentResolver() {
            this.mServices = new HashMap();
        }

        public List<ResolveInfo> queryIntent(Intent intent, String resolvedType, boolean defaultOnly, int userId) {
            this.mFlags = defaultOnly ? 65536 : 0;
            return super.queryIntent(intent, resolvedType, defaultOnly, userId);
        }

        public List<ResolveInfo> queryIntent(Intent intent, String resolvedType, int flags, int userId) {
            this.mFlags = flags;
            return super.queryIntent(intent, resolvedType, (flags & 65536) != 0, userId);
        }

        public List<ResolveInfo> queryIntentForPackage(Intent intent, String resolvedType, int flags, ArrayList<VPackage.ServiceComponent> packageServices, int userId) {
            if (packageServices == null) {
                return null;
            } else {
                this.mFlags = flags;
                boolean defaultOnly = (flags & 65536) != 0;
                int N = packageServices.size();
                ArrayList<VPackage.ServiceIntentInfo[]> listCut = new ArrayList(N);

                for(int i = 0; i < N; ++i) {
                    ArrayList<VPackage.ServiceIntentInfo> intentFilters = ((VPackage.ServiceComponent)packageServices.get(i)).intents;
                    if (intentFilters != null && intentFilters.size() > 0) {
                        VPackage.ServiceIntentInfo[] array = new VPackage.ServiceIntentInfo[intentFilters.size()];
                        intentFilters.toArray(array);
                        listCut.add(array);
                    }
                }

                return super.queryIntentFromList(intent, resolvedType, defaultOnly, listCut, userId);
            }
        }

        public final void addService(VPackage.ServiceComponent s) {
            this.mServices.put(s.getComponentName(), s);
            int NI = s.intents.size();

            for(int j = 0; j < NI; ++j) {
                VPackage.ServiceIntentInfo intent = (VPackage.ServiceIntentInfo)s.intents.get(j);
                this.addFilter(intent);
            }

        }

        public final void removeService(VPackage.ServiceComponent s) {
            this.mServices.remove(s.getComponentName());
            int NI = s.intents.size();

            for(int j = 0; j < NI; ++j) {
                VPackage.ServiceIntentInfo intent = (VPackage.ServiceIntentInfo)s.intents.get(j);
                this.removeFilter(intent);
            }

        }

        protected boolean allowFilterResult(VPackage.ServiceIntentInfo filter, List<ResolveInfo> dest) {
            ServiceInfo filterSi = filter.service.info;

            for(int i = dest.size() - 1; i >= 0; --i) {
                ServiceInfo destAi = ((ResolveInfo)dest.get(i)).serviceInfo;
                if (ObjectsCompat.equals(destAi.name, filterSi.name) && ObjectsCompat.equals(destAi.packageName, filterSi.packageName)) {
                    return false;
                }
            }

            return true;
        }

        protected VPackage.ServiceIntentInfo[] newArray(int size) {
            return new VPackage.ServiceIntentInfo[size];
        }

        protected boolean isFilterStopped(VPackage.ServiceIntentInfo filter) {
            return false;
        }

        protected boolean isPackageForFilter(String packageName, VPackage.ServiceIntentInfo info) {
            return packageName.equals(info.service.owner.packageName);
        }

        protected ResolveInfo newResult(VPackage.ServiceIntentInfo filter, int match, int userId) {
            VPackage.ServiceComponent service = filter.service;
            PackageSetting ps = (PackageSetting)service.owner.mExtras;
            if (!ps.isEnabledAndMatchLPr(service.info, this.mFlags, userId)) {
                return null;
            } else {
                ServiceInfo si = PackageParserEx.generateServiceInfo(service, this.mFlags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                if (si == null) {
                    return null;
                } else {
                    ResolveInfo res = new ResolveInfo();
                    res.serviceInfo = si;
                    if ((this.mFlags & 64) != 0) {
                        res.filter = filter.filter;
                    }

                    res.priority = filter.filter.getPriority();
                    res.preferredOrder = service.owner.mPreferredOrder;
                    res.match = match;
                    res.isDefault = filter.hasDefault;
                    res.labelRes = filter.labelRes;
                    res.nonLocalizedLabel = filter.nonLocalizedLabel;
                    res.icon = filter.icon;
                    return res;
                }
            }
        }

        protected void sortResults(List<ResolveInfo> results) {
            Collections.sort(results, VPackageManagerService.sResolvePrioritySorter);
        }

        protected void dumpFilter(PrintWriter out, String prefix, VPackage.ServiceIntentInfo filter) {
        }

        protected Object filterToLabel(VPackage.ServiceIntentInfo filter) {
            return filter.service;
        }

        protected void dumpFilterLabel(PrintWriter out, String prefix, Object label, int count) {
        }
    }

    private static final class ActivityIntentResolver extends IntentResolver<VPackage.ActivityIntentInfo, ResolveInfo> {
        private final HashMap<ComponentName, VPackage.ActivityComponent> mActivities;
        private int mFlags;

        private ActivityIntentResolver() {
            this.mActivities = new HashMap();
        }

        public List<ResolveInfo> queryIntent(Intent intent, String resolvedType, boolean defaultOnly, int userId) {
            this.mFlags = defaultOnly ? 65536 : 0;
            return super.queryIntent(intent, resolvedType, defaultOnly, userId);
        }

        List<ResolveInfo> queryIntent(Intent intent, String resolvedType, int flags, int userId) {
            this.mFlags = flags;
            return super.queryIntent(intent, resolvedType, (flags & 65536) != 0, userId);
        }

        List<ResolveInfo> queryIntentForPackage(Intent intent, String resolvedType, int flags, ArrayList<VPackage.ActivityComponent> packageActivities, int userId) {
            if (packageActivities == null) {
                return null;
            } else {
                this.mFlags = flags;
                boolean defaultOnly = (flags & 65536) != 0;
                int N = packageActivities.size();
                ArrayList<VPackage.ActivityIntentInfo[]> listCut = new ArrayList(N);

                for(int i = 0; i < N; ++i) {
                    ArrayList<VPackage.ActivityIntentInfo> intentFilters = ((VPackage.ActivityComponent)packageActivities.get(i)).intents;
                    if (intentFilters != null && intentFilters.size() > 0) {
                        VPackage.ActivityIntentInfo[] array = new VPackage.ActivityIntentInfo[intentFilters.size()];
                        intentFilters.toArray(array);
                        listCut.add(array);
                    }
                }

                return super.queryIntentFromList(intent, resolvedType, defaultOnly, listCut, userId);
            }
        }

        public final void addActivity(VPackage.ActivityComponent a, String type) {
            this.mActivities.put(a.getComponentName(), a);
            int NI = a.intents.size();

            for(int j = 0; j < NI; ++j) {
                VPackage.ActivityIntentInfo intent = (VPackage.ActivityIntentInfo)a.intents.get(j);
                if (intent.filter.getPriority() > 0 && "activity".equals(type)) {
                    intent.filter.setPriority(0);
                    Log.w("PackageManager", "Package " + a.info.applicationInfo.packageName + " has activity " + a.className + " with priority > 0, forcing to 0");
                }

                this.addFilter(intent);
            }

        }

        public final void removeActivity(VPackage.ActivityComponent a, String type) {
            this.mActivities.remove(a.getComponentName());
            int NI = a.intents.size();

            for(int j = 0; j < NI; ++j) {
                VPackage.ActivityIntentInfo intent = (VPackage.ActivityIntentInfo)a.intents.get(j);
                this.removeFilter(intent);
            }

        }

        protected boolean allowFilterResult(VPackage.ActivityIntentInfo filter, List<ResolveInfo> dest) {
            ActivityInfo filterAi = filter.activity.info;

            for(int i = dest.size() - 1; i >= 0; --i) {
                ActivityInfo destAi = ((ResolveInfo)dest.get(i)).activityInfo;
                if (ObjectsCompat.equals(destAi.name, filterAi.name) && ObjectsCompat.equals(destAi.packageName, filterAi.packageName)) {
                    return false;
                }
            }

            return true;
        }

        protected VPackage.ActivityIntentInfo[] newArray(int size) {
            return new VPackage.ActivityIntentInfo[size];
        }

        protected boolean isFilterStopped(VPackage.ActivityIntentInfo filter) {
            return false;
        }

        protected boolean isPackageForFilter(String packageName, VPackage.ActivityIntentInfo info) {
            return packageName.equals(info.activity.owner.packageName);
        }

        protected ResolveInfo newResult(VPackage.ActivityIntentInfo info, int match, int userId) {
            VPackage.ActivityComponent activity = info.activity;
            PackageSetting ps = (PackageSetting)activity.owner.mExtras;
            if (!ps.isEnabledAndMatchLPr(activity.info, this.mFlags, userId)) {
                return null;
            } else {
                ActivityInfo ai = PackageParserEx.generateActivityInfo(activity, this.mFlags, ps.readUserState(userId), userId, ps.isRunInExtProcess());
                if (ai == null) {
                    return null;
                } else {
                    ResolveInfo res = new ResolveInfo();
                    res.activityInfo = ai;
                    if ((this.mFlags & 64) != 0) {
                        res.filter = info.filter;
                    }

                    res.priority = info.filter.getPriority();
                    res.preferredOrder = activity.owner.mPreferredOrder;
                    res.match = match;
                    res.isDefault = info.hasDefault;
                    res.labelRes = info.labelRes;
                    res.nonLocalizedLabel = info.nonLocalizedLabel;
                    res.icon = info.icon;
                    return res;
                }
            }
        }

        protected void sortResults(List<ResolveInfo> results) {
            Collections.sort(results, VPackageManagerService.sResolvePrioritySorter);
        }

        protected void dumpFilter(PrintWriter out, String prefix, VPackage.ActivityIntentInfo filter) {
        }

        protected Object filterToLabel(VPackage.ActivityIntentInfo filter) {
            return filter.activity;
        }

        protected void dumpFilterLabel(PrintWriter out, String prefix, Object label, int count) {
        }
    }
}
