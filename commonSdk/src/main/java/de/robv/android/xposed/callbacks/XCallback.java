/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package de.robv.android.xposed.callbacks;

import android.os.Bundle;
import de.robv.android.xposed.XposedBridge;
import java.io.Serializable;

public abstract class XCallback
implements Comparable<XCallback> {
    public final int priority;
    public static final int PRIORITY_DEFAULT = 50;
    public static final int PRIORITY_LOWEST = -10000;
    public static final int PRIORITY_HIGHEST = 10000;

    @Deprecated
    public XCallback() {
        this.priority = 50;
    }

    public XCallback(int priority) {
        this.priority = priority;
    }

    public static void callAll(Param param) {
        if (param.callbacks == null) {
            throw new IllegalStateException("This object was not created for use with callAll");
        }
        for (int i = 0; i < param.callbacks.length; ++i) {
            try {
                ((XCallback)param.callbacks[i]).call(param);
                continue;
            }
            catch (Throwable t) {
                XposedBridge.log(t);
            }
        }
    }

    protected void call(Param param) throws Throwable {
    }

    @Override
    public int compareTo(XCallback other) {
        if (this == other) {
            return 0;
        }
        if (other.priority != this.priority) {
            return other.priority - this.priority;
        }
        if (System.identityHashCode(this) < System.identityHashCode(other)) {
            return -1;
        }
        return 1;
    }

    public static abstract class Param {
        public final Object[] callbacks;
        private Bundle extra;

        @Deprecated
        protected Param() {
            this.callbacks = null;
        }

        protected Param(XposedBridge.CopyOnWriteSortedSet<? extends XCallback> callbacks) {
            this.callbacks = callbacks.getSnapshot();
        }

        public synchronized Bundle getExtra() {
            if (this.extra == null) {
                this.extra = new Bundle();
            }
            return this.extra;
        }

        public Object getObjectExtra(String key) {
            Serializable o = this.getExtra().getSerializable(key);
            if (o instanceof SerializeWrapper) {
                return ((SerializeWrapper)o).object;
            }
            return null;
        }

        public void setObjectExtra(String key, Object o) {
            this.getExtra().putSerializable(key, (Serializable)new SerializeWrapper(o));
        }

        private static class SerializeWrapper
        implements Serializable {
            private static final long serialVersionUID = 1L;
            private final Object object;

            public SerializeWrapper(Object o) {
                this.object = o;
            }
        }
    }
}

