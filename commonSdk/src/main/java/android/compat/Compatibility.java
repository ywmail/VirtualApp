/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.compat;

import android.annotation.SystemApi;
import android.util.Log;
import com.lody.virtual.StringFog;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
public final class Compatibility {
    public static final BehaviorChangeDelegate DEFAULT_CALLBACKS;
    public static volatile BehaviorChangeDelegate sCallbacks;

    private Compatibility() {
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static void reportUnconditionalChange(long changeId) {
        sCallbacks.onChangeReported(changeId);
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isChangeEnabled(long changeId) {
        Log.d((String)"kook", (String)"  Compatibility  isChangeEnabled ");
        return sCallbacks.isChangeEnabled(changeId);
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static void setBehaviorChangeDelegate(BehaviorChangeDelegate callbacks) {
        Log.d((String)"kook", (String)"  Compatibility  setBehaviorChangeDelegate ");
        sCallbacks = Objects.requireNonNull(callbacks);
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static void clearBehaviorChangeDelegate() {
        sCallbacks = DEFAULT_CALLBACKS;
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static void setOverrides(ChangeConfig overrides) {
        if (sCallbacks instanceof OverrideCallbacks) {
            throw new IllegalStateException("setOverrides has already been called!");
        }
        sCallbacks = new OverrideCallbacks(sCallbacks, overrides);
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static void clearOverrides() {
        if (!(sCallbacks instanceof OverrideCallbacks)) {
            throw new IllegalStateException("No overrides set");
        }
        sCallbacks = ((OverrideCallbacks)sCallbacks).delegate;
    }

    static {
        sCallbacks = DEFAULT_CALLBACKS = new BehaviorChangeDelegate(){};
    }

    private static class OverrideCallbacks
    implements BehaviorChangeDelegate {
        private final BehaviorChangeDelegate delegate;
        private final ChangeConfig changeConfig;

        private OverrideCallbacks(BehaviorChangeDelegate delegate, ChangeConfig changeConfig) {
            this.delegate = Objects.requireNonNull(delegate);
            this.changeConfig = Objects.requireNonNull(changeConfig);
        }

        @Override
        public boolean isChangeEnabled(long changeId) {
            if (this.changeConfig.isForceEnabled(changeId)) {
                return true;
            }
            if (this.changeConfig.isForceDisabled(changeId)) {
                return false;
            }
            return this.delegate.isChangeEnabled(changeId);
        }
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static final class ChangeConfig {
        private final Set<Long> enabled;
        private final Set<Long> disabled;

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public ChangeConfig(Set<Long> enabled, Set<Long> disabled) {
            this.enabled = Objects.requireNonNull(enabled);
            this.disabled = Objects.requireNonNull(disabled);
            if (enabled.contains(null)) {
                throw new NullPointerException();
            }
            if (disabled.contains(null)) {
                throw new NullPointerException();
            }
            HashSet<Long> intersection = new HashSet<Long>(enabled);
            intersection.retainAll(disabled);
            if (!intersection.isEmpty()) {
                throw new IllegalArgumentException("Cannot have changes " + intersection + " enabled and disabled!");
            }
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public boolean isEmpty() {
            return this.enabled.isEmpty() && this.disabled.isEmpty();
        }

        private static long[] toLongArray(Set<Long> values) {
            long[] result = new long[values.size()];
            int idx = 0;
            for (Long value : values) {
                result[idx++] = value;
            }
            return result;
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public long[] getEnabledChangesArray() {
            return ChangeConfig.toLongArray(this.enabled);
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public long[] getDisabledChangesArray() {
            return ChangeConfig.toLongArray(this.disabled);
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public Set<Long> getEnabledSet() {
            return Collections.unmodifiableSet(this.enabled);
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public Set<Long> getDisabledSet() {
            return Collections.unmodifiableSet(this.disabled);
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public boolean isForceEnabled(long changeId) {
            return this.enabled.contains(changeId);
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        public boolean isForceDisabled(long changeId) {
            return this.disabled.contains(changeId);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ChangeConfig)) {
                return false;
            }
            ChangeConfig that = (ChangeConfig)o;
            return this.enabled.equals(that.enabled) && this.disabled.equals(that.disabled);
        }

        public int hashCode() {
            return Objects.hash(this.enabled, this.disabled);
        }

        public String toString() {
            return "ChangeConfig{enabled=" + this.enabled + ", disabled=" + this.disabled + '}';
        }
    }

    @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
    public static interface BehaviorChangeDelegate {
        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        default public void onChangeReported(long changeId) {
            Log.d((String)"kook", (String)" onChangeReported ");
        }

        @SystemApi(client=SystemApi.Client.MODULE_LIBRARIES)
        default public boolean isChangeEnabled(long changeId) {
            Log.d((String)"kook", (String)" kook 移植系统java");
            return true;
        }
    }
}

