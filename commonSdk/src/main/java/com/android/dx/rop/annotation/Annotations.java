/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.annotation;

import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.cst.CstType;
import com.android.dx.util.MutabilityControl;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

public final class Annotations
extends MutabilityControl
implements Comparable<Annotations> {
    public static final Annotations EMPTY = new Annotations();
    private final TreeMap<CstType, Annotation> annotations = new TreeMap();

    public static Annotations combine(Annotations a1, Annotations a2) {
        Annotations result = new Annotations();
        result.addAll(a1);
        result.addAll(a2);
        result.setImmutable();
        return result;
    }

    public static Annotations combine(Annotations annotations, Annotation annotation) {
        Annotations result = new Annotations();
        result.addAll(annotations);
        result.add(annotation);
        result.setImmutable();
        return result;
    }

    public int hashCode() {
        return this.annotations.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Annotations)) {
            return false;
        }
        Annotations otherAnnotations = (Annotations)other;
        return this.annotations.equals(otherAnnotations.annotations);
    }

    @Override
    public int compareTo(Annotations other) {
        Iterator<Annotation> thisIter = this.annotations.values().iterator();
        Iterator<Annotation> otherIter = other.annotations.values().iterator();
        while (thisIter.hasNext() && otherIter.hasNext()) {
            Annotation otherOne;
            Annotation thisOne = thisIter.next();
            int result = thisOne.compareTo(otherOne = otherIter.next());
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
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append("annotations{");
        for (Annotation a : this.annotations.values()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(a.toHuman());
        }
        sb.append("}");
        return sb.toString();
    }

    public int size() {
        return this.annotations.size();
    }

    public void add(Annotation annotation) {
        this.throwIfImmutable();
        if (annotation == null) {
            throw new NullPointerException("annotation == null");
        }
        CstType type = annotation.getType();
        if (this.annotations.containsKey(type)) {
            throw new IllegalArgumentException("duplicate type: " + type.toHuman());
        }
        this.annotations.put(type, annotation);
    }

    public void addAll(Annotations toAdd) {
        this.throwIfImmutable();
        if (toAdd == null) {
            throw new NullPointerException("toAdd == null");
        }
        for (Annotation a : toAdd.annotations.values()) {
            this.add(a);
        }
    }

    public Collection<Annotation> getAnnotations() {
        return Collections.unmodifiableCollection(this.annotations.values());
    }

    static {
        EMPTY.setImmutable();
    }
}

