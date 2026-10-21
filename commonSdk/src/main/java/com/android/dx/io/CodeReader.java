/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.io;

import com.android.dex.DexException;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.io.instructions.DecodedInstruction;

public final class CodeReader {
    private Visitor fallbackVisitor = null;
    private Visitor stringVisitor = null;
    private Visitor typeVisitor = null;
    private Visitor fieldVisitor = null;
    private Visitor methodVisitor = null;
    private Visitor methodAndProtoVisitor = null;
    private Visitor callSiteVisitor = null;

    public void setAllVisitors(Visitor visitor) {
        this.fallbackVisitor = visitor;
        this.stringVisitor = visitor;
        this.typeVisitor = visitor;
        this.fieldVisitor = visitor;
        this.methodVisitor = visitor;
        this.methodAndProtoVisitor = visitor;
        this.callSiteVisitor = visitor;
    }

    public void setFallbackVisitor(Visitor visitor) {
        this.fallbackVisitor = visitor;
    }

    public void setStringVisitor(Visitor visitor) {
        this.stringVisitor = visitor;
    }

    public void setTypeVisitor(Visitor visitor) {
        this.typeVisitor = visitor;
    }

    public void setFieldVisitor(Visitor visitor) {
        this.fieldVisitor = visitor;
    }

    public void setMethodVisitor(Visitor visitor) {
        this.methodVisitor = visitor;
    }

    public void setMethodAndProtoVisitor(Visitor visitor) {
        this.methodAndProtoVisitor = visitor;
    }

    public void setCallSiteVisitor(Visitor visitor) {
        this.callSiteVisitor = visitor;
    }

    public void visitAll(DecodedInstruction[] decodedInstructions) throws DexException {
        int size = decodedInstructions.length;
        for (int i = 0; i < size; ++i) {
            DecodedInstruction one = decodedInstructions[i];
            if (one == null) continue;
            this.callVisit(decodedInstructions, one);
        }
    }

    public void visitAll(short[] encodedInstructions) throws DexException {
        DecodedInstruction[] decodedInstructions = DecodedInstruction.decodeAll(encodedInstructions);
        this.visitAll(decodedInstructions);
    }

    private void callVisit(DecodedInstruction[] all, DecodedInstruction one) {
        Visitor visitor = null;
        switch (OpcodeInfo.getIndexType(one.getOpcode())) {
            case STRING_REF: {
                visitor = this.stringVisitor;
                break;
            }
            case TYPE_REF: {
                visitor = this.typeVisitor;
                break;
            }
            case FIELD_REF: {
                visitor = this.fieldVisitor;
                break;
            }
            case METHOD_REF: {
                visitor = this.methodVisitor;
                break;
            }
            case METHOD_AND_PROTO_REF: {
                visitor = this.methodAndProtoVisitor;
                break;
            }
            case CALL_SITE_REF: {
                visitor = this.callSiteVisitor;
            }
        }
        if (visitor == null) {
            visitor = this.fallbackVisitor;
        }
        if (visitor != null) {
            visitor.visit(all, one);
        }
    }

    public static interface Visitor {
        public void visit(DecodedInstruction[] var1, DecodedInstruction var2);
    }
}

