/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.StringDataItem;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public final class StringIdItem
extends IndexedItem
implements Comparable {
    private final CstString value;
    private StringDataItem data;

    public StringIdItem(CstString value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        this.value = value;
        this.data = null;
    }

    public boolean equals(Object other) {
        if (!(other instanceof StringIdItem)) {
            return false;
        }
        StringIdItem otherString = (StringIdItem)other;
        return this.value.equals(otherString.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public int compareTo(Object other) {
        StringIdItem otherString = (StringIdItem)other;
        return this.value.compareTo(otherString.value);
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_STRING_ID_ITEM;
    }

    @Override
    public int writeSize() {
        return 4;
    }

    @Override
    public void addContents(DexFile file) {
        if (this.data == null) {
            MixedItemSection stringData = file.getStringData();
            this.data = new StringDataItem(this.value);
            stringData.add(this.data);
        }
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        int dataOff = this.data.getAbsoluteOffset();
        if (out.annotates()) {
            out.annotate(0, this.indexString() + ' ' + this.value.toQuoted(100));
            out.annotate(4, "  string_data_off: " + Hex.u4(dataOff));
        }
        out.writeInt(dataOff);
    }

    public CstString getValue() {
        return this.value;
    }

    public StringDataItem getData() {
        return this.data;
    }
}

