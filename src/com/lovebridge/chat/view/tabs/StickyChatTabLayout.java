package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.lovebridge.R;
import com.lovebridge.chat.activity.MainActivity;
import com.nineoldandroids.view.ViewHelper;

public class StickyChatTabLayout extends RelativeLayout {
    private static final int NO_ACTIVE_TAB = -1;
    private int activeTabIndex;
    private View bottomOverlay;
    private boolean forceInvalidate;
    private float lastY;
    private ListView listView;
    private boolean overlayed;

    public StickyChatTabLayout(Context context) {
        super(context);
        this.activeTabIndex = NO_ACTIVE_TAB;
        this.lastY = -1f;
        this.setup();
    }

    public StickyChatTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activeTabIndex = NO_ACTIVE_TAB;
        this.lastY = -1f;
        this.setup();
    }

    public StickyChatTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.activeTabIndex = NO_ACTIVE_TAB;
        this.lastY = -1f;
        this.setup();
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean bool = this.overlayed ? false : true;
        return bool;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.overlayed;
    }

    public void refresh() {
        this.activeTabIndex = NO_ACTIVE_TAB;
        ListAdapter listAdapter = this.listView.getAdapter();
        int i = 0;
        while (i < listAdapter.getCount()) {
            ChatTabEntry chatTabEntry = (ChatTabEntry) listAdapter.getItem(i);
            if (chatTabEntry.getThreadId() == MainActivity.getActiveThreadId()) {
                this.configureViewForEntry(chatTabEntry);
                this.activeTabIndex = i;
                break;
            } else {
                ++i;
            }
        }
        this.updatePosition();
    }

    public void setRelatedViews(final ListView listView, View bottomOverlay) {
        this.bottomOverlay = bottomOverlay;
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int listViewLastHeight;

            public void onGlobalLayout() {
                int i = listView.getHeight();
                if (i != this.listViewLastHeight) {
                    StickyChatTabLayout.this.forceInvalidate = true;
                    StickyChatTabLayout.this.updatePosition();
                    this.listViewLastHeight = i;
                }
            }
        });
        this.listView = listView;
    }

    public void updatePosition() {
        int i1;
        int i = 8;
        if (this.activeTabIndex == NO_ACTIVE_TAB || this.listView == null || this.listView.getChildCount() == 0) {
            this.setVisibility(i);
        } else {
            this.setVisibility(0);
            this.bottomOverlay.setVisibility(i);
            if (this.activeTabIndex < this.listView.getFirstVisiblePosition()) {
                i1 = 0x80000000;
            } else if (this.activeTabIndex > this.listView.getLastVisiblePosition()) {
                i1 = 0x7FFFFFFF;
                this.bottomOverlay.setVisibility(0);
            } else {
                i1 = this.listView.getChildAt(this.activeTabIndex - this.listView.getFirstVisiblePosition()).getTop();
            }
            this.setYRelativeToListView(i1);
        }
    }

    private void configureViewForEntry(ChatTabEntry chatTabEntry) {
        this.removeAllViews();
        View view = chatTabEntry.getView(null);
        View view1 = view.findViewById(R.id.title);
        ((TextView) view1).setTypeface(null, 1);
        ViewHelper.setAlpha(view.findViewById(R.id.avatar), 1f);
        view.findViewById(R.id.unread_indicator).setVisibility(8);
        this.addView(view);
    }

    private void setYRelativeToListView(int y) {
        int i = 0x7FFFFFFF;
        boolean bool = false;
        int i1 = this.getResources().getDimensionPixelSize(R.dimen.tabs_new_chat_height)
                + this.getResources().getDimensionPixelSize(R.dimen.tabs_sticky_margin_top);
        if (y != i) {
            i = i1 + y;
        }
        int i2 = Math.min(
                Math.max(i, i1),
                this.listView.getHeight() + i1
                        - this.getResources().getDimensionPixelSize(R.dimen.tabs_tab_height));
        ViewGroup.LayoutParams viewGroup = this.getLayoutParams();
        if ((((float) i2)) != this.lastY || (this.forceInvalidate)) {
            ((RelativeLayout.LayoutParams) viewGroup).topMargin = i2;
            this.setLayoutParams(viewGroup);
            this.lastY = ((float) i2);
            this.forceInvalidate = false;
        }
        if (((RelativeLayout.LayoutParams) viewGroup).topMargin != i) {
            bool = true;
        }
        this.overlayed = bool;
    }

    private void setup() {
        setBackgroundResource(R.drawable.active_tab);
        this.setPadding(0, 0, 0, 0);
    }

    public static int getResourceId(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.resourceId;
    }
}
