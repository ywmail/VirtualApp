/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.AttributionSource
 *  android.os.IInterface
 */
package com.lody.virtual.client.hook.providers;

import android.content.AttributionSource;
import android.os.IInterface;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.providers.ProviderHook;
import java.lang.reflect.Method;

public class InternalProviderHook
extends ProviderHook {
    public InternalProviderHook(IInterface base) {
        super(base);
    }

    @Override
    public void processArgs(Method method, Object ... args) {
        if (args == null || args.length <= 0) {
            return;
        }
        if (args[0] instanceof AttributionSource) {
            ContextFixer.fixAttributionSource(args[0]);
        }
    }
}

