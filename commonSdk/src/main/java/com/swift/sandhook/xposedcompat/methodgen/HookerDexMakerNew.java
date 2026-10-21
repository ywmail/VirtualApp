/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  dalvik.system.InMemoryDexClassLoader
 */
package com.swift.sandhook.xposedcompat.methodgen;

import android.text.TextUtils;
import com.android.dx.Code;
import com.android.dx.DexMaker;
import com.android.dx.FieldId;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.wrapper.HookWrapper;
import com.swift.sandhook.xposedcompat.hookstub.HookStubManager;
import com.swift.sandhook.xposedcompat.methodgen.HookMaker;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import dalvik.system.InMemoryDexClassLoader;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Map;

public class HookerDexMakerNew
implements HookMaker {
    public static final String METHOD_NAME_BACKUP = "backup";
    public static final String METHOD_NAME_HOOK = "hook";
    public static final TypeId<Object[]> objArrayTypeId = TypeId.get(Object[].class);
    private static final String CLASS_DESC_PREFIX = "L";
    private static final String CLASS_NAME_PREFIX = "SandHookerNew";
    private static final String FIELD_NAME_HOOK_INFO = "additionalHookInfo";
    private static final String FIELD_NAME_METHOD = "method";
    private static final String FIELD_NAME_BACKUP_METHOD = "backupMethod";
    private static final TypeId<Member> memberTypeId = TypeId.get(Member.class);
    private static final TypeId<Method> methodTypeId = TypeId.get(Method.class);
    private static final TypeId<XposedBridge.AdditionalHookInfo> hookInfoTypeId = TypeId.get(XposedBridge.AdditionalHookInfo.class);
    private FieldId<?, XposedBridge.AdditionalHookInfo> mHookInfoFieldId;
    private FieldId<?, Member> mMethodFieldId;
    private FieldId<?, Method> mBackupMethodFieldId;
    private MethodId<?, ?> mHookMethodId;
    private MethodId<?, ?> mBackupMethodId;
    private MethodId<?, ?> mSandHookBridgeMethodId;
    private TypeId<?> mHookerTypeId;
    private TypeId<?>[] mParameterTypeIds;
    private Class<?>[] mActualParameterTypes;
    private Class<?> mReturnType;
    private TypeId<?> mReturnTypeId;
    private boolean mIsStatic;
    private boolean mHasThrowable;
    private DexMaker mDexMaker;
    private Member mMember;
    private XposedBridge.AdditionalHookInfo mHookInfo;
    private ClassLoader mAppClassLoader;
    private Class<?> mHookClass;
    private Method mHookMethod;
    private Method mBackupMethod;
    private String mDexDirPath;

    private static TypeId<?>[] getParameterTypeIds(Class<?>[] parameterTypes, boolean isStatic) {
        int parameterSize = parameterTypes.length;
        int targetParameterSize = isStatic ? parameterSize : parameterSize + 1;
        TypeId[] parameterTypeIds = new TypeId[targetParameterSize];
        int offset = 0;
        if (!isStatic) {
            parameterTypeIds[0] = TypeId.OBJECT;
            offset = 1;
        }
        for (int i = 0; i < parameterTypes.length; ++i) {
            parameterTypeIds[i + offset] = TypeId.get(parameterTypes[i]);
        }
        return parameterTypeIds;
    }

    private static Class<?>[] getParameterTypes(Class<?>[] parameterTypes, boolean isStatic) {
        if (isStatic) {
            return parameterTypes;
        }
        int parameterSize = parameterTypes.length;
        int targetParameterSize = parameterSize + 1;
        Class[] newParameterTypes = new Class[targetParameterSize];
        int offset = 1;
        newParameterTypes[0] = Object.class;
        System.arraycopy(parameterTypes, 0, newParameterTypes, offset, parameterTypes.length);
        return newParameterTypes;
    }

    @Override
    public void start(Member member, XposedBridge.AdditionalHookInfo hookInfo, ClassLoader appClassLoader, String dexDirPath) throws Exception {
        if (member instanceof Method) {
            Method method = (Method)member;
            this.mIsStatic = Modifier.isStatic(method.getModifiers());
            this.mReturnType = method.getReturnType();
            if (this.mReturnType.equals(Void.class) || this.mReturnType.equals(Void.TYPE) || this.mReturnType.isPrimitive()) {
                this.mReturnTypeId = TypeId.get(this.mReturnType);
            } else {
                this.mReturnType = Object.class;
                this.mReturnTypeId = TypeId.OBJECT;
            }
            this.mParameterTypeIds = HookerDexMakerNew.getParameterTypeIds(method.getParameterTypes(), this.mIsStatic);
            this.mActualParameterTypes = HookerDexMakerNew.getParameterTypes(method.getParameterTypes(), this.mIsStatic);
            this.mHasThrowable = method.getExceptionTypes().length > 0;
        } else if (member instanceof Constructor) {
            Constructor constructor = (Constructor)member;
            this.mIsStatic = false;
            this.mReturnType = Void.TYPE;
            this.mReturnTypeId = TypeId.VOID;
            this.mParameterTypeIds = HookerDexMakerNew.getParameterTypeIds(constructor.getParameterTypes(), this.mIsStatic);
            this.mActualParameterTypes = HookerDexMakerNew.getParameterTypes(constructor.getParameterTypes(), this.mIsStatic);
            this.mHasThrowable = constructor.getExceptionTypes().length > 0;
        } else {
            if (member.getDeclaringClass().isInterface()) {
                throw new IllegalArgumentException("Cannot hook interfaces: " + member.toString());
            }
            if (Modifier.isAbstract(member.getModifiers())) {
                throw new IllegalArgumentException("Cannot hook abstract methods: " + member.toString());
            }
            throw new IllegalArgumentException("Only methods and constructors can be hooked: " + member.toString());
        }
        this.mMember = member;
        this.mHookInfo = hookInfo;
        this.mDexDirPath = dexDirPath;
        this.mAppClassLoader = appClassLoader == null || appClassLoader.getClass().getName().equals("java.lang.BootClassLoader") ? this.getClass().getClassLoader() : appClassLoader;
        this.mDexMaker = new DexMaker();
        String className = this.getClassName(this.mMember);
        String dexName = className + ".jar";
        HookWrapper.HookEntity hookEntity = null;
        try {
            ClassLoader loader = this.mDexMaker.loadClassDirect(this.mAppClassLoader, new File(this.mDexDirPath), dexName);
            if (loader != null) {
                hookEntity = this.loadHookerClass(loader, className);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (hookEntity == null) {
            hookEntity = this.doMake(className, dexName);
        }
        SandHook.hook(hookEntity);
    }

    private HookWrapper.HookEntity doMake(String className, String dexName) throws Exception {
        ClassLoader loader;
        block6: {
            this.mHookerTypeId = TypeId.get(CLASS_DESC_PREFIX + className + ";");
            this.mDexMaker.declare(this.mHookerTypeId, className + ".generated", 1, TypeId.OBJECT, new TypeId[0]);
            this.generateFields();
            this.generateHookMethod();
            this.generateBackupMethod();
            loader = null;
            if (TextUtils.isEmpty((CharSequence)this.mDexDirPath)) {
                if (SandHookConfig.SDK_INT < 26) {
                    throw new IllegalArgumentException("dexDirPath should not be empty!!!");
                }
                byte[] dexBytes = this.mDexMaker.generate();
                loader = new InMemoryDexClassLoader(ByteBuffer.wrap(dexBytes), this.mAppClassLoader);
            } else {
                try {
                    loader = this.mDexMaker.generateAndLoad(this.mAppClassLoader, new File(this.mDexDirPath), dexName);
                }
                catch (IOException e) {
                    if (SandHookConfig.SDK_INT < 26) break block6;
                    byte[] dexBytes = this.mDexMaker.generate();
                    loader = new InMemoryDexClassLoader(ByteBuffer.wrap(dexBytes), this.mAppClassLoader);
                }
            }
        }
        if (loader == null) {
            return null;
        }
        return this.loadHookerClass(loader, className);
    }

    private HookWrapper.HookEntity loadHookerClass(ClassLoader loader, String className) throws Exception {
        this.mHookClass = loader.loadClass(className);
        this.mHookMethod = this.mHookClass.getMethod(METHOD_NAME_HOOK, this.mActualParameterTypes);
        this.mBackupMethod = this.mHookClass.getMethod(METHOD_NAME_BACKUP, new Class[0]);
        this.setup(this.mHookClass);
        return new HookWrapper.HookEntity(this.mMember, this.mHookMethod, this.mBackupMethod, false);
    }

    private void setup(Class mHookClass) {
        XposedHelpers.setStaticObjectField(mHookClass, FIELD_NAME_METHOD, this.mMember);
        XposedHelpers.setStaticObjectField(mHookClass, FIELD_NAME_BACKUP_METHOD, this.mBackupMethod);
        XposedHelpers.setStaticObjectField(mHookClass, FIELD_NAME_HOOK_INFO, this.mHookInfo);
    }

    private String getClassName(Member originMethod) {
        return "SandHookerNew_" + DexMakerUtils.MD5(originMethod.toString());
    }

    @Override
    public Method getHookMethod() {
        return this.mHookMethod;
    }

    @Override
    public Method getBackupMethod() {
        return this.mBackupMethod;
    }

    @Override
    public Method getCallBackupMethod() {
        return this.mBackupMethod;
    }

    public Class getHookClass() {
        return this.mHookClass;
    }

    private void generateFields() {
        this.mHookInfoFieldId = this.mHookerTypeId.getField(hookInfoTypeId, FIELD_NAME_HOOK_INFO);
        this.mMethodFieldId = this.mHookerTypeId.getField(memberTypeId, FIELD_NAME_METHOD);
        this.mBackupMethodFieldId = this.mHookerTypeId.getField(methodTypeId, FIELD_NAME_BACKUP_METHOD);
        this.mDexMaker.declare(this.mHookInfoFieldId, 8, null);
        this.mDexMaker.declare(this.mMethodFieldId, 8, null);
        this.mDexMaker.declare(this.mBackupMethodFieldId, 8, null);
    }

    private void generateBackupMethod() {
        this.mBackupMethodId = this.mHookerTypeId.getMethod(TypeId.VOID, METHOD_NAME_BACKUP, new TypeId[0]);
        Code code = this.mDexMaker.declare(this.mBackupMethodId, 9);
        code.returnVoid();
    }

    private void generateHookMethod() {
        this.mHookMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, METHOD_NAME_HOOK, this.mParameterTypeIds);
        this.mSandHookBridgeMethodId = TypeId.get(HookStubManager.class).getMethod(TypeId.get(Object.class), "hookBridge", memberTypeId, methodTypeId, hookInfoTypeId, TypeId.get(Object.class), TypeId.get(Object[].class));
        Code code = this.mDexMaker.declare(this.mHookMethodId, 9);
        Local<Member> originMethod = code.newLocal(memberTypeId);
        Local<Method> backupMethod = code.newLocal(methodTypeId);
        Local<XposedBridge.AdditionalHookInfo> hookInfo = code.newLocal(hookInfoTypeId);
        Local<Object> thisObject = code.newLocal(TypeId.OBJECT);
        Local<Object[]> args = code.newLocal(objArrayTypeId);
        Local<Integer> actualParamSize = code.newLocal(TypeId.INT);
        Local<Integer> argIndex = code.newLocal(TypeId.INT);
        Local<Object> resultObj = code.newLocal(TypeId.OBJECT);
        Local[] allArgsLocals = this.createParameterLocals(code);
        Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
        code.loadConstant(args, null);
        code.loadConstant(argIndex, 0);
        code.sget(this.mMethodFieldId, originMethod);
        code.sget(this.mBackupMethodFieldId, backupMethod);
        code.sget(this.mHookInfoFieldId, hookInfo);
        int paramsSize = this.mParameterTypeIds.length;
        int offset = 0;
        if (this.mIsStatic) {
            code.loadConstant(thisObject, null);
        } else {
            offset = 1;
            code.move(thisObject, allArgsLocals[0]);
        }
        code.loadConstant(actualParamSize, paramsSize - offset);
        code.newArray(args, actualParamSize);
        for (int i = offset; i < paramsSize; ++i) {
            Local parameter = allArgsLocals[i];
            DexMakerUtils.autoBoxIfNecessary(code, resultObj, parameter);
            code.loadConstant(argIndex, i - offset);
            code.aput(args, argIndex, resultObj);
        }
        if (this.mReturnTypeId.equals(TypeId.VOID)) {
            code.invokeStatic(this.mSandHookBridgeMethodId, null, originMethod, backupMethod, hookInfo, thisObject, args);
            code.returnVoid();
        } else {
            code.invokeStatic(this.mSandHookBridgeMethodId, resultObj, originMethod, backupMethod, hookInfo, thisObject, args);
            TypeId objTypeId = DexMakerUtils.getObjTypeIdIfPrimitive(this.mReturnTypeId);
            Local matchObjLocal = resultLocals.get(objTypeId);
            code.cast(matchObjLocal, resultObj);
            Local toReturn = resultLocals.get(this.mReturnTypeId);
            DexMakerUtils.autoUnboxIfNecessary(code, toReturn, matchObjLocal, resultLocals, true);
            code.returnValue(toReturn);
        }
    }

    private Local[] createParameterLocals(Code code) {
        Local[] paramLocals = new Local[this.mParameterTypeIds.length];
        for (int i = 0; i < this.mParameterTypeIds.length; ++i) {
            paramLocals[i] = code.getParameter(i, this.mParameterTypeIds[i]);
        }
        return paramLocals;
    }
}

