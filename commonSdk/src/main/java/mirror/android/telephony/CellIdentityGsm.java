/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.telephony.CellIdentityGsm
 */
package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;

@TargetApi(value=17)
public class CellIdentityGsm {
    public static Class<?> TYPE = RefClass.load(CellIdentityGsm.class, android.telephony.CellIdentityGsm.class);
    public static RefConstructor<android.telephony.CellIdentityGsm> ctor;
    public static RefInt mMcc;
    public static RefInt mMnc;
    public static RefInt mLac;
    public static RefInt mCid;
}

