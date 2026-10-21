/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.IInterface
 *  android.text.TextUtils
 */
package com.lody.virtual.client.hook.proxies.bluetooth;

import android.os.Build;
import android.os.IInterface;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.FixAttributionSourceMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.ResultBinderMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.remote.VDeviceConfig;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import mirror.android.bluetooth.IBluetooth;

public class BluetoothStub
extends BinderInvocationProxy {
    private static final String SERVER_NAME = Build.VERSION.SDK_INT >= 17 ? "bluetooth_manager" : "bluetooth";

    public BluetoothStub() {
        super(IBluetooth.Stub.asInterface, SERVER_NAME);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new GetAddress());
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getSystemConfigEnabledProfilesForPackage"));
        if (BuildCompat.isS()) {
            this.addMethodProxy(new FixAttributionSourceMethodProxy("enable"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("enableNoAutoConnect"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("disable"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("getAddress"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("getName"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("onFactoryReset"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("enableBle"));
            this.addMethodProxy(new FixAttributionSourceMethodProxy("disableBle"));
        } else {
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enable"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disable"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableNoAutoConnect"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("updateBleAppCount"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableBle"));
            this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disableBle"));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            this.addMethodProxy(new ResultBinderMethodProxy("registerAdapter"){

                @Override
                public InvocationHandler createProxy(final IInterface base) {
                    return new InvocationHandler(){

                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("getAddress".equals(method.getName())) {
                                String mac = getDeviceConfig().bluetoothMac;
                                VDeviceConfig config = getDeviceConfig();
                                if (config.enable && !TextUtils.isEmpty((CharSequence)(getDeviceConfig().bluetoothMac))) {
                                    return mac;
                                }
                            }
                            return method.invoke(base, args);
                        }
                    };
                }
            });
        }
    }

    private static class GetAddress
    extends ReplaceLastPkgMethodProxy {
        public GetAddress() {
            super("getAddress");
        }

        @Override
        public Object call(Object who, Method method, Object ... args) throws Throwable {
            String mac;
            VDeviceConfig config = GetAddress.getDeviceConfig();
            if (config.enable && !TextUtils.isEmpty((CharSequence)(mac = GetAddress.getDeviceConfig().bluetoothMac))) {
                return mac;
            }
            return super.call(who, method, args);
        }
    }
}

