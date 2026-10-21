/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.Section;
import com.android.dx.util.AnnotatedOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public final class Statistics {
    private final HashMap<String, Data> dataMap = new HashMap(50);

    public void add(Item item) {
        String typeName = item.typeName();
        Data data = this.dataMap.get(typeName);
        if (data == null) {
            this.dataMap.put(typeName, new Data(item, typeName));
        } else {
            data.add(item);
        }
    }

    public void addAll(Section list) {
        Collection<? extends Item> items = list.items();
        for (Item item : items) {
            this.add(item);
        }
    }

    public final void writeAnnotation(AnnotatedOutput out) {
        if (this.dataMap.size() == 0) {
            return;
        }
        out.annotate(0, "\nstatistics:\n");
        TreeMap<String, Data> sortedData = new TreeMap<String, Data>();
        for (Data data : this.dataMap.values()) {
            sortedData.put(data.name, data);
        }
        for (Data data : sortedData.values()) {
            data.writeAnnotation(out);
        }
    }

    public String toHuman() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statistics:\n");
        TreeMap<String, Data> sortedData = new TreeMap<String, Data>();
        for (Data data : this.dataMap.values()) {
            sortedData.put(data.name, data);
        }
        for (Data data : sortedData.values()) {
            sb.append(data.toHuman());
        }
        return sb.toString();
    }

    private static class Data {
        private final String name;
        private int count;
        private int totalSize;
        private int largestSize;
        private int smallestSize;

        public Data(Item item, String name) {
            int size = item.writeSize();
            this.name = name;
            this.count = 1;
            this.totalSize = size;
            this.largestSize = size;
            this.smallestSize = size;
        }

        public void add(Item item) {
            int size = item.writeSize();
            ++this.count;
            this.totalSize += size;
            if (size > this.largestSize) {
                this.largestSize = size;
            }
            if (size < this.smallestSize) {
                this.smallestSize = size;
            }
        }

        public void writeAnnotation(AnnotatedOutput out) {
            out.annotate(this.toHuman());
        }

        public String toHuman() {
            StringBuilder sb = new StringBuilder();
            sb.append("  " + this.name + ": " + this.count + " item" + (this.count == 1 ? "" : "s") + "; " + this.totalSize + " bytes total\n");
            if (this.smallestSize == this.largestSize) {
                sb.append("    " + this.smallestSize + " bytes/item\n");
            } else {
                int average = this.totalSize / this.count;
                sb.append("    " + this.smallestSize + ".." + this.largestSize + " bytes/item; average " + average + "\n");
            }
            return sb.toString();
        }
    }
}

