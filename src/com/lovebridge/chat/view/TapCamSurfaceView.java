package com.lovebridge.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TapCamSurfaceView extends SurfaceView
{
    public TapCamSurfaceView(Context context)
    {
        super(context);
    }

    public TapCamSurfaceView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public TapCamSurfaceView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
    }

    static void setSurfaceHolderType(SurfaceHolder surfaceholder)
    {
        surfaceholder.setType(3);
    }

    public SurfaceHolder getHolder()
    {
        SurfaceHolder surfaceholder = super.getHolder();
        setSurfaceHolderType(surfaceholder);
        return surfaceholder;
    }

    protected void onMeasure(int i, int j)
    {
        android.view.ViewGroup.LayoutParams layoutparams = getLayoutParams();
        super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.width, 0x80000000),
                android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.height, 0x80000000));
    }
}