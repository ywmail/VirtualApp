/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.pm.ApplicationInfo
 */
package de.robv.android.xposed.callbacks;

import android.content.pm.ApplicationInfo;
import com.swift.sandhook.xposedcompat.XposedCompat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XCallback;

public abstract class XC_LoadPackage
extends XCallback
implements IXposedHookLoadPackage {
    public XC_LoadPackage() {
    }

    public XC_LoadPackage(int priority) {
        super(priority);
    }

    @Override
    protected void call(XCallback.Param param) throws Throwable {
        if (param instanceof LoadPackageParam) {
            this.handleLoadPackage((LoadPackageParam)param);
        }
    }

    public static final class LoadPackageParam
    extends XCallback.Param {
        public String packageName = XposedCompat.packageName;
        public String processName = XposedCompat.processName;
        public ClassLoader classLoader = XposedCompat.classLoader;
        public ApplicationInfo appInfo = XposedCompat.context.getApplicationInfo();
        public boolean isFirstApplication = XposedCompat.isFirstApplication;

        public LoadPackageParam(XposedBridge.CopyOnWriteSortedSet<XC_LoadPackage> callbacks) {
            super(callbacks);
        }
    }
}

