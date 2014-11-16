
package com.lovebridge.chat.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import com.easemob.chat.EMMessage;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

public class ContextMenuActivity extends YARActivity {

    private int position;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void copy(View view) {
        setResult(ChatActivity.RESULT_CODE_COPY, new Intent().putExtra("position", position));
        finish();
    }

    public void delete(View view) {
        setResult(ChatActivity.RESULT_CODE_DELETE, new Intent().putExtra("position", position));
        finish();
    }

    public void forward(View view) {
        setResult(ChatActivity.RESULT_CODE_FORWARD, new Intent().putExtra("position", position));
        finish();
    }

    public void open(View v) {
        setResult(ChatActivity.RESULT_CODE_OPEN, new Intent().putExtra("position", position));
        finish();
    }

    public void download(View v) {
        setResult(ChatActivity.RESULT_CODE_DWONLOAD, new Intent().putExtra("position", position));
        finish();
    }

    public void toCloud(View v) {
        setResult(ChatActivity.RESULT_CODE_TO_CLOUD, new Intent().putExtra("position", position));
        finish();
    }

    @Override
    public int doGetContentViewId() {
        int type = getIntent().getIntExtra("type", -1), contentViewId = 0;
        if (type == EMMessage.Type.TXT.ordinal()) {
            contentViewId = R.layout.context_menu_for_text;
        } else if (type == EMMessage.Type.LOCATION.ordinal()) {
            contentViewId = (R.layout.context_menu_for_location);
        } else if (type == EMMessage.Type.IMAGE.ordinal()) {
            contentViewId = (R.layout.context_menu_for_image);
        } else if (type == EMMessage.Type.VOICE.ordinal()) {
            contentViewId = (R.layout.context_menu_for_voice);
        } else if (type == EMMessage.Type.VIDEO.ordinal()) {
            contentViewId = R.layout.context_menu_for_video;
        }
        return contentViewId;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }

}
