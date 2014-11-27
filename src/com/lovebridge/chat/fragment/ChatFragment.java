package com.lovebridge.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.lovebridge.R;
import com.lovebridge.chat.view.tabs.Addresses;
import com.lovebridge.library.YARBaseFragment;

public class ChatFragment extends YARBaseFragment
{
    private ComposerFragment composer;
    private long threadId;

    public static ChatFragment newInstance(long paramLong, boolean paramBoolean, String paramString)
    {
        ChatFragment localChatFragment = new ChatFragment();
        Bundle localBundle = new Bundle();
        localBundle.putLong("thread_id", paramLong);
        localBundle.putBoolean("show_keyboard", paramBoolean);
        localBundle.putString("default_text", paramString);
        localChatFragment.setArguments(localBundle);
        return localChatFragment;
    }

    public static ChatFragment newInstance(String[] param, String paramString)
    {
        ChatFragment localNewChatFragment = new ChatFragment();
        Bundle localBundle = new Bundle();
        localBundle.putStringArray("addresses", new String[] { "sms" , "smsto" , "mms" , "mmsto" });
        localBundle.putString("default_text", "default_text");
        if (!localBundle.isEmpty())
            localNewChatFragment.setArguments(localBundle);
        return localNewChatFragment;
    }

    @Override
    public int doGetContentViewId()
    {
        return R.layout.fragment_chat;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        this.composer = ((ComposerFragment) getFragmentManager().findFragmentById(R.id.composer));
        this.composer.setChat(this.threadId, getArguments().getString("default_text"), new ComposerFragment.Listener()
        {
            public Addresses getAddresses()
            {
                return null;
            }

            public void onTextViewFocus()
            {
            }

            @Override public void sentMessage(long arg1, boolean arg2, PlaceholderType arg3, String arg4)
            {
            }

            @Override public void startActivityForResultHelper(Intent arg1, int arg2)
            {
            }
        });
    }

    @Override
    public void doInitDataes()
    {
        this.threadId = getArguments().getLong("thread_id", 0L);
        if (this.threadId == 0L)
            throw new IllegalArgumentException("no thread id in chat");
    }

    @Override
    public void doAfter()
    {
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public View getNavigationView()
    {
        return super.getNavigationView();
    }
}
