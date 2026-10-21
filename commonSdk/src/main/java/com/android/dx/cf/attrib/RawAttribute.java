/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.attrib;

import com.android.dx.cf.attrib.BaseAttribute;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.util.ByteArray;

public final class RawAttribute
extends BaseAttribute {
    private final ByteArray data;
    private final ConstantPool pool;

    public RawAttribute(String name, ByteArray data, ConstantPool pool) {
        super(name);
        if (data == null) {
            throw new NullPointerException("data == null");
        }
        this.data = data;
        this.pool = pool;
    }

    public RawAttribute(String name, ByteArray data, int offset, int length, ConstantPool pool) {
        this(name, data.slice(offset, offset + length), pool);
    }

    public ByteArray getData() {
        return this.data;
    }

    @Override
    public int byteLength() {
        return this.data.size() + 6;
    }

    public ConstantPool getPool() {
        return this.pool;
    }
}

