/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.ItemType;
import com.android.dx.util.AnnotatedOutput;

public abstract class Item {
    public abstract ItemType itemType();

    public final String typeName() {
        return this.itemType().toHuman();
    }

    public abstract int writeSize();

    public abstract void addContents(DexFile var1);

    public abstract void writeTo(DexFile var1, AnnotatedOutput var2);
}

