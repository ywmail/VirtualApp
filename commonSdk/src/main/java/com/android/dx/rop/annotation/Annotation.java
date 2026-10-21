/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.annotation;

import com.android.dx.rop.annotation.AnnotationVisibility;
import com.android.dx.rop.annotation.NameValuePair;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.MutabilityControl;
import com.android.dx.util.ToHuman;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

public final class Annotation
extends MutabilityControl
implements Comparable<Annotation>,
ToHuman {
    private final CstType type;
    private final AnnotationVisibility visibility;
    private final TreeMap<CstString, NameValuePair> elements;

    public Annotation(CstType type, AnnotationVisibility visibility) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (visibility == null) {
            throw new NullPointerException("visibility == null");
        }
        this.type = type;
        this.visibility = visibility;
        this.elements = new TreeMap();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Annotation)) {
            return false;
        }
        Annotation otherAnnotation = (Annotation)other;
        if (!this.type.equals(otherAnnotation.type) || this.visibility != otherAnnotation.visibility) {
            return false;
        }
        return this.elements.equals(otherAnnotation.elements);
    }

    public int hashCode() {
        int hash = this.type.hashCode();
        hash = hash * 31 + this.elements.hashCode();
        hash = hash * 31 + this.visibility.hashCode();
        return hash;
    }

    @Override
    public int compareTo(Annotation other) {
        int result = this.type.compareTo(other.type);
        if (result != 0) {
            return result;
        }
        result = this.visibility.compareTo(other.visibility);
        if (result != 0) {
            return result;
        }
        Iterator<NameValuePair> thisIter = this.elements.values().iterator();
        Iterator<NameValuePair> otherIter = other.elements.values().iterator();
        while (thisIter.hasNext() && otherIter.hasNext()) {
            NameValuePair otherOne;
            NameValuePair thisOne = thisIter.next();
            result = thisOne.compareTo(otherOne = otherIter.next());
            if (result == 0) continue;
            return result;
        }
        if (thisIter.hasNext()) {
            return 1;
        }
        if (otherIter.hasNext()) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return this.toHuman();
    }

    @Override
    public String toHuman() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.visibility.toHuman());
        sb.append("-annotation ");
        sb.append(this.type.toHuman());
        sb.append(" {");
        boolean first = true;
        for (NameValuePair pair : this.elements.values()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(pair.getName().toHuman());
            sb.append(": ");
            sb.append(pair.getValue().toHuman());
        }
        sb.append("}");
        return sb.toString();
    }

    public CstType getType() {
        return this.type;
    }

    public AnnotationVisibility getVisibility() {
        return this.visibility;
    }

    public void put(NameValuePair pair) {
        this.throwIfImmutable();
        if (pair == null) {
            throw new NullPointerException("pair == null");
        }
        this.elements.put(pair.getName(), pair);
    }

    public void add(NameValuePair pair) {
        this.throwIfImmutable();
        if (pair == null) {
            throw new NullPointerException("pair == null");
        }
        CstString name = pair.getName();
        if (this.elements.get(name) != null) {
            throw new IllegalArgumentException("name already added: " + name);
        }
        this.elements.put(name, pair);
    }

    public Collection<NameValuePair> getNameValuePairs() {
        return Collections.unmodifiableCollection(this.elements.values());
    }
}

