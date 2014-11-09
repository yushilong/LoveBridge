
package com.lovebridge.index;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lovebridge.MainActivity;
import com.lovebridge.R;
import com.lovebridge.bean.User;
import com.lovebridge.library.YARBaseFragment;
import com.lovebridge.library.api.YARApiListenerImpl;
import com.lovebridge.library.api.YARImageLoader;
import com.lovebridge.library.api.YARRequest;
import com.lovebridge.library.api.YARRequestParamsFactory;
import com.lovebridge.library.api.YARResponseParser;
import com.lovebridge.library.tools.notificationcompat.AppNotification;
import com.lovebridge.library.tools.notificationcompat.NotificationCompat;

/**
 * @author yushilong
 * @date 2014-9-30 下午4:07:02
 * @version 1.0
 */
public class IndexFragment extends YARBaseFragment implements OnClickListener {
    private NotificationManager mNotifyManager;
    protected NotificationCompat.Builder mBuilder;
    private ImageView iv_photo;

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.index;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        containerView.findViewById(R.id.button).setOnClickListener(this);
        iv_photo = (ImageView)containerView.findViewById(R.id.iv_photo);
    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub
        JSONObject requestParams = YARRequestParamsFactory.createRequestParams();
        try {
            requestParams.put("email", "yuziya@b5m.com");
            requestParams.put("password", "yushilong0921");
            requestParams.put("encrypted", true);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new YARRequest("appserver.do?m=login", requestParams, new YARApiListenerImpl() {
            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                User user = YARResponseParser.parseModel(response, User.class);
            }
        }, mProgressView).start();
        YARImageLoader.newInstance().get(iv_photo,
                        "http://b.hiphotos.baidu.com/image/pic/item/eac4b74543a9822606bd2a3a8882b9014a90ebab.jpg");
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub
    }

    @Override
    public Object getNavigationTitle() {
        // TODO Auto-generated method stub
        return "INDEX";
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button:
                testNotification();
                break;
            default:
                break;
        }
    }

    private void testNotification() {
        // TODO Auto-generated method stub
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mActivity, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        final Bitmap large = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mBuilder = AppNotification.newBaseNotify(mContext, R.drawable.ic_launcher, "ok", large, resultPendingIntent);
        // mNotifyManager.notify(0, mBuilder.build());
        downloadTest();
    }

    public void downloadTest() {
        // Start a lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                // Do the "lengthy" operation 20 times
                for (incr = 0; incr <= 100; incr += 5) {
                    // Sets the progress indicator to a max value, the
                    // current completion percentage, and "determinate"
                    // state
                    mBuilder.setProgress(100, incr, false).setContentInfo(incr + "%");
                    // Displays the progress bar for the first time.
                    mNotifyManager.notify(0, mBuilder.build());
                    // Sleeps the thread, simulating an operation
                    // that takes time
                    try {
                        // Sleep for 5 seconds
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        Log.d("notify", "sleep failure");
                    }
                }
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Download complete")
                // Removes the progress bar
                                .setProgress(0, 0, false);
                mNotifyManager.notify(0, mBuilder.build());
            }
        }
        // Starts the thread by calling the run() method in its Runnable
        ).start();
    }
}
