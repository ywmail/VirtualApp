/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.Dex;
import com.android.dex.util.Unsigned;

public class MethodHandle
implements Comparable<MethodHandle> {
    private final Dex dex;
    private final MethodHandleType methodHandleType;
    private final int unused1;
    private final int fieldOrMethodId;
    private final int unused2;

    public MethodHandle(Dex dex, MethodHandleType methodHandleType, int unused1, int fieldOrMethodId, int unused2) {
        this.dex = dex;
        this.methodHandleType = methodHandleType;
        this.unused1 = unused1;
        this.fieldOrMethodId = fieldOrMethodId;
        this.unused2 = unused2;
    }

    @Override
    public int compareTo(MethodHandle o) {
        if (this.methodHandleType != o.methodHandleType) {
            return this.methodHandleType.compareTo(o.methodHandleType);
        }
        return Unsigned.compare(this.fieldOrMethodId, o.fieldOrMethodId);
    }

    public MethodHandleType getMethodHandleType() {
        return this.methodHandleType;
    }

    public int getUnused1() {
        return this.unused1;
    }

    public int getFieldOrMethodId() {
        return this.fieldOrMethodId;
    }

    public int getUnused2() {
        return this.unused2;
    }

    public void writeTo(Dex.Section out) {
        out.writeUnsignedShort(this.methodHandleType.value);
        out.writeUnsignedShort(this.unused1);
        out.writeUnsignedShort(this.fieldOrMethodId);
        out.writeUnsignedShort(this.unused2);
    }

    public String toString() {
        if (this.dex == null) {
            return (Object)((Object)this.methodHandleType) + " " + this.fieldOrMethodId;
        }
        return (Object)((Object)this.methodHandleType) + " " + (this.methodHandleType.isField() ? (Comparable)this.dex.fieldIds().get(this.fieldOrMethodId) : (Comparable)this.dex.methodIds().get(this.fieldOrMethodId));
    }

    public static enum MethodHandleType {
        METHOD_HANDLE_TYPE_STATIC_PUT(0),
        METHOD_HANDLE_TYPE_STATIC_GET(1),
        METHOD_HANDLE_TYPE_INSTANCE_PUT(2),
        METHOD_HANDLE_TYPE_INSTANCE_GET(3),
        METHOD_HANDLE_TYPE_INVOKE_STATIC(4),
        METHOD_HANDLE_TYPE_INVOKE_INSTANCE(5),
        METHOD_HANDLE_TYPE_INVOKE_DIRECT(6),
        METHOD_HANDLE_TYPE_INVOKE_CONSTRUCTOR(7),
        METHOD_HANDLE_TYPE_INVOKE_INTERFACE(8);

        private final int value;

        private MethodHandleType(int value) {
            this.value = value;
        }

        static MethodHandleType fromValue(int value) {
            for (MethodHandleType methodHandleType : MethodHandleType.values()) {
                if (methodHandleType.value != value) continue;
                return methodHandleType;
            }
            throw new IllegalArgumentException(String.valueOf(value));
        }

        public boolean isField() {
            switch (this) {
                case METHOD_HANDLE_TYPE_STATIC_PUT: 
                case METHOD_HANDLE_TYPE_STATIC_GET: 
                case METHOD_HANDLE_TYPE_INSTANCE_PUT: 
                case METHOD_HANDLE_TYPE_INSTANCE_GET: {
                    return true;
                }
            }
            return false;
        }
    }
}

