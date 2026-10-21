/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 *  android.widget.Scroller
 */
package com.carlos.science.runner;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import com.carlos.science.runner.ICarrier;

public class ScrollRunner
implements Runnable {
    private Scroller mScroller;
    private ICarrier mCarrier;
    private int mDuration = 250;
    private int lastX;
    private int lastY;

    public ScrollRunner(ICarrier carrier) {
        this.mCarrier = carrier;
        this.mScroller = new Scroller(carrier.getContext(), (Interpolator)new LinearInterpolator());
    }

    public void start(int dx, int dy) {
        this.start(dx, dy, this.mDuration);
    }

    public void start(int dx, int dy, int duration) {
        this.start(0, 0, dx, dy, duration);
    }

    public void start(int startX, int startY, int dx, int dy) {
        this.start(startX, startY, dx, dy, this.mDuration);
    }

    public void start(int startX, int startY, int dx, int dy, int duration) {
        this.mDuration = duration;
        this.mScroller.startScroll(startX, startY, dx, dy, duration);
        this.mCarrier.removeCallbacks(this);
        this.mCarrier.post(this);
        this.lastX = startX;
        this.lastY = startY;
    }

    public boolean isRunning() {
        return !this.mScroller.isFinished();
    }

    @Override
    public void run() {
        if (this.mScroller.computeScrollOffset()) {
            int currentX = this.mScroller.getCurrX();
            int currentY = this.mScroller.getCurrY();
            this.mCarrier.onMove(this.lastX, this.lastY, currentX, currentY);
            this.mCarrier.post(this);
            this.lastX = currentX;
            this.lastY = currentY;
        } else {
            this.mCarrier.removeCallbacks(this);
            this.mCarrier.onDone();
        }
    }
}

