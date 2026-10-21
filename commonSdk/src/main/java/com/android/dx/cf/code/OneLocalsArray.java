/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.cf.code.LocalsArray;
import com.android.dx.cf.code.LocalsArraySet;
import com.android.dx.cf.code.Merger;
import com.android.dx.cf.code.SimException;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;

public class OneLocalsArray
extends LocalsArray {
    private final TypeBearer[] locals;

    public OneLocalsArray(int maxLocals) {
        super(maxLocals != 0);
        this.locals = new TypeBearer[maxLocals];
    }

    @Override
    public OneLocalsArray copy() {
        OneLocalsArray result = new OneLocalsArray(this.locals.length);
        System.arraycopy(this.locals, 0, result.locals, 0, this.locals.length);
        return result;
    }

    @Override
    public void annotate(ExceptionWithContext ex) {
        for (int i = 0; i < this.locals.length; ++i) {
            TypeBearer type = this.locals[i];
            String s = type == null ? "<invalid>" : type.toString();
            ex.addContext("locals[" + Hex.u2(i) + "]: " + s);
        }
    }

    @Override
    public String toHuman() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.locals.length; ++i) {
            TypeBearer type = this.locals[i];
            String s = type == null ? "<invalid>" : type.toString();
            sb.append("locals[" + Hex.u2(i) + "]: " + s + "\n");
        }
        return sb.toString();
    }

    @Override
    public void makeInitialized(Type type) {
        int len = this.locals.length;
        if (len == 0) {
            return;
        }
        this.throwIfImmutable();
        Type initializedType = type.getInitializedType();
        for (int i = 0; i < len; ++i) {
            if (this.locals[i] != type) continue;
            this.locals[i] = initializedType;
        }
    }

    @Override
    public int getMaxLocals() {
        return this.locals.length;
    }

    @Override
    public void set(int idx, TypeBearer type) {
        TypeBearer prev;
        this.throwIfImmutable();
        try {
            type = type.getFrameType();
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
        if (idx < 0) {
            throw new IndexOutOfBoundsException("idx < 0");
        }
        if (type.getType().isCategory2()) {
            this.locals[idx + 1] = null;
        }
        this.locals[idx] = type;
        if (idx != 0 && (prev = this.locals[idx - 1]) != null && prev.getType().isCategory2()) {
            this.locals[idx - 1] = null;
        }
    }

    @Override
    public void set(RegisterSpec spec) {
        this.set(spec.getReg(), spec);
    }

    @Override
    public void invalidate(int idx) {
        this.throwIfImmutable();
        this.locals[idx] = null;
    }

    @Override
    public TypeBearer getOrNull(int idx) {
        return this.locals[idx];
    }

    @Override
    public TypeBearer get(int idx) {
        TypeBearer result = this.locals[idx];
        if (result == null) {
            return OneLocalsArray.throwSimException(idx, "invalid");
        }
        return result;
    }

    @Override
    public TypeBearer getCategory1(int idx) {
        TypeBearer result = this.get(idx);
        Type type = result.getType();
        if (type.isUninitialized()) {
            return OneLocalsArray.throwSimException(idx, "uninitialized instance");
        }
        if (type.isCategory2()) {
            return OneLocalsArray.throwSimException(idx, "category-2");
        }
        return result;
    }

    @Override
    public TypeBearer getCategory2(int idx) {
        TypeBearer result = this.get(idx);
        if (result.getType().isCategory1()) {
            return OneLocalsArray.throwSimException(idx, "category-1");
        }
        return result;
    }

    @Override
    public LocalsArray merge(LocalsArray other) {
        if (other instanceof OneLocalsArray) {
            return this.merge((OneLocalsArray)other);
        }
        return other.merge(this);
    }

    public OneLocalsArray merge(OneLocalsArray other) {
        try {
            return Merger.mergeLocals(this, other);
        }
        catch (SimException ex) {
            ex.addContext("underlay locals:");
            this.annotate(ex);
            ex.addContext("overlay locals:");
            other.annotate(ex);
            throw ex;
        }
    }

    @Override
    public LocalsArraySet mergeWithSubroutineCaller(LocalsArray other, int predLabel) {
        LocalsArraySet result = new LocalsArraySet(this.getMaxLocals());
        return result.mergeWithSubroutineCaller(other, predLabel);
    }

    @Override
    protected OneLocalsArray getPrimary() {
        return this;
    }

    private static TypeBearer throwSimException(int idx, String msg) {
        throw new SimException("local " + Hex.u2(idx) + ": " + msg);
    }
}

