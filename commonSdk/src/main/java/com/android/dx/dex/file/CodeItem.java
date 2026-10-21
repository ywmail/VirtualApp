/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.file.CatchStructs;
import com.android.dx.dex.file.DebugInfoItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.dex.file.TypeIdsSection;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;

public final class CodeItem
extends OffsettedItem {
    private static final int ALIGNMENT = 4;
    private static final int HEADER_SIZE = 16;
    private final CstMethodRef ref;
    private final DalvCode code;
    private CatchStructs catches;
    private final boolean isStatic;
    private final TypeList throwsList;
    private DebugInfoItem debugInfo;

    public CodeItem(CstMethodRef ref, DalvCode code, boolean isStatic, TypeList throwsList) {
        super(4, -1);
        if (ref == null) {
            throw new NullPointerException("ref == null");
        }
        if (code == null) {
            throw new NullPointerException("code == null");
        }
        if (throwsList == null) {
            throw new NullPointerException("throwsList == null");
        }
        this.ref = ref;
        this.code = code;
        this.isStatic = isStatic;
        this.throwsList = throwsList;
        this.catches = null;
        this.debugInfo = null;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_CODE_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        MixedItemSection byteData = file.getByteData();
        TypeIdsSection typeIds = file.getTypeIds();
        if (this.code.hasPositions() || this.code.hasLocals()) {
            this.debugInfo = new DebugInfoItem(this.code, this.isStatic, this.ref);
            byteData.add(this.debugInfo);
        }
        if (this.code.hasAnyCatches()) {
            for (Type type : this.code.getCatchTypes()) {
                typeIds.intern(type);
            }
            this.catches = new CatchStructs(this.code);
        }
        for (Constant c : this.code.getInsnConstants()) {
            file.internIfAppropriate(c);
        }
    }

    public String toString() {
        return "CodeItem{" + this.toHuman() + "}";
    }

    @Override
    public String toHuman() {
        return this.ref.toHuman();
    }

    public CstMethodRef getRef() {
        return this.ref;
    }

    public void debugPrint(PrintWriter out, String prefix, boolean verbose) {
        out.println(this.ref.toHuman() + ":");
        DalvInsnList insns = this.code.getInsns();
        out.println("regs: " + Hex.u2(this.getRegistersSize()) + "; ins: " + Hex.u2(this.getInsSize()) + "; outs: " + Hex.u2(this.getOutsSize()));
        insns.debugPrint(out, prefix, verbose);
        String prefix2 = prefix + "  ";
        if (this.catches != null) {
            out.print(prefix);
            out.println("catches");
            this.catches.debugPrint(out, prefix2);
        }
        if (this.debugInfo != null) {
            out.print(prefix);
            out.println("debug info");
            this.debugInfo.debugPrint(out, prefix2);
        }
    }

    @Override
    protected void place0(Section addedTo, int offset) {
        int catchesSize;
        final DexFile file = addedTo.getFile();
        this.code.assignIndices(new DalvCode.AssignIndicesCallback(){

            @Override
            public int getIndex(Constant cst) {
                IndexedItem item = file.findItemOrNull(cst);
                if (item == null) {
                    return -1;
                }
                return item.getIndex();
            }
        });
        if (this.catches != null) {
            this.catches.encode(file);
            catchesSize = this.catches.writeSize();
        } else {
            catchesSize = 0;
        }
        int insnsSize = this.code.getInsns().codeSize();
        if ((insnsSize & 1) != 0) {
            ++insnsSize;
        }
        this.setWriteSize(16 + insnsSize * 2 + catchesSize);
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        int debugOff;
        boolean annotates = out.annotates();
        int regSz = this.getRegistersSize();
        int outsSz = this.getOutsSize();
        int insSz = this.getInsSize();
        int insnsSz = this.code.getInsns().codeSize();
        boolean needPadding = (insnsSz & 1) != 0;
        int triesSz = this.catches == null ? 0 : this.catches.triesSize();
        int n = debugOff = this.debugInfo == null ? 0 : this.debugInfo.getAbsoluteOffset();
        if (annotates) {
            out.annotate(0, this.offsetString() + ' ' + this.ref.toHuman());
            out.annotate(2, "  registers_size: " + Hex.u2(regSz));
            out.annotate(2, "  ins_size:       " + Hex.u2(insSz));
            out.annotate(2, "  outs_size:      " + Hex.u2(outsSz));
            out.annotate(2, "  tries_size:     " + Hex.u2(triesSz));
            out.annotate(4, "  debug_off:      " + Hex.u4(debugOff));
            out.annotate(4, "  insns_size:     " + Hex.u4(insnsSz));
            int size = this.throwsList.size();
            if (size != 0) {
                out.annotate(0, "  throws " + StdTypeList.toHuman(this.throwsList));
            }
        }
        out.writeShort(regSz);
        out.writeShort(insSz);
        out.writeShort(outsSz);
        out.writeShort(triesSz);
        out.writeInt(debugOff);
        out.writeInt(insnsSz);
        this.writeCodes(file, out);
        if (this.catches != null) {
            if (needPadding) {
                if (annotates) {
                    out.annotate(2, "  padding: 0");
                }
                out.writeShort(0);
            }
            this.catches.writeTo(file, out);
        }
        if (annotates && this.debugInfo != null) {
            out.annotate(0, "  debug info");
            this.debugInfo.annotateTo(file, out, "    ");
        }
    }

    private void writeCodes(DexFile file, AnnotatedOutput out) {
        DalvInsnList insns = this.code.getInsns();
        try {
            insns.writeTo(out);
        }
        catch (RuntimeException ex) {
            throw ExceptionWithContext.withContext(ex, "...while writing instructions for " + this.ref.toHuman());
        }
    }

    private int getInsSize() {
        return this.ref.getParameterWordCount(this.isStatic);
    }

    private int getOutsSize() {
        return this.code.getInsns().getOutsSize();
    }

    private int getRegistersSize() {
        return this.code.getInsns().getRegistersSize();
    }
}

