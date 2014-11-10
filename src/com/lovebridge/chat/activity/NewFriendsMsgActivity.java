
package com.lovebridge.chat.activity;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.lovebridge.R;
import com.lovebridge.application.MainApplication;
import com.lovebridge.chat.adapter.NewFriendsMsgAdapter;
import com.lovebridge.chat.moden.InviteMessage;
import com.lovebridge.db.InviteMessgeDao;
import com.lovebridge.library.YARActivity;
import com.lovebridge.library.tools.YARConstants;

/**
 * 申请与通知
 */
public class NewFriendsMsgActivity extends YARActivity {
    private ListView listView;

    public void back(View view) {
        finish();
    }

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.activity_new_friends_msg;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        listView = (ListView)findViewById(R.id.list);

    }

    @Override
    public void doInitDataes() {
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        // 设置adapter
        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        MainApplication.getInstance().getContactList().get(YARConstants.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }

}
