/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.dedex;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.dedex.DataReader;
import com.lody.virtual.helper.dedex.Dex;
import java.io.IOException;

public class Vdex {
    private static final int VERSION_OREO_006 = 6;
    private static final int VERSION_OREO_MR1_010 = 10;
    private static final int VERSION_P_018 = 18;
    public final Header header;
    public final QuickenDex[] dexFiles;
    public final int[] quickeningTableOffsets;
    public final int dexBegin;

    public Vdex(DataReader r) throws Exception {
        this.header = new Header(r);
        this.dexBegin = r.position();
        r.position(this.dexBegin);
        this.quickeningTableOffsets = this.header.versionNears(18) ? new int[this.header.number_of_dex_files_] : null;
        this.dexFiles = new QuickenDex[this.header.number_of_dex_files_];
        for (int i = 0; i < this.header.number_of_dex_files_; ++i) {
            QuickenDex dex;
            if (this.quickeningTableOffsets != null) {
                this.quickeningTableOffsets[i] = r.readInt();
            }
            this.dexFiles[i] = dex = new QuickenDex(r);
            r.position(dex.dexPosition + dex.header.file_size_);
        }
    }

    public static class QuickenDex
    extends Dex {
        QuickenDex(DataReader r) throws IOException {
            super(r);
        }
    }

    public static class Header {
        final char[] magic_ = new char[4];
        final char[] version_ = new char[4];
        public final int number_of_dex_files_;
        final int dex_size_;
        final int dex_shared_data_size_;
        final int verifier_deps_size_;
        final int quickening_info_size_;
        final int[] vdexCheckSums;
        public final int version;

        public Header(DataReader r) throws IOException {
            r.readBytes(this.magic_);
            String magic = new String(this.magic_);
            if (!"vdex".equals(magic)) {
                throw new IOException("Invalid dex magic '" + magic + "'");
            }
            r.readBytes(this.version_);
            this.version = DataReader.toInt(new String(this.version_));
            this.number_of_dex_files_ = r.readInt();
            this.dex_size_ = r.readInt();
            this.dex_shared_data_size_ = this.versionNears(18) ? r.readInt() : 0;
            this.verifier_deps_size_ = r.readInt();
            this.quickening_info_size_ = r.readInt();
            this.vdexCheckSums = new int[this.number_of_dex_files_];
            for (int i = 0; i < this.vdexCheckSums.length; ++i) {
                this.vdexCheckSums[i] = r.readInt();
            }
        }

        public boolean versionNears(int version) {
            return Math.abs(this.version - version) <= 1;
        }
    }
}

