package com.lovebridge.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lovebridge.R;

/**
 * Created by yushilong on 14-11-16.
 */
public class Navigation extends LinearLayout
{
    public ImageView iv_navigation_left;
    public ImageView iv_navigation_right;
    public TextView tv_navigation_title;

    public Navigation(Context context)
    {
        super(context);
        initView(context, null);
    }

    public Navigation(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context, attrs);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs)
    {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.yar_navigation, this);
        iv_navigation_left = (ImageView) viewGroup.findViewById(R.id.iv_navigation_left);
        iv_navigation_right = (ImageView) viewGroup.findViewById(R.id.iv_navigation_right);
        tv_navigation_title = (TextView) viewGroup.findViewById(R.id.tv_navigation_title);
    }
}
