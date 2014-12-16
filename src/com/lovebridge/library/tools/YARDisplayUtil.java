package com.lovebridge.library.tools;

import android.content.Context;
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

    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
