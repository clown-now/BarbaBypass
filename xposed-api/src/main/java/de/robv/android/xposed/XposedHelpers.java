package de.robv.android.xposed;
public class XposedHelpers {
    public static java.lang.Class<?> findClass(String name, ClassLoader loader) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
    public static void findAndHookMethod(java.lang.Class<?> clazz, String methodName, XC_MethodHook callback) {}
    public static void findAndHookMethod(java.lang.Class<?> clazz, String methodName, Object... paramAndCallback) {}
    public static void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... paramAndCallback) {}
    public static void findAndHookConstructor(java.lang.Class<?> clazz, XC_MethodHook callback) {}
    public static void findAndHookConstructor(java.lang.Class<?> clazz, Object... paramAndCallback) {}
    public static void findAndHookConstructor(String className, ClassLoader classLoader, Object... paramAndCallback) {}
    public static Object callMethod(Object obj, String methodName, Object... args) { return null; }
}
