/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.IndexedItem;
import com.android.dx.dex.file.ItemType;
import com.android.dx.dex.file.Section;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public final class HeaderItem
extends IndexedItem {
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_HEADER_ITEM;
    }

    @Override
    public int writeSize() {
        return 112;
    }

    @Override
    public void addContents(DexFile file) {
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        int mapOff = file.getMap().getFileOffset();
        Section firstDataSection = file.getFirstDataSection();
        Section lastDataSection = file.getLastDataSection();
        int dataOff = firstDataSection.getFileOffset();
        int dataSize = lastDataSection.getFileOffset() + lastDataSection.writeSize() - dataOff;
        String magic = file.getDexOptions().getMagic();
        if (out.annotates()) {
            out.annotate(8, "magic: " + new CstString(magic).toQuoted());
            out.annotate(4, "checksum");
            out.annotate(20, "signature");
            out.annotate(4, "file_size:       " + Hex.u4(file.getFileSize()));
            out.annotate(4, "header_size:     " + Hex.u4(112));
            out.annotate(4, "endian_tag:      " + Hex.u4(305419896));
            out.annotate(4, "link_size:       0");
            out.annotate(4, "link_off:        0");
            out.annotate(4, "map_off:         " + Hex.u4(mapOff));
        }
        for (int i = 0; i < 8; ++i) {
            out.writeByte(magic.charAt(i));
        }
        out.writeZeroes(24);
        out.writeInt(file.getFileSize());
        out.writeInt(112);
        out.writeInt(305419896);
        out.writeZeroes(8);
        out.writeInt(mapOff);
        file.getStringIds().writeHeaderPart(out);
        file.getTypeIds().writeHeaderPart(out);
        file.getProtoIds().writeHeaderPart(out);
        file.getFieldIds().writeHeaderPart(out);
        file.getMethodIds().writeHeaderPart(out);
        file.getClassDefs().writeHeaderPart(out);
        if (out.annotates()) {
            out.annotate(4, "data_size:       " + Hex.u4(dataSize));
            out.annotate(4, "data_off:        " + Hex.u4(dataOff));
        }
        out.writeInt(dataSize);
        out.writeInt(dataOff);
    }
}

