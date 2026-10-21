/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.pm;

import com.lody.virtual.server.pm.IAppChangedCallback;
import java.util.ArrayList;
import java.util.List;

public class AppChangedCallbackList {
    private static final AppChangedCallbackList sInstance = new AppChangedCallbackList();
    private List<IAppChangedCallback> mList = new ArrayList<IAppChangedCallback>(2);

    public static AppChangedCallbackList get() {
        return sInstance;
    }

    public void register(IAppChangedCallback callback) {
        this.mList.add(callback);
    }

    public void unregister(IAppChangedCallback callback) {
        this.mList.remove(callback);
    }

    void notifyCallbacks(boolean removed) {
        for (IAppChangedCallback callback : this.mList) {
            callback.onCallback(removed);
        }
    }
}

