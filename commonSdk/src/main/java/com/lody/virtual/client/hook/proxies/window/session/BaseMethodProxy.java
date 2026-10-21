/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.Build$VERSION
 *  android.view.WindowManager$LayoutParams
 */
package com.lody.virtual.client.hook.proxies.window.session;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.WindowManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

class BaseMethodProxy
extends StaticMethodProxy {
    private boolean mDrawOverlays = false;

    public BaseMethodProxy(String name) {
        super(name);
    }

    protected boolean isDrawOverlays() {
        return this.mDrawOverlays;
    }

    @Override
    @SuppressLint(value={"SwitchIntDef"})
    public boolean beforeCall(Object who, Method method, Object ... args) {
        WindowManager.LayoutParams attrs;
        this.mDrawOverlays = false;
        int index = ArrayUtils.indexOfFirst(args, WindowManager.LayoutParams.class);
        if (index != -1 && (attrs = (WindowManager.LayoutParams)args[index]) != null) {
            attrs.packageName = BaseMethodProxy.getHostPkg();
            switch (attrs.type) {
                case 2002: 
                case 2003: 
                case 2006: 
                case 2007: 
                case 2010: 
                case 2038: {
                    this.mDrawOverlays = true;
                    break;
                }
            }
            if (Build.VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26 && this.mDrawOverlays) {
                attrs.type = 2038;
            }
        }
        return true;
    }
}

