/*
 * Decompiled with CFR 0.152.
 */
package com.swift.sandhook.utils;

import com.swift.sandhook.SandHook;

public class ParamWrapper {
    private static boolean is64Bit = SandHook.is64Bit();

    public static boolean support(Class objectType) {
        if (is64Bit) {
            return objectType != Float.TYPE && objectType != Double.TYPE;
        }
        return objectType != Float.TYPE && objectType != Double.TYPE && objectType != Long.TYPE;
    }

    public static Object addressToObject(Class objectType, long address) {
        if (is64Bit) {
            return ParamWrapper.addressToObject64(objectType, address);
        }
        return ParamWrapper.addressToObject32(objectType, (int)address);
    }

    public static Object addressToObject64(Class objectType, long address) {
        if (objectType == null) {
            return null;
        }
        if (objectType.isPrimitive()) {
            if (objectType == Integer.TYPE) {
                return (int)address;
            }
            if (objectType == Long.TYPE) {
                return address;
            }
            if (objectType == Short.TYPE) {
                return (short)address;
            }
            if (objectType == Byte.TYPE) {
                return (byte)address;
            }
            if (objectType == Character.TYPE) {
                return Character.valueOf((char)address);
            }
            if (objectType == Boolean.TYPE) {
                return address != 0L;
            }
            throw new RuntimeException("unknown type: " + objectType.toString());
        }
        return SandHook.getObject(address);
    }

    public static Object addressToObject32(Class objectType, int address) {
        if (objectType == null) {
            return null;
        }
        if (objectType.isPrimitive()) {
            if (objectType == Integer.TYPE) {
                return address;
            }
            if (objectType == Short.TYPE) {
                return (short)address;
            }
            if (objectType == Byte.TYPE) {
                return (byte)address;
            }
            if (objectType == Character.TYPE) {
                return Character.valueOf((char)address);
            }
            if (objectType == Boolean.TYPE) {
                return address != 0;
            }
            throw new RuntimeException("unknown type: " + objectType.toString());
        }
        return SandHook.getObject(address);
    }

    public static long objectToAddress(Class objectType, Object object) {
        if (is64Bit) {
            return ParamWrapper.objectToAddress64(objectType, object);
        }
        return ParamWrapper.objectToAddress32(objectType, object);
    }

    public static int objectToAddress32(Class objectType, Object object) {
        if (object == null) {
            return 0;
        }
        if (objectType.isPrimitive()) {
            if (objectType == Integer.TYPE) {
                return (Integer)object;
            }
            if (objectType == Short.TYPE) {
                return ((Short)object).shortValue();
            }
            if (objectType == Byte.TYPE) {
                return ((Byte)object).byteValue();
            }
            if (objectType == Character.TYPE) {
                return ((Character)object).charValue();
            }
            if (objectType == Boolean.TYPE) {
                return Boolean.TRUE.equals(object) ? 1 : 0;
            }
            throw new RuntimeException("unknown type: " + objectType.toString());
        }
        return (int)SandHook.getObjectAddress(object);
    }

    public static long objectToAddress64(Class objectType, Object object) {
        if (object == null) {
            return 0L;
        }
        if (objectType.isPrimitive()) {
            if (objectType == Integer.TYPE) {
                return ((Integer)object).intValue();
            }
            if (objectType == Long.TYPE) {
                return (Long)object;
            }
            if (objectType == Short.TYPE) {
                return ((Short)object).shortValue();
            }
            if (objectType == Byte.TYPE) {
                return ((Byte)object).byteValue();
            }
            if (objectType == Character.TYPE) {
                return ((Character)object).charValue();
            }
            if (objectType == Boolean.TYPE) {
                return Boolean.TRUE.equals(object) ? 1L : 0L;
            }
            throw new RuntimeException("unknown type: " + objectType.toString());
        }
        return SandHook.getObjectAddress(object);
    }
}

