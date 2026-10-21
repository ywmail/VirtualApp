/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.swift.sandhook.wrapper;

import android.text.TextUtils;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.MethodReflectParams;
import com.swift.sandhook.annotation.Param;
import com.swift.sandhook.annotation.SkipParamCheck;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.wrapper.StubMethodsFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class HookWrapper {
    public static void addHookClass(Class<?> ... classes) throws HookErrorException {
        HookWrapper.addHookClass(null, classes);
    }

    public static void addHookClass(ClassLoader classLoader, Class<?> ... classes) throws HookErrorException {
        for (Class<?> clazz : classes) {
            HookWrapper.addHookClass(classLoader, clazz);
        }
    }

    public static void addHookClass(ClassLoader classLoader, Class<?> clazz) throws HookErrorException {
        Class targetHookClass = HookWrapper.getTargetHookClass(classLoader, clazz);
        if (targetHookClass == null) {
            throw new HookErrorException("error hook wrapper class :" + clazz.getName());
        }
        Map<Member, HookEntity> hookEntityMap = HookWrapper.getHookMethods(classLoader, targetHookClass, clazz);
        try {
            HookWrapper.fillBackupMethod(classLoader, clazz, hookEntityMap);
        }
        catch (Throwable throwable) {
            throw new HookErrorException("fillBackupMethod error!", throwable);
        }
        for (HookEntity entity : hookEntityMap.values()) {
            SandHook.hook(entity);
        }
    }

    private static void fillBackupMethod(ClassLoader classLoader, Class<?> clazz, Map<Member, HookEntity> hookEntityMap) {
        Field[] fields = null;
        try {
            fields = clazz.getDeclaredFields();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (fields == null || fields.length == 0) {
            return;
        }
        if (hookEntityMap.isEmpty()) {
            return;
        }
        for (Field field : fields) {
            HookMethodBackup hookMethodBackup;
            if (!Modifier.isStatic(field.getModifiers()) || (hookMethodBackup = field.getAnnotation(HookMethodBackup.class)) == null) continue;
            for (HookEntity hookEntity : hookEntityMap.values()) {
                if (!TextUtils.equals((CharSequence)(hookEntity.isCtor() ? "<init>" : hookEntity.target.getName()), (CharSequence)hookMethodBackup.value()) || !HookWrapper.samePars(classLoader, field, hookEntity.pars)) continue;
                field.setAccessible(true);
                if (hookEntity.backup == null) {
                    hookEntity.backup = StubMethodsFactory.getStubMethod();
                    hookEntity.hookIsStub = true;
                    hookEntity.resolveDexCache = false;
                }
                if (hookEntity.backup == null) continue;
                try {
                    if (field.getType() == Method.class) {
                        field.set(null, hookEntity.backup);
                        continue;
                    }
                    if (field.getType() != HookEntity.class) continue;
                    field.set(null, hookEntity);
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<Member, HookEntity> getHookMethods(ClassLoader classLoader, Class targetHookClass, Class<?> hookWrapperClass) throws HookErrorException {
        HashMap<Member, HookEntity> hookEntityMap = new HashMap<Member, HookEntity>();
        Method[] methods = null;
        try {
            methods = hookWrapperClass.getDeclaredMethods();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (methods == null || methods.length == 0) {
            throw new HookErrorException("error hook wrapper class :" + targetHookClass.getName());
        }
        for (Method method : methods) {
            HookEntity entity;
            Executable foundMethod;
            Class[] pars;
            String methodName;
            HookMethod hookMethodAnno = method.getAnnotation(HookMethod.class);
            HookMethodBackup hookMethodBackupAnno = method.getAnnotation(HookMethodBackup.class);
            if (hookMethodAnno != null) {
                methodName = hookMethodAnno.value();
                pars = HookWrapper.parseMethodPars(classLoader, method);
                try {
                    foundMethod = methodName.equals("<init>") ? targetHookClass.getConstructor(pars) : targetHookClass.getDeclaredMethod(methodName, pars);
                }
                catch (NoSuchMethodException e) {
                    throw new HookErrorException("can not find target method: " + methodName, e);
                }
                if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                    HookWrapper.checkSignature(foundMethod, method, pars);
                }
                if ((entity = (HookEntity)hookEntityMap.get(foundMethod)) == null) {
                    entity = new HookEntity(foundMethod);
                    hookEntityMap.put(foundMethod, entity);
                }
                entity.pars = pars;
                entity.hook = method;
                continue;
            }
            if (hookMethodBackupAnno == null) continue;
            methodName = hookMethodBackupAnno.value();
            pars = HookWrapper.parseMethodPars(classLoader, method);
            try {
                foundMethod = methodName.equals("<init>") ? targetHookClass.getConstructor(pars) : targetHookClass.getDeclaredMethod(methodName, pars);
            }
            catch (NoSuchMethodException e) {
                throw new HookErrorException("can not find target method: " + methodName, e);
            }
            if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                HookWrapper.checkSignature(foundMethod, method, pars);
            }
            if ((entity = (HookEntity)hookEntityMap.get(foundMethod)) == null) {
                entity = new HookEntity(foundMethod);
                hookEntityMap.put(foundMethod, entity);
            }
            entity.pars = pars;
            entity.backup = method;
        }
        return hookEntityMap;
    }

    private static Class[] parseMethodPars(ClassLoader classLoader, Method method) throws HookErrorException {
        MethodParams methodParams = method.getAnnotation(MethodParams.class);
        MethodReflectParams methodReflectParams = method.getAnnotation(MethodReflectParams.class);
        if (methodParams != null) {
            return methodParams.value();
        }
        if (methodReflectParams != null) {
            if (methodReflectParams.value().length == 0) {
                return null;
            }
            Class[] pars = new Class[methodReflectParams.value().length];
            for (int i = 0; i < methodReflectParams.value().length; ++i) {
                try {
                    pars[i] = HookWrapper.classNameToClass(methodReflectParams.value()[i], classLoader);
                    continue;
                }
                catch (ClassNotFoundException e) {
                    throw new HookErrorException("hook method pars error: " + method.getName(), e);
                }
            }
            return pars;
        }
        if (HookWrapper.getParsCount(method) > 0) {
            if (HookWrapper.getParsCount(method) == 1) {
                if (HookWrapper.hasThisObject(method)) {
                    return HookWrapper.parseMethodParsNew(classLoader, method);
                }
                return null;
            }
            return HookWrapper.parseMethodParsNew(classLoader, method);
        }
        return null;
    }

    private static Class[] parseMethodPars(ClassLoader classLoader, Field field) throws HookErrorException {
        MethodParams methodParams = field.getAnnotation(MethodParams.class);
        MethodReflectParams methodReflectParams = field.getAnnotation(MethodReflectParams.class);
        if (methodParams != null) {
            return methodParams.value();
        }
        if (methodReflectParams != null) {
            if (methodReflectParams.value().length == 0) {
                return null;
            }
            Class[] pars = new Class[methodReflectParams.value().length];
            for (int i = 0; i < methodReflectParams.value().length; ++i) {
                try {
                    pars[i] = HookWrapper.classNameToClass(methodReflectParams.value()[i], classLoader);
                    continue;
                }
                catch (ClassNotFoundException e) {
                    throw new HookErrorException("hook method pars error: " + field.getName(), e);
                }
            }
            return pars;
        }
        return null;
    }

    private static Class[] parseMethodParsNew(ClassLoader classLoader, Method method) throws HookErrorException {
        Class<?>[] hookMethodPars = method.getParameterTypes();
        if (hookMethodPars == null || hookMethodPars.length == 0) {
            return null;
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        Class[] realPars = null;
        int parIndex = 0;
        for (int i = 0; i < annotations.length; ++i) {
            Class<?> hookPar = hookMethodPars[i];
            Annotation[] methodAnnos = annotations[i];
            if (i == 0) {
                if (HookWrapper.isThisObject(methodAnnos)) {
                    realPars = new Class[annotations.length - 1];
                    continue;
                }
                realPars = new Class[annotations.length];
            }
            try {
                realPars[parIndex] = HookWrapper.getRealParType(classLoader, hookPar, methodAnnos, method.isAnnotationPresent(SkipParamCheck.class));
            }
            catch (Exception e) {
                throw new HookErrorException("hook method <" + method.getName() + "> parser pars error", e);
            }
            ++parIndex;
        }
        return realPars;
    }

    private static Class getRealParType(ClassLoader classLoader, Class hookPar, Annotation[] annotations, boolean skipCheck) throws Exception {
        if (annotations == null || annotations.length == 0) {
            return hookPar;
        }
        for (Annotation annotation : annotations) {
            if (!(annotation instanceof Param)) continue;
            Param param = (Param)annotation;
            if (TextUtils.isEmpty((CharSequence)param.value())) {
                return hookPar;
            }
            Class realPar = HookWrapper.classNameToClass(param.value(), classLoader);
            if (skipCheck || realPar.equals(hookPar) || hookPar.isAssignableFrom(realPar)) {
                return realPar;
            }
            throw new ClassCastException("hook method par cast error!");
        }
        return hookPar;
    }

    private static boolean hasThisObject(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations == null || annotations.length == 0) {
            return false;
        }
        Annotation[] thisParAnno = annotations[0];
        return HookWrapper.isThisObject(thisParAnno);
    }

    private static boolean isThisObject(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (!(annotation instanceof ThisObject)) continue;
            return true;
        }
        return false;
    }

    private static int getParsCount(Method method) {
        Class<?>[] hookMethodPars = method.getParameterTypes();
        return hookMethodPars == null ? 0 : hookMethodPars.length;
    }

    private static Class classNameToClass(String name, ClassLoader classLoader) throws ClassNotFoundException {
        Class clazz;
        switch (name) {
            case "boolean":
                clazz = Boolean.TYPE;
                break;
            case "byte":
                clazz = Byte.TYPE;
                break;
            case "char":
                clazz = Character.TYPE;
                break;
            case "double":
                clazz = Double.TYPE;
                break;
            case "float":
                clazz = Float.TYPE;
                break;
            case "int":
                clazz = Integer.TYPE;
                break;
            case "long":
                clazz = Long.TYPE;
                break;
            case "short":
                clazz = Short.TYPE;
                break;
            default:
                if (classLoader == null) {
                    clazz = Class.forName(name);
                } else {
                    clazz = Class.forName(name, true, classLoader);
                }
        }

        return clazz;
    }

    private static boolean samePars(ClassLoader classLoader, Field field, Class[] par) {
        try {
            Class[] parsOnField = HookWrapper.parseMethodPars(classLoader, field);
            if (parsOnField == null && field.isAnnotationPresent(SkipParamCheck.class)) {
                return true;
            }
            if (par == null) {
                par = new Class[]{};
            }
            if (parsOnField == null) {
                parsOnField = new Class[]{};
            }
            if (par.length != parsOnField.length) {
                return false;
            }
            for (int i = 0; i < par.length; ++i) {
                if (par[i] == parsOnField[i]) continue;
                return false;
            }
            return true;
        }
        catch (HookErrorException e) {
            return false;
        }
    }

    private static Class getTargetHookClass(ClassLoader classLoader, Class<?> hookWrapperClass) {
        HookClass hookClass = hookWrapperClass.getAnnotation(HookClass.class);
        HookReflectClass hookReflectClass = hookWrapperClass.getAnnotation(HookReflectClass.class);
        if (hookClass != null) {
            return hookClass.value();
        }
        if (hookReflectClass != null) {
            try {
                if (classLoader == null) {
                    return Class.forName(hookReflectClass.value());
                }
                return Class.forName(hookReflectClass.value(), true, classLoader);
            }
            catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public static void checkSignature(Member origin, Method fake, Class[] originPars) throws HookErrorException {
        Class<?> originRet;
        if (!Modifier.isStatic(fake.getModifiers())) {
            throw new HookErrorException("hook method must static! - " + fake.getName());
        }
        if (origin instanceof Constructor ? !fake.getReturnType().equals(Void.TYPE) : origin instanceof Method && (originRet = ((Method)origin).getReturnType()) != fake.getReturnType() && !originRet.isAssignableFrom(originRet)) {
            throw new HookErrorException("error return type! - " + fake.getName());
        }
        Class<?>[] fakePars = fake.getParameterTypes();
        if (fakePars == null) {
            fakePars = new Class[]{};
        }
        if (originPars == null) {
            originPars = new Class[]{};
        }
        if (originPars.length == 0 && fakePars.length == 0) {
            return;
        }
        int parOffset = 0;
        if (!Modifier.isStatic(origin.getModifiers())) {
            parOffset = 1;
            if (fakePars.length == 0) {
                throw new HookErrorException("first par must be this! " + fake.getName());
            }
            if (fakePars[0] != origin.getDeclaringClass() && !fakePars[0].isAssignableFrom(origin.getDeclaringClass())) {
                throw new HookErrorException("first par must be this! " + fake.getName());
            }
            if (fakePars.length != originPars.length + 1) {
                throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
            }
        } else if (fakePars.length != originPars.length) {
            throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
        }
        for (int i = 0; i < originPars.length; ++i) {
            if (fakePars[i + parOffset] == originPars[i] || fakePars[i + parOffset].isAssignableFrom(originPars[i])) continue;
            throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
        }
    }

    public static class HookEntity {
        public Member target;
        public Method hook;
        public Method backup;
        public boolean hookIsStub = false;
        public boolean resolveDexCache = true;
        public boolean backupIsStub = true;
        public boolean initClass = true;
        public Class[] pars;
        public int hookMode;

        public HookEntity(Member target) {
            this.target = target;
        }

        public HookEntity(Member target, Method hook, Method backup) {
            this.target = target;
            this.hook = hook;
            this.backup = backup;
        }

        public HookEntity(Member target, Method hook, Method backup, boolean resolveDexCache) {
            this.target = target;
            this.hook = hook;
            this.backup = backup;
            this.resolveDexCache = resolveDexCache;
        }

        public boolean isCtor() {
            return this.target instanceof Constructor;
        }

        public Object callOrigin(Object thiz, Object ... args) throws Throwable {
            return SandHook.callOriginMethod(this.backupIsStub, this.target, this.backup, thiz, args);
        }
    }
}

