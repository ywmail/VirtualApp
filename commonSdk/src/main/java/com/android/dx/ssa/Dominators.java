/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.ssa;

import com.android.dx.ssa.DomFront;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaMethod;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;

public final class Dominators {
    private final boolean postdom;
    private final SsaMethod meth;
    private final ArrayList<SsaBasicBlock> blocks;
    private final DFSInfo[] info;
    private final ArrayList<SsaBasicBlock> vertex;
    private final DomFront.DomInfo[] domInfos;

    private Dominators(SsaMethod meth, DomFront.DomInfo[] domInfos, boolean postdom) {
        this.meth = meth;
        this.domInfos = domInfos;
        this.postdom = postdom;
        this.blocks = meth.getBlocks();
        this.info = new DFSInfo[this.blocks.size() + 2];
        this.vertex = new ArrayList();
    }

    public static Dominators make(SsaMethod meth, DomFront.DomInfo[] domInfos, boolean postdom) {
        Dominators result = new Dominators(meth, domInfos, postdom);
        result.run();
        return result;
    }

    private BitSet getSuccs(SsaBasicBlock block) {
        if (this.postdom) {
            return block.getPredecessors();
        }
        return block.getSuccessors();
    }

    private BitSet getPreds(SsaBasicBlock block) {
        if (this.postdom) {
            return block.getSuccessors();
        }
        return block.getPredecessors();
    }

    private void compress(SsaBasicBlock in) {
        DFSInfo bbInfo = this.info[in.getIndex()];
        DFSInfo ancestorbbInfo = this.info[bbInfo.ancestor.getIndex()];
        if (ancestorbbInfo.ancestor != null) {
            ArrayList<SsaBasicBlock> worklist = new ArrayList<SsaBasicBlock>();
            HashSet<SsaBasicBlock> visited = new HashSet<SsaBasicBlock>();
            worklist.add(in);
            while (!worklist.isEmpty()) {
                int wsize = worklist.size();
                SsaBasicBlock v = (SsaBasicBlock)worklist.get(wsize - 1);
                DFSInfo vbbInfo = this.info[v.getIndex()];
                SsaBasicBlock vAncestor = vbbInfo.ancestor;
                DFSInfo vabbInfo = this.info[vAncestor.getIndex()];
                if (visited.add(vAncestor) && vabbInfo.ancestor != null) {
                    worklist.add(vAncestor);
                    continue;
                }
                worklist.remove(wsize - 1);
                if (vabbInfo.ancestor == null) continue;
                SsaBasicBlock vAncestorRep = vabbInfo.rep;
                SsaBasicBlock vRep = vbbInfo.rep;
                if (this.info[vAncestorRep.getIndex()].semidom < this.info[vRep.getIndex()].semidom) {
                    vbbInfo.rep = vAncestorRep;
                }
                vbbInfo.ancestor = vabbInfo.ancestor;
            }
        }
    }

    private SsaBasicBlock eval(SsaBasicBlock v) {
        DFSInfo bbInfo = this.info[v.getIndex()];
        if (bbInfo.ancestor == null) {
            return v;
        }
        this.compress(v);
        return bbInfo.rep;
    }

    private void run() {
        SsaBasicBlock w;
        int dfsMax;
        int i;
        SsaBasicBlock root;
        SsaBasicBlock ssaBasicBlock = root = this.postdom ? this.meth.getExitBlock() : this.meth.getEntryBlock();
        if (root != null) {
            this.vertex.add(root);
            this.domInfos[root.getIndex()].idom = root.getIndex();
        }
        DfsWalker walker = new DfsWalker();
        this.meth.forEachBlockDepthFirst(this.postdom, walker);
        for (i = dfsMax = this.vertex.size() - 1; i >= 2; --i) {
            w = this.vertex.get(i);
            DFSInfo wInfo = this.info[w.getIndex()];
            BitSet preds = this.getPreds(w);
            int j = preds.nextSetBit(0);
            while (j >= 0) {
                int predSemidom;
                SsaBasicBlock predBlock = this.blocks.get(j);
                DFSInfo predInfo = this.info[predBlock.getIndex()];
                if (predInfo != null && (predSemidom = this.info[this.eval((SsaBasicBlock)predBlock).getIndex()].semidom) < wInfo.semidom) {
                    wInfo.semidom = predSemidom;
                }
                j = preds.nextSetBit(j + 1);
            }
            this.info[this.vertex.get((int)wInfo.semidom).getIndex()].bucket.add(w);
            wInfo.ancestor = wInfo.parent;
            ArrayList<SsaBasicBlock> wParentBucket = this.info[wInfo.parent.getIndex()].bucket;
            while (!wParentBucket.isEmpty()) {
                int lastItem = wParentBucket.size() - 1;
                SsaBasicBlock last = wParentBucket.remove(lastItem);
                SsaBasicBlock U = this.eval(last);
                if (this.info[U.getIndex()].semidom < this.info[last.getIndex()].semidom) {
                    this.domInfos[last.getIndex()].idom = U.getIndex();
                    continue;
                }
                this.domInfos[last.getIndex()].idom = wInfo.parent.getIndex();
            }
        }
        for (i = 2; i <= dfsMax; ++i) {
            w = this.vertex.get(i);
            if (this.domInfos[w.getIndex()].idom == this.vertex.get(this.info[w.getIndex()].semidom).getIndex()) continue;
            this.domInfos[w.getIndex()].idom = this.domInfos[this.domInfos[w.getIndex()].idom].idom;
        }
    }

    private static final class DFSInfo {
        public int semidom;
        public SsaBasicBlock parent;
        public SsaBasicBlock rep;
        public SsaBasicBlock ancestor;
        public ArrayList<SsaBasicBlock> bucket = new ArrayList();
    }

    private class DfsWalker
    implements SsaBasicBlock.Visitor {
        private int dfsNum = 0;

        private DfsWalker() {
        }

        @Override
        public void visitBlock(SsaBasicBlock v, SsaBasicBlock parent) {
            DFSInfo bbInfo = new DFSInfo();
            bbInfo.semidom = ++this.dfsNum;
            bbInfo.rep = v;
            bbInfo.parent = parent;
            Dominators.this.vertex.add(v);
            ((Dominators)Dominators.this).info[v.getIndex()] = bbInfo;
        }
    }
}

