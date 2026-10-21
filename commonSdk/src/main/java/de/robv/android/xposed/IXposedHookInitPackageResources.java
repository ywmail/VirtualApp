/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed;

import de.robv.android.xposed.IXposedMod;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public interface IXposedHookInitPackageResources
extends IXposedMod {
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam var1) throws Throwable;

    public static final class Wrapper
    extends XC_InitPackageResources {
        private final IXposedHookInitPackageResources instance;

        public Wrapper(IXposedHookInitPackageResources instance) {
            this.instance = instance;
        }

        @Override
        public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
            this.instance.handleInitPackageResources(resparam);
        }
    }
}

