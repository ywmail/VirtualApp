/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.AccountManagerCallback
 *  android.accounts.AccountManagerFuture
 *  android.accounts.AuthenticatorException
 *  android.accounts.OperationCanceledException
 *  android.app.Activity
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.RemoteException
 */
package com.lody.virtual.client.stub;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.IAccountManagerResponse;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.VLog;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AmsTask
extends FutureTask<Bundle>
implements AccountManagerFuture<Bundle> {
    protected final IAccountManagerResponse mResponse;
    final Handler mHandler;
    final AccountManagerCallback<Bundle> mCallback;
    final Activity mActivity;

    public AmsTask(Activity activity, Handler handler, AccountManagerCallback<Bundle> callback) {
        super(new Callable<Bundle>(){

            @Override
            public Bundle call() throws Exception {
                throw new IllegalStateException("this should never be called");
            }
        });
        this.mHandler = handler;
        this.mCallback = callback;
        this.mActivity = activity;
        this.mResponse = new Response();
    }

    public final AccountManagerFuture<Bundle> start() {
        try {
            this.doWork();
        }
        catch (RemoteException e) {
            this.setException(e);
        }
        return this;
    }

    @Override
    protected void set(Bundle bundle) {
        if (bundle == null) {
            VLog.e("AccountManager", "the bundle must not be null\n%s", new Exception());
        }
        super.set(bundle);
    }

    public abstract void doWork() throws RemoteException;

    private Bundle internalGetResult(Long timeout, TimeUnit unit) throws OperationCanceledException, IOException, AuthenticatorException {
        try {
            if (timeout == null) {
                Bundle bundle = (Bundle)this.get();
                return bundle;
            }
            Bundle bundle = (Bundle)this.get(timeout, unit);
            return bundle;
        }
        catch (CancellationException e) {
            throw new OperationCanceledException();
        }
        catch (TimeoutException e) {
        }
        catch (InterruptedException e) {
        }
        catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException)cause;
            }
            if (cause instanceof UnsupportedOperationException) {
                throw new AuthenticatorException(cause);
            }
            if (cause instanceof AuthenticatorException) {
                throw (AuthenticatorException)cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw new IllegalStateException(cause);
        }
        finally {
            this.cancel(true);
        }
        throw new OperationCanceledException();
    }

    public Bundle getResult() throws OperationCanceledException, IOException, AuthenticatorException {
        return this.internalGetResult(null, null);
    }

    public Bundle getResult(long timeout, TimeUnit unit) throws OperationCanceledException, IOException, AuthenticatorException {
        return this.internalGetResult(timeout, unit);
    }

    @Override
    protected void done() {
        if (this.mCallback != null) {
            this.postToHandler(this.mHandler, this.mCallback, this);
        }
    }

    private Exception convertErrorToException(int code, String message) {
        if (code == 3) {
            return new IOException(message);
        }
        if (code == 6) {
            return new UnsupportedOperationException(message);
        }
        if (code == 5) {
            return new AuthenticatorException(message);
        }
        if (code == 7) {
            return new IllegalArgumentException(message);
        }
        return new AuthenticatorException(message);
    }

    private void postToHandler(Handler handler, final AccountManagerCallback<Bundle> callback, final AccountManagerFuture<Bundle> future) {
        handler = handler == null ? VirtualRuntime.getUIHandler() : handler;
        handler.post(new Runnable(){

            @Override
            public void run() {
                callback.run(future);
            }
        });
    }

    private class Response
    extends IAccountManagerResponse.Stub {
        private Response() {
        }

        @Override
        public void onResult(Bundle bundle) {
            Intent intent = (Intent)bundle.getParcelable("intent");
            if (intent != null && AmsTask.this.mActivity != null) {
                AmsTask.this.mActivity.startActivity(intent);
            } else if (bundle.getBoolean("retry")) {
                try {
                    AmsTask.this.doWork();
                }
                catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                AmsTask.this.set(bundle);
            }
        }

        @Override
        public void onError(int code, String message) {
            if (code == 4 || code == 100 || code == 101) {
                AmsTask.this.cancel(true);
                return;
            }
            AmsTask.this.setException(AmsTask.this.convertErrorToException(code, message));
        }
    }
}

