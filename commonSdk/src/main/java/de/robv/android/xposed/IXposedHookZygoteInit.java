/*
 * Decompiled with CFR 0.152.
 */
package de.robv.android.xposed;

import de.robv.android.xposed.IXposedMod;

public interface IXposedHookZygoteInit
extends IXposedMod {
    public void initZygote(StartupParam var1) throws Throwable;

    public static final class StartupParam {
        public String modulePath;
        public boolean startsSystemServer;

        StartupParam() {
        }
    }
}

