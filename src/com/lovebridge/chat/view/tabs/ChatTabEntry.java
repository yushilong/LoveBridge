package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.easemob.chat.EMConversation;
import com.lovebridge.chat.fragment.TabsFragment.SelectableTab;

public class ChatTabEntry implements SelectableTab
{
    public long threadId;
    private Context context;
    private Listener listener;
    private EMConversation conversation;

    public ChatTabEntry()
    {
    }

    public ChatTabEntry(FragmentActivity activity, EMConversation conversation, long threadId)
    {
        super();
        this.context = activity;
        this.listener = ((Listener) activity);
        this.conversation = conversation;
        this.threadId = threadId;
    }

    public void deleteChat()
    {
        this.listener.onChatDelete(this.threadId);
    }

    public EMConversation getEMConversation()
    {
        return this.conversation;
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

    public void selectTab(ChatTabEntry localChatTabEntry)
    {
        this.listener.onChatClick(localChatTabEntry, false);
    }

    public interface Listener
    {
        void onChatClick(ChatTabEntry chatTabEntry, boolean arg2);

        void onChatDelete(long arg1);
    }
}
