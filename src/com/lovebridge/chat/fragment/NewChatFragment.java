package com.lovebridge.chat.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.lovebridge.R;
import com.lovebridge.chat.utils.SoundUtils;
import com.lovebridge.chat.view.RecipientsEditor;
import com.lovebridge.chat.view.tabs.Addresses;
import com.lovebridge.library.YARFragment;

public class NewChatFragment extends YARFragment {
    private static final String ARG_ADDRESSES = "addresses";
    private static final String ARG_DEFAULT_TEXT = "default_text";
    private static FocusedTextView lastFocusedObject;
    private static Editable recipientsDraft = null;
    private static int selectionEnd = 0;
    private static int selectionStart = 0;
    private final OttStatusChangeEvents.Listener listener = new OttStatusChangeEvents.Listener() {
        public void onStatusChanged() {
            reloadOttViews();
        }
    };
    private final DataSetObserver recipientsAdapterObserver = new DataSetObserver() {
        public void onChanged() {
            onAddressesChanged();
        }

        public void onInvalidated() {
            onAddressesChanged();
        }
    };
    private final int pageSize = 20;
    private String toChatUsername;

    private boolean haveMoreData = true;
    private ProgressBar loadMorePB;
    private boolean isLoading;
    private ComposerFragment composer;
    private TextView networkIndicator;
    private RecipientsEditor recipientsEditor;
    private ListView listView;

    public NewChatFragment() {
    }

    public static NewChatFragment newInstance(long threadId, boolean paramBoolean, String userId) {
        NewChatFragment localChatFragment = new NewChatFragment();
        Bundle localBundle = new Bundle();
        localBundle.putLong("thread_id", threadId);
        localBundle.putBoolean("show_keyboard", paramBoolean);
        localBundle.putString("userId", userId);
        localChatFragment.setArguments(localBundle);
        return localChatFragment;
    }

    private void onAddressesChanged() {
    }

    private void reloadOttViews() {
        updateNetworkIndicator();
    }

    private void retrieveEligibility(Addresses addresses) {
    }

    private void updateNetworkIndicator() {
    }

    public void onActivityResult(int i, int j, Intent intent) {
        super.onActivityResult(i, j, intent);
        composer.handleActivityResult(i, j, intent);
    }

    @Override
    public int doGetContentViewId() {
        return R.layout.fragment_new_chat;
    }

    @Override
    public void doInitSubViews(View containerView) {
        listView = (ListView) containerView.findViewById(R.id.chat_listView);
        //        chatUsername_tv = (TextView) containerView.findViewById(R.id.name);
        loadMorePB = (ProgressBar) containerView.findViewById(R.id.pb_load_more);
        networkIndicator = (TextView) containerView.findViewById(R.id.network_indicator);
        recipientsEditor = (RecipientsEditor) containerView.findViewById(R.id.recipients);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view1, int i, long l) {
                //                recipientsEditor.replaceText(adapter.getFilter().convertResultToString(adapter.getItem(i)));
                recipientsEditor.requestFocus();
                ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(recipientsEditor, 1);
            }
        });
        recipientsEditor.setListener(new RecipientsEditor.Listener() {
            public void onAddressesChanged() {
                updateNetworkIndicator();
            }
        });
    }

    @Override
    public void doInitDataes() {
        Bundle bundle = getArguments();
        String s;
        if (bundle != null) {
            recipientsEditor.setText(null);
            lastFocusedObject = FocusedTextView.COMPOSER;
            s = bundle.getString("default_text");
            toChatUsername = bundle.getString("userId");
        } else {
            recipientsEditor.setText(recipientsDraft);
            recipientsEditor.setSelection(selectionStart, selectionEnd);
            s = null;
        }
        recipientsEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view1, boolean flag) {
                if (view1.equals(recipientsEditor) && flag) {
                    NewChatFragment.lastFocusedObject = FocusedTextView.RECIPIENT_EDITOR;
                }
            }
        });
        composer = (ComposerFragment) getFragmentManager().findFragmentById(R.id.composer);
        composer.getView().setVisibility(View.VISIBLE);
        composer.setChat(0L, s, new ComposerFragment.Listener() {
            public Addresses getAddresses() {
                return recipientsEditor.getAddresses();
            }

            public void onTextViewFocus() {
                NewChatFragment.lastFocusedObject = FocusedTextView.COMPOSER;
            }

            public void sentMessage(long l, boolean flag, ComposerFragment.Listener.PlaceholderType placeholdertype, String content) {
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

                    // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                    listView.setSelection(listView.getCount() - 1);
                    recipientsEditor.setText(null);
                    NewChatFragment.lastFocusedObject = FocusedTextView.NO_FOCUS;
                }
            }

            public void startActivityForResultHelper(Intent intent, int i) {
                startActivityForResult(intent, i);
            }
        });
        OttStatusChangeEvents.addListener(listener);
        //        chatUsername_tv.setText(toChatUsername);

        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }
    }

    @Override
    public void doAfter() {
    }

    public void onDestroyView() {
        super.onDestroyView();
        recipientsDraft = recipientsEditor.getText();
        selectionStart = recipientsEditor.getSelectionStart();
        selectionEnd = recipientsEditor.getSelectionEnd();
        //        recipientsEditor.getAdapter().unregisterDataSetObserver(recipientsAdapterObserver);
        recipientsEditor.setListener(null);
        OttStatusChangeEvents.removeListener(listener);
    }

    public void onStart() {
        super.onStart();
        reloadOttViews();
        if (lastFocusedObject == FocusedTextView.COMPOSER) {
            composer.showKeyboard();
            return;
        } else {
            recipientsEditor.requestFocus();
            ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(recipientsEditor, 1);
            return;
        }
    }

    static {
        lastFocusedObject = FocusedTextView.NO_FOCUS;
    }

    public void refresh() {
        listView.setSelection(listView.getCount() - 1);
    }

    public void refresh2() {
    }

    public enum FocusedTextView {
        NO_FOCUS, RECIPIENT_EDITOR, COMPOSER
    }

    /**
     * 滑动监听listener
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    composer.hideKeyboard();
                    ((EmojiPickerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.emoji_picker_fragment)).hide();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isLoading && haveMoreData) {
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }
}
