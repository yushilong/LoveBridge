package com.lovebridge.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.lovebridge.R;
import com.lovebridge.chat.adapter.GroupAdapter;
import com.lovebridge.library.YARActivity;

import java.util.List;

public class GroupsActivity extends YARActivity
{
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;

    /**
     * 进入公开群聊列表
     */
    public void onPublicGroups(View view)
    {
        startActivity(new Intent(this, PublicGroupsActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        grouplist = EMGroupManager.getInstance().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view)
    {
        finish();
    }

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.fragment_groups;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        groupListView = (ListView) findViewById(R.id.list);
        groupListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == groupAdapter.getCount() - 1)
                {
                    // 新建群聊
                    startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
                }
                else
                {
                    // 进入群聊
                    Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                    intent.putExtra("groupId", groupAdapter.getItem(position - 1).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }
        });
        groupListView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        grouplist = EMGroupManager.getInstance().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}