/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.DexIndexOverflowException;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.MemberIdItem;
import com.android.dx.dex.file.MethodIdsSection;
import com.android.dx.dex.file.UniformItemSection;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MemberIdsSection
extends UniformItemSection {
    public MemberIdsSection(String name, DexFile file) {
        super(name, file, 4);
    }

    @Override
    protected void orderItems() {
        int idx = 0;
        if (this.items().size() > 65536) {
            throw new DexIndexOverflowException(this.getTooManyMembersMessage());
        }
        for (Item item : this.items()) {
            ((MemberIdItem)item).setIndex(idx);
            ++idx;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String getTooManyMembersMessage() {
        TreeMap<String, AtomicInteger> membersByPackage = new TreeMap<String, AtomicInteger>();
        for (Item item : this.items()) {
            String packageName = ((MemberIdItem)item).getDefiningClass().getPackageName();
            AtomicInteger count = (AtomicInteger)membersByPackage.get(packageName);
            if (count == null) {
                count = new AtomicInteger();
                membersByPackage.put(packageName, count);
            }
            count.incrementAndGet();
        }
        try (Formatter formatter = new Formatter();){
            String string = this instanceof MethodIdsSection ? "method" : "field";
            formatter.format("Too many %1$s references to fit in one dex file: %2$d; max is %3$d.%nYou may try using multi-dex. If multi-dex is enabled then the list of classes for the main dex list is too large.%nReferences by package:", string, this.items().size(), 65536);
            for (Map.Entry entry : membersByPackage.entrySet()) {
                formatter.format("%n%6d %s", ((AtomicInteger)entry.getValue()).get(), entry.getKey());
            }
            String string2 = formatter.toString();
            return string2;
        }
    }
}

