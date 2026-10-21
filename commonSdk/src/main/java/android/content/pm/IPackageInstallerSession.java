/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.IntentSender
 *  android.content.pm.Checksum
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package android.content.pm;

import android.content.IntentSender;
import android.content.pm.Checksum;
import android.content.pm.DataLoaderParamsParcel;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallerSession
extends IInterface {
    public void setClientProgress(float var1) throws RemoteException;

    public void addClientProgress(float var1) throws RemoteException;

    public String[] getNames() throws RemoteException;

    public ParcelFileDescriptor openWrite(String var1, long var2, long var4) throws RemoteException;

    public ParcelFileDescriptor openRead(String var1) throws RemoteException;

    public void removeSplit(String var1) throws RemoteException;

    public void close() throws RemoteException;

    public void commit(IntentSender var1) throws RemoteException;

    public void abandon() throws RemoteException;

    public boolean isMultiPackage() throws RemoteException;

    public DataLoaderParamsParcel getDataLoaderParams() throws RemoteException;

    public void setChecksums(String var1, Checksum[] var2, byte[] var3) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IPackageInstallerSession {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerSession";
        static final int TRANSACTION_setClientProgress = 1;
        static final int TRANSACTION_addClientProgress = 2;
        static final int TRANSACTION_getNames = 3;
        static final int TRANSACTION_openWrite = 4;
        static final int TRANSACTION_openRead = 5;
        static final int TRANSACTION_removeSplit = 6;
        static final int TRANSACTION_close = 7;
        static final int TRANSACTION_commit = 8;
        static final int TRANSACTION_abandon = 9;
        static final int TRANSACTION_isMultiPackage = 10;
        static final int TRANSACTION_getDataLoaderParams = 11;
        static final int TRANSACTION_setChecksums = 12;

        public Stub() {
            this.attachInterface(this, "android.content.pm.IPackageInstallerSession");
        }

        public static IPackageInstallerSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IPackageInstallerSession) {
                return (IPackageInstallerSession)iin;
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
                    float _arg0 = data.readFloat();
                    this.setClientProgress(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    float _arg0 = data.readFloat();
                    this.addClientProgress(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String[] _result = this.getNames();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    long _arg1 = data.readLong();
                    long _arg2 = data.readLong();
                    ParcelFileDescriptor _result = this.openWrite(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    ParcelFileDescriptor _result = this.openRead(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    this.removeSplit(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    this.close();
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    IntentSender _arg0 = 0 != data.readInt() ? (IntentSender)IntentSender.CREATOR.createFromParcel(data) : null;
                    this.commit(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    this.abandon();
                    reply.writeNoException();
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    boolean _result = this.isMultiPackage();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    DataLoaderParamsParcel _result = this.getDataLoaderParams();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    Checksum[] _arg1 = (Checksum[])data.createTypedArray(Checksum.CREATOR);
                    byte[] _arg2 = data.createByteArray();
                    this.setChecksums(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IPackageInstallerSession impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageInstallerSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IPackageInstallerSession {
            private IBinder mRemote;
            public static IPackageInstallerSession sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.content.pm.IPackageInstallerSession";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void setClientProgress(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeFloat(progress);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setClientProgress(progress);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void addClientProgress(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeFloat(progress);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addClientProgress(progress);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public String[] getNames() throws RemoteException {
                String[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String[] stringArray = Stub.getDefaultImpl().getNames();
                        return stringArray;
                    }
                    _reply.readException();
                    _result = _reply.createStringArray();
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
            public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeString(name);
                    _data.writeLong(offsetBytes);
                    _data.writeLong(lengthBytes);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ParcelFileDescriptor parcelFileDescriptor = Stub.getDefaultImpl().openWrite(name, offsetBytes, lengthBytes);
                        return parcelFileDescriptor;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(_reply) : null;
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
            public ParcelFileDescriptor openRead(String name) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ParcelFileDescriptor parcelFileDescriptor = Stub.getDefaultImpl().openRead(name);
                        return parcelFileDescriptor;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(_reply) : null;
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
            public void removeSplit(String splitName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeString(splitName);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeSplit(splitName);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().close();
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void commit(IntentSender statusReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().commit(statusReceiver);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void abandon() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abandon();
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean isMultiPackage() throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().isMultiPackage();
                        return bl;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt();
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
            public DataLoaderParamsParcel getDataLoaderParams() throws RemoteException {
                DataLoaderParamsParcel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        DataLoaderParamsParcel dataLoaderParamsParcel = Stub.getDefaultImpl().getDataLoaderParams();
                        return dataLoaderParamsParcel;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (DataLoaderParamsParcel)DataLoaderParamsParcel.CREATOR.createFromParcel(_reply) : null;
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
            public void setChecksums(String name, Checksum[] checksums, byte[] signature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
                    _data.writeString(name);
                    _data.writeTypedArray((Parcelable[])checksums, 0);
                    _data.writeByteArray(signature);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setChecksums(name, checksums, signature);
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
    implements IPackageInstallerSession {
        @Override
        public void setClientProgress(float progress) throws RemoteException {
        }

        @Override
        public void addClientProgress(float progress) throws RemoteException {
        }

        @Override
        public String[] getNames() throws RemoteException {
            return null;
        }

        @Override
        public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
            return null;
        }

        @Override
        public ParcelFileDescriptor openRead(String name) throws RemoteException {
            return null;
        }

        @Override
        public void removeSplit(String splitName) throws RemoteException {
        }

        @Override
        public void close() throws RemoteException {
        }

        @Override
        public void commit(IntentSender statusReceiver) throws RemoteException {
        }

        @Override
        public void abandon() throws RemoteException {
        }

        @Override
        public boolean isMultiPackage() throws RemoteException {
            return false;
        }

        @Override
        public DataLoaderParamsParcel getDataLoaderParams() throws RemoteException {
            return null;
        }

        @Override
        public void setChecksums(String name, Checksum[] checksums, byte[] signature) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

