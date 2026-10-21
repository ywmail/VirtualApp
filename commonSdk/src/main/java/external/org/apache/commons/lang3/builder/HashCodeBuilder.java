/*
 * Decompiled with CFR 0.152.
 */
package external.org.apache.commons.lang3.builder;

import external.org.apache.commons.lang3.ArrayUtils;
import external.org.apache.commons.lang3.builder.Builder;
import external.org.apache.commons.lang3.builder.IDKey;
import external.org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilder
implements Builder<Integer> {
    private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal();
    private final int iConstant;
    private int iTotal = 0;

    static Set<IDKey> getRegistry() {
        return REGISTRY.get();
    }

    static boolean isRegistered(Object value) {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        return registry != null && registry.contains(new IDKey(value));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
        if (HashCodeBuilder.isRegistered(object)) {
            return;
        }
        try {
            HashCodeBuilder.register(object);
            AccessibleObject[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (AccessibleObject field : fields) {
                if (ArrayUtils.contains(excludeFields, ((Field)field).getName()) || ((Field)field).getName().indexOf(36) != -1 || !useTransients && Modifier.isTransient(((Field)field).getModifiers()) || Modifier.isStatic(((Field)field).getModifiers())) continue;
                try {
                    Object fieldValue = ((Field)field).get(object);
                    builder.append(fieldValue);
                }
                catch (IllegalAccessException e) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }
        finally {
            HashCodeBuilder.unregister(object);
        }
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
        return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, new String[0]);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
        return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, new String[0]);
    }


    public static <T> int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, T object, boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields) {
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        } else {
            HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            Class<?> clazz = object.getClass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);

            while(clazz.getSuperclass() != null && clazz != reflectUpToClass) {
                clazz = clazz.getSuperclass();
                reflectionAppend(object, clazz, builder, testTransients, excludeFields);
            }

            return builder.toHashCode();
        }
    }

    public static int reflectionHashCode(Object object, boolean testTransients) {
        return HashCodeBuilder.reflectionHashCode(17, 37, object, testTransients, null, new String[0]);
    }

    public static int reflectionHashCode(Object object, Collection<String> excludeFields) {
        return HashCodeBuilder.reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionHashCode(Object object, String ... excludeFields) {
        return HashCodeBuilder.reflectionHashCode(17, 37, object, false, null, excludeFields);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void register(Object value) {
        Class<HashCodeBuilder> clazz = HashCodeBuilder.class;
        synchronized (HashCodeBuilder.class) {
            if (HashCodeBuilder.getRegistry() == null) {
                REGISTRY.set(new HashSet());
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            HashCodeBuilder.getRegistry().add(new IDKey(value));
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static void unregister(Object value) {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        if (registry == null) return;
        registry.remove(new IDKey(value));
        Class<HashCodeBuilder> clazz = HashCodeBuilder.class;
        synchronized (HashCodeBuilder.class) {
            registry = HashCodeBuilder.getRegistry();
            if (registry == null || !registry.isEmpty()) return;
            REGISTRY.remove();
            // ** MonitorExit[var2_2] (shouldn't be in output)
            return;
        }
    }

    public HashCodeBuilder() {
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
        }
        if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        }
        if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
        }
        if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        }
        this.iConstant = multiplierNonZeroOddNumber;
        this.iTotal = initialNonZeroOddNumber;
    }

    public HashCodeBuilder append(boolean value) {
        this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (boolean element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(byte value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(byte[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (byte element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(char value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(char[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (char element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(double value) {
        return this.append(Double.doubleToLongBits(value));
    }

    public HashCodeBuilder append(double[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (double element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(float value) {
        this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(value);
        return this;
    }

    public HashCodeBuilder append(float[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (float element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(int value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(int[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(long value) {
        this.iTotal = this.iTotal * this.iConstant + (int)(value ^ value >> 32);
        return this;
    }

    public HashCodeBuilder append(long[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (long element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            this.iTotal *= this.iConstant;
        } else if (object.getClass().isArray()) {
            if (object instanceof long[]) {
                this.append((long[])object);
            } else if (object instanceof int[]) {
                this.append((int[])object);
            } else if (object instanceof short[]) {
                this.append((short[])object);
            } else if (object instanceof char[]) {
                this.append((char[])object);
            } else if (object instanceof byte[]) {
                this.append((byte[])object);
            } else if (object instanceof double[]) {
                this.append((double[])object);
            } else if (object instanceof float[]) {
                this.append((float[])object);
            } else if (object instanceof boolean[]) {
                this.append((boolean[])object);
            } else {
                this.append((Object[])object);
            }
        } else {
            this.iTotal = this.iTotal * this.iConstant + object.hashCode();
        }
        return this;
    }

    public HashCodeBuilder append(Object[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (Object element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder append(short value) {
        this.iTotal = this.iTotal * this.iConstant + value;
        return this;
    }

    public HashCodeBuilder append(short[] array2) {
        if (array2 == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (short element : array2) {
                this.append(element);
            }
        }
        return this;
    }

    public HashCodeBuilder appendSuper(int superHashCode) {
        this.iTotal = this.iTotal * this.iConstant + superHashCode;
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }

    @Override
    public Integer build() {
        return this.toHashCode();
    }

    public int hashCode() {
        return this.toHashCode();
    }
}

