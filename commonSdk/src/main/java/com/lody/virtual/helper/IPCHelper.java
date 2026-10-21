/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IInterface
 *  android.os.RemoteException
 */
package com.lody.virtual.helper;

import android.os.IInterface;
import android.os.RemoteException;

public abstract class IPCHelper<S extends IInterface> {
    private S mInterface;

    public abstract S getInterface();

    public <R> R call(Callable<S, R> callable) {
        for (int i = 0; i <= 2; ++i) {
            if (this.mInterface == null || this.mInterface.asBinder().isBinderAlive()) {
                this.mInterface = this.getInterface();
            }
            try {
                return callable.call(this.mInterface);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                continue;
            }
        }
        return null;
    }

    public boolean callBoolean(Callable<S, Boolean> callable) {
        Boolean res = this.call(callable);
        if (res == null) {
            return false;
        }
        return res;
    }

    public void callVoid(final CallableVoid<S> callable) {
        Callable wrapper = new Callable<S, Void>(){

            @Override
            public Void call(S service) throws RemoteException {
                callable.call(service);
                return null;
            }
        };
        this.call(wrapper);
    }

    public static interface CallableVoid<S> {
        public void call(S var1) throws RemoteException;
    }

    public static interface Callable<S, R> {
        public R call(S var1) throws RemoteException;
    }
}

