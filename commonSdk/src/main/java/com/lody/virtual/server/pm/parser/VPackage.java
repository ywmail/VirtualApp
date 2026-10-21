/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.ConfigurationInfo
 *  android.content.pm.FeatureInfo
 *  android.content.pm.InstrumentationInfo
 *  android.content.pm.PermissionGroupInfo
 *  android.content.pm.PermissionInfo
 *  android.content.pm.ProviderInfo
 *  android.content.pm.ServiceInfo
 *  android.content.pm.Signature
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.lody.virtual.server.pm.parser;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import mirror.android.content.pm.ApplicationInfoP;

public class VPackage
implements Parcelable {
    public static final Parcelable.Creator<VPackage> CREATOR = new Parcelable.Creator<VPackage>(){

        public VPackage createFromParcel(Parcel source) {
            return new VPackage(source);
        }

        public VPackage[] newArray(int size) {
            return new VPackage[size];
        }
    };
    public ArrayList<ActivityComponent> activities;
    public ArrayList<ActivityComponent> receivers;
    public ArrayList<ProviderComponent> providers;
    public ArrayList<ServiceComponent> services;
    public ArrayList<InstrumentationComponent> instrumentation;
    public ArrayList<PermissionComponent> permissions;
    public ArrayList<PermissionGroupComponent> permissionGroups;
    public ArrayList<String> requestedPermissions;
    public ArrayList<String> protectedBroadcasts;
    public ApplicationInfo applicationInfo;
    public Signature[] mSignatures;
    public PackageParser.SigningDetails mSigningDetails;
    public Bundle mAppMetaData;
    public String packageName;
    public int mPreferredOrder;
    public String mVersionName;
    public String mSharedUserId;
    public ArrayList<String> usesLibraries;
    public ArrayList<String> usesOptionalLibraries;
    public int mVersionCode;
    public int mSharedUserLabel;
    public ArrayList<ConfigurationInfo> configPreferences = null;
    public ArrayList<FeatureInfo> reqFeatures = null;
    public ArrayList<String> splitNames = null;
    public ArrayList<SharedLibraryInfo> sharedLibraryInfos = null;
    public Object mExtras;
    public XposedModule xposedModule;

    public VPackage() {
    }

    protected VPackage(Parcel in) {
        int N = in.readInt();
        this.activities = new ArrayList(N);
        while (N-- > 0) {
            this.activities.add(new ActivityComponent(in));
        }
        N = in.readInt();
        this.receivers = new ArrayList(N);
        while (N-- > 0) {
            this.receivers.add(new ActivityComponent(in));
        }
        N = in.readInt();
        this.providers = new ArrayList(N);
        while (N-- > 0) {
            this.providers.add(new ProviderComponent(in));
        }
        N = in.readInt();
        this.services = new ArrayList(N);
        while (N-- > 0) {
            this.services.add(new ServiceComponent(in));
        }
        N = in.readInt();
        this.instrumentation = new ArrayList(N);
        while (N-- > 0) {
            this.instrumentation.add(new InstrumentationComponent(in));
        }
        N = in.readInt();
        this.permissions = new ArrayList(N);
        while (N-- > 0) {
            this.permissions.add(new PermissionComponent(in));
        }
        N = in.readInt();
        this.permissionGroups = new ArrayList(N);
        while (N-- > 0) {
            this.permissionGroups.add(new PermissionGroupComponent(in));
        }
        this.requestedPermissions = in.createStringArrayList();
        this.protectedBroadcasts = in.createStringArrayList();
        this.applicationInfo = (ApplicationInfo)in.readParcelable(ApplicationInfo.class.getClassLoader());
        this.mAppMetaData = in.readBundle(Bundle.class.getClassLoader());
        this.packageName = in.readString();
        this.mPreferredOrder = in.readInt();
        this.mVersionName = in.readString();
        this.mSharedUserId = in.readString();
        this.usesLibraries = in.createStringArrayList();
        this.usesOptionalLibraries = in.createStringArrayList();
        this.mVersionCode = in.readInt();
        this.mSharedUserLabel = in.readInt();
        this.configPreferences = in.createTypedArrayList(ConfigurationInfo.CREATOR);
        this.reqFeatures = in.createTypedArrayList(FeatureInfo.CREATOR);
        this.splitNames = in.createStringArrayList();
        if (ApplicationInfoP.sharedLibraryInfos != null) {
            this.sharedLibraryInfos = in.createTypedArrayList(SharedLibraryInfo.CREATOR);
        }
        this.xposedModule = (XposedModule)in.readParcelable(XposedModule.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.activities.size());
        for (ActivityComponent activityComponent : this.activities) {
            dest.writeParcelable((Parcelable)activityComponent.info, 0);
            dest.writeString(activityComponent.className);
            dest.writeBundle(activityComponent.metaData);
            dest.writeInt(activityComponent.intents != null ? activityComponent.intents.size() : 0);
            if (activityComponent.intents == null) continue;
            for (IntentInfo info : activityComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.receivers.size());
        for (ActivityComponent activityComponent : this.receivers) {
            dest.writeParcelable((Parcelable)activityComponent.info, 0);
            dest.writeString(activityComponent.className);
            dest.writeBundle(activityComponent.metaData);
            dest.writeInt(activityComponent.intents != null ? activityComponent.intents.size() : 0);
            if (activityComponent.intents == null) continue;
            for (IntentInfo info : activityComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.providers.size());
        for (ProviderComponent providerComponent : this.providers) {
            dest.writeParcelable((Parcelable)providerComponent.info, 0);
            dest.writeString(providerComponent.className);
            dest.writeBundle(providerComponent.metaData);
            dest.writeInt(providerComponent.intents != null ? providerComponent.intents.size() : 0);
            if (providerComponent.intents == null) continue;
            for (IntentInfo info : providerComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.services.size());
        for (ServiceComponent serviceComponent : this.services) {
            dest.writeParcelable((Parcelable)serviceComponent.info, 0);
            dest.writeString(serviceComponent.className);
            dest.writeBundle(serviceComponent.metaData);
            dest.writeInt(serviceComponent.intents != null ? serviceComponent.intents.size() : 0);
            if (serviceComponent.intents == null) continue;
            for (IntentInfo info : serviceComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.instrumentation.size());
        for (InstrumentationComponent instrumentationComponent : this.instrumentation) {
            dest.writeParcelable((Parcelable)instrumentationComponent.info, 0);
            dest.writeString(instrumentationComponent.className);
            dest.writeBundle(instrumentationComponent.metaData);
            dest.writeInt(instrumentationComponent.intents != null ? instrumentationComponent.intents.size() : 0);
            if (instrumentationComponent.intents == null) continue;
            for (IntentInfo info : instrumentationComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.permissions.size());
        for (PermissionComponent permissionComponent : this.permissions) {
            dest.writeParcelable((Parcelable)permissionComponent.info, 0);
            dest.writeString(permissionComponent.className);
            dest.writeBundle(permissionComponent.metaData);
            dest.writeInt(permissionComponent.intents != null ? permissionComponent.intents.size() : 0);
            if (permissionComponent.intents == null) continue;
            for (IntentInfo info : permissionComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeInt(this.permissionGroups.size());
        for (PermissionGroupComponent permissionGroupComponent : this.permissionGroups) {
            dest.writeParcelable((Parcelable)permissionGroupComponent.info, 0);
            dest.writeString(permissionGroupComponent.className);
            dest.writeBundle(permissionGroupComponent.metaData);
            dest.writeInt(permissionGroupComponent.intents != null ? permissionGroupComponent.intents.size() : 0);
            if (permissionGroupComponent.intents == null) continue;
            for (IntentInfo info : permissionGroupComponent.intents) {
                info.writeToParcel(dest, flags);
            }
        }
        dest.writeStringList(this.requestedPermissions);
        dest.writeStringList(this.protectedBroadcasts);
        dest.writeParcelable((Parcelable)this.applicationInfo, flags);
        dest.writeBundle(this.mAppMetaData);
        dest.writeString(this.packageName);
        dest.writeInt(this.mPreferredOrder);
        dest.writeString(this.mVersionName);
        dest.writeString(this.mSharedUserId);
        dest.writeStringList(this.usesLibraries);
        dest.writeStringList(this.usesOptionalLibraries);
        dest.writeInt(this.mVersionCode);
        dest.writeInt(this.mSharedUserLabel);
        dest.writeTypedList(this.configPreferences);
        dest.writeTypedList(this.reqFeatures);
        dest.writeStringList(this.splitNames);
        if (ApplicationInfoP.sharedLibraryInfos != null) {
            dest.writeTypedList(this.sharedLibraryInfos);
        }
        dest.writeParcelable((Parcelable)this.xposedModule, flags);
    }

    public static class XposedModule
    implements Parcelable {
        public String desc;
        public int minVersion;
        public static final Parcelable.Creator<XposedModule> CREATOR = new Parcelable.Creator<XposedModule>(){

            public XposedModule createFromParcel(Parcel in) {
                return new XposedModule(in);
            }

            public XposedModule[] newArray(int size) {
                return new XposedModule[size];
            }
        };

        public XposedModule() {
        }

        protected XposedModule(Parcel in) {
            this.desc = in.readString();
            this.minVersion = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.desc);
            dest.writeInt(this.minVersion);
        }
    }

    public static class PermissionGroupComponent
    extends Component<IntentInfo> {
        public PermissionGroupInfo info;

        public PermissionGroupComponent(PackageParser.PermissionGroup p) {
            super(p);
            this.info = p.info;
        }

        protected PermissionGroupComponent(Parcel src) {
            this.info = (PermissionGroupInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new IntentInfo(src));
            }
        }
    }

    public static class PermissionComponent
    extends Component<IntentInfo> {
        public PermissionInfo info;

        public PermissionComponent(PackageParser.Permission p) {
            super(p);
            this.info = p.info;
        }

        protected PermissionComponent(Parcel src) {
            this.info = (PermissionInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new IntentInfo(src));
            }
        }
    }

    public static class InstrumentationComponent
    extends Component<IntentInfo> {
        public InstrumentationInfo info;

        public InstrumentationComponent(PackageParser.Instrumentation i) {
            super(i);
            this.info = i.info;
        }

        protected InstrumentationComponent(Parcel src) {
            this.info = (InstrumentationInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new IntentInfo(src));
            }
        }
    }

    public static class ProviderComponent
    extends Component<ProviderIntentInfo> {
        public ProviderInfo info;

        public ProviderComponent(PackageParser.Provider provider) {
            super(provider);
            if (provider.intents != null) {
                this.intents = new ArrayList(provider.intents.size());
                for (Object o : provider.intents) {
                    this.intents.add(new ProviderIntentInfo((PackageParser.IntentInfo)((Object)o)));
                }
            }
            this.info = provider.info;
        }

        protected ProviderComponent(Parcel src) {
            this.info = (ProviderInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new ProviderIntentInfo(src));
            }
        }
    }

    public static class ServiceComponent
    extends Component<ServiceIntentInfo> {
        public ServiceInfo info;

        public ServiceComponent(PackageParser.Service service) {
            super(service);
            if (service.intents != null) {
                this.intents = new ArrayList(service.intents.size());
                for (Object o : service.intents) {
                    this.intents.add(new ServiceIntentInfo((PackageParser.IntentInfo)((Object)o)));
                }
            }
            this.info = service.info;
        }

        protected ServiceComponent(Parcel src) {
            this.info = (ServiceInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new ServiceIntentInfo(src));
            }
        }
    }

    public static class ActivityComponent
    extends Component<ActivityIntentInfo> {
        public ActivityInfo info;

        public ActivityComponent(PackageParser.Activity activity) {
            super(activity);
            if (activity.intents != null) {
                this.intents = new ArrayList(activity.intents.size());
                for (Object o : activity.intents) {
                    this.intents.add(new ActivityIntentInfo((PackageParser.IntentInfo)((Object)o)));
                }
            }
            this.info = activity.info;
        }

        protected ActivityComponent(Parcel src) {
            this.info = (ActivityInfo)src.readParcelable(ActivityInfo.class.getClassLoader());
            this.className = src.readString();
            this.metaData = src.readBundle(Bundle.class.getClassLoader());
            int N = src.readInt();
            this.intents = new ArrayList(N);
            while (N-- > 0) {
                this.intents.add(new ActivityIntentInfo(src));
            }
        }
    }

    public static class Component<II extends IntentInfo> {
        public VPackage owner;
        public ArrayList<II> intents;
        public String className;
        public Bundle metaData;
        private ComponentName componentName;

        protected Component() {
        }

        public Component(PackageParser.Component component) {
            this.className = component.className;
            this.metaData = component.metaData;
        }

        public ComponentName getComponentName() {
            if (this.componentName != null) {
                return this.componentName;
            }
            if (this.className != null) {
                this.componentName = new ComponentName(this.owner.packageName, this.className);
            }
            return this.componentName;
        }
    }

    public static class IntentInfo
    implements Parcelable {
        public static final Parcelable.Creator<IntentInfo> CREATOR = new Parcelable.Creator<IntentInfo>(){

            public IntentInfo createFromParcel(Parcel source) {
                return new IntentInfo(source);
            }

            public IntentInfo[] newArray(int size) {
                return new IntentInfo[size];
            }
        };
        public IntentFilter filter;
        public boolean hasDefault;
        public int labelRes;
        public String nonLocalizedLabel;
        public int icon;
        public int logo;
        public int banner;

        public IntentInfo(PackageParser.IntentInfo info) {
            this.filter = info;
            this.hasDefault = info.hasDefault;
            this.labelRes = info.labelRes;
            if (info.nonLocalizedLabel != null) {
                this.nonLocalizedLabel = info.nonLocalizedLabel.toString();
            }
            this.icon = info.icon;
            this.logo = info.logo;
            this.banner = info.banner;
        }

        protected IntentInfo(Parcel in) {
            this.filter = (IntentFilter)in.readParcelable(VPackage.class.getClassLoader());
            this.hasDefault = in.readByte() != 0;
            this.labelRes = in.readInt();
            this.nonLocalizedLabel = in.readString();
            this.icon = in.readInt();
            this.logo = in.readInt();
            this.banner = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable((Parcelable)this.filter, flags);
            dest.writeByte(this.hasDefault ? (byte)1 : 0);
            dest.writeInt(this.labelRes);
            dest.writeString(this.nonLocalizedLabel);
            dest.writeInt(this.icon);
            dest.writeInt(this.logo);
            dest.writeInt(this.banner);
        }
    }

    public static class ProviderIntentInfo
    extends IntentInfo {
        public ProviderComponent provider;

        public ProviderIntentInfo(PackageParser.IntentInfo info) {
            super(info);
        }

        protected ProviderIntentInfo(Parcel in) {
            super(in);
        }
    }

    public static class ServiceIntentInfo
    extends IntentInfo {
        public ServiceComponent service;

        public ServiceIntentInfo(PackageParser.IntentInfo info) {
            super(info);
        }

        protected ServiceIntentInfo(Parcel in) {
            super(in);
        }
    }

    public static class ActivityIntentInfo
    extends IntentInfo {
        public ActivityComponent activity;

        public ActivityIntentInfo(PackageParser.IntentInfo info) {
            super(info);
        }

        protected ActivityIntentInfo(Parcel in) {
            super(in);
        }
    }
}

