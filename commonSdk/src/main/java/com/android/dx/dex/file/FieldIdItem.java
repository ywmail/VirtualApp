/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MemberIdItem;
import com.android.dx.dex.file.TypeIdsSection;
import com.android.dx.rop.cst.CstFieldRef;

public final class FieldIdItem
extends MemberIdItem {
    public FieldIdItem(CstFieldRef field) {
        super(field);
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_FIELD_ID_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        super.addContents(file);
        TypeIdsSection typeIds = file.getTypeIds();
        typeIds.intern(this.getFieldRef().getType());
    }

    public CstFieldRef getFieldRef() {
        return (CstFieldRef)this.getRef();
    }

    @Override
    protected int getTypoidIdx(DexFile file) {
        TypeIdsSection typeIds = file.getTypeIds();
        return typeIds.indexOf(this.getFieldRef().getType());
    }

    @Override
    protected String getTypoidName() {
        return "type_idx";
    }
}

