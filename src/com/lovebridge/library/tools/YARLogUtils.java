
package com.lovebridge.library.tools;

import android.util.Log;

/**
 * @author yushilong
 * @date 2014-9-29 下午5:49:48
 * @version 1.0
 */
public class YARLogUtils
{
    public static void i(String tag , String msg)
    {
        if (YARConstants.DEBUG)
        {
            Log.i(tag, msg);
        }
    }
}
