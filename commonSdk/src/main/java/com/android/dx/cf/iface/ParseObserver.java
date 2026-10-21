/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Member;
import com.android.dx.util.ByteArray;

public interface ParseObserver {
    public void changeIndent(int var1);

    public void startParsingMember(ByteArray var1, int var2, String var3, String var4);

    public void endParsingMember(ByteArray var1, int var2, String var3, String var4, Member var5);

    public void parsed(ByteArray var1, int var2, int var3, String var4);
}

