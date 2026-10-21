/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.util;

import com.android.dx.util.FixedSizeList;
import com.android.dx.util.IntList;
import com.android.dx.util.LabeledItem;
import java.util.Arrays;

public class LabeledList
extends FixedSizeList {
    private final IntList labelToIndex;

    public LabeledList(int size) {
        super(size);
        this.labelToIndex = new IntList(size);
    }

    public LabeledList(LabeledList old) {
        super(old.size());
        this.labelToIndex = old.labelToIndex.mutableCopy();
        int sz = old.size();
        for (int i = 0; i < sz; ++i) {
            Object one = old.get0(i);
            if (one == null) continue;
            this.set0(i, one);
        }
    }

    public final int getMaxLabel() {
        int i;
        int sz = this.labelToIndex.size();
        for (i = sz - 1; i >= 0 && this.labelToIndex.get(i) < 0; --i) {
        }
        int newSize = i + 1;
        this.labelToIndex.shrink(newSize);
        return newSize;
    }

    private void removeLabel(int oldLabel) {
        this.labelToIndex.set(oldLabel, -1);
    }

    private void addLabelIndex(int label, int index) {
        int origSz = this.labelToIndex.size();
        for (int i = 0; i <= label - origSz; ++i) {
            this.labelToIndex.add(-1);
        }
        this.labelToIndex.set(label, index);
    }

    public final int indexOfLabel(int label) {
        if (label >= this.labelToIndex.size()) {
            return -1;
        }
        return this.labelToIndex.get(label);
    }

    public final int[] getLabelsInOrder() {
        int sz = this.size();
        int[] result = new int[sz];
        for (int i = 0; i < sz; ++i) {
            LabeledItem li = (LabeledItem)this.get0(i);
            if (li == null) {
                throw new NullPointerException("null at index " + i);
            }
            result[i] = li.getLabel();
        }
        Arrays.sort(result);
        return result;
    }

    @Override
    public void shrinkToFit() {
        super.shrinkToFit();
        this.rebuildLabelToIndex();
    }

    private void rebuildLabelToIndex() {
        int szItems = this.size();
        for (int i = 0; i < szItems; ++i) {
            LabeledItem li = (LabeledItem)this.get0(i);
            if (li == null) continue;
            this.labelToIndex.set(li.getLabel(), i);
        }
    }

    protected void set(int n, LabeledItem item) {
        LabeledItem old = (LabeledItem)this.getOrNull0(n);
        this.set0(n, item);
        if (old != null) {
            this.removeLabel(old.getLabel());
        }
        if (item != null) {
            this.addLabelIndex(item.getLabel(), n);
        }
    }
}

