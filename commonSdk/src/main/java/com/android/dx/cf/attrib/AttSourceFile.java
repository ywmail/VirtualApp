/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.cst.CstString;

public final class AttSourceFile
extends BaseAttribute {
    public static final String ATTRIBUTE_NAME = "SourceFile";
    private final CstString sourceFile;

    public AttSourceFile(CstString sourceFile) {
        super(ATTRIBUTE_NAME);
        if (sourceFile == null) {
            throw new NullPointerException("sourceFile == null");
        }
        this.sourceFile = sourceFile;
    }

    @Override
    public int byteLength() {
        return 8;
    }

    public CstString getSourceFile() {
        return this.sourceFile;
    }
}

