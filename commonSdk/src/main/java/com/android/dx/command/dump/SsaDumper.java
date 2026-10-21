//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.android.dx.command.dump;

import com.android.dx.cf.code.ConcreteMethod;
import com.android.dx.cf.code.Ropper;
import com.android.dx.cf.iface.Member;
import com.android.dx.cf.iface.Method;
import com.android.dx.rop.code.AccessFlags;
import com.android.dx.rop.code.DexTranslationAdvice;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.code.TranslationAdvice;
import com.android.dx.ssa.Optimizer;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.SsaMethod;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import com.android.dx.util.IntList;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

public class SsaDumper extends BlockDumper {
    public static void dump(byte[] bytes, PrintStream out, String filePath, Args args) {
        SsaDumper sd = new SsaDumper(bytes, out, filePath, args);
        sd.dump();
    }

    private SsaDumper(byte[] bytes, PrintStream out, String filePath, Args args) {
        super(bytes, out, filePath, true, args);
    }

    public void endParsingMember(ByteArray bytes, int offset, String name, String descriptor, Member member) {
        if (member instanceof Method) {
            if (this.shouldDumpMethod(name)) {
                if ((member.getAccessFlags() & 1280) == 0) {
                    ConcreteMethod meth = new ConcreteMethod((Method)member, this.classFile, true, true);
                    TranslationAdvice advice = DexTranslationAdvice.THE_ONE;
                    RopMethod rmeth = Ropper.convert(meth, advice, this.classFile.getMethods(), this.dexOptions);
                    SsaMethod ssaMeth = null;
                    boolean isStatic = AccessFlags.isStatic(meth.getAccessFlags());
                    int paramWidth = computeParamWidth(meth, isStatic);
                    if (this.args.ssaStep == null) {
                        ssaMeth = Optimizer.debugNoRegisterAllocation(rmeth, paramWidth, isStatic, true, advice, EnumSet.allOf(Optimizer.OptionalStep.class));
                    } else if ("edge-split".equals(this.args.ssaStep)) {
                        ssaMeth = Optimizer.debugEdgeSplit(rmeth, paramWidth, isStatic, true, advice);
                    } else if ("phi-placement".equals(this.args.ssaStep)) {
                        ssaMeth = Optimizer.debugPhiPlacement(rmeth, paramWidth, isStatic, true, advice);
                    } else if ("renaming".equals(this.args.ssaStep)) {
                        ssaMeth = Optimizer.debugRenaming(rmeth, paramWidth, isStatic, true, advice);
                    } else if ("dead-code".equals(this.args.ssaStep)) {
                        ssaMeth = Optimizer.debugDeadCodeRemover(rmeth, paramWidth, isStatic, true, advice);
                    }

                    StringBuilder sb = new StringBuilder(2000);
                    sb.append("first ");
                    sb.append(Hex.u2(ssaMeth.blockIndexToRopLabel(ssaMeth.getEntryBlockIndex())));
                    sb.append('\n');
                    ArrayList<SsaBasicBlock> blocks = ssaMeth.getBlocks();
                    ArrayList<SsaBasicBlock> sortedBlocks = (ArrayList)blocks.clone();
                    Collections.sort(sortedBlocks, SsaBasicBlock.LABEL_COMPARATOR);
                    Iterator var15 = sortedBlocks.iterator();

                    while(var15.hasNext()) {
                        SsaBasicBlock block = (SsaBasicBlock)var15.next();
                        sb.append("block ").append(Hex.u2(block.getRopLabel())).append('\n');
                        BitSet preds = block.getPredecessors();

                        int primary;
                        for(primary = preds.nextSetBit(0); primary >= 0; primary = preds.nextSetBit(primary + 1)) {
                            sb.append("  pred ");
                            sb.append(Hex.u2(ssaMeth.blockIndexToRopLabel(primary)));
                            sb.append('\n');
                        }

                        sb.append("  live in:" + block.getLiveInRegs());
                        sb.append("\n");
                        Iterator var22 = block.getInsns().iterator();

                        while(var22.hasNext()) {
                            SsaInsn insn = (SsaInsn)var22.next();
                            sb.append("  ");
                            sb.append(insn.toHuman());
                            sb.append('\n');
                        }

                        if (block.getSuccessors().cardinality() == 0) {
                            sb.append("  returns\n");
                        } else {
                            primary = block.getPrimarySuccessorRopLabel();
                            IntList succLabelList = block.getRopLabelSuccessorList();
                            int szSuccLabels = succLabelList.size();

                            for(int i = 0; i < szSuccLabels; ++i) {
                                sb.append("  next ");
                                sb.append(Hex.u2(succLabelList.get(i)));
                                if (szSuccLabels != 1 && primary == succLabelList.get(i)) {
                                    sb.append(" *");
                                }

                                sb.append('\n');
                            }
                        }

                        sb.append("  live out:" + block.getLiveOutRegs());
                        sb.append("\n");
                    }

                    this.suppressDump = false;
                    this.parsed(bytes, 0, bytes.size(), sb.toString());
                    this.suppressDump = true;
                }
            }
        }
    }
}
