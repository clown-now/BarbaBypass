
package de.robv.android.xposed.callbacks;
import android.os.Bundle;
public abstract class XCallback {
    public final int priority;
    public XCallback() { this.priority = 50; }
    public XCallback(int priority) { this.priority = priority; }
    public abstract void call(MethodHookParam param);
    public class Param {
        public Bundle extra;
        public Bundle getExtra() { return new Bundle(); }
    }
}
