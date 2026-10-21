/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package com.lody.virtual.client.hook.proxies.telephony;

import android.os.Build;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSequencePkgMethodProxy;
import java.lang.reflect.Method;
import mirror.com.android.internal.telephony.ITelephonyRegistry;

public class TelephonyRegistryStub
extends BinderInvocationProxy {
    public TelephonyRegistryStub() {
        super(ITelephonyRegistry.Stub.asInterface, "telephony.registry");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addOnSubscriptionsChangedListener"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("addOnOpportunisticSubscriptionsChangedListener"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeOnSubscriptionsChangedListener"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("listen"));
        this.addMethodProxy(new ReplaceCallingPkgMethodProxy("listenWithEventList"));
        this.addMethodProxy(new ReplaceSequencePkgMethodProxy("listenForSubscriber", 1){

            @Override
            public boolean beforeCall(Object who, Method method, Object ... args) {
                if (Build.VERSION.SDK_INT >= 17 && isFakeLocationEnable()) {
                    for (int i = args.length - 1; i > 0; --i) {
                        if (!(args[i] instanceof Integer)) continue;
                        int events = (Integer)args[i];
                        events ^= 0x400;
                        args[i] = events ^= 0x10;
                        break;
                    }
                }
                return super.beforeCall(who, method, args);
            }
        });
    }
}

