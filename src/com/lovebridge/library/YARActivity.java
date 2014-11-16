
package com.lovebridge.library;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.lovebridge.library.tools.YARAppManager;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public abstract class YARActivity extends FragmentActivity {
    public Context mContext;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
        doBefore(savedInstanceState);
        int contentViewId = doGetContentViewId();
        if (contentViewId > 0) {
            View containerView = getLayoutInflater().inflate(doGetContentViewId(), null);
            setContentView(containerView);
            doInitSubViews(containerView);
        }
        doInitDataes();
        doAfter();
        //
        // 添加Activity到堆栈
        YARAppManager.getInstance().addActivity(this);
    }

    private void init() {
        // TODO Auto-generated method stub
        mActivity = this;
        mContext = this;
    }

    public abstract int doGetContentViewId();

    public abstract void doInitSubViews(View containerView);

    public abstract void doInitDataes();

    public abstract void doAfter();

    protected void onFragmentResume(Fragment fragment) {

    }

    protected void doBefore(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        YARAppManager.getInstance().finishActivity(this);
    }
}
