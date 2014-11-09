
package com.lovebridge.chat.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lovebridge.chat.fragment.ImageGridFragment;
import com.lovebridge.library.YARActivity;

public class ImageGridActivity extends YARActivity {

    private static final String TAG = "ImageGridActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }

}
