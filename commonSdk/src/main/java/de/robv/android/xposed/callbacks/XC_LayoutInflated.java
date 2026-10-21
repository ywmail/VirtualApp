/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package de.robv.android.xposed.callbacks;

import android.view.View;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.IXUnhook;
import de.robv.android.xposed.callbacks.XCallback;

public abstract class XC_LayoutInflated
extends XCallback {
    public XC_LayoutInflated() {
    }

    public XC_LayoutInflated(int priority) {
        super(priority);
    }

    @Override
    protected void call(XCallback.Param param) throws Throwable {
        if (param instanceof LayoutInflatedParam) {
            this.handleLayoutInflated((LayoutInflatedParam)param);
        }
    }

    public abstract void handleLayoutInflated(LayoutInflatedParam var1) throws Throwable;

    public class Unhook
    implements IXUnhook<XC_LayoutInflated> {
        private final String resDir;
        private final int id;

        public Unhook(String resDir, int id2) {
            this.resDir = resDir;
            this.id = id2;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public XC_LayoutInflated getCallback() {
            return XC_LayoutInflated.this;
        }

        @Override
        public void unhook() {
        }
    }

    public static final class LayoutInflatedParam
    extends XCallback.Param {
        public View view;

        public LayoutInflatedParam(XposedBridge.CopyOnWriteSortedSet<XC_LayoutInflated> callbacks) {
            super(callbacks);
        }
    }
}

