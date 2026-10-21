/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.fragment.app.Fragment
 *  androidx.fragment.app.FragmentActivity
 *  org.jdeferred.android.AndroidDeferredManager
 */
package com.carlos.common.ui.activity.base;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.carlos.common.ui.activity.abs.BasePresenter;
import com.carlos.common.utils.ResponseProgram;
import org.jdeferred.android.AndroidDeferredManager;

public class VFragment<T extends BasePresenter>
extends Fragment {
    protected T mPresenter;
    private boolean mAttach;

    public T getPresenter() {
        return this.mPresenter;
    }

    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }

    protected AndroidDeferredManager defer() {
        return ResponseProgram.defer();
    }

    public void finishActivity() {
        FragmentActivity activity = this.getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public boolean isAttach() {
        return this.mAttach;
    }

    public void onAttach(Context context) {
        this.mAttach = true;
        super.onAttach(context);
    }

    public void onDetach() {
        this.mAttach = false;
        super.onDetach();
    }

    public void destroy() {
        this.finishActivity();
    }
}

