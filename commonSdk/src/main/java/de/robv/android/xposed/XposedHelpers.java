//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.robv.android.xposed;

import android.content.res.Resources;
import dalvik.system.DexFile;
import external.org.apache.commons.lang3.ClassUtils;
import external.org.apache.commons.lang3.reflect.MemberUtils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipFile;

public final class XposedHelpers {
    private static final HashMap<String, Field> fieldCache = new HashMap();
    private static final HashMap<String, Method> methodCache = new HashMap();
    private static final HashMap<String, Constructor<?>> constructorCache = new HashMap();
    private static final WeakHashMap<Object, HashMap<String, Object>> additionalFields = new WeakHashMap();
    private static final HashMap<String, ThreadLocal<AtomicInteger>> sMethodDepth = new HashMap();

    private XposedHelpers() {
    }

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = XposedBridge.BOOTCLASSLOADER;
        }

        try {
            return ClassUtils.getClass(classLoader, className, false);
        } catch (ClassNotFoundException var3) {
            throw new ClassNotFoundError(var3);
        }
    }

    public static Class<?> findClassIfExists(String className, ClassLoader classLoader) {
        try {
            return findClass(className, classLoader);
        } catch (ClassNotFoundError var3) {
            return null;
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        String fullFieldName = clazz.getName() + '#' + fieldName;
        Field field;
        if (fieldCache.containsKey(fullFieldName)) {
            field = (Field)fieldCache.get(fullFieldName);
            if (field == null) {
                throw new NoSuchFieldError(fullFieldName);
            } else {
                return field;
            }
        } else {
            try {
                field = findFieldRecursiveImpl(clazz, fieldName);
                field.setAccessible(true);
                fieldCache.put(fullFieldName, field);
                return field;
            } catch (NoSuchFieldException var4) {
                fieldCache.put(fullFieldName, (Field) null);
                throw new NoSuchFieldError(fullFieldName);
            }
        }
    }

    public static Field findFieldIfExists(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName);
        } catch (NoSuchFieldError var3) {
            return null;
        }
    }

    private static Field findFieldRecursiveImpl(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException var5) {
            while(true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class)) {
                    throw var5;
                }

                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException var4) {
                }
            }
        }
    }

    public static Field findFirstFieldByExactType(Class<?> clazz, Class<?> type) {
        Class<?> clz = clazz;

        do {
            Field[] var3 = clz.getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (field.getType() == type) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while((clz = clz.getSuperclass()) != null);

        throw new NoSuchFieldError("Field of type " + type.getName() + " in class " + clazz.getName());
    }

    public static XC_MethodHook.Unhook findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (parameterTypesAndCallback.length != 0 && parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook) {
            XC_MethodHook callback = (XC_MethodHook)parameterTypesAndCallback[parameterTypesAndCallback.length - 1];
            Method m = findMethodExact(clazz, methodName, getParameterClasses(clazz.getClassLoader(), parameterTypesAndCallback));
            return XposedBridge.hookMethod(m, callback);
        } else {
            throw new IllegalArgumentException("no callback defined");
        }
    }

    public static XC_MethodHook.Unhook findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {
        return findAndHookMethod(findClass(className, classLoader), methodName, parameterTypesAndCallback);
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Object... parameterTypes) {
        return findMethodExact(clazz, methodName, getParameterClasses(clazz.getClassLoader(), parameterTypes));
    }

    public static Method findMethodExactIfExists(Class<?> clazz, String methodName, Object... parameterTypes) {
        try {
            return findMethodExact(clazz, methodName, parameterTypes);
        } catch (NoSuchMethodError | ClassNotFoundError var4) {
            return null;
        }
    }

    public static Method findMethodExact(String className, ClassLoader classLoader, String methodName, Object... parameterTypes) {
        return findMethodExact(findClass(className, classLoader), methodName, getParameterClasses(classLoader, parameterTypes));
    }

    public static Method findMethodExactIfExists(String className, ClassLoader classLoader, String methodName, Object... parameterTypes) {
        try {
            return findMethodExact(className, classLoader, methodName, parameterTypes);
        } catch (NoSuchMethodError | ClassNotFoundError var5) {
            return null;
        }
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        String fullMethodName = clazz.getName() + '#' + methodName + getParametersString(parameterTypes) + "#exact";
        Method method;
        if (methodCache.containsKey(fullMethodName)) {
            method = (Method)methodCache.get(fullMethodName);
            if (method == null) {
                throw new NoSuchMethodError(fullMethodName);
            } else {
                return method;
            }
        } else {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                methodCache.put(fullMethodName, method);
                return method;
            } catch (NoSuchMethodException var5) {
                methodCache.put(fullMethodName, (Method) null);
                throw new NoSuchMethodError(fullMethodName);
            }
        }
    }

    public static Method[] findMethodsByExactParameters(Class<?> clazz, Class<?> returnType, Class<?>... parameterTypes) {
        List<Method> result = new LinkedList();
        Method[] var4 = clazz.getDeclaredMethods();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if (returnType == null || returnType == method.getReturnType()) {
                Class<?>[] methodParameterTypes = method.getParameterTypes();
                if (parameterTypes.length == methodParameterTypes.length) {
                    boolean match = true;

                    for(int i = 0; i < parameterTypes.length; ++i) {
                        if (parameterTypes[i] != methodParameterTypes[i]) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        method.setAccessible(true);
                        result.add(method);
                    }
                }
            }
        }

        return (Method[])result.toArray(new Method[result.size()]);
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        String fullMethodName = clazz.getName() + '#' + methodName + getParametersString(parameterTypes) + "#bestmatch";
        Method bestMatch;
        if (methodCache.containsKey(fullMethodName)) {
            bestMatch = (Method)methodCache.get(fullMethodName);
            if (bestMatch == null) {
                throw new NoSuchMethodError(fullMethodName);
            } else {
                return bestMatch;
            }
        } else {
            try {
                bestMatch = findMethodExact(clazz, methodName, parameterTypes);
                methodCache.put(fullMethodName, bestMatch);
                return bestMatch;
            } catch (NoSuchMethodError var11) {
                bestMatch = null;
                Class<?> clz = clazz;
                boolean considerPrivateMethods = true;

                do {
                    Method[] var7 = clz.getDeclaredMethods();
                    int var8 = var7.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        Method method = var7[var9];
                        if ((considerPrivateMethods || !Modifier.isPrivate(method.getModifiers())) && method.getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true) && (bestMatch == null || MemberUtils.compareParameterTypes(method.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                            bestMatch = method;
                        }
                    }

                    considerPrivateMethods = false;
                } while((clz = clz.getSuperclass()) != null);

                if (bestMatch != null) {
                    bestMatch.setAccessible(true);
                    methodCache.put(fullMethodName, bestMatch);
                    return bestMatch;
                } else {
                    NoSuchMethodError e = new NoSuchMethodError(fullMethodName);
                    methodCache.put(fullMethodName, (Method) null);
                    throw e;
                }
            }
        }
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Object... args) {
        return findMethodBestMatch(clazz, methodName, getParameterTypes(args));
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Class<?>[] argsClasses = null;

        for(int i = 0; i < parameterTypes.length; ++i) {
            if (parameterTypes[i] == null) {
                if (argsClasses == null) {
                    argsClasses = getParameterTypes(args);
                }

                parameterTypes[i] = argsClasses[i];
            }
        }

        return findMethodBestMatch(clazz, methodName, parameterTypes);
    }

    public static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] clazzes = new Class[args.length];

        for(int i = 0; i < args.length; ++i) {
            clazzes[i] = args[i] != null ? args[i].getClass() : null;
        }

        return clazzes;
    }

    private static Class<?>[] getParameterClasses(ClassLoader classLoader, Object[] parameterTypesAndCallback) {
        Class<?>[] parameterClasses = null;

        for(int i = parameterTypesAndCallback.length - 1; i >= 0; --i) {
            Object type = parameterTypesAndCallback[i];
            if (type == null) {
                throw new ClassNotFoundError("parameter type must not be null", (Throwable)null);
            }

            if (!(type instanceof XC_MethodHook)) {
                if (parameterClasses == null) {
                    parameterClasses = new Class[i + 1];
                }

                if (type instanceof Class) {
                    parameterClasses[i] = (Class)type;
                } else {
                    if (!(type instanceof String)) {
                        throw new ClassNotFoundError("parameter type must either be specified as Class or String", (Throwable)null);
                    }

                    parameterClasses[i] = findClass((String)type, classLoader);
                }
            }
        }

        if (parameterClasses == null) {
            parameterClasses = new Class[0];
        }

        return parameterClasses;
    }

    public static Class<?>[] getClassesAsArray(Class<?>... clazzes) {
        return clazzes;
    }

    private static String getParametersString(Class<?>... clazzes) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        Class[] var3 = clazzes;
        int var4 = clazzes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Class<?> clazz = var3[var5];
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            if (clazz != null) {
                sb.append(clazz.getCanonicalName());
            } else {
                sb.append("null");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    public static Constructor<?> findConstructorExact(Class<?> clazz, Object... parameterTypes) {
        return findConstructorExact(clazz, getParameterClasses(clazz.getClassLoader(), parameterTypes));
    }

    public static Constructor<?> findConstructorExactIfExists(Class<?> clazz, Object... parameterTypes) {
        try {
            return findConstructorExact(clazz, parameterTypes);
        } catch (NoSuchMethodError | ClassNotFoundError var3) {
            return null;
        }
    }

    public static Constructor<?> findConstructorExact(String className, ClassLoader classLoader, Object... parameterTypes) {
        return findConstructorExact(findClass(className, classLoader), getParameterClasses(classLoader, parameterTypes));
    }

    public static Constructor<?> findConstructorExactIfExists(String className, ClassLoader classLoader, Object... parameterTypes) {
        try {
            return findConstructorExact(className, classLoader, parameterTypes);
        } catch (NoSuchMethodError | ClassNotFoundError var4) {
            return null;
        }
    }

    public static Constructor<?> findConstructorExact(Class<?> clazz, Class<?>... parameterTypes) {
        String fullConstructorName = clazz.getName() + getParametersString(parameterTypes) + "#exact";
        Constructor constructor;
        if (constructorCache.containsKey(fullConstructorName)) {
            constructor = (Constructor)constructorCache.get(fullConstructorName);
            if (constructor == null) {
                throw new NoSuchMethodError(fullConstructorName);
            } else {
                return constructor;
            }
        } else {
            try {
                constructor = clazz.getDeclaredConstructor(parameterTypes);
                constructor.setAccessible(true);
                constructorCache.put(fullConstructorName, constructor);
                return constructor;
            } catch (NoSuchMethodException var4) {
                constructorCache.put(fullConstructorName, (Constructor<?>) null);
                throw new NoSuchMethodError(fullConstructorName);
            }
        }
    }

    public static XC_MethodHook.Unhook findAndHookConstructor(Class<?> clazz, Object... parameterTypesAndCallback) {
        if (parameterTypesAndCallback.length != 0 && parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook) {
            XC_MethodHook callback = (XC_MethodHook)parameterTypesAndCallback[parameterTypesAndCallback.length - 1];
            Constructor<?> m = findConstructorExact(clazz, getParameterClasses(clazz.getClassLoader(), parameterTypesAndCallback));
            return XposedBridge.hookMethod(m, callback);
        } else {
            throw new IllegalArgumentException("no callback defined");
        }
    }

    public static XC_MethodHook.Unhook findAndHookConstructor(String className, ClassLoader classLoader, Object... parameterTypesAndCallback) {
        return findAndHookConstructor(findClass(className, classLoader), parameterTypesAndCallback);
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Class<?>... parameterTypes) {
        String fullConstructorName = clazz.getName() + getParametersString(parameterTypes) + "#bestmatch";
        Constructor bestMatch;
        if (constructorCache.containsKey(fullConstructorName)) {
            bestMatch = (Constructor)constructorCache.get(fullConstructorName);
            if (bestMatch == null) {
                throw new NoSuchMethodError(fullConstructorName);
            } else {
                return bestMatch;
            }
        } else {
            try {
                bestMatch = findConstructorExact(clazz, parameterTypes);
                constructorCache.put(fullConstructorName, bestMatch);
                return bestMatch;
            } catch (NoSuchMethodError var9) {
                bestMatch = null;
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                Constructor[] var5 = constructors;
                int var6 = constructors.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Constructor<?> constructor = var5[var7];
                    if (ClassUtils.isAssignable(parameterTypes, constructor.getParameterTypes(), true) && (bestMatch == null || MemberUtils.compareParameterTypes(constructor.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                        bestMatch = constructor;
                    }
                }

                if (bestMatch != null) {
                    bestMatch.setAccessible(true);
                    constructorCache.put(fullConstructorName, bestMatch);
                    return bestMatch;
                } else {
                    NoSuchMethodError e = new NoSuchMethodError(fullConstructorName);
                    constructorCache.put(fullConstructorName, (Constructor<?>) null);
                    throw e;
                }
            }
        }
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Object... args) {
        return findConstructorBestMatch(clazz, getParameterTypes(args));
    }

    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Class<?>[] parameterTypes, Object[] args) {
        Class<?>[] argsClasses = null;

        for(int i = 0; i < parameterTypes.length; ++i) {
            if (parameterTypes[i] == null) {
                if (argsClasses == null) {
                    argsClasses = getParameterTypes(args);
                }

                parameterTypes[i] = argsClasses[i];
            }
        }

        return findConstructorBestMatch(clazz, parameterTypes);
    }

    public static int getFirstParameterIndexByType(Member method, Class<?> type) {
        Class<?>[] classes = method instanceof Method ? ((Method)method).getParameterTypes() : ((Constructor)method).getParameterTypes();

        for(int i = 0; i < classes.length; ++i) {
            if (classes[i] == type) {
                return i;
            }
        }

        throw new NoSuchFieldError("No parameter of type " + type + " found in " + method);
    }

    public static int getParameterIndexByType(Member method, Class<?> type) {
        Class<?>[] classes = method instanceof Method ? ((Method)method).getParameterTypes() : ((Constructor)method).getParameterTypes();
        int idx = -1;

        for(int i = 0; i < classes.length; ++i) {
            if (classes[i] == type) {
                if (idx != -1) {
                    throw new NoSuchFieldError("More than one parameter of type " + type + " found in " + method);
                }

                idx = i;
            }
        }

        if (idx != -1) {
            return idx;
        } else {
            throw new NoSuchFieldError("No parameter of type " + type + " found in " + method);
        }
    }

    public static void setObjectField(Object obj, String fieldName, Object value) {
        try {
            findField(obj.getClass(), fieldName).set(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setBooleanField(Object obj, String fieldName, boolean value) {
        try {
            findField(obj.getClass(), fieldName).setBoolean(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setByteField(Object obj, String fieldName, byte value) {
        try {
            findField(obj.getClass(), fieldName).setByte(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setCharField(Object obj, String fieldName, char value) {
        try {
            findField(obj.getClass(), fieldName).setChar(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setDoubleField(Object obj, String fieldName, double value) {
        try {
            findField(obj.getClass(), fieldName).setDouble(obj, value);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        }
    }

    public static void setFloatField(Object obj, String fieldName, float value) {
        try {
            findField(obj.getClass(), fieldName).setFloat(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setIntField(Object obj, String fieldName, int value) {
        try {
            findField(obj.getClass(), fieldName).setInt(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setLongField(Object obj, String fieldName, long value) {
        try {
            findField(obj.getClass(), fieldName).setLong(obj, value);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        }
    }

    public static void setShortField(Object obj, String fieldName, short value) {
        try {
            findField(obj.getClass(), fieldName).setShort(obj, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static Object getObjectField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).get(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static Object getSurroundingThis(Object obj) {
        return getObjectField(obj, "this$0");
    }

    public static boolean getBooleanField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getBoolean(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static byte getByteField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getByte(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static char getCharField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getChar(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static double getDoubleField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getDouble(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static float getFloatField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getFloat(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static int getIntField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getInt(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static long getLongField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getLong(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static short getShortField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getShort(obj);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {
        try {
            findField(clazz, fieldName).set((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticBooleanField(Class<?> clazz, String fieldName, boolean value) {
        try {
            findField(clazz, fieldName).setBoolean((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticByteField(Class<?> clazz, String fieldName, byte value) {
        try {
            findField(clazz, fieldName).setByte((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticCharField(Class<?> clazz, String fieldName, char value) {
        try {
            findField(clazz, fieldName).setChar((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticDoubleField(Class<?> clazz, String fieldName, double value) {
        try {
            findField(clazz, fieldName).setDouble((Object)null, value);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        }
    }

    public static void setStaticFloatField(Class<?> clazz, String fieldName, float value) {
        try {
            findField(clazz, fieldName).setFloat((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticIntField(Class<?> clazz, String fieldName, int value) {
        try {
            findField(clazz, fieldName).setInt((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static void setStaticLongField(Class<?> clazz, String fieldName, long value) {
        try {
            findField(clazz, fieldName).setLong((Object)null, value);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        }
    }

    public static void setStaticShortField(Class<?> clazz, String fieldName, short value) {
        try {
            findField(clazz, fieldName).setShort((Object)null, value);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        }
    }

    public static Object getStaticObjectField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).get((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static boolean getStaticBooleanField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getBoolean((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static byte getStaticByteField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getByte((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static char getStaticCharField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getChar((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static double getStaticDoubleField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getDouble((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static float getStaticFloatField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getFloat((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static int getStaticIntField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getInt((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static long getStaticLongField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getLong((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static short getStaticShortField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getShort((Object)null);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static Object callMethod(Object obj, String methodName, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, args).invoke(obj, args);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new InvocationTargetError(var6.getCause());
        }
    }

    public static Object callMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, parameterTypes, args).invoke(obj, args);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new InvocationTargetError(var7.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, args).invoke((Object)null, args);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new InvocationTargetError(var6.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, parameterTypes, args).invoke((Object)null, args);
        } catch (IllegalAccessException var5) {
            XposedBridge.log(var5);
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new InvocationTargetError(var7.getCause());
        }
    }

    public static Object newInstance(Class<?> clazz, Object... args) {
        try {
            return findConstructorBestMatch(clazz, args).newInstance(args);
        } catch (IllegalAccessException var3) {
            XposedBridge.log(var3);
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        } catch (InvocationTargetException var5) {
            throw new InvocationTargetError(var5.getCause());
        } catch (InstantiationException var6) {
            throw new InstantiationError(var6.getMessage());
        }
    }

    public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object... args) {
        try {
            return findConstructorBestMatch(clazz, parameterTypes, args).newInstance(args);
        } catch (IllegalAccessException var4) {
            XposedBridge.log(var4);
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new InvocationTargetError(var6.getCause());
        } catch (InstantiationException var7) {
            throw new InstantiationError(var7.getMessage());
        }
    }

    public static Object setAdditionalInstanceField(Object obj, String key, Object value) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key == null) {
            throw new NullPointerException("key must not be null");
        } else {
            HashMap objectFields;
            synchronized(additionalFields) {
                objectFields = (HashMap)additionalFields.get(obj);
                if (objectFields == null) {
                    objectFields = new HashMap();
                    additionalFields.put(obj, objectFields);
                }
            }

            synchronized(objectFields) {
                return objectFields.put(key, value);
            }
        }
    }

    public static Object getAdditionalInstanceField(Object obj, String key) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key == null) {
            throw new NullPointerException("key must not be null");
        } else {
            HashMap objectFields;
            synchronized(additionalFields) {
                objectFields = (HashMap)additionalFields.get(obj);
                if (objectFields == null) {
                    return null;
                }
            }

            synchronized(objectFields) {
                return objectFields.get(key);
            }
        }
    }

    public static Object removeAdditionalInstanceField(Object obj, String key) {
        if (obj == null) {
            throw new NullPointerException("object must not be null");
        } else if (key == null) {
            throw new NullPointerException("key must not be null");
        } else {
            HashMap objectFields;
            synchronized(additionalFields) {
                objectFields = (HashMap)additionalFields.get(obj);
                if (objectFields == null) {
                    return null;
                }
            }

            synchronized(objectFields) {
                return objectFields.remove(key);
            }
        }
    }

    public static Object setAdditionalStaticField(Object obj, String key, Object value) {
        return setAdditionalInstanceField(obj.getClass(), key, value);
    }

    public static Object getAdditionalStaticField(Object obj, String key) {
        return getAdditionalInstanceField(obj.getClass(), key);
    }

    public static Object removeAdditionalStaticField(Object obj, String key) {
        return removeAdditionalInstanceField(obj.getClass(), key);
    }

    public static Object setAdditionalStaticField(Class<?> clazz, String key, Object value) {
        return setAdditionalInstanceField(clazz, key, value);
    }

    public static Object getAdditionalStaticField(Class<?> clazz, String key) {
        return getAdditionalInstanceField(clazz, key);
    }

    public static Object removeAdditionalStaticField(Class<?> clazz, String key) {
        return removeAdditionalInstanceField(clazz, key);
    }

    public static byte[] assetAsByteArray(Resources res, String path) throws IOException {
        return inputStreamToByteArray(res.getAssets().open(path));
    }

    static byte[] inputStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];

        int read;
        while((read = is.read(temp)) > 0) {
            buf.write(temp, 0, read);
        }

        is.close();
        return buf.toByteArray();
    }

    static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException var2) {
            }
        }

    }

    static void closeSilently(DexFile dexFile) {
        if (dexFile != null) {
            try {
                dexFile.close();
            } catch (IOException var2) {
            }
        }

    }

    static void closeSilently(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException var2) {
            }
        }

    }

    public static String getMD5Sum(String file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[8192];

            int read;
            while((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }

            is.close();
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException var7) {
            return "";
        }
    }

    public static int incrementMethodDepth(String method) {
        return ((AtomicInteger)getMethodDepthCounter(method).get()).incrementAndGet();
    }

    public static int decrementMethodDepth(String method) {
        return ((AtomicInteger)getMethodDepthCounter(method).get()).decrementAndGet();
    }

    public static int getMethodDepth(String method) {
        return ((AtomicInteger)getMethodDepthCounter(method).get()).get();
    }

    private static ThreadLocal<AtomicInteger> getMethodDepthCounter(String method) {
        synchronized(sMethodDepth) {
            ThreadLocal<AtomicInteger> counter = (ThreadLocal)sMethodDepth.get(method);
            if (counter == null) {
                counter = new ThreadLocal<AtomicInteger>() {
                    protected AtomicInteger initialValue() {
                        return new AtomicInteger();
                    }
                };
                sMethodDepth.put(method, counter);
            }

            return counter;
        }
    }

    static boolean fileContains(File file, String str) throws IOException {
        BufferedReader in = null;

        boolean var4;
        try {
            in = new BufferedReader(new FileReader(file));

            String line;
            do {
                if ((line = in.readLine()) == null) {
                    var4 = false;
                    return var4;
                }
            } while(!line.contains(str));

            var4 = true;
        } finally {
            closeSilently((Closeable)in);
        }

        return var4;
    }

    static Method getOverriddenMethod(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
            String name = method.getName();
            Class<?>[] parameters = method.getParameterTypes();
            Class<?> clazz = method.getDeclaringClass().getSuperclass();

            while(clazz != null) {
                try {
                    Method superMethod = clazz.getDeclaredMethod(name, parameters);
                    modifiers = superMethod.getModifiers();
                    if (!Modifier.isPrivate(modifiers) && !Modifier.isAbstract(modifiers)) {
                        return superMethod;
                    }

                    return null;
                } catch (NoSuchMethodException var6) {
                    clazz = clazz.getSuperclass();
                }
            }

            return null;
        } else {
            return null;
        }
    }

    static Set<Method> getOverriddenMethods(Class<?> clazz) {
        Set<Method> methods = new HashSet();
        Method[] var2 = clazz.getDeclaredMethods();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Method method = var2[var4];
            Method overridden = getOverriddenMethod(method);
            if (overridden != null) {
                methods.add(overridden);
            }
        }

        return methods;
    }

    public static final class InvocationTargetError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        public InvocationTargetError(Throwable cause) {
            super(cause);
        }
    }

    public static final class ClassNotFoundError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        public ClassNotFoundError(Throwable cause) {
            super(cause);
        }

        public ClassNotFoundError(String detailMessage, Throwable cause) {
            super(detailMessage, cause);
        }
    }
}
