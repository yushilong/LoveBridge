package com.lovebridge.chat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.ChatFragment;
import com.lovebridge.chat.fragment.EmojiPickerFragment;
import com.lovebridge.chat.fragment.NewChatFragment;
import com.lovebridge.chat.fragment.TabsFragment;
import com.lovebridge.chat.view.tabs.ChatTabEntry;
import com.lovebridge.chat.view.tabs.FooterTabLayout;
import com.lovebridge.chat.view.tabs.NewChatTabLayout;
import com.lovebridge.chat.view.tabs.TabsLayout;
import com.lovebridge.library.YARActivity;
import com.lovebridge.library.YARFragment;

public class MainActivity extends YARActivity implements EmojiPickerFragment.Listener, ChatTabEntry.Listener, FooterTabLayout.Listener,
        NewChatTabLayout.Listener, TabsFragment.Listener
{
    public static String ACTION_RESTART = null;
    public static String EXTRA_SHOW_KEYBOARD = null;
    public static String EXTRA_THREAD_ID = null;
    private ViewGroup content;
    private TabsFragment tabs;
    private TabsLayout tabsLayout;

    static
    {
        MainActivity.ACTION_RESTART = String.valueOf(MainActivity.class.getPackage().getName()) + ".activity_restart";
        MainActivity.EXTRA_SHOW_KEYBOARD = String.valueOf(MainActivity.class.getPackage().getName()) + ".show_keyboard";
        MainActivity.EXTRA_THREAD_ID = String.valueOf(MainActivity.class.getPackage().getName()) + ".thread_id";
    }

    private NewMessageBroadcastReceiver receiver;
    private String toChatUsername;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private YARFragment mCurrentFragment;
    /**
     * 消息送达BroadcastReceiver
     */
    public BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null)
            {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null)
                {
                    msg.isDelivered = true;
                }
            }
            abortBroadcast();
            if (mCurrentFragment instanceof NewChatFragment)
            {
                NewChatFragment chatFragment = (NewChatFragment) mCurrentFragment;
                chatFragment.refresh2();
            }
            else if (mCurrentFragment instanceof ChatFragment)
            {
                ChatFragment chatFragment = (ChatFragment) mCurrentFragment;
                chatFragment.refresh2();
            }
            //            adapter.notifyDataSetChanged();
        }
    };
    /**
     * 消息回执BroadcastReceiver
     */
    public BroadcastReceiver ackMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null)
            {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null)
                {
                    msg.isAcked = true;
                }
            }
            abortBroadcast();
            if (mCurrentFragment instanceof NewChatFragment)
            {
                NewChatFragment chatFragment = (NewChatFragment) mCurrentFragment;
                chatFragment.refresh2();
            }
            else if (mCurrentFragment instanceof ChatFragment)
            {
                ChatFragment chatFragment = (ChatFragment) mCurrentFragment;
                chatFragment.refresh2();
            }
        }
    };

    private static String makeFragmentName(long id)
    {
        return "android:switcher:" + id;
    }

    public void makeRoomForEmojiPicker(int keyboardHeight)
    {
        if (this.tabsLayout != null)
        {
            Rect rect = new Rect();
            this.findViewById(Window.ID_ANDROID_CONTENT).getWindowVisibleDisplayFrame(rect);
            ViewGroup.LayoutParams viewGroup = this.tabsLayout.getLayoutParams();
            ((RelativeLayout.LayoutParams) viewGroup).height = this.getResources().getDisplayMetrics().heightPixels
                    - keyboardHeight - rect.top;
            this.tabsLayout.setLayoutParams(viewGroup);
            View view = this.findViewById(R.id.emoji_shadow);
            int i = keyboardHeight != 0 ? View.VISIBLE : View.GONE;
            view.setVisibility(i);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode)
        {
            case 100:
            {
                if (resultCode != 1)
                {
                    return;
                }
                break;
            }
        }
    }

    public void onChatClick(long threadId, boolean showKeyboard)
    {
    }

    @Override
    public void onChatClick(ChatTabEntry chatTabEntry, boolean showKeyboard)
    {
        this.openChat(chatTabEntry, showKeyboard);
    }

    public void onChatDelete(final long threadId)
    {
        MainActivity.this.tabs.deleteChatTab(threadId);
    }

    public void onChatLoadFailure(long threadId)
    {
        Toast.makeText(this, "加载失败", Toast.LENGTH_LONG).show();
        this.tabs.selectDefaultTab(threadId);
    }

    public void onNewChatClick()
    {
        this.openNewChat(1);
    }

    public void onOpenClick()
    {
        this.tabsLayout.toggle();
    }

    public void onPause()
    {
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        this.openSettings();
        return false;
    }

    public void onSettingsClick()
    {
        this.openSettings();
    }

    public void onTabsScroll()
    {
        this.tabsLayout.animate(true);
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        this.setIntent(intent);
        this.handleNotificationInfo();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    private void handleNotificationInfo()
    {
        Intent intent = this.getIntent();
        if ((intent.getFlags() & 0x100000) == 0)
        {
            toChatUsername = intent.getStringExtra("userId");
            //            this.openNewChat(1);
        }
    }

    private void openChat(ChatTabEntry chatTabEntry, boolean showKeyboard)
    {
        this.setActiveTab(chatTabEntry.threadId);
        this.content.removeAllViews();
        //        instantiateItem(chatTabEntry, showKeyboard, chatTabEntry.threadId);
        //        ChatFragment chatFragment = ChatFragment.newInstance(chatTabEntry, showKeyboard, chatTabEntry.getEMConversation().getUserName());
        //        getSupportFragmentManager().beginTransaction().replace(R.id.content, chatFragment, "ChatFragment").commit();
        Bundle bundle = new Bundle();
        bundle.putLong("thread_id", chatTabEntry.threadId);
        bundle.putBoolean("show_keyboard", showKeyboard);
        bundle.putString("userId", chatTabEntry.getEMConversation().getUserName());
        initFragment(ChatFragment.class, makeFragmentName(chatTabEntry.threadId), bundle);
    }

    private void openNewChat(long threadId)
    {
        this.setActiveTab(-1);
        this.content.removeAllViews();
        NewChatFragment chatFragment = NewChatFragment.newInstance(threadId, false, toChatUsername);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, chatFragment).commit();
    }

    private void openSettings()
    {
    }

    private void setActiveTab(long threadId)
    {
        this.tabs.refresh(threadId);
    }

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.activity_main;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        this.content = (ViewGroup) this.findViewById(R.id.content);
        this.tabsLayout = (TabsLayout) this.findViewById(R.id.tabsLayout);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.tabs = (TabsFragment) fragmentManager.findFragmentById(R.id.tabs);
    }

    @Override
    public void doInitDataes()
    {
        this.handleNotificationInfo();
        // 注册接收消息广播
        receiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);
        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
                .getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(5);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        // 注册一个消息送达的BroadcastReceiver
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
                .getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onHelpClick()
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // 注销广播
        try
        {
            unregisterReceiver(receiver);
            receiver = null;
        }
        catch (Exception e)
        {
        }
        try
        {
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        }
        catch (Exception e)
        {
        }
    }

    public void destroyItem(ViewGroup container, int position, Object object)
    {
        if (this.mCurTransaction == null)
        {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mCurTransaction.detach((Fragment) object);
    }

    private void initFragment(Class<?> name, String tag, Bundle bundle)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        YARFragment fragment = (YARFragment) fragmentManager.findFragmentByTag(tag);
        if (mCurrentFragment != null)
        {
            fragmentTransaction.detach(mCurrentFragment);
        }
        if (fragment != null)
        {
            fragmentTransaction.attach(fragment);
        }
        else
        {
            fragment = (YARFragment) Fragment.instantiate(mActivity, name.getName(), bundle);
            fragmentTransaction.add(R.id.content, fragment, tag);
        }
        mCurrentFragment = fragment;
        fragmentTransaction.commit();
    }

    public YARFragment getCurrentFragment()
    {
        return mCurrentFragment;
    }

    /**
     * 消息广播接收者
     */
    public class NewMessageBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat)
            {
                username = message.getTo();
            }
            if (!username.equals(toChatUsername))
            {
                // 消息不是发给当前会话，return
                return;
            }
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
            if (mCurrentFragment instanceof NewChatFragment)
            {
                NewChatFragment chatFragment = (NewChatFragment) mCurrentFragment;
                chatFragment.refresh();
            }
            else if (mCurrentFragment instanceof ChatFragment)
            {
                ChatFragment chatFragment = (ChatFragment) mCurrentFragment;
                chatFragment.refresh();
            }
            // 记得把广播给终结掉
            abortBroadcast();
        }
    }
}
