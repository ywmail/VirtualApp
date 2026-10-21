/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.direct;

import com.android.dx.cf.code.ByteOps;
import com.android.dx.cf.code.BytecodeArray;
import com.android.dx.cf.code.SwitchList;
import com.android.dx.cf.iface.ParseObserver;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstDouble;
import com.android.dx.rop.cst.CstFloat;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstKnownNull;
import com.android.dx.rop.cst.CstLong;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArray;
import com.android.dx.util.Hex;
import java.util.ArrayList;

public class CodeObserver
implements BytecodeArray.Visitor {
    private final ByteArray bytes;
    private final ParseObserver observer;

    public CodeObserver(ByteArray bytes, ParseObserver observer) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        this.bytes = bytes;
        this.observer = observer;
    }

    @Override
    public void visitInvalid(int opcode, int offset, int length) {
        this.observer.parsed(this.bytes, offset, length, this.header(offset));
    }

    @Override
    public void visitNoArgs(int opcode, int offset, int length, Type type) {
        this.observer.parsed(this.bytes, offset, length, this.header(offset));
    }

    @Override
    public void visitLocal(int opcode, int offset, int length, int idx, Type type, int value) {
        String idxStr = length <= 3 ? Hex.u1(idx) : Hex.u2(idx);
        boolean argComment = length == 1;
        String valueStr = "";
        if (opcode == 132) {
            valueStr = ", #" + (length <= 3 ? Hex.s1(value) : Hex.s2(value));
        }
        String catStr = "";
        if (type.isCategory2()) {
            catStr = (argComment ? "," : " //") + " category-2";
        }
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + (argComment ? " // " : " ") + idxStr + valueStr + catStr);
    }

    @Override
    public void visitConstant(int opcode, int offset, int length, Constant cst, int value) {
        if (cst instanceof CstKnownNull) {
            this.visitNoArgs(opcode, offset, length, null);
            return;
        }
        if (cst instanceof CstInteger) {
            this.visitLiteralInt(opcode, offset, length, value);
            return;
        }
        if (cst instanceof CstLong) {
            this.visitLiteralLong(opcode, offset, length, ((CstLong)cst).getValue());
            return;
        }
        if (cst instanceof CstFloat) {
            this.visitLiteralFloat(opcode, offset, length, ((CstFloat)cst).getIntBits());
            return;
        }
        if (cst instanceof CstDouble) {
            this.visitLiteralDouble(opcode, offset, length, ((CstDouble)cst).getLongBits());
            return;
        }
        String valueStr = "";
        if (value != 0) {
            valueStr = ", ";
            valueStr = opcode == 197 ? valueStr + Hex.u1(value) : valueStr + Hex.u2(value);
        }
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + " " + cst + valueStr);
    }

    @Override
    public void visitBranch(int opcode, int offset, int length, int target) {
        String targetStr = length <= 3 ? Hex.u2(target) : Hex.u4(target);
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + " " + targetStr);
    }

    @Override
    public void visitSwitch(int opcode, int offset, int length, SwitchList cases, int padding) {
        int sz = cases.size();
        StringBuilder sb = new StringBuilder(sz * 20 + 100);
        sb.append(this.header(offset));
        if (padding != 0) {
            sb.append(" // padding: " + Hex.u4(padding));
        }
        sb.append('\n');
        for (int i = 0; i < sz; ++i) {
            sb.append("  ");
            sb.append(Hex.s4(cases.getValue(i)));
            sb.append(": ");
            sb.append(Hex.u2(cases.getTarget(i)));
            sb.append('\n');
        }
        sb.append("  default: ");
        sb.append(Hex.u2(cases.getDefaultTarget()));
        this.observer.parsed(this.bytes, offset, length, sb.toString());
    }

    @Override
    public void visitNewarray(int offset, int length, CstType cst, ArrayList<Constant> intVals) {
        String commentOrSpace = length == 1 ? " // " : " ";
        String typeName = cst.getClassType().getComponentType().toHuman();
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + commentOrSpace + typeName);
    }

    @Override
    public void setPreviousOffset(int offset) {
    }

    @Override
    public int getPreviousOffset() {
        return -1;
    }

    private String header(int offset) {
        int opcode = this.bytes.getUnsignedByte(offset);
        String name = ByteOps.opName(opcode);
        if (opcode == 196) {
            opcode = this.bytes.getUnsignedByte(offset + 1);
            name = name + " " + ByteOps.opName(opcode);
        }
        return Hex.u2(offset) + ": " + name;
    }

    private void visitLiteralInt(int opcode, int offset, int length, int value) {
        String commentOrSpace = length == 1 ? " // " : " ";
        opcode = this.bytes.getUnsignedByte(offset);
        String valueStr = length == 1 || opcode == 16 ? "#" + Hex.s1(value) : (opcode == 17 ? "#" + Hex.s2(value) : "#" + Hex.s4(value));
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + commentOrSpace + valueStr);
    }

    private void visitLiteralLong(int opcode, int offset, int length, long value) {
        String commentOrLit = length == 1 ? " // " : " #";
        String valueStr = length == 1 ? Hex.s1((int)value) : Hex.s8(value);
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + commentOrLit + valueStr);
    }

    private void visitLiteralFloat(int opcode, int offset, int length, int bits) {
        String optArg = length != 1 ? " #" + Hex.u4(bits) : "";
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + optArg + " // " + Float.intBitsToFloat(bits));
    }

    private void visitLiteralDouble(int opcode, int offset, int length, long bits) {
        String optArg = length != 1 ? " #" + Hex.u8(bits) : "";
        this.observer.parsed(this.bytes, offset, length, this.header(offset) + optArg + " // " + Double.longBitsToDouble(bits));
    }
}

