
package com.lovebridge.library.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * @author yushilong
 * @date 2014-9-30 上午10:52:24
 * @version 1.0
 */
public class YARNetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            YARNetUtils.setCurrentNetState(context);
        }
    }
}
