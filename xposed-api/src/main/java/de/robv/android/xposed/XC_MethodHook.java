package de.robv.android.xposed;

import de.robv.android.xposed.callbacks.XCallback;

public abstract class XC_MethodHook extends XCallback {

    public XC_MethodHook() {}
    public XC_MethodHook(int priority) { super(priority); }

    protected void beforeHookedMethod(MethodHookParam param) {}
    protected void afterHookedMethod(MethodHookParam param) {}

    public static final int PRIORITY_HIGHEST = Integer.MAX_VALUE;
    public static final int PRIORITY_LOWEST = Integer.MIN_VALUE;

    public static class MethodHookParam extends Param {
        public Object[] args;
        public Object thisObject;
        public java.lang.reflect.Member hookMethod;
        public Object returnValue;
        public Throwable throwThrowable;

        public XC_MethodHook unhook() { return null; }
        public void setResult(Object r) { returnValue = r; }
        public void setThrowable(Throwable t) { throwThrowable = t; }
    }

    @Override
    public void call(Param param) {
        if (param instanceof MethodHookParam) {
            beforeHookedMethod((MethodHookParam) param);
            ((MethodHookParam) param).returnValue = null;
            ((MethodHookParam) param).throwThrowable = null;
        }
    }

    protected void setArgs(Object[] args) {}
}
