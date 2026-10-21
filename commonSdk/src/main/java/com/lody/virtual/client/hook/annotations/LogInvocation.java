/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.client.hook.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface LogInvocation {
    public Condition value() default Condition.ALWAYS;

    public static enum Condition {
        NEVER{

            @Override
            public int getLogLevel(boolean isHooked, boolean isError) {
                return -1;
            }
        }
        ,
        ALWAYS{

            @Override
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isError ? 5 : 4;
            }
        }
        ,
        ON_ERROR{

            @Override
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isError ? 5 : -1;
            }
        }
        ,
        NOT_HOOKED{

            @Override
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isHooked ? -1 : (isError ? 5 : 4);
            }
        };


        public abstract int getLogLevel(boolean var1, boolean var2);
    }
}

