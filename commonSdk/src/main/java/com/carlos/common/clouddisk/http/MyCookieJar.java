/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  okhttp3.Cookie
 *  okhttp3.CookieJar
 *  okhttp3.HttpUrl
 */
package com.carlos.common.clouddisk.http;

import android.util.Log;
import com.kook.librelease.StringFog;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar
implements CookieJar {
    static List<Cookie> cache = new ArrayList<Cookie>();

    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cache.addAll(cookies);
    }

    public List<Cookie> loadForRequest(HttpUrl url) {
        ArrayList<Cookie> invalidCookies = new ArrayList<Cookie>();
        ArrayList<Cookie> validCookies = new ArrayList<Cookie>();
        for (Cookie cookie : cache) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                invalidCookies.add(cookie);
                continue;
            }
            if (!cookie.matches(url)) continue;
            validCookies.add(cookie);
        }
        cache.removeAll(invalidCookies);
        return validCookies;
    }

    public static void resetCookies() {
        cache.clear();
    }

    public static boolean isCookiesActivation() {
        return !cache.isEmpty();
    }

    public static void print() {
        Log.d((String)"cookie", (String)"null");
        for (Cookie s : cache) {
            Log.d((String)"cookie", (String)s.toString());
        }
    }
}

