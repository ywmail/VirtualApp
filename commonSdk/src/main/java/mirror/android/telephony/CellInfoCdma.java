/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.telephony.CellIdentityCdma
 *  android.telephony.CellInfoCdma
 *  android.telephony.CellSignalStrengthCdma
 */
package mirror.android.telephony;

import android.annotation.TargetApi;
import android.telephony.CellIdentityCdma;
import android.telephony.CellSignalStrengthCdma;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefObject;

@TargetApi(value=17)
public class CellInfoCdma {
    public static Class<?> TYPE = RefClass.load(CellInfoCdma.class, android.telephony.CellInfoCdma.class);
    public static RefConstructor<android.telephony.CellInfoCdma> ctor;
    public static RefObject<CellIdentityCdma> mCellIdentityCdma;
    public static RefObject<CellSignalStrengthCdma> mCellSignalStrengthCdma;
}

