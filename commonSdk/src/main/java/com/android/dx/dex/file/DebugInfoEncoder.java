/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.code.LocalList;
import com.android.dx.dex.code.PositionList;
import com.android.dx.dex.file.DexFile;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;

public final class DebugInfoEncoder {
    private static final boolean DEBUG = false;
    private final PositionList positions;
    private final LocalList locals;
    private final ByteArrayAnnotatedOutput output;
    private final DexFile file;
    private final int codeSize;
    private final int regSize;
    private final Prototype desc;
    private final boolean isStatic;
    private int address = 0;
    private int line = 1;
    private AnnotatedOutput annotateTo;
    private PrintWriter debugPrint;
    private String prefix;
    private boolean shouldConsume;
    private final LocalList.Entry[] lastEntryForReg;

    public DebugInfoEncoder(PositionList positions, LocalList locals, DexFile file, int codeSize, int regSize, boolean isStatic, CstMethodRef ref) {
        this.positions = positions;
        this.locals = locals;
        this.file = file;
        this.desc = ref.getPrototype();
        this.isStatic = isStatic;
        this.codeSize = codeSize;
        this.regSize = regSize;
        this.output = new ByteArrayAnnotatedOutput();
        this.lastEntryForReg = new LocalList.Entry[regSize];
    }

    private void annotate(int length, String message) {
        if (this.prefix != null) {
            message = this.prefix + message;
        }
        if (this.annotateTo != null) {
            this.annotateTo.annotate(this.shouldConsume ? length : 0, message);
        }
        if (this.debugPrint != null) {
            this.debugPrint.println(message);
        }
    }

    public byte[] convert() {
        try {
            byte[] ret = this.convert0();
            return ret;
        }
        catch (IOException ex) {
            throw ExceptionWithContext.withContext(ex, "...while encoding debug info");
        }
    }

    public byte[] convertAndAnnotate(String prefix, PrintWriter debugPrint, AnnotatedOutput out, boolean consume) {
        this.prefix = prefix;
        this.debugPrint = debugPrint;
        this.annotateTo = out;
        this.shouldConsume = consume;
        byte[] result = this.convert();
        return result;
    }

    private byte[] convert0() throws IOException {
        ArrayList<PositionList.Entry> sortedPositions = this.buildSortedPositions();
        ArrayList<LocalList.Entry> methodArgs = this.extractMethodArguments();
        this.emitHeader(sortedPositions, methodArgs);
        this.output.writeByte(7);
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(1, String.format("%04x: prologue end", this.address));
        }
        int positionsSz = sortedPositions.size();
        int localsSz = this.locals.size();
        int curPositionIdx = 0;
        int curLocalIdx = 0;
        while (true) {
            int next;
            curLocalIdx = this.emitLocalsAtAddress(curLocalIdx);
            curPositionIdx = this.emitPositionsAtAddress(curPositionIdx, sortedPositions);
            int nextAddrL = Integer.MAX_VALUE;
            int nextAddrP = Integer.MAX_VALUE;
            if (curLocalIdx < localsSz) {
                nextAddrL = this.locals.get(curLocalIdx).getAddress();
            }
            if (curPositionIdx < positionsSz) {
                nextAddrP = sortedPositions.get(curPositionIdx).getAddress();
            }
            if ((next = Math.min(nextAddrP, nextAddrL)) == Integer.MAX_VALUE || next == this.codeSize && nextAddrL == Integer.MAX_VALUE && nextAddrP == Integer.MAX_VALUE) break;
            if (next == nextAddrP) {
                this.emitPosition(sortedPositions.get(curPositionIdx++));
                continue;
            }
            this.emitAdvancePc(next - this.address);
        }
        this.emitEndSequence();
        return this.output.toByteArray();
    }

    private int emitLocalsAtAddress(int curLocalIdx) throws IOException {
        int sz = this.locals.size();
        while (curLocalIdx < sz && this.locals.get(curLocalIdx).getAddress() == this.address) {
            int reg;
            LocalList.Entry prevEntry;
            LocalList.Entry entry;
            if ((entry = this.locals.get(curLocalIdx++)) == (prevEntry = this.lastEntryForReg[reg = entry.getRegister()])) continue;
            this.lastEntryForReg[reg] = entry;
            if (entry.isStart()) {
                if (prevEntry != null && entry.matches(prevEntry)) {
                    if (prevEntry.isStart()) {
                        throw new RuntimeException("shouldn't happen");
                    }
                    this.emitLocalRestart(entry);
                    continue;
                }
                this.emitLocalStart(entry);
                continue;
            }
            if (entry.getDisposition() == LocalList.Disposition.END_REPLACED) continue;
            this.emitLocalEnd(entry);
        }
        return curLocalIdx;
    }

    private int emitPositionsAtAddress(int curPositionIdx, ArrayList<PositionList.Entry> sortedPositions) throws IOException {
        int positionsSz = sortedPositions.size();
        while (curPositionIdx < positionsSz && sortedPositions.get(curPositionIdx).getAddress() == this.address) {
            this.emitPosition(sortedPositions.get(curPositionIdx++));
        }
        return curPositionIdx;
    }

    private void emitHeader(ArrayList<PositionList.Entry> sortedPositions, ArrayList<LocalList.Entry> methodArgs) throws IOException {
        boolean annotate = this.annotateTo != null || this.debugPrint != null;
        int mark = this.output.getCursor();
        if (sortedPositions.size() > 0) {
            PositionList.Entry entry = sortedPositions.get(0);
            this.line = entry.getPosition().getLine();
        }
        this.output.writeUleb128(this.line);
        if (annotate) {
            this.annotate(this.output.getCursor() - mark, "line_start: " + this.line);
        }
        int curParam = this.getParamBase();
        StdTypeList paramTypes = this.desc.getParameterTypes();
        int szParamTypes = paramTypes.size();
        if (!this.isStatic) {
            for (LocalList.Entry arg : methodArgs) {
                if (curParam != arg.getRegister()) continue;
                this.lastEntryForReg[curParam] = arg;
                break;
            }
            ++curParam;
        }
        mark = this.output.getCursor();
        this.output.writeUleb128(szParamTypes);
        if (annotate) {
            this.annotate(this.output.getCursor() - mark, String.format("parameters_size: %04x", szParamTypes));
        }
        for (int i = 0; i < szParamTypes; ++i) {
            Type pt = paramTypes.get(i);
            LocalList.Entry found = null;
            mark = this.output.getCursor();
            for (LocalList.Entry arg : methodArgs) {
                if (curParam != arg.getRegister()) continue;
                found = arg;
                if (arg.getSignature() != null) {
                    this.emitStringIndex(null);
                } else {
                    this.emitStringIndex(arg.getName());
                }
                this.lastEntryForReg[curParam] = arg;
                break;
            }
            if (found == null) {
                this.emitStringIndex(null);
            }
            if (annotate) {
                String parameterName = found == null || found.getSignature() != null ? "<unnamed>" : found.getName().toHuman();
                this.annotate(this.output.getCursor() - mark, "parameter " + parameterName + " " + "v" + curParam);
            }
            curParam += pt.getCategory();
        }
        for (LocalList.Entry arg : this.lastEntryForReg) {
            CstString signature;
            if (arg == null || (signature = arg.getSignature()) == null) continue;
            this.emitLocalStartExtended(arg);
        }
    }

    private ArrayList<PositionList.Entry> buildSortedPositions() {
        int sz = this.positions == null ? 0 : this.positions.size();
        ArrayList<PositionList.Entry> result = new ArrayList<PositionList.Entry>(sz);
        for (int i = 0; i < sz; ++i) {
            result.add(this.positions.get(i));
        }
        Collections.sort(result, new Comparator<PositionList.Entry>(){

            @Override
            public int compare(PositionList.Entry a, PositionList.Entry b) {
                return a.getAddress() - b.getAddress();
            }

            @Override
            public boolean equals(Object obj) {
                return obj == this;
            }
        });
        return result;
    }

    private int getParamBase() {
        return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ? 0 : 1);
    }

    private ArrayList<LocalList.Entry> extractMethodArguments() {
        ArrayList<LocalList.Entry> result = new ArrayList<LocalList.Entry>(this.desc.getParameterTypes().size());
        int argBase = this.getParamBase();
        BitSet seen = new BitSet(this.regSize - argBase);
        int sz = this.locals.size();
        for (int i = 0; i < sz; ++i) {
            LocalList.Entry e = this.locals.get(i);
            int reg = e.getRegister();
            if (reg < argBase || seen.get(reg - argBase)) continue;
            seen.set(reg - argBase);
            result.add(e);
        }
        Collections.sort(result, new Comparator<LocalList.Entry>(){

            @Override
            public int compare(LocalList.Entry a, LocalList.Entry b) {
                return a.getRegister() - b.getRegister();
            }

            @Override
            public boolean equals(Object obj) {
                return obj == this;
            }
        });
        return result;
    }

    private String entryAnnotationString(LocalList.Entry e) {
        StringBuilder sb = new StringBuilder();
        sb.append("v");
        sb.append(e.getRegister());
        sb.append(' ');
        CstString name = e.getName();
        if (name == null) {
            sb.append("null");
        } else {
            sb.append(name.toHuman());
        }
        sb.append(' ');
        CstType type = e.getType();
        if (type == null) {
            sb.append("null");
        } else {
            sb.append(type.toHuman());
        }
        CstString signature = e.getSignature();
        if (signature != null) {
            sb.append(' ');
            sb.append(signature.toHuman());
        }
        return sb.toString();
    }

    private void emitLocalRestart(LocalList.Entry entry) throws IOException {
        int mark = this.output.getCursor();
        this.output.writeByte(6);
        this.emitUnsignedLeb128(entry.getRegister());
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: +local restart %s", this.address, this.entryAnnotationString(entry)));
        }
    }

    private void emitStringIndex(CstString string) throws IOException {
        if (string == null || this.file == null) {
            this.output.writeUleb128(0);
        } else {
            this.output.writeUleb128(1 + this.file.getStringIds().indexOf(string));
        }
    }

    private void emitTypeIndex(CstType type) throws IOException {
        if (type == null || this.file == null) {
            this.output.writeUleb128(0);
        } else {
            this.output.writeUleb128(1 + this.file.getTypeIds().indexOf(type));
        }
    }

    private void emitLocalStart(LocalList.Entry entry) throws IOException {
        if (entry.getSignature() != null) {
            this.emitLocalStartExtended(entry);
            return;
        }
        int mark = this.output.getCursor();
        this.output.writeByte(3);
        this.emitUnsignedLeb128(entry.getRegister());
        this.emitStringIndex(entry.getName());
        this.emitTypeIndex(entry.getType());
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: +local %s", this.address, this.entryAnnotationString(entry)));
        }
    }

    private void emitLocalStartExtended(LocalList.Entry entry) throws IOException {
        int mark = this.output.getCursor();
        this.output.writeByte(4);
        this.emitUnsignedLeb128(entry.getRegister());
        this.emitStringIndex(entry.getName());
        this.emitTypeIndex(entry.getType());
        this.emitStringIndex(entry.getSignature());
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: +localx %s", this.address, this.entryAnnotationString(entry)));
        }
    }

    private void emitLocalEnd(LocalList.Entry entry) throws IOException {
        int mark = this.output.getCursor();
        this.output.writeByte(5);
        this.output.writeUleb128(entry.getRegister());
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: -local %s", this.address, this.entryAnnotationString(entry)));
        }
    }

    private void emitPosition(PositionList.Entry entry) throws IOException {
        int opcode;
        SourcePosition pos = entry.getPosition();
        int newLine = pos.getLine();
        int newAddress = entry.getAddress();
        int deltaLines = newLine - this.line;
        int deltaAddress = newAddress - this.address;
        if (deltaAddress < 0) {
            throw new RuntimeException("Position entries must be in ascending address order");
        }
        if (deltaLines < -4 || deltaLines > 10) {
            this.emitAdvanceLine(deltaLines);
            deltaLines = 0;
        }
        if (((opcode = DebugInfoEncoder.computeOpcode(deltaLines, deltaAddress)) & 0xFFFFFF00) > 0) {
            this.emitAdvancePc(deltaAddress);
            deltaAddress = 0;
            opcode = DebugInfoEncoder.computeOpcode(deltaLines, deltaAddress);
            if ((opcode & 0xFFFFFF00) > 0) {
                this.emitAdvanceLine(deltaLines);
                deltaLines = 0;
                opcode = DebugInfoEncoder.computeOpcode(deltaLines, deltaAddress);
            }
        }
        this.output.writeByte(opcode);
        this.line += deltaLines;
        this.address += deltaAddress;
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(1, String.format("%04x: line %d", this.address, this.line));
        }
    }

    private static int computeOpcode(int deltaLines, int deltaAddress) {
        if (deltaLines < -4 || deltaLines > 10) {
            throw new RuntimeException("Parameter out of range");
        }
        return deltaLines - -4 + 15 * deltaAddress + 10;
    }

    private void emitAdvanceLine(int deltaLines) throws IOException {
        int mark = this.output.getCursor();
        this.output.writeByte(2);
        this.output.writeSleb128(deltaLines);
        this.line += deltaLines;
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("line = %d", this.line));
        }
    }

    private void emitAdvancePc(int deltaAddress) throws IOException {
        int mark = this.output.getCursor();
        this.output.writeByte(1);
        this.output.writeUleb128(deltaAddress);
        this.address += deltaAddress;
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: advance pc", this.address));
        }
    }

    private void emitUnsignedLeb128(int n) throws IOException {
        if (n < 0) {
            throw new RuntimeException("Signed value where unsigned required: " + n);
        }
        this.output.writeUleb128(n);
    }

    private void emitEndSequence() {
        this.output.writeByte(0);
        if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(1, "end sequence");
        }
    }
}

