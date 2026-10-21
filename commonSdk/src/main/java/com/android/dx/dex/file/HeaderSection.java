/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.HeaderItem;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.UniformItemSection;
import com.android.dx.rop.cst.Constant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class HeaderSection
extends UniformItemSection {
    private final List<HeaderItem> list;

    public HeaderSection(DexFile file) {
        super(null, file, 4);
        HeaderItem item = new HeaderItem();
        item.setIndex(0);
        this.list = Collections.singletonList(item);
    }

    @Override
    public IndexedItem get(Constant cst) {
        return null;
    }

    @Override
    public Collection<? extends Item> items() {
        return this.list;
    }

    @Override
    protected void orderItems() {
    }
}

