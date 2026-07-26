
package de.robv.android.xposed;
import de.robv.android.xposed.callbacks.XC_MethodHook.*;
public abstract class XC_MethodHook extends de.robv.android.xposed.callbacks.XCallback {
    public XC_MethodHook() {}
    public XC_MethodHook(int priority) {}
    protected void beforeHookedMethod(MethodHookParam param) {}
    protected void afterHookedMethod(MethodHookParam param) {}
    public static final int PRIORITIY_HIGHEST = Integer.MAX_VALUE;
    public abstract class MethodHookParam {
        public Object thisObject, returnValue;
        public Throwable throwThrowable;
        public java.lang.reflect.Member hookMethod;
        public Object[] args;
        public XC_MethodHook unhook() { return null; }
        public void setResult(Object r) {}
        public void setThrowable(Throwable t) {}
    }
}
