/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Messenger
 */
package com.lody.virtual.client.hook.proxies.system;

import android.net.wifi.IWifiScanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Messenger;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import java.util.ArrayList;
import mirror.android.net.wifi.WifiScanner;
import mirror.android.os.ServiceManager;

public class WifiScannerStub
extends BinderInvocationProxy {
    private static final String SERVICE_NAME = "wifiscanner";

    public WifiScannerStub() {
        super(new EmptyWifiScannerImpl(), "wifiscanner");
    }

    @Override
    public void inject() throws Throwable {
        if (ServiceManager.checkService.call(SERVICE_NAME) == null) {
            super.inject();
        }
    }

    static class EmptyWifiScannerImpl
    extends IWifiScanner.Stub {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        EmptyWifiScannerImpl() {
        }

        @Override
        public Messenger getMessenger() {
            return new Messenger(this.mHandler);
        }

        @Override
        public Bundle getAvailableChannels(int band) {
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList(WifiScanner.GET_AVAILABLE_CHANNELS_EXTRA.get(), new ArrayList(0));
            return bundle;
        }
    }
}

