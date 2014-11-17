package com.lovebridge.library.view;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author yushilong
 * @date 2014-10-8 下午4:19:55
 * @version 1.0
 */
public class CopyTextView extends TextView
{
    private AlertDialog alertDialog;

    public CopyTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public CopyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public CopyTextView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        init(context, null);
    }

    public void init(final Context context, AttributeSet attrs)
    {
        this.setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                // TODO Auto-generated method stub
                final String text = getText().toString();
                if (!TextUtils.isEmpty(text))
                {
                    if (alertDialog == null)
                    {
                        String[] items = { "复制" };
                        alertDialog = new AlertDialog.Builder(context).setItems(items,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // TODO Auto-generated method
                                        // stub
                                        dialog.dismiss();
                                        ClipboardManager clipboardManager = (ClipboardManager) context
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("simpleText", text);
                                        clipboardManager.setPrimaryClip(clip);
                                    }
                                }).create();
                    }
                    alertDialog.show();
                    return true;
                }
                return false;
            }
        });
    }
}
