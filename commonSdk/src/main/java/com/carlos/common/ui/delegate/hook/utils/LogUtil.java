/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Log
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.carlos.common.ui.delegate.hook.utils;

import android.text.TextUtils;
import android.util.Log;
import com.kook.librelease.StringFog;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogUtil {
    private static String TAG = "ghost";
    private static boolean LOG_DEBUG = true;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;
    private static final int JSON = 8;
    private static final int XML = 9;
    private static final int JSON_INDENT = 4;

    public static void init(boolean isDebug, String tag) {
        TAG = tag;
        LOG_DEBUG = isDebug;
    }

    public static void v(String msg) {
        LogUtil.log(2, null, msg);
    }

    public static void v(String tag, String msg) {
        LogUtil.log(2, tag, msg);
    }

    public static void d(String msg) {
        LogUtil.log(3, null, msg);
    }

    public static void d(String tag, String msg) {
        LogUtil.log(3, tag, msg);
    }

    public static void i(Object ... msg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : msg) {
            sb.append(obj);
            sb.append(",");
        }
        LogUtil.log(4, null, String.valueOf(sb));
    }

    public static void d(Object ... msg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : msg) {
            sb.append(obj);
            sb.append(",");
        }
        LogUtil.log(3, null, String.valueOf(sb));
    }

    public static void w(String msg) {
        LogUtil.log(5, null, msg);
    }

    public static void w(String tag, String msg) {
        LogUtil.log(5, tag, msg);
    }

    public static void e(String msg) {
        LogUtil.log(6, null, msg);
    }

    public static void e(String tagStr, String msg) {
        LogUtil.log(6, tagStr, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        LogUtil.log(6, tag, msg + '\n' + Log.getStackTraceString((Throwable)tr));
    }

    public static void a(String msg) {
        LogUtil.log(7, null, msg);
    }

    public static void a(String tag, String msg) {
        LogUtil.log(7, tag, msg);
    }

    public static void json(String json) {
        LogUtil.log(8, null, json);
    }

    public static void json(String tag, String json) {
        LogUtil.log(8, tag, json);
    }

    public static void xml(String xml) {
        LogUtil.log(9, null, xml);
    }

    public static void xml(String tag, String xml) {
        LogUtil.log(9, tag, xml);
    }

    private static void log(int logType, String tagStr, Object objects) {
        String[] contents = LogUtil.wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        if (LOG_DEBUG) {
            switch (logType) {
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: 
                case 7: {
                    LogUtil.printDefault(logType, tag, headString + msg);
                    break;
                }
                case 8: {
                    LogUtil.printJson(tag, msg, headString);
                    break;
                }
                case 9: {
                    LogUtil.printXml(tag, msg, headString);
                    break;
                }
            }
        }
    }

    private static void printDefault(int type, String tag, String msg) {
        if (TextUtils.isEmpty((CharSequence)tag)) {
            tag = TAG;
        }
        LogUtil.printLine(tag, true);
        int maxLength = 4000;
        int countOfSub = msg.length();
        if (countOfSub > maxLength) {
            for (int i = 0; i < countOfSub; i += maxLength) {
                if (i + maxLength < countOfSub) {
                    LogUtil.printSub(type, tag, msg.substring(i, i + maxLength));
                    continue;
                }
                LogUtil.printSub(type, tag, msg.substring(i, countOfSub));
            }
        } else {
            LogUtil.printSub(type, tag, msg);
        }
        LogUtil.printLine(tag, false);
    }

    private static void printSub(int type, String tag, String msg) {
        switch (type) {
            case 2: {
                Log.v((String)tag, (String)msg);
                break;
            }
            case 3: {
                Log.d((String)tag, (String)msg);
                break;
            }
            case 4: {
                Log.i((String)tag, (String)msg);
                break;
            }
            case 5: {
                Log.w((String)tag, (String)msg);
                break;
            }
            case 6: {
                LogUtil.d(tag, msg);
                break;
            }
            case 7: {
                Log.wtf((String)tag, (String)msg);
            }
        }
    }

    private static void printJson(String tag, String json, String headString) {
        String[] lines;
        String message;
        if (TextUtils.isEmpty((CharSequence)json)) {
            LogUtil.d("Empty/Null json content");
            return;
        }
        if (TextUtils.isEmpty((CharSequence)tag)) {
            tag = TAG;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        }
        catch (JSONException e) {
            message = json;
        }
        LogUtil.printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        for (String line : lines = message.split(LINE_SEPARATOR)) {
            LogUtil.printSub(3, tag, "|" + line);
        }
        LogUtil.printLine(tag, false);
    }

    private static void printXml(String tag, String xml, String headString) {
        String[] lines;
        if (TextUtils.isEmpty((CharSequence)tag)) {
            tag = TAG;
        }
        if (xml != null) {
            try {
                StreamSource xmlInput = new StreamSource(new StringReader(xml));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, xmlOutput);
                xml = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
            xml = headString + "\n" + xml;
        } else {
            xml = headString + "Log with null object";
        }
        LogUtil.printLine(tag, true);
        for (String line : lines = xml.split(LINE_SEPARATOR)) {
            if (TextUtils.isEmpty((CharSequence)line)) continue;
            LogUtil.printSub(3, tag, "|" + line);
        }
        LogUtil.printLine(tag, false);
    }

    private static String[] wrapperContent(String tag, Object ... objects) {
        StackTraceElement[] stackTrace;
        StackTraceElement targetElement;
        String className;
        String[] classNameInfo;
        if (TextUtils.isEmpty((CharSequence)tag)) {
            tag = TAG;
        }
        if ((classNameInfo = (className = (targetElement = (stackTrace = Thread.currentThread().getStackTrace())[5]).getClassName()).split("\\.")).length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + ".java";
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String msg = objects == null ? "Log with null object" : LogUtil.getObjectsString(objects);
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object ... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; ++i) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                    continue;
                }
                stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
            }
            return stringBuilder.toString();
        }
        Object object = objects[0];
        return object == null ? "null" : object.toString();
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d((String)tag, (String)"╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d((String)tag, (String)"╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}

