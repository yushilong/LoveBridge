
package com.lovebridge.application;

import android.app.Application;
import android.content.Context;

import com.lovebridge.library.api.YARVolley;
import com.lovebridge.library.tools.YARNetUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
        initImageLoader(this);
        YARVolley.init(this);
    }

    public static MainApplication getInstance()
    {
        return _instance;
    }

    public static void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                                                                                 // for
                                                                                 // release
                                                                                 // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
