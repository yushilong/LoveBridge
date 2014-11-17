package com.lovebridge.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.lovebridge.R;
import com.lovebridge.application.MainApplication;
import com.lovebridge.chat.adapter.ContactAdapter;
import com.lovebridge.chat.moden.ChatUser;
import com.lovebridge.chat.view.Sidebar;
import com.lovebridge.library.YARActivity;
import com.lovebridge.library.tools.YARConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupPickContactsActivity extends YARActivity
{
    private ListView listView;
    /** 是否为一个新建的群组 */
    protected boolean isCreatingNewGroup;
    /** 是否为单选 */
    private boolean isSignleChecked;
    private PickContactAdapter contactAdapter;
    /** group中一开始就有的成员 */
    private List<String> exitingMembers;

    /**
     * 确认选择的members
     *
     * @param v
     */
    public void save(View v)
    {
        setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
        finish();
    }

    /**
     * 获取要被添加的成员
     *
     * @return
     */
    private List<String> getToBeAddMembers()
    {
        List<String> members = new ArrayList<String>();
        int length = contactAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++)
        {
            String ChatUsername = contactAdapter.getItem(i + 1).getUsername();
            if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(ChatUsername))
            {
                members.add(ChatUsername);
            }
        }
        return members;
    }

    /**
     * adapter
     */
    private class PickContactAdapter extends ContactAdapter
    {
        private boolean[] isCheckedArray;

        public PickContactAdapter(Context context, int resource, List<ChatUser> ChatUsers)
        {
            super(context, resource, ChatUsers, null);
            isCheckedArray = new boolean[ChatUsers.size()];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View view = super.getView(position, convertView, parent);
            if (position > 0)
            {
                final String ChatUsername = getItem(position).getUsername();
                // 选择框checkbox
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                if (exitingMembers != null && exitingMembers.contains(ChatUsername))
                {
                    checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
                }
                else
                {
                    checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
                }
                if (checkBox != null)
                {
                    // checkBox.setOnCheckedChangeListener(null);
                    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            // 群组中原来的成员一直设为选中状态
                            if (exitingMembers.contains(ChatUsername))
                            {
                                isChecked = true;
                                checkBox.setChecked(true);
                            }
                            isCheckedArray[position - 1] = isChecked;
                            // 如果是单选模式
                            if (isSignleChecked && isChecked)
                            {
                                for (int i = 0; i < isCheckedArray.length; i++)
                                {
                                    if (i != position - 1)
                                    {
                                        isCheckedArray[i] = false;
                                    }
                                }
                                contactAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    // 群组中原来的成员一直设为选中状态
                    if (exitingMembers.contains(ChatUsername))
                    {
                        checkBox.setChecked(true);
                        isCheckedArray[position - 1] = true;
                    }
                    else
                    {
                        checkBox.setChecked(isCheckedArray[position - 1]);
                    }
                }
            }
            return view;
        }
    }

    public void back(View view)
    {
        finish();
    }

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.activity_group_pick_contacts;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        listView = (ListView) findViewById(R.id.list);
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        // String groupName = getIntent().getStringExtra("groupName");
        String groupId = getIntent().getStringExtra("groupId");
        if (groupId == null)
        {// 创建群组
            isCreatingNewGroup = true;
        }
        else
        {
            // 获取此群组的成员列表
            EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
            exitingMembers = group.getMembers();
        }
        if (exitingMembers == null)
            exitingMembers = new ArrayList<String>();
        // 获取好友列表
        final List<ChatUser> allChatUserList = new ArrayList<ChatUser>();
        for (ChatUser user : MainApplication.getInstance().getContactList().values())
        {
            if (!user.getUsername().equals(YARConstants.NEW_FRIENDS_USERNAME)
                    & !user.getUsername().equals(YARConstants.GROUP_USERNAME))
                allChatUserList.add(user);
        }
        // 对list进行排序
        Collections.sort(allChatUserList, new Comparator<ChatUser>()
        {
            @Override
            public int compare(ChatUser lhs, ChatUser rhs)
            {
                return (lhs.getUsername().compareTo(rhs.getUsername()));
            }
        });
        contactAdapter = new PickContactAdapter(this, R.layout.row_contact_with_checkbox, allChatUserList);
        listView.setAdapter(contactAdapter);
        ((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.toggle();
            }
        });
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
