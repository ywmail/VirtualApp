/*
 * Decompiled with CFR 0.152.
 */
package external.org.apache.commons.lang3.builder;

import external.org.apache.commons.lang3.ObjectUtils;
import external.org.apache.commons.lang3.builder.Builder;
import external.org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import external.org.apache.commons.lang3.builder.ToStringStyle;

public class ToStringBuilder
implements Builder<String> {
    private static volatile ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;
    private final StringBuffer buffer;
    private final Object object;
    private final ToStringStyle style;

    public static ToStringStyle getDefaultStyle() {
        return defaultStyle;
    }

    public static void setDefaultStyle(ToStringStyle style2) {
        if (style2 == null) {
            throw new IllegalArgumentException("The style must not be null");
        }
        defaultStyle = style2;
    }

    public static String reflectionToString(Object object) {
        return ReflectionToStringBuilder.toString(object);
    }

    public static String reflectionToString(Object object, ToStringStyle style2) {
        return ReflectionToStringBuilder.toString(object, style2);
    }

    public static String reflectionToString(Object object, ToStringStyle style2, boolean outputTransients) {
        return ReflectionToStringBuilder.toString(object, style2, outputTransients, false, null);
    }

    public static <T> String reflectionToString(T object, ToStringStyle style2, boolean outputTransients, Class<? super T> reflectUpToClass) {
        return ReflectionToStringBuilder.toString(object, style2, outputTransients, false, reflectUpToClass);
    }

    public ToStringBuilder(Object object) {
        this(object, null, null);
    }

    public ToStringBuilder(Object object, ToStringStyle style2) {
        this(object, style2, null);
    }

    public ToStringBuilder(Object object, ToStringStyle style2, StringBuffer buffer) {
        if (style2 == null) {
            style2 = ToStringBuilder.getDefaultStyle();
        }
        if (buffer == null) {
            buffer = new StringBuffer(512);
        }
        this.buffer = buffer;
        this.style = style2;
        this.object = object;
        style2.appendStart(buffer, object);
    }

    public ToStringBuilder append(boolean value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(boolean[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(byte value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(byte[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(char value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(char[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(double value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(double[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(float value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(float[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(int value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(int[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(long value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(long[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(Object obj) {
        this.style.append(this.buffer, null, obj, null);
        return this;
    }

    public ToStringBuilder append(Object[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(short value) {
        this.style.append(this.buffer, (String)null, value);
        return this;
    }

    public ToStringBuilder append(short[] array2) {
        this.style.append(this.buffer, (String)null, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, byte value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, char value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, double value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, double[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, double[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, float value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, float[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, float[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, int value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, long value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, long[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, long[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object obj) {
        this.style.append(this.buffer, fieldName, obj, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object obj, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, obj, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder append(String fieldName, short value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] array2) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)null);
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] array2, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array2, (Boolean)fullDetail);
        return this;
    }

    public ToStringBuilder appendAsObjectToString(Object object) {
        ObjectUtils.identityToString(this.getStringBuffer(), object);
        return this;
    }

    public ToStringBuilder appendSuper(String superToString) {
        if (superToString != null) {
            this.style.appendSuper(this.buffer, superToString);
        }
        return this;
    }

    public ToStringBuilder appendToString(String toString) {
        if (toString != null) {
            this.style.appendToString(this.buffer, toString);
        }
        return this;
    }

    public Object getObject() {
        return this.object;
    }

    public StringBuffer getStringBuffer() {
        return this.buffer;
    }

    public ToStringStyle getStyle() {
        return this.style;
    }

    public String toString() {
        if (this.getObject() == null) {
            this.getStringBuffer().append(this.getStyle().getNullText());
        } else {
            this.style.appendEnd(this.getStringBuffer(), this.getObject());
        }
        return this.getStringBuffer().toString();
    }

    @Override
    public String build() {
        return this.toString();
    }
}

