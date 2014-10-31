
package com.lovebridge.library.api;

import android.widget.ImageView;

import com.lovebridge.R;
import com.lovebridge.library.volley.ex.FadeInImageListener;
import com.lovebridge.library.volley.toolbox.ImageLoader.ImageListener;

/**
 * @author yushilong
 * @date 2014-10-31 下午2:58:24
 * @version 1.0
 */
public class YARImageLoader
{
    public final static int DEFAULT_LOAD_IMAGE = R.drawable.bottom;
    public final static int DEFAULT_ERROR_IMAGE = DEFAULT_LOAD_IMAGE;
    private ImageListener mImageListener;

    public static YARImageLoader newInstance()
    {
        return new YARImageLoader();
    }

    /**
     * 获取图片，默认使用FadeInImageListener
     * @param imageView
     * @param imgUrl
     */
    public void get(ImageView imageView , String imgUrl)
    {
        if (mImageListener == null)
            mImageListener = new FadeInImageListener(imageView, DEFAULT_ERROR_IMAGE);
        YARVolley.getImageLoader().get(imgUrl, mImageListener);
    }
}
