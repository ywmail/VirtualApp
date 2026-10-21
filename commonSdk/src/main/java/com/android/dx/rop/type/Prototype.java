/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.type;

import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Prototype
implements Comparable<Prototype> {
    private static final ConcurrentMap<String, Prototype> internTable = new ConcurrentHashMap<String, Prototype>(10000, 0.75f);
    private final String descriptor;
    private final Type returnType;
    private final StdTypeList parameterTypes;
    private StdTypeList parameterFrameTypes;

    public static Prototype intern(String descriptor) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        Prototype result = (Prototype)internTable.get(descriptor);
        if (result != null) {
            return result;
        }
        result = Prototype.fromDescriptor(descriptor);
        return Prototype.putIntern(result);
    }

    public static Prototype fromDescriptor(String descriptor) {
        Prototype result = (Prototype)internTable.get(descriptor);
        if (result != null) {
            return result;
        }
        Type[] params = Prototype.makeParameterArray(descriptor);
        int paramCount = 0;
        int at = 1;
        while (true) {
            int startAt = at;
            char c = descriptor.charAt(at);
            if (c == ')') break;
            while (c == '[') {
                c = descriptor.charAt(++at);
            }
            if (c == 'L') {
                int endAt = descriptor.indexOf(59, at);
                if (endAt == -1) {
                    throw new IllegalArgumentException("bad descriptor");
                }
                at = endAt + 1;
            } else {
                ++at;
            }
            params[paramCount] = Type.intern(descriptor.substring(startAt, at));
            ++paramCount;
        }
        Type returnType = Type.internReturnType(descriptor.substring(++at));
        StdTypeList parameterTypes = new StdTypeList(paramCount);
        for (int i = 0; i < paramCount; ++i) {
            parameterTypes.set(i, params[i]);
        }
        return new Prototype(descriptor, returnType, parameterTypes);
    }

    public static void clearInternTable() {
        internTable.clear();
    }

    private static Type[] makeParameterArray(String descriptor) {
        int length = descriptor.length();
        if (descriptor.charAt(0) != '(') {
            throw new IllegalArgumentException("bad descriptor");
        }
        int closeAt = 0;
        int maxParams = 0;
        for (int i = 1; i < length; ++i) {
            char c = descriptor.charAt(i);
            if (c == ')') {
                closeAt = i;
                break;
            }
            if (c < 'A' || c > 'Z') continue;
            ++maxParams;
        }
        if (closeAt == 0 || closeAt == length - 1) {
            throw new IllegalArgumentException("bad descriptor");
        }
        if (descriptor.indexOf(41, closeAt + 1) != -1) {
            throw new IllegalArgumentException("bad descriptor");
        }
        return new Type[maxParams];
    }

    public static Prototype intern(String descriptor, Type definer, boolean isStatic, boolean isInit) {
        Prototype base = Prototype.intern(descriptor);
        if (isStatic) {
            return base;
        }
        if (isInit) {
            definer = definer.asUninitialized(Integer.MAX_VALUE);
        }
        return base.withFirstParameter(definer);
    }

    public static Prototype internInts(Type returnType, int count) {
        StringBuilder sb = new StringBuilder(100);
        sb.append('(');
        for (int i = 0; i < count; ++i) {
            sb.append('I');
        }
        sb.append(')');
        sb.append(returnType.getDescriptor());
        return Prototype.intern(sb.toString());
    }

    private Prototype(String descriptor, Type returnType, StdTypeList parameterTypes) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        if (returnType == null) {
            throw new NullPointerException("returnType == null");
        }
        if (parameterTypes == null) {
            throw new NullPointerException("parameterTypes == null");
        }
        this.descriptor = descriptor;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterFrameTypes = null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Prototype)) {
            return false;
        }
        return this.descriptor.equals(((Prototype)other).descriptor);
    }

    public int hashCode() {
        return this.descriptor.hashCode();
    }

    @Override
    public int compareTo(Prototype other) {
        if (this == other) {
            return 0;
        }
        int result = this.returnType.compareTo(other.returnType);
        if (result != 0) {
            return result;
        }
        int thisSize = this.parameterTypes.size();
        int otherSize = other.parameterTypes.size();
        int size = Math.min(thisSize, otherSize);
        for (int i = 0; i < size; ++i) {
            Type otherType;
            Type thisType = this.parameterTypes.get(i);
            result = thisType.compareTo(otherType = other.parameterTypes.get(i));
            if (result == 0) continue;
            return result;
        }
        if (thisSize < otherSize) {
            return -1;
        }
        if (thisSize > otherSize) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        return this.descriptor;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public StdTypeList getParameterTypes() {
        return this.parameterTypes;
    }

    public StdTypeList getParameterFrameTypes() {
        if (this.parameterFrameTypes == null) {
            int sz = this.parameterTypes.size();
            StdTypeList list = new StdTypeList(sz);
            boolean any = false;
            for (int i = 0; i < sz; ++i) {
                Type one = this.parameterTypes.get(i);
                if (one.isIntlike()) {
                    any = true;
                    one = Type.INT;
                }
                list.set(i, one);
            }
            this.parameterFrameTypes = any ? list : this.parameterTypes;
        }
        return this.parameterFrameTypes;
    }

    public Prototype withFirstParameter(Type param) {
        String newDesc = "(" + param.getDescriptor() + this.descriptor.substring(1);
        StdTypeList newParams = this.parameterTypes.withFirst(param);
        newParams.setImmutable();
        Prototype result = new Prototype(newDesc, this.returnType, newParams);
        return Prototype.putIntern(result);
    }

    private static Prototype putIntern(Prototype desc) {
        Prototype result = internTable.putIfAbsent(desc.getDescriptor(), desc);
        return result != null ? result : desc;
    }
}

