package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.lovebridge.R;

public class FooterTabLayout extends RelativeLayout
{
    public interface Listener
    {
        void onHelpClick();

        void onOpenClick();

        void onSettingsClick();
    }

    Listener listener;

    public FooterTabLayout(Context context)
    {
        super(context);
        this.setup();
    }

    public FooterTabLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setup();
    }

    public FooterTabLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.setup();
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    private void setup()
    {
        FooterTabLayout.inflate(this.getContext(), R.layout.tab_footer, ((ViewGroup) this));
        this.findViewById(R.id.settingsIcon).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                FooterTabLayout.this.listener.onSettingsClick();
            }
        });
        this.findViewById(R.id.openIcon).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                FooterTabLayout.this.listener.onOpenClick();
            }
        });
        this.findViewById(R.id.feedback_icon).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                FooterTabLayout.this.listener.onHelpClick();
            }
        });
    }
}
