/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Messenger
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.net.wifi;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IWifiScanner
extends IInterface {
    public Messenger getMessenger() throws RemoteException;

    public Bundle getAvailableChannels(int var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IWifiScanner {
        private static final String DESCRIPTOR = "android.net.wifi.IWifiScanner";
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_getAvailableChannels = 2;

        public Stub() {
            this.attachInterface(this, "android.net.wifi.IWifiScanner");
        }

        public static IWifiScanner asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IWifiScanner) {
                return (IWifiScanner)iin;
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
                    Messenger _result = this.getMessenger();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    Bundle _result = this.getAvailableChannels(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IWifiScanner impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWifiScanner getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IWifiScanner {
            private IBinder mRemote;
            public static IWifiScanner sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.net.wifi.IWifiScanner";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public Messenger getMessenger() throws RemoteException {
                Messenger _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.wifi.IWifiScanner");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Messenger messenger = Stub.getDefaultImpl().getMessenger();
                        return messenger;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (Messenger)Messenger.CREATOR.createFromParcel(_reply) : null;
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public Bundle getAvailableChannels(int band) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.net.wifi.IWifiScanner");
                    _data.writeInt(band);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle bundle = Stub.getDefaultImpl().getAvailableChannels(band);
                        return bundle;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (Bundle)Bundle.CREATOR.createFromParcel(_reply) : null;
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }
    }

    public static class Default
    implements IWifiScanner {
        @Override
        public Messenger getMessenger() throws RemoteException {
            return null;
        }

        @Override
        public Bundle getAvailableChannels(int band) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

