package com.lovebridge.library.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *author: lscm
 *date: 2014/11/27 21:44
 */
public class YARActivityUtil
{
    /**
     * 启动不带参的intent
     * @param activity
     * @param intent
     */
    public static void start(Activity activity, Intent intent)
    {
        activity.startActivity(intent);
    }

    /**
     * 启动带参的intent
     * @param activity
     * @param intent
     * @param bundle
     */
    public static void start(Activity activity, Intent intent, Bundle bundle)
    {
        if (bundle != null && !bundle.isEmpty())
        {
            intent.putExtras(bundle);
        }
        start(activity, intent);
    }
}
