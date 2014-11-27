package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.lovebridge.chat.fragment.TabsFragment.SelectableTab;

public class ChatTabEntry implements SelectableTab
{
    public interface Listener
    {
        void onChatClick(long arg1, boolean arg2);

        void onChatDelete(long arg1);
    }

    private final Addresses addresses;
    private final Context context;
    private Listener listener;
    private final Message message;
    private final long threadId;

    public ChatTabEntry(FragmentActivity activity, Message message)
    {
        super();
        this.context = ((Context) activity);
        this.listener = ((Listener) activity);
        this.message = message;
        this.threadId = message.threadId;
        this.addresses = message.getAddresses();
    }

    public void deleteChat()
    {
        this.listener.onChatDelete(this.threadId);
    }

    public Addresses getAddresses()
    {
        return this.addresses;
    }

    public Message getMessage()
    {
        return this.message;
    }

    public long getThreadId()
    {
        return this.threadId;
    }

    public View getView(View view)
    {
        if (view == null)
        {
            view = new ChatTabLayout(this.context);
        }
        ((ChatTabLayout) view).loadChatTabContents(this);
        return view;
    }

    public void selectTab()
    {
        this.listener.onChatClick(this.threadId, false);
    }
}
