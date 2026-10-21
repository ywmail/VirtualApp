/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.SharedPreferences$OnSharedPreferenceChangeListener
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.kook.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SPTools {
    private static final String TAG = "KookSharedPreferences";
    private static final String FILE_NAME = "sharedPreferences";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

    public static void setOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mOnSharedPreferenceChangeListener = onSharedPreferenceChangeListener;
        if (mSharedPreferences != null) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
        }
        return mSharedPreferences;
    }

    public static int getInt(Context context, String keyname, int def) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        int v = shared.getInt(keyname, def);
        if (v == def) {
            return def;
        }
        return v;
    }

    public static int getInt(Context context, String keyname) {
        return SPTools.getInt(context, keyname, -1);
    }

    public static String getString(Context context, String keyname, String defValues) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        String str = shared.getString(keyname, null);
        if (str == null) {
            return defValues;
        }
        return str;
    }

    public static String getString(Context context, String keyname) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        String str = shared.getString(keyname, null);
        if (str == null) {
            return null;
        }
        return str;
    }

    public static long getLong(Context context, String keyname) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        long str = shared.getLong(keyname, -1L);
        return str;
    }

    public static boolean getBoolean(Context context, String keyname) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        boolean str = shared.getBoolean(keyname, false);
        return str;
    }

    public static boolean getBoolean(Context context, String keyname, boolean defaultBoolean) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        boolean str = shared.getBoolean(keyname, defaultBoolean);
        return str;
    }

    public static void putString(Context context, String keyname, String values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor e = shared.edit();
        e.putString(keyname, values);
        boolean b = e.commit();
        if (b) {
            // empty if block
        }
    }

    public static void putIntList(Context context, String keyname, List<Integer> values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(keyname, values.size());
        for (int i = 0; i < values.size(); ++i) {
            editor.putInt(keyname + "_" + i, values.get(i).intValue());
        }
        boolean b = editor.commit();
        if (b) {
            // empty if block
        }
    }

    public static void putMap(Context context, String key, Map<String, String> datas) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        JSONArray mJsonArray = new JSONArray();
        Iterator<Map.Entry<String, String>> iterator = datas.entrySet().iterator();
        JSONObject object = new JSONObject();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            try {
                object.put(entry.getKey(), (Object)entry.getValue());
            }
            catch (JSONException jSONException) {}
        }
        mJsonArray.put((Object)object);
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

    public static Map<String, String> getMap(Context context, String key) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        LinkedHashMap<String, String> datas = new LinkedHashMap<String, String>();
        String result = shared.getString(key, "");
        try {
            JSONArray array2 = new JSONArray(result);
            for (int i = 0; i < array2.length(); ++i) {
                JSONObject itemObject = array2.getJSONObject(i);
                JSONArray names = itemObject.names();
                if (names == null) continue;
                for (int j = 0; j < names.length(); ++j) {
                    String name = names.getString(j);
                    String value = itemObject.getString(name);
                    datas.put(name, value);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static List<Integer> getIntList(Context context, String keyname) {
        ArrayList<Integer> environmentList = new ArrayList<Integer>();
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        int environNums = shared.getInt(keyname, 0);
        for (int i = 0; i < environNums; ++i) {
            String key = keyname + "_" + i;
            if (!shared.contains(key)) continue;
            int environItem = shared.getInt(keyname + "_" + i, 0);
            environmentList.add(environItem);
        }
        return environmentList;
    }

    public static void putStringList(Context context, String keyname, List<String> values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(keyname, values.size());
        for (int i = 0; i < values.size(); ++i) {
            editor.putString(keyname + "_" + i, values.get(i));
        }
        boolean b = editor.commit();
        if (b) {
            // empty if block
        }
    }

    public static List<String> getStringList(Context context, String keyname) {
        ArrayList<String> environmentList = new ArrayList<String>();
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        int environNums = shared.getInt(keyname, 0);
        for (int i = 0; i < environNums; ++i) {
            String key = keyname + "_" + i;
            if (!shared.contains(key)) continue;
            String environItem = shared.getString(keyname + "_" + i, null);
            environmentList.add(environItem);
        }
        return environmentList;
    }

    public static void removeListAll(Context context, String keyname) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        int environNums = shared.getInt(keyname, 0);
        for (int i = 0; i < environNums; ++i) {
            editor.remove(keyname + "" + i);
        }
        boolean b = editor.commit();
    }

    public static void removeListItem(Context context, String keyname, Object value) {
        if (value instanceof String || value instanceof Integer) {
            try {
                throw new Exception("value \u7c7b\u578b\u5fc5\u987b\u662f int \u6216\u8005 String");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        int environNums = shared.getInt(keyname, 0);
        for (int i = 0; i < environNums; ++i) {
            String environItem;
            if (value instanceof Integer) {
                int environItem2 = shared.getInt(keyname + "_" + i, 0);
                if (environItem2 != (Integer)value) continue;
                editor.remove(keyname + "_" + i);
                editor.commit();
                continue;
            }
            if (!(value instanceof String) || !(environItem = shared.getString(keyname + "_" + i, null)).equals((String)value)) continue;
            editor.remove(keyname + "_" + i);
            editor.commit();
        }
    }

    public static void putInt(Context context, String keyname, int values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor e = shared.edit();
        e.putInt(keyname, values);
        boolean b = e.commit();
        if (b) {
            // empty if block
        }
    }

    public static void putLong(Context context, String keyname, long values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor e = shared.edit();
        e.putLong(keyname, values);
        boolean b = e.commit();
        if (b) {
            // empty if block
        }
    }

    public static void putBoolean(Context context, String keyname, boolean values) {
        SharedPreferences shared = SPTools.getSharedPreferences(context);
        SharedPreferences.Editor e = shared.edit();
        e.putBoolean(keyname, values);
        boolean b = e.commit();
        if (b) {
            // empty if block
        }
    }
}

