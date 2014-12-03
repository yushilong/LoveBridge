package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import com.lovebridge.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class TabsLayout extends LinearLayout
{
    private static final int ANIMATION_TIME = 0xB4;
    public static final double TABS_OPEN_PERCENT = 0.67;
    private GestureDetector gestureDetector;
    private View tabs;
    private int tabsClosedWidth;
    private int tabsOpenWidth;
    private ViewGroup.LayoutParams tabsParams;
    private int targetWidth;
    private ValueAnimator valueAnimator;

    public TabsLayout(Context context)
    {
        super(context);
        this.targetWidth = 0xFFFFFFFF;
    }

    public TabsLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.targetWidth = 0xFFFFFFFF;
    }

    public void animate(boolean open)
    {
        this.animate(open, 0f);
    }

    public void animate(boolean open, float velocity)
    {
        int i = open ? this.tabsOpenWidth : this.tabsClosedWidth;
        if (this.tabsParams.width != i)
        {
            if (this.valueAnimator != null && (this.valueAnimator.isRunning()))
            {
                if (this.targetWidth != i)
                {
                    this.valueAnimator.cancel();
                }
                else
                {
                    return;
                }
            }
            this.targetWidth = i;
            this.valueAnimator = ValueAnimator.ofInt(this.tabsParams.width, this.targetWidth);
            if (velocity > 0f)
            {
                int i1 = this.tabsParams.width;
                int i2 = open ? this.tabsOpenWidth : this.tabsClosedWidth;
                this.valueAnimator.setDuration((long) (Math.min(180f, Math.abs(i1 - i2) * 1000) / velocity));
                this.valueAnimator.setInterpolator(new LinearInterpolator());
            }
            else
            {
                this.valueAnimator.setDuration(ANIMATION_TIME);
            }
            this.valueAnimator.addUpdateListener(new AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    TabsLayout.this.setTabsWidth((Integer) animation.getAnimatedValue());
                }
            });
            this.valueAnimator.addListener(new AnimatorListener()
            {
                public void onAnimationCancel(Animator a)
                {
                }

                public void onAnimationEnd(Animator a)
                {
                    TabsLayout.this.valueAnimator = null;
                }

                public void onAnimationRepeat(Animator a)
                {
                }

                public void onAnimationStart(Animator a)
                {
                }
            });
            this.valueAnimator.start();
        }
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        this.resize();
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return this.gestureDetector.onTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        boolean bool = true;
        boolean bool1 = false;
        if (!this.gestureDetector.onTouchEvent(event))
        {
            switch (event.getActionMasked())
            {
                case 1:
                case 3:
                {
                    if (this.tabsParams.width <= this.tabsOpenWidth / 2)
                    {
                        bool = false;
                    }
                    this.animate(bool);
                    return bool1;
                }
            }
        }
        else
        {
            return true;
        }
        return bool1;
    }

    public void onWelcomeScroll(int width)
    {
        this.setTabsWidth(this.tabsClosedWidth + width);
    }

    public void toggle()
    {
        boolean bool = this.targetWidth != this.tabsOpenWidth ? true : false;
        this.animate(bool);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        this.setOrientation(0);
        this.gestureDetector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener()
        {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                boolean bool = true;
                boolean bool1 = false;
                if (Math.abs(velocityX) > Math.abs(velocityY))
                {
                    TabsLayout tabsLayout = TabsLayout.this;
                    if (velocityX > 0f)
                    {
                        bool1 = true;
                    }
                    tabsLayout.animate(bool1, Math.abs(velocityX));
                }
                else
                {
                    bool = false;
                }
                return bool;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                boolean bool = false;
                float f = 2f;
                if (Math.abs(distanceX) > Math.abs(distanceY) * f)
                {
                    TabsLayout.this.setTabsWidth(Math.round((TabsLayout.this.tabsParams.width) - distanceX));
                    bool = true;
                }
                else if (TabsLayout.this.tabsParams.width > TabsLayout.this.tabsClosedWidth
                        && e1.getX() > (TabsLayout.this.tabsParams.width)
                        && Math.abs(distanceY) > Math.abs(distanceX) * f)
                {
                    TabsLayout.this.animate(false);
                }
                return bool;
            }

            public boolean onSingleTapUp(MotionEvent e)
            {
                int[] array_i = new int[2];
                TabsLayout.this.findViewById(R.id.footer).getLocationInWindow(array_i);
                if (TabsLayout.this.tabsParams.width > TabsLayout.this.tabsClosedWidth
                        && (e.getY() < (((float) array_i[1])) || e.getX() > (((float) TabsLayout.this.tabsParams.width))))
                {
                    TabsLayout.this.animate(false);
                }
                return false;
            }
        });
        this.tabs = this.findViewById(R.id.tabs);
        this.tabsParams = this.tabs.getLayoutParams();
        this.resize();
    }

    private void resize()
    {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int i = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        if (this.tabsOpenWidth == 0)
        {
            this.tabsOpenWidth = ((int) ((((double) i)) * 0.67));
            this.tabsClosedWidth = ((int) this.getContext().getResources().getDimension(R.dimen.tabs_closed_width));
            this.setTabsWidth(this.tabsClosedWidth);
            View view = this.tabs.findViewById(R.id.tabs_inner_wrapper);
            ViewGroup.LayoutParams viewGroup = view.getLayoutParams();
            viewGroup.width = this.tabsOpenWidth;
            view.setLayoutParams(viewGroup);
        }
        View view1 = this.findViewById(R.id.content_holder);
        ViewGroup.LayoutParams viewGroup = view1.getLayoutParams();
        viewGroup.width = displayMetrics.widthPixels - this.tabsClosedWidth;
        view1.setLayoutParams(viewGroup);
    }

    private void setTabsWidth(int width)
    {
        this.tabsParams.width = width;
        if (this.tabsParams.width > this.tabsOpenWidth)
        {
            this.tabsParams.width = this.tabsOpenWidth;
        }
        else if (this.tabsParams.width < this.tabsClosedWidth)
        {
            this.tabsParams.width = this.tabsClosedWidth;
        }
        this.tabs.setLayoutParams(this.tabsParams);
    }
}
