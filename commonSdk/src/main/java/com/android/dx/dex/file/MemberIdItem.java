/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IdItem;
import com.android.dx.dex.file.StringIdsSection;
import com.android.dx.dex.file.TypeIdsSection;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public abstract class MemberIdItem
extends IdItem {
    private final CstMemberRef cst;

    public MemberIdItem(CstMemberRef cst) {
        super(cst.getDefiningClass());
        this.cst = cst;
    }

    @Override
    public int writeSize() {
        return 8;
    }

    @Override
    public void addContents(DexFile file) {
        super.addContents(file);
        StringIdsSection stringIds = file.getStringIds();
        stringIds.intern(this.getRef().getNat().getName());
    }

    @Override
    public final void writeTo(DexFile file, AnnotatedOutput out) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        CstNat nat = this.cst.getNat();
        int classIdx = typeIds.indexOf(this.getDefiningClass());
        int nameIdx = stringIds.indexOf(nat.getName());
        int typoidIdx = this.getTypoidIdx(file);
        if (out.annotates()) {
            out.annotate(0, this.indexString() + ' ' + this.cst.toHuman());
            out.annotate(2, "  class_idx: " + Hex.u2(classIdx));
            out.annotate(2, String.format("  %-10s %s", this.getTypoidName() + ':', Hex.u2(typoidIdx)));
            out.annotate(4, "  name_idx:  " + Hex.u4(nameIdx));
        }
        out.writeShort(classIdx);
        out.writeShort(typoidIdx);
        out.writeInt(nameIdx);
    }

    protected abstract int getTypoidIdx(DexFile var1);

    protected abstract String getTypoidName();

    public final CstMemberRef getRef() {
        return this.cst;
    }
}

