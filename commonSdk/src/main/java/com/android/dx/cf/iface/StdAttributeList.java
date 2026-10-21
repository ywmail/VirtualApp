/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.iface;

import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.util.FixedSizeList;

public final class StdAttributeList
extends FixedSizeList
implements AttributeList {
    public StdAttributeList(int size) {
        super(size);
    }

    @Override
    public Attribute get(int n) {
        return (Attribute)this.get0(n);
    }

    @Override
    public int byteLength() {
        int sz = this.size();
        int result = 2;
        for (int i = 0; i < sz; ++i) {
            result += this.get(i).byteLength();
        }
        return result;
    }

    @Override
    public Attribute findFirst(String name) {
        int sz = this.size();
        for (int i = 0; i < sz; ++i) {
            Attribute att = this.get(i);
            if (!att.getName().equals(name)) continue;
            return att;
        }
        return null;
    }

    @Override
    public Attribute findNext(Attribute attrib) {
        int at;
        int sz;
        block4: {
            sz = this.size();
            for (at = 0; at < sz; ++at) {
                Attribute att = this.get(at);
                if (att != attrib) {
                    continue;
                }
                break block4;
            }
            return null;
        }
        String name = attrib.getName();
        ++at;
        while (at < sz) {
            Attribute att = this.get(at);
            if (att.getName().equals(name)) {
                return att;
            }
            ++at;
        }
        return null;
    }

    public void set(int n, Attribute attribute) {
        this.set0(n, attribute);
    }
}

