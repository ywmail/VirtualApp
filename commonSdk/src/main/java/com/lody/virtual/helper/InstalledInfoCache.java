/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.lody.virtual.helper;

import android.graphics.drawable.Drawable;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.utils.ACache;
import java.io.Serializable;

public class InstalledInfoCache {
    private static ACache diskCache = ACache.get(VirtualCore.get().getContext(), "AppInfoCache");

    public static void save(CacheItem cacheItem) {
        diskCache.put("va_installed_info_cache@" + cacheItem.packageName, cacheItem);
        cacheItem.saveIcon();
    }

    public static CacheItem get(String packageName) {
        return (CacheItem)diskCache.getAsObject("va_installed_info_cache@" + packageName);
    }

    @Keep
    public static class CacheItem
    implements Serializable {
        private static final long serialVersionUID = 1L;
        public static final transient String INFO_CACHE_PREFIX = "va_installed_info_cache@";
        public static final transient String ICON_CACHE_PREFIX = "va_installed_icon_cache@";
        public String packageName;
        public String label;
        public transient Drawable icon;

        public CacheItem(String packageName, String label, Drawable icon) {
            this.packageName = packageName;
            this.label = label;
            this.icon = icon;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public String getLabel() {
            return this.label;
        }

        public synchronized void saveIcon() {
            if (this.icon != null) {
                diskCache.put(ICON_CACHE_PREFIX + this.packageName, this.icon);
            }
        }

        public synchronized Drawable getIcon() {
            if (this.icon == null) {
                this.icon = diskCache.getAsDrawable(ICON_CACHE_PREFIX + this.packageName);
            }
            return this.icon;
        }
    }
}

