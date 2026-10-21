/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SyncAdapterType
 *  android.content.pm.ComponentInfo
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.util.AttributeSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 */
package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.server.accounts.RegisteredServicesParser;
import com.lody.virtual.server.pm.VPackageManagerService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mirror.android.content.SyncAdapterType;
import mirror.android.content.SyncAdapterTypeN;
import mirror.com.android.internal.R_Hide;
import org.xmlpull.v1.XmlPullParser;

public class SyncAdaptersCache {
    private Context mContext;
    private final Map<String, SyncAdapterInfo> mSyncAdapterInfos = new HashMap<String, SyncAdapterInfo>();

    public SyncAdaptersCache(Context context) {
        this.mContext = context;
    }

    public void refreshServiceCache(String packageName) {
        Intent intent = new Intent("android.content.SyncAdapter");
        if (packageName != null) {
            intent.setPackage(packageName);
        }
        this.generateServicesMap(VPackageManagerService.get().queryIntentServices(intent, null, 128, 0), this.mSyncAdapterInfos, new RegisteredServicesParser());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SyncAdapterInfo getServiceInfo(Account account, String providerName) {
        Map<String, SyncAdapterInfo> map = this.mSyncAdapterInfos;
        synchronized (map) {
            return this.mSyncAdapterInfos.get(account.type + "/" + providerName);
        }
    }

    public Collection<SyncAdapterInfo> getAllServices() {
        return this.mSyncAdapterInfos.values();
    }

    private void generateServicesMap(List<ResolveInfo> services, Map<String, SyncAdapterInfo> map, RegisteredServicesParser accountParser) {
        for (ResolveInfo info : services) {
            XmlResourceParser parser = accountParser.getParser(this.mContext, info.serviceInfo, "android.content.SyncAdapter");
            if (parser == null) continue;
            try {
                android.content.SyncAdapterType adapterType;
                int type;
                AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)parser);
                while ((type = parser.next()) != 1 && type != 2) {
                }
                if (!"sync-adapter".equals(parser.getName()) || (adapterType = this.parseSyncAdapterType(accountParser.getResources(this.mContext, info.serviceInfo.applicationInfo), attributeSet)) == null) continue;
                String key = adapterType.accountType + "/" + adapterType.authority;
                map.put(key, new SyncAdapterInfo(adapterType, info.serviceInfo));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private android.content.SyncAdapterType parseSyncAdapterType(Resources res, AttributeSet set) {
        TypedArray obtainAttributes = res.obtainAttributes(set, R_Hide.styleable.SyncAdapter.get());
        try {
            String contentAuthority = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_contentAuthority.get());
            String accountType = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_accountType.get());
            if (contentAuthority == null || accountType == null) {
                obtainAttributes.recycle();
                return null;
            }
            boolean userVisible = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_userVisible.get(), true);
            boolean supportsUploading = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_supportsUploading.get(), true);
            boolean isAlwaysSyncable = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_isAlwaysSyncable.get(), true);
            boolean allowParallelSyncs = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_allowParallelSyncs.get(), true);
            String settingsActivity = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_settingsActivity.get());
            if (SyncAdapterTypeN.ctor != null) {
                android.content.SyncAdapterType type = SyncAdapterTypeN.ctor.newInstance(contentAuthority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity, null);
                obtainAttributes.recycle();
                return type;
            }
            android.content.SyncAdapterType type = SyncAdapterType.ctor.newInstance(contentAuthority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity);
            obtainAttributes.recycle();
            return type;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class SyncAdapterInfo {
        public android.content.SyncAdapterType type;
        public ServiceInfo serviceInfo;
        public ComponentName componentName;

        SyncAdapterInfo(android.content.SyncAdapterType adapterType, ServiceInfo serviceInfo) {
            this.type = adapterType;
            this.serviceInfo = serviceInfo;
            this.componentName = ComponentUtils.toComponentName((ComponentInfo)serviceInfo);
        }
    }
}

