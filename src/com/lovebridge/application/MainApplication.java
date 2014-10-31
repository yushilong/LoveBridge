
package com.lovebridge.application;

import android.app.Application;

import com.lovebridge.library.api.YARVolley;
import com.lovebridge.library.tools.YARNetUtils;

/**
 * @author yushilong
 * @date 2014-9-29 下午5:18:47
 * @version 1.0
 */
public class MainApplication extends Application
{
    private static MainApplication _instance;

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        _instance = this;
        init();
    }

    public void init()
    {
        YARNetUtils.setCurrentNetState(_instance);
        YARVolley.init(this);
    }

    public static MainApplication getInstance()
    {
        return _instance;
    }
}
