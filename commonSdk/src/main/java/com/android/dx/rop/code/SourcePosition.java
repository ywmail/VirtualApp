/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.cst.CstString;
import com.android.dx.util.Hex;

public final class SourcePosition {
    public static final SourcePosition NO_INFO = new SourcePosition(null, -1, -1);
    private final CstString sourceFile;
    private final int address;
    private final int line;

    public SourcePosition(CstString sourceFile, int address, int line) {
        if (address < -1) {
            throw new IllegalArgumentException("address < -1");
        }
        if (line < -1) {
            throw new IllegalArgumentException("line < -1");
        }
        this.sourceFile = sourceFile;
        this.address = address;
        this.line = line;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        if (this.sourceFile != null) {
            sb.append(this.sourceFile.toHuman());
            sb.append(":");
        }
        if (this.line >= 0) {
            sb.append(this.line);
        }
        sb.append('@');
        if (this.address < 0) {
            sb.append("????");
        } else {
            sb.append(Hex.u2(this.address));
        }
        return sb.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof SourcePosition)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        SourcePosition pos = (SourcePosition)other;
        return this.address == pos.address && this.sameLineAndFile(pos);
    }

    public int hashCode() {
        return this.sourceFile.hashCode() + this.address + this.line;
    }

    public boolean sameLine(SourcePosition other) {
        return this.line == other.line;
    }

    public boolean sameLineAndFile(SourcePosition other) {
        return this.line == other.line && (this.sourceFile == other.sourceFile || this.sourceFile != null && this.sourceFile.equals(other.sourceFile));
    }

    public CstString getSourceFile() {
        return this.sourceFile;
    }

    public int getAddress() {
        return this.address;
    }

    public int getLine() {
        return this.line;
    }
}

