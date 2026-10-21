/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.telephony.CellSignalStrengthCdma
 */
package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;

@TargetApi(value=17)
public class CellSignalStrengthCdma {
    public static Class<?> TYPE = RefClass.load(CellSignalStrengthCdma.class, android.telephony.CellSignalStrengthCdma.class);
    public static RefConstructor<android.telephony.CellSignalStrengthCdma> ctor;
    public static RefInt mCdmaDbm;
    public static RefInt mCdmaEcio;
    public static RefInt mEvdoDbm;
    public static RefInt mEvdoEcio;
    public static RefInt mEvdoSnr;
}

