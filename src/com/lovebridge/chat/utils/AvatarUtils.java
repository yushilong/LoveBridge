package com.lovebridge.chat.utils;

import android.content.Context;
import android.graphics.*;
import android.media.ThumbnailUtils;
import com.lovebridge.R;
import com.lovebridge.chat.view.tabs.Address;
import com.lovebridge.chat.view.tabs.Addresses;

import java.util.regex.Pattern;

public class AvatarUtils
{
    private static Pattern LONG_PATTERN = null;
    private static Pattern SHORT_PATTERN;

    static
    {
        AvatarUtils.LONG_PATTERN = Pattern.compile("\\+\\d{1,5}|\\d{1,6}");
        AvatarUtils.SHORT_PATTERN = Pattern.compile("\\+\\d{1,3}|\\d{1,4}");
    }

    public AvatarUtils()
    {
        super();
    }

    public static Bitmap getCircularBitmap(Context context, Addresses addresses)
    {
        int i = 3;
        float f = 48f;
        int i1 = 2;
        Bitmap bitmap = Bitmap.createBitmap(0x60, 0x60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(7);
        int i2 = context.getResources().getColor(R.color.tab_avatar_background);
        paint.setColor(i2);
        canvas.drawCircle(f, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setTextAlign(Paint.Align.CENTER);
        switch (addresses.size())
        {
            case 1:
            {
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(0), 0);
                break;
            }
            case 2:
            {
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(0), 1);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(1), 4);
                break;
            }
            case 3:
            {
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(0), 1);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(1), 5);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(i1), 6);
                break;
            }
            case 4:
            {
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(0), i1);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(1), 5);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(i1), i);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(i), 6);
                break;
            }
            default:
            {
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(0), i1);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(1), 5);
                AvatarUtils.drawAvatar(canvas, paint, addresses.getAddress(i1), i);
                paint.setColor(context.getResources().getColor(R.color.white));
                paint.setTextSize(32f);
                canvas.drawText("+", 68f, ((float) (AvatarUtils.getTextHeightOffset(paint, "+") + 0x46)), paint);
                break;
            }
        }
        return bitmap;
    }

    public static Bitmap getCircularBitmap(Context themedContext, Address address)
    {
        return AvatarUtils.getCircularBitmap(themedContext, new Addresses(address));
    }

    static Pattern access$0()
    {
        return AvatarUtils.LONG_PATTERN;
    }

    static Pattern access$1()
    {
        return AvatarUtils.SHORT_PATTERN;
    }

    private static void drawAvatar(Canvas canvas, Paint paint, Address address, int area)
    {
        int i = 0x60;
        int i1 = 0x30;
        Rect rect = new Rect(0, 0, i, i);
        switch (area)
        {
            case 1:
            {
                rect.set(0, 0, i1, i);
                break;
            }
            case 2:
            {
                rect.set(0, 0, i1, i1);
                break;
            }
            case 3:
            {
                rect.set(0, i1, i1, i);
                break;
            }
            case 4:
            {
                rect.set(i1, 0, i, i);
                break;
            }
            case 5:
            {
                rect.set(i1, 0, i, i1);
                break;
            }
            case 6:
            {
                rect.set(i1, i1, i, i);
                break;
            }
        }
        Bitmap bitmap = address.getFullBitmap(false);
        if (bitmap != null)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, i, i, 2);
            Rect rect1 = null;
            if ((area == 1 || area == 4) && bitmap.getWidth() > i1)
            {
                rect1 = new Rect(bitmap.getWidth() / 4, 0, bitmap.getWidth() * 3 / 4, bitmap.getHeight());
            }
            canvas.drawBitmap(bitmap, rect1, rect, paint);
            bitmap.recycle();
        }
    }

    private static int getTextHeightOffset(Paint paint, String text)
    {
        int i1;
        int i = 2;
        Rect rect = new Rect();
        if (text.length() == i)
        {
            paint.getTextBounds(text.substring(0, 1), 0, 1, rect);
            i1 = rect.top;
            if (text.length() == i)
            {
                paint.getTextBounds(text.substring(1, i), 0, 1, rect);
                i1 = Math.max(rect.top, i1);
            }
        }
        else
        {
            paint.getTextBounds(text, 0, text.length(), rect);
            i1 = rect.top;
        }
        return Math.abs(i1 / 2);
    }
}
