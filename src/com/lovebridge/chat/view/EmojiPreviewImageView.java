package com.lovebridge.chat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.lovebridge.chat.utils.EmojiUtils;
import com.lovebridge.chat.utils.EmojiUtils.Size;

public class EmojiPreviewImageView extends ImageView
{
    public EmojiPreviewImageView(Context context)
    {
        super(context);
    }

    public EmojiPreviewImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EmojiPreviewImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setEmojiCodePoint(Integer codePoint)
    {
        Bitmap bitmap = EmojiUtils.getCachedBitmap(codePoint.intValue(), Size.PICKER);
        this.setImageBitmap(bitmap);
    }
}
