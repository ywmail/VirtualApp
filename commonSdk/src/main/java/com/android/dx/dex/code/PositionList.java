/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.CodeAddress;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.FixedSizeList;

public final class PositionList
extends FixedSizeList {
    public static final PositionList EMPTY = new PositionList(0);
    public static final int NONE = 1;
    public static final int LINES = 2;
    public static final int IMPORTANT = 3;

    public static PositionList make(DalvInsnList insns, int howMuch) {
        SourcePosition noInfo;
        switch (howMuch) {
            case 1: {
                return EMPTY;
            }
            case 2: 
            case 3: {
                break;
            }
            default: {
                throw new IllegalArgumentException("bogus howMuch");
            }
        }
        SourcePosition cur = noInfo = SourcePosition.NO_INFO;
        int sz = insns.size();
        Entry[] arr = new Entry[sz];
        boolean lastWasTarget = false;
        int at = 0;
        for (int i = 0; i < sz; ++i) {
            DalvInsn insn = insns.get(i);
            if (insn instanceof CodeAddress) {
                lastWasTarget = true;
                continue;
            }
            SourcePosition pos = insn.getPosition();
            if (pos.equals(noInfo) || pos.sameLine(cur) || howMuch == 3 && !lastWasTarget) continue;
            cur = pos;
            arr[at] = new Entry(insn.getAddress(), pos);
            ++at;
            lastWasTarget = false;
        }
        PositionList result = new PositionList(at);
        for (int i = 0; i < at; ++i) {
            result.set(i, arr[i]);
        }
        result.setImmutable();
        return result;
    }

    public PositionList(int size) {
        super(size);
    }

    public Entry get(int n) {
        return (Entry)this.get0(n);
    }

    public void set(int n, Entry entry) {
        this.set0(n, entry);
    }

    public static class Entry {
        private final int address;
        private final SourcePosition position;

        public Entry(int address, SourcePosition position) {
            if (address < 0) {
                throw new IllegalArgumentException("address < 0");
            }
            if (position == null) {
                throw new NullPointerException("position == null");
            }
            this.address = address;
            this.position = position;
        }

        public int getAddress() {
            return this.address;
        }

        public SourcePosition getPosition() {
            return this.position;
        }
    }
}

