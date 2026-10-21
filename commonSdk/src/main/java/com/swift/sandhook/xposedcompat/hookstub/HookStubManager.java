//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.swift.sandhook.xposedcompat.hookstub;

import android.util.Log;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookMethodResolver;
import com.swift.sandhook.utils.ParamWrapper;
import com.swift.sandhook.wrapper.StubMethodsFactory;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;

public class HookStubManager {
    public static volatile boolean is64Bit = SandHook.is64Bit();
    public static final int MAX_64_ARGS = 7;
    public static int MAX_STUB_ARGS = 0;
    public static int[] stubSizes;
    public static boolean hasStubBackup;
    public static AtomicInteger[] curUseStubIndexes;
    public static int ALL_STUB = 0;
    public static Member[] originMethods;
    public static HookMethodEntity[] hookMethodEntities;
    public static XposedBridge.AdditionalHookInfo[] additionalHookInfos;

    public HookStubManager() {
    }

    public static HookMethodEntity getHookMethodEntity(Member origin, XposedBridge.AdditionalHookInfo additionalHookInfo) {
        if (!support()) {
            return null;
        } else {
            boolean isStatic = Modifier.isStatic(origin.getModifiers());
            Class[] parType;
            Class retType;
            if (origin instanceof Method) {
                Method method = (Method)origin;
                retType = method.getReturnType();
                parType = method.getParameterTypes();
            } else {
                if (!(origin instanceof Constructor)) {
                    return null;
                }

                Constructor constructor = (Constructor)origin;
                retType = Void.TYPE;
                parType = constructor.getParameterTypes();
            }

            if (!ParamWrapper.support(retType)) {
                return null;
            } else {
                int needStubArgCount = isStatic ? 0 : 1;
                if (parType != null) {
                    needStubArgCount += parType.length;
                    if (needStubArgCount > MAX_STUB_ARGS) {
                        return null;
                    }

                    if (is64Bit && needStubArgCount > 7) {
                        return null;
                    }

                    Class[] var6 = parType;
                    int var7 = parType.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Class par = var6[var8];
                        if (!ParamWrapper.support(par)) {
                            return null;
                        }
                    }
                } else {
                    parType = new Class[0];
                }

                Class var14 = HookStubManager.class;
                synchronized(HookStubManager.class) {
                    StubMethodsInfo stubMethodInfo = getStubMethodPair(is64Bit, needStubArgCount);
                    if (stubMethodInfo == null) {
                        return null;
                    } else {
                        HookMethodEntity entity = new HookMethodEntity(origin, stubMethodInfo.hook, stubMethodInfo.backup);
                        entity.retType = retType;
                        entity.parType = parType;
                        if (hasStubBackup && !tryCompileAndResolveCallOriginMethod(entity.backup, stubMethodInfo.args, stubMethodInfo.index)) {
                            DexLog.w("internal stub <" + entity.hook.getName() + "> call origin compile failure, skip use internal stub");
                            return null;
                        } else {
                            int id = getMethodId(stubMethodInfo.args, stubMethodInfo.index);
                            originMethods[id] = origin;
                            hookMethodEntities[id] = entity;
                            additionalHookInfos[id] = additionalHookInfo;
                            return entity;
                        }
                    }
                }
            }
        }
    }

    public static int getMethodId(int args, int index) {
        int id = index;

        for(int i = 0; i < args; ++i) {
            id += stubSizes[i];
        }

        return id;
    }

    public static String getHookMethodName(int index) {
        return "stub_hook_" + index;
    }

    public static String getBackupMethodName(int index) {
        return "stub_backup_" + index;
    }

    public static String getCallOriginClassName(int args, int index) {
        return "call_origin_" + args + "_" + index;
    }

    private static synchronized StubMethodsInfo getStubMethodPair(boolean is64Bit, int stubArgs) {
        stubArgs = getMatchStubArgsCount(stubArgs);
        if (stubArgs < 0) {
            return null;
        } else {
            int curUseStubIndex = curUseStubIndexes[stubArgs].getAndIncrement();
            Class[] pars = getFindMethodParTypes(is64Bit, stubArgs);

            try {
                Method hook;
                Method backup;
                if (is64Bit) {
                    hook = MethodHookerStubs64.class.getDeclaredMethod(getHookMethodName(curUseStubIndex), pars);
                    backup = hasStubBackup ? MethodHookerStubs64.class.getDeclaredMethod(getBackupMethodName(curUseStubIndex), pars) : StubMethodsFactory.getStubMethod();
                    return hook != null && backup != null ? new StubMethodsInfo(stubArgs, curUseStubIndex, hook, backup) : null;
                } else {
                    hook = MethodHookerStubs32.class.getDeclaredMethod(getHookMethodName(curUseStubIndex), pars);
                    backup = hasStubBackup ? MethodHookerStubs32.class.getDeclaredMethod(getBackupMethodName(curUseStubIndex), pars) : StubMethodsFactory.getStubMethod();
                    return hook != null && backup != null ? new StubMethodsInfo(stubArgs, curUseStubIndex, hook, backup) : null;
                }
            } catch (Throwable var6) {
                return null;
            }
        }
    }

    public static Method getCallOriginMethod(int args, int index) {
        Class stubClass = is64Bit ? MethodHookerStubs64.class : MethodHookerStubs32.class;
        String className = stubClass.getName();
        className = className + "$";
        className = className + getCallOriginClassName(args, index);

        try {
            Class callOriginClass = Class.forName(className, true, stubClass.getClassLoader());
            return callOriginClass.getDeclaredMethod("call", long[].class);
        } catch (Throwable var5) {
            Log.e("HookStubManager", "load call origin class error!", var5);
            return null;
        }
    }

    public static boolean tryCompileAndResolveCallOriginMethod(Method backupMethod, int args, int index) {
        Method method = getCallOriginMethod(args, index);
        if (method != null) {
            SandHookMethodResolver.resolveMethod(method, backupMethod);
            return SandHook.compileMethod(method);
        } else {
            return false;
        }
    }

    public static int getMatchStubArgsCount(int stubArgs) {
        for(int i = stubArgs; i <= MAX_STUB_ARGS; ++i) {
            if (curUseStubIndexes[i].get() < stubSizes[i]) {
                return i;
            }
        }

        return -1;
    }

    public static Class[] getFindMethodParTypes(boolean is64Bit, int stubArgs) {
        if (stubArgs == 0) {
            return null;
        } else {
            Class[] args = new Class[stubArgs];
            int i;
            if (is64Bit) {
                for(i = 0; i < stubArgs; ++i) {
                    args[i] = Long.TYPE;
                }
            } else {
                for(i = 0; i < stubArgs; ++i) {
                    args[i] = Integer.TYPE;
                }
            }

            return args;
        }
    }

    public static long hookBridge(int id, CallOriginCallBack callOrigin, long... stubArgs) throws Throwable {
        Member originMethod = originMethods[id];
        HookMethodEntity entity = hookMethodEntities[id];
        Object thiz = null;
        Object[] args = null;
        if (hasArgs(stubArgs)) {
            thiz = entity.getThis(stubArgs[0]);
            args = entity.getArgs(stubArgs);
        }

        if (XposedBridge.disableHooks) {
            return hasStubBackup ? callOrigin.call(stubArgs) : callOrigin(entity, originMethod, thiz, args);
        } else {
            DexLog.printMethodHookIn(originMethod);
            Object[] snapshot = additionalHookInfos[id].callbacks.getSnapshot();
            if (snapshot != null && snapshot.length != 0) {
                XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();
                param.method = originMethod;
                param.thisObject = thiz;
                param.args = args;
                int beforeIdx = 0;

                do {
                    label68: {
                        try {
                            ((XC_MethodHook)snapshot[beforeIdx]).callBeforeHookedMethod(param);
                        } catch (Throwable var16) {
                            param.setResult((Object)null);
                            param.returnEarly = false;
                            break label68;
                        }

                        if (param.returnEarly) {
                            ++beforeIdx;
                            break;
                        }
                    }

                    ++beforeIdx;
                } while(beforeIdx < snapshot.length);

                if (!param.returnEarly) {
                    try {
                        if (hasStubBackup) {
                            long[] newArgs = entity.getArgsAddress(stubArgs, param.args);
                            param.setResult(entity.getResult(callOrigin.call(newArgs)));
                        } else {
                            param.setResult(SandHook.callOriginMethod(originMethod, entity.backup, thiz, param.args));
                        }
                    } catch (Throwable var14) {
                        XposedBridge.log(var14);
                        param.setThrowable(var14);
                    }
                }

                int afterIdx = beforeIdx - 1;

                do {
                    Object lastResult = param.getResult();
                    Throwable lastThrowable = param.getThrowable();

                    try {
                        ((XC_MethodHook)snapshot[afterIdx]).callAfterHookedMethod(param);
                    } catch (Throwable var15) {
                        XposedBridge.log(var15);
                        if (lastThrowable == null) {
                            param.setResult(lastResult);
                        } else {
                            param.setThrowable(lastThrowable);
                        }
                    }

                    --afterIdx;
                } while(afterIdx >= 0);

                if (!param.hasThrowable()) {
                    return entity.getResultAddress(param.getResult());
                } else {
                    throw param.getThrowable();
                }
            } else {
                return hasStubBackup ? callOrigin.call(stubArgs) : callOrigin(entity, originMethod, thiz, args);
            }
        }
    }

    public static Object hookBridge(Member origin, Method backup, XposedBridge.AdditionalHookInfo additionalHookInfo, Object thiz, Object... args) throws Throwable {
        if (XposedBridge.disableHooks) {
            return SandHook.callOriginMethod(origin, backup, thiz, args);
        } else {
            DexLog.printMethodHookIn(origin);
            Object[] snapshot = additionalHookInfo.callbacks.getSnapshot();
            if (snapshot != null && snapshot.length != 0) {
                XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();
                param.method = origin;
                param.thisObject = thiz;
                param.args = args;
                int beforeIdx = 0;

                do {
                    label55: {
                        try {
                            ((XC_MethodHook)snapshot[beforeIdx]).callBeforeHookedMethod(param);
                        } catch (Throwable var14) {
                            param.setResult((Object)null);
                            param.returnEarly = false;
                            break label55;
                        }

                        if (param.returnEarly) {
                            ++beforeIdx;
                            break;
                        }
                    }

                    ++beforeIdx;
                } while(beforeIdx < snapshot.length);

                if (!param.returnEarly) {
                    try {
                        param.setResult(SandHook.callOriginMethod(origin, backup, thiz, param.args));
                    } catch (Throwable var12) {
                        XposedBridge.log(var12);
                        param.setThrowable(var12);
                    }
                }

                int afterIdx = beforeIdx - 1;

                do {
                    Object lastResult = param.getResult();
                    Throwable lastThrowable = param.getThrowable();

                    try {
                        ((XC_MethodHook)snapshot[afterIdx]).callAfterHookedMethod(param);
                    } catch (Throwable var13) {
                        XposedBridge.log(var13);
                        if (lastThrowable == null) {
                            param.setResult(lastResult);
                        } else {
                            param.setThrowable(lastThrowable);
                        }
                    }

                    --afterIdx;
                } while(afterIdx >= 0);

                if (!param.hasThrowable()) {
                    return param.getResult();
                } else {
                    throw param.getThrowable();
                }
            } else {
                return SandHook.callOriginMethod(origin, backup, thiz, args);
            }
        }
    }

    public static final long callOrigin(HookMethodEntity entity, Member origin, Object thiz, Object[] args) throws Throwable {
        Object res = SandHook.callOriginMethod(origin, entity.backup, thiz, args);
        return entity.getResultAddress(res);
    }

    private static boolean hasArgs(long... args) {
        return args != null && args.length > 0;
    }

    public static boolean support() {
        return MAX_STUB_ARGS > 0 && SandHook.canGetObject() && SandHook.canGetObjectAddress();
    }

    static {
        Class stubClass = is64Bit ? MethodHookerStubs64.class : MethodHookerStubs32.class;
        stubSizes = (int[])XposedHelpers.getStaticObjectField(stubClass, "stubSizes");
        Boolean hasBackup = (Boolean)XposedHelpers.getStaticObjectField(stubClass, "hasStubBackup");
        hasStubBackup = hasBackup != null && hasBackup && !XposedCompat.useNewCallBackup;
        if (stubSizes != null && stubSizes.length > 0) {
            MAX_STUB_ARGS = stubSizes.length - 1;
            curUseStubIndexes = new AtomicInteger[MAX_STUB_ARGS + 1];

            for(int i = 0; i < MAX_STUB_ARGS + 1; ++i) {
                curUseStubIndexes[i] = new AtomicInteger(0);
                ALL_STUB += stubSizes[i];
            }

            originMethods = new Member[ALL_STUB];
            hookMethodEntities = new HookMethodEntity[ALL_STUB];
            additionalHookInfos = new XposedBridge.AdditionalHookInfo[ALL_STUB];
        }

    }

    static class StubMethodsInfo {
        int args = 0;
        int index = 0;
        Method hook;
        Method backup;

        public StubMethodsInfo(int args, int index, Method hook, Method backup) {
            this.args = args;
            this.index = index;
            this.hook = hook;
            this.backup = backup;
        }
    }
}
