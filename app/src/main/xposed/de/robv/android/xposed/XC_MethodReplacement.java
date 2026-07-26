
package de.robv.android.xposed;
public abstract class XC_MethodReplacement extends XC_MethodHook {
    public XC_MethodReplacement() {}
    public XC_MethodReplacement(int priority) {}
    protected abstract Object replaceHookedMethod(MethodHookParam param) throws Throwable;
    public static final XC_MethodReplacement DO_NOTHING = new XC_MethodReplacement(0) {};
    public static XC_MethodReplacement returnConstant(final Object result) {
        return new XC_MethodReplacement(0) {
            protected Object replaceHookedMethod(MethodHookParam param) { return result; }
        };
    }
}
