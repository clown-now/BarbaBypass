package de.robv.android.xposed;
import java.lang.reflect.Member;
public abstract class XC_MethodHook {
    protected void beforeHookedMethod(MethodHookParam param) {}
    protected void afterHookedMethod(MethodHookParam param) {}
    public static class MethodHookParam {
        public Object thisObject; public Object returnValue; public Throwable throwThrowable;
        public Member hookMethod; public Object[] args;
    }
}
