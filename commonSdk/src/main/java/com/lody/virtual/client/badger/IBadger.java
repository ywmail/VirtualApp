/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 */
package com.lody.virtual.client.badger;

import android.content.Intent;
import com.lody.virtual.remote.BadgerInfo;

public interface IBadger {
    public String getAction();

    public BadgerInfo handleBadger(Intent var1);
}

