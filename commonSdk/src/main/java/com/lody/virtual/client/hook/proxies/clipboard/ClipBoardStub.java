/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ClipboardManager
 *  android.os.Build$VERSION
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.proxies.clipboard;

import android.content.ClipboardManager;
import android.os.Build;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import mirror.android.content.ClipboardManagerOreo;

public class ClipBoardStub
extends BinderInvocationProxy {
    public ClipBoardStub() {
        super(ClipBoardStub.getInterface(), "clipboard");
    }

    private static IInterface getInterface() {
        if (mirror.android.content.ClipboardManager.getService != null) {
            return mirror.android.content.ClipboardManager.getService.call(new Object[0]);
        }
        if (ClipboardManagerOreo.mService != null) {
            ClipboardManager cm = (ClipboardManager)VirtualCore.get().getContext().getSystemService("clipboard");
            return ClipboardManagerOreo.mService.get(cm);
        }
        if (ClipboardManagerOreo.sService != null) {
            return ClipboardManagerOreo.sService.get();
        }
        return null;
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getPrimaryClip"));
        if (Build.VERSION.SDK_INT > 17) {
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("setPrimaryClip"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("getPrimaryClipDescription"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("hasPrimaryClip"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("addPrimaryClipChangedListener"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("removePrimaryClipChangedListener"));
            this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy("hasClipboardText"));
        }
    }

    @Override
    public void inject() throws Throwable {
        super.inject();
        if (ClipboardManagerOreo.mService != null) {
            ClipboardManager cm = (ClipboardManager)VirtualCore.get().getContext().getSystemService("clipboard");
            ClipboardManagerOreo.mService.set(cm, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        } else if (ClipboardManagerOreo.sService != null) {
            ClipboardManagerOreo.sService.set((IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
        }
    }
}

