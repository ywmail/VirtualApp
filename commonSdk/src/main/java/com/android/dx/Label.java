/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx;

import com.android.dx.Code;
import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Label {
    final List<Insn> instructions = new ArrayList<Insn>();
    Code code;
    boolean marked = false;
    List<Label> catchLabels = Collections.emptyList();
    Label primarySuccessor;
    Label alternateSuccessor;
    int id = -1;

    boolean isEmpty() {
        return this.instructions.isEmpty();
    }

    void compact() {
        for (int i = 0; i < this.catchLabels.size(); ++i) {
            while (this.catchLabels.get(i).isEmpty()) {
                this.catchLabels.set(i, this.catchLabels.get((int)i).primarySuccessor);
            }
        }
        while (this.primarySuccessor != null && this.primarySuccessor.isEmpty()) {
            this.primarySuccessor = this.primarySuccessor.primarySuccessor;
        }
        while (this.alternateSuccessor != null && this.alternateSuccessor.isEmpty()) {
            this.alternateSuccessor = this.alternateSuccessor.primarySuccessor;
        }
    }

    BasicBlock toBasicBlock() {
        InsnList result = new InsnList(this.instructions.size());
        for (int i = 0; i < this.instructions.size(); ++i) {
            result.set(i, this.instructions.get(i));
        }
        result.setImmutable();
        int primarySuccessorIndex = -1;
        IntList successors = new IntList();
        for (Label catchLabel : this.catchLabels) {
            successors.add(catchLabel.id);
        }
        if (this.primarySuccessor != null) {
            primarySuccessorIndex = this.primarySuccessor.id;
            successors.add(primarySuccessorIndex);
        }
        if (this.alternateSuccessor != null) {
            successors.add(this.alternateSuccessor.id);
        }
        successors.setImmutable();
        return new BasicBlock(this.id, result, successors, primarySuccessorIndex);
    }
}

