/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.os.Binder
 *  android.os.Process
 */
package com.lody.virtual.client.fixer;

import android.content.ContentProvider;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Binder;
import android.os.Process;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.hook.proxies.graphics.GraphicsStatsStub;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.RefObjUtil;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import mirror.RefObject;
import mirror.android.app.ContextImpl;
import mirror.android.app.ContextImplKitkat;
import mirror.android.content.AttributionSource;
import mirror.android.content.AttributionSourceState;
import mirror.android.content.ContentResolverJBMR2;

public class ContextFixer {
    public static void fixContext(Context context, String appPkg) {
        block11: {
            try {
                context.getPackageName();
                int deep = 0;
                while (context instanceof ContextWrapper) {
                    context = ((ContextWrapper)context).getBaseContext();
                    if (++deep < 10) continue;
                    return;
                }
                ContextImpl.mPackageManager.set(context, null);
                try {
                    context.getPackageManager();
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
                if (!VirtualCore.get().isVAppProcess()) {
                    return;
                }
                InvocationStubManager stubManager = InvocationStubManager.getInstance();
                stubManager.checkEnv(GraphicsStatsStub.class);
                stubManager.checkEnv(ActivityManagerStub.class);
                if (appPkg == null) break block11;
                String hostPkg = VirtualCore.get().getHostPkg();
                ContextImpl.mBasePackageName.set(context, hostPkg);
                if (ContextImplKitkat.mOpPackageName == null) {
                    try {
                        ContextImplKitkat.mOpPackageName = new RefObject(ContextImplKitkat.TYPE, ContextImplKitkat.TYPE.getDeclaredField("mOpPackageName"));
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
                if (ContextImplKitkat.mOpPackageName != null) {
                    ContextImplKitkat.mOpPackageName.set(context, hostPkg);
                }
                ContentResolverJBMR2.mPackageName.set(context.getContentResolver(), appPkg);
                if (BuildCompat.isS()) {
                    ContextFixer.fixAttributionSource(ContextImpl.getAttributionSource(context));
                    ContextFixer.fixContentProvider();
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void fixAttributionSource(Object attributionSource) {
        Object mAttributionSourceState;
        if (attributionSource == null || (mAttributionSourceState = AttributionSource.mAttributionSourceState(attributionSource)) == null) {
            return;
        }
        AttributionSourceState.packageName(mAttributionSourceState, VirtualCore.get().getHostPkg());
        AttributionSourceState.uid(mAttributionSourceState, VirtualCore.get().myUid());
        RefObjUtil.setRefObjectValue(AttributionSourceState.pid, mAttributionSourceState, -1);
        ContextFixer.fixAttributionSource(AttributionSource.getNext.call(attributionSource, new Object[0]));
    }

    public static void fixAttributionSourceAsApp(Object attributionSource) {
        Object mAttributionSourceState;
        if (attributionSource == null || (mAttributionSourceState = AttributionSource.mAttributionSourceState(attributionSource)) == null) {
            return;
        }
        AttributionSourceState.packageName(mAttributionSourceState, VClient.get().getCurrentPackage());
        AttributionSourceState.uid(mAttributionSourceState, VirtualCore.get().myUid());
        RefObjUtil.setRefObjectValue(AttributionSourceState.pid, mAttributionSourceState, -1);
        ContextFixer.fixAttributionSourceAsApp(AttributionSource.getNext.call(attributionSource, new Object[0]));
    }

    private static void fixContentProvider() {
        try {
            XposedHelpers.findAndHookMethod(ContentProvider.class, "getCallingAttributionSource", new XC_MethodHook(){

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    int callingPid = Binder.getCallingPid();
                    int mypid = Process.myPid();
                    Object attributionSource = param.getResult();
                    if (callingPid == mypid) {
                        ContextFixer.fixAttributionSourceAsApp(attributionSource);
                    }
                }
            });
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            XposedHelpers.findAndHookMethod(ContentProvider.class, "getCallingPackage", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    int mypid;
                    int callingPid = Binder.getCallingPid();
                    if (callingPid == (mypid = Process.myPid())) {
                        param.setResult(VClient.get().getCurrentPackage());
                    }
                }
            });
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            XposedHelpers.findAndHookMethod(ContentProvider.class, "getCallingPackageUnchecked", new XC_MethodHook(){

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    int mypid;
                    int callingPid = Binder.getCallingPid();
                    if (callingPid == (mypid = Process.myPid())) {
                        param.setResult(VClient.get().getCurrentPackage());
                    }
                }
            });
        }
        catch (Throwable e3) {
            e3.printStackTrace();
        }
    }
}

