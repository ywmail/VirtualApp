/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.type;

import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Type implements TypeBearer, Comparable<Type> {
    private static final ConcurrentMap<String, Type> internTable = new ConcurrentHashMap<String, Type>(10000, 0.75f);
    public static final int BT_VOID = 0;
    public static final int BT_BOOLEAN = 1;
    public static final int BT_BYTE = 2;
    public static final int BT_CHAR = 3;
    public static final int BT_DOUBLE = 4;
    public static final int BT_FLOAT = 5;
    public static final int BT_INT = 6;
    public static final int BT_LONG = 7;
    public static final int BT_SHORT = 8;
    public static final int BT_OBJECT = 9;
    public static final int BT_ADDR = 10;
    public static final int BT_COUNT = 11;
    public static final Type BOOLEAN = new Type("Z", 1);
    public static final Type BYTE = new Type("B", 2);
    public static final Type CHAR = new Type("C", 3);
    public static final Type DOUBLE = new Type("D", 4);
    public static final Type FLOAT = new Type("F", 5);
    public static final Type INT = new Type("I", 6);
    public static final Type LONG = new Type("J", 7);
    public static final Type SHORT = new Type("S", 8);
    public static final Type VOID = new Type("V", 0);
    public static final Type KNOWN_NULL = new Type("<null>", 9);
    public static final Type RETURN_ADDRESS = new Type("<addr>", 10);
    public static final Type ANNOTATION = new Type("Ljava/lang/annotation/Annotation;", 9);
    public static final Type CLASS = new Type("Ljava/lang/Class;", 9);
    public static final Type CLONEABLE = new Type("Ljava/lang/Cloneable;", 9);
    public static final Type METHOD_HANDLE = new Type("Ljava/lang/invoke/MethodHandle;", 9);
    public static final Type METHOD_TYPE = new Type("Ljava/lang/invoke/MethodType;", 9);
    public static final Type VAR_HANDLE = new Type("Ljava/lang/invoke/VarHandle;", 9);
    public static final Type OBJECT = new Type("Ljava/lang/Object;", 9);
    public static final Type SERIALIZABLE = new Type("Ljava/io/Serializable;", 9);
    public static final Type STRING = new Type("Ljava/lang/String;", 9);
    public static final Type THROWABLE = new Type("Ljava/lang/Throwable;", 9);
    public static final Type BOOLEAN_CLASS = new Type("Ljava/lang/Boolean;", 9);
    public static final Type BYTE_CLASS = new Type("Ljava/lang/Byte;", 9);
    public static final Type CHARACTER_CLASS = new Type("Ljava/lang/Character;", 9);
    public static final Type DOUBLE_CLASS = new Type("Ljava/lang/Double;", 9);
    public static final Type FLOAT_CLASS = new Type("Ljava/lang/Float;", 9);
    public static final Type INTEGER_CLASS = new Type("Ljava/lang/Integer;", 9);
    public static final Type LONG_CLASS = new Type("Ljava/lang/Long;", 9);
    public static final Type SHORT_CLASS = new Type("Ljava/lang/Short;", 9);
    public static final Type VOID_CLASS = new Type("Ljava/lang/Void;", 9);
    public static final Type BOOLEAN_ARRAY = new Type("[" + Type.BOOLEAN.descriptor, 9);
    public static final Type BYTE_ARRAY = new Type("[" + Type.BYTE.descriptor, 9);
    public static final Type CHAR_ARRAY = new Type("[" + Type.CHAR.descriptor, 9);
    public static final Type DOUBLE_ARRAY = new Type("[" + Type.DOUBLE.descriptor, 9);
    public static final Type FLOAT_ARRAY = new Type("[" + Type.FLOAT.descriptor, 9);
    public static final Type INT_ARRAY = new Type("[" + Type.INT.descriptor, 9);
    public static final Type LONG_ARRAY = new Type("[" + Type.LONG.descriptor, 9);
    public static final Type OBJECT_ARRAY = new Type("[" + Type.OBJECT.descriptor, 9);
    public static final Type SHORT_ARRAY = new Type("[" + Type.SHORT.descriptor, 9);
    private final String descriptor;
    private final int basicType;
    private final int newAt;
    private String className;
    private Type arrayType;
    private Type componentType;
    private Type initializedType;

    private static void initInterns() {
        Type.putIntern(BOOLEAN);
        Type.putIntern(BYTE);
        Type.putIntern(CHAR);
        Type.putIntern(DOUBLE);
        Type.putIntern(FLOAT);
        Type.putIntern(INT);
        Type.putIntern(LONG);
        Type.putIntern(SHORT);
        Type.putIntern(ANNOTATION);
        Type.putIntern(CLASS);
        Type.putIntern(CLONEABLE);
        Type.putIntern(METHOD_HANDLE);
        Type.putIntern(VAR_HANDLE);
        Type.putIntern(OBJECT);
        Type.putIntern(SERIALIZABLE);
        Type.putIntern(STRING);
        Type.putIntern(THROWABLE);
        Type.putIntern(BOOLEAN_CLASS);
        Type.putIntern(BYTE_CLASS);
        Type.putIntern(CHARACTER_CLASS);
        Type.putIntern(DOUBLE_CLASS);
        Type.putIntern(FLOAT_CLASS);
        Type.putIntern(INTEGER_CLASS);
        Type.putIntern(LONG_CLASS);
        Type.putIntern(SHORT_CLASS);
        Type.putIntern(VOID_CLASS);
        Type.putIntern(BOOLEAN_ARRAY);
        Type.putIntern(BYTE_ARRAY);
        Type.putIntern(CHAR_ARRAY);
        Type.putIntern(DOUBLE_ARRAY);
        Type.putIntern(FLOAT_ARRAY);
        Type.putIntern(INT_ARRAY);
        Type.putIntern(LONG_ARRAY);
        Type.putIntern(OBJECT_ARRAY);
        Type.putIntern(SHORT_ARRAY);
    }

    public static Type intern(String descriptor) {
        char firstChar;
        Type result = (Type)internTable.get(descriptor);
        if (result != null) {
            return result;
        }
        try {
            firstChar = descriptor.charAt(0);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("descriptor is empty");
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("descriptor == null");
        }
        if (firstChar == '[') {
            result = Type.intern(descriptor.substring(1));
            return result.getArrayType();
        }
        int length = descriptor.length();
        if (firstChar != 'L' || descriptor.charAt(length - 1) != ';') {
            throw new IllegalArgumentException("bad descriptor: " + descriptor);
        }
        int limit = length - 1;
        block7: for (int i = 1; i < limit; ++i) {
            char c = descriptor.charAt(i);
            switch (c) {
                case '(': 
                case ')': 
                case '.': 
                case ';': 
                case '[': {
                    throw new IllegalArgumentException("bad descriptor: " + descriptor);
                }
                case '/': {
                    if (i != 1 && i != length - 1 && descriptor.charAt(i - 1) != '/') continue block7;
                    throw new IllegalArgumentException("bad descriptor: " + descriptor);
                }
            }
        }
        result = new Type(descriptor, 9);
        return Type.putIntern(result);
    }

    public static Type internReturnType(String descriptor) {
        try {
            if (descriptor.equals("V")) {
                return VOID;
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("descriptor == null");
        }
        return Type.intern(descriptor);
    }

    public static Type internClassName(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (name.startsWith("[")) {
            return Type.intern(name);
        }
        return Type.intern('L' + name + ';');
    }

    private Type(String descriptor, int basicType, int newAt) {
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        if (basicType < 0 || basicType >= 11) {
            throw new IllegalArgumentException("bad basicType");
        }
        if (newAt < -1) {
            throw new IllegalArgumentException("newAt < -1");
        }
        this.descriptor = descriptor;
        this.basicType = basicType;
        this.newAt = newAt;
        this.arrayType = null;
        this.componentType = null;
        this.initializedType = null;
    }

    private Type(String descriptor, int basicType) {
        this(descriptor, basicType, -1);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Type)) {
            return false;
        }
        return this.descriptor.equals(((Type)other).descriptor);
    }

    public int hashCode() {
        return this.descriptor.hashCode();
    }

    @Override
    public int compareTo(Type other) {
        return this.descriptor.compareTo(other.descriptor);
    }

    public String toString() {
        return this.descriptor;
    }

    @Override
    public String toHuman() {
        switch (this.basicType) {
            case 0: {
                return "void";
            }
            case 1: {
                return "boolean";
            }
            case 2: {
                return "byte";
            }
            case 3: {
                return "char";
            }
            case 4: {
                return "double";
            }
            case 5: {
                return "float";
            }
            case 6: {
                return "int";
            }
            case 7: {
                return "long";
            }
            case 8: {
                return "short";
            }
            case 9: {
                break;
            }
            default: {
                return this.descriptor;
            }
        }
        if (this.isArray()) {
            return this.getComponentType().toHuman() + "[]";
        }
        return this.getClassName().replace("/", ".");
    }

    @Override
    public Type getType() {
        return this;
    }

    @Override
    public Type getFrameType() {
        switch (this.basicType) {
            case 1: 
            case 2: 
            case 3: 
            case 6: 
            case 8: {
                return INT;
            }
        }
        return this;
    }

    @Override
    public int getBasicType() {
        return this.basicType;
    }

    @Override
    public int getBasicFrameType() {
        switch (this.basicType) {
            case 1: 
            case 2: 
            case 3: 
            case 6: 
            case 8: {
                return 6;
            }
        }
        return this.basicType;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public String getClassName() {
        if (this.className == null) {
            if (!this.isReference()) {
                throw new IllegalArgumentException("not an object type: " + this.descriptor);
            }
            this.className = this.descriptor.charAt(0) == '[' ? this.descriptor : this.descriptor.substring(1, this.descriptor.length() - 1);
        }
        return this.className;
    }

    public int getCategory() {
        switch (this.basicType) {
            case 4: 
            case 7: {
                return 2;
            }
        }
        return 1;
    }

    public boolean isCategory1() {
        switch (this.basicType) {
            case 4: 
            case 7: {
                return false;
            }
        }
        return true;
    }

    public boolean isCategory2() {
        switch (this.basicType) {
            case 4: 
            case 7: {
                return true;
            }
        }
        return false;
    }

    public boolean isIntlike() {
        switch (this.basicType) {
            case 1: 
            case 2: 
            case 3: 
            case 6: 
            case 8: {
                return true;
            }
        }
        return false;
    }

    public boolean isPrimitive() {
        switch (this.basicType) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                return true;
            }
        }
        return false;
    }

    public boolean isReference() {
        return this.basicType == 9;
    }

    public boolean isArray() {
        return this.descriptor.charAt(0) == '[';
    }

    public boolean isArrayOrKnownNull() {
        return this.isArray() || this.equals(KNOWN_NULL);
    }

    public boolean isUninitialized() {
        return this.newAt >= 0;
    }

    public int getNewAt() {
        return this.newAt;
    }

    public Type getInitializedType() {
        if (this.initializedType == null) {
            throw new IllegalArgumentException("initialized type: " + this.descriptor);
        }
        return this.initializedType;
    }

    public Type getArrayType() {
        if (this.arrayType == null) {
            this.arrayType = Type.putIntern(new Type('[' + this.descriptor, 9));
        }
        return this.arrayType;
    }

    public Type getComponentType() {
        if (this.componentType == null) {
            if (this.descriptor.charAt(0) != '[') {
                throw new IllegalArgumentException("not an array type: " + this.descriptor);
            }
            this.componentType = Type.intern(this.descriptor.substring(1));
        }
        return this.componentType;
    }

    public Type asUninitialized(int newAt) {
        if (newAt < 0) {
            throw new IllegalArgumentException("newAt < 0");
        }
        if (!this.isReference()) {
            throw new IllegalArgumentException("not a reference type: " + this.descriptor);
        }
        if (this.isUninitialized()) {
            throw new IllegalArgumentException("already uninitialized: " + this.descriptor);
        }
        String newDesc = 'N' + Hex.u2(newAt) + this.descriptor;
        Type result = new Type(newDesc, 9, newAt);
        result.initializedType = this;
        return Type.putIntern(result);
    }

    private static Type putIntern(Type type) {
        Type result = internTable.putIfAbsent(type.getDescriptor(), type);
        return result != null ? result : type;
    }

    public static void clearInternTable() {
        internTable.clear();
        Type.initInterns();
    }

    static {
        Type.initInterns();
    }
}

