/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.annotation;

import com.android.dx.util.ToHuman;

public enum AnnotationVisibility implements ToHuman
{
    RUNTIME("runtime"),
    BUILD("build"),
    SYSTEM("system"),
    EMBEDDED("embedded");

    private final String human;

    private AnnotationVisibility(String human) {
        this.human = human;
    }

    @Override
    public String toHuman() {
        return this.human;
    }
}

