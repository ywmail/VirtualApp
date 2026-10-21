/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Intent
 */
package com.lody.virtual.client.badger;

import android.content.ComponentName;
import android.content.Intent;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.badger.IBadger;
import com.lody.virtual.remote.BadgerInfo;

public abstract class BroadcastBadger2
implements IBadger {
    @Override
    public abstract String getAction();

    public abstract String getComponentKey();

    public abstract String getCountKey();

    @Override
    public BadgerInfo handleBadger(Intent intent) {
        BadgerInfo info = new BadgerInfo();
        String componentName = intent.getStringExtra(this.getComponentKey());
        ComponentName component = ComponentName.unflattenFromString((String)componentName);
        if (component != null) {
            info.packageName = component.getPackageName();
            info.className = component.getClassName();
            info.badgerCount = intent.getIntExtra(this.getCountKey(), 0);
            return info;
        }
        return null;
    }

    static class NewHtcHomeBadger1
    extends BroadcastBadger2 {
        NewHtcHomeBadger1() {
        }

        @Override
        public String getAction() {
            return "com.htc.launcher.action.SET_NOTIFICATION";
        }

        @Override
        public String getComponentKey() {
            return "com.htc.launcher.extra.COMPONENT";
        }

        @Override
        public String getCountKey() {
            return "com.htc.launcher.extra.COUNT";
        }
    }
}

