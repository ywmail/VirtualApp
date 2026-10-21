/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 */
package com.lody.virtual.client.badger;

import android.content.Intent;
import com.lody.virtual.client.badger.BroadcastBadger1;
import com.lody.virtual.client.badger.BroadcastBadger2;
import com.lody.virtual.client.badger.IBadger;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.BadgerInfo;
import java.util.HashMap;
import java.util.Map;

public class BadgerManager {
    private static final Map<String, IBadger> BADGERS = new HashMap<String, IBadger>(10);

    private static void addBadger(IBadger badger) {
        BADGERS.put(badger.getAction(), badger);
    }

    public static boolean handleBadger(Intent intent) {
        IBadger badger = BADGERS.get(intent.getAction());
        if (badger != null) {
            BadgerInfo info = badger.handleBadger(intent);
            VActivityManager.get().notifyBadgerChange(info);
            return true;
        }
        return false;
    }

    static {
        BadgerManager.addBadger(new BroadcastBadger1.AdwHomeBadger());
        BadgerManager.addBadger(new BroadcastBadger1.AospHomeBadger());
        BadgerManager.addBadger(new BroadcastBadger1.LGHomeBadger());
        BadgerManager.addBadger(new BroadcastBadger1.NewHtcHomeBadger2());
        BadgerManager.addBadger(new BroadcastBadger1.OPPOHomeBader());
        BadgerManager.addBadger(new BroadcastBadger2.NewHtcHomeBadger1());
    }
}

