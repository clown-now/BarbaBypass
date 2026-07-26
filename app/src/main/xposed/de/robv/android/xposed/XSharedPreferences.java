package de.robv.android.xposed;

import java.util.*;

public class XSharedPreferences implements SharedPreferences {
    private String mPackage, mName;
    private Map<String, Object> mData = new HashMap<>();

    public XSharedPreferences(String package_name, String pref_name) {
        this.mPackage = package_name;
        this.mName = pref_name;
    }

    public void makeVisible() {}
    public void reload() {}

    @Override public boolean contains(String key) { return false; }
    @Override public Map<String, ?> getAll() { return Collections.emptyMap(); }
    @Override public String getString(String key, String defValue) { return defValue; }
    @Override public Set<String> getStringSet(String key, Set<String> defValues) { return null; }
    @Override public int getInt(String key, int defValue) { return defValue; }
    @Override public long getLong(String key, long defValue) { return defValue; }
    @Override public float getFloat(String key, float defValue) { return defValue; }
    @Override public boolean getBoolean(String key, boolean defValue) { return defValue; }
    @Override public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
    @Override public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}

    public static class Editor implements SharedPreferences.Editor {
        private final XSharedPreferences mPrefs;
        Editor(XSharedPreferences prefs) { this.mPrefs = prefs; }
        @Override public Editor putString(String key, String value) { return this; }
        @Override public Editor putStringSet(String key, Set<String> values) { return this; }
        @Override public Editor putInt(String key, int value) { return this; }
        @Override public Editor putLong(String key, long value) { return this; }
        @Override public Editor putFloat(String key, float value) { return this; }
        @Override public Editor putBoolean(String key, boolean value) { return this; }
        @Override public Editor remove(String key) { return this; }
        @Override public Editor clear() { return this; }
        @Override public boolean commit() { return true; }
    }

    public Editor edit() { return new Editor(this); }
}
