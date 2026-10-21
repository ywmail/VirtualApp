/*
 * Decompiled with CFR 0.152.
 */
package mirror;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class RefClassAttr {
    public static List<String> getClassField(Class clz) {
        Field[] declaredFields = clz.getDeclaredFields();
        ArrayList<String> fieldList = new ArrayList<String>();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            fieldList.add(fieldName);
        }
        return fieldList;
    }

    public static String getFieldStringValueByObject(String fieldName, Object object) {
        Class<?> clz = object.getClass();
        try {
            Field field = clz.getField(fieldName);
            Class<?> type = field.getType();
            Object value = field.get(object);
            if (value instanceof String) {
                return (String)value;
            }
            if (value instanceof Integer || value instanceof Double || value instanceof Float) {
                return String.valueOf(value);
            }
            return String.valueOf(value);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

