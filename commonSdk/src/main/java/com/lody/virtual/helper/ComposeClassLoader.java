/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper;

public class ComposeClassLoader
extends ClassLoader {
    private final ClassLoader mAppClassLoader;

    public ComposeClassLoader(ClassLoader parent, ClassLoader appClassLoader) {
        super(parent);
        this.mAppClassLoader = appClassLoader;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = this.mAppClassLoader.loadClass(name);
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        if (clazz == null) {
            clazz = super.loadClass(name, resolve);
        }
        if (clazz == null) {
            throw new ClassNotFoundException();
        }
        return clazz;
    }
}

