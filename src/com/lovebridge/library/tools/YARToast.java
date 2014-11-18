package com.lovebridge.library.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * @author yushilong
 * @date 2014-9-30 下午1:38:27
 * @version 1.0
 */
@SuppressLint("ShowToast")
public class YARToast
{
    public static Toast mShortToast, mLongToast;
    public static Object lock = new Object();

    public static void showShort(Context context, String msg)
    {
        synchronized (lock)
        {
            if (mShortToast == null)
            {
                mShortToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
            updateToast(mShortToast, msg);
        }
    }

    public static void showLong(Context context, String msg)
    {
        synchronized (lock)
        {
            if (mLongToast == null)
            {
                mLongToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
            updateToast(mLongToast, msg);
        }
    }

    public static void updateToast(Toast toast, String msg)
    {
        toast.cancel();
        toast.setText(msg);
        toast.show();
    }

    public static void showDefault(Context context, String msg)
    {
        showShort(context, msg);
    }
}
