package com.lovebridge.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

import java.util.List;

public class PublicGroupsActivity extends YARActivity
{
    private ProgressBar pb;
    private ListView listView;
    private EditText query;
    private ImageButton clearSearch;
    private GroupsAdapter adapter;

    private class GroupsAdapter extends ArrayAdapter<EMGroupInfo>
    {
        private LayoutInflater inflater;

        public GroupsAdapter(Context context, int res, List<EMGroupInfo> groups)
        {
            super(context, res, groups);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.row_group, null);
            }
            ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getGroupName());
            return convertView;
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
        return R.layout.activity_public_groups;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        pb = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.list);
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    // 从服务器获取所用公开的群聊
                    final List<EMGroupInfo> groupsList = EMGroupManager.getInstance().getAllPublicGroupsFromServer();
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pb.setVisibility(View.INVISIBLE);
                            adapter = new GroupsAdapter(PublicGroupsActivity.this, 1, groupsList);
                            listView.setAdapter(adapter);
                            // 设置item点击事件
                            listView.setOnItemClickListener(new OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    startActivity(new Intent(PublicGroupsActivity.this, GroupSimpleDetailActivity.class)
                                            .putExtra("groupinfo", adapter.getItem(position)));
                                }
                            });
                            // 搜索框
                            query = (EditText) findViewById(R.id.query);
                            // 搜索框中清除button
                            clearSearch = (ImageButton) findViewById(R.id.search_clear);
                            query.addTextChangedListener(new TextWatcher()
                            {
                                public void onTextChanged(CharSequence s, int start, int before, int count)
                                {
                                    adapter.getFilter().filter(s);
                                    if (s.length() > 0)
                                    {
                                        clearSearch.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        clearSearch.setVisibility(View.INVISIBLE);
                                    }
                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                                {
                                }

                                public void afterTextChanged(Editable s)
                                {
                                }
                            });
                            clearSearch.setOnClickListener(new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    query.getText().clear();
                                }
                            });
                        }
                    });
                }
                catch (EaseMobException e)
                {
                    e.printStackTrace();
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
