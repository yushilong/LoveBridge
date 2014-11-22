
package com.lovebridge.chat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ImageSpan;

import com.lovebridge.chat.utils.EmojiUtils.Size;
import com.lovebridge.chat.view.ImageUtils;

public class EmojiSpan extends ImageSpan {
    private final Size size;

    public EmojiSpan(Context context, Bitmap bitmap, Size size) {
        super(context, bitmap, 0);
        this.size = size;
    }

    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        canvas.save();
        double d = this.size == Size.NORMAL ? 17.5 : 11.5;
        canvas.translate(x, ((float)(y - ImageUtils.dpToPx(d))));
        this.getDrawable().draw(canvas);
        canvas.restore();
    }

    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null && fm.top == 0) {
            if (this.size == Size.NORMAL) {
                fm.top = ImageUtils.dpToPx(-19);
                fm.ascent = ImageUtils.dpToPx(-16.5);
                fm.descent = ImageUtils.dpToPx(4.5);
                fm.bottom = ImageUtils.dpToPx(5);
            } else {
                fm.top = ImageUtils.dpToPx(-13);
                fm.ascent = ImageUtils.dpToPx(-11);
                fm.descent = ImageUtils.dpToPx(3);
                fm.bottom = ImageUtils.dpToPx(3.5);
            }
        }

        return this.getDrawable().getBounds().right;
    }
}
