
package com.lovebridge.library;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lovebridge.R;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public abstract class YARBaseFragment extends Fragment {
    protected View containerView;
    public Context mContext;
    public Activity mActivity;
    protected ProgressBar mProgressView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = inflater.inflate(doGetContentViewId(), container, false);
            doBefore(savedInstanceState);
            init(containerView);
            doInitSubViews(containerView);
            doInitDataes();
            doAfter();
        }
        ViewGroup parent = (ViewGroup)containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }
        return containerView;
    }

    protected void init(View containerView) {
        // TODO Auto-generated method stub
        mActivity = getActivity();
        mContext = getActivity();
        if (mActivity instanceof YARActivity) {
            mProgressView = (ProgressBar)mActivity.findViewById(R.id.pb_navigation);
        }
    }

    public abstract int doGetContentViewId();

    public abstract void doInitSubViews(View containerView);

    public abstract void doInitDataes();

    public abstract void doAfter();

    private void doBefore(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((YARActivity)mActivity).onFragmentResume(this);
    }

}
