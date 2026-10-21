//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.client.hook.proxies.audio;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import java.lang.reflect.Method;
import mirror.android.media.IAudioService.Stub;

public class AudioManagerStub extends BinderInvocationProxy {
    public AudioManagerStub() {
        super(Stub.asInterface, "audio");
    }

    protected void onBindMethods() {
        super.onBindMethods();
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("adjustVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("adjustLocalOrRemoteStreamVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("adjustSuggestedStreamVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("adjustStreamVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("adjustMasterVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setStreamVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setMasterVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setMicrophoneMute") {
            public Object call(Object who, Method method, Object... args) throws Throwable {
                replaceLastUserId(args);
                return super.call(who, method, args);
            }
        });
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setRingerModeExternal"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setRingerModeInternal"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setMode"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("avrcpSupportsAbsoluteVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("abandonAudioFocus"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("requestAudioFocus"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setWiredDeviceConnectionState"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setSpeakerphoneOn"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("setBluetoothScoOn"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("stopBluetoothSco"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("startBluetoothSco"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("disableSafeMediaVolume"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("registerRemoteControlClient"));
        this.addMethodProxy(new ReplaceLastPkgMethodProxy("unregisterAudioFocusClient"));
    }
}
