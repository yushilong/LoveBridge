package com.lovebridge.chat.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLruCache extends LruCache<Object, Object>
{
    public BitmapLruCache(double fractionOfMemory)
    {
        super(BitmapLruCache.computeMaxSize(fractionOfMemory));
    }

    protected int sizeOf(Object arg5, Bitmap value)
    {
        return ((int) Math.ceil((((double) (value.getRowBytes() * value.getHeight()))) / 1024));
    }

    protected int sizeOf(Object arg2, Object arg3)
    {
        return this.sizeOf(arg2, ((Bitmap) arg3));
    }

    private static int computeMaxSize(double fractionOfMemory)
    {
        return ((int) ((((double) Runtime.getRuntime().maxMemory())) * fractionOfMemory / 1024));
    }
}
