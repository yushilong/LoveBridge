
package com.lovebridge.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingBoolean extends Setting {
    protected final boolean defaultValue;

    protected SettingBoolean(int key, boolean defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public boolean getValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(this.getKey()),
                        this.defaultValue);
    }

    public void setValue(Context context, boolean value) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        sharedPreferences.putBoolean(context.getString(this.getKey()), value);
        sharedPreferences.apply();
    }
}