/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.annotation;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;

public final class NameValuePair
implements Comparable<NameValuePair> {
    private final CstString name;
    private final Constant value;

    public NameValuePair(CstString name, Constant value) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return this.name.toHuman() + ":" + this.value;
    }

    public int hashCode() {
        return this.name.hashCode() * 31 + this.value.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof NameValuePair)) {
            return false;
        }
        NameValuePair otherPair = (NameValuePair)other;
        return this.name.equals(otherPair.name) && this.value.equals(otherPair.value);
    }

    @Override
    public int compareTo(NameValuePair other) {
        int result = this.name.compareTo(other.name);
        if (result != 0) {
            return result;
        }
        return this.value.compareTo(other.value);
    }

    public CstString getName() {
        return this.name;
    }

    public Constant getValue() {
        return this.value;
    }
}

