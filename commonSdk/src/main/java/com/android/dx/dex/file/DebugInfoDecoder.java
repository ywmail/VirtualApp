/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.Leb128;
import com.android.dex.util.ByteArrayByteInput;
import com.android.dex.util.ByteInput;
import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.LocalList;
import com.android.dx.dex.code.PositionList;
import com.android.dx.dex.file.DexFile;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugInfoDecoder {
    private final byte[] encoded;
    private final ArrayList<PositionEntry> positions;
    private final ArrayList<LocalEntry> locals;
    private final int codesize;
    private final LocalEntry[] lastEntryForReg;
    private final Prototype desc;
    private final boolean isStatic;
    private final DexFile file;
    private final int regSize;
    private int line = 1;
    private int address = 0;
    private final int thisStringIdx;

    DebugInfoDecoder(byte[] encoded, int codesize, int regSize, boolean isStatic, CstMethodRef ref, DexFile file) {
        if (encoded == null) {
            throw new NullPointerException("encoded == null");
        }
        this.encoded = encoded;
        this.isStatic = isStatic;
        this.desc = ref.getPrototype();
        this.file = file;
        this.regSize = regSize;
        this.positions = new ArrayList();
        this.locals = new ArrayList();
        this.codesize = codesize;
        this.lastEntryForReg = new LocalEntry[regSize];
        int idx = -1;
        try {
            idx = file.getStringIds().indexOf(new CstString("this"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        this.thisStringIdx = idx;
    }

    public List<PositionEntry> getPositionList() {
        return this.positions;
    }

    public List<LocalEntry> getLocals() {
        return this.locals;
    }

    public void decode() {
        try {
            this.decode0();
        }
        catch (Exception ex) {
            throw ExceptionWithContext.withContext(ex, "...while decoding debug info");
        }
    }

    private int readStringIndex(ByteInput bs) throws IOException {
        int offsetIndex = Leb128.readUnsignedLeb128(bs);
        return offsetIndex - 1;
    }

    private int getParamBase() {
        return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ? 0 : 1);
    }

    private void decode0() throws IOException {
        ByteArrayByteInput bs = new ByteArrayByteInput(this.encoded);
        this.line = Leb128.readUnsignedLeb128(bs);
        int szParams = Leb128.readUnsignedLeb128(bs);
        StdTypeList params = this.desc.getParameterTypes();
        int curReg = this.getParamBase();
        if (szParams != params.size()) {
            throw new RuntimeException("Mismatch between parameters_size and prototype");
        }
        if (!this.isStatic) {
            LocalEntry thisEntry = new LocalEntry(0, true, curReg, this.thisStringIdx, 0, 0);
            this.locals.add(thisEntry);
            this.lastEntryForReg[curReg] = thisEntry;
            ++curReg;
        }
        for (int i = 0; i < szParams; ++i) {
            Type paramType = params.getType(i);
            int nameIdx = this.readStringIndex(bs);
            LocalEntry le = nameIdx == -1 ? new LocalEntry(0, true, curReg, -1, 0, 0) : new LocalEntry(0, true, curReg, nameIdx, 0, 0);
            this.locals.add(le);
            this.lastEntryForReg[curReg] = le;
            curReg += paramType.getCategory();
        }
        block17: while (true) {
            int opcode = bs.readByte() & 0xFF;
            switch (opcode) {
                case 3: {
                    int reg = Leb128.readUnsignedLeb128(bs);
                    int nameIdx = this.readStringIndex(bs);
                    int typeIdx = this.readStringIndex(bs);
                    LocalEntry le = new LocalEntry(this.address, true, reg, nameIdx, typeIdx, 0);
                    this.locals.add(le);
                    this.lastEntryForReg[reg] = le;
                    continue block17;
                }
                case 4: {
                    int reg = Leb128.readUnsignedLeb128(bs);
                    int nameIdx = this.readStringIndex(bs);
                    int typeIdx = this.readStringIndex(bs);
                    int sigIdx = this.readStringIndex(bs);
                    LocalEntry le = new LocalEntry(this.address, true, reg, nameIdx, typeIdx, sigIdx);
                    this.locals.add(le);
                    this.lastEntryForReg[reg] = le;
                    continue block17;
                }
                case 6: {
                    LocalEntry le;
                    int reg = Leb128.readUnsignedLeb128(bs);
                    try {
                        LocalEntry prevle = this.lastEntryForReg[reg];
                        if (prevle.isStart) {
                            throw new RuntimeException("nonsensical RESTART_LOCAL on live register v" + reg);
                        }
                        le = new LocalEntry(this.address, true, reg, prevle.nameIndex, prevle.typeIndex, 0);
                    }
                    catch (NullPointerException ex) {
                        throw new RuntimeException("Encountered RESTART_LOCAL on new v" + reg);
                    }
                    this.locals.add(le);
                    this.lastEntryForReg[reg] = le;
                    continue block17;
                }
                case 5: {
                    LocalEntry le;
                    int reg = Leb128.readUnsignedLeb128(bs);
                    try {
                        LocalEntry prevle = this.lastEntryForReg[reg];
                        if (!prevle.isStart) {
                            throw new RuntimeException("nonsensical END_LOCAL on dead register v" + reg);
                        }
                        le = new LocalEntry(this.address, false, reg, prevle.nameIndex, prevle.typeIndex, prevle.signatureIndex);
                    }
                    catch (NullPointerException ex) {
                        throw new RuntimeException("Encountered END_LOCAL on new v" + reg);
                    }
                    this.locals.add(le);
                    this.lastEntryForReg[reg] = le;
                    continue block17;
                }
                case 0: {
                    return;
                }
                case 1: {
                    this.address += Leb128.readUnsignedLeb128(bs);
                    continue block17;
                }
                case 2: {
                    this.line += Leb128.readSignedLeb128(bs);
                    continue block17;
                }
                case 7: {
                    continue block17;
                }
                case 8: {
                    continue block17;
                }
                case 9: {
                    continue block17;
                }
            }
            if (opcode < 10) {
                throw new RuntimeException("Invalid extended opcode encountered " + opcode);
            }
            int adjopcode = opcode - 10;
            this.address += adjopcode / 15;
            this.line += -4 + adjopcode % 15;
            this.positions.add(new PositionEntry(this.address, this.line));
        }
    }

    public static void validateEncode(byte[] info, DexFile file, CstMethodRef ref, DalvCode code, boolean isStatic) {
        PositionList pl = code.getPositions();
        LocalList ll = code.getLocals();
        DalvInsnList insns = code.getInsns();
        int codeSize = insns.codeSize();
        int countRegisters = insns.getRegistersSize();
        try {
            DebugInfoDecoder.validateEncode0(info, codeSize, countRegisters, isStatic, ref, file, pl, ll);
        }
        catch (RuntimeException ex) {
            System.err.println("instructions:");
            insns.debugPrint(System.err, "  ", true);
            System.err.println("local list:");
            ll.debugPrint(System.err, "  ");
            throw ExceptionWithContext.withContext(ex, "while processing " + ref.toHuman());
        }
    }

    private static void validateEncode0(byte[] info, int codeSize, int countRegisters, boolean isStatic, CstMethodRef ref, DexFile file, PositionList pl, LocalList ll) {
        DebugInfoDecoder decoder = new DebugInfoDecoder(info, codeSize, countRegisters, isStatic, ref, file);
        decoder.decode();
        List<PositionEntry> decodedEntries = decoder.getPositionList();
        if (decodedEntries.size() != pl.size()) {
            throw new RuntimeException("Decoded positions table not same size was " + decodedEntries.size() + " expected " + pl.size());
        }
        for (PositionEntry entry : decodedEntries) {
            boolean found = false;
            for (int i = pl.size() - 1; i >= 0; --i) {
                PositionList.Entry ple = pl.get(i);
                if (entry.line != ple.getPosition().getLine() || entry.address != ple.getAddress()) continue;
                found = true;
                break;
            }
            if (found) continue;
            throw new RuntimeException("Could not match position entry: " + entry.address + ", " + entry.line);
        }
        List<LocalEntry> decodedLocals = decoder.getLocals();
        int thisStringIdx = decoder.thisStringIdx;
        int decodedSz = decodedLocals.size();
        int paramBase = decoder.getParamBase();
        block2: for (int i = 0; i < decodedSz; ++i) {
            LocalEntry entry = decodedLocals.get(i);
            int idx = entry.nameIndex;
            if (idx >= 0 && idx != thisStringIdx) continue;
            for (int j = i + 1; j < decodedSz; ++j) {
                LocalEntry e2 = decodedLocals.get(j);
                if (e2.address != 0) continue block2;
                if (entry.reg != e2.reg || !e2.isStart) continue;
                decodedLocals.set(i, e2);
                decodedLocals.remove(j);
                --decodedSz;
                continue block2;
            }
        }
        int origSz = ll.size();
        int decodeAt = 0;
        boolean problem = false;
        for (int i = 0; i < origSz; ++i) {
            LocalEntry decodedEntry;
            LocalList.Entry origEntry = ll.get(i);
            if (origEntry.getDisposition() == LocalList.Disposition.END_REPLACED) continue;
            do {
                decodedEntry = decodedLocals.get(decodeAt);
            } while (decodedEntry.nameIndex < 0 && ++decodeAt < decodedSz);
            int decodedAddress = decodedEntry.address;
            if (decodedEntry.reg != origEntry.getRegister()) {
                System.err.println("local register mismatch at orig " + i + " / decoded " + decodeAt);
                problem = true;
                break;
            }
            if (decodedEntry.isStart != origEntry.isStart()) {
                System.err.println("local start/end mismatch at orig " + i + " / decoded " + decodeAt);
                problem = true;
                break;
            }
            if (decodedAddress != origEntry.getAddress() && (decodedAddress != 0 || decodedEntry.reg < paramBase)) {
                System.err.println("local address mismatch at orig " + i + " / decoded " + decodeAt);
                problem = true;
                break;
            }
            ++decodeAt;
        }
        if (problem) {
            System.err.println("decoded locals:");
            for (LocalEntry e : decodedLocals) {
                System.err.println("  " + e);
            }
            throw new RuntimeException("local table problem");
        }
    }

    private static class LocalEntry {
        public int address;
        public boolean isStart;
        public int reg;
        public int nameIndex;
        public int typeIndex;
        public int signatureIndex;

        public LocalEntry(int address, boolean isStart, int reg, int nameIndex, int typeIndex, int signatureIndex) {
            this.address = address;
            this.isStart = isStart;
            this.reg = reg;
            this.nameIndex = nameIndex;
            this.typeIndex = typeIndex;
            this.signatureIndex = signatureIndex;
        }

        public String toString() {
            return String.format("[%x %s v%d %04x %04x %04x]", this.address, this.isStart ? "start" : "end", this.reg, this.nameIndex, this.typeIndex, this.signatureIndex);
        }
    }

    private static class PositionEntry {
        public int address;
        public int line;

        public PositionEntry(int address, int line) {
            this.address = address;
            this.line = line;
        }
    }
}

