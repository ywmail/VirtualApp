/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.wallpaper;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import mirror.android.app.IWallpaperManager;

public class WallpaperManagerStub
extends BinderInvocationProxy {
    public WallpaperManagerStub() {
        super(IWallpaperManager.Stub.asInterface, "wallpaper");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getWallpaper"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setWallpaper"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setDimensionHints"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setDisplayPadding"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("clearWallpaper"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setWallpaperComponentChecked"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isWallpaperSupported"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("isSetWallpaperAllowed"));
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
    }
}

