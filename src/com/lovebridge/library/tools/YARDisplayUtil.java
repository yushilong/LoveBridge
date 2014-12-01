package com.lovebridge.library.tools;

import android.util.DisplayMetrics;
import com.lovebridge.application.MainApplication;

/**
 *author: lscm
 *date: 2014/12/1 15:05
 */
public class YARDisplayUtil
{
    public static DisplayMetrics displayMetrics = MainApplication.getInstance().getResources().getDisplayMetrics();

    /**
     * 获取屏幕宽
     * @return
     */
    public static int getScreenWidth()
    {
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高
     * @return
     */
    public static int getScreenHeight()
    {
        return displayMetrics.heightPixels;
    }
}
