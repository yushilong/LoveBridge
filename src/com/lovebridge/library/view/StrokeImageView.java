
package com.lovebridge.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 * 加边框的imageView
 */
public class StrokeImageView extends ImageView
{
    public StrokeImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public StrokeImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public StrokeImageView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //获取控件需要重新绘制的区域
        Rect rect = canvas.getClipBounds();
        rect.bottom--;
        rect.right--;
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(rect, paint);
    }
}
