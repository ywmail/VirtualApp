/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.direct.AttributeFactory;
import com.android.dx.cf.direct.AttributeListParser;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.cf.iface.StdAttributeList;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;

abstract class MemberListParser {
    private final DirectClassFile cf;
    private final CstType definer;
    private final int offset;
    private final AttributeFactory attributeFactory;
    private int endOffset;
    private ParseObserver observer;

    public MemberListParser(DirectClassFile cf, CstType definer, int offset, AttributeFactory attributeFactory) {
        if (cf == null) {
            throw new NullPointerException("cf == null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (attributeFactory == null) {
            throw new NullPointerException("attributeFactory == null");
        }
        this.cf = cf;
        this.definer = definer;
        this.offset = offset;
        this.attributeFactory = attributeFactory;
        this.endOffset = -1;
    }

    public int getEndOffset() {
        this.parseIfNecessary();
        return this.endOffset;
    }

    public final void setObserver(ParseObserver observer) {
        this.observer = observer;
    }

    protected final void parseIfNecessary() {
        if (this.endOffset < 0) {
            this.parse();
        }
    }

    protected final int getCount() {
        ByteArray bytes = this.cf.getBytes();
        return bytes.getUnsignedShort(this.offset);
    }

    protected final CstType getDefiner() {
        return this.definer;
    }

    protected abstract String humanName();

    protected abstract String humanAccessFlags(int var1);

    protected abstract int getAttributeContext();

    protected abstract Member set(int var1, int var2, CstNat var3, AttributeList var4);

    private void parse() {
        int attributeContext = this.getAttributeContext();
        int count = this.getCount();
        int at = this.offset + 2;
        ByteArray bytes = this.cf.getBytes();
        ConstantPool pool = this.cf.getConstantPool();
        if (this.observer != null) {
            this.observer.parsed(bytes, this.offset, 2, this.humanName() + "s_count: " + Hex.u2(count));
        }
        for (int i = 0; i < count; ++i) {
            try {
                int accessFlags = bytes.getUnsignedShort(at);
                int nameIdx = bytes.getUnsignedShort(at + 2);
                int descIdx = bytes.getUnsignedShort(at + 4);
                CstString name = (CstString)pool.get(nameIdx);
                CstString desc = (CstString)pool.get(descIdx);
                if (this.observer != null) {
                    this.observer.startParsingMember(bytes, at, name.getString(), desc.getString());
                    this.observer.parsed(bytes, at, 0, "\n" + this.humanName() + "s[" + i + "]:\n");
                    this.observer.changeIndent(1);
                    this.observer.parsed(bytes, at, 2, "access_flags: " + this.humanAccessFlags(accessFlags));
                    this.observer.parsed(bytes, at + 2, 2, "name: " + name.toHuman());
                    this.observer.parsed(bytes, at + 4, 2, "descriptor: " + desc.toHuman());
                }
                AttributeListParser parser = new AttributeListParser(this.cf, attributeContext, at += 6, this.attributeFactory);
                parser.setObserver(this.observer);
                at = parser.getEndOffset();
                StdAttributeList attributes = parser.getList();
                attributes.setImmutable();
                CstNat nat = new CstNat(name, desc);
                Member member = this.set(i, accessFlags, nat, attributes);
                if (this.observer == null) continue;
                this.observer.changeIndent(-1);
                this.observer.parsed(bytes, at, 0, "end " + this.humanName() + "s[" + i + "]\n");
                this.observer.endParsingMember(bytes, at, name.getString(), desc.getString(), member);
                continue;
            }
            catch (ParseException ex) {
                ex.addContext("...while parsing " + this.humanName() + "s[" + i + "]");
                throw ex;
            }
            catch (RuntimeException ex) {
                ParseException pe = new ParseException(ex);
                pe.addContext("...while parsing " + this.humanName() + "s[" + i + "]");
                throw pe;
            }
        }
        this.endOffset = at;
    }
}

