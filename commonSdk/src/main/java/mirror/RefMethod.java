package mirror;

import com.lody.virtual.StringFog;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefMethod<T> {
    private Method method;

    public RefMethod(Class<?> cls, Field field) throws NoSuchMethodException {
        Class<?> type;
        if (field.isAnnotationPresent(MethodParams.class)) {
            Class<?>[] types = ((MethodParams)field.getAnnotation(MethodParams.class)).value();
            for (int i = 0; i < types.length; ++i) {
                Class<?> clazz = types[i];
                if (clazz.getClassLoader() == this.getClass().getClassLoader()) {
                    // 查找 TYPE 字段
                    try {
                        Class.forName(clazz.getName());
                        type = (Class<?>) clazz.getField("TYPE").get(null);
                        types[i] = type;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.method = cls.getDeclaredMethod(field.getName(), types);
            this.method.setAccessible(true);

        } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
            String[] typeNames = ((MethodReflectParams)field.getAnnotation(MethodReflectParams.class)).value();
            Class<?>[] types = new Class[typeNames.length];
            for (int i = 0; i < typeNames.length; ++i) {
                type = RefStaticMethod.getProtoType(typeNames[i]);
                if (type == null) {
                    try {
                        type = Class.forName(typeNames[i]);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                types[i] = type;
            }
            this.method = cls.getDeclaredMethod(field.getName(), types);
            this.method.setAccessible(true);

        } else {
            Method[] methods = cls.getDeclaredMethods();
            for (int j = 0; j < methods.length; ++j) {
                Method method = methods[j];
                if (method.getName().equals(field.getName())) {
                    this.method = method;
                    this.method.setAccessible(true);
                    break;
                }
            }
        }

        if (this.method == null) {
            throw new NoSuchMethodException(field.getName());
        }
    }

    public T call(Object receiver, Object... args) {
        try {
            return (T) this.method.invoke(receiver, args);
        } catch (InvocationTargetException var4) {
            if (var4.getCause() != null) {
                var4.getCause().printStackTrace();
            } else {
                var4.printStackTrace();
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
        }
        return null;
    }

    public T callWithException(Object receiver, Object... args) throws Throwable {
        try {
            return (T) this.method.invoke(receiver, args);
        } catch (InvocationTargetException var4) {
            if (var4.getCause() != null) {
                throw var4.getCause();
            } else {
                throw var4;
            }
        }
    }

    public Class<?>[] paramList() {
        return this.method.getParameterTypes();
    }
}