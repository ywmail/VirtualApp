/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.telephony.CellIdentityCdma
 */
package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;

@TargetApi(value=17)
public class CellIdentityCdma {
    public static Class<?> TYPE = RefClass.load(CellIdentityCdma.class, android.telephony.CellIdentityCdma.class);
    public static RefConstructor<android.telephony.CellIdentityCdma> ctor;
    public static RefInt mNetworkId;
    public static RefInt mSystemId;
    public static RefInt mBasestationId;
}

