/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.cf.code.Merger;
import com.android.dx.cf.code.SimException;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;
import com.android.dx.util.MutabilityControl;

public final class ExecutionStack
extends MutabilityControl {
    private final TypeBearer[] stack;
    private final boolean[] local;
    private int stackPtr;

    public ExecutionStack(int maxStack) {
        super(maxStack != 0);
        this.stack = new TypeBearer[maxStack];
        this.local = new boolean[maxStack];
        this.stackPtr = 0;
    }

    public ExecutionStack copy() {
        ExecutionStack result = new ExecutionStack(this.stack.length);
        System.arraycopy(this.stack, 0, result.stack, 0, this.stack.length);
        System.arraycopy(this.local, 0, result.local, 0, this.local.length);
        result.stackPtr = this.stackPtr;
        return result;
    }

    public void annotate(ExceptionWithContext ex) {
        int limit = this.stackPtr - 1;
        for (int i = 0; i <= limit; ++i) {
            String idx = i == limit ? "top0" : Hex.u2(limit - i);
            ex.addContext("stack[" + idx + "]: " + ExecutionStack.stackElementString(this.stack[i]));
        }
    }

    public void makeInitialized(Type type) {
        if (this.stackPtr == 0) {
            return;
        }
        this.throwIfImmutable();
        Type initializedType = type.getInitializedType();
        for (int i = 0; i < this.stackPtr; ++i) {
            if (this.stack[i] != type) continue;
            this.stack[i] = initializedType;
        }
    }

    public int getMaxStack() {
        return this.stack.length;
    }

    public int size() {
        return this.stackPtr;
    }

    public void clear() {
        this.throwIfImmutable();
        for (int i = 0; i < this.stackPtr; ++i) {
            this.stack[i] = null;
            this.local[i] = false;
        }
        this.stackPtr = 0;
    }

    public void push(TypeBearer type) {
        int category;
        this.throwIfImmutable();
        try {
            type = type.getFrameType();
            category = type.getType().getCategory();
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
        if (this.stackPtr + category > this.stack.length) {
            ExecutionStack.throwSimException("overflow");
            return;
        }
        if (category == 2) {
            this.stack[this.stackPtr] = null;
            ++this.stackPtr;
        }
        this.stack[this.stackPtr] = type;
        ++this.stackPtr;
    }

    public void setLocal() {
        this.throwIfImmutable();
        this.local[this.stackPtr] = true;
    }

    public TypeBearer peek(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        if (n >= this.stackPtr) {
            return ExecutionStack.throwSimException("underflow");
        }
        return this.stack[this.stackPtr - n - 1];
    }

    public boolean peekLocal(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        if (n >= this.stackPtr) {
            throw new SimException("stack: underflow");
        }
        return this.local[this.stackPtr - n - 1];
    }

    public Type peekType(int n) {
        return this.peek(n).getType();
    }

    public TypeBearer pop() {
        this.throwIfImmutable();
        TypeBearer result = this.peek(0);
        this.stack[this.stackPtr - 1] = null;
        this.local[this.stackPtr - 1] = false;
        this.stackPtr -= result.getType().getCategory();
        return result;
    }

    public void change(int n, TypeBearer type) {
        this.throwIfImmutable();
        try {
            type = type.getFrameType();
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
        int idx = this.stackPtr - n - 1;
        TypeBearer orig = this.stack[idx];
        if (orig == null || orig.getType().getCategory() != type.getType().getCategory()) {
            ExecutionStack.throwSimException("incompatible substitution: " + ExecutionStack.stackElementString(orig) + " -> " + ExecutionStack.stackElementString(type));
        }
        this.stack[idx] = type;
    }

    public ExecutionStack merge(ExecutionStack other) {
        try {
            return Merger.mergeStack(this, other);
        }
        catch (SimException ex) {
            ex.addContext("underlay stack:");
            this.annotate(ex);
            ex.addContext("overlay stack:");
            other.annotate(ex);
            throw ex;
        }
    }

    private static String stackElementString(TypeBearer type) {
        if (type == null) {
            return "<invalid>";
        }
        return type.toString();
    }

    private static TypeBearer throwSimException(String msg) {
        throw new SimException("stack: " + msg);
    }
}

