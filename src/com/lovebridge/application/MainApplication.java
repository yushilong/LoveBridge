
package com.lovebridge.application;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.lovebridge.chat.activity.ChatActivity;
import com.lovebridge.chat.moden.ChatUser;
import com.lovebridge.db.UserDao;
import com.lovebridge.index.TabActivity;
import com.lovebridge.library.api.YARVolley;
import com.lovebridge.library.tools.YARNetUtils;
import com.lovebridge.library.tools.YARPreferenceUtils;

public class MainApplication extends Application {
    private static MainApplication context;
    public static String currentUserNick;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = this;
        init();
    }

    public static MainApplication getInstance() {
        return context;
    }

    public void init() {
        YARNetUtils.setCurrentNetState(context);
        YARVolley.init(this);
        String processAppName = getAppName(Process.myPid());
        if (processAppName == null || processAppName.equals("")) {
            return;
        }
        // 初始化环信SDK
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认环信是不维护好友关系列表的
        options.setUseRoster(true);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置收到消息是否有新消息通知，默认为true
        options.setNotifyBySoundAndVibrate(YARPreferenceUtils.getInstance(context).getSettingMsgNotification());
        // 设置收到消息是否有声音提示，默认为true
        options.setNoticeBySound(YARPreferenceUtils.getInstance(context).getSettingMsgSound());
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(YARPreferenceUtils.getInstance(context).getSettingMsgVibrate());
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        options.setUseSpeaker(YARPreferenceUtils.getInstance(context).getSettingMsgSpeaker());
        // 设置notification消息点击时，跳转的intent为自定义的intent
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(context, ChatActivity.class);
                ChatType chatType = message.getChatType();
                if (chatType == ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { // 群聊信息
                         // message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        });
        // 监听账户重复登陆
        EMChatManager.getInstance().addConnectionListener(new OnConnectionListener());
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    Log.d("Process",
                                    "Id: " + info.pid + " ProcessName: " + info.processName + "  Label: "
                                                    + c.toString());
                    processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    class OnConnectionListener implements ConnectionListener {
        @Override
        public void onReConnecting() {
        }

        @Override
        public void onReConnected() {
        }

        @Override
        public void onDisConnected(String errorString) {
            if (errorString != null && errorString.contains("conflict")) {
                Intent intent = new Intent(context, TabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("conflict", true);
                startActivity(intent);
            }

        }

        @Override
        public void onConnecting(String progress) {

        }

        @Override
        public void onConnected() {
        }
    }

    public final String PREF_USERNAME = "username";
    private String userName = null;
    private static final String PREF_PWD = "pwd";
    private String password = null;
    private Map<String, ChatUser> contactList;

    /**
     * 获取当前登陆用户名
     * 
     * @return
     */
    public String getUserName() {
        if (userName == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            userName = preferences.getString(PREF_USERNAME, null);
        }
        return userName;
    }

    /**
     * 获取密码
     * 
     * @return
     */
    public String getPassword() {
        if (password == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            password = preferences.getString(PREF_PWD, null);
        }
        return password;
    }

    /**
     * 设置用户名
     * 
     * @param user
     */
    public void setUserName(String username) {
        if (username != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            if (editor.putString(PREF_USERNAME, username).commit()) {
                userName = username;
            }
        }
    }

    /**
     * 设置密码
     * 
     * @param pwd
     */
    public void setPassword(String pwd) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_PWD, pwd).commit()) {
            password = pwd;
        }
    }

    /**
     * 获取内存中好友user list
     * 
     * @return
     */
    public Map<String, ChatUser> getContactList() {
        if (getUserName() != null && contactList == null) {
            UserDao dao = new UserDao(context);
            // 获取本地好友user list到内存,方便以后获取好友list
            contactList = dao.getContactList();
        }
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     * 
     * @param contactList
     */
    public void setContactList(Map<String, ChatUser> contactList) {
        this.contactList = contactList;
    }
}
