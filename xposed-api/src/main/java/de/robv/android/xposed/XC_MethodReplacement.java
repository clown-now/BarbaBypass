package de.robv.android.xposed;

public abstract class XC_MethodReplacement extends XC_MethodHook {

    public XC_MethodReplacement() {}
    public XC_MethodReplacement(int priority) { super(priority); }

    protected abstract Object replaceHookedMethod(MethodHookParam param) throws Throwable;

    @Override
    protected void beforeHookedMethod(MethodHookParam param) {
        try {
            Object result = replaceHookedMethod(param);
            param.setResult(result);
        } catch (Throwable t) {
            param.setThrowable(t);
        }
    }

    public static final XC_MethodReplacement DO_NOTHING = new XC_MethodReplacement(50) {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) { return null; }
    };

    public static XC_MethodReplacement returnConstant(final Object result) {
        return returnConstant(50, result);
    }

    public static XC_MethodReplacement returnConstant(final int priority, final Object result) {
        return new XC_MethodReplacement(priority) {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) { return result; }
        };
    }
}
