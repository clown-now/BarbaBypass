package de.robv.android.xposed;
public class XposedHelpers {
    public static java.lang.Class<?> findClass(String n, ClassLoader l) { return null; }
    public static void findAndHookMethod(java.lang.Class<?> c, String m, XC_MethodHook cb) {}
    public static void findAndHookConstructor(java.lang.Class<?> c, XC_MethodHook cb) {}
    public static void findAndHookMethod(String c, ClassLoader l, String m, Object... a) {}
    public static void findAndHookConstructor(String c, ClassLoader l, Object... a) {}
    public static Object callMethod(Object o, String n, Object... a) { return null; }
}
