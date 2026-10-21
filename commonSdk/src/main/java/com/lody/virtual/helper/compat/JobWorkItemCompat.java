/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.job.JobWorkItem
 *  android.content.Intent
 */
package com.lody.virtual.helper.compat;

import android.annotation.TargetApi;
import android.content.Intent;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ComponentUtils;
import mirror.android.app.job.JobWorkItem;

@TargetApi(value=26)
public class JobWorkItemCompat {
    public static android.app.job.JobWorkItem redirect(int userId, android.app.job.JobWorkItem item, String pkg) {
        if (item != null) {
            Intent target = JobWorkItem.getIntent.call(item, new Object[0]);
            if (target.hasExtra("_VA_|_intent_")) {
                return item;
            }
            Intent intent = ComponentUtils.getProxyIntentSenderIntent(userId, 4, pkg, target);
            android.app.job.JobWorkItem workItem = (android.app.job.JobWorkItem)JobWorkItem.ctor.newInstance(intent);
            int wordId = JobWorkItem.mWorkId.get(item);
            JobWorkItem.mWorkId.set(workItem, wordId);
            Object obj = JobWorkItem.mGrants.get(item);
            JobWorkItem.mGrants.set(workItem, obj);
            int deliveryCount = JobWorkItem.mDeliveryCount.get(item);
            JobWorkItem.mDeliveryCount.set(workItem, deliveryCount);
            return workItem;
        }
        return null;
    }

    public static android.app.job.JobWorkItem parse(android.app.job.JobWorkItem item) {
        if (item != null) {
            Intent target = JobWorkItem.getIntent.call(item, new Object[0]);
            if (target.hasExtra("_VA_|_intent_")) {
                return item;
            }
            Intent intent = ComponentUtils.parseIntentSenderInfo((Intent)item.getIntent(), (boolean)false).intent;
            ComponentUtils.unpackFillIn(intent, JobWorkItemCompat.class.getClassLoader());
            android.app.job.JobWorkItem workItem = (android.app.job.JobWorkItem)JobWorkItem.ctor.newInstance(intent);
            int wordId = JobWorkItem.mWorkId.get(item);
            JobWorkItem.mWorkId.set(workItem, wordId);
            Object obj = JobWorkItem.mGrants.get(item);
            JobWorkItem.mGrants.set(workItem, obj);
            int deliveryCount = JobWorkItem.mDeliveryCount.get(item);
            JobWorkItem.mDeliveryCount.set(workItem, deliveryCount);
            return workItem;
        }
        return null;
    }
}

