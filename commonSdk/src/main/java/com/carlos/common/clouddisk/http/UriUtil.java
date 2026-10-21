/*
 * Decompiled with CFR 0.152.
 */
package com.carlos.common.clouddisk.http;

import com.kook.librelease.StringFog;
import java.util.ArrayList;
import java.util.List;

public class UriUtil {
    public static final List<String> HttpPaths;
    public static final String LOGIN;
    public static final String TASK;
    public static final String UPLOADFILE;
    public static final String SHAREHEAD;
    public static final String DOWNFILEHEAD;
    public static final String DOWNFILEPATH;
    public static final String MYGITHUB = "";

    static {
        LOGIN = "https://pc.woozooo.com/mlogin.php";
        TASK = "https://pc.woozooo.com/doupload.php";
        UPLOADFILE = "https://pc.woozooo.com/fileup.php";
        SHAREHEAD = "https://wws.lanzouj.com/";
        DOWNFILEHEAD = "https://vip.d0.baidupan.com/file/";
        DOWNFILEPATH = "/file/";
        HttpPaths = new ArrayList<String>();
    }
}

