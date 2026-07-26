package de.robv.android.xposed.callbacks;
public interface IXUnhook<T> { T getHookedMethod(); void unhook(); }
