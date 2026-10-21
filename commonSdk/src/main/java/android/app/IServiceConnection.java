/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IServiceConnection
extends IInterface {
    public void connected(ComponentName var1, IBinder var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IServiceConnection {
        private static final String DESCRIPTOR = "android.app.IServiceConnection";
        static final int TRANSACTION_connected = 1;

        public Stub() {
            this.attachInterface(this, "android.app.IServiceConnection");
        }

        public static IServiceConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IServiceConnection) {
                return (IServiceConnection)iin;
            }
            return new Proxy(obj);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case 1598968902: {
                    reply.writeString(descriptor);
                    return true;
                }
                case 1: {
                    data.enforceInterface(descriptor);
                    ComponentName _arg0 = 0 != data.readInt() ? (ComponentName)ComponentName.CREATOR.createFromParcel(data) : null;
                    IBinder _arg1 = data.readStrongBinder();
                    this.connected(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IServiceConnection impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IServiceConnection getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IServiceConnection {
            private IBinder mRemote;
            public static IServiceConnection sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.app.IServiceConnection";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void connected(ComponentName name, IBinder service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.IServiceConnection");
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(service);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().connected(name, service);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }

    public static class Default
    implements IServiceConnection {
        @Override
        public void connected(ComponentName name, IBinder service) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

