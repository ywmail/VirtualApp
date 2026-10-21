/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.RemoteException
 */
package com.lody.virtual.server.fs;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.FileInfo;

public interface IFileTransfer
extends IInterface {
    public FileInfo[] listFiles(String var1) throws RemoteException;

    public ParcelFileDescriptor openFile(String var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IFileTransfer {
        private static final String DESCRIPTOR = "com.lody.virtual.server.fs.IFileTransfer";
        static final int TRANSACTION_listFiles = 1;
        static final int TRANSACTION_openFile = 2;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.fs.IFileTransfer");
        }

        public static IFileTransfer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IFileTransfer) {
                return (IFileTransfer)iin;
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
                    Parcelable[] _result = this.listFiles(_arg0);
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    ParcelFileDescriptor _result = this.openFile(_arg0);
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

        public static boolean setDefaultImpl(IFileTransfer impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFileTransfer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IFileTransfer {
            private IBinder mRemote;
            public static IFileTransfer sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.fs.IFileTransfer";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public FileInfo[] listFiles(String path) throws RemoteException {
                FileInfo[] _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.fs.IFileTransfer");
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        FileInfo[] fileInfoArray = Stub.getDefaultImpl().listFiles(path);
                        return fileInfoArray;
                    }
                    _reply.readException();
                    _result = (FileInfo[])_reply.createTypedArray(FileInfo.CREATOR);
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
            public ParcelFileDescriptor openFile(String path) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.fs.IFileTransfer");
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        ParcelFileDescriptor parcelFileDescriptor = Stub.getDefaultImpl().openFile(path);
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
        }
    }

    public static class Default
    implements IFileTransfer {
        @Override
        public FileInfo[] listFiles(String path) throws RemoteException {
            return null;
        }

        @Override
        public ParcelFileDescriptor openFile(String path) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

