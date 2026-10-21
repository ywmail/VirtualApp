/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.science.stebcore;

import com.carlos.science.stebcore.IStep;
import com.carlos.science.stebcore.IStepController;
import java.io.Serializable;

public interface IStepInfo<T extends IStep>
extends Serializable {
    public static final long serialVersionUID = 1L;

    public long getStepId();

    public String getTitle();

    public T getStepImpl();

    public IStepController getController();
}

