/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovebridge.chat.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

public class GroupSimpleDetailActivity extends YARActivity {
    private Button btn_add_group;
    private TextView tv_admin;
    private TextView tv_name;
    private TextView tv_introduction;
    private EMGroup group;
    private String groupid;
    private ProgressBar progressBar;

    // 加入群聊
    public void addToGroup(View view) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在发送请求...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 如果是membersOnly的群，需要申请加入，不能直接join
                    if (group.isMembersOnly()) {
                        EMGroupManager.getInstance().applyJoinToGroup(groupid, "求加入");
                    } else {
                        EMGroupManager.getInstance().joinGroup(groupid);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            if (group.isMembersOnly())
                                Toast.makeText(GroupSimpleDetailActivity.this, "发送请求成功，等待群主同意", 0).show();
                            else
                                Toast.makeText(GroupSimpleDetailActivity.this, "加入群聊成功", 0).show();
                            btn_add_group.setEnabled(false);
                        }
                    });
                } catch (final EaseMobException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(GroupSimpleDetailActivity.this, "加入群聊失败：" + e.getMessage(), 0).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View view) {
        finish();
    }

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.activity_group_simle_details;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        tv_name = (TextView)findViewById(R.id.name);
        tv_admin = (TextView)findViewById(R.id.tv_admin);
        btn_add_group = (Button)findViewById(R.id.btn_add_to_group);
        tv_introduction = (TextView)findViewById(R.id.tv_introduction);
        progressBar = (ProgressBar)findViewById(R.id.loading);
    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub
        EMGroupInfo groupInfo = (EMGroupInfo)getIntent().getSerializableExtra("groupinfo");
        String groupname = groupInfo.getGroupName();
        groupid = groupInfo.getGroupId();

        tv_name.setText(groupname);

        new Thread(new Runnable() {

            public void run() {
                // 从服务器获取详情
                try {
                    group = EMGroupManager.getInstance().getGroupFromServer(groupid);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            // 获取详情成功，并且自己不在群中，才让加入群聊按钮可点击
                            if (!group.getMembers().contains(EMChatManager.getInstance().getCurrentUser()))
                                btn_add_group.setEnabled(true);
                            tv_name.setText(group.getGroupName());
                            tv_admin.setText(group.getOwner());
                            tv_introduction.setText(group.getDescription());
                        }
                    });
                } catch (final EaseMobException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GroupSimpleDetailActivity.this, "获取群聊信息失败: " + e.getMessage(), 1).show();
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }
}
