
package com.lovebridge.chat.view.tabs;

import com.lovebridge.R;
import com.lovebridge.chat.fragment.TabsFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class NewChatTabLayout extends RelativeLayout implements TabsFragment.SelectableTab {
    public static interface Listener {

        public abstract void onNewChatClick();
    }

    private Listener listener;

    public NewChatTabLayout(Context context) {
        super(context);
        setup();
    }

    public NewChatTabLayout(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        setup();
    }

    public NewChatTabLayout(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
        setup();
    }

    private void setup() {
        inflate(getContext(), R.layout.tab_new_chat, this);
        setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                selectTab();
            }
        });
    }

    public void refresh() {
        View view = findViewById(R.id.active_background);
        View view1 = findViewById(R.id.imageView);
        view.setVisibility(0);
        view1.setBackgroundColor(0);
    }

    public void selectTab() {
        listener.onNewChatClick();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}