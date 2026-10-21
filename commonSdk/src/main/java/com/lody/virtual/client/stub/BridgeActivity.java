/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Intent
 *  android.os.Bundle
 */
package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lody.virtual.StringFog;

public class BridgeActivity
extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.finish();
        Intent intent = this.getIntent();
        if (intent != null) {
            Intent targetIntent = (Intent)intent.getParcelableExtra("_VA_|_intent_");
            Bundle targetOptions = (Bundle)intent.getParcelableExtra("_VA_|_bundle_");
            if (targetIntent != null && targetOptions != null) {
                this.startActivity(targetIntent, targetOptions);
            }
        }
    }
}

