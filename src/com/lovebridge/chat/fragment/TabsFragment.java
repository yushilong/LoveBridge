package com.lovebridge.chat.fragment;

import android.app.ProgressDialog;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony.MmsSms;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.lovebridge.R;
import com.lovebridge.chat.view.tabs.*;
import com.lovebridge.library.YARFragment;

import java.util.*;

public class TabsFragment extends YARFragment
{
    private static final long TABS_RECENT_SCROLL_THRESHOLD = 0x77359400;
    private static volatile long tabsLastScrollTime;
    private BaseAdapter adapter;
    private ContentObserver conversationsObserver;
    private ListView listView;
    private Listener listener;
    private NewChatTabLayout newChatTabLayout;
    private StickyChatTabLayout stickyChatTabLayout;
    private List<ChatTabEntry> tabs;
    private boolean tabsAreScrolling;

    public TabsFragment()
    {
        super();
        this.tabs = new ArrayList<ChatTabEntry>();
        this.tabsAreScrolling = false;
        this.conversationsObserver = new ContentObserver(new Handler(Looper.getMainLooper()))
        {
            public void onChange(boolean selfChange)
            {
                if (TabsFragment.this.isResumed())
                {
                    TabsFragment.this.buildTabs();
                }
            }
        };
    }

    public static void setTabsScrollTime()
    {
        TabsFragment.tabsLastScrollTime = System.nanoTime();
    }

    public static boolean tabsRecentlyScrolled()
    {
        boolean bool = System.nanoTime() - TabsFragment.tabsLastScrollTime < TABS_RECENT_SCROLL_THRESHOLD ? true
                : false;
        return bool;
    }

    public void deleteChatTab(long threadId)
    {
        NewChatTabLayout newChatTabLayout = null;
        int i = 0;
        while (i < this.tabs.size())
        {
            if (this.tabs.get(i).getThreadId() != threadId)
            {
                ++i;
                continue;
            }
            else if (i + 1 < this.tabs.size())
            {
                // object = this.tabs.get(i + 1);
            }
            else if (i > 0)
            {
                // object = this.tabs.get(i - 1);
            }
            else
            {
                newChatTabLayout = this.newChatTabLayout;
            }
            break;
        }
        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("");
        if (newChatTabLayout != null)
        {
            newChatTabLayout.selectTab();
        }
    }

    @Override public int doGetContentViewId()
    {
        return R.layout.fragment_tabs;
    }

    @Override public void doInitSubViews(View view)
    {
        this.listener = (Listener) this.getActivity();
        this.listView = (ListView) view.findViewById(R.id.listView);
        this.newChatTabLayout = (NewChatTabLayout) view.findViewById(R.id.newChat);
        this.newChatTabLayout.setListener((NewChatTabLayout.Listener) this.getActivity());
        this.stickyChatTabLayout = (StickyChatTabLayout) view.findViewById(R.id.stickyChatTab);
        this.stickyChatTabLayout.setRelatedViews(this.listView, view.findViewById(R.id.stickyChatTabOverlay));
        ((FooterTabLayout) view.findViewById(R.id.footer)).setListener((FooterTabLayout.Listener) this.getActivity());
        this.listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int i = 0;
                TabsFragment.this.stickyChatTabLayout.updatePosition();
                TabsFragment.setTabsScrollTime();
                View view = firstVisibleItem == 0 ? TabsFragment.this.listView.getChildAt(0) : null;
                if (view != null)
                {
                    if (view.getTop() != 0)
                    {
                        i = 0;
                    }
                    else
                    {
                        i = 1;
                    }
                }
                if (i == 0 && (TabsFragment.this.tabsAreScrolling))
                {
                    TabsFragment.this.tabsAreScrolling = false;
                    TabsFragment.this.listener.onTabsScroll();
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                boolean bool = true;
                TabsFragment tabsFragment = TabsFragment.this;
                if (scrollState != 1)
                {
                    bool = false;
                }
                tabsFragment.tabsAreScrolling = bool;
            }
        });
    }

    @Override public void doInitDataes()
    {
        buildTabs();
        getActivity().getContentResolver().registerContentObserver(MmsSms.CONTENT_CONVERSATIONS_URI, true,
                this.conversationsObserver);
        this.adapter = new BaseAdapter()
        {
            public int getCount()
            {
                return TabsFragment.this.tabs.size();
            }

            public Object getItem(int position)
            {
                return TabsFragment.this.tabs.get(position);
            }

            public long getItemId(int position)
            {
                return 0;
            }

            public View getView(int position, View view, ViewGroup parent)
            {
                return TabsFragment.this.tabs.get(position).getView(view);
            }
        };
        this.listView.setAdapter(this.adapter);
    }

    @Override public void doAfter()
    {
    }

    public void onPause()
    {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(this.conversationsObserver);

    }

    public void refresh(long threadId)
    {
        this.stickyChatTabLayout.refresh(threadId);
        this.newChatTabLayout.refresh(threadId);
        this.adapter.notifyDataSetChanged();
    }

    public void scrollToTop()
    {
        this.listView.setSelection(0);
    }

    public void selectDefaultTab(long paramLong)
    {
        if (this.tabs.size() > 0)
        {
            ChatTabEntry chatTabEntry = this.tabs.get(0);
            if (chatTabEntry.getThreadId() != paramLong)
            {
                chatTabEntry.selectTab(chatTabEntry);
                return;
            }
        }
        this.newChatTabLayout.selectTab();
    }

    private void buildTabs()
    {
        List<EMConversation> list = loadConversationsWithRecentChat();
        for (int i = 0; i < list.size(); i++)
        {
            EMConversation conversation = list.get(i);
            ChatTabEntry item = new ChatTabEntry(getActivity(), conversation, i + 1);
            tabs.add(item);
        }
    }

    @Override public void onResume()
    {
        super.onResume();
        selectDefaultTab(-1);
    }

    /**
     * 获取所有会话
     *
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat()
    {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values())
        {
            if (conversation.getAllMessages().size() != 0)
                list.add(conversation);
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<EMConversation> conversationList)
    {
        Collections.sort(conversationList, new Comparator<EMConversation>()
        {
            @Override
            public int compare(final EMConversation con1, final EMConversation con2)
            {
                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime())
                {
                    return 0;
                }
                else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime())
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
        });
    }

    public interface Listener
    {
        void onTabsScroll();
    }

    public interface SelectableTab
    {
        void selectTab(ChatTabEntry localChatTabEntry);
    }
}
