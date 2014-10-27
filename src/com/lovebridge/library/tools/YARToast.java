
package com.lovebridge.library.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * @author yushilong
 * @date 2014-9-30 下午1:38:27
 * @version 1.0
 */
public class YARToast
{
    public static Toast mShortToast, mLongToast;
    public static Object lock = new Object();

    public static void showShort(Context context , String msg)
    {
        synchronized (lock)
        {
            if (mShortToast == null)
            {
                mShortToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
            mShortToast.cancel();
            mShortToast.setText(msg);
            mShortToast.show();
        }
    }

    public static void showLong(Context context , String msg)
    {
        synchronized (lock)
        {
            if (mLongToast == null)
            {
                mLongToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
            mLongToast.cancel();
            mLongToast.setText(msg);
            mLongToast.show();
        }
    }

    public static void showDefault(Context context , String msg)
    {
        showShort(context, msg);
    }
}
