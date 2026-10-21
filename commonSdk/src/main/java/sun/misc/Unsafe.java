/*
 * Decompiled with CFR 0.152.
 */
package sun.misc;

import com.lody.virtual.StringFog;
import java.lang.reflect.Field;

public class Unsafe {
    public native long getLong(Object var1, long var2);

    public native void putLong(Object var1, long var2, long var4);

    public native int getInt(Object var1, long var2);

    public native void putInt(Object var1, long var2, int var4);

    public native short getShort(Object var1, long var2);

    public native Object getObject(Object var1, long var2);

    public native void putObject(Object var1, long var2, Object var4);

    public native byte getByte(long var1);

    public native void putByte(long var1, byte var3);

    public native int addressSize();

    public native int getInt(long var1);

    public native long getLong(long var1);

    public int arrayBaseOffset(Class clazz) {
        throw new RuntimeException("Stub!");
    }

    public long objectFieldOffset(Field field) {
        throw new RuntimeException("Stub!");
    }
}

