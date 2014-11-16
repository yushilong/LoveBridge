
package com.lovebridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.lovebridge.application.MainApplication;
import com.lovebridge.chat.activity.AlertActivity;
import com.lovebridge.chat.moden.ChatUser;
import com.lovebridge.chat.utils.CommonUtils;
import com.lovebridge.db.UserDao;
import com.lovebridge.index.TabActivity;
import com.lovebridge.library.YARActivity;
import com.lovebridge.library.tools.YARConstants;

/**
 * 登陆页面
 */
public class LoginActivity extends YARActivity {
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private boolean progressShow;

    /**
     * 登陆
     * 
     * @param view
     */
    public void login(View view) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(LoginActivity.this, AlertActivity.class);
        intent.putExtra("editTextShow", true);
        intent.putExtra("titleIsCancel", true);
        intent.putExtra("msg", "请设置当前用户的昵称\n为了ios离线推送不是userid而是nick，详情见注释");
        startActivityForResult(intent, REQUEST_CODE_SETNICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SETNICK) {
                MainApplication.currentUserNick = data.getStringExtra("edittext");

                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    progressShow = true;
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressShow = false;
                        }
                    });
                    pd.setMessage("正在登陆...");
                    pd.show();
                    // 调用sdk登陆方法登陆聊天服务器
                    EMChatManager.getInstance().login(username, password, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            if (!progressShow) {
                                return;
                            }
                            // 登陆成功，保存用户名密码
                            MainApplication.getInstance().setUserName(username);
                            MainApplication.getInstance().setPassword(password);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.setMessage("正在获取好友和群聊列表...");
                                }
                            });
                            try {
                                // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
                                List<String> usernames = EMContactManager.getInstance().getContactUserNames();
                                Map<String, ChatUser> userlist = new HashMap<String, ChatUser>();
                                for (String username : usernames) {
                                    ChatUser user = new ChatUser();
                                    user.setUsername(username);
                                    setUserHearder(username, user);
                                    userlist.put(username, user);
                                }
                                // 添加user"申请与通知"
                                ChatUser newFriends = new ChatUser();
                                newFriends.setUsername(YARConstants.NEW_FRIENDS_USERNAME);
                                newFriends.setNick("申请与通知");
                                newFriends.setHeader("");
                                userlist.put(YARConstants.NEW_FRIENDS_USERNAME, newFriends);
                                // 添加"群聊"
                                ChatUser groupUser = new ChatUser();
                                groupUser.setUsername(YARConstants.GROUP_USERNAME);
                                groupUser.setNick("群聊");
                                groupUser.setHeader("");
                                userlist.put(YARConstants.GROUP_USERNAME, groupUser);

                                // 存入内存
                                MainApplication.getInstance().setContactList(userlist);
                                // 存入db
                                UserDao dao = new UserDao(LoginActivity.this);
                                List<ChatUser> users = new ArrayList<ChatUser>(userlist.values());
                                dao.saveContactList(users);

                                // 获取群聊列表(群聊里只有groupid和groupname的简单信息),sdk会把群组存入到内存和db中
                                EMGroupManager.getInstance().getGroupsFromServer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                                            MainApplication.currentUserNick);
                            if (!updatenick) {
                                EMLog.e("LoginActivity", "update current user nick fail");
                            }

                            if (!LoginActivity.this.isFinishing())
                                pd.dismiss();
                            // 进入主页面
                            startActivity(new Intent(LoginActivity.this, TabActivity.class));
                            finish();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, final String message) {
                            if (!progressShow) {
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "登录失败: " + message, 0).show();

                                }
                            });
                        }
                    });
                }

            }

        }
    }

    /**
     * 注册
     * 
     * @param view
     */
    public void register(View view) {
        // startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainApplication.getInstance().getUserName() != null) {
            usernameEditText.setText(MainApplication.getInstance().getUserName());
        }
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     * 
     * @param username
     * @param user
     */
    protected void setUserHearder(String username, ChatUser user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(YARConstants.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                            .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.activity_login;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub
        // 如果用户名密码都有，直接进入主页面
        if (MainApplication.getInstance().getUserName() != null && MainApplication.getInstance().getPassword() != null) {
            startActivity(new Intent(this, TabActivity.class));
            finish();
        }
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }
}
