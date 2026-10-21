/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.CatchBuilder;
import com.android.dx.dex.code.CatchTable;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.LocalList;
import com.android.dx.dex.code.OutputFinisher;
import com.android.dx.dex.code.PositionList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.Type;
import java.util.HashSet;

public final class DalvCode {
    private final int positionInfo;
    private OutputFinisher unprocessedInsns;
    private CatchBuilder unprocessedCatches;
    private CatchTable catches;
    private PositionList positions;
    private LocalList locals;
    private DalvInsnList insns;

    public DalvCode(int positionInfo, OutputFinisher unprocessedInsns, CatchBuilder unprocessedCatches) {
        if (unprocessedInsns == null) {
            throw new NullPointerException("unprocessedInsns == null");
        }
        if (unprocessedCatches == null) {
            throw new NullPointerException("unprocessedCatches == null");
        }
        this.positionInfo = positionInfo;
        this.unprocessedInsns = unprocessedInsns;
        this.unprocessedCatches = unprocessedCatches;
        this.catches = null;
        this.positions = null;
        this.locals = null;
        this.insns = null;
    }

    private void finishProcessingIfNecessary() {
        if (this.insns != null) {
            return;
        }
        this.insns = this.unprocessedInsns.finishProcessingAndGetList();
        this.positions = PositionList.make(this.insns, this.positionInfo);
        this.locals = LocalList.make(this.insns);
        this.catches = this.unprocessedCatches.build();
        this.unprocessedInsns = null;
        this.unprocessedCatches = null;
    }

    public void assignIndices(AssignIndicesCallback callback) {
        this.unprocessedInsns.assignIndices(callback);
    }

    public boolean hasPositions() {
        return this.positionInfo != 1 && this.unprocessedInsns.hasAnyPositionInfo();
    }

    public boolean hasLocals() {
        return this.unprocessedInsns.hasAnyLocalInfo();
    }

    public boolean hasAnyCatches() {
        return this.unprocessedCatches.hasAnyCatches();
    }

    public HashSet<Type> getCatchTypes() {
        return this.unprocessedCatches.getCatchTypes();
    }

    public HashSet<Constant> getInsnConstants() {
        return this.unprocessedInsns.getAllConstants();
    }

    public DalvInsnList getInsns() {
        this.finishProcessingIfNecessary();
        return this.insns;
    }

    public CatchTable getCatches() {
        this.finishProcessingIfNecessary();
        return this.catches;
    }

    public PositionList getPositions() {
        this.finishProcessingIfNecessary();
        return this.positions;
    }

    public LocalList getLocals() {
        this.finishProcessingIfNecessary();
        return this.locals;
    }

    public static interface AssignIndicesCallback {
        public int getIndex(Constant var1);
    }
}

