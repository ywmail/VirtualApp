/*
 * Decompiled with CFR 0.152.
 */
package mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import mirror.RefBoolean;
import mirror.RefConstructor;
import mirror.RefDouble;
import mirror.RefFloat;
import mirror.RefInt;
import mirror.RefLong;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticInt;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public final class RefClass {
    private static HashMap<Class<?>, Constructor<?>> REF_TYPES = new HashMap();

    public static Class<?> load(Class<?> mappingClass, String className) {
        try {
            return RefClass.load(mappingClass, Class.forName(className));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Class load(Class mappingClass, Class<?> realClass) {
        Field[] fields;
        for (Field field : fields = mappingClass.getDeclaredFields()) {
            try {
                Constructor<?> constructor;
                if (!Modifier.isStatic(field.getModifiers()) || (constructor = REF_TYPES.get(field.getType())) == null) continue;
                field.set(null, constructor.newInstance(realClass, field));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return realClass;
    }

    static {
        try {
            REF_TYPES.put(RefObject.class, RefObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefMethod.class, RefMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefInt.class, RefInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefLong.class, RefLong.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefFloat.class, RefFloat.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefDouble.class, RefDouble.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefBoolean.class, RefBoolean.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticObject.class, RefStaticObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticInt.class, RefStaticInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticMethod.class, RefStaticMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefConstructor.class, RefConstructor.class.getConstructor(Class.class, Field.class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

