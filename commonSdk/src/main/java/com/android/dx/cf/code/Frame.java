/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.cf.code.ExecutionStack;
import com.android.dx.cf.code.LocalsArray;
import com.android.dx.cf.code.LocalsArraySet;
import com.android.dx.cf.code.OneLocalsArray;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.util.IntList;

public final class Frame {
    private final LocalsArray locals;
    private final ExecutionStack stack;
    private final IntList subroutines;

    private Frame(LocalsArray locals, ExecutionStack stack) {
        this(locals, stack, IntList.EMPTY);
    }

    private Frame(LocalsArray locals, ExecutionStack stack, IntList subroutines) {
        if (locals == null) {
            throw new NullPointerException("locals == null");
        }
        if (stack == null) {
            throw new NullPointerException("stack == null");
        }
        subroutines.throwIfMutable();
        this.locals = locals;
        this.stack = stack;
        this.subroutines = subroutines;
    }

    public Frame(int maxLocals, int maxStack) {
        this(new OneLocalsArray(maxLocals), new ExecutionStack(maxStack));
    }

    public Frame copy() {
        return new Frame(this.locals.copy(), this.stack.copy(), this.subroutines);
    }

    public void setImmutable() {
        this.locals.setImmutable();
        this.stack.setImmutable();
    }

    public void makeInitialized(Type type) {
        this.locals.makeInitialized(type);
        this.stack.makeInitialized(type);
    }

    public LocalsArray getLocals() {
        return this.locals;
    }

    public ExecutionStack getStack() {
        return this.stack;
    }

    public IntList getSubroutines() {
        return this.subroutines;
    }

    public void initializeWithParameters(StdTypeList params) {
        int at = 0;
        int sz = params.size();
        for (int i = 0; i < sz; ++i) {
            Type one = params.get(i);
            this.locals.set(at, one);
            at += one.getCategory();
        }
    }

    public Frame subFrameForLabel(int startLabel, int subLabel) {
        IntList newSubroutines;
        LocalsArray subLocals = null;
        if (this.locals instanceof LocalsArraySet) {
            subLocals = ((LocalsArraySet)this.locals).subArrayForLabel(subLabel);
        }
        try {
            newSubroutines = this.subroutines.mutableCopy();
            if (newSubroutines.pop() != startLabel) {
                throw new RuntimeException("returning from invalid subroutine");
            }
            newSubroutines.setImmutable();
        }
        catch (IndexOutOfBoundsException ex) {
            throw new RuntimeException("returning from invalid subroutine");
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("can't return from non-subroutine");
        }
        return subLocals == null ? null : new Frame(subLocals, this.stack, newSubroutines);
    }

    public Frame mergeWith(Frame other) {
        LocalsArray resultLocals = this.getLocals().merge(other.getLocals());
        ExecutionStack resultStack = this.getStack().merge(other.getStack());
        IntList resultSubroutines = this.mergeSubroutineLists(other.subroutines);
        if ((resultLocals = Frame.adjustLocalsForSubroutines(resultLocals, resultSubroutines)) == this.getLocals() && resultStack == this.getStack() && this.subroutines == resultSubroutines) {
            return this;
        }
        return new Frame(resultLocals, resultStack, resultSubroutines);
    }

    private IntList mergeSubroutineLists(IntList otherSubroutines) {
        if (this.subroutines.equals(otherSubroutines)) {
            return this.subroutines;
        }
        IntList resultSubroutines = new IntList();
        int szSubroutines = this.subroutines.size();
        int szOthers = otherSubroutines.size();
        for (int i = 0; i < szSubroutines && i < szOthers && this.subroutines.get(i) == otherSubroutines.get(i); ++i) {
            resultSubroutines.add(i);
        }
        resultSubroutines.setImmutable();
        return resultSubroutines;
    }

    private static LocalsArray adjustLocalsForSubroutines(LocalsArray locals, IntList subroutines) {
        if (!(locals instanceof LocalsArraySet)) {
            return locals;
        }
        LocalsArraySet laSet = (LocalsArraySet)locals;
        if (subroutines.size() == 0) {
            return laSet.getPrimary();
        }
        return laSet;
    }

    public Frame mergeWithSubroutineCaller(Frame other, int subLabel, int predLabel) {
        IntList resultSubroutines;
        LocalsArraySet resultLocals = this.getLocals().mergeWithSubroutineCaller(other.getLocals(), predLabel);
        ExecutionStack resultStack = this.getStack().merge(other.getStack());
        IntList newOtherSubroutines = other.subroutines.mutableCopy();
        newOtherSubroutines.add(subLabel);
        newOtherSubroutines.setImmutable();
        if (resultLocals == this.getLocals() && resultStack == this.getStack() && this.subroutines.equals(newOtherSubroutines)) {
            return this;
        }
        if (this.subroutines.equals(newOtherSubroutines)) {
            resultSubroutines = this.subroutines;
        } else {
            IntList nonResultSubroutines;
            if (this.subroutines.size() > newOtherSubroutines.size()) {
                resultSubroutines = this.subroutines;
                nonResultSubroutines = newOtherSubroutines;
            } else {
                resultSubroutines = newOtherSubroutines;
                nonResultSubroutines = this.subroutines;
            }
            int szResult = resultSubroutines.size();
            int szNonResult = nonResultSubroutines.size();
            for (int i = szNonResult - 1; i >= 0; --i) {
                if (nonResultSubroutines.get(i) == resultSubroutines.get(i + (szResult - szNonResult))) continue;
                throw new RuntimeException("Incompatible merged subroutines");
            }
        }
        return new Frame(resultLocals, resultStack, resultSubroutines);
    }

    public Frame makeNewSubroutineStartFrame(int subLabel, int callerLabel) {
        IntList newSubroutines = this.subroutines.mutableCopy();
        newSubroutines.add(subLabel);
        Frame newFrame = new Frame(this.locals.getPrimary(), this.stack, IntList.makeImmutable(subLabel));
        return newFrame.mergeWithSubroutineCaller(this, subLabel, callerLabel);
    }

    public Frame makeExceptionHandlerStartFrame(CstType exceptionClass) {
        ExecutionStack newStack = this.getStack().copy();
        newStack.clear();
        newStack.push(exceptionClass);
        return new Frame(this.getLocals(), newStack, this.subroutines);
    }

    public void annotate(ExceptionWithContext ex) {
        this.locals.annotate(ex);
        this.stack.annotate(ex);
    }
}

