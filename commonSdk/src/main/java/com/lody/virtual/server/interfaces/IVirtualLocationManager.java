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
package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.List;

public interface IVirtualLocationManager
extends IInterface {
    public int getMode(int var1, String var2) throws RemoteException;

    public void setMode(int var1, String var2, int var3) throws RemoteException;

    public void setCell(int var1, String var2, VCell var3) throws RemoteException;

    public void setAllCell(int var1, String var2, List<VCell> var3) throws RemoteException;

    public void setNeighboringCell(int var1, String var2, List<VCell> var3) throws RemoteException;

    public void setGlobalCell(VCell var1) throws RemoteException;

    public void setGlobalAllCell(List<VCell> var1) throws RemoteException;

    public void setGlobalNeighboringCell(List<VCell> var1) throws RemoteException;

    public VCell getCell(int var1, String var2) throws RemoteException;

    public List<VCell> getAllCell(int var1, String var2) throws RemoteException;

    public List<VCell> getNeighboringCell(int var1, String var2) throws RemoteException;

    public void setLocation(int var1, String var2, VLocation var3) throws RemoteException;

    public VLocation getLocation(int var1, String var2) throws RemoteException;

    public void setGlobalLocation(VLocation var1) throws RemoteException;

    public VLocation getGlobalLocation() throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IVirtualLocationManager {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IVirtualLocationManager";
        static final int TRANSACTION_getMode = 1;
        static final int TRANSACTION_setMode = 2;
        static final int TRANSACTION_setCell = 3;
        static final int TRANSACTION_setAllCell = 4;
        static final int TRANSACTION_setNeighboringCell = 5;
        static final int TRANSACTION_setGlobalCell = 6;
        static final int TRANSACTION_setGlobalAllCell = 7;
        static final int TRANSACTION_setGlobalNeighboringCell = 8;
        static final int TRANSACTION_getCell = 9;
        static final int TRANSACTION_getAllCell = 10;
        static final int TRANSACTION_getNeighboringCell = 11;
        static final int TRANSACTION_setLocation = 12;
        static final int TRANSACTION_getLocation = 13;
        static final int TRANSACTION_setGlobalLocation = 14;
        static final int TRANSACTION_getGlobalLocation = 15;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IVirtualLocationManager");
        }

        public static IVirtualLocationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IVirtualLocationManager) {
                return (IVirtualLocationManager)iin;
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
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    int _result = this.getMode(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    this.setMode(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    VCell _arg2 = 0 != data.readInt() ? (VCell)VCell.CREATOR.createFromParcel(data) : null;
                    this.setCell(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    ArrayList _arg2 = data.createTypedArrayList(VCell.CREATOR);
                    this.setAllCell(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    ArrayList _arg2 = data.createTypedArrayList(VCell.CREATOR);
                    this.setNeighboringCell(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    VCell _arg0 = 0 != data.readInt() ? (VCell)VCell.CREATOR.createFromParcel(data) : null;
                    this.setGlobalCell(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    ArrayList _arg0 = data.createTypedArrayList(VCell.CREATOR);
                    this.setGlobalAllCell(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    ArrayList _arg0 = data.createTypedArrayList(VCell.CREATOR);
                    this.setGlobalNeighboringCell(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    VCell _result = this.getCell(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    List _result = this.getAllCell(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    List _result = this.getNeighboringCell(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    VLocation _arg2 = 0 != data.readInt() ? (VLocation)VLocation.CREATOR.createFromParcel(data) : null;
                    this.setLocation(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    VLocation _result = this.getLocation(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    VLocation _arg0 = 0 != data.readInt() ? (VLocation)VLocation.CREATOR.createFromParcel(data) : null;
                    this.setGlobalLocation(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    VLocation _result = this.getGlobalLocation();
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

        public static boolean setDefaultImpl(IVirtualLocationManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IVirtualLocationManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IVirtualLocationManager {
            private IBinder mRemote;
            public static IVirtualLocationManager sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IVirtualLocationManager";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int getMode(int userId, String pkg) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().getMode(userId, pkg);
                        return n;
                    }
                    _reply.readException();
                    _result = _reply.readInt();
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
            public void setMode(int userId, String pkg, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMode(userId, pkg, mode);
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
            public void setCell(int userId, String pkg, VCell cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    if (cell != null) {
                        _data.writeInt(1);
                        cell.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCell(userId, pkg, cell);
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
            public void setAllCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    _data.writeTypedList(cell);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAllCell(userId, pkg, cell);
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
            public void setNeighboringCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    _data.writeTypedList(cell);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNeighboringCell(userId, pkg, cell);
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
            public void setGlobalCell(VCell cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    if (cell != null) {
                        _data.writeInt(1);
                        cell.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalCell(cell);
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
            public void setGlobalAllCell(List<VCell> cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeTypedList(cell);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalAllCell(cell);
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
            public void setGlobalNeighboringCell(List<VCell> cell) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeTypedList(cell);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalNeighboringCell(cell);
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
            public VCell getCell(int userId, String pkg) throws RemoteException {
                VCell _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VCell vCell = Stub.getDefaultImpl().getCell(userId, pkg);
                        return vCell;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VCell)VCell.CREATOR.createFromParcel(_reply) : null;
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
            public List<VCell> getAllCell(int userId, String pkg) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<VCell> list = Stub.getDefaultImpl().getAllCell(userId, pkg);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(VCell.CREATOR);
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
            public List<VCell> getNeighboringCell(int userId, String pkg) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<VCell> list = Stub.getDefaultImpl().getNeighboringCell(userId, pkg);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(VCell.CREATOR);
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
            public void setLocation(int userId, String pkg, VLocation loc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    if (loc != null) {
                        _data.writeInt(1);
                        loc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLocation(userId, pkg, loc);
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
            public VLocation getLocation(int userId, String pkg) throws RemoteException {
                VLocation _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VLocation vLocation = Stub.getDefaultImpl().getLocation(userId, pkg);
                        return vLocation;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VLocation)VLocation.CREATOR.createFromParcel(_reply) : null;
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
            public void setGlobalLocation(VLocation loc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    if (loc != null) {
                        _data.writeInt(1);
                        loc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalLocation(loc);
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
            public VLocation getGlobalLocation() throws RemoteException {
                VLocation _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualLocationManager");
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        VLocation vLocation = Stub.getDefaultImpl().getGlobalLocation();
                        return vLocation;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (VLocation)VLocation.CREATOR.createFromParcel(_reply) : null;
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
    implements IVirtualLocationManager {
        @Override
        public int getMode(int userId, String pkg) throws RemoteException {
            return 0;
        }

        @Override
        public void setMode(int userId, String pkg, int mode) throws RemoteException {
        }

        @Override
        public void setCell(int userId, String pkg, VCell cell) throws RemoteException {
        }

        @Override
        public void setAllCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
        }

        @Override
        public void setNeighboringCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
        }

        @Override
        public void setGlobalCell(VCell cell) throws RemoteException {
        }

        @Override
        public void setGlobalAllCell(List<VCell> cell) throws RemoteException {
        }

        @Override
        public void setGlobalNeighboringCell(List<VCell> cell) throws RemoteException {
        }

        @Override
        public VCell getCell(int userId, String pkg) throws RemoteException {
            return null;
        }

        @Override
        public List<VCell> getAllCell(int userId, String pkg) throws RemoteException {
            return null;
        }

        @Override
        public List<VCell> getNeighboringCell(int userId, String pkg) throws RemoteException {
            return null;
        }

        @Override
        public void setLocation(int userId, String pkg, VLocation loc) throws RemoteException {
        }

        @Override
        public VLocation getLocation(int userId, String pkg) throws RemoteException {
            return null;
        }

        @Override
        public void setGlobalLocation(VLocation loc) throws RemoteException {
        }

        @Override
        public VLocation getGlobalLocation() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

