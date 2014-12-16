package com.lovebridge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lovebridge.R;
import com.lovebridge.library.tools.YARDisplayUtil;

/**
 *author: lscm
 *date: 2014/12/10 16:37
 */
public class CustomItem extends LinearLayout
{
    public CustomItem(Context context)
    {
        super(context);
        initView(context, null);
    }

    public CustomItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomItem(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public void initView(final Context context, AttributeSet attrs)
    {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.custom_mineitem, this, true);
        ImageView iv_leftIcon = (ImageView) viewGroup.findViewById(R.id.iv_lefticon);
        TextView tv_middleUp = (TextView) viewGroup.findViewById(R.id.tv_middleUp);
        TextView tv_middleDown = (TextView) viewGroup.findViewById(R.id.tv_middleDown);
        TextView tv_right = (TextView) viewGroup.findViewById(R.id.tv_right);
        ImageView iv_rightIcon = (ImageView) viewGroup.findViewById(R.id.iv_righticon);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mineitem);
        if (typedArray.hasValue(R.styleable.mineitem_left_icon))
            iv_leftIcon.setImageResource(typedArray.getResourceId(R.styleable.mineitem_left_icon, 0));
        if (typedArray.hasValue(R.styleable.mineitem_middle_upText))
            tv_middleUp.setText(typedArray.getText(R.styleable.mineitem_middle_upText));
        if (typedArray.hasValue(R.styleable.mineitem_middle_downText))
            tv_middleDown.setText(typedArray.getText(R.styleable.mineitem_middle_downText));
        if (typedArray.hasValue(R.styleable.mineitem_middle_upTextColor))
            tv_middleUp.setTextColor(typedArray.getColor(R.styleable.mineitem_middle_upTextColor, android.R.color.black));
        if (typedArray.hasValue(R.styleable.mineitem_middle_downTextColor))
            tv_middleDown.setTextColor(typedArray.getColor(R.styleable.mineitem_middle_downTextColor, android.R.color.black));
        if (typedArray.hasValue(R.styleable.mineitem_middle_down_visible))
            tv_middleDown.setVisibility(typedArray.getBoolean(R.styleable.mineitem_middle_down_visible, false) ? VISIBLE : GONE);
        if (typedArray.hasValue(R.styleable.mineitem_right_text))
            tv_right.setText(typedArray.getText(R.styleable.mineitem_right_text));
        if (typedArray.hasValue(R.styleable.mineitem_right_icon))
            iv_rightIcon.setImageResource(typedArray.getResourceId(R.styleable.mineitem_right_icon, 0));
        if (typedArray.hasValue(R.styleable.mineitem_left_iconSize))
        {
            RelativeLayout.LayoutParams leftIconLayoutParams = (RelativeLayout.LayoutParams) iv_leftIcon.getLayoutParams();
            int imgSize = (int) typedArray.getDimension(R.styleable.mineitem_left_iconSize, YARDisplayUtil.dip2px(context, 20));
            leftIconLayoutParams.width = imgSize;
            leftIconLayoutParams.height = imgSize;
            iv_leftIcon.setLayoutParams(leftIconLayoutParams);
        }
        if (typedArray.hasValue(R.styleable.mineitem_right_iconSize))
        {
            RelativeLayout.LayoutParams rightIconLayoutParams = (RelativeLayout.LayoutParams) iv_rightIcon.getLayoutParams();
            int imgSize = (int) typedArray.getDimension(R.styleable.mineitem_right_iconSize, YARDisplayUtil.dip2px(context, 20));
            rightIconLayoutParams.width = imgSize;
            rightIconLayoutParams.height = imgSize;
            iv_rightIcon.setLayoutParams(rightIconLayoutParams);
        }
        typedArray.recycle();
    }
}
