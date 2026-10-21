/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.net.Uri
 */
package com.lody.virtual.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.app.IAppInstallerSession;
import com.lody.virtual.server.pm.VAppManagerService;
import java.util.ArrayList;
import java.util.List;

public class VAppInstallerSession
extends IAppInstallerSession.Stub {
    private Context mContext;
    private VAppManagerService mApp;
    private boolean mCommited = false;
    private boolean mCacneled = false;
    private final List<Uri> mUris = new ArrayList<Uri>();
    private final List<Uri> mSplitUris = new ArrayList<Uri>();
    private IntentSender mStatusReceiver = null;

    public VAppInstallerSession(Context context, VAppManagerService app) {
        this.mContext = context;
        this.mApp = app;
    }

    @Override
    public void addPackage(Uri uri) {
        this.mUris.add(uri);
    }

    @Override
    public void addSplit(Uri uri) {
        this.mSplitUris.add(uri);
    }

    @Override
    public void commit(IntentSender statusReceiver) {
        if (this.mCacneled) {
            throw new IllegalStateException("A canceled session cannot be committed.");
        }
        this.mCommited = true;
        this.mStatusReceiver = statusReceiver;
        try {
            this.mStatusReceiver.sendIntent(this.mContext, 0, new Intent(), null, null);
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        if (this.mCommited) {
            throw new IllegalStateException("Session that have already been committed cannot be cancelled.");
        }
        this.mCacneled = true;
    }
}

