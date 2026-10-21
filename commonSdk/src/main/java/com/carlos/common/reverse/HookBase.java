/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.reverse;


import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import mirror.android.compat.Compatibility;

public class HookBase {
    public static void hook(ClassLoader classLoader) {
        try {
            try {
                Object default_callbacks = Compatibility.DEFAULT_CALLBACKS.get();
                Object sCallbacks = Compatibility.sCallbacks.get();
                if (sCallbacks != null) {
                    Compatibility.setBehaviorChangeDelegate.call(Compatibility.DEFAULT_CALLBACKS, new android.compat.Compatibility.BehaviorChangeDelegate(){

                        @Override
                        public boolean isChangeEnabled(long changeId) {
                            return false;
                        }
                    });
                }
            }
            catch (Exception e) {
                HVLog.printException(e);
            }
            XposedHelpers.findAndHookMethod("android.app.AppCompatCallbacks", classLoader, "isChangeEnabled", Long.TYPE, new XC_MethodHook(){

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.setResult(false);
                }
            });
            XposedHelpers.findAndHookMethod("android.app.PendingIntent", classLoader, "checkFlags", Integer.TYPE, String.class, new XC_MethodHook(){

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }

                @Override
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    int flags = (Integer)param.args[0];
                    String packageName = (String)param.args[1];
                    boolean flagImmutableSet = (flags & 0x4000000) != 0;
                    boolean flagMutableSet = (flags & 0x2000000) != 0;
                    HVLog.d("VA-", "flagImmutableSet:" + flagImmutableSet + "   flagMutableSet:" + flagMutableSet);
                }
            });
        }
        catch (Exception e) {
            HVLog.printThrowable(e);
        }
    }
}

