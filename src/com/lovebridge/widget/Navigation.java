package com.lovebridge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lovebridge.R;

/**
 * Created by yushilong on 14-11-16.
 */
public class Navigation extends LinearLayout{
    public Button bt_navigation_left;
    public Button bt_navigation_right;
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
        bt_navigation_left = (Button)viewGroup.findViewById(R.id.bt_navigation_left);
        bt_navigation_right = (Button)viewGroup.findViewById(R.id.bt_navigation_right);
        tv_navigation_title = (Button)viewGroup.findViewById(R.id.tv_navigation_title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigationStyle);
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationLeftButtonBackground)){
            bt_navigation_left.setBackgroundDrawable(typedArray.getDrawable(R.styleable.navigationStyle_navigationLeftButtonBackground));
        }
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationRightButtonBackground)){
            bt_navigation_right.setBackgroundDrawable(typedArray.getDrawable(R.styleable.navigationStyle_navigationLeftButtonBackground));
        }
        if (typedArray.hasValue(R.styleable.navigationStyle_navigationTitleResId)){
            tv_navigation_title.setText(typedArray.getInteger(R.styleable.navigationStyle_navigationTitleResId,-1));
        }
        typedArray.recycle();
    }
}
