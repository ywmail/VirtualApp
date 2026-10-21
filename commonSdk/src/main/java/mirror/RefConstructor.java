/*
 * Decompiled with CFR 0.152.
 */
package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefStaticMethod;

public class RefConstructor<T> {
    private Constructor<?> ctor;

    public RefConstructor(Class<?> cls, Field field) throws NoSuchMethodException {
        if (field.isAnnotationPresent(MethodParams.class)) {
            Class<?>[] types = field.getAnnotation(MethodParams.class).value();
            this.ctor = cls.getDeclaredConstructor(types);
        } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
            String[] values = field.getAnnotation(MethodReflectParams.class).value();
            Class[] parameterTypes = new Class[values.length];
            int N = 0;
            while (N < values.length) {
                try {
                    Class<?> type = RefStaticMethod.getProtoType(values[N]);
                    if (type == null) {
                        type = Class.forName(values[N]);
                    }
                    parameterTypes[N] = type;
                    ++N;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.ctor = cls.getDeclaredConstructor(parameterTypes);
        } else {
            this.ctor = cls.getDeclaredConstructor(new Class[0]);
        }
        if (this.ctor != null && !this.ctor.isAccessible()) {
            this.ctor.setAccessible(true);
        }
    }

    public T newInstance() {
        try {
            return (T)this.ctor.newInstance(new Object[0]);
        }
        catch (Exception e) {
            return null;
        }
    }

    public T newInstance(Object ... params) {
        try {
            return (T)this.ctor.newInstance(params);
        }
        catch (Exception e) {
            return null;
        }
    }
}

