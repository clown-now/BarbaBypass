
package de.robv.android.xposed.callbacks;
import android.content.pm.ApplicationInfo;
import android.app.Application;
public abstract class XC_LoadPackage extends XCallback {
    public static class LoadPackageParam extends XCallback.Param {
        public ApplicationInfo appInfo;
        public String processName, packageName;
        public ClassLoader classLoader;
        public boolean isFirstApplication;
        public boolean handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) { return false; }
    }
    protected abstract void handleLoadPackage(LoadPackageParam lpparam) throws Throwable;
}
