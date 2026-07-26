package de.robv.android.xposed;
public class XposedBridge {
    private static int[] sHooks = new int[0];
    public static void hookMethod(java.lang.reflect.Member m, XC_MethodHook c) {}
    public static void log(String msg) {}
}
