/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.telephony.CellSignalStrengthGsm
 */
package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;

@TargetApi(value=17)
public class CellSignalStrengthGsm {
    public static Class<?> TYPE = RefClass.load(CellSignalStrengthGsm.class, android.telephony.CellSignalStrengthGsm.class);
    public static RefConstructor<android.telephony.CellSignalStrengthGsm> ctor;
    public static RefInt mSignalStrength;
    public static RefInt mBitErrorRate;
}

