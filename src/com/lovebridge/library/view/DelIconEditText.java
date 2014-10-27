
package com.lovebridge.library.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lovebridge.R;

/**
 * @author yushilong
 * @date 2014-10-8 下午3:54:30
 * @version 1.0
 */
public class DelIconEditText extends RelativeLayout
{
    private EditText et_del;
    private ImageView iv_del;

    public DelIconEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public DelIconEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public DelIconEditText(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        init(context, null);
    }

    public void init(Context context , AttributeSet attrs)
    {
        LayoutInflater.from(context).inflate(R.layout.del_edittext, this, true);
        et_del = (EditText) findViewById(R.id.deledittext);
        iv_del = (ImageView) findViewById(R.id.delimageview);
        et_del.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count)
            {
                // TODO Auto-generated method stub
                if (s != null && s.length() > 0)
                {
                    if (!iv_del.isShown())
                    {
                        iv_del.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if (iv_del.isShown())
                    {
                        iv_del.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
            }
        });
        iv_del.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                et_del.setText(null);
            }
        });
    }

    public EditText getEditText()
    {
        return et_del;
    }

    public ImageView getImageView()
    {
        return iv_del;
    }
}
