/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.Leb128;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.file.CodeItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedMember;
import com.android.dx.dex.file.MethodIdsSection;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;

public final class EncodedMethod
extends EncodedMember
implements Comparable<EncodedMethod> {
    private final CstMethodRef method;
    private final CodeItem code;

    public EncodedMethod(CstMethodRef method, int accessFlags, DalvCode code, TypeList throwsList) {
        super(accessFlags);
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.method = method;
        if (code == null) {
            this.code = null;
        } else {
            boolean isStatic = (accessFlags & 8) != 0;
            this.code = new CodeItem(method, code, isStatic, throwsList);
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof EncodedMethod)) {
            return false;
        }
        return this.compareTo((EncodedMethod)other) == 0;
    }

    @Override
    public int compareTo(EncodedMethod other) {
        return this.method.compareTo(other.method);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Hex.u2(this.getAccessFlags()));
        sb.append(' ');
        sb.append(this.method);
        if (this.code != null) {
            sb.append(' ');
            sb.append(this.code);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void addContents(DexFile file) {
        MethodIdsSection methodIds = file.getMethodIds();
        MixedItemSection wordData = file.getWordData();
        methodIds.intern(this.method);
        if (this.code != null) {
            wordData.add(this.code);
        }
    }

    @Override
    public final String toHuman() {
        return this.method.toHuman();
    }

    @Override
    public final CstString getName() {
        return this.method.getNat().getName();
    }

    @Override
    public void debugPrint(PrintWriter out, boolean verbose) {
        if (this.code == null) {
            out.println(this.getRef().toHuman() + ": abstract or native");
        } else {
            this.code.debugPrint(out, "  ", verbose);
        }
    }

    public final CstMethodRef getRef() {
        return this.method;
    }

    @Override
    public int encode(DexFile file, AnnotatedOutput out, int lastIndex, int dumpSeq) {
        boolean shouldHaveCode;
        int methodIdx = file.getMethodIds().indexOf(this.method);
        int diff = methodIdx - lastIndex;
        int accessFlags = this.getAccessFlags();
        int codeOff = OffsettedItem.getAbsoluteOffsetOr0(this.code);
        boolean hasCode = codeOff != 0;
        boolean bl = shouldHaveCode = (accessFlags & 0x500) == 0;
        if (hasCode != shouldHaveCode) {
            throw new UnsupportedOperationException("code vs. access_flags mismatch");
        }
        if (out.annotates()) {
            out.annotate(0, String.format("  [%x] %s", dumpSeq, this.method.toHuman()));
            out.annotate(Leb128.unsignedLeb128Size(diff), "    method_idx:   " + Hex.u4(methodIdx));
            out.annotate(Leb128.unsignedLeb128Size(accessFlags), "    access_flags: " + AccessFlags.methodString(accessFlags));
            out.annotate(Leb128.unsignedLeb128Size(codeOff), "    code_off:     " + Hex.u4(codeOff));
        }
        out.writeUleb128(diff);
        out.writeUleb128(accessFlags);
        out.writeUleb128(codeOff);
        return methodIdx;
    }
}

