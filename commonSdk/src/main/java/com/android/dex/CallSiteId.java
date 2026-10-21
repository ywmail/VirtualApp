/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.Dex;
import com.android.dex.util.Unsigned;

public class CallSiteId
implements Comparable<CallSiteId> {
    private final Dex dex;
    private final int offset;

    public CallSiteId(Dex dex, int offset) {
        this.dex = dex;
        this.offset = offset;
    }

    @Override
    public int compareTo(CallSiteId o) {
        return Unsigned.compare(this.offset, o.offset);
    }

    public int getCallSiteOffset() {
        return this.offset;
    }

    public void writeTo(Dex.Section out) {
        out.writeInt(this.offset);
    }

    public String toString() {
        if (this.dex == null) {
            return String.valueOf(this.offset);
        }
        return this.dex.protoIds().get(this.offset).toString();
    }
}

