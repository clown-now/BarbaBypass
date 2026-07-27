package de.robv.android.xposed.callbacks;
public abstract class XC_LoadPackage extends XCallback {
    public static class LoadPackageParam extends Param {
        public String processName, packageName;
        public ClassLoader classLoader;
    }
    protected abstract void handleLoadPackage(LoadPackageParam lpparam) throws Throwable;
}
