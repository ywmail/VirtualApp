/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 */
package com.kook.deviceinfo.models;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseModel {
    JSONObject mElement = new JSONObject();
    Map map = new HashMap();

    public String toJSON() {
        this.addProperty();
        return this.mElement.toString();
    }

    abstract void addProperty();

    protected void addProperty(String property, Object object) {
        if (this.mElement.containsKey((Object)property)) {
            this.mElement.remove((Object)property);
        }
        if (object instanceof String) {
            this.mElement.put(property, (Object)((String)object));
        } else if (object instanceof Integer) {
            this.mElement.put(property, (Object)((Integer)object));
        } else if (object instanceof Boolean) {
            this.mElement.put(property, (Object)((Boolean)object));
        } else if (object instanceof Long) {
            this.mElement.put(property, (Object)((Long)object));
        } else if (object instanceof Float) {
            this.mElement.put(property, (Object)((Float)object));
        }
    }
}

