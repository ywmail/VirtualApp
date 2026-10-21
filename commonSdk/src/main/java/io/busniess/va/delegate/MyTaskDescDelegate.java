/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.ActivityManager$TaskDescription
 */
package io.busniess.va.delegate;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.os.VUserManager;

@TargetApi(value=21)
public class MyTaskDescDelegate
implements TaskDescriptionDelegate {
    @Override
    public ActivityManager.TaskDescription getTaskDescription(ActivityManager.TaskDescription oldTaskDescription) {
        String oldLabel;
        if (oldTaskDescription == null) {
            return null;
        }
        int userId = VUserManager.get().getUserHandle();
        String suffix = " (" + (userId + 1) + ")";
        String string2 = oldLabel = oldTaskDescription.getLabel() != null ? oldTaskDescription.getLabel() : "";
        if (!oldLabel.endsWith(suffix)) {
            return new ActivityManager.TaskDescription(oldTaskDescription.getLabel() + suffix, oldTaskDescription.getIcon(), oldTaskDescription.getPrimaryColor());
        }
        return oldTaskDescription;
    }
}

