/*
 * Decompiled with CFR 0.152.
 */
package libcore.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface CorePlatformApi {
    public Status status() default Status.LEGACY_ONLY;

    public static enum Status {
        STABLE,
        LEGACY_ONLY;

    }
}

