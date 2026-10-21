/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.dedex;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.dedex.DataReader;
import com.lody.virtual.helper.dedex.Dex;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.File;
import java.io.IOException;

public class Oat {
    public static final String SECTION_RODATA = ".rodata";
    public final long oatPosition;
    public final Header header;
    public final OatDexFile[] oatDexFiles;
    public final Dex[] dexFiles;
    public final File srcFile;

    public Oat(DataReader reader) throws Exception {
        this.oatPosition = reader.position();
        if (this.oatPosition != 4096L) {
            throw new IOException("Strange oat position " + this.oatPosition);
        }
        this.srcFile = reader.getFile();
        this.header = new Header(reader);
        this.oatDexFiles = new OatDexFile[this.header.dex_file_count_];
        this.dexFiles = new Dex[this.header.dex_file_count_];
        for (int i = 0; i < this.oatDexFiles.length; ++i) {
            Dex dex;
            OatDexFile odf;
            this.oatDexFiles[i] = odf = new OatDexFile(reader, this.header.artVersion);
            long curOatPos = reader.position();
            if (odf.dex_file_pointer_ != null) {
                DataReader r = new DataReader(odf.dex_file_pointer_);
                reader.addAssociatedReader(r);
                r.seek(odf.dex_file_offset_);
                dex = new Dex(r);
            } else {
                reader.seek(this.oatPosition + (long)odf.dex_file_offset_);
                dex = new Dex(reader);
            }
            this.dexFiles[i] = dex;
            if (this.header.artVersion < Version.N_70.oat) {
                int num_methods_offsets_ = dex.header.class_defs_size_;
                reader.seek(curOatPos + (long)(4 * num_methods_offsets_));
                if (reader.previewInt() <= 255) continue;
                reader.readInt();
                continue;
            }
            reader.seek(curOatPos);
        }
    }

    public int getArtVersion() {
        return this.header.artVersion;
    }

    public static class OatDexFile {
        public final int dex_file_location_size_;
        public final byte[] dex_file_location_data_;
        final int dex_file_location_checksum_;
        final int dex_file_offset_;
        File dex_file_pointer_;
        int class_offsets_offset_;
        int lookup_table_offset_;

        public OatDexFile(DataReader r, int version) throws IOException {
            this.dex_file_location_size_ = r.readInt();
            this.dex_file_location_data_ = new byte[this.dex_file_location_size_];
            r.readBytes(this.dex_file_location_data_);
            this.dex_file_location_checksum_ = r.readInt();
            this.dex_file_offset_ = r.readInt();
            File vdex = FileUtils.changeExt(r.getFile(), "vdex");
            if (vdex.exists()) {
                this.dex_file_pointer_ = vdex;
            } else if (this.dex_file_offset_ == 28) {
                throw new IOException("dex_file_offset_=" + this.dex_file_offset_ + ", does " + vdex.getName() + " miss?");
            }
            if (version >= Version.N_70.oat) {
                this.class_offsets_offset_ = r.readInt();
                this.lookup_table_offset_ = r.readInt();
            }
        }

        public String getLocation() {
            return new String(this.dex_file_location_data_);
        }
    }

    public static class Header {
        final char[] magic_ = new char[4];
        final char[] version_ = new char[4];
        final int adler32_checksum_;
        final int instruction_set_;
        final int instruction_set_features_;
        final int dex_file_count_;
        final int executable_offset_;
        final int interpreter_to_interpreter_bridge_offset_;
        final int interpreter_to_compiled_code_bridge_offset_;
        final int jni_dlsym_lookup_offset_;
        int portable_imt_conflict_trampoline_offset_;
        int portable_resolution_trampoline_offset_;
        int portable_to_interpreter_bridge_offset_;
        final int quick_generic_jni_trampoline_offset_;
        final int quick_imt_conflict_trampoline_offset_;
        final int quick_resolution_trampoline_offset_;
        final int quick_to_interpreter_bridge_offset_;
        final int image_patch_delta_;
        final int image_file_location_oat_checksum_;
        final int image_file_location_oat_data_begin_;
        final int key_value_store_size_;
        final char[] key_value_store_;
        int artVersion;

        public Header(DataReader r) throws IOException {
            r.readBytes(this.magic_);
            if (this.magic_[0] != 'o' || this.magic_[1] != 'a' || this.magic_[2] != 't') {
                throw new IOException(String.format("Invalid art magic %c%c%c", Character.valueOf(this.magic_[0]), Character.valueOf(this.magic_[1]), Character.valueOf(this.magic_[2])));
            }
            r.readBytes(this.version_);
            this.artVersion = DataReader.toInt(new String(this.version_));
            this.adler32_checksum_ = r.readInt();
            this.instruction_set_ = r.readInt();
            this.instruction_set_features_ = r.readInt();
            this.dex_file_count_ = r.readInt();
            this.executable_offset_ = r.readInt();
            this.interpreter_to_interpreter_bridge_offset_ = r.readInt();
            this.interpreter_to_compiled_code_bridge_offset_ = r.readInt();
            this.jni_dlsym_lookup_offset_ = r.readInt();
            if (this.artVersion < 52) {
                this.portable_imt_conflict_trampoline_offset_ = r.readInt();
                this.portable_resolution_trampoline_offset_ = r.readInt();
                this.portable_to_interpreter_bridge_offset_ = r.readInt();
            }
            this.quick_generic_jni_trampoline_offset_ = r.readInt();
            this.quick_imt_conflict_trampoline_offset_ = r.readInt();
            this.quick_resolution_trampoline_offset_ = r.readInt();
            this.quick_to_interpreter_bridge_offset_ = r.readInt();
            this.image_patch_delta_ = r.readInt();
            this.image_file_location_oat_checksum_ = r.readInt();
            this.image_file_location_oat_data_begin_ = r.readInt();
            this.key_value_store_size_ = r.readInt();
            this.key_value_store_ = new char[this.key_value_store_size_];
            r.readBytes(this.key_value_store_);
        }
    }

    public static final class InstructionSet {
        public static final int kNone = 0;
        public static final int kArm = 1;
        public static final int kArm64 = 2;
        public static final int kThumb2 = 3;
        public static final int kX86 = 4;
        public static final int kX86_64 = 5;
        public static final int kMips = 6;
        public static final int kMips64 = 7;
    }

    public static enum Version {
        L_50(21, 39),
        L_MR1_51(22, 45),
        M_60(23, 64),
        N_70(24, 79),
        N_MR1_71(25, 88),
        O_80(26, 124),
        O_MR1_81(27, 131);

        public final int api;
        public final int oat;

        private Version(int api, int oat) {
            this.api = api;
            this.oat = oat;
        }
    }
}

