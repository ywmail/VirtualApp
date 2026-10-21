/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science.utils;

import com.carlos.libcommon.StringFog;
import com.carlos.science.exception.AgencyException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static <T> T newInstance(Class<T> cls) {
        T instance = null;
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            instance = constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            throw new AgencyException("Do not do strange operation in the constructor.");
        }
        return instance;
    }

    public static Object invokeMethod(Object targetObject, String methodName, Object[] params, Class[] paramTypes) {
        Object returnObj = null;
        if (targetObject == null || methodName == null || methodName.isEmpty()) {
            return null;
        }
        AccessibleObject method = null;
        for (Class<?> cls = targetObject.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                method = cls.getDeclaredMethod(methodName, paramTypes);
                break;
            }
            catch (Exception exception) {
                continue;
            }
        }
        if (method != null) {
            method.setAccessible(true);
            try {
                returnObj = ((Method)method).invoke(targetObject, params);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnObj;
    }
}

