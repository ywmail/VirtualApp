/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallObserver
extends IInterface {
    public void packageInstalled(String var1, int var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IPackageInstallObserver {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstallObserver";
        static final int TRANSACTION_packageInstalled = 1;

        public Stub() {
            this.attachInterface(this, "android.content.pm.IPackageInstallObserver");
        }

        public static IPackageInstallObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IPackageInstallObserver) {
                return (IPackageInstallObserver)iin;
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
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.packageInstalled(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IPackageInstallObserver impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageInstallObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IPackageInstallObserver {
            private IBinder mRemote;
            public static IPackageInstallObserver sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.content.pm.IPackageInstallObserver";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallObserver");
                    _data.writeString(packageName);
                    _data.writeInt(returnCode);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().packageInstalled(packageName, returnCode);
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
    implements IPackageInstallObserver {
        @Override
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

