package mirror.java.lang;

import java.lang.reflect.Field;
import java.util.List;
import mirror.RefClass;
import mirror.RefObject;

public class ThreadGroup {
    public static Class<?> TYPE = RefClass.load(ThreadGroup.class, java.lang.ThreadGroup.class);
    public static RefObject<List<java.lang.ThreadGroup>> groups;
    public static RefObject<java.lang.ThreadGroup> parent;

    static {
        try {
            Field groupsField = TYPE.getDeclaredField("groups");
            groups = new RefObject<>(TYPE, groupsField);

            Field parentField = TYPE.getDeclaredField("parent");
            parent = new RefObject<>(TYPE, parentField);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}