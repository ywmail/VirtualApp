/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.util.SparseArray
 */
package com.lody.virtual.server.am;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.SparseArray;
import com.lody.virtual.client.core.VirtualCore;
import java.util.HashMap;
import java.util.WeakHashMap;

public final class AttributeCache {
    private static final AttributeCache sInstance = new AttributeCache();
    private final WeakHashMap<String, Package> mPackages = new WeakHashMap();
    private final Configuration mConfiguration = new Configuration();

    private AttributeCache() {
    }

    public static AttributeCache instance() {
        return sInstance;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removePackage(String packageName) {
        AttributeCache attributeCache = this;
        synchronized (attributeCache) {
            this.mPackages.remove(packageName);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void updateConfiguration(Configuration config) {
        AttributeCache attributeCache = this;
        synchronized (attributeCache) {
            int changes = this.mConfiguration.updateFrom(config);
            if ((changes & 0xBFFFFF5F) != 0) {
                this.mPackages.clear();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Entry get(String packageName, int resId, int[] styleable2) {
        AttributeCache attributeCache = this;
        synchronized (attributeCache) {
            Entry ent;
            Package pkg = this.mPackages.get(packageName);
            HashMap<int[], Entry> map = null;
            if (pkg != null) {
                map = (HashMap<int[], Entry>)pkg.mMap.get(resId);
                if (map != null && (ent = (Entry)map.get(styleable2)) != null) {
                    return ent;
                }
            } else {
                Resources res;
                try {
                    res = VirtualCore.get().getResources(packageName);
                }
                catch (Throwable e) {
                    return null;
                }
                pkg = new Package(res);
                this.mPackages.put(packageName, pkg);
            }
            if (map == null) {
                map = new HashMap<int[], Entry>();
                pkg.mMap.put(resId, map);
            }
            try {
                ent = new Entry(pkg.resources, pkg.resources.newTheme().obtainStyledAttributes(resId, styleable2));
                map.put(styleable2, ent);
            }
            catch (Resources.NotFoundException e) {
                return null;
            }
            return ent;
        }
    }

    public static final class Entry {
        public final Resources resource;
        public final TypedArray array;

        public Entry(Resources res, TypedArray ta) {
            this.resource = res;
            this.array = ta;
        }
    }

    public static final class Package {
        public final Resources resources;
        private final SparseArray<HashMap<int[], Entry>> mMap = new SparseArray();

        public Package(Resources res) {
            this.resources = res;
        }
    }
}

