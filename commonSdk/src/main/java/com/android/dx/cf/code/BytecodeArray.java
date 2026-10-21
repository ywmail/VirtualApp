/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dx.cf.code.ByteOps;
import com.android.dx.cf.code.SimException;
import com.android.dx.cf.code.SwitchList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.ConstantPool;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.Bits;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.util.ArrayList;

public final class BytecodeArray {
    public static final Visitor EMPTY_VISITOR = new BaseVisitor();
    private final ByteArray bytes;
    private final ConstantPool pool;

    public BytecodeArray(ByteArray bytes, ConstantPool pool) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (pool == null) {
            throw new NullPointerException("pool == null");
        }
        this.bytes = bytes;
        this.pool = pool;
    }

    public ByteArray getBytes() {
        return this.bytes;
    }

    public int size() {
        return this.bytes.size();
    }

    public int byteLength() {
        return 4 + this.bytes.size();
    }

    public void forEach(Visitor visitor) {
        int sz = this.bytes.size();
        for (int at = 0; at < sz; at += this.parseInstruction(at, visitor)) {
        }
    }

    public int[] getInstructionOffsets() {
        int length;
        int sz = this.bytes.size();
        int[] result = Bits.makeBitSet(sz);
        for (int at = 0; at < sz; at += length) {
            Bits.set(result, at, true);
            length = this.parseInstruction(at, null);
        }
        return result;
    }

    public void processWorkSet(int[] workSet, Visitor visitor) {
        int offset;
        if (visitor == null) {
            throw new NullPointerException("visitor == null");
        }
        while ((offset = Bits.findFirst(workSet, 0)) >= 0) {
            Bits.clear(workSet, offset);
            this.parseInstruction(offset, visitor);
            visitor.setPreviousOffset(offset);
        }
    }

    public int parseInstruction(int offset, Visitor visitor) {
        if (visitor == null) {
            visitor = EMPTY_VISITOR;
        }
        try {
            int opcode = this.bytes.getUnsignedByte(offset);
            int info = ByteOps.opInfo(opcode);
            int fmt = info & 0x1F;
            switch (opcode) {
                case 0: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.VOID);
                    return 1;
                }
                case 1: {
                    visitor.visitConstant(18, offset, 1, CstKnownNull.THE_ONE, 0);
                    return 1;
                }
                case 2: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_M1, -1);
                    return 1;
                }
                case 3: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_0, 0);
                    return 1;
                }
                case 4: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_1, 1);
                    return 1;
                }
                case 5: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_2, 2);
                    return 1;
                }
                case 6: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_3, 3);
                    return 1;
                }
                case 7: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_4, 4);
                    return 1;
                }
                case 8: {
                    visitor.visitConstant(18, offset, 1, CstInteger.VALUE_5, 5);
                    return 1;
                }
                case 9: {
                    visitor.visitConstant(18, offset, 1, CstLong.VALUE_0, 0);
                    return 1;
                }
                case 10: {
                    visitor.visitConstant(18, offset, 1, CstLong.VALUE_1, 0);
                    return 1;
                }
                case 11: {
                    visitor.visitConstant(18, offset, 1, CstFloat.VALUE_0, 0);
                    return 1;
                }
                case 12: {
                    visitor.visitConstant(18, offset, 1, CstFloat.VALUE_1, 0);
                    return 1;
                }
                case 13: {
                    visitor.visitConstant(18, offset, 1, CstFloat.VALUE_2, 0);
                    return 1;
                }
                case 14: {
                    visitor.visitConstant(18, offset, 1, CstDouble.VALUE_0, 0);
                    return 1;
                }
                case 15: {
                    visitor.visitConstant(18, offset, 1, CstDouble.VALUE_1, 0);
                    return 1;
                }
                case 16: {
                    int value = this.bytes.getByte(offset + 1);
                    visitor.visitConstant(18, offset, 2, CstInteger.make(value), value);
                    return 2;
                }
                case 17: {
                    int value = this.bytes.getShort(offset + 1);
                    visitor.visitConstant(18, offset, 3, CstInteger.make(value), value);
                    return 3;
                }
                case 18: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    Constant cst = this.pool.get(idx);
                    int value = cst instanceof CstInteger ? ((CstInteger)cst).getValue() : 0;
                    visitor.visitConstant(18, offset, 2, cst, value);
                    return 2;
                }
                case 19: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    Constant cst = this.pool.get(idx);
                    int value = cst instanceof CstInteger ? ((CstInteger)cst).getValue() : 0;
                    visitor.visitConstant(18, offset, 3, cst, value);
                    return 3;
                }
                case 20: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    Constant cst = this.pool.get(idx);
                    visitor.visitConstant(20, offset, 3, cst, 0);
                    return 3;
                }
                case 21: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(21, offset, 2, idx, Type.INT, 0);
                    return 2;
                }
                case 22: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(21, offset, 2, idx, Type.LONG, 0);
                    return 2;
                }
                case 23: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(21, offset, 2, idx, Type.FLOAT, 0);
                    return 2;
                }
                case 24: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(21, offset, 2, idx, Type.DOUBLE, 0);
                    return 2;
                }
                case 25: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(21, offset, 2, idx, Type.OBJECT, 0);
                    return 2;
                }
                case 26: 
                case 27: 
                case 28: 
                case 29: {
                    int idx = opcode - 26;
                    visitor.visitLocal(21, offset, 1, idx, Type.INT, 0);
                    return 1;
                }
                case 30: 
                case 31: 
                case 32: 
                case 33: {
                    int idx = opcode - 30;
                    visitor.visitLocal(21, offset, 1, idx, Type.LONG, 0);
                    return 1;
                }
                case 34: 
                case 35: 
                case 36: 
                case 37: {
                    int idx = opcode - 34;
                    visitor.visitLocal(21, offset, 1, idx, Type.FLOAT, 0);
                    return 1;
                }
                case 38: 
                case 39: 
                case 40: 
                case 41: {
                    int idx = opcode - 38;
                    visitor.visitLocal(21, offset, 1, idx, Type.DOUBLE, 0);
                    return 1;
                }
                case 42: 
                case 43: 
                case 44: 
                case 45: {
                    int idx = opcode - 42;
                    visitor.visitLocal(21, offset, 1, idx, Type.OBJECT, 0);
                    return 1;
                }
                case 46: {
                    visitor.visitNoArgs(46, offset, 1, Type.INT);
                    return 1;
                }
                case 47: {
                    visitor.visitNoArgs(46, offset, 1, Type.LONG);
                    return 1;
                }
                case 48: {
                    visitor.visitNoArgs(46, offset, 1, Type.FLOAT);
                    return 1;
                }
                case 49: {
                    visitor.visitNoArgs(46, offset, 1, Type.DOUBLE);
                    return 1;
                }
                case 50: {
                    visitor.visitNoArgs(46, offset, 1, Type.OBJECT);
                    return 1;
                }
                case 51: {
                    visitor.visitNoArgs(46, offset, 1, Type.BYTE);
                    return 1;
                }
                case 52: {
                    visitor.visitNoArgs(46, offset, 1, Type.CHAR);
                    return 1;
                }
                case 53: {
                    visitor.visitNoArgs(46, offset, 1, Type.SHORT);
                    return 1;
                }
                case 54: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(54, offset, 2, idx, Type.INT, 0);
                    return 2;
                }
                case 55: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(54, offset, 2, idx, Type.LONG, 0);
                    return 2;
                }
                case 56: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(54, offset, 2, idx, Type.FLOAT, 0);
                    return 2;
                }
                case 57: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(54, offset, 2, idx, Type.DOUBLE, 0);
                    return 2;
                }
                case 58: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(54, offset, 2, idx, Type.OBJECT, 0);
                    return 2;
                }
                case 59: 
                case 60: 
                case 61: 
                case 62: {
                    int idx = opcode - 59;
                    visitor.visitLocal(54, offset, 1, idx, Type.INT, 0);
                    return 1;
                }
                case 63: 
                case 64: 
                case 65: 
                case 66: {
                    int idx = opcode - 63;
                    visitor.visitLocal(54, offset, 1, idx, Type.LONG, 0);
                    return 1;
                }
                case 67: 
                case 68: 
                case 69: 
                case 70: {
                    int idx = opcode - 67;
                    visitor.visitLocal(54, offset, 1, idx, Type.FLOAT, 0);
                    return 1;
                }
                case 71: 
                case 72: 
                case 73: 
                case 74: {
                    int idx = opcode - 71;
                    visitor.visitLocal(54, offset, 1, idx, Type.DOUBLE, 0);
                    return 1;
                }
                case 75: 
                case 76: 
                case 77: 
                case 78: {
                    int idx = opcode - 75;
                    visitor.visitLocal(54, offset, 1, idx, Type.OBJECT, 0);
                    return 1;
                }
                case 79: {
                    visitor.visitNoArgs(79, offset, 1, Type.INT);
                    return 1;
                }
                case 80: {
                    visitor.visitNoArgs(79, offset, 1, Type.LONG);
                    return 1;
                }
                case 81: {
                    visitor.visitNoArgs(79, offset, 1, Type.FLOAT);
                    return 1;
                }
                case 82: {
                    visitor.visitNoArgs(79, offset, 1, Type.DOUBLE);
                    return 1;
                }
                case 83: {
                    visitor.visitNoArgs(79, offset, 1, Type.OBJECT);
                    return 1;
                }
                case 84: {
                    visitor.visitNoArgs(79, offset, 1, Type.BYTE);
                    return 1;
                }
                case 85: {
                    visitor.visitNoArgs(79, offset, 1, Type.CHAR);
                    return 1;
                }
                case 86: {
                    visitor.visitNoArgs(79, offset, 1, Type.SHORT);
                    return 1;
                }
                case 87: 
                case 88: 
                case 89: 
                case 90: 
                case 91: 
                case 92: 
                case 93: 
                case 94: 
                case 95: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.VOID);
                    return 1;
                }
                case 96: 
                case 100: 
                case 104: 
                case 108: 
                case 112: 
                case 116: 
                case 120: 
                case 122: 
                case 124: 
                case 126: 
                case 128: 
                case 130: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.INT);
                    return 1;
                }
                case 97: 
                case 101: 
                case 105: 
                case 109: 
                case 113: 
                case 117: 
                case 121: 
                case 123: 
                case 125: 
                case 127: 
                case 129: 
                case 131: {
                    visitor.visitNoArgs(opcode - 1, offset, 1, Type.LONG);
                    return 1;
                }
                case 98: 
                case 102: 
                case 106: 
                case 110: 
                case 114: 
                case 118: {
                    visitor.visitNoArgs(opcode - 2, offset, 1, Type.FLOAT);
                    return 1;
                }
                case 99: 
                case 103: 
                case 107: 
                case 111: 
                case 115: 
                case 119: {
                    visitor.visitNoArgs(opcode - 3, offset, 1, Type.DOUBLE);
                    return 1;
                }
                case 132: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    int value = this.bytes.getByte(offset + 2);
                    visitor.visitLocal(opcode, offset, 3, idx, Type.INT, value);
                    return 3;
                }
                case 133: 
                case 140: 
                case 143: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.LONG);
                    return 1;
                }
                case 134: 
                case 137: 
                case 144: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.FLOAT);
                    return 1;
                }
                case 135: 
                case 138: 
                case 141: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.DOUBLE);
                    return 1;
                }
                case 136: 
                case 139: 
                case 142: 
                case 145: 
                case 146: 
                case 147: 
                case 148: 
                case 149: 
                case 150: 
                case 151: 
                case 152: 
                case 190: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.INT);
                    return 1;
                }
                case 153: 
                case 154: 
                case 155: 
                case 156: 
                case 157: 
                case 158: 
                case 159: 
                case 160: 
                case 161: 
                case 162: 
                case 163: 
                case 164: 
                case 165: 
                case 166: 
                case 167: 
                case 168: 
                case 198: 
                case 199: {
                    int target = offset + this.bytes.getShort(offset + 1);
                    visitor.visitBranch(opcode, offset, 3, target);
                    return 3;
                }
                case 169: {
                    int idx = this.bytes.getUnsignedByte(offset + 1);
                    visitor.visitLocal(opcode, offset, 2, idx, Type.RETURN_ADDRESS, 0);
                    return 2;
                }
                case 170: {
                    return this.parseTableswitch(offset, visitor);
                }
                case 171: {
                    return this.parseLookupswitch(offset, visitor);
                }
                case 172: {
                    visitor.visitNoArgs(172, offset, 1, Type.INT);
                    return 1;
                }
                case 173: {
                    visitor.visitNoArgs(172, offset, 1, Type.LONG);
                    return 1;
                }
                case 174: {
                    visitor.visitNoArgs(172, offset, 1, Type.FLOAT);
                    return 1;
                }
                case 175: {
                    visitor.visitNoArgs(172, offset, 1, Type.DOUBLE);
                    return 1;
                }
                case 176: {
                    visitor.visitNoArgs(172, offset, 1, Type.OBJECT);
                    return 1;
                }
                case 177: 
                case 191: 
                case 194: 
                case 195: {
                    visitor.visitNoArgs(opcode, offset, 1, Type.VOID);
                    return 1;
                }
                case 178: 
                case 179: 
                case 180: 
                case 181: 
                case 182: 
                case 183: 
                case 184: 
                case 187: 
                case 189: 
                case 192: 
                case 193: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    Constant cst = this.pool.get(idx);
                    visitor.visitConstant(opcode, offset, 3, cst, 0);
                    return 3;
                }
                case 185: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    int count = this.bytes.getUnsignedByte(offset + 3);
                    int expectZero = this.bytes.getUnsignedByte(offset + 4);
                    Constant cst = this.pool.get(idx);
                    visitor.visitConstant(opcode, offset, 5, cst, count | expectZero << 8);
                    return 5;
                }
                case 186: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    CstInvokeDynamic cstInvokeDynamic = (CstInvokeDynamic)this.pool.get(idx);
                    visitor.visitConstant(opcode, offset, 5, cstInvokeDynamic, 0);
                    return 5;
                }
                case 188: {
                    return this.parseNewarray(offset, visitor);
                }
                case 196: {
                    return this.parseWide(offset, visitor);
                }
                case 197: {
                    int idx = this.bytes.getUnsignedShort(offset + 1);
                    int dimensions = this.bytes.getUnsignedByte(offset + 3);
                    Constant cst = this.pool.get(idx);
                    visitor.visitConstant(opcode, offset, 4, cst, dimensions);
                    return 4;
                }
                case 200: 
                case 201: {
                    int target = offset + this.bytes.getInt(offset + 1);
                    int newop = opcode == 200 ? 167 : 168;
                    visitor.visitBranch(newop, offset, 5, target);
                    return 5;
                }
            }
            visitor.visitInvalid(opcode, offset, 1);
            return 1;
        }
        catch (SimException ex) {
            ex.addContext("...at bytecode offset " + Hex.u4(offset));
            throw ex;
        }
        catch (RuntimeException ex) {
            SimException se = new SimException(ex);
            se.addContext("...at bytecode offset " + Hex.u4(offset));
            throw se;
        }
    }

    private int parseTableswitch(int offset, Visitor visitor) {
        int at = offset + 4 & 0xFFFFFFFC;
        int padding = 0;
        for (int i = offset + 1; i < at; ++i) {
            padding = padding << 8 | this.bytes.getUnsignedByte(i);
        }
        int defaultTarget = offset + this.bytes.getInt(at);
        int low = this.bytes.getInt(at + 4);
        int high = this.bytes.getInt(at + 8);
        int count = high - low + 1;
        at += 12;
        if (low > high) {
            throw new SimException("low / high inversion");
        }
        SwitchList cases = new SwitchList(count);
        for (int i = 0; i < count; ++i) {
            int target = offset + this.bytes.getInt(at);
            at += 4;
            cases.add(low + i, target);
        }
        cases.setDefaultTarget(defaultTarget);
        cases.removeSuperfluousDefaults();
        cases.setImmutable();
        int length = at - offset;
        visitor.visitSwitch(171, offset, length, cases, padding);
        return length;
    }

    private int parseLookupswitch(int offset, Visitor visitor) {
        int at = offset + 4 & 0xFFFFFFFC;
        int padding = 0;
        for (int i = offset + 1; i < at; ++i) {
            padding = padding << 8 | this.bytes.getUnsignedByte(i);
        }
        int defaultTarget = offset + this.bytes.getInt(at);
        int npairs = this.bytes.getInt(at + 4);
        at += 8;
        SwitchList cases = new SwitchList(npairs);
        for (int i = 0; i < npairs; ++i) {
            int match = this.bytes.getInt(at);
            int target = offset + this.bytes.getInt(at + 4);
            at += 8;
            cases.add(match, target);
        }
        cases.setDefaultTarget(defaultTarget);
        cases.removeSuperfluousDefaults();
        cases.setImmutable();
        int length = at - offset;
        visitor.visitSwitch(171, offset, length, cases, padding);
        return length;
    }

    private int parseNewarray(int offset, Visitor visitor) {
        int curOffset;
        CstType type;
        int value = this.bytes.getUnsignedByte(offset + 1);
        switch (value) {
            case 4: {
                type = CstType.BOOLEAN_ARRAY;
                break;
            }
            case 5: {
                type = CstType.CHAR_ARRAY;
                break;
            }
            case 7: {
                type = CstType.DOUBLE_ARRAY;
                break;
            }
            case 6: {
                type = CstType.FLOAT_ARRAY;
                break;
            }
            case 8: {
                type = CstType.BYTE_ARRAY;
                break;
            }
            case 9: {
                type = CstType.SHORT_ARRAY;
                break;
            }
            case 10: {
                type = CstType.INT_ARRAY;
                break;
            }
            case 11: {
                type = CstType.LONG_ARRAY;
                break;
            }
            default: {
                throw new SimException("bad newarray code " + Hex.u1(value));
            }
        }
        int previousOffset = visitor.getPreviousOffset();
        ConstantParserVisitor constantVisitor = new ConstantParserVisitor();
        int arrayLength = 0;
        if (previousOffset >= 0) {
            this.parseInstruction(previousOffset, constantVisitor);
            if (constantVisitor.cst instanceof CstInteger && constantVisitor.length + previousOffset == offset) {
                arrayLength = constantVisitor.value;
            }
        }
        int nInit = 0;
        int lastOffset = curOffset = offset + 2;
        ArrayList<Constant> initVals = new ArrayList<Constant>();
        if (arrayLength != 0) {
            while (true) {
                int nextByte;
                boolean punt = false;
                if ((nextByte = this.bytes.getUnsignedByte(curOffset++)) != 89) break;
                this.parseInstruction(curOffset, constantVisitor);
                if (constantVisitor.length == 0 || !(constantVisitor.cst instanceof CstInteger) || constantVisitor.value != nInit) break;
                this.parseInstruction(curOffset += constantVisitor.length, constantVisitor);
                if (constantVisitor.length == 0 || !(constantVisitor.cst instanceof CstLiteralBits)) break;
                curOffset += constantVisitor.length;
                initVals.add(constantVisitor.cst);
                nextByte = this.bytes.getUnsignedByte(curOffset++);
                switch (value) {
                    case 4: 
                    case 8: {
                        if (nextByte == 84) break;
                        punt = true;
                        break;
                    }
                    case 5: {
                        if (nextByte == 85) break;
                        punt = true;
                        break;
                    }
                    case 7: {
                        if (nextByte == 82) break;
                        punt = true;
                        break;
                    }
                    case 6: {
                        if (nextByte == 81) break;
                        punt = true;
                        break;
                    }
                    case 9: {
                        if (nextByte == 86) break;
                        punt = true;
                        break;
                    }
                    case 10: {
                        if (nextByte == 79) break;
                        punt = true;
                        break;
                    }
                    case 11: {
                        if (nextByte == 80) break;
                        punt = true;
                        break;
                    }
                    default: {
                        punt = true;
                    }
                }
                if (punt) break;
                lastOffset = curOffset;
                ++nInit;
            }
        }
        if (nInit < 2 || nInit != arrayLength) {
            visitor.visitNewarray(offset, 2, type, null);
            return 2;
        }
        visitor.visitNewarray(offset, lastOffset - offset, type, initVals);
        return lastOffset - offset;
    }

    private int parseWide(int offset, Visitor visitor) {
        int opcode = this.bytes.getUnsignedByte(offset + 1);
        int idx = this.bytes.getUnsignedShort(offset + 2);
        switch (opcode) {
            case 21: {
                visitor.visitLocal(21, offset, 4, idx, Type.INT, 0);
                return 4;
            }
            case 22: {
                visitor.visitLocal(21, offset, 4, idx, Type.LONG, 0);
                return 4;
            }
            case 23: {
                visitor.visitLocal(21, offset, 4, idx, Type.FLOAT, 0);
                return 4;
            }
            case 24: {
                visitor.visitLocal(21, offset, 4, idx, Type.DOUBLE, 0);
                return 4;
            }
            case 25: {
                visitor.visitLocal(21, offset, 4, idx, Type.OBJECT, 0);
                return 4;
            }
            case 54: {
                visitor.visitLocal(54, offset, 4, idx, Type.INT, 0);
                return 4;
            }
            case 55: {
                visitor.visitLocal(54, offset, 4, idx, Type.LONG, 0);
                return 4;
            }
            case 56: {
                visitor.visitLocal(54, offset, 4, idx, Type.FLOAT, 0);
                return 4;
            }
            case 57: {
                visitor.visitLocal(54, offset, 4, idx, Type.DOUBLE, 0);
                return 4;
            }
            case 58: {
                visitor.visitLocal(54, offset, 4, idx, Type.OBJECT, 0);
                return 4;
            }
            case 169: {
                visitor.visitLocal(opcode, offset, 4, idx, Type.RETURN_ADDRESS, 0);
                return 4;
            }
            case 132: {
                int value = this.bytes.getShort(offset + 4);
                visitor.visitLocal(opcode, offset, 6, idx, Type.INT, value);
                return 6;
            }
        }
        visitor.visitInvalid(196, offset, 1);
        return 1;
    }

    class ConstantParserVisitor
    extends BaseVisitor {
        Constant cst;
        int length;
        int value;

        ConstantParserVisitor() {
        }

        private void clear() {
            this.length = 0;
        }

        @Override
        public void visitInvalid(int opcode, int offset, int length) {
            this.clear();
        }

        @Override
        public void visitNoArgs(int opcode, int offset, int length, Type type) {
            this.clear();
        }

        @Override
        public void visitLocal(int opcode, int offset, int length, int idx, Type type, int value) {
            this.clear();
        }

        @Override
        public void visitConstant(int opcode, int offset, int length, Constant cst, int value) {
            this.cst = cst;
            this.length = length;
            this.value = value;
        }

        @Override
        public void visitBranch(int opcode, int offset, int length, int target) {
            this.clear();
        }

        @Override
        public void visitSwitch(int opcode, int offset, int length, SwitchList cases, int padding) {
            this.clear();
        }

        @Override
        public void visitNewarray(int offset, int length, CstType type, ArrayList<Constant> initVals) {
            this.clear();
        }

        @Override
        public void setPreviousOffset(int offset) {
        }

        @Override
        public int getPreviousOffset() {
            return -1;
        }
    }

    public static class BaseVisitor
    implements Visitor {
        private int previousOffset = -1;

        BaseVisitor() {
        }

        @Override
        public void visitInvalid(int opcode, int offset, int length) {
        }

        @Override
        public void visitNoArgs(int opcode, int offset, int length, Type type) {
        }

        @Override
        public void visitLocal(int opcode, int offset, int length, int idx, Type type, int value) {
        }

        @Override
        public void visitConstant(int opcode, int offset, int length, Constant cst, int value) {
        }

        @Override
        public void visitBranch(int opcode, int offset, int length, int target) {
        }

        @Override
        public void visitSwitch(int opcode, int offset, int length, SwitchList cases, int padding) {
        }

        @Override
        public void visitNewarray(int offset, int length, CstType type, ArrayList<Constant> initValues) {
        }

        @Override
        public void setPreviousOffset(int offset) {
            this.previousOffset = offset;
        }

        @Override
        public int getPreviousOffset() {
            return this.previousOffset;
        }
    }

    public static interface Visitor {
        public void visitInvalid(int var1, int var2, int var3);

        public void visitNoArgs(int var1, int var2, int var3, Type var4);

        public void visitLocal(int var1, int var2, int var3, int var4, Type var5, int var6);

        public void visitConstant(int var1, int var2, int var3, Constant var4, int var5);

        public void visitBranch(int var1, int var2, int var3, int var4);

        public void visitSwitch(int var1, int var2, int var3, SwitchList var4, int var5);

        public void visitNewarray(int var1, int var2, CstType var3, ArrayList<Constant> var4);

        public void setPreviousOffset(int var1);

        public int getPreviousOffset();
    }
}

