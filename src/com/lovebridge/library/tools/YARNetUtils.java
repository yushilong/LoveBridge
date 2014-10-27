
package com.lovebridge.library.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author yushilong
 * @date 2014-9-30 上午10:50:34
 * @version 1.0
 */
public class YARNetUtils
{
    public static final int NET_NONE = 0;
    public static final int NET_WIFI = 1;
    public static final int NET_MOBILE = 2;
    public static int CURRENT_NET = Integer.MAX_VALUE;

    public static void setCurrentNetState(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
        {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType())
            {
                CURRENT_NET = NET_WIFI;
            }
            else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType())
            {
                CURRENT_NET = NET_MOBILE;
            }
        }
        else
        {
            CURRENT_NET = NET_NONE;
        }
    }

    public static int getCurrentNetState(Context context)
    {
        return CURRENT_NET;
    }

    public static boolean isNetAvailable()
    {
        return CURRENT_NET != NET_NONE;
    }
}
