/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.cst;

import com.android.dx.cf.iface.ParseException;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstInterfaceMethodRef;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.cst.StdConstantPool;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.util.BitSet;

public final class ConstantPoolParser {
    private final ByteArray bytes;
    private final StdConstantPool pool;
    private final int[] offsets;
    private int endOffset;
    private ParseObserver observer;

    public ConstantPoolParser(ByteArray bytes) {
        int size = bytes.getUnsignedShort(8);
        this.bytes = bytes;
        this.pool = new StdConstantPool(size);
        this.offsets = new int[size];
        this.endOffset = -1;
    }

    public void setObserver(ParseObserver observer) {
        this.observer = observer;
    }

    public int getEndOffset() {
        this.parseIfNecessary();
        return this.endOffset;
    }

    public StdConstantPool getPool() {
        this.parseIfNecessary();
        return this.pool;
    }

    private void parseIfNecessary() {
        if (this.endOffset < 0) {
            this.parse();
        }
    }

    private void parse() {
        int i;
        this.determineOffsets();
        if (this.observer != null) {
            this.observer.parsed(this.bytes, 8, 2, "constant_pool_count: " + Hex.u2(this.offsets.length));
            this.observer.parsed(this.bytes, 10, 0, "\nconstant_pool:");
            this.observer.changeIndent(1);
        }
        BitSet wasUtf8 = new BitSet(this.offsets.length);
        for (i = 1; i < this.offsets.length; ++i) {
            int offset = this.offsets[i];
            if (offset == 0 || this.pool.getOrNull(i) != null) continue;
            this.parse0(i, wasUtf8);
        }
        if (this.observer != null) {
            for (i = 1; i < this.offsets.length; ++i) {
                Constant cst = this.pool.getOrNull(i);
                if (cst == null) continue;
                int offset = this.offsets[i];
                int nextOffset = this.endOffset;
                for (int j = i + 1; j < this.offsets.length; ++j) {
                    int off = this.offsets[j];
                    if (off == 0) continue;
                    nextOffset = off;
                    break;
                }
                String human = wasUtf8.get(i) ? Hex.u2(i) + ": utf8{\"" + cst.toHuman() + "\"}" : Hex.u2(i) + ": " + cst.toString();
                this.observer.parsed(this.bytes, offset, nextOffset - offset, human);
            }
            this.observer.changeIndent(-1);
            this.observer.parsed(this.bytes, this.endOffset, 0, "end constant_pool");
        }
    }

    private void determineOffsets() {
        int lastCategory;
        int at = 10;
        for (int i = 1; i < this.offsets.length; i += lastCategory) {
            this.offsets[i] = at;
            int tag = this.bytes.getUnsignedByte(at);
            try {
                switch (tag) {
                    case 3: 
                    case 4: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: {
                        lastCategory = 1;
                        at += 5;
                        break;
                    }
                    case 5: 
                    case 6: {
                        lastCategory = 2;
                        at += 9;
                        break;
                    }
                    case 7: 
                    case 8: {
                        lastCategory = 1;
                        at += 3;
                        break;
                    }
                    case 1: {
                        lastCategory = 1;
                        at += this.bytes.getUnsignedShort(at + 1) + 3;
                        break;
                    }
                    case 15: {
                        lastCategory = 1;
                        at += 4;
                        break;
                    }
                    case 16: {
                        lastCategory = 1;
                        at += 3;
                        break;
                    }
                    case 18: {
                        lastCategory = 1;
                        at += 5;
                        break;
                    }
                    default: {
                        throw new ParseException("unknown tag byte: " + Hex.u1(tag));
                    }
                }
                continue;
            }
            catch (ParseException ex) {
                ex.addContext("...while preparsing cst " + Hex.u2(i) + " at offset " + Hex.u4(at));
                throw ex;
            }
        }
        this.endOffset = at;
    }

    private Constant parse0(int idx, BitSet wasUtf8) {
        Constant cst = this.pool.getOrNull(idx);
        if (cst != null) {
            return cst;
        }
        int at = this.offsets[idx];
        try {
            int tag = this.bytes.getUnsignedByte(at);
            switch (tag) {
                case 1: {
                    cst = this.parseUtf8(at);
                    wasUtf8.set(idx);
                    break;
                }
                case 3: {
                    int value = this.bytes.getInt(at + 1);
                    cst = CstInteger.make(value);
                    break;
                }
                case 4: {
                    int bits = this.bytes.getInt(at + 1);
                    cst = CstFloat.make(bits);
                    break;
                }
                case 5: {
                    long value = this.bytes.getLong(at + 1);
                    cst = CstLong.make(value);
                    break;
                }
                case 6: {
                    long bits = this.bytes.getLong(at + 1);
                    cst = CstDouble.make(bits);
                    break;
                }
                case 7: {
                    int nameIndex = this.bytes.getUnsignedShort(at + 1);
                    CstString name = (CstString)this.parse0(nameIndex, wasUtf8);
                    cst = new CstType(Type.internClassName(name.getString()));
                    break;
                }
                case 8: {
                    int stringIndex = this.bytes.getUnsignedShort(at + 1);
                    cst = this.parse0(stringIndex, wasUtf8);
                    break;
                }
                case 9: {
                    int classIndex = this.bytes.getUnsignedShort(at + 1);
                    CstType type = (CstType)this.parse0(classIndex, wasUtf8);
                    int natIndex = this.bytes.getUnsignedShort(at + 3);
                    CstNat nat = (CstNat)this.parse0(natIndex, wasUtf8);
                    cst = new CstFieldRef(type, nat);
                    break;
                }
                case 10: {
                    int classIndex = this.bytes.getUnsignedShort(at + 1);
                    CstType type = (CstType)this.parse0(classIndex, wasUtf8);
                    int natIndex = this.bytes.getUnsignedShort(at + 3);
                    CstNat nat = (CstNat)this.parse0(natIndex, wasUtf8);
                    cst = new CstMethodRef(type, nat);
                    break;
                }
                case 11: {
                    int classIndex = this.bytes.getUnsignedShort(at + 1);
                    CstType type = (CstType)this.parse0(classIndex, wasUtf8);
                    int natIndex = this.bytes.getUnsignedShort(at + 3);
                    CstNat nat = (CstNat)this.parse0(natIndex, wasUtf8);
                    cst = new CstInterfaceMethodRef(type, nat);
                    break;
                }
                case 12: {
                    int nameIndex = this.bytes.getUnsignedShort(at + 1);
                    CstString name = (CstString)this.parse0(nameIndex, wasUtf8);
                    int descriptorIndex = this.bytes.getUnsignedShort(at + 3);
                    CstString descriptor = (CstString)this.parse0(descriptorIndex, wasUtf8);
                    cst = new CstNat(name, descriptor);
                    break;
                }
                case 15: {
                    Constant ref;
                    int kind = this.bytes.getUnsignedByte(at + 1);
                    int constantIndex = this.bytes.getUnsignedShort(at + 2);
                    switch (kind) {
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: {
                            ref = (CstFieldRef)this.parse0(constantIndex, wasUtf8);
                            break;
                        }
                        case 5: 
                        case 8: {
                            ref = (CstMethodRef)this.parse0(constantIndex, wasUtf8);
                            break;
                        }
                        case 6: 
                        case 7: {
                            ref = this.parse0(constantIndex, wasUtf8);
                            if (ref instanceof CstMethodRef || ref instanceof CstInterfaceMethodRef) break;
                            throw new ParseException("Unsupported ref constant type for MethodHandle " + ref.getClass());
                        }
                        case 9: {
                            ref = (CstInterfaceMethodRef)this.parse0(constantIndex, wasUtf8);
                            break;
                        }
                        default: {
                            throw new ParseException("Unsupported MethodHandle kind: " + kind);
                        }
                    }
                    int methodHandleType = ConstantPoolParser.getMethodHandleTypeForKind(kind);
                    cst = CstMethodHandle.make(methodHandleType, ref);
                    break;
                }
                case 16: {
                    int descriptorIndex = this.bytes.getUnsignedShort(at + 1);
                    CstString descriptor = (CstString)this.parse0(descriptorIndex, wasUtf8);
                    cst = CstProtoRef.make(descriptor);
                    break;
                }
                case 18: {
                    int bootstrapMethodIndex = this.bytes.getUnsignedShort(at + 1);
                    int natIndex = this.bytes.getUnsignedShort(at + 3);
                    CstNat nat = (CstNat)this.parse0(natIndex, wasUtf8);
                    cst = CstInvokeDynamic.make(bootstrapMethodIndex, nat);
                    break;
                }
                default: {
                    throw new ParseException("unknown tag byte: " + Hex.u1(tag));
                }
            }
        }
        catch (ParseException ex) {
            ex.addContext("...while parsing cst " + Hex.u2(idx) + " at offset " + Hex.u4(at));
            throw ex;
        }
        catch (RuntimeException ex) {
            ParseException pe = new ParseException(ex);
            pe.addContext("...while parsing cst " + Hex.u2(idx) + " at offset " + Hex.u4(at));
            throw pe;
        }
        this.pool.set(idx, cst);
        return cst;
    }

    private CstString parseUtf8(int at) {
        int length = this.bytes.getUnsignedShort(at + 1);
        ByteArray ubytes = this.bytes.slice(at += 3, at + length);
        try {
            return new CstString(ubytes);
        }
        catch (IllegalArgumentException ex) {
            throw new ParseException(ex);
        }
    }

    private static int getMethodHandleTypeForKind(int kind) {
        switch (kind) {
            case 1: {
                return 3;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
            case 4: {
                return 0;
            }
            case 5: {
                return 5;
            }
            case 6: {
                return 4;
            }
            case 7: {
                return 7;
            }
            case 8: {
                return 6;
            }
            case 9: {
                return 8;
            }
        }
        throw new IllegalArgumentException("invalid kind: " + kind);
    }
}

