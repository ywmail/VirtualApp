/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.job.JobWorkItem
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.RemoteException
 */
package android.app.job;

import android.app.job.JobWorkItem;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IJobCallback
extends IInterface {
    public void acknowledgeStartMessage(int var1, boolean var2) throws RemoteException;

    public void acknowledgeStopMessage(int var1, boolean var2) throws RemoteException;

    public JobWorkItem dequeueWork(int var1) throws RemoteException;

    public boolean completeWork(int var1, int var2) throws RemoteException;

    public void jobFinished(int var1, boolean var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IJobCallback {
        private static final String DESCRIPTOR = "android.app.job.IJobCallback";
        static final int TRANSACTION_acknowledgeStartMessage = 1;
        static final int TRANSACTION_acknowledgeStopMessage = 2;
        static final int TRANSACTION_dequeueWork = 3;
        static final int TRANSACTION_completeWork = 4;
        static final int TRANSACTION_jobFinished = 5;

        public Stub() {
            this.attachInterface(this, "android.app.job.IJobCallback");
        }

        public static IJobCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && iin instanceof IJobCallback) {
                return (IJobCallback)iin;
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
                    boolean _arg1 = 0 != data.readInt();
                    this.acknowledgeStartMessage(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _arg1 = 0 != data.readInt();
                    this.acknowledgeStopMessage(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    JobWorkItem _result = this.dequeueWork(_arg0);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    int _arg1 = data.readInt();
                    boolean _result = this.completeWork(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    int _arg0 = data.readInt();
                    boolean _arg1 = 0 != data.readInt();
                    this.jobFinished(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        public static boolean setDefaultImpl(IJobCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IJobCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy
        implements IJobCallback {
            private IBinder mRemote;
            public static IJobCallback sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.app.job.IJobCallback";
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.job.IJobCallback");
                    _data.writeInt(jobId);
                    _data.writeInt(ongoing ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().acknowledgeStartMessage(jobId, ongoing);
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
            public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.job.IJobCallback");
                    _data.writeInt(jobId);
                    _data.writeInt(reschedule ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().acknowledgeStopMessage(jobId, reschedule);
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
            public JobWorkItem dequeueWork(int jobId) throws RemoteException {
                JobWorkItem _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.job.IJobCallback");
                    _data.writeInt(jobId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        JobWorkItem jobWorkItem = Stub.getDefaultImpl().dequeueWork(jobId);
                        return jobWorkItem;
                    }
                    _reply.readException();
                    _result = 0 != _reply.readInt() ? (JobWorkItem)JobWorkItem.CREATOR.createFromParcel(_reply) : null;
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
            public boolean completeWork(int jobId, int workId) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.job.IJobCallback");
                    _data.writeInt(jobId);
                    _data.writeInt(workId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bl = Stub.getDefaultImpl().completeWork(jobId, workId);
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
            public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.app.job.IJobCallback");
                    _data.writeInt(jobId);
                    _data.writeInt(reschedule ? 1 : 0);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().jobFinished(jobId, reschedule);
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
    implements IJobCallback {
        @Override
        public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
        }

        @Override
        public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
        }

        @Override
        public JobWorkItem dequeueWork(int jobId) throws RemoteException {
            return null;
        }

        @Override
        public boolean completeWork(int jobId, int workId) throws RemoteException {
            return false;
        }

        @Override
        public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}

