/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Message
 */
package mirror.android.os;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class Message {
    public static Class<?> TYPE = RefClass.load(Message.class, android.os.Message.class);
    @MethodParams(value={int.class})
    public static RefStaticMethod<Void> updateCheckRecycle;
}

