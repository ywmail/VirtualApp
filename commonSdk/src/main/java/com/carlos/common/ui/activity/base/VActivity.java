/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Handler
 *  android.view.MenuItem
 *  android.view.View
 *  androidx.annotation.IdRes
 *  androidx.appcompat.app.ActionBar
 *  androidx.fragment.app.Fragment
 *  org.jdeferred.android.AndroidDeferredManager
 */
package com.carlos.common.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.utils.ResponseProgram;
import org.jdeferred.android.AndroidDeferredManager;

public abstract class VActivity
extends BaseActivity {
    Handler mHandler = new Handler();

    public Activity getActivity() {
        return this;
    }

    public Context getContext() {
        return this;
    }

    protected AndroidDeferredManager defer() {
        return ResponseProgram.defer();
    }

    public Fragment findFragmentById(@IdRes int id2) {
        return this.getSupportFragmentManager().findFragmentById(id2);
    }

    public void replaceFragment(@IdRes int id2, Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction().replace(id2, fragment).commit();
    }

    protected <T extends View> T bind(int id2) {
        return (T)this.findViewById(id2);
    }

    public void enableBackHome() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void onStart() {
        super.onStart();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332: {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public Handler getHandler() {
        return this.mHandler;
    }
}

