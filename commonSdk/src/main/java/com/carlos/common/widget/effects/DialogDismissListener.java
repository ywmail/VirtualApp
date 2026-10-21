/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.fragment.app.DialogFragment
 */
package com.carlos.common.widget.effects;

import androidx.fragment.app.DialogFragment;
import java.util.List;

public interface DialogDismissListener {
    public void dismiss(DialogFragment var1);

    public static interface BaseListView<M> {
        public void onRefreshSuccess(List<M> var1);

        public void onLoadMoreSuccess(List<M> var1);

        public void onLoadMoreFailed();

        public void showMoreMore();

        public void onComplete(boolean var1);

        public void noNetConnect();
    }
}

