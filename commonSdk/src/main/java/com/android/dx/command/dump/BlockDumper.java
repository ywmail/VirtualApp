/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.command.dump;

import com.android.dx.cf.code.BasicBlocker;
import com.android.dx.cf.code.ByteBlock;
import com.android.dx.cf.code.ByteBlockList;
import com.android.dx.cf.code.ByteCatchList;
import com.android.dx.cf.code.BytecodeArray;
import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.code.Ropper;
import com.android.dx.cf.direct.CodeObserver;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.Method;
import com.android.dx.command.dump.Args;
import com.android.dx.command.dump.BaseDumper;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.DexTranslationAdvice;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.cst.CstType;
import com.android.dx.ssa.Optimizer;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import java.io.PrintStream;

public class BlockDumper
extends BaseDumper {
    private final boolean rop;
    protected DirectClassFile classFile;
    protected boolean suppressDump;
    private boolean first;
    private final boolean optimize;

    public static void dump(byte[] bytes, PrintStream out, String filePath, boolean rop, Args args) {
        BlockDumper bd = new BlockDumper(bytes, out, filePath, rop, args);
        bd.dump();
    }

    BlockDumper(byte[] bytes, PrintStream out, String filePath, boolean rop, Args args) {
        super(bytes, out, filePath, args);
        this.rop = rop;
        this.classFile = null;
        this.suppressDump = true;
        this.first = true;
        this.optimize = args.optimize;
    }

    public void dump() {
        byte[] bytes = this.getBytes();
        ByteArray ba = new ByteArray(bytes);
        this.classFile = new DirectClassFile(ba, this.getFilePath(), this.getStrictParse());
        this.classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        this.classFile.getMagic();
        DirectClassFile liveCf = new DirectClassFile(ba, this.getFilePath(), this.getStrictParse());
        liveCf.setAttributeFactory(StdAttributeFactory.THE_ONE);
        liveCf.setObserver(this);
        liveCf.getMagic();
    }

    @Override
    public void changeIndent(int indentDelta) {
        if (!this.suppressDump) {
            super.changeIndent(indentDelta);
        }
    }

    @Override
    public void parsed(ByteArray bytes, int offset, int len, String human) {
        if (!this.suppressDump) {
            super.parsed(bytes, offset, len, human);
        }
    }

    protected boolean shouldDumpMethod(String name) {
        return this.args.method == null || this.args.method.equals(name);
    }

    @Override
    public void startParsingMember(ByteArray bytes, int offset, String name, String descriptor) {
        if (descriptor.indexOf(40) < 0) {
            return;
        }
        if (!this.shouldDumpMethod(name)) {
            return;
        }
        this.suppressDump = false;
        if (this.first) {
            this.first = false;
        } else {
            this.parsed(bytes, offset, 0, "\n");
        }
        this.parsed(bytes, offset, 0, "method " + name + " " + descriptor);
        this.suppressDump = true;
    }

    @Override
    public void endParsingMember(ByteArray bytes, int offset, String name, String descriptor, Member member) {
        if (!(member instanceof Method)) {
            return;
        }
        if (!this.shouldDumpMethod(name)) {
            return;
        }
        if ((member.getAccessFlags() & 0x500) != 0) {
            return;
        }
        ConcreteMethod meth = new ConcreteMethod((Method)member, this.classFile, true, true);
        if (this.rop) {
            this.ropDump(meth);
        } else {
            this.regularDump(meth);
        }
    }

    private void regularDump(ConcreteMethod meth) {
        BytecodeArray code = meth.getCode();
        ByteArray bytes = code.getBytes();
        ByteBlockList list = BasicBlocker.identifyBlocks(meth);
        int sz = list.size();
        CodeObserver codeObserver = new CodeObserver(bytes, this);
        this.suppressDump = false;
        int byteAt = 0;
        for (int i = 0; i < sz; ++i) {
            int len;
            ByteBlock bb = list.get(i);
            int start = bb.getStart();
            int end = bb.getEnd();
            if (byteAt < start) {
                this.parsed(bytes, byteAt, start - byteAt, "dead code " + Hex.u2(byteAt) + ".." + Hex.u2(start));
            }
            this.parsed(bytes, start, 0, "block " + Hex.u2(bb.getLabel()) + ": " + Hex.u2(start) + ".." + Hex.u2(end));
            this.changeIndent(1);
            for (int j = start; j < end; j += len) {
                len = code.parseInstruction(j, codeObserver);
                codeObserver.setPreviousOffset(j);
            }
            IntList successors = bb.getSuccessors();
            int ssz = successors.size();
            if (ssz == 0) {
                this.parsed(bytes, end, 0, "returns");
            } else {
                for (int j = 0; j < ssz; ++j) {
                    int succ = successors.get(j);
                    this.parsed(bytes, end, 0, "next " + Hex.u2(succ));
                }
            }
            ByteCatchList catches = bb.getCatches();
            int csz = catches.size();
            for (int j = 0; j < csz; ++j) {
                ByteCatchList.Item one = catches.get(j);
                CstType exceptionClass = one.getExceptionClass();
                this.parsed(bytes, end, 0, "catch " + (exceptionClass == CstType.OBJECT ? "<any>" : exceptionClass.toHuman()) + " -> " + Hex.u2(one.getHandlerPc()));
            }
            this.changeIndent(-1);
            byteAt = end;
        }
        int end = bytes.size();
        if (byteAt < end) {
            this.parsed(bytes, byteAt, end - byteAt, "dead code " + Hex.u2(byteAt) + ".." + Hex.u2(end));
        }
        this.suppressDump = true;
    }

    private void ropDump(ConcreteMethod meth) {
        DexTranslationAdvice advice = DexTranslationAdvice.THE_ONE;
        BytecodeArray code = meth.getCode();
        ByteArray bytes = code.getBytes();
        RopMethod rmeth = Ropper.convert(meth, advice, this.classFile.getMethods(), this.dexOptions);
        StringBuilder sb = new StringBuilder(2000);
        if (this.optimize) {
            boolean isStatic = AccessFlags.isStatic(meth.getAccessFlags());
            int paramWidth = BlockDumper.computeParamWidth(meth, isStatic);
            rmeth = Optimizer.optimize(rmeth, paramWidth, isStatic, true, advice);
        }
        BasicBlockList blocks = rmeth.getBlocks();
        int[] order = blocks.getLabelsInOrder();
        sb.append("first " + Hex.u2(rmeth.getFirstLabel()) + "\n");
        for (int label : order) {
            BasicBlock bb = blocks.get(blocks.indexOfLabel(label));
            sb.append("block ");
            sb.append(Hex.u2(label));
            sb.append("\n");
            IntList preds = rmeth.labelToPredecessors(label);
            int psz = preds.size();
            for (int i = 0; i < psz; ++i) {
                sb.append("  pred ");
                sb.append(Hex.u2(preds.get(i)));
                sb.append("\n");
            }
            InsnList il = bb.getInsns();
            int ilsz = il.size();
            for (int i = 0; i < ilsz; ++i) {
                Insn one = il.get(i);
                sb.append("  ");
                sb.append(il.get(i).toHuman());
                sb.append("\n");
            }
            IntList successors = bb.getSuccessors();
            int ssz = successors.size();
            if (ssz == 0) {
                sb.append("  returns\n");
                continue;
            }
            int primary = bb.getPrimarySuccessor();
            for (int i = 0; i < ssz; ++i) {
                int succ = successors.get(i);
                sb.append("  next ");
                sb.append(Hex.u2(succ));
                if (ssz != 1 && succ == primary) {
                    sb.append(" *");
                }
                sb.append("\n");
            }
        }
        this.suppressDump = false;
        this.parsed(bytes, 0, bytes.size(), sb.toString());
        this.suppressDump = true;
    }
}

