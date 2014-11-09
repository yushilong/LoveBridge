
package com.lovebridge.library.api;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

import com.lovebridge.application.MainApplication;
import com.lovebridge.library.tools.YARToast;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import com.lovebridge.library.volley.NetworkError;
import com.lovebridge.library.volley.VolleyError;

/**
 * @author yushilong
 * @date 2014-9-29 下午6:09:00
 * @version 1.0
 */
public class YARApiListenerImpl implements YARApiListener {
    private View mProgressView;
    private PullToRefreshBase<?> mPullToRefreshBase;

    public void setmProgressView(View mProgressView) {
        this.mProgressView = mProgressView;
    }

    public void setmPullToRefreshBase(PullToRefreshBase<?> mPullToRefreshBase) {
        this.mPullToRefreshBase = mPullToRefreshBase;
    }

    @Override
    public void onSuccess(JSONObject response) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess(JSONArray response) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFailure(VolleyError volleyError) {
        // TODO Auto-generated method stub
        if (volleyError == null) {
            return;
        }
        String errorStr = volleyError.getMessage();
        if (volleyError instanceof NetworkError) {
        }
        if (!TextUtils.isEmpty(errorStr)) {
            YARToast.showDefault(MainApplication.getInstance(), errorStr);
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        if (mProgressView != null) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }
        if (mPullToRefreshBase != null) {
            mPullToRefreshBase.onRefreshComplete();
        }
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        // TODO Auto-generated method stub
    }
}
