//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.helper;

import android.os.Bundle;
import android.os.Parcel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParcelHelper {
    public ParcelHelper() {
    }

    public static void writeMeta(Parcel p, Bundle meta) {
        Map<String, String> map = new HashMap();
        if (meta != null) {
            Iterator var3 = meta.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                map.put(key, meta.getString(key));
            }
        }

        p.writeMap(map);
    }

    public static Bundle readMeta(Parcel p) {
        Bundle meta = new Bundle();
        Map<String, String> map = p.readHashMap(String.class.getClassLoader());
        Iterator var3 = map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            meta.putString((String)entry.getKey(), (String)entry.getValue());
        }

        return meta;
    }
}
