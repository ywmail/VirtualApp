/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.proxies.oem.vivo;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import mirror.oem.vivo.IPopupCameraManager;

public class PopupCameraManagerStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = "popup_camera_service";

    public PopupCameraManagerStub() {
        super(IPopupCameraManager.Stub.TYPE, "popup_camera_service");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("notifyCameraStatus"));
    }
}

