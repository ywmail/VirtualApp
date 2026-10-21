/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface HookMode {
    public static final int AUTO = 0;
    public static final int INLINE = 1;
    public static final int REPLACE = 2;

    public int value() default 0;
}

