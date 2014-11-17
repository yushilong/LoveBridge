package com.lovebridge.chat.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.easemob.chat.EMChatConfig;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.FileMessageBody;
import com.easemob.cloud.CloudOperationCallback;
import com.easemob.cloud.HttpFileManager;
import com.easemob.util.FileUtils;
import com.lovebridge.R;
import com.lovebridge.library.YARActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

<<<<<<< HEAD
public class ShowNormalFileActivity extends YARActivity
{
=======
public class ShowNormalFileActivity extends YARActivity {
>>>>>>> f56bc730f3884f9cc6f8667940c235ff59896ed1
    private ProgressBar progressBar;
    private File file;

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.activity_show_file;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void doInitDataes()
    {
        final FileMessageBody messageBody = getIntent().getParcelableExtra("msgbody");
        file = new File(messageBody.getLocalUrl());
        // set head map
        final Map<String, String> maps = new HashMap<String, String>();
        String accessToken = EMChatManager.getInstance().getAccessToken();
        maps.put("Authorization", "Bearer " + accessToken);
        if (!TextUtils.isEmpty(messageBody.getSecret()))
        {
            maps.put("share-secret", messageBody.getSecret());
        }
        maps.put("Accept", "application/octet-stream");
        // 下载文件
        new Thread(new Runnable()
        {
            public void run()
            {
                HttpFileManager fileManager = new HttpFileManager(ShowNormalFileActivity.this, EMChatConfig
                        .getInstance().getStorageUrl());
                fileManager.downloadFile(messageBody.getRemoteUrl(), messageBody.getLocalUrl(),
                        EMChatConfig.getInstance().APPKEY, maps, new CloudOperationCallback()
                        {
                            @Override
                            public void onSuccess(String result)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        FileUtils.openFile(file, ShowNormalFileActivity.this);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(final int progress)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        progressBar.setProgress(progress);
                                    }
                                });
                            }

                            @Override
                            public void onError(final String msg)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        if (file != null && file.exists())
                                            file.delete();
                                        Toast.makeText(ShowNormalFileActivity.this, "下载文件失败: " + msg, 0).show();
                                        finish();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
