package com.lovebridge.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingString extends Setting
{
    private final String defaultValue;

    SettingString(int key, String defaultValue)
    {
        super(key);
        this.defaultValue = defaultValue;
    }

    public String getValue(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(this.getKey()),
                this.defaultValue);
    }

    public void setValue(Context context, String value)
    {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        sharedPreferences.putString(context.getString(this.getKey()), value);
        sharedPreferences.apply();
    }
}