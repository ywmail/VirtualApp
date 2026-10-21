/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Instrumentation
 *  android.app.Instrumentation$ActivityResult
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.IBinder
 */
package mirror.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import com.swift.sandhook.annotation.MethodReflectParams;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.android.app.Activity;

public class Instrumentation {
    public static Class<?> TYPE = RefClass.load(Instrumentation.class, android.app.Instrumentation.class);
    @MethodReflectParams(value={"android.app.ActivityThread"})
    public static RefMethod basicInit;
    public static RefObject mThread;
    @MethodParams(value={Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class})
    public static RefMethod<android.app.Instrumentation.ActivityResult> execStartActivity;
}

