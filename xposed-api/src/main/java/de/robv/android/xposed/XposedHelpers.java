
package de.robv.android.xposed;
public class XposedHelpers {
    public static java.lang.Class<?> findClass(String name, ClassLoader loader) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
    public static void findAndHookMethod(String clazz, ClassLoader loader, String method, Object... params) {}
    public static void findAndHookMethod(java.lang.Class<?> clazz, String method, XC_MethodHook callback) {}
    public static void findAndHookConstructor(java.lang.Class<?> clazz, XC_MethodHook callback) {}
    public static void findAndHookConstructor(java.lang.Class<?> clazz, Object... params) {}
    public static Object callMethod(Object obj, String methodName, Object... args) { return null; }
}
