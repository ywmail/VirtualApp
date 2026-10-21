/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IdItem;
import com.android.dx.dex.file.ItemType;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public final class TypeIdItem
extends IdItem {
    public TypeIdItem(CstType type) {
        super(type);
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_TYPE_ID_ITEM;
    }

    @Override
    public int writeSize() {
        return 4;
    }

    @Override
    public void addContents(DexFile file) {
        file.getStringIds().intern(this.getDefiningClass().getDescriptor());
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        CstType type = this.getDefiningClass();
        CstString descriptor = type.getDescriptor();
        int idx = file.getStringIds().indexOf(descriptor);
        if (out.annotates()) {
            out.annotate(0, this.indexString() + ' ' + descriptor.toHuman());
            out.annotate(4, "  descriptor_idx: " + Hex.u4(idx));
        }
        out.writeInt(idx);
    }
}

