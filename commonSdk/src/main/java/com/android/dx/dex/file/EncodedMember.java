/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ToHuman;
import java.io.PrintWriter;

public abstract class EncodedMember
implements ToHuman {
    private final int accessFlags;

    public EncodedMember(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public final int getAccessFlags() {
        return this.accessFlags;
    }

    public abstract CstString getName();

    public abstract void debugPrint(PrintWriter var1, boolean var2);

    public abstract void addContents(DexFile var1);

    public abstract int encode(DexFile var1, AnnotatedOutput var2, int var3, int var4);
}

