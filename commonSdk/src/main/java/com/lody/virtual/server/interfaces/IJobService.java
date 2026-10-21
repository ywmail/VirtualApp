/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.job.JobInfo
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package com.lody.virtual.server.interfaces;

import android.app.job.JobInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.VJobWorkItem;
import java.util.ArrayList;
import java.util.List;

public interface IJobService
extends IInterface {
    public int schedule(int var1, JobInfo var2) throws RemoteException;

    public void cancel(int var1, int var2) throws RemoteException;

    public void cancelAll(int var1) throws RemoteException;

    public List<JobInfo> getAllPendingJobs(int var1) throws RemoteException;

    public JobInfo getPendingJob(int var1, int var2) throws RemoteException;

    public int enqueue(int var1, JobInfo var2, VJobWorkItem var3) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IJobService {
        private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IJobService";
        static final int TRANSACTION_schedule = 1;
        static final int TRANSACTION_cancel = 2;
        static final int TRANSACTION_cancelAll = 3;
        static final int TRANSACTION_getAllPendingJobs = 4;
        static final int TRANSACTION_getPendingJob = 5;
        static final int TRANSACTION_enqueue = 6;

        public Stub() {
            this.attachInterface(this, "com.lody.virtual.server.interfaces.IJobService");
        }

        public static IJobService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IJobService) {
                return (IJobService)iin;
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
                    JobInfo _arg1 = 0 != data.readInt() ? (JobInfo)JobInfo.CREATOR.createFromParcel(data) : null;
                    int _result = this.schedule(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    this.cancel(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    this.cancelAll(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    List _result = this.getAllPendingJobs(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    JobInfo _result = this.getPendingJob(_arg0, _arg1);
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
                    int _arg0 = data.readInt();
                    JobInfo _arg1 = 0 != data.readInt() ? (JobInfo)JobInfo.CREATOR.createFromParcel(data) : null;
                    VJobWorkItem _arg2 = 0 != data.readInt() ? (VJobWorkItem)VJobWorkItem.CREATOR.createFromParcel(data) : null;
                    int _result = this.enqueue(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IJobService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IJobService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IJobService {
            private IBinder mRemote;
            public static IJobService sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.lody.virtual.server.interfaces.IJobService";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public int schedule(int uid, JobInfo job) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    if (job != null) {
                        _data.writeInt(1);
                        job.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().schedule(uid, job);
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
            public void cancel(int uid, int jobId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    _data.writeInt(jobId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancel(uid, jobId);
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
            public void cancelAll(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAll(uid);
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
            public List<JobInfo> getAllPendingJobs(int uid) throws RemoteException {
                ArrayList _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        List<JobInfo> list = Stub.getDefaultImpl().getAllPendingJobs(uid);
                        return list;
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(JobInfo.CREATOR);
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
            public JobInfo getPendingJob(int uid, int jobId) throws RemoteException {
                JobInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    _data.writeInt(jobId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        JobInfo jobInfo = Stub.getDefaultImpl().getPendingJob(uid, jobId);
                        return jobInfo;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (JobInfo)JobInfo.CREATOR.createFromParcel(_reply) : null;
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
            public int enqueue(int uid, JobInfo job, VJobWorkItem workItem) throws RemoteException {
                int _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IJobService");
                    _data.writeInt(uid);
                    if (job != null) {
                        _data.writeInt(1);
                        job.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (workItem != null) {
                        _data.writeInt(1);
                        workItem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int n = Stub.getDefaultImpl().enqueue(uid, job, workItem);
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
        }
    }

    public static class Default
    implements IJobService {
        @Override
        public int schedule(int uid, JobInfo job) throws RemoteException {
            return 0;
        }

        @Override
        public void cancel(int uid, int jobId) throws RemoteException {
        }

        @Override
        public void cancelAll(int uid) throws RemoteException {
        }

        @Override
        public List<JobInfo> getAllPendingJobs(int uid) throws RemoteException {
            return null;
        }

        @Override
        public JobInfo getPendingJob(int uid, int jobId) throws RemoteException {
            return null;
        }

        @Override
        public int enqueue(int uid, JobInfo job, VJobWorkItem workItem) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

