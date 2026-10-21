/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.graphics.Rect
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.SystemClock
 *  android.view.InputEvent
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.MotionEvent$PointerCoords
 *  android.view.MotionEvent$PointerProperties
 *  android.view.View
 *  androidx.annotation.RequiresApi
 */
package com.carlos.science.server;

import android.app.Service;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class EventService
extends Service
implements Runnable {
    String TAG = "EventService";
    Thread threadProcess;
    private Queue<Event> queue = new ConcurrentLinkedQueue<Event>();
    private boolean EVENT_FLAG = false;
    private final int EVENT_TYPE_EMULATOR_TOUCH = 2;
    private final int EVENT_TYPE_EMULATOR_CLICK = 3;
    private final int EVENT_TYPE_EMULATOR_KEY_CODE = 4;
    public static final int CONTROLLER_EVENT_FLAGS = 1107;
    Handler mHandler = new Handler();
    public static final int TOOL_TYPE_UNKNOWN = 0;
    public static final int TOOL_TYPE_FINGER = 1;
    public static final int TOOL_TYPE_STYLUS = 2;
    public static final int TOOL_TYPE_MOUSE = 3;
    public static final int TOOL_TYPE_ERASER = 4;
    boolean loopVirtualTouchEvent = false;
    Method recycleMethod = null;
    Class<?> clazzInputManager = null;
    Object objectInputManager = null;
    Method getInstanceMethod = null;
    Method injectInputEventMethod = null;
    static MotionEvent.PointerProperties properties;
    static MotionEvent.PointerProperties[] pointerProperties;
    static MotionEvent.PointerCoords[] pointerCoords;
    static MotionEvent.PointerCoords pressure;

    @RequiresApi(api=16)
    public void onCreate() {
        super.onCreate();
        this.threadProcess = new Thread(this);
        this.threadProcess.start();
    }

    public boolean emulatorClick(int centerX, int centerY) {
        if (this.isMainThread()) {
            HVLog.i(this.TAG, "非主线程操作");
            Event event = new Event(3);
            event.centerx = centerX;
            event.centery = centerY;
            this.addEvent(event);
            while (this.EVENT_FLAG) {
                this.sleep(200L);
            }
            return true;
        }
        return this.virtualClick(centerX, centerY);
    }

    public boolean emulatorKey(int key_code) {
        if (this.isMainThread()) {
            Event event = new Event(4);
            event.key_code = key_code;
            this.addEvent(event);
            while (this.EVENT_FLAG) {
                this.sleep(200L);
            }
            return true;
        }
        return this.sendKeyEvent(4098, key_code, false);
    }

    public int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    protected boolean clickableView(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        int width = view.getWidth();
        int height = view.getHeight();
        int centerX = loc[0] + width / 2;
        int centerY = loc[1] + height / 2;
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        if (!rect.contains(centerX, centerY)) {
            centerX = rect.centerX();
            centerY = rect.centerY();
            if (centerX < 0 || centerY < 0) {
                HVLog.e(this.TAG, " 当前view 不在屏幕范围中可见,点击失败");
                return false;
            }
        }
        return this.emulatorClick(centerX, centerY);
    }

    public boolean emulatorTouch(int fromX, int fromY, int toX, int toY, boolean direction) {
        if (this.isMainThread()) {
            Event event = new Event(2);
            event.fromX = fromX;
            event.fromY = fromY;
            event.toX = toX;
            event.toY = toY;
            event.direction = direction;
            this.addEvent(event);
            while (this.EVENT_FLAG) {
                this.sleep(200L);
            }
            return true;
        }
        return this.virtualTouch(fromX, fromY, toX, toY, direction);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Queue<Event> queue = this.queue;
            synchronized (queue) {
                try {
                    if (null == this.queue || this.queue.size() == 0) {
                        this.queue.wait();
                    }
                    Event event = this.queue.poll();
                    if (event.action_type == 3) {
                        this.virtualClick(event.centerx, event.centery);
                    } else if (event.action_type == 2) {
                        this.virtualTouch(event.fromX, event.fromY, event.toX, event.toY, event.direction);
                    } else if (event.action_type == 4) {
                        this.sendKeyEvent(4098, event.key_code, false);
                    }
                    this.EVENT_FLAG = false;
                }
                catch (Exception e) {
                    HVLog.i(this.TAG, "exception e:" + e.toString());
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addEvent(Event event) {
        this.EVENT_FLAG = true;
        Queue<Event> queue = this.queue;
        synchronized (queue) {
            this.queue.add(event);
            this.queue.notifyAll();
        }
    }

    private boolean sendKeyEvent(int inputSource, int keyCode, boolean longpress) {
        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, inputSource);
        this.invokeInjectInputEventMethod((InputEvent)down, 2);
        if (longpress) {
            KeyEvent down_long = new KeyEvent(now, now, 0, keyCode, 1, 0, -1, 0, 128, inputSource);
            this.invokeInjectInputEventMethod((InputEvent)down_long, 2);
        }
        KeyEvent up = new KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, inputSource);
        this.invokeInjectInputEventMethod((InputEvent)up, 2);
        return true;
    }

    private boolean virtualClick(int centerX, int centerY) {
        long currentTimeMillis = System.currentTimeMillis();
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent motionEvent = EventService.makeMotionEvent(downTime, eventTime, 0, centerX, centerY);
        this.invokeInjectInputEventMethod((InputEvent)motionEvent, 1);
        motionEvent = EventService.makeMotionEvent(downTime, eventTime + 2L, 1, centerX, centerY);
        this.invokeInjectInputEventMethod((InputEvent)motionEvent, 2);
        HVLog.e(this.TAG, "ControllerServiceBase virtualClick 耗时 :" + (System.currentTimeMillis() - currentTimeMillis) + "    centerX:" + centerX + "    centerY:" + centerY + "    是否在主线程  " + (Looper.getMainLooper() == Looper.myLooper()));
        return true;
    }

    public boolean isMainThread() {
        boolean isMainLooper = Looper.getMainLooper() == Looper.myLooper();
        HVLog.d(this.TAG, "isMainLooper def return true:" + isMainLooper);
        return true;
    }

    protected boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) {
        try {
            int step = 20;
            int y = 0;
            y = direction ? fromY : toY;
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();
            MotionEvent motionEvent = null;
            motionEvent = EventService.makeMotionEvent(downTime, eventTime, 0, fromX, y);
            this.invokeInjectInputEventMethod((InputEvent)motionEvent, 1);
            int stepCount = Math.abs((fromY - toY) / step);
            for (int i = 0; i < stepCount; ++i) {
                y = direction ? (y -= step) : (y += step);
                motionEvent = EventService.makeMotionEvent(downTime, eventTime += 20L, 2, fromX, y);
                this.invokeInjectInputEventMethod((InputEvent)motionEvent, 2);
            }
            motionEvent = EventService.makeMotionEvent(downTime, eventTime, 1, toX, y);
            this.invokeInjectInputEventMethod((InputEvent)motionEvent, 2);
            return true;
        }
        catch (Exception e) {
            HVLog.i(this.TAG, "virtualTouch exception e:" + e.toString());
            return false;
        }
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            HVLog.i(this.TAG, "exception e:" + e.toString());
        }
    }

    private void invokeInjectInputEventMethod(InputEvent event, int mode) {
        boolean isException = false;
        try {
            if (this.clazzInputManager == null) {
                this.clazzInputManager = Class.forName("android.hardware.input.InputManager");
            }
            if (this.getInstanceMethod == null) {
                this.getInstanceMethod = this.clazzInputManager.getMethod("getInstance", new Class[0]);
                this.objectInputManager = this.getInstanceMethod.invoke(this.clazzInputManager, new Object[0]);
            }
            if (this.injectInputEventMethod == null) {
                this.injectInputEventMethod = this.clazzInputManager.getMethod("injectInputEvent", InputEvent.class, Integer.TYPE);
            }
            if (this.objectInputManager != null) {
                this.injectInputEventMethod.invoke(this.objectInputManager, event, mode);
                this.recycleMethod = event.getClass().getMethod("recycle", new Class[0]);
                this.recycleMethod.invoke(event, new Object[0]);
            } else {
                HVLog.e(this.TAG, "objectInputManager is null");
            }
        }
        catch (IllegalAccessException e) {
            isException = true;
            HVLog.e(this.TAG, " IllegalAccessException " + e.toString());
        }
        catch (InvocationTargetException e) {
            isException = true;
            HVLog.e(this.TAG, " InvocationTargetException " + e.toString());
        }
        catch (NoSuchMethodException e) {
            isException = true;
            HVLog.e(this.TAG, " NoSuchMethodException " + e.toString());
        }
        catch (ClassNotFoundException e) {
            isException = true;
            HVLog.e(this.TAG, " ClassNotFoundException " + e.toString());
        }
        catch (SecurityException e) {
            isException = true;
            HVLog.e(this.TAG, " SecurityException " + e.toString());
        }
        if (isException) {
            HVLog.e(this.TAG, " 异常的点的event :" + event);
        }
    }

    public static MotionEvent makeMotionEvent(long downTime, long eventTime, int action, float x, float y) {
        int pointerCount = 1;
        if (properties == null) {
            properties = new MotionEvent.PointerProperties();
        }
        EventService.properties.id = 1;
        EventService.properties.toolType = 1;
        if (pointerProperties == null) {
            pointerProperties = new MotionEvent.PointerProperties[1];
        }
        EventService.pointerProperties[0] = properties;
        if (pointerCoords == null) {
            pointerCoords = new MotionEvent.PointerCoords[1];
        }
        if (pressure == null) {
            pressure = new MotionEvent.PointerCoords();
        }
        EventService.pressure.x = x;
        EventService.pressure.y = y;
        EventService.pressure.pressure = 1000.0f;
        EventService.pressure.size = 100.0f;
        EventService.pointerCoords[0] = pressure;
        int metaState = 0;
        int buttonState = 0;
        float xPrecision = 1.3342593f;
        float yPrecision = 1.3338541f;
        int deviceId = 4;
        int edgeFlags = 1107;
        int source = 4098;
        int flags = 0;
        MotionEvent motionEvent = MotionEvent.obtain((long)downTime, (long)eventTime, (int)action, (int)pointerCount, (MotionEvent.PointerProperties[])pointerProperties, (MotionEvent.PointerCoords[])pointerCoords, (int)metaState, (int)buttonState, (float)xPrecision, (float)yPrecision, (int)deviceId, (int)edgeFlags, (int)source, (int)flags);
        motionEvent.setSource(source);
        return motionEvent;
    }

    private static final float lerp(float a, float b, float alpha) {
        return (b - a) * alpha + a;
    }

    protected void sendSwipe(int inputSource, float x1, float y1, float x2, float y2, int duration) {
        if (duration < 0) {
            duration = 300;
        }
        long now = SystemClock.uptimeMillis();
        this.injectMotionEvent(inputSource, 0, now, x1, y1, 1.0f);
        long startTime = now;
        long endTime = startTime + (long)duration;
        while (now < endTime) {
            long elapsedTime = now - startTime;
            float alpha = (float)elapsedTime / (float)duration;
            this.injectMotionEvent(inputSource, 2, now, EventService.lerp(x1, x2, alpha), EventService.lerp(y1, y2, alpha), 1.0f);
            now = SystemClock.uptimeMillis();
        }
        this.injectMotionEvent(inputSource, 1, now, x2, y2, 0.0f);
    }

    private void injectMotionEvent(int inputSource, int action, long when, float x, float y, float pressure) {
        float DEFAULT_SIZE = 1.0f;
        boolean DEFAULT_META_STATE = false;
        float DEFAULT_PRECISION_X = 1.0f;
        float DEFAULT_PRECISION_Y = 1.0f;
        boolean DEFAULT_DEVICE_ID = false;
        boolean DEFAULT_EDGE_FLAGS = false;
        MotionEvent event = MotionEvent.obtain((long)when, (long)when, (int)action, (float)x, (float)y, (float)pressure, (float)1.0f, (int)0, (float)1.0f, (float)1.0f, (int)0, (int)0);
        event.setSource(inputSource);
        this.invokeInjectInputEventMethod((InputEvent)event, 2);
    }

    private class Event {
        int key_code;
        int action_type;
        int fromX;
        int fromY;
        int toX;
        int toY;
        int centerx;
        int centery;
        boolean direction;

        public Event(int action_type) {
            this.action_type = action_type;
        }
    }
}

