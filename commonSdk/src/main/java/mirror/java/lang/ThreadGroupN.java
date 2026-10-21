package mirror.java.lang;

import java.lang.reflect.Field;
import mirror.RefClass;
import mirror.RefObject;

public class ThreadGroupN {
    public static Class<?> TYPE = RefClass.load(ThreadGroupN.class, java.lang.ThreadGroup.class);
    public static RefObject<Integer> ngroups;
    public static RefObject<java.lang.ThreadGroup[]> groups;
    public static RefObject<java.lang.ThreadGroup> parent;

    static {
        try {
            Field ngroupsField = TYPE.getDeclaredField("ngroups");
            ngroups = new RefObject<>(TYPE, ngroupsField);

            Field groupsField = TYPE.getDeclaredField("groups");
            groups = new RefObject<>(TYPE, groupsField);

            Field parentField = TYPE.getDeclaredField("parent");
            parent = new RefObject<>(TYPE, parentField);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}