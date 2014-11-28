package com.lovebridge.chat.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import com.lovebridge.chat.view.tabs.Address;

class RecipientSpan extends ReplacementSpan
{
    private final Address address;
    private final String displayText;
    private final Paint textPaint;

    RecipientSpan(Address address1, String s, Paint paint)
    {
        address = address1;
        displayText = s;
        textPaint = paint;
    }

    public void draw(Canvas canvas, CharSequence charsequence, int i, int j, float f, int k, int l,
            int i1, Paint paint)
    {
        canvas.drawText(displayText, 0, displayText.length(), f, l, textPaint);
    }

    Address getAddress()
    {
        return address;
    }

    public int getSize(Paint paint, CharSequence charsequence, int i, int j, Paint.FontMetricsInt fontmetricsint)
    {
        return (int) Math.ceil(textPaint.measureText(displayText, 0, displayText.length()));
    }
}