/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.provider.BaseColumns
 */
package com.carlos.common.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import com.carlos.common.provider.ScorpionProvider;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;

public class ToolsSettings {

    public static final class ServerInfo
    extends Base
    implements BaseDataColumns {
        public static int ACCOUNT_TYPE_PHONE;
        public static int ACCOUNT_TYPE_ID;
        public static final String SERVER_INFO;
        public static final String SERVER_IP;
        public static final String SERVER_PORT;

        public static final Uri getContentUri(String packageName) {
            String URI2 = "content://" + ScorpionProvider.getAUTHORITY(packageName) + "/%s?" + "notify" + "=true";
            return Uri.parse((String)String.format(URI2, SERVER_INFO));
        }

        public static Uri getContentUri(String packageName, long id2, boolean notify) {
            return Uri.parse((String)("content://" + ScorpionProvider.getAUTHORITY(packageName) + "/" + SERVER_INFO + "/" + id2 + "?" + "notify" + "=" + notify));
        }

        public static String onCreateTable() {
            HVLog.d(" onCreateTable :");
            return "CREATE TABLE serverinfo (_id INTEGER PRIMARY KEY,serverIp TEXT,serverPort TEXT);";
        }

        static {
            SERVER_INFO = "serverinfo";
            SERVER_IP = "serverIp";
            SERVER_PORT = "serverPort";
            ACCOUNT_TYPE_PHONE = 1;
            ACCOUNT_TYPE_ID = 2;
        }
    }

    public static interface BaseDataColumns
    extends BaseColumns {
    }

    public static abstract class Base {
    }
}

