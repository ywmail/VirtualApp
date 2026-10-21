//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.pm;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import java.util.Iterator;
import java.util.Map;

public class ComponentStateManager {
    private static SparseArray<UserComponentState> helpers = new SparseArray();

    public ComponentStateManager() {
    }

    public static synchronized UserComponentState user(int userId) {
        UserComponentState state = (UserComponentState)helpers.get(userId);
        if (state == null) {
            state = new UserComponentState(userId);
            helpers.put(userId, state);
        }

        return state;
    }

    public static class UserComponentState {
        private SharedPreferences sharedPreferences;

        private UserComponentState(int userId) {
            this.sharedPreferences = VirtualCore.get().getContext().getSharedPreferences("va_components_state_u" + userId, 0);
        }

        public int get(ComponentName componentName) {
            return this.sharedPreferences.getInt(this.componentKey(componentName), 0);
        }

        public void set(ComponentName componentName, int state) {
            this.sharedPreferences.edit().putInt(this.componentKey(componentName), state).apply();
        }

        public void clear(String packageName) {
            Map<String, Integer> all = (Map<String, Integer>) this.sharedPreferences.getAll();
            if (all != null) {
                Iterator var3 = all.keySet().iterator();

                while(var3.hasNext()) {
                    String component = (String)var3.next();
                    if (component.startsWith(packageName + "@")) {
                        this.sharedPreferences.edit().remove(component).apply();
                    }
                }

            }
        }

        public void clearAll() {
            this.sharedPreferences.edit().clear().apply();
        }

        private String componentKey(ComponentName componentName) {
            return componentName.getPackageName() + "@" + componentName.getClassName();
        }
    }
}
