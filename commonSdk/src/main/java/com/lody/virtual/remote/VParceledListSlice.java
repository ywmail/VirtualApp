/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.RemoteException
 *  android.util.Log
 */
package com.lody.virtual.remote;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public class VParceledListSlice<T extends Parcelable>
implements Parcelable {
    public static final Parcelable.ClassLoaderCreator<VParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator<VParceledListSlice>(){

        public VParceledListSlice createFromParcel(Parcel in) {
            return new VParceledListSlice(in, null);
        }

        public VParceledListSlice createFromParcel(Parcel in, ClassLoader loader) {
            return new VParceledListSlice(in, loader);
        }

        public VParceledListSlice[] newArray(int size) {
            return new VParceledListSlice[size];
        }
    };
    private static final int MAX_IPC_SIZE = 262144;
    private static final int MAX_FIRST_IPC_SIZE = 131072;
    private static String TAG = "ParceledListSlice";
    private static boolean DEBUG = false;
    private final List<T> mList;

    public VParceledListSlice(List<T> list) {
        this.mList = list;
    }

    private VParceledListSlice(Parcel p, ClassLoader loader) {
        int i;
        if (loader == null) {
            loader = VParceledListSlice.class.getClassLoader();
        }
        int N = p.readInt();
        this.mList = new ArrayList<T>(N);
        if (DEBUG) {
            Log.d((String)TAG, (String)("Retrieving " + N + " items"));
        }
        if (N <= 0) {
            return;
        }
        Class<?> listElementClass = null;
        for (i = 0; i < N && p.readInt() != 0; ++i) {
            Parcelable parcelable = p.readParcelable(loader);
            if (listElementClass == null) {
                listElementClass = parcelable.getClass();
            } else if (parcelable != null) {
                VParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
            }
            this.mList.add((T) parcelable);
            if (!DEBUG) continue;
            Log.d((String)TAG, (String)("Read inline #" + i + ": " + this.mList.get(this.mList.size() - 1)));
        }
        if (i >= N) {
            return;
        }
        IBinder retriever = p.readStrongBinder();
        while (i < N) {
            if (DEBUG) {
                Log.d((String)TAG, (String)("Reading more @" + i + " of " + N + ": retriever=" + retriever));
            }
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInt(i);
            try {
                retriever.transact(1, data, reply, 0);
            }
            catch (RemoteException e) {
                Log.w((String)TAG, (String)("Failure retrieving array; only received " + i + " of " + N), (Throwable)e);
                return;
            }
            while (i < N && reply.readInt() != 0) {
                Parcelable parcelable = reply.readParcelable(loader);
                if (parcelable != null) {
                    VParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                }
                this.mList.add((T) parcelable);
                if (DEBUG) {
                    Log.d((String)TAG, (String)("Read extra #" + i + ": " + this.mList.get(this.mList.size() - 1)));
                }
                ++i;
            }
            reply.recycle();
            data.recycle();
        }
    }

    private static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            throw new IllegalArgumentException("Can't unparcel type " + actual.getName() + " in list of type " + expected.getName());
        }
    }

    public List<T> getList() {
        return this.mList;
    }

    public int describeContents() {
        int contents = 0;
        for (int i = 0; i < this.mList.size(); ++i) {
            contents |= ((Parcelable)this.mList.get(i)).describeContents();
        }
        return contents;
    }

    public void writeToParcel(Parcel dest, int flags) {
        final int N = this.mList.size();
        final int callFlags = flags;
        dest.writeInt(N);
        if (DEBUG) {
            Log.d((String)TAG, (String)("Writing " + N + " items"));
        }
        if (N > 0) {
            int i;
            final Class<?> listElementClass = ((Parcelable)this.mList.get(0)).getClass();
            for (i = 0; i < N && dest.dataSize() < 131072; ++i) {
                dest.writeInt(1);
                Parcelable parcelable = (Parcelable)this.mList.get(i);
                if (parcelable == null) {
                    dest.writeString(null);
                } else {
                    VParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                    dest.writeParcelable(parcelable, callFlags);
                }
                if (!DEBUG) continue;
                Log.d((String)TAG, (String)("Wrote inline #" + i + ": " + this.mList.get(i)));
            }
            if (i < N) {
                dest.writeInt(0);
                Binder retriever = new Binder(){

                    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                        if (code != 1) {
                            return super.onTransact(code, data, reply, flags);
                        }
                        int i = data.readInt();
                        if (DEBUG) {
                            Log.d((String)TAG, (String)("Writing more @" + i + " of " + N));
                        }
                        while (i < N && reply.dataSize() < 262144) {
                            reply.writeInt(1);
                            Parcelable parcelable = (Parcelable)VParceledListSlice.this.mList.get(i);
                            VParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                            reply.writeParcelable(parcelable, callFlags);
                            if (DEBUG) {
                                Log.d((String)TAG, (String)("Wrote extra #" + i + ": " + VParceledListSlice.this.mList.get(i)));
                            }
                            ++i;
                        }
                        if (i < N) {
                            if (DEBUG) {
                                Log.d((String)TAG, (String)("Breaking @" + i + " of " + N));
                            }
                            reply.writeInt(0);
                        }
                        return true;
                    }
                };
                if (DEBUG) {
                    Log.d((String)TAG, (String)("Breaking @" + i + " of " + N + ": retriever=" + retriever));
                }
                dest.writeStrongBinder((IBinder)retriever);
            }
        }
    }
}

