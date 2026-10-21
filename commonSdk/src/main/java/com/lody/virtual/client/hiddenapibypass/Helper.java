/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hiddenapibypass;

import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodType;
import java.lang.reflect.Member;

public class Helper {

    public static class NeverCall {
        static void a() {
        }

        static void b() {
        }
    }

    public static final class Class {
        private transient ClassLoader classLoader;
        private transient java.lang.Class<?> componentType;
        private transient Object dexCache;
        private transient Object extData;
        private transient Object[] ifTable;
        private transient String name;
        private transient java.lang.Class<?> superClass;
        private transient Object vtable;
        private transient long iFields;
        private transient long methods;
        private transient long sFields;
        private transient int accessFlags;
        private transient int classFlags;
        private transient int classSize;
        private transient int clinitThreadId;
        private transient int dexClassDefIndex;
        private volatile transient int dexTypeIndex;
        private transient int numReferenceInstanceFields;
        private transient int numReferenceStaticFields;
        private transient int objectSize;
        private transient int objectSizeAllocFastPath;
        private transient int primitiveType;
        private transient int referenceInstanceOffsets;
        private transient int status;
        private transient short copiedMethodsOffset;
        private transient short virtualMethodsOffset;
    }

    public static final class HandleInfo {
        private final Member member = null;
        private final MethodHandle handle = null;
    }

    public static final class MethodHandleImpl
    extends MethodHandle {
        private final MethodHandleInfo info = null;
    }

    public static class MethodHandle {
        private final MethodType type = null;
        private MethodType nominalType;
        private MethodHandle cachedSpreadInvoker;
        protected final int handleKind = 0;
        protected final long artFieldOrMethod = 0L;
    }
}

