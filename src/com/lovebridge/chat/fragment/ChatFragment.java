package com.lovebridge.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.lovebridge.R;
import com.lovebridge.chat.adapter.MessageAdapter;
import com.lovebridge.chat.view.tabs.Addresses;
import com.lovebridge.library.YARFragment;

import java.util.List;

public class ChatFragment extends YARFragment implements View.OnClickListener
{
    private final int pageSize = 20;
    private ComposerFragment composer;
    private long threadId;
    private ListView listView;
    private String toChatUsername;
    private TextView chatUsername_tv;
    private EMConversation conversation;
    private MessageAdapter adapter;
    private boolean haveMoreData = true;
    private ProgressBar loadMorePB;
    private boolean isLoading;

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
        listView = (ListView) containerView.findViewById(R.id.chat_listView);
        chatUsername_tv = (TextView) containerView.findViewById(R.id.name);
        loadMorePB = (ProgressBar) containerView.findViewById(R.id.pb_load_more);
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
        Bundle bundle = getArguments();
        this.threadId = bundle.getLong("thread_id", 0L);
        if (this.threadId == 0L)
            throw new IllegalArgumentException("no thread id in chat");
        toChatUsername = bundle.getString("userId");
        chatUsername_tv.setText(toChatUsername);
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnsetMsgCount();
        adapter = new MessageAdapter(this.getActivity(), toChatUsername, 1);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0)
        {
            listView.setSelection(count - 1);
        }
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

    @Override public void onClick(View view)
    {
    }

    /**
     *  滑动监听listener
     */
    private class ListScrollListener implements AbsListView.OnScrollListener
    {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            switch (scrollState)
            {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isLoading && haveMoreData)
                    {
                        loadMorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try
                        {
                            messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pageSize);
                        }
                        catch (Exception e1)
                        {
                            loadMorePB.setVisibility(View.GONE);
                            return;
                        }
                        try
                        {
                            Thread.sleep(300);
                        }
                        catch (InterruptedException e)
                        {
                        }
                        if (messages.size() != 0)
                        {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                            if (messages.size() != pageSize)
                                haveMoreData = false;
                        }
                        else
                        {
                            haveMoreData = false;
                        }
                        loadMorePB.setVisibility(View.GONE);
                        isLoading = false;
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {
        }
    }
}
