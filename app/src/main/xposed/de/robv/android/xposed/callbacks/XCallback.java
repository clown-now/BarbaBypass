package de.robv.android.xposed.callbacks;

import android.os.Bundle;

public abstract class XCallback {

    public final int priority;

    public XCallback() { this.priority = 50; }
    public XCallback(int priority) { this.priority = priority; }

    public abstract void call(Param param);

    public Class<?> getReferentClass() { return null; }
    public Object getReferent() { return null; }
    public boolean isUnhooked() { return false; }

    public void unhook() {}

    public abstract static class Param {
        public Bundle extra;
        public Bundle getExtra() { return new Bundle(); }
    }
}

