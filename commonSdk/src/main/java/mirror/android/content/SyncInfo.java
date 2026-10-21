/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 */
package mirror.android.content;

import android.accounts.Account;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class SyncInfo {
    public static Class<?> TYPE = RefClass.load(SyncInfo.class, android.content.SyncInfo.class);
    @MethodParams(value={int.class, Account.class, String.class, long.class})
    public static RefConstructor<android.content.SyncInfo> ctor;
}

