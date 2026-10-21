/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed;

import de.robv.android.xposed.IXposedMod;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface IXposedHookLoadPackage
extends IXposedMod {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam var1) throws Throwable;

    public static final class Wrapper
    extends XC_LoadPackage {
        private final IXposedHookLoadPackage instance;

        public Wrapper(IXposedHookLoadPackage instance) {
            this.instance = instance;
        }

        @Override
        public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            this.instance.handleLoadPackage(lpparam);
        }
    }
}

