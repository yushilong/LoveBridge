
package com.lovebridge.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovebridge.R;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class TabVertical extends LinearLayout
{
    private ImageView iv_tab;
    private TextView tv_tab;

    public TabVertical(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        init(context, null);
    }

    public TabVertical(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public TabVertical(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public void init(Context context , AttributeSet attrs)
    {
        LayoutInflater.from(context).inflate(R.layout.tab_vertical, this, true);
        iv_tab = (ImageView) findViewById(R.id.iv_tab);
        tv_tab = (TextView) findViewById(R.id.tv_tab);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.tab_vertical);
        if (typedArray.hasValue(R.styleable.tab_vertical_tabImageSrc))
        {
            int tab_src = typedArray.getResourceId(R.styleable.tab_vertical_tabImageSrc, 0);
            iv_tab.setImageResource(tab_src);
        }
        if (typedArray.hasValue(R.styleable.tab_vertical_tabImageSize))
        {
            float tab_imageSize = typedArray.getDimension(R.styleable.tab_vertical_tabImageSize, 40);
            android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (int) tab_imageSize;
            layoutParams.height = (int) tab_imageSize;
            iv_tab.setLayoutParams(layoutParams);
        }
        if (typedArray.hasValue(R.styleable.tab_vertical_tabTextResId))
        {
            int tab_text = typedArray.getResourceId(R.styleable.tab_vertical_tabTextResId, 0);
            tv_tab.setText(tab_text);
        }
        if (typedArray.hasValue(R.styleable.tab_vertical_tabTextColor))
        {
            tv_tab.setTextColor(typedArray.getColorStateList(R.styleable.tab_vertical_tabTextColor));
        }
        if (typedArray.hasValue(R.styleable.tab_vertical_tabTextSize))
        {
            tv_tab.setTextSize(typedArray.getDimension(R.styleable.tab_vertical_tabTextSize, 10));
        }
        typedArray.recycle();
    }

    public void setImageSrcResId(int resId)
    {
        iv_tab.setImageResource(resId);
    }

    public void setText(int resId)
    {
        tv_tab.setText(resId);
    }

    public void setTextColor(int resId)
    {
        tv_tab.setTextColor(getResources().getColor(resId));
    }
}
