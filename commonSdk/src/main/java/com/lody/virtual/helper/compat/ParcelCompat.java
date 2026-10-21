/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.util.SparseArray
 */
package com.lody.virtual.helper.compat;

import android.os.Parcel;
import android.util.SparseArray;
import com.lody.virtual.StringFog;

public class ParcelCompat {
    private static final int VAL_BOOLEAN = 9;
    private static final int VAL_BOOLEANARRAY = 23;
    private static final int VAL_BUNDLE = 3;
    private static final int VAL_BYTE = 20;
    private static final int VAL_BYTEARRAY = 13;
    private static final int VAL_CHARSEQUENCE = 10;
    private static final int VAL_CHARSEQUENCEARRAY = 24;
    private static final int VAL_DOUBLE = 8;
    private static final int VAL_DOUBLEARRAY = 28;
    private static final int VAL_FLOAT = 7;
    private static final int VAL_IBINDER = 15;
    private static final int VAL_INTARRAY = 18;
    private static final int VAL_INTEGER = 1;
    private static final int VAL_LIST = 11;
    private static final int VAL_LONG = 6;
    private static final int VAL_LONGARRAY = 19;
    private static final int VAL_MAP = 2;
    private static final int VAL_NULL = -1;
    private static final int VAL_OBJECTARRAY = 17;
    private static final int VAL_PARCELABLE = 4;
    private static final int VAL_PARCELABLEARRAY = 16;
    private static final int VAL_PERSISTABLEBUNDLE = 25;
    private static final int VAL_SERIALIZABLE = 21;
    private static final int VAL_SHORT = 5;
    private static final int VAL_SIZE = 26;
    private static final int VAL_SIZEF = 27;
    private static final int VAL_SPARSEARRAY = 12;
    private static final int VAL_SPARSEBOOLEANARRAY = 22;
    private static final int VAL_STRING = 0;
    private static final int VAL_STRINGARRAY = 14;
    private final Parcel parcel;

    public ParcelCompat(Parcel parcel) {
        this.parcel = parcel;
    }

    public final <T> SparseArray<T> readSparseArray(ClassLoader loader) {
        int N = this.parcel.readInt();
        if (N < 0) {
            return null;
        }
        SparseArray sa = new SparseArray(N);
        this.readSparseArrayInternal(sa, N, loader);
        return sa;
    }

    private void readSparseArrayInternal(SparseArray outVal, int N, ClassLoader loader) {
        while (N > 0) {
            int key = this.parcel.readInt();
            Object value = this.readValue(loader);
            outVal.append(key, value);
            --N;
        }
    }

    public final Object readValue(ClassLoader loader) {
        int type = this.parcel.readInt();
        switch (type) {
            case -1: {
                return null;
            }
            case 0: {
                return this.parcel.readString();
            }
            case 1: {
                return this.parcel.readInt();
            }
            case 2: {
                return this.parcel.readHashMap(loader);
            }
            case 3: {
                return this.parcel.readBundle(loader);
            }
            case 4: {
                return this.parcel.readParcelable(loader);
            }
            case 5: {
                return (short)this.parcel.readInt();
            }
            case 6: {
                return this.parcel.readLong();
            }
            case 7: {
                return Float.valueOf(this.parcel.readFloat());
            }
            case 8: {
                return this.parcel.readDouble();
            }
            case 9: {
                return this.parcel.readInt() == 1;
            }
            default: {
                int off = this.parcel.dataPosition() - 4;
                throw new RuntimeException("Parcel " + this + ": Unmarshalling unknown type code " + type + " at offset " + off);
            }
            case 11: {
                return this.parcel.readArrayList(loader);
            }
            case 12: {
                return this.readSparseArray(loader);
            }
            case 13: {
                return this.parcel.createByteArray();
            }
            case 15: {
                return this.parcel.readStrongBinder();
            }
            case 16: {
                return this.parcel.readParcelableArray(loader);
            }
            case 17: {
                return this.parcel.readArray(loader);
            }
            case 18: {
                return this.parcel.createIntArray();
            }
            case 19: {
                return this.parcel.createLongArray();
            }
            case 20: {
                return this.parcel.readByte();
            }
            case 21: {
                return this.parcel.readSerializable();
            }
            case 22: {
                return this.parcel.readSparseBooleanArray();
            }
            case 23: {
                return this.parcel.createBooleanArray();
            }
            case 25: {
                return this.parcel.readPersistableBundle(loader);
            }
            case 26: {
                return this.parcel.readSize();
            }
            case 27: {
                return this.parcel.readSizeF();
            }
            case 28: 
        }
        return this.parcel.createDoubleArray();
    }
}

