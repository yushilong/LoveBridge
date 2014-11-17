package com.lovebridge.library.api;

import android.app.ActivityManager;
import android.content.Context;
import com.lovebridge.library.volley.RequestQueue;
import com.lovebridge.library.volley.ex.BitmapLruCache;
import com.lovebridge.library.volley.toolbox.ImageLoader;
import com.lovebridge.library.volley.toolbox.Volley;

/**
 * @author yushilong
 * @date 2014-10-29 下午3:07:01
 * @version 1.0
 */
public class YARVolley
{
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public YARVolley()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void init(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
    }

    public static RequestQueue getRequestQueue()
    {
        if (mRequestQueue != null)
        {
            return mRequestQueue;
        }
        else
        {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache}
     * which effectively means that no memory caching is used. This is useful
     * for images that you know that will be show only once.
     *
     * @return
     */
    public static ImageLoader getImageLoader()
    {
        if (mImageLoader != null)
        {
            return mImageLoader;
        }
        else
        {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
