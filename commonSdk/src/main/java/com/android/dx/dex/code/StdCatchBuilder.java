/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.dex.code;

import com.android.dx.dex.code.BlockAddresses;
import com.android.dx.dex.code.CatchBuilder;
import com.android.dx.dex.code.CatchHandlerList;
import com.android.dx.dex.code.CatchTable;
import com.android.dx.dex.code.CodeAddress;
import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.BasicBlockList;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.HashSet;

public final class StdCatchBuilder
implements CatchBuilder {
    private static final int MAX_CATCH_RANGE = 65535;
    private final RopMethod method;
    private final int[] order;
    private final BlockAddresses addresses;

    public StdCatchBuilder(RopMethod method, int[] order, BlockAddresses addresses) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (order == null) {
            throw new NullPointerException("order == null");
        }
        if (addresses == null) {
            throw new NullPointerException("addresses == null");
        }
        this.method = method;
        this.order = order;
        this.addresses = addresses;
    }

    @Override
    public CatchTable build() {
        return StdCatchBuilder.build(this.method, this.order, this.addresses);
    }

    @Override
    public boolean hasAnyCatches() {
        BasicBlockList blocks = this.method.getBlocks();
        int size = blocks.size();
        for (int i = 0; i < size; ++i) {
            BasicBlock block = blocks.get(i);
            TypeList catches = block.getLastInsn().getCatches();
            if (catches.size() == 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public HashSet<Type> getCatchTypes() {
        HashSet<Type> result = new HashSet<Type>(20);
        BasicBlockList blocks = this.method.getBlocks();
        int size = blocks.size();
        for (int i = 0; i < size; ++i) {
            BasicBlock block = blocks.get(i);
            TypeList catches = block.getLastInsn().getCatches();
            int catchSize = catches.size();
            for (int j = 0; j < catchSize; ++j) {
                result.add(catches.getType(j));
            }
        }
        return result;
    }

    public static CatchTable build(RopMethod method, int[] order, BlockAddresses addresses) {
        int resultSz;
        int len = order.length;
        BasicBlockList blocks = method.getBlocks();
        ArrayList<CatchTable.Entry> resultList = new ArrayList<CatchTable.Entry>(len);
        CatchHandlerList currentHandlers = CatchHandlerList.EMPTY;
        BasicBlock currentStartBlock = null;
        BasicBlock currentEndBlock = null;
        for (int i = 0; i < len; ++i) {
            BasicBlock block = blocks.labelToBlock(order[i]);
            if (!block.canThrow()) continue;
            CatchHandlerList handlers = StdCatchBuilder.handlersFor(block, addresses);
            if (currentHandlers.size() == 0) {
                currentStartBlock = block;
                currentEndBlock = block;
                currentHandlers = handlers;
                continue;
            }
            if (currentHandlers.equals(handlers) && StdCatchBuilder.rangeIsValid(currentStartBlock, block, addresses)) {
                currentEndBlock = block;
                continue;
            }
            if (currentHandlers.size() != 0) {
                CatchTable.Entry entry = StdCatchBuilder.makeEntry(currentStartBlock, currentEndBlock, currentHandlers, addresses);
                resultList.add(entry);
            }
            currentStartBlock = block;
            currentEndBlock = block;
            currentHandlers = handlers;
        }
        if (currentHandlers.size() != 0) {
            CatchTable.Entry entry = StdCatchBuilder.makeEntry(currentStartBlock, currentEndBlock, currentHandlers, addresses);
            resultList.add(entry);
        }
        if ((resultSz = resultList.size()) == 0) {
            return CatchTable.EMPTY;
        }
        CatchTable result = new CatchTable(resultSz);
        for (int i = 0; i < resultSz; ++i) {
            result.set(i, (CatchTable.Entry)resultList.get(i));
        }
        result.setImmutable();
        return result;
    }

    private static CatchHandlerList handlersFor(BasicBlock block, BlockAddresses addresses) {
        IntList successors = block.getSuccessors();
        int succSize = successors.size();
        int primary = block.getPrimarySuccessor();
        TypeList catches = block.getLastInsn().getCatches();
        int catchSize = catches.size();
        if (catchSize == 0) {
            return CatchHandlerList.EMPTY;
        }
        if (primary == -1 && succSize != catchSize || primary != -1 && (succSize != catchSize + 1 || primary != successors.get(catchSize))) {
            throw new RuntimeException("shouldn't happen: weird successors list");
        }
        for (int i = 0; i < catchSize; ++i) {
            Type type = catches.getType(i);
            if (!type.equals(Type.OBJECT)) continue;
            catchSize = i + 1;
            break;
        }
        CatchHandlerList result = new CatchHandlerList(catchSize);
        for (int i = 0; i < catchSize; ++i) {
            CstType oneType = new CstType(catches.getType(i));
            CodeAddress oneHandler = addresses.getStart(successors.get(i));
            result.set(i, oneType, oneHandler.getAddress());
        }
        result.setImmutable();
        return result;
    }

    private static CatchTable.Entry makeEntry(BasicBlock start, BasicBlock end, CatchHandlerList handlers, BlockAddresses addresses) {
        CodeAddress startAddress = addresses.getLast(start);
        CodeAddress endAddress = addresses.getEnd(end);
        return new CatchTable.Entry(startAddress.getAddress(), endAddress.getAddress(), handlers);
    }

    private static boolean rangeIsValid(BasicBlock start, BasicBlock end, BlockAddresses addresses) {
        if (start == null) {
            throw new NullPointerException("start == null");
        }
        if (end == null) {
            throw new NullPointerException("end == null");
        }
        int startAddress = addresses.getLast(start).getAddress();
        int endAddress = addresses.getEnd(end).getAddress();
        return endAddress - startAddress <= 65535;
    }
}

