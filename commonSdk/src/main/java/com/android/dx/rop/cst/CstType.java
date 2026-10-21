/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.cst;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.rop.type.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class CstType
extends TypedConstant {
    private static final ConcurrentMap<Type, CstType> interns = new ConcurrentHashMap<Type, CstType>(1000, 0.75f);
    public static final CstType OBJECT = new CstType(Type.OBJECT);
    public static final CstType BOOLEAN = new CstType(Type.BOOLEAN_CLASS);
    public static final CstType BYTE = new CstType(Type.BYTE_CLASS);
    public static final CstType CHARACTER = new CstType(Type.CHARACTER_CLASS);
    public static final CstType DOUBLE = new CstType(Type.DOUBLE_CLASS);
    public static final CstType FLOAT = new CstType(Type.FLOAT_CLASS);
    public static final CstType LONG = new CstType(Type.LONG_CLASS);
    public static final CstType INTEGER = new CstType(Type.INTEGER_CLASS);
    public static final CstType SHORT = new CstType(Type.SHORT_CLASS);
    public static final CstType VOID = new CstType(Type.VOID_CLASS);
    public static final CstType BOOLEAN_ARRAY = new CstType(Type.BOOLEAN_ARRAY);
    public static final CstType BYTE_ARRAY = new CstType(Type.BYTE_ARRAY);
    public static final CstType CHAR_ARRAY = new CstType(Type.CHAR_ARRAY);
    public static final CstType DOUBLE_ARRAY = new CstType(Type.DOUBLE_ARRAY);
    public static final CstType FLOAT_ARRAY = new CstType(Type.FLOAT_ARRAY);
    public static final CstType LONG_ARRAY = new CstType(Type.LONG_ARRAY);
    public static final CstType INT_ARRAY = new CstType(Type.INT_ARRAY);
    public static final CstType SHORT_ARRAY = new CstType(Type.SHORT_ARRAY);
    public static final CstType METHOD_HANDLE = new CstType(Type.METHOD_HANDLE);
    public static final CstType VAR_HANDLE = new CstType(Type.VAR_HANDLE);
    private final Type type;
    private CstString descriptor;

    private static void initInterns() {
        CstType.internInitial(OBJECT);
        CstType.internInitial(BOOLEAN);
        CstType.internInitial(BYTE);
        CstType.internInitial(CHARACTER);
        CstType.internInitial(DOUBLE);
        CstType.internInitial(FLOAT);
        CstType.internInitial(LONG);
        CstType.internInitial(INTEGER);
        CstType.internInitial(SHORT);
        CstType.internInitial(VOID);
        CstType.internInitial(BOOLEAN_ARRAY);
        CstType.internInitial(BYTE_ARRAY);
        CstType.internInitial(CHAR_ARRAY);
        CstType.internInitial(DOUBLE_ARRAY);
        CstType.internInitial(FLOAT_ARRAY);
        CstType.internInitial(LONG_ARRAY);
        CstType.internInitial(INT_ARRAY);
        CstType.internInitial(SHORT_ARRAY);
        CstType.internInitial(METHOD_HANDLE);
    }

    private static void internInitial(CstType cst) {
        if (interns.putIfAbsent(cst.getClassType(), cst) != null) {
            throw new IllegalStateException("Attempted re-init of " + cst);
        }
    }

    public static CstType forBoxedPrimitiveType(Type primitiveType) {
        switch (primitiveType.getBasicType()) {
            case 1: {
                return BOOLEAN;
            }
            case 2: {
                return BYTE;
            }
            case 3: {
                return CHARACTER;
            }
            case 4: {
                return DOUBLE;
            }
            case 5: {
                return FLOAT;
            }
            case 6: {
                return INTEGER;
            }
            case 7: {
                return LONG;
            }
            case 8: {
                return SHORT;
            }
            case 0: {
                return VOID;
            }
        }
        throw new IllegalArgumentException("not primitive: " + primitiveType);
    }

    public static CstType intern(Type type) {
        CstType cst = new CstType(type);
        CstType result = interns.putIfAbsent(type, cst);
        return result != null ? result : cst;
    }

    public CstType(Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (type == Type.KNOWN_NULL) {
            throw new UnsupportedOperationException("KNOWN_NULL is not representable");
        }
        this.type = type;
        this.descriptor = null;
    }

    public boolean equals(Object other) {
        if (!(other instanceof CstType)) {
            return false;
        }
        return this.type == ((CstType)other).type;
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    protected int compareTo0(Constant other) {
        String thisDescriptor = this.type.getDescriptor();
        String otherDescriptor = ((CstType)other).type.getDescriptor();
        return thisDescriptor.compareTo(otherDescriptor);
    }

    public String toString() {
        return "type{" + this.toHuman() + '}';
    }

    @Override
    public Type getType() {
        return Type.CLASS;
    }

    @Override
    public String typeName() {
        return "type";
    }

    @Override
    public boolean isCategory2() {
        return false;
    }

    @Override
    public String toHuman() {
        return this.type.toHuman();
    }

    public Type getClassType() {
        return this.type;
    }

    public CstString getDescriptor() {
        if (this.descriptor == null) {
            this.descriptor = new CstString(this.type.getDescriptor());
        }
        return this.descriptor;
    }

    public String getPackageName() {
        String descriptor = this.getDescriptor().getString();
        int lastSlash = descriptor.lastIndexOf(47);
        int lastLeftSquare = descriptor.lastIndexOf(91);
        if (lastSlash == -1) {
            return "default";
        }
        return descriptor.substring(lastLeftSquare + 2, lastSlash).replace('/', '.');
    }

    public static void clearInternTable() {
        interns.clear();
        CstType.initInterns();
    }

    static {
        CstType.initInterns();
    }
}

