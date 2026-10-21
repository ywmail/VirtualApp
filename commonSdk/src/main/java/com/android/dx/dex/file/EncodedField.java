/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.Leb128;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedMember;
import com.android.dx.dex.file.FieldIdsSection;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;

public final class EncodedField
extends EncodedMember
implements Comparable<EncodedField> {
    private final CstFieldRef field;

    public EncodedField(CstFieldRef field, int accessFlags) {
        super(accessFlags);
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        this.field = field;
    }

    public int hashCode() {
        return this.field.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof EncodedField)) {
            return false;
        }
        return this.compareTo((EncodedField)other) == 0;
    }

    @Override
    public int compareTo(EncodedField other) {
        return this.field.compareTo(other.field);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Hex.u2(this.getAccessFlags()));
        sb.append(' ');
        sb.append(this.field);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void addContents(DexFile file) {
        FieldIdsSection fieldIds = file.getFieldIds();
        fieldIds.intern(this.field);
    }

    @Override
    public CstString getName() {
        return this.field.getNat().getName();
    }

    @Override
    public String toHuman() {
        return this.field.toHuman();
    }

    @Override
    public void debugPrint(PrintWriter out, boolean verbose) {
        out.println(this.toString());
    }

    public CstFieldRef getRef() {
        return this.field;
    }

    @Override
    public int encode(DexFile file, AnnotatedOutput out, int lastIndex, int dumpSeq) {
        int fieldIdx = file.getFieldIds().indexOf(this.field);
        int diff = fieldIdx - lastIndex;
        int accessFlags = this.getAccessFlags();
        if (out.annotates()) {
            out.annotate(0, String.format("  [%x] %s", dumpSeq, this.field.toHuman()));
            out.annotate(Leb128.unsignedLeb128Size(diff), "    field_idx:    " + Hex.u4(fieldIdx));
            out.annotate(Leb128.unsignedLeb128Size(accessFlags), "    access_flags: " + AccessFlags.fieldString(accessFlags));
        }
        out.writeUleb128(diff);
        out.writeUleb128(accessFlags);
        return fieldIdx;
    }
}

