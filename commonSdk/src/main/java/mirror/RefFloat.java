/*
 * Decompiled with CFR 0.152.
 */
package mirror;

import java.lang.reflect.Field;

public class RefFloat {
    private Field field;

    public RefFloat(Class cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public float get(Object object) {
        try {
            return this.field.getFloat(object);
        }
        catch (Exception e) {
            return 0.0f;
        }
    }

    public void set(Object obj, float value) {
        try {
            this.field.setFloat(obj, value);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

