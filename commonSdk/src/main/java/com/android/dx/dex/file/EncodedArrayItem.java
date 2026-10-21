/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.dex.file.ValueEncoder;
import com.android.dx.rop.cst.CstArray;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;

public final class EncodedArrayItem
extends OffsettedItem {
    private static final int ALIGNMENT = 1;
    private final CstArray array;
    private byte[] encodedForm;

    public EncodedArrayItem(CstArray array) {
        super(1, -1);
        if (array == null) {
            throw new NullPointerException("array == null");
        }
        this.array = array;
        this.encodedForm = null;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ENCODED_ARRAY_ITEM;
    }

    public int hashCode() {
        return this.array.hashCode();
    }

    @Override
    protected int compareTo0(OffsettedItem other) {
        EncodedArrayItem otherArray = (EncodedArrayItem)other;
        return this.array.compareTo(otherArray.array);
    }

    @Override
    public String toHuman() {
        return this.array.toHuman();
    }

    @Override
    public void addContents(DexFile file) {
        ValueEncoder.addContents(file, this.array);
    }

    @Override
    protected void place0(Section addedTo, int offset) {
        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
        ValueEncoder encoder = new ValueEncoder(addedTo.getFile(), out);
        encoder.writeArray(this.array, false);
        this.encodedForm = out.toByteArray();
        this.setWriteSize(this.encodedForm.length);
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        boolean annotates = out.annotates();
        if (annotates) {
            out.annotate(0, this.offsetString() + " encoded array");
            ValueEncoder encoder = new ValueEncoder(file, out);
            encoder.writeArray(this.array, true);
        } else {
            out.write(this.encodedForm);
        }
    }
}

