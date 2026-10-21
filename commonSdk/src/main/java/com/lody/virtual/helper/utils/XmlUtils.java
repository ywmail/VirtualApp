/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package com.lody.virtual.helper.utils;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlUtils {
    public static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        int outerDepth = parser.getDepth();
        while ((type = parser.next()) != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
        }
    }

    public static void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) break;
        } while (type != 1);
    }
}

