//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.carlos.common.imagepicker.util;

public final class CubicEasing {
    public CubicEasing() {
    }

    public static float easeOut(float time, float start, float end, float duration) {
        return end * ((time = time / duration - 1.0F) * time * time + 1.0F) + start;
    }

    public static float easeIn(float time, float start, float end, float duration) {
        return end * (time /= duration) * time * time + start;
    }

    public static float easeInOut(float time, float start, float end, float duration) {
        return (time /= duration / 2.0F) < 1.0F ? end / 2.0F * time * time * time + start : end / 2.0F * ((time -= 2.0F) * time * time + 2.0F) + start;
    }
}
