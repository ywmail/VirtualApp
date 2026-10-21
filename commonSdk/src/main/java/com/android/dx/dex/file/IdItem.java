/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.TypeIdsSection;
import com.android.dx.rop.cst.CstType;

public abstract class IdItem
extends IndexedItem {
    private final CstType type;

    public IdItem(CstType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.type = type;
    }

    @Override
    public void addContents(DexFile file) {
        TypeIdsSection typeIds = file.getTypeIds();
        typeIds.intern(this.type);
    }

    public final CstType getDefiningClass() {
        return this.type;
    }
}

