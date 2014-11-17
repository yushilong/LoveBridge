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

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

public class ExitGroupDialog extends YARActivity
{
    private TextView text;
    private Button exitBtn;

    public void logout(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    public void cancel(View view)
    {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        finish();
        return true;
    }

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.logout_actionsheet;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        text = (TextView) findViewById(R.id.tv_text);
        exitBtn = (Button) findViewById(R.id.btn_exit);
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        text.setText(R.string.exit_group_hint);
        String toast = getIntent().getStringExtra("deleteToast");
        if (toast != null)
            text.setText(toast);
        exitBtn.setText(R.string.exit_group);
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
