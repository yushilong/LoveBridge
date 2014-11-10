
package com.lovebridge.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

public class NewGroupActivity extends YARActivity {
    private EditText groupNameEditText;
    private ProgressDialog progressDialog;
    private EditText introductionEditText;
    private CheckBox checkBox;
    private CheckBox memberCheckbox;
    private LinearLayout openInviteContainer;

    /**
     * @param v
     */
    public void save(View v) {
        String name = groupNameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Intent intent = new Intent(this, AlertActivity.class);
            intent.putExtra("msg", "群组名称不能为空");
            startActivity(intent);
        } else {
            // 进通讯录选人
            startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 新建群组
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在创建群聊...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 调用sdk创建群组方法
                    String groupName = groupNameEditText.getText().toString().trim();
                    String desc = introductionEditText.getText().toString();
                    String[] members = data.getStringArrayExtra("newmembers");
                    try {
                        if (checkBox.isChecked()) {
                            // 创建公开群，此种方式创建的群，可以自由加入
                            // EMGroupManager.getInstance().createPublicGroup(groupName,
                            // desc, members, false);
                            // 创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
                            EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true);
                        } else {
                            // 创建不公开群
                            EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members,
                                            memberCheckbox.isChecked());
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    } catch (final Exception e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(NewGroupActivity.this, "创建群组失败:" + e.getLocalizedMessage(), 1).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.activity_new_group;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        groupNameEditText = (EditText)findViewById(R.id.edit_group_name);
        introductionEditText = (EditText)findViewById(R.id.edit_group_introduction);
        checkBox = (CheckBox)findViewById(R.id.cb_public);
        memberCheckbox = (CheckBox)findViewById(R.id.cb_member_inviter);
        openInviteContainer = (LinearLayout)findViewById(R.id.ll_open_invite);

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openInviteContainer.setVisibility(View.INVISIBLE);
                } else {
                    openInviteContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }
}
