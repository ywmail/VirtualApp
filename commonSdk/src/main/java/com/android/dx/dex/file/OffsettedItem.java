/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.Section;
import com.android.dx.util.AnnotatedOutput;

public abstract class OffsettedItem
extends Item
implements Comparable<OffsettedItem> {
    private final int alignment;
    private int writeSize;
    private Section addedTo;
    private int offset;

    public static int getAbsoluteOffsetOr0(OffsettedItem item) {
        if (item == null) {
            return 0;
        }
        return item.getAbsoluteOffset();
    }

    public OffsettedItem(int alignment, int writeSize) {
        Section.validateAlignment(alignment);
        if (writeSize < -1) {
            throw new IllegalArgumentException("writeSize < -1");
        }
        this.alignment = alignment;
        this.writeSize = writeSize;
        this.addedTo = null;
        this.offset = -1;
    }

    public final boolean equals(Object other) {
        ItemType otherType;
        if (this == other) {
            return true;
        }
        OffsettedItem otherItem = (OffsettedItem)other;
        ItemType thisType = this.itemType();
        if (thisType != (otherType = otherItem.itemType())) {
            return false;
        }
        return this.compareTo0(otherItem) == 0;
    }

    @Override
    public final int compareTo(OffsettedItem other) {
        ItemType otherType;
        if (this == other) {
            return 0;
        }
        ItemType thisType = this.itemType();
        if (thisType != (otherType = other.itemType())) {
            return thisType.compareTo(otherType);
        }
        return this.compareTo0(other);
    }

    public final void setWriteSize(int writeSize) {
        if (writeSize < 0) {
            throw new IllegalArgumentException("writeSize < 0");
        }
        if (this.writeSize >= 0) {
            throw new UnsupportedOperationException("writeSize already set");
        }
        this.writeSize = writeSize;
    }

    @Override
    public final int writeSize() {
        if (this.writeSize < 0) {
            throw new UnsupportedOperationException("writeSize is unknown");
        }
        return this.writeSize;
    }

    @Override
    public final void writeTo(DexFile file, AnnotatedOutput out) {
        out.alignTo(this.alignment);
        try {
            if (this.writeSize < 0) {
                throw new UnsupportedOperationException("writeSize is unknown");
            }
            out.assertCursor(this.getAbsoluteOffset());
        }
        catch (RuntimeException ex) {
            throw ExceptionWithContext.withContext(ex, "...while writing " + this);
        }
        this.writeTo0(file, out);
    }

    public final int getRelativeOffset() {
        if (this.offset < 0) {
            throw new RuntimeException("offset not yet known");
        }
        return this.offset;
    }

    public final int getAbsoluteOffset() {
        if (this.offset < 0) {
            throw new RuntimeException("offset not yet known");
        }
        return this.addedTo.getAbsoluteOffset(this.offset);
    }

    public final int place(Section addedTo, int offset) {
        if (addedTo == null) {
            throw new NullPointerException("addedTo == null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (this.addedTo != null) {
            throw new RuntimeException("already written");
        }
        int mask = this.alignment - 1;
        offset = offset + mask & ~mask;
        this.addedTo = addedTo;
        this.offset = offset;
        this.place0(addedTo, offset);
        return offset;
    }

    public final int getAlignment() {
        return this.alignment;
    }

    public final String offsetString() {
        return '[' + Integer.toHexString(this.getAbsoluteOffset()) + ']';
    }

    public abstract String toHuman();

    protected int compareTo0(OffsettedItem other) {
        throw new UnsupportedOperationException("unsupported");
    }

    protected void place0(Section addedTo, int offset) {
    }

    protected abstract void writeTo0(DexFile var1, AnnotatedOutput var2);
}

