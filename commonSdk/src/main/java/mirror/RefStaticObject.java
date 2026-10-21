/*
 * Decompiled with CFR 0.152.
 */
package mirror;

import java.lang.reflect.Field;

public class RefStaticObject<T> {
    private Field field;

    public RefStaticObject(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public Class<?> type() {
        return this.field.getType();
    }

    public T get() {
        Object obj = null;
        try {
            obj = this.field.get(null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return (T)obj;
    }

    public void set(T obj) {
        try {
            this.field.set(null, obj);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

