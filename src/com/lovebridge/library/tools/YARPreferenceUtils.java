/*
 * [文 件 名]:B5MPreferenceHelper.java
 * [创 建 人]:allenwang
 * [创建时间]:Oct 22, 2013
 * [编　　码]:UTF-8
 * [版　　权]:Copyright © 2012 B5Msoft Co,Ltd. 
 */

package com.lovebridge.library.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class YARPreferenceUtils
{
    public static String readStringValue(Context context , String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = prefs.getString(key, null);
        return TextUtils.isEmpty(value) ? null : decode(value);
    }

    public static String readStringValue(Context context , String key , String defaultStr)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = prefs.getString(key, null);
        return TextUtils.isEmpty(value) ? defaultStr : decode(value);
    }

    public static int readIntValue(Context context , String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public static int readIntValue(Context context , String key , int def)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, def);
    }

    public static Long readLongValue(Context context , String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(key, 0);
    }

    public static void saveIntValue(Context context , String key , int value)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveStringValue(Context context , String key , String value)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString(key, TextUtils.isEmpty(value) ? value : encode(value));
        editor.commit();
    }

    public static void saveLongValue(Context context , String key , Long value)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * base64加密
     * 
     * @param encodeStr
     * @return
     */
    public static String encode(String encodeStr)
    {
        return Base64.encodeToString(encodeStr.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解密
     * 
     * @param decodeStr
     * @return
     */
    public static String decode(String encodeStr)
    {
        return new String(Base64.decode(encodeStr, Base64.DEFAULT));
    }

    public static class PreferenceKeys
    {
        public static final String USER_ID = "USER_ID";
    }
}
