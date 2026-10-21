/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

public final class Code {
    private final int registersSize;
    private final int insSize;
    private final int outsSize;
    private final int debugInfoOffset;
    private final short[] instructions;
    private final Try[] tries;
    private final CatchHandler[] catchHandlers;

    public Code(int registersSize, int insSize, int outsSize, int debugInfoOffset, short[] instructions, Try[] tries, CatchHandler[] catchHandlers) {
        this.registersSize = registersSize;
        this.insSize = insSize;
        this.outsSize = outsSize;
        this.debugInfoOffset = debugInfoOffset;
        this.instructions = instructions;
        this.tries = tries;
        this.catchHandlers = catchHandlers;
    }

    public int getRegistersSize() {
        return this.registersSize;
    }

    public int getInsSize() {
        return this.insSize;
    }

    public int getOutsSize() {
        return this.outsSize;
    }

    public int getDebugInfoOffset() {
        return this.debugInfoOffset;
    }

    public short[] getInstructions() {
        return this.instructions;
    }

    public Try[] getTries() {
        return this.tries;
    }

    public CatchHandler[] getCatchHandlers() {
        return this.catchHandlers;
    }

    public static class CatchHandler {
        final int[] typeIndexes;
        final int[] addresses;
        final int catchAllAddress;
        final int offset;

        public CatchHandler(int[] typeIndexes, int[] addresses, int catchAllAddress, int offset) {
            this.typeIndexes = typeIndexes;
            this.addresses = addresses;
            this.catchAllAddress = catchAllAddress;
            this.offset = offset;
        }

        public int[] getTypeIndexes() {
            return this.typeIndexes;
        }

        public int[] getAddresses() {
            return this.addresses;
        }

        public int getCatchAllAddress() {
            return this.catchAllAddress;
        }

        public int getOffset() {
            return this.offset;
        }
    }

    public static class Try {
        final int startAddress;
        final int instructionCount;
        final int catchHandlerIndex;

        Try(int startAddress, int instructionCount, int catchHandlerIndex) {
            this.startAddress = startAddress;
            this.instructionCount = instructionCount;
            this.catchHandlerIndex = catchHandlerIndex;
        }

        public int getStartAddress() {
            return this.startAddress;
        }

        public int getInstructionCount() {
            return this.instructionCount;
        }

        public int getCatchHandlerIndex() {
            return this.catchHandlerIndex;
        }
    }
}

