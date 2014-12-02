package com.lovebridge.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.ChatFragment;
import com.lovebridge.chat.fragment.EmojiPickerFragment.Listener;
import com.lovebridge.chat.fragment.NewChatFragment;
import com.lovebridge.chat.fragment.TabsFragment;
import com.lovebridge.chat.view.tabs.ChatTabEntry;
import com.lovebridge.chat.view.tabs.FooterTabLayout;
import com.lovebridge.chat.view.tabs.NewChatTabLayout;
import com.lovebridge.chat.view.tabs.TabsLayout;
import com.lovebridge.library.YARActivity;

import java.util.Arrays;

public class MainActivity extends YARActivity implements Listener, ChatTabEntry.Listener, FooterTabLayout.Listener,
        NewChatTabLayout.Listener, TabsFragment.Listener
{
    public static String ACTION_RESTART = null;
    public static String EXTRA_SHOW_KEYBOARD = null;
    public static String EXTRA_THREAD_ID = null;
    private static volatile long activeThreadId;
    private ViewGroup content;
    private TabsFragment tabs;
    private TabsLayout tabsLayout;

    static
    {
        MainActivity.ACTION_RESTART = String.valueOf(MainActivity.class.getPackage().getName()) + ".activity_restart";
        MainActivity.EXTRA_SHOW_KEYBOARD = String.valueOf(MainActivity.class.getPackage().getName()) + ".show_keyboard";
        MainActivity.EXTRA_THREAD_ID = String.valueOf(MainActivity.class.getPackage().getName()) + ".thread_id";
    }

    public static long getActiveThreadId()
    {
        return MainActivity.activeThreadId;
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
            int i = keyboardHeight != 0 ? 0 : 8;
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
        this.openChat(threadId, showKeyboard, null);
    }

    public void onChatDelete(final long threadId)
    {
        MainActivity.this.tabs.deleteChatTab(threadId);
        if (threadId != MainActivity.getActiveThreadId())
        {
            this.openChat(threadId, false, "");
        }
    }

    public void onChatLoadFailure(long threadId)
    {
        Toast.makeText(this, "加载失败", 0).show();
        this.tabs.selectDefaultTab(threadId);
    }

    public void onNewChatClick()
    {
        this.openNewChat();
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

    public void openChatForNumber(String number)
    {
        this.openChat(1, false, null);
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

    private String getDefaultText(Intent intent)
    {
        String string;
        if (intent.hasExtra("sms_body"))
        {
            string = intent.getStringExtra("sms_body");
        }
        else if (intent.hasExtra("android.intent.extra.TEXT"))
        {
            string = intent.getStringExtra("android.intent.extra.TEXT");
        }
        else
        {
            string = null;
        }
        return string;
    }

    private void handleNotificationInfo()
    {
        String[] array_string;
        long l;
        Intent intent = this.getIntent();
        if ((intent.getFlags() & 0x100000) == 0)
        {
            if (("android.intent.action.VIEW".equals(intent.getAction()))
                    && ("vnd.android-dir/mms-sms".equals(intent.getType())))
            {
                l = intent.getLongExtra("thread_id", 0);
                String string = intent.getStringExtra("address");
                array_string = string != null ? new String[] { string } : null;
                if (l == 0 && array_string != null)
                {
                    l = 1;
                }
                if (l != 0)
                {
                    this.openChat(l, false, this.getDefaultText(intent));
                    return;
                }
                this.openNewChat(array_string, this.getDefaultText(intent));
                return;
            }
            if (Arrays.asList(new String[] { "sms" , "smsto" , "mms" , "mmsto" }).indexOf(intent.getScheme()) != 0xFFFFFFFF)
            {
                Uri uri = intent.getData();
                String string1 = uri.getEncodedSchemeSpecificPart();
                if (!string1.startsWith("//"))
                {
                    string1 += "//";
                }
                int i = string1.indexOf(";");
                int i1 = string1.indexOf("?");
                if (i != 0xFFFFFFFF && (i1 == 0xFFFFFFFF || i < i1))
                {
                    string1 = string1.replaceFirst(";", "?");
                }
                Uri uri1 = Uri.parse(String.valueOf(uri.getScheme()) + ":" + string1);
                array_string = TextUtils.split(uri1.getAuthority(), ",");
                l = 1;
                String string2 = uri1.getQueryParameter("body");
                if (string2 == null)
                {
                    string2 = this.getDefaultText(intent);
                }
                if (l != 0)
                {
                    this.openChat(l, false, string2);
                    return;
                }
                this.openNewChat(new String[] { "sms" , "smsto" , "mms" , "mmsto" }, string2);
                return;
            }
            if (MainActivity.ACTION_RESTART.equals(intent.getAction()))
            {
                long l1 = intent.getLongExtra(MainActivity.EXTRA_THREAD_ID, -1);
                if (l1 == -1)
                {
                    this.openNewChat();
                    return;
                }
                this.openChat(l1, false, null);
                return;
            }
            l = intent.getLongExtra(MainActivity.EXTRA_THREAD_ID, 0);
            if (l == 0)
            {

                return;
            }
            this.openNewChat(new String[] { "sms" , "smsto" , "mms" , "mmsto" }, "");
//            this.openChat(l, intent.getBooleanExtra(MainActivity.EXTRA_SHOW_KEYBOARD, true), null);
        }
    }

    private void openChat(long threadId, boolean showKeyboard, String defaultText)
    {
        if (threadId == 0)
        {
            throw new IllegalArgumentException("no thread id in openChat");
        }
        this.setActiveTab(threadId);
        this.content.removeAllViews();
        ChatFragment chatFragment = ChatFragment.newInstance(threadId, showKeyboard, defaultText);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, chatFragment).commit();
    }

    private void openNewChat(String[] addresses, String defaultText)
    {
        this.setActiveTab(-1);
        this.content.removeAllViews();
        NewChatFragment chatFragment = NewChatFragment.newInstance(addresses, defaultText);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, chatFragment).commit();
    }

    private void openNewChat()
    {
        this.openNewChat(null, null);
    }

    private void openSettings()
    {
    }

    private void setActiveTab(long threadId)
    {
        MainActivity.activeThreadId = threadId;
        this.tabs.refresh();
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
}
