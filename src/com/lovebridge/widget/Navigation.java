package com.lovebridge.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lovebridge.R;

/**
 * 导航栏布局
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


    public void initView(final Context context, AttributeSet attrs)
    {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.yar_navigation, this);
        iv_navigation_left = (ImageView) viewGroup.findViewById(R.id.iv_navigation_left);
        iv_navigation_right = (ImageView) viewGroup.findViewById(R.id.iv_navigation_right);
        tv_navigation_title = (TextView) viewGroup.findViewById(R.id.tv_navigation_title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigation);
        Boolean show = typedArray.getBoolean(R.styleable.navigation_image_left_show, true);
        if (show)
        {
            iv_navigation_left.setVisibility(VISIBLE);
            iv_navigation_left.setImageResource(typedArray.getResourceId(R.styleable.navigation_image_left, R.drawable.btn_back_selector));
        }
        else
        {
            iv_navigation_left.setVisibility(GONE);
        }
        if (typedArray.hasValue(R.styleable.navigation_image_right))
        {
            int resId = typedArray.getResourceId(R.styleable.navigation_image_right, -1);
            if (resId != -1)
            {
                iv_navigation_right.setVisibility(VISIBLE);
                iv_navigation_right.setImageResource(resId);
            }
        }
        if (typedArray.hasValue(R.styleable.navigation_title))
        {
            int resId = typedArray.getResourceId(R.styleable.navigation_title, -1);
            if (resId != -1)
            {
                tv_navigation_title.setText(resId);
            }
        }
        //
        iv_navigation_left.setOnClickListener(new OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (context instanceof Activity)
                {
                    ((Activity) context).finish();
                }
            }
        });
        typedArray.recycle();
    }
}
