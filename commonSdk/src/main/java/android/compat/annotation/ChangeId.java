/*
 * Decompiled with CFR 0.152.
 */
package android.compat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.FIELD, ElementType.PARAMETER})
public @interface ChangeId {
}

