package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.TabsFragment;

public class NewChatTabLayout extends RelativeLayout implements TabsFragment.SelectableTab
{
    private Listener listener;

    public NewChatTabLayout(Context context)
    {
        super(context);
        setup();
    }

    public NewChatTabLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        setup();
    }

    public NewChatTabLayout(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setup();
    }

    public static int getResourceId(Context context, int attrId)
    {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getColor(Context context, int attrId)
    {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    @Override
    public void selectTab(ChatTabEntry localChatTabEntry)
    {
    }

    private void setup()
    {
        inflate(getContext(), R.layout.tab_new_chat, this);
        // this.setBackgroundResource(getResourceId(this.getContext(),
        // R.attr.drawable_bg_tab_solid_top));
        setBackgroundResource(R.drawable.bg_tab_solid_top);
        setOnClickListener(new android.view.View.OnClickListener()
        {
            public void onClick(View view)
            {
                selectTab();
            }
        });
    }

    public void refresh(long threadId)
    {
        View view = findViewById(R.id.active_background);
        View view1 = findViewById(R.id.imageView);
        if (-1 == threadId)
        {
            view.setVisibility(View.VISIBLE);
            view1.setBackgroundColor(0);
        }
        else
        {
            view.setVisibility(View.INVISIBLE);
            view1.setBackgroundColor(getColor(this.getContext(), R.attr.color_tab_background));
        }
    }

    public void selectTab()
    {
        listener.onNewChatClick();
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public static interface Listener
    {
        public abstract void onNewChatClick();
    }
}