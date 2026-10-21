/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.core;

import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.hook.proxies.oem.vivo.PhysicalFlingManagerStub;
import com.lody.virtual.client.hook.proxies.oem.vivo.PopupCameraManagerStub;
import com.lody.virtual.client.hook.proxies.oem.vivo.SuperResolutionManagerStub;
import com.lody.virtual.client.hook.proxies.oem.vivo.SystemDefenceManagerStub;
import com.lody.virtual.client.hook.proxies.oem.vivo.VivoPermissionServiceStub;
import mirror.oem.vivo.IPhysicalFlingManagerStub;
import mirror.oem.vivo.IPopupCameraManager;
import mirror.oem.vivo.ISuperResolutionManager;
import mirror.oem.vivo.ISystemDefenceManager;
import mirror.oem.vivo.IVivoPermissonService;

public class OemInjectManager {
    public static void oemInject(InvocationStubManager stubManager) {
        OemInjectManager.injectVivo(stubManager);
    }

    private static void injectVivo(InvocationStubManager stubManager) {
        if (IPhysicalFlingManagerStub.TYPE != null) {
            stubManager.addInjector(new PhysicalFlingManagerStub());
        }
        if (IPopupCameraManager.TYPE != null) {
            stubManager.addInjector(new PopupCameraManagerStub());
        }
        if (ISuperResolutionManager.TYPE != null) {
            stubManager.addInjector(new SuperResolutionManagerStub());
        }
        if (ISystemDefenceManager.TYPE != null) {
            stubManager.addInjector(new SystemDefenceManagerStub());
        }
        if (IVivoPermissonService.TYPE != null) {
            stubManager.addInjector(new VivoPermissionServiceStub());
        }
    }
}

