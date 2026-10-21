/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science;

import com.carlos.libcommon.StringFog;
import com.carlos.science.ServiceConfig;
import com.carlos.science.exception.AgencyException;
import com.carlos.science.utils.ReflectUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServiceAgency {
    private final HashMap<Class, Object> cacheMap = new LinkedHashMap<Class, Object>();
    private boolean isServiceConfigExists;

    public static <T> T getService(Class<T> tClass) {
        return InstanceHolder.instance.getServiceFromMap(tClass);
    }

    public static void clear() {
        InstanceHolder.instance.clearMap();
    }

    public <T> T getServiceFromMap(Class<T> tClass) {
        Object service;
        if (!this.isServiceConfigExists) {
            try {
                Class.forName("com.kook.controller.floatcontroller.ServiceConfig");
                this.isServiceConfigExists = true;
            }
            catch (ClassNotFoundException e) {
                throw new AgencyException("No class annotate with ServiceAgent.");
            }
        }
        if ((service = this.cacheMap.get(tClass)) == null) {
            for (ServiceConfig serviceEnum : ServiceConfig.values()) {
                try {
                    Class<?> serviceClass = Class.forName(serviceEnum.className);
                    if (!tClass.isAssignableFrom(serviceClass)) continue;
                    service = ReflectUtil.newInstance(serviceClass);
                    this.cacheMap.put(tClass, service);
                    return (T)service;
                }
                catch (ClassNotFoundException e) {
                    throw new AgencyException(e);
                }
            }
        }
        if (service == null) {
            throw new AgencyException("No class implements " + tClass.getName() + " and annotated with ServiceAgent.");
        }
        return (T)service;
    }

    public void clearMap() {
        this.cacheMap.clear();
    }

    private static class InstanceHolder {
        private static final ServiceAgency instance = new ServiceAgency();

        private InstanceHolder() {
        }
    }
}

