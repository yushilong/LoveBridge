package com.lovebridge.chat.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.lovebridge.R;
import com.lovebridge.application.MainApplication;
import com.lovebridge.chat.activity.AddContactActivity;
import com.lovebridge.chat.activity.GroupsActivity;
import com.lovebridge.chat.activity.MainActivity;
import com.lovebridge.chat.activity.NewFriendsMsgActivity;
import com.lovebridge.chat.adapter.ContactAdapter;
import com.lovebridge.chat.moden.ChatUser;
import com.lovebridge.chat.view.Sidebar;
import com.lovebridge.db.InviteMessgeDao;
import com.lovebridge.db.UserDao;
import com.lovebridge.library.YARFragment;
import com.lovebridge.library.tools.YARConstants;

import java.util.*;

public class ContactlistFragment extends YARFragment
{
    private ContactAdapter adapter;
    private List<ChatUser> contactList;
    private ListView listView;
    private boolean hidden;
    private Sidebar sidebar;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 长按前两个不弹menu
        if (((AdapterContextMenuInfo) menuInfo).position > 2)
        {
            getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.delete_contact)
        {
            ChatUser tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            // 删除此联系人
            deleteContact(tobeDeleteUser);
            // 删除相关的邀请消息
            InviteMessgeDao dao = new InviteMessgeDao(getActivity());
            dao.deleteMessage(tobeDeleteUser.getUsername());
            return true;
        }
        else if (item.getItemId() == R.id.add_to_blacklist)
        {
            ChatUser user = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            moveToBlacklist(user.getUsername());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden)
        {
            refresh();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!hidden)
        {
            refresh();
        }
    }

    /**
     * 删除联系人
     */
    public void deleteContact(final ChatUser tobeDeleteUser)
    {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("正在删除...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    MainApplication.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pd.dismiss();
                            adapter.remove(tobeDeleteUser);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (final Exception e)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "删除失败: " + e.getMessage(), 1).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 把user移入到黑名单
     */
    private void moveToBlacklist(final String username)
    {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("正在移入黑名单...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    // 加入到黑名单
                    EMContactManager.getInstance().addUserToBlackList(username, true);
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "移入黑名单成功", 0).show();
                        }
                    });
                }
                catch (EaseMobException e)
                {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "移入黑名单失败", 0).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 刷新ui
    public void refresh()
    {
        try
        {
            // 可能会在子线程中调到这方法
            getActivity().runOnUiThread(new Runnable()
            {
                public void run()
                {
                    getContactList();
                    adapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getContactList()
    {
        contactList.clear();
        Map<String, ChatUser> users = MainApplication.getInstance().getContactList();
        Iterator<Map.Entry<String, ChatUser>> iterator = users.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, ChatUser> entry = iterator.next();
            if (!entry.getKey().equals(YARConstants.NEW_FRIENDS_USERNAME)
                    && !entry.getKey().equals(YARConstants.GROUP_USERNAME))
                contactList.add(entry.getValue());
        }
        // 排序
        Collections.sort(contactList, new Comparator<ChatUser>()
        {
            @Override
            public int compare(ChatUser lhs, ChatUser rhs)
            {
                return lhs.getUsername().compareTo(rhs.getUsername());
            }
        });
        // 加入"添加联系人"
//        ChatUser user = new ChatUser();
//        user.setUsername(YARConstants.ADD_USER);
//        contactList.add(0, user);
        // 加入"申请与通知"和"群聊"
        contactList.add(0, users.get(YARConstants.GROUP_USERNAME));
        // 把"申请与通知"添加到首位
        contactList.add(0, users.get(YARConstants.NEW_FRIENDS_USERNAME));
    }

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.fragment_contact_list;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) containerView.findViewById(R.id.list);
        sidebar = (Sidebar) containerView.findViewById(R.id.sidebar);
        sidebar.setListView(listView);
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        contactList = new ArrayList<ChatUser>();
        // 获取设置contactlist
        getContactList();
        // 设置adapter
        adapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList, sidebar);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String username = adapter.getItem(position).getUsername();
                if (YARConstants.NEW_FRIENDS_USERNAME.equals(username))
                {
                    // 进入申请与通知页面
                    ChatUser user = MainApplication.getInstance().getContactList()
                            .get(YARConstants.NEW_FRIENDS_USERNAME);
                    user.setUnreadMsgCount(0);
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                }
                else if (YARConstants.GROUP_USERNAME.equals(username))
                {
                    // 进入群聊列表页面
                    startActivity(new Intent(getActivity(), GroupsActivity.class));
                }
                else if (YARConstants.ADD_USER.equals(username))
                {
                    startActivity(new Intent(getActivity(), AddContactActivity.class));
                }
                else
                {
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), MainActivity.class).putExtra("userId",
                            adapter.getItem(position).getUsername()));
                }
            }
        });
        listView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // 隐藏软键盘
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
