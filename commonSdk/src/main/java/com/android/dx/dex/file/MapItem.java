/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.Item;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.MixedItemSection;
import com.android.dx.dex.file.OffsettedItem;
import com.android.dx.dex.file.Section;
import com.android.dx.dex.file.UniformListItem;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;
import java.util.ArrayList;

public final class MapItem
extends OffsettedItem {
    private static final int ALIGNMENT = 4;
    private static final int WRITE_SIZE = 12;
    private final ItemType type;
    private final Section section;
    private final Item firstItem;
    private final Item lastItem;
    private final int itemCount;

    public static void addMap(Section[] sections, MixedItemSection mapSection) {
        if (sections == null) {
            throw new NullPointerException("sections == null");
        }
        if (mapSection.items().size() != 0) {
            throw new IllegalArgumentException("mapSection.items().size() != 0");
        }
        ArrayList<MapItem> items = new ArrayList<MapItem>(50);
        for (Section section : sections) {
            ItemType currentType = null;
            Item firstItem = null;
            Item lastItem = null;
            int count = 0;
            for (Item item : section.items()) {
                ItemType type = item.itemType();
                if (type != currentType) {
                    if (count != 0) {
                        items.add(new MapItem(currentType, section, firstItem, lastItem, count));
                    }
                    currentType = type;
                    firstItem = item;
                    count = 0;
                }
                lastItem = item;
                ++count;
            }
            if (count != 0) {
                items.add(new MapItem(currentType, section, firstItem, lastItem, count));
                continue;
            }
            if (section != mapSection) continue;
            items.add(new MapItem(mapSection));
        }
        mapSection.add(new UniformListItem(ItemType.TYPE_MAP_LIST, items));
    }

    private MapItem(ItemType type, Section section, Item firstItem, Item lastItem, int itemCount) {
        super(4, 12);
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (section == null) {
            throw new NullPointerException("section == null");
        }
        if (firstItem == null) {
            throw new NullPointerException("firstItem == null");
        }
        if (lastItem == null) {
            throw new NullPointerException("lastItem == null");
        }
        if (itemCount <= 0) {
            throw new IllegalArgumentException("itemCount <= 0");
        }
        this.type = type;
        this.section = section;
        this.firstItem = firstItem;
        this.lastItem = lastItem;
        this.itemCount = itemCount;
    }

    private MapItem(Section section) {
        super(4, 12);
        if (section == null) {
            throw new NullPointerException("section == null");
        }
        this.type = ItemType.TYPE_MAP_LIST;
        this.section = section;
        this.firstItem = null;
        this.lastItem = null;
        this.itemCount = 1;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_MAP_ITEM;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(this.section.toString());
        sb.append(' ');
        sb.append(this.type.toHuman());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void addContents(DexFile file) {
    }

    @Override
    public final String toHuman() {
        return this.toString();
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        int value = this.type.getMapValue();
        int offset = this.firstItem == null ? this.section.getFileOffset() : this.section.getAbsoluteItemOffset(this.firstItem);
        if (out.annotates()) {
            out.annotate(0, this.offsetString() + ' ' + this.type.getTypeName() + " map");
            out.annotate(2, "  type:   " + Hex.u2(value) + " // " + this.type.toString());
            out.annotate(2, "  unused: 0");
            out.annotate(4, "  size:   " + Hex.u4(this.itemCount));
            out.annotate(4, "  offset: " + Hex.u4(offset));
        }
        out.writeShort(value);
        out.writeShort(0);
        out.writeInt(this.itemCount);
        out.writeInt(offset);
    }
}

