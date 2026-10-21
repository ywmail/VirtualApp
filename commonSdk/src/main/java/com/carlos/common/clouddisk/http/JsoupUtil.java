/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jsoup.Jsoup
 *  org.jsoup.nodes.Document
 *  org.jsoup.nodes.Element
 *  org.jsoup.safety.Safelist
 */
package com.carlos.common.clouddisk.http;

import com.kook.librelease.StringFog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class JsoupUtil {
    public static String LoginInfo(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.none());
    }

    public static String getText(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.none());
    }

    public static String getSimpleHtml(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.simpleText());
    }

    public static String getBasicHtml(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.basic());
    }

    public static String getBasicHtmlandimage(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.basicWithImages());
    }

    public static String getFullHtml(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean((String)html, (Safelist)Safelist.relaxed());
    }

    public static String clearTags(String html, String ... tags) {
        Safelist wl = new Safelist();
        return Jsoup.clean((String)html, (Safelist)wl.addTags(tags));
    }

    public static String getImgSrc(String html) {
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parseBodyFragment((String)html);
        Element image = doc.select("img").first();
        return image == null ? null : image.attr("src");
    }
}

