package com.lovebridge.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.lovebridge.R;
import com.lovebridge.chat.adapter.MessageAdapter;
import com.lovebridge.chat.utils.SoundUtils;
import com.lovebridge.chat.view.tabs.Address;
import com.lovebridge.chat.view.tabs.Addresses;
import com.lovebridge.chat.view.tabs.ChatTabEntry;
import com.lovebridge.library.YARFragment;

import java.util.List;

public class ChatFragment extends YARFragment implements View.OnClickListener {
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

    public static ChatFragment newInstance(ChatTabEntry chatTabEntry, boolean paramBoolean, String userId) {
        ChatFragment localChatFragment = new ChatFragment();
        Bundle localBundle = new Bundle();
        localBundle.putLong("thread_id", chatTabEntry.threadId);
        localBundle.putBoolean("show_keyboard", paramBoolean);
        localBundle.putString("userId", userId);
        localChatFragment.setArguments(localBundle);
        return localChatFragment;
    }


    @Override
    public int doGetContentViewId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void doInitSubViews(View containerView) {
        listView = (ListView) containerView.findViewById(R.id.chat_listView);
        chatUsername_tv = (TextView) containerView.findViewById(R.id.name);
        loadMorePB = (ProgressBar) containerView.findViewById(R.id.pb_load_more);
        this.composer = ((ComposerFragment) getFragmentManager().findFragmentById(R.id.composer));

    }

    @Override
    public void doInitDataes() {
        Bundle bundle = getArguments();
        this.threadId = bundle.getLong("thread_id", 0L);
        if (this.threadId == 0L)
            throw new IllegalArgumentException("no thread id in chat");
        toChatUsername = bundle.getString("userId");
//        chatUsername_tv.setText(toChatUsername);
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnsetMsgCount();
        adapter = new MessageAdapter(this.getActivity(), toChatUsername, 1);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }
        this.composer.setChat(this.threadId, getArguments().getString("default_text"), new ComposerFragment.Listener() {
            public Addresses getAddresses() {
                return new Addresses(new Address("number"));
            }

            public void onTextViewFocus() {
            }

            @Override
            public void sentMessage(long arg1, boolean arg2, PlaceholderType arg3, String content) {
                String toChatUsername = composer.getToChatUsername();
                Log.i("chat", "====" + toChatUsername);
                if (SoundUtils.shouldPlayChatSounds(getActivity())) {
                    SoundUtils.playSound(getActivity(), R.raw.send_message);
                }
                if (content.length() > 0) {
                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    TextMessageBody txtBody = new TextMessageBody(content);
                    // 设置消息body
                    message.addBody(txtBody);
                    // 设置要发给谁,用户username或者群聊groupid
                    message.setReceipt(toChatUsername);
                    // 把messgage加到conversation中
                    conversation.addMessage(message);
                    // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                    adapter.refresh();
                    scrollToBottom();
                }
            }

            @Override
            public void startActivityForResultHelper(Intent arg1, int arg2) {
            }
        });
    }

    @Override
    public void doAfter() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("chat", "=======" + toChatUsername);
        composer.setToChatUsername(toChatUsername);
    }

    @Override
    public View getNavigationView() {
        return super.getNavigationView();
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 滑动监听listener
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isLoading && haveMoreData) {
                        loadMorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try {
                            messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pageSize);
                        } catch (Exception e1) {
                            loadMorePB.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                        if (messages.size() != 0) {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                            if (messages.size() != pageSize)
                                haveMoreData = false;
                        } else {
                            haveMoreData = false;
                        }
                        loadMorePB.setVisibility(View.GONE);
                        isLoading = false;
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    private void scrollToBottom() {
        this.listView.setSelection(this.listView.getCount() - 1);
    }

    public void refresh() {
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
    }

    public void refresh2() {
        adapter.notifyDataSetChanged();
    }
}
