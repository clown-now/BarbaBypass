
package de.robv.android.xposed;
import android.content.pm.ApplicationInfo;
import android.app.Application;
public interface IXposedHookLoadPackage extends IXposedMod {
    void handleLoadPackage(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam lpparam) throws Throwable;
}
