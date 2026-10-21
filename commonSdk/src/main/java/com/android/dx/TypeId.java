//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.android.dx;

import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import java.util.HashMap;
import java.util.Map;

public final class TypeId<T> {
    public static final TypeId<Boolean> BOOLEAN;
    public static final TypeId<Byte> BYTE;
    public static final TypeId<Character> CHAR;
    public static final TypeId<Double> DOUBLE;
    public static final TypeId<Float> FLOAT;
    public static final TypeId<Integer> INT;
    public static final TypeId<Long> LONG;
    public static final TypeId<Short> SHORT;
    public static final TypeId<Void> VOID;
    public static final TypeId<Object> OBJECT;
    public static final TypeId<String> STRING;
    private static final Map<Class<?>, TypeId<?>> PRIMITIVE_TO_TYPE;
    final String name;
    final Type ropType;
    final CstType constant;

    TypeId(Type ropType) {
        this(ropType.getDescriptor(), ropType);
    }

    TypeId(String name, Type ropType) {
        if (name != null && ropType != null) {
            this.name = name;
            this.ropType = ropType;
            this.constant = CstType.intern(ropType);
        } else {
            throw new NullPointerException();
        }
    }

    public static <T> TypeId<T> get(String name) {
        return new TypeId(name, Type.internReturnType(name));
    }

    public static <T> TypeId<T> get(Class<T> type) {
        if (type.isPrimitive()) {
            TypeId<T> result = (TypeId)PRIMITIVE_TO_TYPE.get(type);
            return result;
        } else {
            String name = type.getName().replace('.', '/');
            return get(type.isArray() ? name : 'L' + name + ';');
        }
    }

    public <V> FieldId<T, V> getField(TypeId<V> type, String name) {
        return new FieldId(this, type, name);
    }

    public MethodId<T, Void> getConstructor(TypeId<?>... parameters) {
        return new MethodId(this, VOID, "<init>", new TypeList(parameters));
    }

    public MethodId<T, Void> getStaticInitializer() {
        return new MethodId(this, VOID, "<clinit>", new TypeList(new TypeId[0]));
    }

    public <R> MethodId<T, R> getMethod(TypeId<R> returnType, String name, TypeId<?>... parameters) {
        return new MethodId(this, returnType, name, new TypeList(parameters));
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        return o instanceof TypeId && ((TypeId)o).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return this.name;
    }

    static {
        BOOLEAN = new TypeId(Type.BOOLEAN);
        BYTE = new TypeId(Type.BYTE);
        CHAR = new TypeId(Type.CHAR);
        DOUBLE = new TypeId(Type.DOUBLE);
        FLOAT = new TypeId(Type.FLOAT);
        INT = new TypeId(Type.INT);
        LONG = new TypeId(Type.LONG);
        SHORT = new TypeId(Type.SHORT);
        VOID = new TypeId(Type.VOID);
        OBJECT = new TypeId(Type.OBJECT);
        STRING = new TypeId(Type.STRING);
        PRIMITIVE_TO_TYPE = new HashMap();
        PRIMITIVE_TO_TYPE.put(Boolean.TYPE, BOOLEAN);
        PRIMITIVE_TO_TYPE.put(Byte.TYPE, BYTE);
        PRIMITIVE_TO_TYPE.put(Character.TYPE, CHAR);
        PRIMITIVE_TO_TYPE.put(Double.TYPE, DOUBLE);
        PRIMITIVE_TO_TYPE.put(Float.TYPE, FLOAT);
        PRIMITIVE_TO_TYPE.put(Integer.TYPE, INT);
        PRIMITIVE_TO_TYPE.put(Long.TYPE, LONG);
        PRIMITIVE_TO_TYPE.put(Short.TYPE, SHORT);
        PRIMITIVE_TO_TYPE.put(Void.TYPE, VOID);
    }
}
