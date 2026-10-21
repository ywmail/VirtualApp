/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io.instructions;

import com.android.dex.DexException;
import com.android.dx.io.IndexType;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.io.instructions.CodeInput;
import com.android.dx.io.instructions.CodeOutput;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.FillArrayDataPayloadDecodedInstruction;
import com.android.dx.io.instructions.FiveRegisterDecodedInstruction;
import com.android.dx.io.instructions.FourRegisterDecodedInstruction;
import com.android.dx.io.instructions.InvokePolymorphicDecodedInstruction;
import com.android.dx.io.instructions.InvokePolymorphicRangeDecodedInstruction;
import com.android.dx.io.instructions.OneRegisterDecodedInstruction;
import com.android.dx.io.instructions.PackedSwitchPayloadDecodedInstruction;
import com.android.dx.io.instructions.RegisterRangeDecodedInstruction;
import com.android.dx.io.instructions.SparseSwitchPayloadDecodedInstruction;
import com.android.dx.io.instructions.ThreeRegisterDecodedInstruction;
import com.android.dx.io.instructions.TwoRegisterDecodedInstruction;
import com.android.dx.io.instructions.ZeroRegisterDecodedInstruction;
import com.android.dx.util.Hex;
import java.io.EOFException;
import java.util.Arrays;

public enum InstructionCodec {
    FORMAT_00X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, opcodeUnit, 0, null, 0, 0L);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }
    }
    ,
    FORMAT_10X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int literal = InstructionCodec.byte1(opcodeUnit);
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null, 0, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }
    }
    ,
    FORMAT_12X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int b = InstructionCodec.nibble3(opcodeUnit);
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getB())));
        }
    }
    ,
    FORMAT_11N{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int literal = InstructionCodec.nibble3(opcodeUnit) << 28 >> 28;
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getLiteralNibble())));
        }
    }
    ,
    FORMAT_11X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()));
        }
    }
    ,
    FORMAT_10T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            byte target = (byte)InstructionCodec.byte1(opcodeUnit);
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null, baseAddress + target, 0L);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTargetByte(out.cursor());
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), relativeTarget));
        }
    }
    ,
    FORMAT_20T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int literal = InstructionCodec.byte1(opcodeUnit);
            short target = (short)in.read();
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null, baseAddress + target, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(insn.getOpcodeUnit(), relativeTarget);
        }
    }
    ,
    FORMAT_20BC{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int literal = InstructionCodec.byte1(opcodeUnit);
            int index = in.read();
            return new ZeroRegisterDecodedInstruction(this, opcode, index, IndexType.VARIES, 0, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getLiteralByte()), insn.getIndexUnit());
        }
    }
    ,
    FORMAT_22X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int b = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getBUnit());
        }
    }
    ,
    FORMAT_21T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            short target = (short)in.read();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, baseAddress + target, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), relativeTarget);
        }
    }
    ,
    FORMAT_21S{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            short literal = (short)in.read();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getLiteralUnit());
        }
    }
    ,
    FORMAT_21H{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            long literal = (short)in.read();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, literal <<= opcode == 21 ? 16 : 48, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int opcode = insn.getOpcode();
            int shift = opcode == 21 ? 16 : 48;
            short literal = (short)(insn.getLiteral() >> shift);
            out.write(InstructionCodec.codeUnit(opcode, insn.getA()), literal);
        }
    }
    ,
    FORMAT_21C{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int index = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new OneRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getIndexUnit());
        }
    }
    ,
    FORMAT_23X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int bc = in.read();
            int b = InstructionCodec.byte0(bc);
            int c = InstructionCodec.byte1(bc);
            return new ThreeRegisterDecodedInstruction(this, opcode, 0, null, 0, 0L, a, b, c);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getC()));
        }
    }
    ,
    FORMAT_22B{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int bc = in.read();
            int b = InstructionCodec.byte0(bc);
            byte literal = (byte)InstructionCodec.byte1(bc);
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getLiteralByte()));
        }
    }
    ,
    FORMAT_22T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int b = InstructionCodec.nibble3(opcodeUnit);
            short target = (short)in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, baseAddress + target, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), relativeTarget);
        }
    }
    ,
    FORMAT_22S{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int b = InstructionCodec.nibble3(opcodeUnit);
            short literal = (short)in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getLiteralUnit());
        }
    }
    ,
    FORMAT_22C{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int b = InstructionCodec.nibble3(opcodeUnit);
            int index = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new TwoRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
        }
    }
    ,
    FORMAT_22CS{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.nibble2(opcodeUnit);
            int b = InstructionCodec.nibble3(opcodeUnit);
            int index = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, index, IndexType.FIELD_OFFSET, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
        }
    }
    ,
    FORMAT_30T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int literal = InstructionCodec.byte1(opcodeUnit);
            int target = in.readInt();
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null, baseAddress + target, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(insn.getOpcodeUnit(), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
        }
    }
    ,
    FORMAT_32X{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int literal = InstructionCodec.byte1(opcodeUnit);
            int a = in.read();
            int b = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), insn.getAUnit(), insn.getBUnit());
        }
    }
    ,
    FORMAT_31I{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int literal = in.readInt();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int literal = insn.getLiteralInt();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal));
        }
    }
    ,
    FORMAT_31T{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int target = baseAddress + in.readInt();
            switch (opcode) {
                case 43: 
                case 44: {
                    in.setBaseAddress(target, baseAddress);
                    break;
                }
            }
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, target, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
        }
    }
    ,
    FORMAT_31C{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            int index = in.readInt();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new OneRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(index), InstructionCodec.unit1(index));
        }
    }
    ,
    FORMAT_35C{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    }
    ,
    FORMAT_35MS{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    }
    ,
    FORMAT_35MI{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterList((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterList(insn, out);
        }
    }
    ,
    FORMAT_3RC{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    }
    ,
    FORMAT_3RMS{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    }
    ,
    FORMAT_3RMI{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            return InstructionCodec.decodeRegisterRange((InstructionCodec)this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InstructionCodec.encodeRegisterRange(insn, out);
        }
    }
    ,
    FORMAT_51L{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            int a = InstructionCodec.byte1(opcodeUnit);
            long literal = in.readLong();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            long literal = insn.getLiteral();
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal), InstructionCodec.unit2(literal), InstructionCodec.unit3(literal));
        }
    }
    ,
    FORMAT_45CC{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            if (opcode != 250) {
                throw new UnsupportedOperationException(String.valueOf(opcode));
            }
            int g = InstructionCodec.nibble2(opcodeUnit);
            int registerCount = InstructionCodec.nibble3(opcodeUnit);
            int methodIndex = in.read();
            int cdef = in.read();
            int c = InstructionCodec.nibble0(cdef);
            int d = InstructionCodec.nibble1(cdef);
            int e = InstructionCodec.nibble2(cdef);
            int f = InstructionCodec.nibble3(cdef);
            int protoIndex = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            if (registerCount < 1 || registerCount > 5) {
                throw new DexException("bogus registerCount: " + Hex.uNibble(registerCount));
            }
            int[] registers = new int[]{c, d, e, f, g};
            registers = Arrays.copyOfRange(registers, 0, registerCount);
            return new InvokePolymorphicDecodedInstruction((InstructionCodec)this, opcode, methodIndex, indexType, protoIndex, registers);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            InvokePolymorphicDecodedInstruction polyInsn = (InvokePolymorphicDecodedInstruction)insn;
            out.write(InstructionCodec.codeUnit(polyInsn.getOpcode(), InstructionCodec.makeByte(polyInsn.getG(), polyInsn.getRegisterCount())), polyInsn.getIndexUnit(), InstructionCodec.codeUnit(polyInsn.getC(), polyInsn.getD(), polyInsn.getE(), polyInsn.getF()), polyInsn.getProtoIndex());
        }
    }
    ,
    FORMAT_4RCC{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int opcode = InstructionCodec.byte0(opcodeUnit);
            if (opcode != 251) {
                throw new UnsupportedOperationException(String.valueOf(opcode));
            }
            int registerCount = InstructionCodec.byte1(opcodeUnit);
            int methodIndex = in.read();
            int c = in.read();
            int protoIndex = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new InvokePolymorphicRangeDecodedInstruction(this, opcode, methodIndex, indexType, c, registerCount, protoIndex);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getRegisterCount()), insn.getIndexUnit(), insn.getCUnit(), insn.getProtoIndex());
        }
    }
    ,
    FORMAT_PACKED_SWITCH_PAYLOAD{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int baseAddress = in.baseAddressForCursor() - 1;
            int size = in.read();
            int firstKey = in.readInt();
            int[] targets = new int[size];
            for (int i = 0; i < size; ++i) {
                targets[i] = baseAddress + in.readInt();
            }
            return new PackedSwitchPayloadDecodedInstruction(this, opcodeUnit, firstKey, targets);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            PackedSwitchPayloadDecodedInstruction payload = (PackedSwitchPayloadDecodedInstruction)insn;
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();
            out.write(payload.getOpcodeUnit());
            out.write(InstructionCodec.asUnsignedUnit(targets.length));
            out.writeInt(payload.getFirstKey());
            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }
    }
    ,
    FORMAT_SPARSE_SWITCH_PAYLOAD{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int i;
            int baseAddress = in.baseAddressForCursor() - 1;
            int size = in.read();
            int[] keys = new int[size];
            int[] targets = new int[size];
            for (i = 0; i < size; ++i) {
                keys[i] = in.readInt();
            }
            for (i = 0; i < size; ++i) {
                targets[i] = baseAddress + in.readInt();
            }
            return new SparseSwitchPayloadDecodedInstruction(this, opcodeUnit, keys, targets);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            SparseSwitchPayloadDecodedInstruction payload = (SparseSwitchPayloadDecodedInstruction)insn;
            int[] keys = payload.getKeys();
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();
            out.write(payload.getOpcodeUnit());
            out.write(InstructionCodec.asUnsignedUnit(targets.length));
            for (int key : keys) {
                out.writeInt(key);
            }
            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }
    }
    ,
    FORMAT_FILL_ARRAY_DATA_PAYLOAD{

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
            int elementWidth = in.read();
            int size = in.readInt();
            switch (elementWidth) {
                case 1: {
                    byte[] array = new byte[size];
                    boolean even = true;
                    int value = 0;
                    for (int i = 0; i < size; ++i) {
                        if (even) {
                            value = in.read();
                        }
                        array[i] = (byte)(value & 0xFF);
                        value >>= 8;
                        even = !even;
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec)this, opcodeUnit, array);
                }
                case 2: {
                    short[] array = new short[size];
                    for (int i = 0; i < size; ++i) {
                        array[i] = (short)in.read();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec)this, opcodeUnit, array);
                }
                case 4: {
                    int[] array = new int[size];
                    for (int i = 0; i < size; ++i) {
                        array[i] = in.readInt();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec)this, opcodeUnit, array);
                }
                case 8: {
                    long[] array = new long[size];
                    for (int i = 0; i < size; ++i) {
                        array[i] = in.readLong();
                    }
                    return new FillArrayDataPayloadDecodedInstruction((InstructionCodec)this, opcodeUnit, array);
                }
            }
            throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            FillArrayDataPayloadDecodedInstruction payload = (FillArrayDataPayloadDecodedInstruction)insn;
            short elementWidth = payload.getElementWidthUnit();
            Object data = payload.getData();
            out.write(payload.getOpcodeUnit());
            out.write(elementWidth);
            out.writeInt(payload.getSize());
            switch (elementWidth) {
                case 1: {
                    out.write((byte[])data);
                    break;
                }
                case 2: {
                    out.write((short[])data);
                    break;
                }
                case 4: {
                    out.write((int[])data);
                    break;
                }
                case 8: {
                    out.write((long[])data);
                    break;
                }
                default: {
                    throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
                }
            }
        }
    };


    public abstract DecodedInstruction decode(int var1, CodeInput var2) throws EOFException;

    public abstract void encode(DecodedInstruction var1, CodeOutput var2);

    private static DecodedInstruction decodeRegisterList(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
        int opcode = InstructionCodec.byte0(opcodeUnit);
        int e = InstructionCodec.nibble2(opcodeUnit);
        int registerCount = InstructionCodec.nibble3(opcodeUnit);
        int index = in.read();
        int abcd = in.read();
        int a = InstructionCodec.nibble0(abcd);
        int b = InstructionCodec.nibble1(abcd);
        int c = InstructionCodec.nibble2(abcd);
        int d = InstructionCodec.nibble3(abcd);
        IndexType indexType = OpcodeInfo.getIndexType(opcode);
        switch (registerCount) {
            case 0: {
                return new ZeroRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L);
            }
            case 1: {
                return new OneRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a);
            }
            case 2: {
                return new TwoRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b);
            }
            case 3: {
                return new ThreeRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c);
            }
            case 4: {
                return new FourRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c, d);
            }
            case 5: {
                return new FiveRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c, d, e);
            }
        }
        throw new DexException("bogus registerCount: " + Hex.uNibble(registerCount));
    }

    private static void encodeRegisterList(DecodedInstruction insn, CodeOutput out) {
        out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getE(), insn.getRegisterCount())), insn.getIndexUnit(), InstructionCodec.codeUnit(insn.getA(), insn.getB(), insn.getC(), insn.getD()));
    }

    private static DecodedInstruction decodeRegisterRange(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
        int opcode = InstructionCodec.byte0(opcodeUnit);
        int registerCount = InstructionCodec.byte1(opcodeUnit);
        int index = in.read();
        int a = in.read();
        IndexType indexType = OpcodeInfo.getIndexType(opcode);
        return new RegisterRangeDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, registerCount);
    }

    private static void encodeRegisterRange(DecodedInstruction insn, CodeOutput out) {
        out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getRegisterCount()), insn.getIndexUnit(), insn.getAUnit());
    }

    private static short codeUnit(int lowByte, int highByte) {
        if ((lowByte & 0xFFFFFF00) != 0) {
            throw new IllegalArgumentException("bogus lowByte");
        }
        if ((highByte & 0xFFFFFF00) != 0) {
            throw new IllegalArgumentException("bogus highByte");
        }
        return (short)(lowByte | highByte << 8);
    }

    private static short codeUnit(int nibble0, int nibble1, int nibble2, int nibble3) {
        if ((nibble0 & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus nibble0");
        }
        if ((nibble1 & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus nibble1");
        }
        if ((nibble2 & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus nibble2");
        }
        if ((nibble3 & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus nibble3");
        }
        return (short)(nibble0 | nibble1 << 4 | nibble2 << 8 | nibble3 << 12);
    }

    private static int makeByte(int lowNibble, int highNibble) {
        if ((lowNibble & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus lowNibble");
        }
        if ((highNibble & 0xFFFFFFF0) != 0) {
            throw new IllegalArgumentException("bogus highNibble");
        }
        return lowNibble | highNibble << 4;
    }

    private static short asUnsignedUnit(int value) {
        if ((value & 0xFFFF0000) != 0) {
            throw new IllegalArgumentException("bogus unsigned code unit");
        }
        return (short)value;
    }

    private static short unit0(int value) {
        return (short)value;
    }

    private static short unit1(int value) {
        return (short)(value >> 16);
    }

    private static short unit0(long value) {
        return (short)value;
    }

    private static short unit1(long value) {
        return (short)(value >> 16);
    }

    private static short unit2(long value) {
        return (short)(value >> 32);
    }

    private static short unit3(long value) {
        return (short)(value >> 48);
    }

    private static int byte0(int value) {
        return value & 0xFF;
    }

    private static int byte1(int value) {
        return value >> 8 & 0xFF;
    }

    private static int byte2(int value) {
        return value >> 16 & 0xFF;
    }

    private static int byte3(int value) {
        return value >>> 24;
    }

    private static int nibble0(int value) {
        return value & 0xF;
    }

    private static int nibble1(int value) {
        return value >> 4 & 0xF;
    }

    private static int nibble2(int value) {
        return value >> 8 & 0xF;
    }

    private static int nibble3(int value) {
        return value >> 12 & 0xF;
    }
}

