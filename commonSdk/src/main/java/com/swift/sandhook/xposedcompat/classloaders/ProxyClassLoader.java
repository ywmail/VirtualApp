/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.xposedcompat.classloaders;

public class ProxyClassLoader
extends ClassLoader {
    private final ClassLoader mClassLoader;

    public ProxyClassLoader(ClassLoader parentCL, ClassLoader appCL) {
        super(parentCL);
        this.mClassLoader = appCL;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = this.mClassLoader.loadClass(name);
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        if (clazz == null && (clazz = super.loadClass(name, resolve)) == null) {
            throw new ClassNotFoundException();
        }
        return clazz;
    }
}

