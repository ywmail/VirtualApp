/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.helper.dedex;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.dedex.DataReader;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.zip.Adler32;

public class Dex {
    private final DataReader mReader;
    public final int dexPosition;
    public final int dataEnd;
    public final Header header;

    public Dex(DataReader r) throws IOException {
        this.dexPosition = r.position();
        this.mReader = r;
        this.header = new Header(r);
        this.dataEnd = this.header.isCompactDex ? this.header.data_off_ + this.header.data_size_ : this.header.file_size_;
    }

    private void calcSignature(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(bytes, 32, bytes.length - 32);
            digest.digest(bytes, 12, 20);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcChecksum(byte[] bytes) {
        Adler32 a32 = new Adler32();
        a32.update(bytes, 12, bytes.length - 12);
        int checksum = (int)a32.getValue();
        if (this.header.checksum_ != checksum) {
            bytes[8] = (byte)checksum;
            bytes[9] = (byte)(checksum >> 8);
            bytes[10] = (byte)(checksum >> 16);
            bytes[11] = (byte)(checksum >> 24);
        }
    }

    public byte[] getFixedBytes() {
        byte[] bytes = this.getBytes();
        this.calcSignature(bytes);
        this.calcChecksum(bytes);
        return bytes;
    }

    public byte[] getBytes() {
        byte[] dexBytes = new byte[this.dataEnd];
        this.mReader.position(this.dexPosition);
        this.mReader.readBytes(dexBytes);
        return dexBytes;
    }

    public void writeTo(File outputFile) throws IOException {
        FileUtils.writeToFile(this.getFixedBytes(), outputFile);
    }

    public static class Header {
        static final String MAGIC_DEX = "dex";
        static final String MAGIC_COMPACT_DEX = "cdex";
        final char[] magic_ = new char[4];
        final char[] version_ = new char[4];
        final int checksum_;
        final byte[] signature_ = new byte[20];
        public final int file_size_;
        public final int header_size_;
        final int endian_tag_;
        final int link_size_;
        final int link_off_;
        final int map_off_;
        final int string_ids_size_;
        final int string_ids_off_;
        final int type_ids_size_;
        final int type_ids_off_;
        final int proto_ids_size_;
        final int proto_ids_off_;
        final int field_ids_size_;
        final int field_ids_off_;
        final int method_ids_size_;
        final int method_ids_off_;
        final int class_defs_size_;
        final int class_defs_off_;
        final int data_size_;
        public final int data_off_;
        final String magic;
        final String version;
        final boolean isCompactDex;

        public Header(DataReader r) throws IOException {
            r.readBytes(this.magic_);
            this.magic = new String(this.magic_).trim();
            this.isCompactDex = this.magic.equals("cdex");
            if (!this.magic.equals("dex") && !this.isCompactDex) {
                throw new IOException(String.format("Invalid dex magic '%s'", this.magic));
            }
            r.readBytes(this.version_);
            this.version = new String(this.version_).trim();
            if (!this.isCompactDex && this.version.compareTo("035") < 0) {
                throw new IOException(String.format("Invalid dex version '%s'", this.version));
            }
            this.checksum_ = r.readInt();
            r.readBytes(this.signature_);
            this.file_size_ = r.readInt();
            this.header_size_ = r.readInt();
            this.endian_tag_ = r.readInt();
            this.link_size_ = r.readInt();
            this.link_off_ = r.readInt();
            this.map_off_ = r.readInt();
            this.string_ids_size_ = r.readInt();
            this.string_ids_off_ = r.readInt();
            this.type_ids_size_ = r.readInt();
            this.type_ids_off_ = r.readInt();
            this.proto_ids_size_ = r.readInt();
            this.proto_ids_off_ = r.readInt();
            this.field_ids_size_ = r.readInt();
            this.field_ids_off_ = r.readInt();
            this.method_ids_size_ = r.readInt();
            this.method_ids_off_ = r.readInt();
            this.class_defs_size_ = r.readInt();
            this.class_defs_off_ = r.readInt();
            this.data_size_ = r.readInt();
            this.data_off_ = r.readInt();
        }
    }
}

