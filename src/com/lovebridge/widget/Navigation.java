package com.lovebridge.widget;

import android.content.Context;
import android.content.res.TypedArray;
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
public class Navigation extends LinearLayout{
    public ImageView iv_navigation_left;
    public ImageView iv_navigation_right;
    public TextView tv_navigation_title;
    public Navigation(Context context) {
        super(context);
        initView(context,null);
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context,attrs);
    }
    public void initView(Context context, AttributeSet attrs){
        ViewGroup viewGroup = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.yar_navigation,this);
        iv_navigation_left = (ImageView)viewGroup.findViewById(R.id.iv_navigation_left);
        iv_navigation_right = (ImageView)viewGroup.findViewById(R.id.iv_navigation_right);
        tv_navigation_title = (TextView)viewGroup.findViewById(R.id.tv_navigation_title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigationStyle);
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationLeftButtonBackground)){
            iv_navigation_left.setImageDrawable(typedArray.getDrawable(R.styleable.navigationStyle_navigationLeftButtonBackground));
        }
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationRightButtonBackground)){
            iv_navigation_right.setImageDrawable(typedArray.getDrawable(R.styleable.navigationStyle_navigationLeftButtonBackground));
        }
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationTitleResId)){
            tv_navigation_title.setText(typedArray.getInteger(R.styleable.navigationStyle_navigationTitleResId,-1));
        }
        typedArray.recycle();
    }
}
