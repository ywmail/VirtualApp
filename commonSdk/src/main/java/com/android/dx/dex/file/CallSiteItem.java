/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.dex.file.ValueEncoder;
import com.android.dx.rop.cst.CstCallSite;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;

public final class CallSiteItem
extends OffsettedItem {
    private final CstCallSite value;
    private byte[] encodedForm;

    public CallSiteItem(CstCallSite value) {
        super(1, CallSiteItem.writeSize(value));
        this.value = value;
    }

    private static int writeSize(CstCallSite value) {
        return -1;
    }

    @Override
    protected void place0(Section addedTo, int offset) {
        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
        ValueEncoder encoder = new ValueEncoder(addedTo.getFile(), out);
        encoder.writeArray(this.value, true);
        this.encodedForm = out.toByteArray();
        this.setWriteSize(this.encodedForm.length);
    }

    @Override
    public String toHuman() {
        return this.value.toHuman();
    }

    public String toString() {
        return this.value.toString();
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        if (out.annotates()) {
            out.annotate(0, this.offsetString() + " call site");
            ValueEncoder encoder = new ValueEncoder(file, out);
            encoder.writeArray(this.value, true);
        } else {
            out.write(this.encodedForm);
        }
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ENCODED_ARRAY_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        ValueEncoder.addContents(file, this.value);
    }
}

